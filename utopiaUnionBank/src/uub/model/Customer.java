package uub.model;


public class Customer extends User{

	private String aadhar;
	private String pAN;
	private String address;
	
	
	
	public Customer() {
		super();
		super.setUserType("Customer");
		
	};
	
	public Customer(int id, String name, String email, String phone, long dOB, String gender, String password,
			String userType, String status, String aadhar, String pAN, String address) {
		super(id, name, email, phone, dOB, gender, password, userType, status);
		this.aadhar = aadhar;
		this.pAN = pAN;
		this.address = address;
		
	}
	public String toString() {
		return "Customer ["+ toString(0) +", aadhar=" + aadhar + ", pAN=" + pAN + ", address=" + address + "]";
	}
	public String getAadhar() {
		return aadhar;
	}
	public void setAadhar(String aadhar) {
		this.aadhar = aadhar;
	}
	public String getpAN() {
		return pAN;
	}
	public void setpAN(String pAN) {
		this.pAN = pAN;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
	
}
