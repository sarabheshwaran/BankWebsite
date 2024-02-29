package uub.persistentLayer;

import java.util.List;
import java.util.Map;

import uub.model.Account;
import uub.staticLayer.CustomBankException;

public interface IAccountDao {

	void addAccounts(List<Account> accounts) throws CustomBankException;

	int updateAccount(Account account) throws CustomBankException;

	Map<Integer, List<Account>> getBranchAccounts(int branchId, String status) throws CustomBankException;

	List<Account> getUserAccounts(int userId, String status) throws CustomBankException;

	List<Account> getAccount(int accNo) throws CustomBankException;

	List<Account> getAllAccounts(String status) throws CustomBankException;

	Map<Integer, List<Account>> getBranchAccounts(String status) throws CustomBankException;
	



	
}
