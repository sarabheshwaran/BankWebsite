package uub.persistentinterfaces;

import java.util.List;

import uub.model.Transaction;
import uub.staticLayer.CustomBankException;

public interface ITransactionDao {

	
	public void addTransaction(Transaction transaction) throws CustomBankException;
	
	public int getLastId() throws CustomBankException;







	List<Transaction> getTransactions(int accNo, long from, long to, int limit, int offSet) throws CustomBankException;

	List<Transaction> getTransactionsOfUser(int userId, long from, long to, int limit, int offSet)
			throws CustomBankException;
	
}
