package uub.persistentLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uub.model.User;
import uub.staticLayer.ConnectionManager;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HelperUtils;

public class UserDao implements IUserDao {

	private Connection connection;

	public UserDao() throws CustomBankException {
		connection = ConnectionManager.getConnection();
	}

	@Override
	public User getUserWithId(int userId) throws CustomBankException {

		String getQuery = "SELECT * FROM USER WHERE ID = '" + userId + "'";

		List<User> users = getUsers(getQuery);

		if (!users.isEmpty()) {
			return users.get(0);
		} else {
			return null;
		}

	}

	@Override
	public User getUserWithEmail(String email) throws CustomBankException {

		String getQuery = "SELECT * FROM USER WHERE EMAIL = '" + email + "' AND STATUS = 'ACTIVE'";

		List<User> users = getUsers(getQuery);

		if (!users.isEmpty()) {
			return users.get(0);
		} else {
			return null;
		}

	}

	@Override
	public List<User> getAllUsers(String type , String status) throws CustomBankException {

		
		String getQuery = "SELECT * FROM USER WHERE STATUS = '" + status +"' AND USER_TYPE = '"+type+"'";

		return getUsers(getQuery);

	}
	
	

	private List<User> getUsers(String query) throws CustomBankException {

		List<User> users = new ArrayList<User>();

		try (Statement statement = connection.createStatement()) {

			
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {

				User user = mapUser(resultSet);
				users.add(user);
			}

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

		return users;

	}

	@Override
	public void updateUser(User user) throws CustomBankException {

		HelperUtils.nullCheck(user);

		StringBuilder updateQuery = new StringBuilder("UPDATE USER SET  ");

		updateQuery.append(getFieldList(user)).append("WHERE ID = ");

		try (PreparedStatement statement = connection.prepareStatement(updateQuery.toString())) {

			setValues(statement, user);
			statement.executeUpdate();

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

	}

	private String getFieldList(User user) {

		StringBuilder queryBuilder = new StringBuilder("  ");

		if (user.getName() != null) {
			queryBuilder.append("NAME = ? , ");
		}
		if (user.getEmail() != null) {
			queryBuilder.append("EMAIL = ? , ");
		}
		if (user.getPhone() != null) {
			queryBuilder.append("PHONE = ? , ");
		}
		if (user.getdOB() != 0) {
			queryBuilder.append("DOB = ? , ");
		}
		if (user.getGender() != null) {
			queryBuilder.append("GENDER = ? , ");
		}
		if (user.getPassword() != null) {
			queryBuilder.append("PASSWORD = ?, ");
		}
		if (user.getUserType() != null) {
			queryBuilder.append("USER_TYPE = ? , ");
		}
		if (user.getStatus() != null) {
			queryBuilder.append("STATUS = ? , ");
		}
		if (user.getId() != 0) {
			queryBuilder.append("USER.ID = ? , ");
		}

		queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
		return queryBuilder.toString();

	}

	private void setValues(PreparedStatement statement, User user) throws SQLException {

		int index = 1;

		if (user.getName() != null) {
			statement.setObject(index++, user.getName());
		}
		if (user.getEmail() != null) {
			statement.setObject(index++, user.getEmail());
		}
		if (user.getPhone() != null) {
			statement.setObject(index++, user.getPhone());
		}
		if (user.getdOB() != 0) {
			statement.setLong(index++, user.getdOB());
		}
		if (user.getGender() != null) {
			statement.setObject(index++, user.getGender());
		}
		if (user.getPassword() != null) {
			statement.setObject(index++, user.getPassword());
		}
		if (user.getUserType() != null) {
			statement.setObject(index++, user.getUserType());
		}
		if (user.getStatus() != null) {
			statement.setObject(index++, user.getStatus());
		}
		if (user.getId() != 0) {
			statement.setObject(index++, user.getId());
		}

	}

	private User mapUser(ResultSet resultSet) throws SQLException {

		User user = new User();

		user.setId(resultSet.getInt("USER.ID"));
		user.setName(resultSet.getString("USER.NAME"));
		user.setEmail(resultSet.getString("USER.EMAIL"));
		user.setPhone(resultSet.getString("USER.PHONE"));
		user.setdOB(resultSet.getLong("USER.DOB"));
		user.setGender(resultSet.getString("USER.GENDER"));
		user.setPassword(resultSet.getString("USER.PASSWORD"));
		user.setUserType(resultSet.getString("USER.USER_TYPE"));
		user.setStatus(resultSet.getString("USER.STATUS"));

		return user;

	}

}
