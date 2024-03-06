package uub.persistentlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uub.model.Employee;
import uub.persistentinterfaces.IEmployeeDao;
import uub.staticLayer.ConnectionManager;
import uub.staticLayer.CustomBankException;

public class EmployeeDao implements IEmployeeDao {

	@Override
	public List<Employee> getEmployees(int id) throws CustomBankException {

		String getQuery = "SELECT * FROM EMPLOYEE JOIN USER ON EMPLOYEE.ID = USER.ID WHERE EMPLOYEE.ID = " + id
				+ " AND STATUS = 'ACTIVE'";

		return getEmployees(getQuery);

	}

	@Override
	public Map<Integer, List<Employee>> getEmployeesWithBranch(int branchId, int limit, int offSet)
			throws CustomBankException {

		String getQuery = "SELECT * FROM EMPLOYEE JOIN USER ON EMPLOYEE.ID = USER.ID WHERE EMPLOYEE.BRANCH_ID = "
				+ branchId + " AND STATUS = 'ACTIVE'" + " LIMIT " + limit + " OFFSET " + offSet;

		return getEmployeesOfBranch(getQuery);

	}

	@Override
	public void addEmployee(List<Employee> employees) throws CustomBankException {

		Connection connection = ConnectionManager.getConnection();

		String addQuery1 = "INSERT INTO USER (NAME,EMAIL,PHONE,DOB,GENDER,PASSWORD,USER_TYPE,STATUS) VALUES (?,?,?,?,?,?,?,?)";
		String addQuery2 = "INSERT INTO EMPLOYEE VALUES (?,?,?)";

		try (PreparedStatement statement = connection.prepareStatement(addQuery1,
				PreparedStatement.RETURN_GENERATED_KEYS);
				PreparedStatement statement2 = connection.prepareStatement(addQuery2)) {

			connection.setAutoCommit(false);
			for (Employee employee : employees) {
				statement.setObject( 1, employee.getName());
				statement.setObject( 2, employee.getEmail());
				statement.setObject( 3, employee.getPhone());
				statement.setObject( 4, employee.getdOB());
				statement.setObject( 5, employee.getGender());
				statement.setObject( 6, employee.getPassword());
				statement.setObject( 7, employee.getUserType());
				statement.setObject( 8, employee.getStatus());
				statement.addBatch();
			}
			statement.executeBatch();

			try (ResultSet resultSet = statement.getGeneratedKeys()) {
				int index = 0;
				while (resultSet.next()) {

					int id = resultSet.getInt(1);
					Employee employee = employees.get(index);
					statement2.setObject( 1, id);
					statement2.setObject( 2, employee.getRole());
					statement2.setObject( 3, employee.getBranchId());

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
		} finally {
			try {
				connection.close();
			} catch (SQLException e1) {
				throw new CustomBankException(e1.getMessage());

			}
		}

	}

	@Override
	public void updateEmployee(Employee employee) throws CustomBankException {

		StringBuilder updateQuery = new StringBuilder("UPDATE EMPLOYEE JOIN USER ON USER.ID = EMPLOYEE.ID SET  ");
		updateQuery.append(getFieldList(employee)).append("WHERE ID = ?");

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(updateQuery.toString())) {

			setValues(statement, employee);
			statement.executeUpdate();

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

	}

	private List<Employee> getEmployees(String query) throws CustomBankException {

		List<Employee> employees = new ArrayList<Employee>();

		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {

				Employee ResultEmployee = mapEmployee(resultSet);
				employees.add(ResultEmployee);
			}

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

		return employees;
	}

	private Map<Integer, List<Employee>> getEmployeesOfBranch(String query) throws CustomBankException {

		Map<Integer, List<Employee>> employeeMap = new HashMap<Integer, List<Employee>>();

		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {

				Employee employee;

				int id = resultSet.getInt("ID");

				if (!employeeMap.containsKey(id)) {

					employeeMap.put(id, new ArrayList<Employee>());

				}
				id = resultSet.getInt("ID");
				employee = mapEmployee(resultSet);
				employeeMap.get(id).add(employee);
			}

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

		return employeeMap;
	}

	private String getFieldList(Employee employee) {

		StringBuilder queryBuilder = new StringBuilder("  ");

		if (employee.getId() != 0) {
			queryBuilder.append("EMPLOYEE.ID = ? , ");
		}
		if (employee.getName() != null) {
			queryBuilder.append("NAME = ? , ");
		}
		if (employee.getEmail() != null) {
			queryBuilder.append("EMAIL = ? , ");
		}
		if (employee.getPhone() != null) {
			queryBuilder.append("PHONE = ? , ");
		}
		if (employee.getdOB() != 0) {
			queryBuilder.append("DOB = ? , ");
		}
		if (employee.getGender() != null) {
			queryBuilder.append("GENDER = ? , ");
		}
		if (employee.getPassword() != null) {
			queryBuilder.append("PASSWORD = ?, ");
		}
		if (employee.getUserType() != null) {
			queryBuilder.append("USER_TYPE = ? , ");
		}
		if (employee.getStatus() != null) {
			queryBuilder.append("STATUS = ? , ");
		}
		if (employee.getRole() != null) {
			queryBuilder.append("ROLE = ? , ");
		}
		if (employee.getBranchId() != 0) {
			queryBuilder.append("BRANCH_ID = ? , ");
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
