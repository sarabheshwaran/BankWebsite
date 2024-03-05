package uub.persistentLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uub.model.Customer;
import uub.staticLayer.ConnectionManager;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HelperUtils;

public class CustomerDao implements ICustomerDao {

	private static final String addQuery1 = "INSERT INTO USER (NAME,EMAIL,PHONE,DOB,GENDER,PASSWORD,USER_TYPE,STATUS) VALUES (?,?,?,?,?,?,?,?)";
	private static final String addQuery2 = "INSERT INTO CUSTOMER VALUES (?,?,?,?)";

	

	@Override
	public int[] addCustomer(List<Customer> customers) throws CustomBankException {

		Connection connection = ConnectionManager.getConnection();
		
		try (
				PreparedStatement statement = connection.prepareStatement(addQuery1,
				PreparedStatement.RETURN_GENERATED_KEYS);
				PreparedStatement statement2 = connection.prepareStatement(addQuery2)) {

			connection.setAutoCommit(false);
			for (Customer customer : customers) {
				HelperUtils.setParameter(statement, 1, customer.getName());
				HelperUtils.setParameter(statement, 2, customer.getEmail());
				HelperUtils.setParameter(statement, 3, customer.getPhone());
				HelperUtils.setParameter(statement, 4, customer.getdOB());
				HelperUtils.setParameter(statement, 5, customer.getGender());
				HelperUtils.setParameter(statement, 6, customer.getPassword());
				HelperUtils.setParameter(statement, 7, customer.getUserType());
				HelperUtils.setParameter(statement, 8, customer.getStatus());
				statement.addBatch();
			}
			statement.executeBatch();

			try (ResultSet resultSet = statement.getGeneratedKeys()) {
				int index = 0;
				while (resultSet.next()) {

					int id = resultSet.getInt(1);
					Customer customer = customers.get(index);
					HelperUtils.setParameter(statement2, 1, id);
					HelperUtils.setParameter(statement2, 2, customer.getAadhar());
					HelperUtils.setParameter(statement2, 3, customer.getpAN());
					HelperUtils.setParameter(statement2, 4, customer.getAddress());

					statement2.addBatch();
					index++;
				}

				int[] a = statement2.executeBatch();
				connection.commit();
				return a;
			}

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new CustomBankException(e.getMessage());
			}
			throw new CustomBankException(e.getMessage());
		}finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CustomBankException(e.getMessage());
			}
		}
	

	}

	@Override
	public List<Customer> getCustomers(int id) throws CustomBankException {

		String getQuery = "SELECT * FROM CUSTOMER JOIN USER ON CUSTOMER.ID = USER.ID WHERE CUSTOMER.ID = " + id +" AND STATUS = 'INACTIVE'";

		return getCustomers(getQuery);

	}

	@Override
	public List<Customer> getCustomersWithEmail(String email) throws CustomBankException {

		String getQuery = "SELECT * FROM CUSTOMER JOIN USER ON CUSTOMER.ID = USER.ID WHERE EMAIL = '" + email+"' AND STATUS = 'ACTIVE'";

		return getCustomers(getQuery);

	}

	
	@Override
	public void updateCustomer(Customer customer) throws CustomBankException {

		HelperUtils.nullCheck(customer);
		
		 StringBuilder updateQuery = new StringBuilder("UPDATE CUSTOMER JOIN USER ON USER.ID = CUSTOMER.ID SET  ");

		updateQuery.append(getFieldList(customer)).append("WHERE ID = ");

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(updateQuery.toString())) {

			setValues(statement, customer);
			statement.executeUpdate();

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

	}
	
	private List<Customer> getCustomers(String query) throws CustomBankException {

		List<Customer> customers = new ArrayList<Customer>();

		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {

				Customer resultCustomer = mapCustomer(resultSet);
				customers.add(resultCustomer);
			}

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

		return customers;
	}


	private String getFieldList(Customer customer) {

		StringBuilder queryBuilder = new StringBuilder("  ");

		if (customer.getId() != 0) {
			queryBuilder.append("CUSTOMER.ID = ? , ");
		}
		if (customer.getName() != null) {
			queryBuilder.append("NAME = ? , ");
		}
		if (customer.getEmail() != null) {
			queryBuilder.append("EMAIL = ? , ");
		}
		if (customer.getPhone() != null) {
			queryBuilder.append("PHONE = ? , ");
		}
		if (customer.getdOB() != 0) {
			queryBuilder.append("DOB = ? , ");
		}
		if (customer.getGender() != null) {
			queryBuilder.append("GENDER = ? , ");
		}
		if (customer.getPassword() != null) {
			queryBuilder.append("PASSWORD = ?, ");
		}
		if (customer.getUserType() != null) {
			queryBuilder.append("USER_TYPE = ? , ");
		}
		if (customer.getStatus() != null) {
			queryBuilder.append("STATUS = ? , ");
		}
		if (customer.getAadhar() != null) {
			queryBuilder.append("AADHAR_NO = ? , ");
		}
		if (customer.getpAN() != null) {
			queryBuilder.append("PAN = ? , ");
		}
		if (customer.getAddress() != null) {
			queryBuilder.append("ADDRESS = ? , ");
		}

		queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
		return queryBuilder.toString();

	}

	private void setValues(PreparedStatement statement, Customer customer) throws SQLException {

		int index = 1;

		if (customer.getName() != null) {
			statement.setObject(index++, customer.getName());
		}
		if (customer.getEmail() != null) {
			statement.setObject(index++, customer.getEmail());
		}
		if (customer.getPhone() != null) {
			statement.setObject(index++, customer.getPhone());
		}
		if (customer.getdOB() != 0) {
			statement.setLong(index++, customer.getdOB());
		}
		if (customer.getGender() != null) {
			statement.setObject(index++, customer.getGender());
		}
		if (customer.getPassword() != null) {
			statement.setObject(index++, customer.getPassword());
		}
		if (customer.getUserType() != null) {
			statement.setObject(index++, customer.getUserType());
		}
		if (customer.getStatus() != null) {
			statement.setObject(index++, customer.getStatus());
		}
		if (customer.getAadhar() != null) {
			statement.setObject(index++, customer.getAadhar());
		}
		if (customer.getpAN() != null) {
			statement.setObject(index++, customer.getpAN());
		}
		if (customer.getAddress() != null) {
			statement.setObject(index++, customer.getAddress());
		}

		if (customer.getId() != 0) {
			statement.setObject(index++, customer.getId());
		}

	}

	private Customer mapCustomer(ResultSet resultSet) throws SQLException {

		Customer customer = new Customer();

		customer.setId(resultSet.getInt("CUSTOMER.ID"));
		customer.setName(resultSet.getString("USER.NAME"));
		customer.setEmail(resultSet.getString("USER.EMAIL"));
		customer.setPhone(resultSet.getString("USER.PHONE"));
		customer.setdOB(resultSet.getLong("USER.DOB"));
		customer.setGender(resultSet.getString("USER.GENDER"));
		customer.setPassword(resultSet.getString("USER.PASSWORD"));
		customer.setUserType(resultSet.getString("USER.USER_TYPE"));
		customer.setStatus(resultSet.getString("USER.STATUS"));
		customer.setAadhar(resultSet.getString("CUSTOMER.AADHAR_NO"));
		customer.setpAN(resultSet.getString("CUSTOMER.PAN"));
		customer.setAddress(resultSet.getString("CUSTOMER.ADDRESS"));

		return customer;

	}

	

}
