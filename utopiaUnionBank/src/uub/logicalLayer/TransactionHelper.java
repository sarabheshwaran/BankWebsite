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

		transaction.setTime(System.currentTimeMillis());

		int accountNo = transaction.getAccNo();

		transaction.setOpeningBal(accountHelper.getBalance(accountNo));
		transaction.setClosingBal(accountHelper.getBalance(accountNo) + transaction.getAmount());

		Account account;

		try {
			account = accountHelper.updateBalance(accountNo, 0 - transaction.getAmount());
			accountHelper.updateAccount(account);

			transaction.setStatus("PASS");
		} catch (Exception e) {

			transaction.setStatus("FAIL");
			throw new CustomBankException("Transaction Failed ! Cause : " + e.getMessage());

		} finally {
			try {
				transactionDao.addTransaction(transaction);
				if (accountHelper.getUserId(transaction.getTransactionAcc()) != 0) {
					performRecepientTransaction(transaction);
				}
			} catch (Exception e) {

				throw new CustomBankException("Transaction Failed ! Cause : " + e.getMessage());
			}

		}

	}

	private void performRecepientTransaction(Transaction transaction) throws CustomBankException {

		AccountHelper accountHelper = new AccountHelper();

		int accNo = transaction.getTransactionAcc();
		int userId = accountHelper.getUserId(transaction.getTransactionAcc());
		double balance = accountHelper.getBalance(accNo);

		transaction.setUserId(userId);
		transaction.setTransactionAcc(transaction.getAccNo());
		transaction.setAccNo(accNo);
		transaction.setAmount(0 - transaction.getAmount());
		transaction.setTime(System.currentTimeMillis());
		transaction.setStatus("PASS");

		transaction.setOpeningBal(balance);
		transaction.setClosingBal(balance + transaction.getAmount());

		Account account = accountHelper.updateBalance(accNo, 0 - transaction.getAmount());
		accountHelper.updateAccount(account);
		transactionDao.addTransaction(transaction);

	}
	
	public List<Transaction> getNDaysTransaction(int accNo, int days) throws CustomBankException {
		
		long todayMillis = System.currentTimeMillis();
		
		long ansMillis = todayMillis - 86400000*(days);
		
		return transactionDao.getTransactions(accNo, ansMillis);
		
		
	}

}
