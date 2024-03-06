package uub.persistentinterfaces;

import java.sql.SQLException;
import java.util.List;

import uub.model.Customer;
import uub.staticLayer.CustomBankException;

public interface ICustomerDao {

	
	public int[] addCustomer(List<Customer> customers) throws CustomBankException, SQLException;
		
	
	void updateCustomer(Customer customer) throws CustomBankException;


	public List<Customer> getCustomers(int id) throws CustomBankException;


	List<Customer> getCustomersWithEmail(String email) throws CustomBankException;
}
