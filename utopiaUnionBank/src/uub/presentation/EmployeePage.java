package uub.presentation;

import java.util.Date;
import java.util.InputMismatchException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import uub.logicalLayer.AccountHelper;
import uub.logicalLayer.BranchHelper;
import uub.logicalLayer.EmployeeHelper;
import uub.model.Account;
import uub.model.Branch;
import uub.model.Customer;
import uub.model.Employee;
import uub.model.User;
import uub.staticLayer.CustomBankException;

public class EmployeePage extends Runner {


	private static Employee employee;

	public EmployeePage(String username) throws CustomBankException {
		
		EmployeeHelper employeeHelper = new EmployeeHelper();

		employee = employeeHelper.getProfile(username);
		String role = employee.getRole();

		boolean exit = false;

		if (role.equals("Admin")) {

			while (!exit) {

				try {
					int i = 1;

					logger.fine("Employee Portal");
					logger.info("Please Select an option : -");
					logger.info(i++ + ". See Profile.");
					logger.info(i++ + ". See All Customers.");
					logger.info(i++ + ". See All Branches.");
					logger.info(i++ + ". Add Customer.");
					logger.info(i++ + ". Logout");

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

						logger.info("Customer List : ");

//						logBranches();

						break;
					}
					case 3: {

						logger.info("BranchList : ");

						logBranches();

						break;
					}

					case 4: {

						createCustomer();
						logger.warning("Press Enter to Exit");
						scanner.nextLine();
						break;
					}
					
					case 10:

						break;
					case 6: {

						exit = true;
						break;
					}
					default:
						break;
					}
				} catch (InputMismatchException e) {
					logger.severe("Invalid input!");
					scanner.next();
				} catch (CustomBankException e) {
					logger.severe(e.getMessage());
					scanner.next();
				}

			}

		}

		else {

			while (!exit) {

				try {
					int i = 1;

					logger.fine("Employee Portal");
					logger.info("Please Select an option : -");
					logger.info(i++ + ". See Profile.");
					logger.info(i++ + ". See All Active accounts.");
					logger.info(i++ + ". See Inactive accounts.");

					logger.info(i++ + ". Add Account.");

					logger.info(i++ + ". Add Customer.");
					logger.info(i++ + ". Logout");

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

						logActiveAccounts(employee.getBranchId());
						logger.warning("Press Enter to Exit");
						scanner.nextLine();

						break;
					}
					case 3: {

						logInactiveAccounts(employee.getBranchId());
						logger.warning("Press Enter to Exit");
						scanner.nextLine();

						break;
					}
					case 5: {

						createCustomer();
						logger.warning("Press Enter to Exit");
						scanner.nextLine();
						break;
					}
					case 4: {

						createAccount(employee.getBranchId());
						logger.warning("Press Enter to Exit");
						scanner.nextLine();

						break;
					}
					case 10:

						break;
					case 6: {

						exit = true;
						break;
					}
					default:
						break;
					}
				} catch (InputMismatchException e) {
					logger.severe("Invalid input!");
					scanner.next();
				}

			}
		}

	}

	private void createAccount(int branchId) {
		boolean exit = false;
		Account account = new Account();

		while (!exit) {

			try {

				logger.fine("Account Creation");

				logger.info("Enter UserId : ");
				account.setUserId(scanner.nextInt());
				scanner.nextLine();
				logger.info("Enter Type : ");
				account.setType(scanner.nextLine());
				account.setBranchId(branchId);

				AccountHelper accountHelper = new AccountHelper();

				accountHelper.addAccount(account);

				logger.warning("Account Created !");
				exit = true;

			} catch (Exception e) {
				logger.severe("Account Creation Failed : " + e.getMessage());
			}

		}

	}

	private void createCustomer() {
		boolean exit = false;
		Customer customer = new Customer();
		while (!exit) {

			try {

				logger.fine("Customer Creation:");

				logger.info("Enter Name: ");
				customer.setName(scanner.nextLine());

				logger.info("Enter Email: ");
				customer.setEmail(scanner.nextLine());

				logger.info("Enter Phone Number: ");
				customer.setPhone(scanner.nextLine());

				logger.info("Enter Date of Birth (dd-mm-yyyy): ");

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");

				Date date;
				long millis = 0;

				date = dateFormat.parse(scanner.nextLine());
				millis = date.getTime();

				customer.setdOB(millis);

				logger.info("Enter Gender: ");
				customer.setGender(scanner.nextLine());

				logger.info("Enter Password: ");
				customer.setPassword(scanner.nextLine());

				logger.info("Enter Address:");
				customer.setAddress(scanner.nextLine());

				logger.info("Enter Aadhar: ");
				customer.setAadhar(scanner.nextLine());

				logger.info("Enter PAN : ");
				customer.setpAN(scanner.nextLine());

				EmployeeHelper employeeHelper = new EmployeeHelper();

				employeeHelper.addCustomer(customer);

				logger.warning("CUstomer created ");
				exit = true;

			} catch (CustomBankException | ParseException e) {
				logger.warning(e.getMessage());
			}

			logger.info("Enter to exit");

		}

	}

	private void createEmployee() {
		boolean exit = false;
		Employee employee = new Employee();
		while (!exit) {

			try {

				logger.fine("Employee Creation:");

				logger.info("Enter Branch id :");
				employee.setBranchId(scanner.nextInt());
				scanner.nextLine();

				logger.info("Enter Name: ");
				employee.setName(scanner.nextLine());

				logger.info("Enter Email: ");
				employee.setEmail(scanner.nextLine());

				logger.info("Enter Phone Number: ");
				employee.setPhone(scanner.nextLine());

				logger.info("Enter Date of Birth (dd-mm-yyyy): ");

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");

				Date date;
				long millis = 0;

				date = dateFormat.parse(scanner.nextLine());
				millis = date.getTime();

				employee.setdOB(millis);

				logger.info("Enter Gender: ");
				employee.setGender(scanner.nextLine());

				logger.info("Enter Password: ");
				employee.setPassword(scanner.nextLine());

				logger.info("Enter Role:");
				employee.setRole(scanner.nextLine());

				EmployeeHelper employeeHelper = new EmployeeHelper();

				employeeHelper.addEmployee(employee);
				logger.warning("Employee Created ");
				exit = true;
			} catch (CustomBankException | ParseException e) {
				logger.warning(e.getMessage());
			}

			logger.info("Enter to exit");

		}

	}

	private void logActiveAccounts(int branchId) {

		try {
			EmployeeHelper employeeHelper = new EmployeeHelper();
			List<User> users = employeeHelper.getActiveAccounts(branchId);

			int size = users.size();
			logger.info("Active accounts of your branch : - ");
			if (size <= 0) {
				logger.info("There are no active accounts !");

			} else {

				for (User user : users) {

					logger.info(user.toString());

				}
			}
		} catch (CustomBankException e) {
			logger.severe(e.getMessage());
		}
	}

	private void logInactiveAccounts(int branchId){
		try {
			EmployeeHelper employeeHelper = new EmployeeHelper();
			List<User> users = employeeHelper.getInactiveAccounts(branchId);

			int size = users.size();

			logger.info("Inactive accounts are : - ");
			if (size <= 0) {
				logger.info("No Inactive accounts !");

			} else {

				for (User user : users) {

					logger.info(user.toString());

				}
			}
		} catch (CustomBankException e) {
			logger.severe(e.getMessage());
		}

	}

	private void logBranches() throws CustomBankException {

		boolean exit = false;
		while(!exit) {
			
			try {
			
		BranchHelper branchHelper = new BranchHelper();
		logger.info("All Branches :");

		List<Branch> branches = branchHelper.getAllBranches();
		int size = branches.size();

		for (Branch branch : branches) {

			logger.info(branch.toString());
		}

		if (size == 0) {
			logger.info("No Branches Available");
			logger.info("0. Create Branch.");
		} else {
			int i=0;
			logger.info(i+++". Create Branch.");
			logger.info(i+++". See employees of branch");
			logger.info(i+++". See active accounts of branch");
			logger.info(i+++". See inactive accounts of branch");
			logger.info(i+++". update branch");
			logger.info("-1. ");

		}

		int choice = scanner.nextInt();
		scanner.nextLine();

		switch (choice) {
		case 0:{
			
			createBranch();
		}
		case 1:{
			logger.info("Enter branch id  :");
			logEmployees(scanner.nextInt());
			scanner.nextLine();
			break;}
		case 2:{
			logger.info("Enter branch id  :");
			logActiveAccounts(scanner.nextInt());
			scanner.nextLine();
			break;
		}
		case 3:{
			logger.info("Enter branch id  :");
			logInactiveAccounts(scanner.nextInt());
			scanner.nextLine();
			break;
		}
		case 4:{
			logger.info("Enter branch id  :");
			logActiveAccounts(scanner.nextInt());
			scanner.nextLine();
			break;
		}
		case -1:{
			exit = true;
			break;
		}
		default:{
			logger.warning("Enter correct option !");
			break;}
		}
			}catch (InputMismatchException e) {
				
				logger.severe("Invalid input !");
				scanner.next();
			}
			catch( CustomBankException e) {
				logger.severe(e.getMessage());
				scanner.next();
			}
		
		}

	}

	private void createBranch() {
		
		boolean exit = false;
		
		while(!exit) {
		try {
		
		BranchHelper branchHelper = new BranchHelper();
		
		logger.finer("Branch Creation :");
		
		Branch branch = new Branch();
		
		logger.info("Enter branch name :");
		branch.setName(scanner.nextLine());
		
		logger.info("Enter branch address :");
		branch.setAddress(scanner.nextLine());
		
		
		branchHelper.addBranch(branch);
		exit = true;
		}
		catch(CustomBankException e) {
			logger.warning(e.getMessage());
			scanner.next();
		}
		catch(InputMismatchException e) {
			logger.severe("Invalid input !");
			scanner.next();
		}}
		
		
	}

	private void logEmployees(int branchId) throws CustomBankException {

		boolean exit = false;
		
		while(!exit) {
			
			try {
		
		EmployeeHelper employeeHelper = new EmployeeHelper();
		logger.info("Employees are :");

		List<Employee> employees = employeeHelper.getEmployees(branchId);

		int size = employees.size();

		for (Employee employee : employees) {

			logger.info(employee.toString());

		}
		if (size == 0) {
			logger.info("No Employees Available");
			logger.info("0. Create Employee.");
		} else {
			int i=0;
			logger.info(i+++". Create Employee.");
			logger.info("-1. Exit.");

		}

		int choice = scanner.nextInt();
		scanner.nextLine();
		
		switch (choice) {
		case 0 :{
			
			createEmployee();
			
			break;
			}
		
		case -1 :{
			
			exit = true;
			
			break;
			}
		
		default:{
			logger.warning("Enter correct option !");
			break;}
		}

			}catch(InputMismatchException e) {
				logger.severe("Invalid input !");
				scanner.next();
			}catch (CustomBankException e) {
				
				logger.severe(e.getMessage());
				scanner.next();
			}
		}
	}

	private void logProfile() {

		logger.info("\n");
		logger.info("User ID: " + employee.getId());
		logger.info("Name: " + employee.getName());
		logger.info("Email: " + employee.getEmail());
		logger.info("Phone: " + employee.getPhone());
		logger.info("DOB: " + employee.getdOB());
		logger.info("Gender: " + employee.getGender());
		logger.info("Password: " + employee.getPassword());
		logger.info("User Type: " + employee.getUserType());
		logger.info("Status: " + employee.getStatus());
		logger.info("\n");
	}

}
