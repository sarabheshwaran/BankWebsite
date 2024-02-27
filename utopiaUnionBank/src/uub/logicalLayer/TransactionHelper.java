package uub.logicalLayer;

import uub.model.Account;
import uub.model.Transaction;
import uub.persistentLayer.ITransactionDao;
import uub.persistentLayer.TransactionDao;
import uub.staticLayer.CustomBankException;

public class TransactionHelper {

	public void performTransaction(Transaction transaction) throws CustomBankException {

		ITransactionDao transactionDao = new TransactionDao();
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

		ITransactionDao transactionDao = new TransactionDao();
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

}
