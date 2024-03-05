package uub.logicalLayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import uub.model.Account;
import uub.model.Customer;
import uub.persistentLayer.IAccountDao;
import uub.persistentLayer.ICustomerDao;
import uub.staticLayer.CustomBankException;

public class CustomerHelper {

	private IAccountDao accountDao ;
	private ICustomerDao customerDao;

	public CustomerHelper() throws CustomBankException {
		try {
			Class<?> AccountDao = Class.forName("uub.persistentLayer.AccountDao");
			Constructor<?> accDao = AccountDao.getDeclaredConstructor();

			Class<?> CustomerDao = Class.forName("uub.persistentLayer.CustomerDao");
			Constructor<?> cusDao = CustomerDao.getDeclaredConstructor();

			accountDao = (IAccountDao) accDao.newInstance();
			customerDao = (ICustomerDao) cusDao.newInstance();

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


}
