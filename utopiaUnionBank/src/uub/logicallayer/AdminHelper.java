package uub.logicallayer;

import java.util.List;
import java.util.Map;

import uub.enums.UserStatus;
import uub.enums.UserType;
import uub.model.Employee;
import uub.model.User;
import uub.staticlayer.CustomBankException;
import uub.staticlayer.EmployeeUtils;
import uub.staticlayer.HashEncoder;
import uub.staticlayer.HelperUtils;

public class AdminHelper extends EmployeeHelper{

	public AdminHelper() throws CustomBankException {
		super();
	}
	

	public Map<Integer, Map<Integer,Employee>> getEmployees(int branchId,int limit, int offSet) throws CustomBankException {

		return employeeDao.getEmployeesWithBranch(branchId,limit,offSet);
	}

	public List<User> getActiveEmployees(int limit, int offSet) throws CustomBankException {

		return userDao.getAllUsers(UserType.EMPLOYEE, UserStatus.ACTIVE,limit,offSet);
	}

	public List<User> getInactiveEmployees(int limit, int offSet) throws CustomBankException {

		return userDao.getAllUsers(UserType.EMPLOYEE, UserStatus.INACTIVE,limit,offSet);

	}

	public void activateUser(int userId) throws CustomBankException {

		User user = new User();

		user.setId(userId);
		user.setStatus(UserStatus.ACTIVE);
		userDao.updateUser(user);

	}

	public void deActivateUser(int userId) throws CustomBankException {

		User user = new User();

		user.setId(userId);
		user.setStatus(UserStatus.INACTIVE);
		userDao.updateUser(user);

	}
	

	public void addEmployee(Employee employee) throws CustomBankException {

		HelperUtils.nullCheck(employee);
		employee.setStatus(UserStatus.ACTIVE);

		try {
			if (EmployeeUtils.validatePhone(employee.getPhone()) && EmployeeUtils.validateEmail(employee.getEmail())
					&& EmployeeUtils.validatePass(employee.getPassword())) {

				String password = employee.getPassword();
				employee.setPassword(HashEncoder.encode(password));

				employeeDao.addEmployee(List.of(employee));
			}
		} catch (Exception e) {
			throw new CustomBankException("Signup failed ! " + e.getMessage());

		}

	}

}
