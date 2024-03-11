package uub.logicallayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import uub.enums.AccountStatus;
import uub.enums.Exceptions;
import uub.enums.TransactionStatus;
import uub.enums.TransferType;
import uub.model.Account;
import uub.model.Customer;
import uub.model.Transaction;
import uub.persistentinterfaces.IAccountDao;
import uub.persistentinterfaces.ICustomerDao;
import uub.staticlayer.CustomBankException;
import uub.staticlayer.DateUtils;
import uub.staticlayer.HelperUtils;
import uub.staticlayer.TransactionUtils;

public class CustomerHelper extends UserHelper{

	private IAccountDao accountDao ;
	private ICustomerDao customerDao;

	public CustomerHelper() throws CustomBankException {
		try {
			Class<?> AccountDao = Class.forName("uub.persistentlayer.AccountDao");
			Constructor<?> accDao = AccountDao.getDeclaredConstructor();

			Class<?> CustomerDao = Class.forName("uub.persistentlayer.CustomerDao");
			Constructor<?> cusDao = CustomerDao.getDeclaredConstructor();

			accountDao = (IAccountDao) accDao.newInstance();
			customerDao = (ICustomerDao) cusDao.newInstance();

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			
			throw new CustomBankException( Exceptions.DATABASE_CONNECTION_ERROR, e);
		}
	}


	public Customer getCustomer(int id) throws CustomBankException {

		List<Customer> customers = customerDao.getCustomers(id);

		
		if (!customers.isEmpty()) {
			return customers.get(0);
		} else {
			throw new CustomBankException(Exceptions.CUSTOMER_NOT_FOUND);
		}

	}

	public Map<Integer, Account> getActiveAccounts(int customerId) throws CustomBankException {

		return accountDao.getUserAccounts(customerId, AccountStatus.ACTIVE);
		
	}
	
	public Map<Integer, Map<Integer, Account>> getInactiveAccounts(int customerId, int limit, int offSet) throws CustomBankException {

		return accountDao.getBranchAccounts(customerId, AccountStatus.INACTIVE, limit, offSet);
		
	}
	


	public void makeTransaction(Transaction transaction, String password) throws CustomBankException {

		HelperUtils.nullCheck(transaction);
		HelperUtils.nullCheck(password);
		
		TransactionHelper transactionHelper = new TransactionHelper();
		

		TransactionUtils.validateTransaction(transaction);


		try {

			passwordValidate(transaction.getUserId(), password);
			
			String id = TransactionUtils.generateUniqueId(transaction.getAccNo(), transaction.getTransactionAcc());
			transaction.setId(id);

			transaction.setStatus(TransactionStatus.SUCCESS);

			TransferType type = transaction.getType();

			switch (type) {

			case WITHDRAW:
			case DEPOSIT: {
				transactionHelper.selfTransfer(transaction, type);
				break;
			}
			case INTER_BANK: {
				transactionHelper.outBankTransfer(transaction);
				break;
			}
			case INTRA_BANK: {
				transactionHelper.inBankTransfer(transaction);
				break;
			}
			}

		} catch (CustomBankException e) {
			throw new CustomBankException(Exceptions.TRANSACTION_ERROR, e);
		}

	}
	

	public List<Transaction> getNDaysTransaction(int accNo, int days, int limit, int page) throws CustomBankException {

		TransactionHelper transactionHelper = new TransactionHelper();
		long todayMillis = System.currentTimeMillis();

		long ansMillis = todayMillis - 86400000 * (days);

		return transactionHelper.getTransactions(accNo, ansMillis, todayMillis, limit, (page - 1) * limit);

	}

	public List<Transaction> getTransaction(int accNo, String from, String to, int limit, int page)
			throws CustomBankException {

		
		HelperUtils.nullCheck(from);
		HelperUtils.nullCheck(to);

		TransactionHelper transactionHelper = new TransactionHelper();
		LocalDate fromDate = LocalDate.parse(from);
		LocalDate toDate = LocalDate.parse(to);

		toDate = toDate.plusDays(1);

		long fromMillis = DateUtils.formatDate(fromDate);
		long toMillis = DateUtils.formatDate(toDate);

		return transactionHelper.getTransactions(accNo, fromMillis, toMillis, limit, (page - 1) * limit);
	}


}
