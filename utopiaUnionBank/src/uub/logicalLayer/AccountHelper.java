package uub.logicalLayer;

import java.util.List;

import uub.model.Account;
import uub.model.User;
import uub.persistentLayer.AccountDao;
import uub.persistentLayer.IAccountDao;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HelperUtils;

public class AccountHelper {
	
	
	public int getUserId(int accNo) throws CustomBankException{
		int userId = 0;
		
		IAccountDao accountDao = new AccountDao();
		
		List<User> users = accountDao.getUserAccounts(accNo);
		
		if(!users.isEmpty()) {
			
			userId = users.get(0).getId();
			
		}
		
		return userId;
	}
	
	
	public Account getAccount(int accNo)throws CustomBankException{
		
		IAccountDao accountDao = new AccountDao();
		
		
		List<Account> accounts  = accountDao.getAllAccounts(accNo,true);
		
		
		if(!accounts.isEmpty()) {
			
		return accounts.get(0);
		 
		
		}else {
			throw new CustomBankException("Account Not Found");
		}
		
		
		
	}
	
	public double getBalance(int accNo) throws CustomBankException {

		double balance = getAccount(accNo).getBalance();
		
		return balance;

	}
	
	public void updateAccount(Account account) throws CustomBankException {
		
		IAccountDao accountDao = new AccountDao();
		HelperUtils.nullCheck(account);
		
		accountDao.updateAccount(account);

	}
	
	public Account updateBalance(int accNo, double amount) throws CustomBankException{
		
		
		Account account = getAccount(accNo);
		
		double balance = account.getBalance();
		
		if(balance >= amount) {
		
		account.setBalance(balance-amount);
		
		return account;
		
		}
		else {
			
			throw new CustomBankException("Insufficient Balance");
		}
		
	}
	
	public void addAccount(Account account)throws CustomBankException{
		
		IAccountDao accountDao = new AccountDao();
		CustomerHelper customerHelper = new CustomerHelper();
		
		customerHelper.getCustomer(account.getUserId());
		
		account.setBalance(0);
		
		account.setStatus("ACTIVE");
		
		accountDao.addAccounts(List.of(account));
		
	}
	
	
	

}
