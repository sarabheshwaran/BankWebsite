package uub.presentation;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import uub.logicalLayer.LoginHelper;
import uub.staticLayer.ConnectionManager;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HelperUtils;

public class Runner {

	public static Scanner scanner = new Scanner(System.in);
	public static Logger logger = Logger.getGlobal();
	
	static {
	
		HelperUtils.formatLogger();
	
	
		logger.setLevel(Level.FINER);
	
	}
	
	public static void main(String[] args)  {
		
		try {
		
		LoginHelper loginHelper = new LoginHelper();
		

		boolean exit = false;
		while (!exit) {
			
			try {
			
			logger.finer("--------WELCOME TO UUB--------");
			logger.fine("Please Select an option : - ");
			logger.fine("1. Login");
			logger.fine("1. About");
			logger.fine("3. Exit");
			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				
				logger.info("Enter UserName :");
				String username = scanner.nextLine();
				logger.info("Enter Password :");
				String password = scanner.nextLine();
				
				int login = loginHelper.login(username, password);
				
				switch (login) {
				case 1 :{
					logger.info("Welcome Customer");
					new CustomerPage(username);
					break;}
				case 2 :
					logger.info("Welcome Employee");
					new EmployeePage(username);
					break;
				
				default:
					break;
				}
				
				break;
			case 2:
				break;
			case 3:
				exit = true;
				ConnectionManager.close();
				break;
			default:
			}}
			catch(CustomBankException e) {
				logger.warning(e.getMessage());
				
				
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
