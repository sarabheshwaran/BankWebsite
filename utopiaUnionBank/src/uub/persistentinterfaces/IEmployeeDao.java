package uub.persistentinterfaces;

import java.util.List;
import java.util.Map;

import uub.model.Employee;
import uub.staticlayer.CustomBankException;

public interface IEmployeeDao {

	public void addEmployee(List<Employee> employees) throws CustomBankException;

	void updateEmployee(Employee employee) throws CustomBankException;

	List<Employee> getEmployees(int id) throws CustomBankException;

	Map<Integer, Employee> getEmployeesWithBranch(int branchId, int limit, int offSet) throws CustomBankException;

}