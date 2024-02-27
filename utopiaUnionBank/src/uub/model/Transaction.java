package uub.model;


public class Transaction {

	private int id;
	private int userId;
	private int accNo;
	private int transactionAcc;
	private String type;
	private double amount;
	private double openingBal;
	private double closingBal;
	private String desc;
	private long time;
	private String status;
	
	
	
	public Transaction() {
		
		this.openingBal = -1;
		this.closingBal = -1;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", userId=" + userId + ", accNo=" + accNo + ", transactionAcc="
				+ transactionAcc + ", type=" + type + ", amount=" + amount + ", openingBal=" + openingBal
				+ ", closingBal=" + closingBal + ", desc=" + desc + ", time=" + time + ", status=" + status + "]";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getAccNo() {
		return accNo;
	}
	public void setAccNo(int accNo) {
		this.accNo = accNo;
	}
	public int getTransactionAcc() {
		return transactionAcc;
	}
	public void setTransactionAcc(int transactionAcc) {
		this.transactionAcc = transactionAcc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getOpeningBal() {
		return openingBal;
	}
	public void setOpeningBal(double openingBal) {
		this.openingBal = openingBal;
	}
	public double getClosingBal() {
		return closingBal;
	}
	public void setClosingBal(double closingBal) {
		this.closingBal = closingBal;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
