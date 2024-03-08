package uub.model;


import uub.enums.UserStatus;
import uub.enums.UserType;

public class User {

	
	private int id;
	private String name;
	private String email;
	private String phone;
	private long dOB;
	private String gender;
	private String password;
	private UserType userType;
	private UserStatus status;
	
	
	
	public User() {}
	public User(int id, String name, String email, String phone, long dOB, String gender, String password,
			UserType userType, UserStatus status) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.dOB = dOB;
		this.gender = gender;
		this.password = password;
		this.userType = userType;
		this.status = status;
	}
	
	

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", dOB=" + dOB
				+ ", gender=" + gender +", userType=" + userType + ", status=" + status
				+ ", accounts=    " ;
	}
	public String toString(int i) {
		return "id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", dOB=" + dOB
				+ ", gender=" + gender +  ", userType=" + userType + ", status=" + status;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public long getdOB() {
		return  dOB;
	}
	public void setdOB(long dOB) {
		this.dOB = dOB;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserType getUserType() {
		return userType;
	}
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	public void setUserType(int type) {
		this.userType = UserType.valueOf(type);
	}
	public UserStatus getStatus() {
		return status;
	}
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	public void setStatus(int status) {
		this.status = UserStatus.valueOf(status);
	}

	
	
}
