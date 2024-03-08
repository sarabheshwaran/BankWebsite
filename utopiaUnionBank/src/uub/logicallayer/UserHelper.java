package uub.logicallayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import uub.enums.UserStatus;
import uub.model.User;
import uub.persistentinterfaces.IUserDao;
import uub.staticlayer.CustomBankException;

public class UserHelper {

	private IUserDao userDao;

	public UserHelper() throws CustomBankException {

		try {

			Class<?> UserDao = Class.forName("uub.persistentlayer.UserDao");
			Constructor<?> useDao = UserDao.getDeclaredConstructor();

			userDao = (IUserDao) useDao.newInstance();

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			throw new CustomBankException("Error getting Data ! ", e);
		}

	}



	public User getUser(int id) throws CustomBankException {

		List<User> users = userDao.getUserWithId(id,UserStatus.ACTIVE);

		if (!users.isEmpty()) {
			return users.get(0);
		} else {
			throw new CustomBankException("User Not Found !");
		}

	}

}
