package uub.model;

public class Employee extends User{

	
	private String role;
	private int branchId;
	
	
	public Employee() {
		super();
		super.setUserType("Employee");
	}


	public Employee(int id, String name, String email, String phone, long dOB, String gender, String password,
			String userType, String status,String role, int branchId) {
		super(id, name, email, phone, dOB, gender, password, userType, status);
		
		this.branchId= branchId;
		this.role = role;
	}


	public String toString() {
		return  "Employee ["+ toString(0)+", role=" + role + ", branchId=" + branchId + "]";
	}
	
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	
	
}
