package uub.model;

import uub.enums.EmployeeRole;
import uub.enums.UserStatus;
import uub.enums.UserType;

public class Employee extends User{

	
	private EmployeeRole role;
	private int branchId;
	
	
	public Employee() {
		super();
		super.setUserType(UserType.EMPLOYEE);
	}


	public Employee(int id, String name, String email, String phone, long dOB, String gender, String password,
			UserType userType, UserStatus status,EmployeeRole role, int branchId) {
		super(id, name, email, phone, dOB, gender, password, userType, status);
		
		this.branchId= branchId;
		this.role = role;
	}


	public String toString() {
		return  "Employee ["+ toString(0)+", role=" + role + ", branchId=" + branchId + "]";
	}
	
	
	public EmployeeRole getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = EmployeeRole.valueOf(role);
	}
	public void setRole(EmployeeRole role) {
		this.role = role;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	
	
}
