package uub.presentation;

import java.util.Date;
import java.util.InputMismatchException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import uub.logicalLayer.AccountHelper;
import uub.logicalLayer.AdminHelper;
import uub.logicalLayer.BranchHelper;
import uub.logicalLayer.EmployeeHelper;
import uub.model.Account;
import uub.model.Branch;
import uub.model.Customer;
import uub.model.Employee;
import uub.model.User;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.DateUtils;

public class EmployeePage extends Runner {

	private static Employee employee;

	public EmployeePage(int id) throws CustomBankException {

		EmployeeHelper employeeHelper = new EmployeeHelper();

		employee = employeeHelper.getEmployee(id);
		String role = employee.getRole();

		boolean exit = false;

		if (role.equals("Admin")) {

			while (!exit) {

				try {
					int i = 1;

					logger.fine("Employee Portal");
					logger.info("Please Select an option : -");
					logger.info(i++ + ". See Profile.");
					logger.info(i++ + ". See All Branches.");
					logger.info(i+++". Activate account");
					logger.info(i+++". Dectivate account");
					logger.info(i+++". Activate user");
					logger.info(i+++". Dectivate user");
					logger.info(i++ + ". See All Employees.");
					logger.info(i++ + ". Add Customer.");
					logger.info(i++ + ". Add Employee.");
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

						logger.info("BranchList : ");
						logBranches();
						logger.warning("Press Enter to Exit");
						scanner.nextLine();

						break;
					}
					case 3: {

						activateAcc();
						logger.warning("Press Enter to Exit");
						scanner.nextLine();
						break;
					}
					case 4: {

						inActivateAcc();
						logger.warning("Press Enter to Exit");
						scanner.nextLine();
						break;
					}
					case 5: {

						activateUser();
						logger.warning("Press Enter to Exit");
						scanner.nextLine();
						break;
					}
					case 6: {

						inActivateUser();
						logger.warning("Press Enter to Exit");
						scanner.nextLine();
						break;
					}
					case 7: {

						createCustomer();
						logger.warning("Press Enter to Exit");
						scanner.nextLine();
						break;
					}
					case 8:{
						createEmployee();
						logger.warning("Press Enter to Exit");
						scanner.nextLine();
						break;
					}
					case 9: {

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
					logger.info(i+++". Activate account");
					logger.info(i+++". Dectivate account");
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

						activateAcc();
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

					case 4: {
						createAccount(employee.getBranchId());
						logger.warning("Press Enter to Exit");
						scanner.nextLine();

						break;
					}
					case 5: {

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

	private void activateUser() throws CustomBankException {
		AdminHelper adminHelper = new AdminHelper();

		boolean exit = false;

		while (!exit) {
			try {

				logger.info("Enter the userId to activate :");
				int id = scanner.nextInt();

				adminHelper.activateUser(id);
				exit = true;
			} catch (CustomBankException e) {
				logger.severe(e.getMessage());
			}
		}
	}

	private void inActivateUser() throws CustomBankException {
		AdminHelper adminHelper = new AdminHelper();

		boolean exit = false;

		while (!exit) {
			try {

				logger.info("Enter the userId to deactivate :");
				int id = scanner.nextInt();

				adminHelper.deActivateUser(id);
				exit = true;
			} catch (CustomBankException e) {
				logger.severe(e.getMessage());
			}
		}
	}

	private void activateAcc() throws CustomBankException {
		EmployeeHelper employeeHelper = new EmployeeHelper();

		boolean exit = false;

		while (!exit) {
			try {

				logger.info("Enter the accNo to activate :");
				int accNo = scanner.nextInt();

				employeeHelper.activateAcc(accNo);
				exit = true;
			} catch (CustomBankException e) {
				logger.severe(e.getMessage());
			}
		}
	}

	private void inActivateAcc() throws CustomBankException {
		EmployeeHelper employeeHelper = new EmployeeHelper();

		boolean exit = false;

		while (!exit) {
			try {

				logger.info("Enter the accNo to deactivate :");
				int accNo = scanner.nextInt();

				employeeHelper.deActivateAcc(accNo);
				exit = true;
			} catch (CustomBankException e) {
				logger.severe(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void logInactiveCustomers() throws CustomBankException {

		boolean exit = false;
		while (!exit) {
			try {
				EmployeeHelper employeeHelper = new EmployeeHelper();

				List<User> users = employeeHelper.getInactiveCustomers(10,0);

				int size = users.size();

				logger.info("Inactive Customers : ");
				for (int i = 0; i < size; i++) {
					logger.info(i + " - " + users.get(i));

				}

				if (size == 0) {
					logger.info("No inactive Customers !");
				}
				int i = 0;
				logger.info(i++ + ". Activate Customer");
				logger.info("-1. exit");
				int choice = scanner.nextInt();
				scanner.nextLine();
				switch (choice) {
				case 0: {
					inActivateUser();
					logger.warning("Press Enter to Exit");
					scanner.nextLine();
					break;
				}
				case -1: {
					exit = true;
					break;
				}

				default: {
					logger.warning("Enter correct option");
					logger.warning("Press Enter to Exit");
					scanner.nextLine();
					break;
				}
				}
			} catch (CustomBankException e) {
				logger.severe(e.getMessage());
			} catch (InputMismatchException e) {
				logger.severe("Enter valid input!");
				scanner.next();
			}
		}

	}

	private void logInactiveEmployees() throws CustomBankException {

		boolean exit = false;
		while (!exit) {
			try {
				AdminHelper adminHelper = new AdminHelper();

				List<User> users = adminHelper.getInactiveEmployees(10,0);

				int size = users.size();

				logger.info("Inactive Employees : ");
				for (int i = 0; i < size; i++) {
					logger.info(i + " - " + users.get(i));

				}

				if (size == 0) {
					logger.info("No inactive Employees !");
				}
				int i = 0;
				logger.info(i++ + ". Activate Employee");
				logger.info("-1. exit");
				int choice = scanner.nextInt();
				scanner.nextLine();
				switch (choice) {
				case 0: {
					activateUser();
					logger.warning("Press Enter to Exit");
					scanner.nextLine();
					break;
				}
				case -1: {
					exit = true;
					break;
				}

				default: {
					logger.warning("Enter correct option");
					logger.warning("Press Enter to Exit");
					scanner.nextLine();
					break;
				}
				}
			} catch (CustomBankException e) {
				logger.severe(e.getMessage());
			} catch (InputMismatchException e) {
				logger.severe("Enter valid input!");
				scanner.next();
			}
		}

	}

	private void logActiveEmployees() throws CustomBankException {

		boolean exit = false;
		while (!exit) {
			try {
				AdminHelper adminHelper = new AdminHelper();

				List<User> users = adminHelper.getActiveEmployees(10,0);

				int size = users.size();

				logger.info("Active Employees : ");
				for (int i = 0; i < size; i++) {
					logger.info(i + " - " + users.get(i));

				}

				if (size == 0) {
					logger.info("No active Employees !");
				}
				int i = 0;

				logger.info(i++ + ". Add Employee");
				logger.info(i++ + ". Deactivate Employee");
				logger.info("-1. exit");
				int choice = scanner.nextInt();
				scanner.nextLine();
				switch (choice) {
				case 0: {
					createEmployee();
					logger.warning("Press Enter to Exit");
					scanner.nextLine();
					;
				}
				case 1: {
					inActivateUser();
					logger.warning("Press Enter to Exit");
					scanner.nextLine();
					break;
				}
				case -1: {
					exit = true;
					break;
				}

				default: {
					logger.warning("Enter correct option");
					break;
				}
				}
			} catch (CustomBankException e) {
				logger.severe(e.getMessage());
			} catch (InputMismatchException e) {
				logger.severe("Enter valid input!");
				scanner.next();
			}
		}

	}

	private void logActiveCustomers() throws CustomBankException {

		boolean exit = false;
		while (!exit) {
			try {
				EmployeeHelper employeeHelper = new EmployeeHelper();

				List<User> users = employeeHelper.getActiveCustomers(10,0);

				int size = users.size();

				logger.info("Active Customers : ");
				for (int i = 0; i < size; i++) {
					logger.info(i + " - " + users.get(i));

				}

				if (size == 0) {
					logger.info("No active Customers !");
				}
				int i = 0;
				logger.info(i++ + ". Add Customers");
				logger.info(i++ + ". Deactivate Customer");
				logger.info("-1. exit");
				int choice = scanner.nextInt();
				scanner.nextLine();
				switch (choice) {
				case 0: {
					createCustomer();
					logger.warning("Press Enter to Exit");
					scanner.nextLine();
					;
				}
				case 1: {
					inActivateUser();
					logger.warning("Press Enter to Exit");
					scanner.nextLine();
					break;
				}
				case -1: {
					exit = true;
					break;
				}

				default: {
					logger.warning("Enter correct option");
					break;
				}
				}
			} catch (CustomBankException e) {
				logger.severe(e.getMessage());
			} catch (InputMismatchException e) {
				logger.severe("Enter valid input!");
				scanner.next();
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

			} catch (CustomBankException e) {
				logger.severe("Account Creation Failed : " + e.getMessage());
			} catch (InputMismatchException e) {
				logger.severe("Enter valid input!");
				scanner.next();
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


				
				String dateString = scanner.nextLine();
				
				long millis = DateUtils.formatDate(DateUtils.formatDateString(dateString));
				

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

			} catch (CustomBankException  e) {
				logger.warning(e.getMessage());
			} catch (InputMismatchException e) {
				logger.severe("Enter valid input!");
				scanner.next();
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

				AdminHelper adminHelper = new AdminHelper();

				adminHelper.addEmployee(employee);
				logger.warning("Employee Created ");
				exit = true;
			} catch (CustomBankException | ParseException e) {
				logger.warning(e.getMessage());
				scanner.nextLine();
			} catch (InputMismatchException e) {
				logger.severe("Enter valid input!");
				scanner.next();
			}

			logger.info("Enter to exit");

		}

	}

	private void logActiveAccounts(int branchId) {

		boolean exit = false;

		while (!exit) {

			try {
				EmployeeHelper employeeHelper = new EmployeeHelper();
				
				logger.info("Enter page no.");
				int p = scanner.nextInt();
				scanner.nextLine();
				
				Map<Integer, List<Account>> accounts = employeeHelper.getActiveAccounts(branchId,20,(p-1)*20);

				int size = accounts.size();

				logger.info("Active accounts of your branch : - ");
				if (size <= 0) {
					logger.info("There are no active accounts !");

				} else {

					for (Map.Entry<Integer, List<Account>> a : accounts.entrySet()) {

						logger.info(a.toString());
					}
					int i=0;
					logger.info(i++ + ". Add Account");
					logger.info(i++ + ". Deactivate Account");
					logger.info("-1. exit");
					int choice = scanner.nextInt();
					scanner.nextLine();
					switch (choice) {
					case 0: {
						createAccount(branchId);
						logger.warning("Press Enter to Exit");
						scanner.nextLine();
						
					}
					case 1: {
						inActivateAcc();
						logger.warning("Press Enter to Exit");
						scanner.nextLine();
						break;
					}
					case -1: {
						exit = true;
						break;
					}

					default: {
						logger.warning("Enter correct option");
						break;
					}
					}
				}
			} catch (CustomBankException e) {
				logger.severe(e.getMessage());
			}

		}
	}

	private void logInactiveAccounts(int branchId) {
		
		boolean exit = false;
		
		while(!exit) {
		try {
			EmployeeHelper employeeHelper = new EmployeeHelper();
			
			logger.info("Enter page no.");
			int p = scanner.nextInt();
			scanner.nextLine();
			
			Map<Integer, List<Account>> accounts = employeeHelper.getInactiveAccounts(branchId,20,(p-1)*20);

			int size = accounts.size();

			logger.info("Inactive accounts are : - ");
			if (size <= 0) {
				logger.info("No Inactive accounts !");

			} else {

				logger.info(accounts.toString());
			}
			int i=0;
			logger.info(i++ + ". Activate Account");
			logger.info("-1. exit");
			int choice = scanner.nextInt();
			scanner.nextLine();
			switch (choice) {

			case 0: {
				activateAcc();
				logger.warning("Press Enter to Exit");
				scanner.nextLine();
				break;
			}
			case -1: {
				exit = true;
				break;
			}

			default: {
				logger.warning("Enter correct option");
				break;
			}
			}
		} catch (CustomBankException e) {
			logger.severe(e.getMessage());
		}
	}
		

	}

	private void logBranches() throws CustomBankException {

		boolean exit = false;
		while (!exit) {

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
				}
				int i = 0;
				logger.info(i++ + ". Create Branch.");
				logger.info(i++ + ". See employees of branch");
				logger.info(i++ + ". See active accounts of branch");
				logger.info(i++ + ". See inactive accounts of branch");
				logger.info(i++ + ". update branch");
				logger.info("-1. Exit.");

				int choice = scanner.nextInt();
				scanner.nextLine();

				switch (choice) {
				case 0: {

					createBranch();
				}
				case 1: {
					logger.info("Enter branch id  :");
					logEmployees(scanner.nextInt());
					scanner.nextLine();
					break;
				}
				case 2: {
					logger.info("Enter branch id  :");
					logActiveAccounts(scanner.nextInt());
					scanner.nextLine();
					logger.info("Press Enter to exit");
					scanner.nextLine();
					break;
				}
				case 3: {
					logger.info("Enter branch id  :");
					logInactiveAccounts(scanner.nextInt());
					logger.info("Press Enter to exit");
					scanner.nextLine();
					scanner.nextLine();
					break;
				}
				case 4: {
					logger.info("Enter branch id  :");

					logger.info("Press Enter to exit!");
					scanner.nextLine();
					break;
				}
				case -1: {
					exit = true;
					break;
				}
				default: {
					logger.warning("Enter correct option !");
					break;
				}
				}
			} catch (InputMismatchException e) {

				logger.severe("Invalid input !");
				scanner.next();
			} catch (CustomBankException e) {
				logger.severe(e.getMessage());
				scanner.next();
			}

		}

	}

	private void createBranch() {

		boolean exit = false;

		while (!exit) {
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
			} catch (CustomBankException e) {
				logger.warning(e.getMessage());
				scanner.next();
			} catch (InputMismatchException e) {
				logger.severe("Invalid input !");
				scanner.next();
			}
		}

	}

	private void logEmployees(int branchId) throws CustomBankException {

		boolean exit = false;

		while (!exit) {

			try {

				AdminHelper adminHelper = new AdminHelper();
				logger.info("Employees are :");

				Map<Integer, List<Employee>> employees = adminHelper.getEmployees(branchId,20,0);

				int size = employees.size();

				for (Map.Entry<Integer, List<Employee>> employee : employees.entrySet()) {

					logger.info(employee.toString());

				}
				if (size == 0) {
					logger.info("No Employees Available");
				}
				int i = 0;
				logger.info(i++ + ". Create Employee.");
				logger.info("-1. Exit.");

				int choice = scanner.nextInt();
				scanner.nextLine();

				switch (choice) {
				case 0: {

					createEmployee();
					logger.warning("Press Enter to Exit");
					scanner.nextLine();
					break;
				}

				case -1: {

					exit = true;

					break;
				}

				default: {
					logger.warning("Enter correct option !");
					break;
				}
				}

			} catch (InputMismatchException e) {
				logger.severe("Invalid input !");
				scanner.next();
			} catch (CustomBankException e) {

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
		logger.info("DOB: " + DateUtils.formatDate( employee.getdOB()));
		logger.info("Gender: " + employee.getGender());
		logger.info("User Type: " + employee.getUserType());
		logger.info("Status: " + employee.getStatus());
		logger.info("\n");
	}

}
