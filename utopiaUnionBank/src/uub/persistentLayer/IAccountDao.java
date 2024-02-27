package uub.persistentLayer;

import java.util.List;

import uub.model.Account;
import uub.model.User;
import uub.staticLayer.CustomBankException;

public interface IAccountDao {

	void addAccounts(List<Account> accounts) throws CustomBankException;

	void updateAccount(Account account) throws CustomBankException;

	List<User> getUserAccounts(int userId, boolean active) throws CustomBankException;

	List<User> getBranchAccounts(int branchId, boolean active) throws CustomBankException;

	List<Account> getAllAccounts(int accNo, boolean active) throws CustomBankException;

	List<User> getUserAccounts(int accNo) throws CustomBankException;
	



	
}
