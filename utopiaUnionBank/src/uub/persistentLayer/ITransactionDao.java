package uub.persistentLayer;

import java.util.List;

import uub.model.Transaction;
import uub.staticLayer.CustomBankException;

public interface ITransactionDao {

	public List<Transaction> getTransactions(String field, Object value) throws CustomBankException;
	
	public void addTransaction(Transaction transaction) throws CustomBankException;
	
	public int getLastId() throws CustomBankException;

	List<Transaction> getTransactions(Transaction transaction) throws CustomBankException;
	
}
