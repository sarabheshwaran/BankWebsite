package uub.presentation;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import uub.logicallayer.LoginHelper;
import uub.staticlayer.CustomBankException;
import uub.staticlayer.HelperUtils;

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
				int id = scanner.nextInt();
				scanner.nextLine();
				logger.info("Enter Password :");
				String password = scanner.nextLine();
				
				int login = loginHelper.login(id, password);
				
				switch (login) {
				case 0 :{
					logger.info("Welcome Customer");
					new CustomerPage(id);
					break;}
				case 1 :
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
