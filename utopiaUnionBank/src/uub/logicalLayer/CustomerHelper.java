package uub.logicalLayer;

import java.util.ArrayList;
import java.util.List;

import uub.model.Account;
import uub.model.Customer;
import uub.model.Transaction;
import uub.model.User;
import uub.persistentLayer.AccountDao;
import uub.persistentLayer.CustomerDao;
import uub.persistentLayer.IAccountDao;
import uub.persistentLayer.ICustomerDao;
import uub.persistentLayer.ITransactionDao;
import uub.persistentLayer.TransactionDao;
import uub.staticLayer.CustomBankException;

public class CustomerHelper {

	public Customer getProfile(String username) throws CustomBankException {
		
		ICustomerDao customerDao = new CustomerDao();
		
		List<Customer> customers = customerDao.getCustomersWithEmail(username);
		
		if(!customers.isEmpty()) {
			return customers.get(0);
		}else {
			throw new CustomBankException("Customer not found!");
		}
		
	}
	
	public Customer getCustomer(int id) throws CustomBankException {
		
		ICustomerDao customerDao = new CustomerDao();
		
		List<Customer> customers = customerDao.getCustomers(id);
		
		if(!customers.isEmpty()) {
			return customers.get(0);
		}else {
			throw new CustomBankException("Customer not found!");
		}
		
	}
	
	
	public List<Account> getAccounts(int customerId) throws CustomBankException{
		
		IAccountDao accountDao = new AccountDao();
		
		
		List<User> users =  accountDao.getUserAccounts(customerId,true);
		
		if(!users.isEmpty()) {
			return users.get(0).getAccounts();
		}else {
			return new ArrayList<Account>();
		}
		
	}

	public List<Transaction> getHistory(int id) throws CustomBankException {
		
		ITransactionDao transactionDao = new TransactionDao();
		
		Transaction transaction = new  Transaction();
		transaction.setAccNo(id);
		
		return transactionDao.getTransactions(transaction);
	}
	
	
	
	
}
