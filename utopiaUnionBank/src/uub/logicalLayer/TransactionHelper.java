package uub.logicalLayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;

import uub.enums.SelfTransferType;
import uub.model.Transaction;
import uub.persistentinterfaces.ITransactionDao;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.DateUtils;
import uub.staticLayer.HelperUtils;

public class TransactionHelper {
	private ITransactionDao transactionDao;

	public TransactionHelper() throws CustomBankException {

		try {

			Class<?> TransactionDao = Class.forName("uub.persistentlayer.TransactionDao");
			Constructor<?> transDao = TransactionDao.getDeclaredConstructor();

			transactionDao = (ITransactionDao) transDao.newInstance();

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			throw new CustomBankException("Error getting Data ! ", e);
		}

	}

	private int getId() throws CustomBankException {
		int id = transactionDao.getLastId();
		return id + 1;
	}

	public void selfTransfer(Transaction transaction, String password, SelfTransferType type)
			throws CustomBankException {

		

		HelperUtils.nullCheck(transaction);
		HelperUtils.nullCheck(password);
		

		validateTransaction(transaction);

		AccountHelper accountHelper = new AccountHelper();
		
		try {

			accountHelper.accountAuth(transaction.getAccNo(), password);

			int id = getId();
			transaction.setId(id);

			if (type == SelfTransferType.WITHDRAW) {

				transaction.setAmount(0 - transaction.getAmount());

			}

			setTransaction(transaction);

			transaction.setStatus("PASS");

		} catch (CustomBankException e) {
			transaction.setStatus("FAIL");
			transaction.setClosingBal(transaction.getOpeningBal());
			throw new CustomBankException("Transaction Failed ! ", e);
		} finally {
			transactionDao.addTransaction(transaction);
		}

	}

	public void outBankTransfer(Transaction transaction, String password) throws CustomBankException {

	

		HelperUtils.nullCheck(password);
		HelperUtils.nullCheck(transaction);

		
		validateTransaction(transaction);
		
		AccountHelper accountHelper = new AccountHelper();

		try {
			accountHelper.accountAuth(transaction.getAccNo(), password);

			int id = getId();
			transaction.setId(id);
			transaction.setAmount(0 - transaction.getAmount());
			transaction.setStatus("PASS");

			setTransaction(transaction);

		} catch (CustomBankException e) {
			transaction.setStatus("FAIL");
			transaction.setClosingBal(transaction.getOpeningBal());
			throw new CustomBankException("Transaction Failed ! ", e);
		} finally {
			transactionDao.addTransaction(transaction);
		}

	}

	public void inBankTransfer(Transaction transaction, String password) throws CustomBankException {


		HelperUtils.nullCheck(password);
		HelperUtils.nullCheck(transaction);

		validateTransaction(transaction);
		

		AccountHelper accountHelper = new AccountHelper();

		try {
			accountHelper.accountAuth(transaction.getAccNo(), password);
			
			int id = getId();
			transaction.setId(id);
			transaction.setAmount(0 - transaction.getAmount());
			transaction.setStatus("PASS");

			Transaction rTransaction = generateReceiverTransaction(transaction);

			setTransaction(transaction);
			setTransaction(rTransaction);
			
			transactionDao.addTransaction(rTransaction);

		} catch (CustomBankException e) {
			transaction.setStatus("FAIL");
			transaction.setClosingBal(transaction.getOpeningBal());
			throw new CustomBankException("Transaction Failed ! ", e);
		} finally {

			transactionDao.addTransaction(transaction);

		}

	}

	private void setTransaction(Transaction transaction) throws CustomBankException {

		AccountHelper accountHelper = new AccountHelper();

		transaction.setTime(System.currentTimeMillis());

		int accNo = transaction.getAccNo();
		accountHelper.validateAccount(accNo);
		
		double balance = accountHelper.getBalance(accNo);
		double closingBalance = balance + transaction.getAmount();
		
		
		transaction.setOpeningBal(balance);
		transaction.setClosingBal(closingBalance);

		accountHelper.updateBalance(accNo, closingBalance);


	}

	private void validateTransaction(Transaction transaction) throws CustomBankException {

		if (transaction.getAccNo() == transaction.getTransactionAcc()) {
			throw new CustomBankException("Invalid receiver account no.");
		}
		if (transaction.getAmount() <= 0) {
			throw new CustomBankException("Invalid amount");

		}
	}

	private Transaction generateReceiverTransaction(Transaction transaction) throws CustomBankException {

		Transaction rTransaction = new Transaction(transaction);

		int accNo = transaction.getTransactionAcc();

		rTransaction.setAccNo(accNo);
		rTransaction.setTransactionAcc(transaction.getAccNo());
		rTransaction.setAmount(0 - transaction.getAmount());

		AccountHelper accountHelper = new AccountHelper();
		int userId = accountHelper.getUserId(accNo);

		rTransaction.setUserId(userId);

		return rTransaction;

	}

	public List<Transaction> getNDaysTransaction(int accNo, int days, int limit, int page) throws CustomBankException {

		long todayMillis = System.currentTimeMillis();

		long ansMillis = todayMillis - 86400000 * (days);

		return getTransactions(accNo, ansMillis, todayMillis, limit, (page - 1) * limit);

	}

	public List<Transaction> getTransaction(int accNo, String from, String to, int limit, int page) throws CustomBankException {

		HelperUtils.nullCheck(from);
		HelperUtils.nullCheck(to);

		LocalDate fromDate = LocalDate.parse(from);
		LocalDate toDate = LocalDate.parse(to);

		toDate = toDate.plusDays(1);

		long fromMillis = DateUtils.formatDate(fromDate);
		long toMillis = DateUtils.formatDate(toDate);

		return getTransactions(accNo, fromMillis, toMillis, limit,  (page - 1) * limit);
	}

	public List<Transaction> getTransactions(int accNo, long from, long to, int limit, int offSet)
			throws CustomBankException {

		return transactionDao.getTransactions(accNo, from, to, limit, offSet);

	}

}
