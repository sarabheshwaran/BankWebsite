package uub.persistentinterfaces;

import java.util.List;
import java.util.Map;

import uub.model.Account;
import uub.staticLayer.CustomBankException;

public interface IAccountDao {

	void addAccounts(List<Account> accounts) throws CustomBankException;

	int updateAccount(Account account) throws CustomBankException;


	List<Account> getUserAccounts(int userId, String status) throws CustomBankException;

	List<Account> getAccount(int accNo) throws CustomBankException;



	Map<Integer, List<Account>> getBranchAccounts(int branchId, String status, int limit, int offSet)
			throws CustomBankException;
	



	
}
