package uub.persistentLayer;

import java.util.List;

import uub.model.User;
import uub.staticLayer.CustomBankException;

public interface IUserDao {

//	public List<User> getUsers(String field, Object value ) throws CustomBankException;

	List<User> getUsers(User user) throws CustomBankException;
}
