package uub.logicallayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;

import uub.enums.TransactionStatus;
import uub.enums.TransferType;
import uub.model.Transaction;
import uub.persistentinterfaces.ITransactionDao;
import uub.staticlayer.CustomBankException;
import uub.staticlayer.DateUtils;
import uub.staticlayer.HelperUtils;
import uub.staticlayer.TransactionUtils;

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


	public void makeTransaction(Transaction transaction, String password) throws CustomBankException {

		HelperUtils.nullCheck(transaction);
		HelperUtils.nullCheck(password);

		validateTransaction(transaction);

		AccountHelper accountHelper = new AccountHelper();
		try {

			accountHelper.accountAuth(transaction.getAccNo(), password);
			String id = TransactionUtils.generateUniqueId(transaction.getAccNo(), transaction.getTransactionAcc());
			transaction.setId(id);

			transaction.setStatus(TransactionStatus.SUCCESS);

			TransferType type = transaction.getType();

			switch (type) {

			case WITHDRAW:
			case DEPOSIT: {
				selfTransfer(transaction, type);
				break;
			}
			case INTER_BANK: {
				outBankTransfer(transaction);
				break;
			}
			case INTRA_BANK: {
				inBankTransfer(transaction);
				break;
			}
			}

		} catch (CustomBankException e) {
			throw new CustomBankException("Transaction Failed ! ", e);
		}

	}

	private void selfTransfer(Transaction transaction, TransferType type) throws CustomBankException {

		if (type == TransferType.WITHDRAW) {

			transaction.setAmount(0 - transaction.getAmount());

		}

		setTransaction(transaction);

		transactionDao.makeTransaction(List.of(transaction));

	}

	private void outBankTransfer(Transaction transaction) throws CustomBankException {

		transaction.setAmount(0 - transaction.getAmount());

		setTransaction(transaction);

		transactionDao.makeTransaction(List.of(transaction));

	}

	private void inBankTransfer(Transaction transaction) throws CustomBankException {

		transaction.setAmount(0 - transaction.getAmount());

		Transaction rTransaction = generateReceiverTransaction(transaction);

		setTransaction(transaction);
		setTransaction(rTransaction);

		transactionDao.makeTransaction(List.of(transaction, rTransaction));

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

		Transaction rTransaction = new Transaction();

		rTransaction.setId(transaction.getId());
		rTransaction.setTime(transaction.getTime());
		rTransaction.setDesc(transaction.getDesc());
		rTransaction.setType(transaction.getType());
		rTransaction.setStatus(transaction.getStatus());

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

		return transactionDao.getTransactions(accNo, ansMillis, todayMillis, limit, (page - 1) * limit);

	}

	public List<Transaction> getTransaction(int accNo, String from, String to, int limit, int page)
			throws CustomBankException {

		HelperUtils.nullCheck(from);
		HelperUtils.nullCheck(to);

		LocalDate fromDate = LocalDate.parse(from);
		LocalDate toDate = LocalDate.parse(to);

		toDate = toDate.plusDays(1);

		long fromMillis = DateUtils.formatDate(fromDate);
		long toMillis = DateUtils.formatDate(toDate);

		return transactionDao.getTransactions(accNo, fromMillis, toMillis, limit, (page - 1) * limit);
	}

}
