package uub.persistentLayer;


import java.util.List;

import uub.model.User;
import uub.staticLayer.CustomBankException;

public interface IUserDao {


	User getUserWithId(int userId) throws CustomBankException;

	void updateUser(User user) throws CustomBankException;

	User getUserWithEmail(String email) throws CustomBankException;



	List<User> getAllUsers(String type, String status) throws CustomBankException;
}
