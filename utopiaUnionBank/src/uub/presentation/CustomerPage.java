package uub.presentation;

import java.util.List;

import uub.logicalLayer.CustomerHelper;
import uub.logicalLayer.TransactionHelper;
import uub.model.Account;
import uub.model.Customer;
import uub.model.Transaction;
import uub.staticLayer.CustomBankException;

public class CustomerPage extends Runner {
	

	private static Customer customer ;

	public CustomerPage(String username) throws CustomBankException {


		CustomerHelper customerHelper = new CustomerHelper();
		TransactionHelper transactionHelper = new TransactionHelper();
		
		customer = customerHelper.getProfile(username);

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

				Transaction transaction = getTransaction();
				
				transactionHelper.performTransaction(transaction);

				logger.warning("Press Enter to Exit");
				scanner.nextLine();

				break;
			}
			case 4: {

				exit = true;

				break;
			}

			
			default:
				break;
			}

		}

	}

	private Transaction getTransaction() throws CustomBankException {
		CustomerHelper customerHelper = new CustomerHelper();
		
		List<Account> accounts = customerHelper.getAccounts(customer.getId());
		
		
		Transaction transfer = new Transaction();

		transfer.setUserId(customer.getId());
		transfer.setType("P2P");

		logger.info("Enter Amount :-");
		transfer.setAmount(0-scanner.nextDouble());
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
		
		return transfer;
	}

	private void logAccounts() throws CustomBankException {
		
		CustomerHelper customerHelper = new CustomerHelper();
		
		List<Account> accounts = customerHelper.getAccounts(customer.getId());

		int size = accounts.size();

		logger.info("Your active accounts are : - ");
		if (size <= 0) {
			logger.info("You dont have active accounts !");

		} else {

			for (Account account : accounts) {

				logger.info(account.toString());

			}
			logger.info("Enter account id to get history :");
			displayHistory(scanner.nextInt());
		}
	}

	private void logProfile() {
		
		logger.info("\n");
		logger.info("User ID: " + customer.getId());
		logger.info("Name: " + customer.getName());
		logger.info("Email: " + customer.getEmail());
		logger.info("Phone: " + customer.getPhone());
		logger.info("DOB: " + customer.getdOB());
		logger.info("Gender: " + customer.getGender());
		logger.info("Password: " + customer.getPassword());
		logger.info("User Type: " + customer.getUserType());
		logger.info("Status: " + customer.getStatus());
		logger.info("\n");
	}

	private void displayHistory(int accNo) throws CustomBankException {
		
		CustomerHelper customerHelper = new CustomerHelper();
		
		List<Transaction> transactions = customerHelper.getHistory(accNo);
		if(!transactions.isEmpty()) {
		for(Transaction transaction : transactions) {
			logger.info(transaction.toString());
			
		}}
		else {
			logger.info("No Transactions from this account !");
		}
		
	}
}
