package uub.model;

public class Branch {

	private int id;
	private String iFSC;
	private String name;
	private String address;

	public Branch() {
	}

	public Branch(int id, String iFSC, String name, String address) {
		super();
		this.id = id;
		this.iFSC = iFSC;
		this.name = name;
		this.address = address;
	}

	@Override
	public String toString() {
		return "Branch [id=" + id + ", iFSC=" + iFSC + ", name=" + name + ", address=" + address + "]\n";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getiFSC() {
		return iFSC;
	}

	public void setiFSC(String iFSC) {
		this.iFSC = iFSC;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
