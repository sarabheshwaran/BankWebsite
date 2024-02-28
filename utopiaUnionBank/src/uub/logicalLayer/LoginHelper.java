package uub.logicalLayer;


import uub.model.Employee;
import uub.model.User;
import uub.persistentLayer.EmployeeDao;
import uub.persistentLayer.IEmployeeDao;
import uub.persistentLayer.IUserDao;
import uub.persistentLayer.UserDao;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HashEncoder;

public class LoginHelper {

	public int login(String userName, String passWord) throws CustomBankException {

		IUserDao userDao = new UserDao();
		IEmployeeDao employeeDao = new EmployeeDao();
		
		passWord = HashEncoder.encode(passWord);

		
		User user = userDao.getUserWithEmail(userName);

		if (user!=null) {

			if (user.getPassword().equals(passWord)) {

				String userType = user.getUserType();

				if (userType.equals("Customer")) {
					return 1;
				} else {
					

					Employee employee = employeeDao.getEmployeesWithEmail(userName);
					
					String role = employee.getRole();

					if (role.equals("Admin")) {
						return 3;
					} else {
						return 2;
					}

				}

			} else {

				return 0;

			}
		} else {
			return -1;
		}

	}

}
