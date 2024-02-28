package uub.persistentLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uub.model.Employee;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HelperUtils;

public class EmployeeDao implements IEmployeeDao{

	private Connection connection;
	
	public EmployeeDao()throws CustomBankException {
		connection = ConnectionManager.getConnection();
	}
	
	
	private static final StringBuilder getQuery2 = new StringBuilder("SELECT * FROM EMPLOYEE JOIN USER ON EMPLOYEE.ID = USER.ID ");

	private static final String addQuery1 = "INSERT INTO USER (NAME,EMAIL,PHONE,DOB,GENDER,PASSWORD,USER_TYPE,STATUS) VALUES (?,?,?,?,?,?,?,?)";
	private static final String addQuery2 = "INSERT INTO EMPLOYEE VALUES (?,?,?)";
	
	
	
	
	
	@Override
	public Employee getEmployees(int id) throws CustomBankException {

		String getQuery = "SELECT * FROM EMPLOYEE JOIN USER ON EMPLOYEE.ID = USER.ID WHERE EMPLOYEE.ID = " + id;

		List<Employee> employees =  getEmployees(getQuery);
		
		if(!employees.isEmpty()) {
			return employees.get(0);
		}else {
			return null;
		}

	}

	@Override
	public Employee getEmployeesWithEmail(String email) throws CustomBankException {

		String getQuery = "SELECT * FROM EMPLOYEE JOIN USER ON EMPLOYEE.ID = USER.ID WHERE EMAIL = '" + email+"'";

		List<Employee> employees =  getEmployees(getQuery);
		
		if(!employees.isEmpty()) {
			return employees.get(0);
		}else {
			return null;
		}

	}
	
	@Override
	public List<Employee> getEmployeesWithBranch(int branchId) throws CustomBankException {

		String getQuery = "SELECT * FROM EMPLOYEE JOIN USER ON EMPLOYEE.ID = USER.ID WHERE EMPLOYEE.BRANCH_ID = " + branchId;

		return getEmployees(getQuery);

	}

	
	private List<Employee> getEmployees(String query) throws CustomBankException {
		
		
		
		List<Employee> employees = new ArrayList<Employee>();
		
		
		
		try(Statement statement = connection.createStatement()) {
			
			ResultSet resultSet = statement.executeQuery(query);
			
			while(resultSet.next()) {
				
				Employee ResultEmployee = mapEmployee(resultSet);
				employees.add(ResultEmployee);
			}
			
			
			
		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}
		
		return employees;
	}
	

	
	@Override
	public List<Employee> getEmployees() throws CustomBankException {
		
		
		List<Employee> employees = new ArrayList<Employee>();
		
		
		
		try(PreparedStatement statement = connection.prepareStatement(getQuery2.toString())) {
			
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				
				Employee employee = mapEmployee(resultSet);
				employees.add(employee);
			}
			
			
			
		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}
		
		return employees;
	}

	@Override
	public void addEmployee(List<Employee> employees) throws CustomBankException {
		
		
		try(PreparedStatement statement = connection.prepareStatement(addQuery1,PreparedStatement.RETURN_GENERATED_KEYS);
				PreparedStatement statement2 = connection.prepareStatement(addQuery2)) {
			
			connection.setAutoCommit(false);
			for(Employee employee : employees) {
			HelperUtils.setParameter(statement, 1, employee.getName());
			HelperUtils.setParameter(statement, 2, employee.getEmail());
			HelperUtils.setParameter(statement, 3, employee.getPhone());
			HelperUtils.setParameter(statement, 4, employee.getdOB());
			HelperUtils.setParameter(statement, 5, employee.getGender());
			HelperUtils.setParameter(statement, 6, employee.getPassword());
			HelperUtils.setParameter(statement, 7, employee.getUserType());
			HelperUtils.setParameter(statement, 8, employee.getStatus());
			statement.addBatch();
			}
			statement.executeBatch();
			
				
				try(ResultSet resultSet = statement.getGeneratedKeys()){
					int index = 0;
					while(resultSet.next()) {
					
                        int id = resultSet.getInt(1);
    					Employee employee = employees.get(index);
    						HelperUtils.setParameter(statement2, 1, id);
    						HelperUtils.setParameter(statement2, 2, employee.getRole());
    						HelperUtils.setParameter(statement2, 3, employee.getBranchId());
    				
    					    statement2.addBatch();
    				index++;	
					}
					
					statement2.executeBatch();
                   
					connection.commit();
				}
				
			
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new CustomBankException(e.getMessage());
				
			}
			throw new CustomBankException(e.getMessage());
		}
		
	}

	@Override
	public void updateEmployee(Employee employee) throws CustomBankException {
		
		StringBuilder updateQuery = new StringBuilder("UPDATE EMPLOYEE JOIN USER ON USER.ID = EMPLOYEE.ID SET  ");
		updateQuery.append(getFieldList(employee)).append("WHERE ID = ?");
		
		
		try(PreparedStatement statement = connection.prepareStatement(updateQuery.toString())) {
			
			setValues(statement, employee);
			
			statement.executeUpdate();
			
			
		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}
		

		
	}
	


	private String getFieldList(Employee employee ) {
		
		
		StringBuilder queryBuilder = new StringBuilder("  ");
        
		if (employee.getId() != 0) {
		    queryBuilder.append("EMPLOYEE.ID = ? , " );
		}
		if (employee.getName() != null) {
		    queryBuilder.append("NAME = ? , " );
		}
		if (employee.getEmail() != null) {
		    queryBuilder.append("EMAIL = ? , " );
		}
		if (employee.getPhone() != null) {
		    queryBuilder.append("PHONE = ? , " );
		}
		if (employee.getdOB() != 0) {
		    queryBuilder.append("DOB = ? , " );
		}
		if (employee.getGender() != null) {
		    queryBuilder.append("GENDER = ? , " );
		}
		if (employee.getPassword() != null) {
		    queryBuilder.append("PASSWORD = ?, " );
		}
		if (employee.getUserType() != null) {
		    queryBuilder.append("USER_TYPE = ? , " );
		}
		if (employee.getStatus() != null) {
		    queryBuilder.append("STATUS = ? , " );
		}
		if (employee.getRole() != null) {
		    queryBuilder.append("ROLE = ? , " );
		}
		if (employee.getBranchId() != 0) {
		    queryBuilder.append("BRANCH_ID = ? , " );
		}


		 queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
		 return queryBuilder.toString();
		
	}
	
	
	private void setValues(PreparedStatement statement, Employee employee) throws SQLException {
		
		int index = 1;
		
		
		if (employee.getName() != null) {
		    statement.setObject(index++, employee.getName());
		}
		if (employee.getEmail() != null) {
		    statement.setObject(index++, employee.getEmail());
		}
		if (employee.getPhone() != null) {
		    statement.setObject(index++, employee.getPhone());
		}
		if (employee.getdOB() != 0) {
		    statement.setLong(index++, employee.getdOB());
		}
		if (employee.getGender() != null) {
		    statement.setObject(index++, employee.getGender());
		}
		if (employee.getPassword() != null) {
		    statement.setObject(index++, employee.getPassword());
		}
		if (employee.getUserType() != null) {
		    statement.setObject(index++, employee.getUserType());
		}
		if (employee.getStatus() != null) {
		    statement.setObject(index++, employee.getStatus());
		}
		if (employee.getRole() != null) {
		    statement.setObject(index++, employee.getRole());
		}
		if (employee.getBranchId() != 0) {
		    statement.setObject(index++, employee.getBranchId());
		}
		if (employee.getId() != 0) {
		    statement.setObject(index++, employee.getId());
		}

		
		
	}
	
	
	private Employee mapEmployee(ResultSet resultSet) throws SQLException {
		
		Employee employee = new Employee();
		
		employee.setId(resultSet.getInt("EMPLOYEE.ID"));
		employee.setName(resultSet.getString("USER.NAME"));
		employee.setEmail(resultSet.getString("USER.EMAIL"));
		employee.setPhone(resultSet.getString("USER.PHONE"));
		employee.setdOB(resultSet.getLong("USER.DOB"));
		employee.setGender(resultSet.getString("USER.GENDER"));
		employee.setPassword(resultSet.getString("USER.PASSWORD"));
		employee.setUserType(resultSet.getString("USER.USER_TYPE"));
		employee.setStatus(resultSet.getString("USER.STATUS"));
		employee.setRole(resultSet.getString("EMPLOYEE.ROLE"));
		employee.setBranchId(resultSet.getInt("EMPLOYEE.BRANCH_ID"));
		
		
		return employee;
		
		
	}



}
