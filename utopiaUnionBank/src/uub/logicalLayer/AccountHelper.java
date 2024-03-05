package uub.logicalLayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import uub.model.Account;
import uub.model.Transaction;
import uub.model.User;
import uub.persistentLayer.IAccountDao;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HelperUtils;

public class AccountHelper {

	private IAccountDao accountDao;

	public AccountHelper() throws CustomBankException {

		try {
			Class<?> AccountDao = Class.forName("uub.persistentLayer.AccountDao");
			Constructor<?> accDao = AccountDao.getDeclaredConstructor();

			accountDao = (IAccountDao) accDao.newInstance();

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new CustomBankException("Error getting Data ! ", e);
		}

	}

	public int getUserId(int accNo) throws CustomBankException {
		Account account = getAccount(accNo);

		return account.getUserId();
	}

	public Account getAccount(int accNo) throws CustomBankException {

		List<Account> accounts = accountDao.getAccount(accNo);

		if (!accounts.isEmpty()) {

			return accounts.get(0);

		} else {
			throw new CustomBankException("Account Not Found");
		}

	}
	
	public void validateAccount(int userId, String password)throws CustomBankException{
		
		UserHelper userHelper = new UserHelper();
		
		User user = userHelper.getUser(userId);
		
		LoginHelper loginHelper = new LoginHelper();
		
		loginHelper.passwordValidate(password, user.getPassword());
		
	}

	public double getBalance(int accNo) throws CustomBankException {

		double balance = getAccount(accNo).getBalance();

		return balance;

	}

	public void updateAccount(Account account) throws CustomBankException {

		HelperUtils.nullCheck(account);

		int result = accountDao.updateAccount(account);

		if (result == 0) {
			throw new CustomBankException("Account not found !");
		}

	}

	public Account credit(int accNo, double amount) throws CustomBankException {

		return updateBalance(accNo, amount);

	}

	public Account debit(int accNo, double amount) throws CustomBankException {

		return updateBalance(accNo, 0-amount);

	}

	private Account updateBalance(int accNo, double amount) throws CustomBankException {

		Account account = getAccount(accNo);

		double balance = account.getBalance();

		if (balance >= amount) {

			account.setBalance(balance - amount);

			return account;

		} else {

			throw new CustomBankException("Insufficient Balance");
		}

	}

	public void addAccount(Account account) throws CustomBankException {

		CustomerHelper customerHelper = new CustomerHelper();

		customerHelper.getCustomer(account.getUserId());

		account.setBalance(0);

		account.setStatus("ACTIVE");

		accountDao.addAccounts(List.of(account));

	}

	

	public List<Transaction> getNDaysTransaction(int accNo, int days,int limit, int page) throws CustomBankException {

		TransactionHelper transactionHelper = new TransactionHelper();
		long todayMillis = System.currentTimeMillis();

		long ansMillis = todayMillis - 86400000 * (days);

		return transactionHelper.getTransactions(accNo, ansMillis, limit ,(page-1)*50);

	}
}
