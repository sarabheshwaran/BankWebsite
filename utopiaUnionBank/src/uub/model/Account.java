package uub.model;

public class Account {
	
	private int accNo;
	private int userId;
	private int branchId;
	private String type;
	private double balance;
	private String status;
	
	
	
	@Override
	public String toString() {
		return "\nAccount [accNo=" + accNo + ", userId=" + userId + ", branchId=" + branchId + ", type=" + type
				+ ", balance=" + balance + ", status=" + status + "]";
	}
	
	
	public int getAccNo() {
		return accNo;
	}
	public void setAccNo(int accNo) {
		this.accNo = accNo;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String string) {
		this.status = string;
	}
	
	

}
