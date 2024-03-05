package uub.logicalLayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import uub.model.Account;
import uub.model.Transaction;
import uub.persistentLayer.ITransactionDao;
import uub.staticLayer.CustomBankException;

public class TransactionHelper {
	private ITransactionDao transactionDao;

	public TransactionHelper() throws CustomBankException {

		try {

			Class<?> TransactionDao = Class.forName("uub.persistentLayer.TransactionDao");
			Constructor<?> transDao = TransactionDao.getDeclaredConstructor();

			transactionDao = (ITransactionDao) transDao.newInstance();

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			throw new CustomBankException("Error getting Data ! ", e);
		}

	}
	
	

	public void performTransaction(Transaction transaction) throws CustomBankException {

		AccountHelper accountHelper = new AccountHelper();

		int id = transactionDao.getLastId();

		transaction.setId(id + 1);

		transaction.setTime(System.currentTimeMillis());

		int accNo = transaction.getAccNo();
		int rAccNo = transaction.getTransactionAcc();

		transaction.setOpeningBal(accountHelper.getBalance(accNo));
		transaction.setClosingBal(accountHelper.getBalance(accNo) + transaction.getAmount());

		Account account = null;
		Transaction rTransaction = null;

		try {
			account = accountHelper.debit(accNo, transaction.getAmount());

			if (rAccNo != 0) {
				rTransaction = performRecepientTransaction(transaction);

			}

			accountHelper.updateAccount(account);

			transaction.setStatus("PASS");
		} catch (CustomBankException e) {

			transaction.setStatus("FAIL");
			transaction.setClosingBal(transaction.getOpeningBal());
			throw new CustomBankException("Transaction Failed ! ", e);

		} finally {

			transactionDao.addTransaction(transaction);
			if (rTransaction != null) {
				transactionDao.addTransaction(rTransaction);
			}

		}

	}

	private Transaction performRecepientTransaction(Transaction transaction) throws CustomBankException {

		AccountHelper accountHelper = new AccountHelper();

		Transaction rTransaction = new Transaction();

		int accNo = transaction.getTransactionAcc();
		int userId = accountHelper.getUserId(accNo);
		double balance = accountHelper.getBalance(accNo);
		double amount = transaction.getAmount();
		int rAccNo = transaction.getAccNo();

		Account account = accountHelper.credit(accNo, amount);
		accountHelper.updateAccount(account);

		if (rAccNo == accNo) {

			throw new CustomBankException("Invalid Receiver !");
		}

		rTransaction.setId(transaction.getId());
		rTransaction.setUserId(userId);
		rTransaction.setTransactionAcc(rTransaction.getAccNo());
		rTransaction.setAccNo(accNo);
		rTransaction.setTransactionAcc(rAccNo);
		rTransaction.setType(transaction.getType());
		rTransaction.setAmount(0 - amount);
		rTransaction.setTime(System.currentTimeMillis());
		rTransaction.setStatus("PASS");

		rTransaction.setOpeningBal(balance);
		rTransaction.setClosingBal(balance + rTransaction.getAmount());

		rTransaction.setDesc(transaction.getDesc());


		return rTransaction;

	}

	public List<Transaction> getTransactions(int accNo, long from, int limit, int offSet) throws CustomBankException {

		return transactionDao.getTransactions(accNo, from, limit, offSet);

	}

}
