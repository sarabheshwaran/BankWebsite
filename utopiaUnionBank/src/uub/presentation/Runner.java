package uub.presentation;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import uub.logicalLayer.LoginHelper;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HelperUtils;

public class Runner {

	public static Scanner scanner = new Scanner(System.in);
	public static Logger logger = Logger.getGlobal();
	
	static {
	
		HelperUtils.formatLogger();
	
	
		logger.setLevel(Level.FINER);
	
	}
	
	public static void main(String[] args) throws SecurityException, IOException, CustomBankException {
		
//		EmployeeHelper em = new EmployeeHelper();
		
//		em.deActivateAcc(4);

//		Employee employee = new Employee(1, "Light Yagami", "kira", "1234321234", 0, "unknown", "kira", "Employee", "Active", "Admin", 1);
//
//		ICustomerDao cus = new CustomerDao();
//		
//		Customer cc = new Customer();
//		
//		cc.setName("sara");
//		
//		System.out.println(cus.getCustomers(cc));
//		
//		AccountHelper acc = new AccountHelper();
//		
//		Account a = new Account();
//		
//		a.setAccNo(3);
//		
//		try {
//		System.out.println(acc.getBalance(1));}
//		catch (Exception e) {
//			
//			e.printStackTrace();
//		}
//		
//		System.out.println(acc.getBalance(3));
		
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
				case 3 :
					logger.info("Welcome Admin");
					new EmployeePage(username);
					break;
				case 0 :
					logger.warning("Password is wrong");
					break;
				case -1 :
					logger.warning("User Not Found");
					break;

				default:
					break;
				}
				
				break;
			case 2:
				break;
			case 3:
				exit = true;
				break;
			default:
			}}
			catch(CustomBankException e) {
				logger.warning(e.getMessage());
				
				e.printStackTrace();
				
			}
			catch(InputMismatchException e) {
				
				logger.severe("Enter Proper input ! ");

				scanner.next();
			}
		}

		scanner.close();

	}
	
	

}
