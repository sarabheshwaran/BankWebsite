package uub.presentation;

import java.util.List;

import uub.enums.TransferType;
import uub.logicallayer.AccountHelper;
import uub.logicallayer.CustomerHelper;
import uub.logicallayer.TransactionHelper;
import uub.model.Account;
import uub.model.Customer;
import uub.model.Transaction;
import uub.staticlayer.CustomBankException;
import uub.staticlayer.DateUtils;

public class CustomerPage extends Runner {
	

	private static Customer customer ;

	public CustomerPage(int id) throws CustomBankException {


		CustomerHelper customerHelper = new CustomerHelper();
		TransactionHelper transactionHelper = new TransactionHelper();
		
		customer = customerHelper.getCustomer(id);

		boolean exit = false;

		while (!exit) {

			logger.fine("Customer Portal");
			logger.info("Please Select an option : -");
			logger.info("1. See Profile.");
			logger.info("2. See Account details.");
			logger.info("3. Do Transaction.");
			logger.info("4. Logout");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1: {

				logProfile();

				logger.warning("Press Enter to exit ");
				scanner.nextLine();
				break;
			}
			case 2: {

				
				logger.info("Select account : ");
				
				logAccounts();
				
				
				scanner.nextLine();

				logger.warning("Press Enter to Exit");
				scanner.nextLine();

				break;
			}
			case 3: {

				logger.fine("Transaction Portal");
				boolean flag = true;
				while(flag) {

					try {
						
						getTransaction();
//				Transaction transaction = getTransaction();
//				
//				logger.info("Enter Password");
//				
//				String password = scanner.nextLine();
//				
//				transactionHelper.inBankTransfer(transaction, password);
				flag = false;
				logger.warning("Press Enter to Exit");
				scanner.nextLine();
				}
				catch (CustomBankException e) {
					
					Throwable cause = e.getCause();
					
					logger.severe(e.getMessage());
					if(cause != null) {
						System.out.println(cause.getMessage());
					}
				}}

				break;
			}
			case 4:{
				
				logger.fine("Histor");
				
			}
			case 5: {

				exit = true;

				break;
			}

			
			default:
				break;
			}

		}

	}

	private void getTransaction() throws CustomBankException {
		CustomerHelper customerHelper = new CustomerHelper();
		TransactionHelper transactionHelper = new TransactionHelper();
		
		List<Account> accounts = customerHelper.getActiveAccounts(customer.getId());
		
		
		Transaction transfer = new Transaction();
		
		logger.info("0. In bank transafer \n1. Out of bank transafer");
		int choice = scanner.nextInt();
		scanner.nextLine();

		transfer.setUserId(customer.getId());
		

		logger.info("Enter Amount :-");
		transfer.setAmount(scanner.nextDouble());
		scanner.nextLine();

		logger.info("Select Account :");
		for (Account account : accounts) {
			logger.info(account.toString());
		}
		
		transfer.setAccNo(scanner.nextInt());

		logger.info("Enter receiver account :");
		transfer.setTransactionAcc(scanner.nextInt());
		scanner.nextLine();

		logger.info("Enter transaction description :");
		transfer.setDesc(scanner.nextLine());
		logger.info("Enter Password");
		
		String password = scanner.nextLine();
		
		if(choice == 0) {
		
			transfer.setType(TransferType.INTRA_BANK);
		transactionHelper.makeTransaction(transfer, password);
		
		}
		else if(choice == 1) {
			transfer.setType(TransferType.INTER_BANK);
			transactionHelper.makeTransaction(transfer, password);
		}
	}

	private void logAccounts() throws CustomBankException {
		
		CustomerHelper customerHelper = new CustomerHelper();
		
		List<Account> accounts = customerHelper.getActiveAccounts(customer.getId());

		int size = accounts.size();

		logger.info("Your active accounts are : - ");
		if (size <= 0) {
			logger.info("You dont have active accounts !");

		} else {

			for (Account account : accounts) {

				logger.info(account.toString());

			}
			
			logger.info("Enter account id to view :");
			displayAccount(scanner.nextInt());
		}
	}

	private void displayAccount(int accNo) throws CustomBankException {
		
		AccountHelper accountHelper = new AccountHelper();
		logger.info(accountHelper.getAccount(accNo).toString());
		
		logger.info("0.withdraw \n1.deposit \n2.history");
		int option = scanner.nextInt();
		
		if(option == 0) {
			withdraw(accNo);
		}
		else if(option == 1) {
			deposit(accNo);
		}
		else if(option == 2) {
			displayHistory(accNo);
		}
	}
	
	private void deposit(int accNo) throws CustomBankException {
		
		TransactionHelper transactionHelper = new TransactionHelper();
		
		Transaction transaction = new Transaction();
		
		transaction.setAccNo(accNo);
		
		logger.info("Enter amount :");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		transaction.setAmount(amount);
		
		transaction.setType(TransferType.DEPOSIT);
		
		logger.info("Enter description : ");
		String desc = scanner.nextLine();
		
		transaction.setDesc(desc);
		
		transaction.setUserId(customer.getId());
		
		logger.info("Enter password :");
		String password = scanner.nextLine();
		
		transactionHelper.makeTransaction(transaction, password);
		
	}
	
	private void withdraw(int accNo) throws CustomBankException{
		
		TransactionHelper transactionHelper = new TransactionHelper();
		
		Transaction transaction = new Transaction();
		
		transaction.setAccNo(accNo);
		
		logger.info("Enter amount :");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		transaction.setAmount(amount);
		
		transaction.setType(TransferType.WITHDRAW);
		
		logger.info("Enter description : ");
		String desc = scanner.nextLine();
		
		transaction.setDesc(desc);
		
		transaction.setUserId(customer.getId());
		
		logger.info("Enter password :");
		String password = scanner.nextLine();
		
		transactionHelper.makeTransaction(transaction, password);
		
	}
	private void logProfile() {
		
		logger.info("\n");
		logger.info("User ID: " + customer.getId());
		logger.info("Name: " + customer.getName());
		logger.info("Email: " + customer.getEmail());
		logger.info("Phone: " + customer.getPhone());
		logger.info("DOB: " + DateUtils.formatDate(customer.getdOB()));
		logger.info("Gender: " + customer.getGender());
		logger.info("User Type: " + customer.getUserType());
		logger.info("Status: " + customer.getStatus());
		logger.info("\n");
	}

	private void displayHistory(int accNo) throws CustomBankException {
		
		TransactionHelper transactionHelper = new TransactionHelper();
		
	
		

			

			
		List<Transaction> transactions = transactionHelper.getTransaction(accNo, "2024-03-01","2024-03-07", 10, 1);
		if(!transactions.isEmpty()) {
		for(Transaction transaction : transactions) {
			
			logger.info(transaction.getId()  +" - "+ DateUtils.formatDate(transaction.getTime())+" = " + transaction.toString());
			
		}}
		else {
			logger.info("No Transactions from this account !");
		}
		
		
		
		
	}
}
