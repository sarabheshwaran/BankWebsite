package uub.persistentLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uub.model.User;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HelperUtils;

public class UserDao implements IUserDao {

	private Connection connection;

	public UserDao() throws CustomBankException {
		connection = ConnectionManager.getConnection();
	}

//	private static final StringBuilder getQuery = new StringBuilder("SELECT * FROM USER WHERE ");

//	@Override
//	public List<User> getUsers(String field, Object value) throws CustomBankException {
//
//		List<User> users = new ArrayList<User>();
//
//		HelperUtils.nullCheck(value);
//
//		getQuery.append(field).append(" = ? ;");
//
//		try (PreparedStatement statement = connection.prepareStatement(getQuery.toString())) {
//
//			HelperUtils.setParameter(statement, 1, value);
//			ResultSet resultSet = statement.executeQuery();
//
//			while (resultSet.next()) {
//
//				User user = mapUser(resultSet);
//				users.add(user);
//			}
//
//		} catch (SQLException e) {
//			throw new CustomBankException(e.getMessage());
//		}
//
//		return users;
//
//	}
	
	@Override
	public List<User> getUsers(User user) throws CustomBankException {

		List<User> users = new ArrayList<User>();

		HelperUtils.nullCheck(user);

		StringBuilder getQuery = new StringBuilder("SELECT * FROM USER WHERE ");
		
		getQuery.append(getFieldList(user, " AND "));

		try (PreparedStatement statement = connection.prepareStatement(getQuery.toString())) {

			setValues(statement, user);
			
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {

				user = mapUser(resultSet);
				users.add(user);
			}

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

		return users;

	}



	private String getFieldList(User user,String delimeter ) {
		
		
		StringBuilder queryBuilder = new StringBuilder("  ");
        
		if (user.getId() != 0) {
		    queryBuilder.append("USER.ID = ? " + delimeter );
		}
		if (user.getName() != null) {
		    queryBuilder.append("NAME = ? " + delimeter );
		}
		if (user.getEmail() != null) {
		    queryBuilder.append("EMAIL = ? " + delimeter );
		}
		if (user.getPhone() != null) {
		    queryBuilder.append("PHONE = ? " + delimeter );
		}
		if (user.getdOB() != 0) {
		    queryBuilder.append("DOB = ? " + delimeter );
		}
		if (user.getGender() != null) {
		    queryBuilder.append("GENDER = ? " + delimeter );
		}
		if (user.getPassword() != null) {
		    queryBuilder.append("PASSWORD = ?" + delimeter );
		}
		if (user.getUserType() != null) {
		    queryBuilder.append("USER_TYPE = ? " + delimeter );
		}
		if (user.getStatus() != null) {
		    queryBuilder.append("STATUS = ? " + delimeter );
		}
		

		 queryBuilder.delete(queryBuilder.length() - (delimeter.length() + 1), queryBuilder.length());
		 return queryBuilder.toString();
		
	}
	
	
	private void setValues(PreparedStatement statement, User user) throws SQLException {
		
		int index = 1;
		
		if (user.getId() != 0) {
		    statement.setObject(index++, user.getId());
		}
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
