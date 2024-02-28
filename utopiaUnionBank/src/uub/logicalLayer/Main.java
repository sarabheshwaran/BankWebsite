package uub.logicalLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import uub.staticLayer.HelperUtils;

class Employee {
    String name;

    public Employee(String name) {
        this.name = name;
    }
}

class Branch {
    String name;
    List<Employee> employees;

    public Branch(String name) {
        this.name = name;
        this.employees = new ArrayList<>();
    }
}

class AdminDetails {
    String name;

    public AdminDetails(String name) {
        this.name = name;
    }
}

class Admin {
    String name;
    AdminDetails adminDetails;
    List<Branch> branches;

    public Admin(String name) {
        this.name = name;
        this.adminDetails = new AdminDetails(name);
        this.branches = new ArrayList<>();
    }
}

public class Main {
	
	public static Logger logger = Logger.getGlobal();
	
	static {
		
		HelperUtils.formatLogger();
	
	
		logger.setLevel(Level.FINER);
	
	}
	

	public static void printOrganizationTree(Object node, String indent, boolean last) {
        System.out.print(indent);
        if (last) {
            System.out.print("└─ ");
            indent += "   ";
        } else {
            System.out.print("├─ ");
            indent += "│  ";
        }

        if (node instanceof Admin) {
            logger.warning("Admin: " + ((Admin) node).name);
            printOrganizationTree(((Admin) node).adminDetails, indent, ((Admin) node).branches.isEmpty());
            for (int i = 0; i < ((Admin) node).branches.size(); i++) {
                printOrganizationTree(((Admin) node).branches.get(i), indent, i == ((Admin) node).branches.size() - 1);
            }
        } else if (node instanceof AdminDetails) {
            logger.warning("Admin Details: " + ((AdminDetails) node).name);
        } else if (node instanceof Branch) {
            logger.warning("Branch: " + ((Branch) node).name);
            for (int i = 0; i < ((Branch) node).employees.size(); i++) {
                printOrganizationTree(((Branch) node).employees.get(i), indent, i == ((Branch) node).employees.size() - 1);
            }
        } else if (node instanceof Employee) {
            logger.warning("Employee: " + ((Employee) node).name);
        }
    }

    public static void main(String[] args) {
        Admin admin1 = new Admin("Admin1");
        admin1.adminDetails = new AdminDetails("AdminDetails1");

        Branch branch1 = new Branch("Branch1");
        branch1.employees.add(new Employee("Employee1"));
        branch1.employees.add(new Employee("Employee2"));

        admin1.branches.add(branch1);

        printOrganizationTree(admin1, "",true);
    	
    }
}
