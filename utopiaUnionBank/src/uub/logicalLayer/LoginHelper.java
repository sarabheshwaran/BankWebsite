package uub.logicalLayer;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import uub.model.Employee;
import uub.model.User;
import uub.persistentLayer.IEmployeeDao;
import uub.persistentLayer.IUserDao;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HashEncoder;

public class LoginHelper {
	
	private IUserDao userDao;
	private IEmployeeDao employeeDao;

	public LoginHelper() throws CustomBankException {
		
		
		try {

			Class<?> UserDao = Class.forName("uub.persistentLayer.UserDao");
			Constructor<?> useDao = UserDao.getDeclaredConstructor();

			Class<?> EmployeeDao = Class.forName("uub.persistentLayer.EmployeeDao");
			Constructor<?> empDao = EmployeeDao.getDeclaredConstructor();
			
			
			userDao = (IUserDao) useDao.newInstance();
			employeeDao = (IEmployeeDao) empDao.newInstance();
			
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException |
				InstantiationException | IllegalAccessException | IllegalArgumentException 
				| InvocationTargetException e) {
			
			throw new CustomBankException("Error getting Data ! ",e);
		}
		
		
	}

	public int login(String userName, String passWord) throws CustomBankException {

		EmployeeHelper employeeHelper = new EmployeeHelper();
		
		passWord = HashEncoder.encode(passWord);

		
		User user = userDao.getUserWithEmail(userName);

		if (user!=null) {

			if (user.getPassword().equals(passWord)) {

				String userType = user.getUserType();

				if (userType.equals("Customer")) {
					return 1;
				} else {
					

					Employee employee = employeeHelper.getProfile(userName);
					
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
