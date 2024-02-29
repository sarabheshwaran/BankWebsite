package uub.logicalLayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import uub.model.Account;
import uub.model.Customer;
import uub.model.Transaction;
import uub.model.User;
import uub.persistentLayer.IAccountDao;
import uub.persistentLayer.ICustomerDao;
import uub.persistentLayer.ITransactionDao;
import uub.staticLayer.CustomBankException;

public class CustomerHelper {

	private IAccountDao accountDao ;
	private ICustomerDao customerDao;
	private ITransactionDao transactionDao;

	public CustomerHelper() throws CustomBankException {
		try {
			Class<?> AccountDao = Class.forName("uub.persistentLayer.AccountDao");
			Constructor<?> accDao = AccountDao.getDeclaredConstructor();

			Class<?> CustomerDao = Class.forName("uub.persistentLayer.CustomerDao");
			Constructor<?> cusDao = CustomerDao.getDeclaredConstructor();

			Class<?> TransactionDao = Class.forName("uub.persistentLayer.TransactionDao");
			Constructor<?> transDao = TransactionDao.getDeclaredConstructor();

			accountDao = (IAccountDao) accDao.newInstance();
			customerDao = (ICustomerDao) cusDao.newInstance();
			transactionDao = (ITransactionDao) transDao.newInstance();

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			
			throw new CustomBankException("Error getting Data ! ", e);
		}
	}

	public Customer getProfile(String username) throws CustomBankException {

		List<Customer> customers = customerDao.getCustomersWithEmail(username);

		if (!customers.isEmpty()) {
			return customers.get(0);
		} else {
			throw new CustomBankException("Customer not found!");
		}

	}

	public Customer getCustomer(int id) throws CustomBankException {

		List<Customer> customers = customerDao.getCustomers(id);

		if (!customers.isEmpty()) {
			return customers.get(0);
		} else {
			throw new CustomBankException("Customer not found!");
		}

	}

	public List<Account> getAccounts(int customerId) throws CustomBankException {

		return accountDao.getUserAccounts(customerId, "ACTIVE");

		

	}

	public List<Transaction> getHistory(int id) throws CustomBankException {

		Transaction transaction = new Transaction();
		transaction.setAccNo(id);

		return transactionDao.getTransactions(transaction);
	}

}
