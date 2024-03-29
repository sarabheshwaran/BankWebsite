package uub.logicalLayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import uub.model.Account;
import uub.model.Customer;
import uub.model.Employee;
import uub.model.User;
import uub.persistentinterfaces.IAccountDao;
import uub.persistentinterfaces.ICustomerDao;
import uub.persistentinterfaces.IEmployeeDao;
import uub.persistentinterfaces.IUserDao;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HashEncoder;
import uub.staticLayer.HelperUtils;

public class EmployeeHelper {

	protected IEmployeeDao employeeDao;
	protected IAccountDao accountDao;
	protected IUserDao userDao;
	protected ICustomerDao customerDao;

	public EmployeeHelper() throws CustomBankException {

		try {
			Class<?> AccountDao = Class.forName("uub.persistentlayer.AccountDao");
			Constructor<?> accDao = AccountDao.getDeclaredConstructor();

			Class<?> EmployeeDao = Class.forName("uub.persistentlayer.EmployeeDao");
			Constructor<?> empDao = EmployeeDao.getDeclaredConstructor();

			Class<?> UserDao = Class.forName("uub.persistentlayer.UserDao");
			Constructor<?> useDao = UserDao.getDeclaredConstructor();

			Class<?> CustomerDao = Class.forName("uub.persistentlayer.CustomerDao");
			Constructor<?> cusDao = CustomerDao.getDeclaredConstructor();

			accountDao = (IAccountDao) accDao.newInstance();
			employeeDao = (IEmployeeDao) empDao.newInstance();
			userDao = (IUserDao) useDao.newInstance();
			customerDao = (ICustomerDao) cusDao.newInstance();

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			throw new CustomBankException("Error getting Data ! ", e);
		}

	}



	public Employee getEmployee(int id) throws CustomBankException {
		

		List<Employee> employees = employeeDao.getEmployees(id);

		if (!employees.isEmpty()) {
			return employees.get(0);
		} else {
			throw new CustomBankException("Employee not found !");
		}
	}


	public Map<Integer, List<Account>> getActiveAccounts(int branchId,int limit, int offSet) throws CustomBankException {

		return accountDao.getBranchAccounts(branchId, "ACTIVE", limit,offSet);
	}

	public Map<Integer, List<Account>> getInactiveAccounts(int branchId,int limit, int offSet) throws CustomBankException {

		return accountDao.getBranchAccounts(branchId, "INACTIVE",limit,offSet);
	}

	public List<User> getActiveCustomers(int limit, int offSet) throws CustomBankException {

		return userDao.getAllUsers("Customer", "ACTIVE",limit,offSet);
	}

	public List<User> getInactiveCustomers(int limit, int offSet) throws CustomBankException {

		return userDao.getAllUsers("Customer", "INACTIVE",limit,offSet);
	}



	public void activateAcc(int accNo) throws CustomBankException {

		Account account = new Account();

		account.setAccNo(accNo);
		account.setStatus("ACTIVE");
		int result = accountDao.updateAccount(account);

		if (result == 0) {
			throw new CustomBankException("Account not found");
		}
	}

	public void deActivateAcc(int accNo) throws CustomBankException {

		Account account = new Account();

		account.setAccNo(accNo);
		account.setStatus("INACTIVE");
		int result = accountDao.updateAccount(account);

		if (result == 0) {
			throw new CustomBankException("Account not found");
		}

	}

	public int addCustomer(Customer customer) throws CustomBankException {

		HelperUtils.nullCheck(customer);
		
		customer.setUserType("Customer");
		customer.setStatus("ACTIVE");
		Validator validator = new Validator();

		try {
			if (validator.validatePhone(customer.getPhone()) && validator.validateEmail(customer.getEmail())
					&& validator.validatePass(customer.getPassword())) {

				String password = customer.getPassword();
				customer.setPassword(HashEncoder.encode(password));

				return customerDao.addCustomer(List.of(customer))[0];
			}
		} catch (Exception e) {
			throw new CustomBankException("Signup failed : " + e.getMessage());

		}
		return 0;

	}




}
