package uub.persistentinterfaces;


import java.util.List;

import uub.enums.UserStatus;
import uub.enums.UserType;
import uub.model.User;
import uub.staticlayer.CustomBankException;

public interface IUserDao {


	List<User> getUserWithId(int userId, UserStatus status) throws CustomBankException;

	void updateUser(User user) throws CustomBankException;

	List<User> getAllUsers(UserType type, UserStatus status, int limit, int offSet) throws CustomBankException;
}
