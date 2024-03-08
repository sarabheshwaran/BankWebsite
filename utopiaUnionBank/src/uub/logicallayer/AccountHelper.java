package uub.logicallayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import uub.enums.AccountStatus;
import uub.model.Account;
import uub.model.User;
import uub.persistentinterfaces.IAccountDao;
import uub.staticlayer.CustomBankException;
import uub.staticlayer.HelperUtils;

public class AccountHelper {

	private IAccountDao accountDao;

	public AccountHelper() throws CustomBankException {

		try {
			Class<?> AccountDao = Class.forName("uub.persistentlayer.AccountDao");
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

	public User getUser(int accNo) throws CustomBankException {

		UserHelper userHelper = new UserHelper();

		User user = userHelper.getUser(getUserId(accNo));

		return user;

	}

	public Account getAccount(int accNo) throws CustomBankException {

		List<Account> accounts = accountDao.getAccount(accNo);

		if (!accounts.isEmpty()) {

			return accounts.get(0);

		} else {
			throw new CustomBankException("Account Not Found");
		}

	}

	public void validateAccount(int accNo) throws CustomBankException {

		Account account = getAccount(accNo);
		if (account.getStatus()==AccountStatus.INACTIVE) {
			throw new CustomBankException("Account Invalid !");
		}
	}

	public void accountAuth(int accNo, String password) throws CustomBankException {

		HelperUtils.nullCheck(password);

		LoginHelper loginHelper = new LoginHelper();
		loginHelper.passwordValidate(getUser(accNo), password);

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

	public void updateBalance(int accNo, double balance) throws CustomBankException {

		Account account = new Account();

		if (balance >= 0) {
			account.setAccNo(accNo);
			account.setBalance(balance);
			accountDao.updateAccount(account);

		} else {

			throw new CustomBankException("Insufficient Balance");
		}

	}

	public void addAccount(Account account) throws CustomBankException {

		CustomerHelper customerHelper = new CustomerHelper();

		customerHelper.getCustomer(account.getUserId());

		account.setBalance(0);

		account.setStatus(AccountStatus.INACTIVE);

		accountDao.addAccounts(List.of(account));

	}

}
