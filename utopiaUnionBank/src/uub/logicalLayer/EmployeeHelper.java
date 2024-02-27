package uub.logicalLayer;

import java.util.List;
import java.util.regex.Pattern;


import uub.model.Account;
import uub.model.Customer;
import uub.model.Employee;
import uub.model.User;
import uub.persistentLayer.AccountDao;
import uub.persistentLayer.CustomerDao;
import uub.persistentLayer.EmployeeDao;
import uub.persistentLayer.IAccountDao;
import uub.persistentLayer.ICustomerDao;
import uub.persistentLayer.IEmployeeDao;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HashEncoder;
import uub.staticLayer.HelperUtils;

public class EmployeeHelper {

	public Employee getProfile(String email) throws CustomBankException {

		IEmployeeDao employeeDao = new EmployeeDao();

		return employeeDao.getEmployeesWithEmail(email).get(0);

	}
	
	public List<Employee> getEmployees (int branchId) throws CustomBankException{
		
		IEmployeeDao employeeDao = new EmployeeDao();
		
		return employeeDao.getEmployeesWithBranch(branchId);
		
	}

	public List<User> getActiveAccounts(int branchId) throws CustomBankException {

		IAccountDao accountDao = new AccountDao();

		return accountDao.getBranchAccounts(branchId, true);
	}

	public List<User> getInactiveAccounts(int branchId) throws CustomBankException {

		IAccountDao accountDao = new AccountDao();

		return accountDao.getBranchAccounts(branchId, false);
	}

	public void deActivateAcc(int accNo) throws CustomBankException {

		IAccountDao accountDao = new AccountDao();

		Account account = new Account();

		account.setAccNo(accNo);
		account.setStatus("INACTIVE");
		accountDao.updateAccount(account);

	}
	
	public void deActivateUser(int accNo) throws CustomBankException {

		IAccountDao accountDao = new AccountDao();

		Account account = new Account();

		account.setAccNo(accNo);
		account.setStatus("INACTIVE");
		accountDao.updateAccount(account);

	}

	public void activateAcc(int accNo) throws CustomBankException {

		IAccountDao accountDao = new AccountDao();

		Account account = new Account();

		account.setAccNo(accNo);

		account.setStatus("ACTIVE");

		accountDao.updateAccount(account);

	}

	public int addCustomer(Customer customer) throws CustomBankException {

		ICustomerDao customerDao = new CustomerDao();
		customer.setUserType("Customer");
		customer.setStatus("ACTIVE");

		try {
		if (validatePhone(customer.getPhone()) && validateEmail(customer.getEmail()) && validatePass(customer.getPassword())) {
			
			
			String password = customer.getPassword();
			
			customer.setPassword(HashEncoder.encode(password));

			return customerDao.addCustomer(List.of(customer))[0];}
		}
		catch(Exception e) {
			throw new CustomBankException("Signup failed : "+e.getMessage());
			
		}
		return 0;

	}

	public void addEmployee(Employee employee) throws CustomBankException {

		IEmployeeDao employeeDao = new EmployeeDao();
		
		employee.setUserType("Employee");
		employee.setStatus("ACTIVE");

		try {
		if (validatePhone(employee.getPhone()) && validateEmail(employee.getEmail()) && validatePass(employee.getPassword())) {
			
			String password = employee.getPassword();
			
			employee.setPassword(HashEncoder.encode(password));

			employeeDao.addEmployee(List.of(employee));}
		}
		catch(Exception e) {
			throw new CustomBankException("Signup failed : "+e.getMessage());
			
		}

	}

	
	private boolean validateEmail(String email) throws CustomBankException {

		HelperUtils.nullCheck(email);
		boolean ans = Pattern.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,5}$", email);
		
		if(ans) {
			return ans;
		}else {
			throw new CustomBankException("Email invalid !");
		}
	}

	private boolean validatePhone(String phoneNo) throws CustomBankException {

		HelperUtils.nullCheck(phoneNo);
		boolean ans = Pattern.matches("[789]{1}\\d{9}", phoneNo);
		
		if(ans) {
			return ans;
		}else {
			throw new CustomBankException("Email invalid !");
		}

	}

	private boolean validatePass(String password) throws CustomBankException {

		HelperUtils.nullCheck(password);

		String regex = "^(?=.*[A-Z])" 
				+ "(?=.*[a-z])" 
				+ "(?=.*\\d)"
				+ "(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\\\"\\\\|,.<>/?])" 
				+ "(?=.*[0-9])" 
				+ ".{8,}$";

		boolean ans = Pattern.matches(regex, password);
		
		if(ans) {
			return ans;
		}else {
			throw new CustomBankException("Email invalid !");
		}
	}

}
