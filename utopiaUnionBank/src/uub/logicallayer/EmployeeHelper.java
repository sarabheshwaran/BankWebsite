package uub.logicallayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import uub.enums.AccountStatus;
import uub.enums.UserStatus;
import uub.enums.UserType;
import uub.model.Account;
import uub.model.Customer;
import uub.model.Employee;
import uub.model.User;
import uub.persistentinterfaces.IAccountDao;
import uub.persistentinterfaces.ICustomerDao;
import uub.persistentinterfaces.IEmployeeDao;
import uub.persistentinterfaces.IUserDao;
import uub.staticlayer.CustomBankException;
import uub.staticlayer.EmployeeUtils;
import uub.staticlayer.HashEncoder;
import uub.staticlayer.HelperUtils;

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


	public Map<Integer, Map<Integer,Account>> getActiveAccounts(int branchId,int limit, int offSet) throws CustomBankException {

		return accountDao.getBranchAccounts(branchId, AccountStatus.ACTIVE, limit,offSet);
	}

	public Map<Integer, Map<Integer,Account>> getInactiveAccounts(int branchId,int limit, int offSet) throws CustomBankException {

		return accountDao.getBranchAccounts(branchId, AccountStatus.INACTIVE,limit,offSet);
	}

	public List<User> getActiveCustomers(int limit, int offSet) throws CustomBankException {

		return userDao.getAllUsers(UserType.CUSTOMER,UserStatus.ACTIVE,limit,offSet);
	}

	public List<User> getInactiveCustomers(int limit, int offSet) throws CustomBankException {

		return userDao.getAllUsers(UserType.CUSTOMER, UserStatus.INACTIVE,limit,offSet);
	}



	public void activateAcc(int accNo) throws CustomBankException {

		Account account = new Account();

		account.setAccNo(accNo);
		account.setStatus(AccountStatus.ACTIVE);
		int result = accountDao.updateAccount(account);

		if (result == 0) {
			throw new CustomBankException("Account not found");
		}
	}

	public void deActivateAcc(int accNo) throws CustomBankException {

		Account account = new Account();

		account.setAccNo(accNo);
		account.setStatus(AccountStatus.INACTIVE);
		int result = accountDao.updateAccount(account);

		if (result == 0) {
			throw new CustomBankException("Account not found");
		}

	}

	public void addCustomer(Customer customer) throws CustomBankException {

		HelperUtils.nullCheck(customer);
		
		customer.setStatus(UserStatus.ACTIVE);

		try {
			if (EmployeeUtils.validatePhone(customer.getPhone()) && EmployeeUtils.validateEmail(customer.getEmail())
					&& EmployeeUtils.validatePass(customer.getPassword())) {

				String password = customer.getPassword();
				customer.setPassword(HashEncoder.encode(password));

				customerDao.addCustomer(List.of(customer));
			}
		} catch (Exception e) {
			throw new CustomBankException("Signup failed ! " + e.getMessage());

		}

	}




}
