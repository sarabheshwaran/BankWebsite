package uub.persistentLayer;

import java.util.List;

import uub.model.Employee;
import uub.staticLayer.CustomBankException;

public interface IEmployeeDao {

	

	public List<Employee> getEmployees() throws CustomBankException;

	public void addEmployee(List<Employee> employees) throws CustomBankException;



	void updateEmployee(Employee employee) throws CustomBankException;



	Employee getEmployees(int id) throws CustomBankException;

	Employee getEmployeesWithEmail(String email) throws CustomBankException;


	List<Employee> getEmployeesWithBranch(int branchId) throws CustomBankException;




}
