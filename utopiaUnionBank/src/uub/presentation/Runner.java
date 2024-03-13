package uub.presentation;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import uub.enums.UserType;
import uub.logicallayer.TransactionHelper;
import uub.logicallayer.UserHelper;
import uub.staticlayer.CustomBankException;
import uub.staticlayer.HelperUtils;

public class Runner {

	public static Scanner scanner = new Scanner(System.in);
	public static Logger logger = Logger.getGlobal();
	
	static {
	
		HelperUtils.formatLogger();
	
	
		logger.setLevel(Level.FINEST);
	
	}
	
	public static void main(String[] args)  {
		
		try {
		
		UserHelper loginHelper = new UserHelper();
		

		boolean exit = false;
		while (!exit) {
			
			try {
			
			logger.finer("--------WELCOME TO UUB--------");
			logger.finer("Please Select an option : - ");
			logger.fine("1. Login");
			logger.fine("1. About");
			logger.fine("3. Exit");
			logger.info(UserHelper.customerCache.toString());

			logger.info(UserHelper.accountMapCache.toString());
			
			logger.info(UserHelper.accountCache.toString());
			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				
				logger.info("Enter UserName :");
				int id = scanner.nextInt();
				scanner.nextLine();
				logger.info("Enter Password :");
				System.out.print("\u001B[8m");
				String password = scanner.nextLine();
				System.out.print("[0m");
				
				UserType login = loginHelper.login(id, password);
				
				switch (login) {
				case CUSTOMER :{
					logger.info("Welcome Customer");
					new CustomerPage(id);
					break;}
				case EMPLOYEE:
					logger.info("Welcome Employee");
					new EmployeePage(id);
					break;
				
				default:
					break;
				}
				
				break;
			case 2:
				break;
			case 3:
				exit = true;
				scanner.close();
				break;
			default:
			}}
			catch(CustomBankException e) {
				Throwable cause = e.getCause();
				
				logger.severe(e.getMessage());
				if(cause != null) {
					System.out.println(cause.getMessage());
				}
				
			}
			catch(InputMismatchException e) {
				
				logger.severe("Enter Proper input ! ");

				scanner.next();
			}
		}

		scanner.close();

	}catch (Exception e) {
		logger.severe("Problem in setting up files!");

		e.printStackTrace();
	}
		
	}
	
	
	

}
