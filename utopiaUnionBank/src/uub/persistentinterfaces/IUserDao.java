package uub.persistentinterfaces;


import java.util.List;

import uub.enums.UserStatus;
import uub.model.User;
import uub.staticlayer.CustomBankException;

public interface IUserDao {

	List<User> getUserWithId(int userId, UserStatus status) throws CustomBankException;

	int updateUser(User user) throws CustomBankException;

}
