package uub.persistentLayer;


import uub.model.User;
import uub.staticLayer.CustomBankException;

public interface IUserDao {


	User getUserWithId(int userId) throws CustomBankException;

	void updateUser(User user) throws CustomBankException;

	User getUserWithEmail(String email) throws CustomBankException;
}
