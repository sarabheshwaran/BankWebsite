package uub.logicalLayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import uub.persistentLayer.IEmployeeDao;
import uub.persistentLayer.IUserDao;
import uub.staticLayer.CustomBankException;

public class UserHelper {

	private IUserDao userDao;
	private IEmployeeDao employeeDao;

	public UserHelper() throws CustomBankException {
		
		
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
	
	
	
}
