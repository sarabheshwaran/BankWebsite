package uub.persistentLayer;

import java.util.List;
import java.util.Map;

import uub.model.Employee;
import uub.staticLayer.CustomBankException;

public interface IEmployeeDao {

	

	public List<Employee> getEmployees() throws CustomBankException;

	public void addEmployee(List<Employee> employees) throws CustomBankException;

	void updateEmployee(Employee employee) throws CustomBankException;


	List<Employee> getEmployees(int id) throws CustomBankException;

	List<Employee> getEmployeesWithEmail(String email) throws CustomBankException;


	Map<Integer, List<Employee>> getEmployeesWithBranch(int branchId) throws CustomBankException;




}
