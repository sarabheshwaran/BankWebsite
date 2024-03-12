package uub.logicallayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import uub.enums.Exceptions;
import uub.enums.UserStatus;
import uub.enums.UserType;
import uub.model.Account;
import uub.model.Customer;
import uub.model.User;
import uub.persistentinterfaces.IUserDao;
import uub.staticlayer.CustomBankException;
import uub.staticlayer.HashEncoder;
import uub.staticlayer.HelperUtils;

public class UserHelper {
	
	protected IUserDao userDao;
	public static LRUCache<Integer,Customer> customerCache = new LRUCache<Integer, Customer>(50);
	public static LRUCache<Integer, Map<Integer, Account>> accountMapCache = new LRUCache<Integer, Map<Integer,Account>>(50);
	
	
	
	public UserHelper() throws CustomBankException {

		try {

			Class<?> UserDao = Class.forName("uub.persistentlayer.UserDao");
			Constructor<?> useDao = UserDao.getDeclaredConstructor();

			userDao = (IUserDao) useDao.newInstance();

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			throw new CustomBankException(Exceptions.DATABASE_CONNECTION_ERROR, e);
		}

	}



	public User getUser(int id) throws CustomBankException {

		List<User> users = userDao.getUser(id,UserStatus.ACTIVE);

		if (!users.isEmpty()) {
			return users.get(0);
		} else {
			throw new CustomBankException(Exceptions.USER_NOT_FOUND);
		}

	}

	public void passwordValidate(int userId, String password) throws CustomBankException {

		HelperUtils.nullCheck(password);

		password = HashEncoder.encode(password);
		User user = getUser(userId);

		if (!user.getPassword().equals(password)) {

			throw new CustomBankException(Exceptions.PASSWORD_WRONG);

		}

	}

	public UserType login(int id, String password) throws CustomBankException {

		HelperUtils.nullCheck(password);
		
		try {

			User user = getUser(id);

			passwordValidate(id, password);

			return user.getUserType();
					
			
		} catch (CustomBankException e) {
			throw new CustomBankException(Exceptions.LOGIN_FAILED, e);
		}

	}

}
