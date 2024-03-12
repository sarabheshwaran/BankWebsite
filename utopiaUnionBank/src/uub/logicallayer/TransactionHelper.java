package uub.logicallayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import uub.enums.Exceptions;
import uub.enums.TransferType;
import uub.model.Account;
import uub.model.Transaction;
import uub.persistentinterfaces.IAccountDao;
import uub.persistentinterfaces.ITransactionDao;
import uub.staticlayer.CustomBankException;
import uub.staticlayer.TransactionUtils;

public class TransactionHelper {
	
	private IAccountDao accountDao;
	private ITransactionDao transactionDao;

	public static LRUCache<Integer,Account> accountCache = new LRUCache<Integer, Account>(50);

	public TransactionHelper() throws CustomBankException {

		try {

			Class<?> TransactionDao = Class.forName("uub.persistentlayer.TransactionDao");
			Constructor<?> transDao = TransactionDao.getDeclaredConstructor();
			
			Class<?> AccountDao = Class.forName("uub.persistentlayer.AccountDao");
			Constructor<?> accDao = AccountDao.getDeclaredConstructor();

			accountDao = (IAccountDao) accDao.newInstance();

			transactionDao = (ITransactionDao) transDao.newInstance();

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			throw new CustomBankException(Exceptions.DATABASE_CONNECTION_ERROR, e);
		}

	}
	
	public Account getAccount(int accNo) throws CustomBankException {
		
		Account account = accountCache.get(accNo);
		
		if(account != null) {
			return account;
		}else {

		
		List<Account> accounts = accountDao.getAccount(accNo);

		if (!accounts.isEmpty()) {

			account =  accounts.get(0);
			
			accountCache.put(accNo, account);
			
			return account;

		} else {
			throw new CustomBankException(Exceptions.ACCOUNT_NOT_FOUND);
		}}

	}


	public void selfTransfer(Transaction transaction, TransferType type) throws CustomBankException {

		if (type == TransferType.WITHDRAW) {

			transaction.setAmount(0 - transaction.getAmount());

		}

		setTransaction(transaction);

		transactionDao.makeTransaction(List.of(transaction));

	}

	public void outBankTransfer(Transaction transaction) throws CustomBankException {

		transaction.setAmount(0 - transaction.getAmount());

		setTransaction(transaction);

		transactionDao.makeTransaction(List.of(transaction));

	}

	public void inBankTransfer(Transaction transaction) throws CustomBankException {

		transaction.setAmount(0 - transaction.getAmount());

		Transaction rTransaction = generateReceiverTransaction(transaction);

		setTransaction(transaction);
		setTransaction(rTransaction);

		transactionDao.makeTransaction(List.of(transaction, rTransaction));

	}

	private void setTransaction(Transaction transaction) throws CustomBankException {


		transaction.setTime(System.currentTimeMillis());
		
		int accNo = transaction.getAccNo();
		
		Account account = getAccount(accNo);


		TransactionUtils.validateAccount(account);

		double balance = account.getBalance();
		
		double closingBalance = balance + transaction.getAmount();
		
		if(closingBalance < 0) {
			throw new CustomBankException(Exceptions.BALANCE_INSUFFICIENT);
		}

		transaction.setOpeningBal(balance);
		transaction.setClosingBal(closingBalance);

	}

	private Transaction generateReceiverTransaction(Transaction transaction) throws CustomBankException {

		Transaction rTransaction = new Transaction();

		rTransaction.setId(transaction.getId());
		rTransaction.setTime(transaction.getTime());
		rTransaction.setDesc(transaction.getDesc());
		rTransaction.setType(transaction.getType());
		rTransaction.setStatus(transaction.getStatus());

		int accNo = transaction.getTransactionAcc();

		rTransaction.setAccNo(accNo);
		rTransaction.setTransactionAcc(transaction.getAccNo());
		rTransaction.setAmount(0 - transaction.getAmount());

		Account account = getAccount(accNo);
		
		int userId = account.getUserId();

		rTransaction.setUserId(userId);

		return rTransaction;

	}

	public List<Transaction> getTransactions(int accNo, long from, long to, int limit, int offSet) throws CustomBankException{
		return transactionDao.getTransactions(accNo, from, to, limit, offSet);
	}

}
