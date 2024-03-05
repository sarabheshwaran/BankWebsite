package uub.logicalLayer;

import java.util.List;
import java.util.Map;

import uub.model.Account;
import uub.model.Employee;
import uub.model.User;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HashEncoder;
import uub.staticLayer.HelperUtils;

public class AdminHelper extends EmployeeHelper{

	public AdminHelper() throws CustomBankException {
		super();
	}
	

	public Map<Integer, List<Employee>> getEmployees(int branchId) throws CustomBankException {

		return employeeDao.getEmployeesWithBranch(branchId);
	}

	public List<User> getActiveEmployees() throws CustomBankException {

		return userDao.getAllUsers("Employee", "ACTIVE");
	}

	public List<User> getInactiveEmployees() throws CustomBankException {

		return userDao.getAllUsers("Employee", "ACTIVE");

	}

	public void activateUser(int userId) throws CustomBankException {

		User user = new User();

		user.setId(userId);
		user.setStatus("ACTIVE");
		userDao.updateUser(user);

	}

	public void deActivateUser(int accNo) throws CustomBankException {

		Account account = new Account();

		account.setAccNo(accNo);
		account.setStatus("INACTIVE");
		accountDao.updateAccount(account);

	}
	

	public void addEmployee(Employee employee) throws CustomBankException {

		HelperUtils.nullCheck(employee);
		employee.setUserType("Employee");
		employee.setStatus("ACTIVE");
		Validator validator = new Validator();

		try {
			if (validator.validatePhone(employee.getPhone()) && validator.validateEmail(employee.getEmail())
					&& validator.validatePass(employee.getPassword())) {

				String password = employee.getPassword();
				employee.setPassword(HashEncoder.encode(password));

				employeeDao.addEmployee(List.of(employee));
			}
		} catch (Exception e) {
			throw new CustomBankException("Signup failed : " + e.getMessage());

		}

	}

}
