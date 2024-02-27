package uub.persistentLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uub.model.Account;
import uub.model.User;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HelperUtils;

public class AccountDao implements IAccountDao {

	private Connection connection;

	public AccountDao() throws CustomBankException {
		connection = ConnectionManager.getConnection();
	}

	
	private static final String addQuery = "INSERT INTO ACCOUNTS (USER_ID,BRANCH_ID,TYPE,BALANCE,STATUS) VALUES (?,?,?,?,?)";

	@Override
	public List<User> getUserAccounts(int userId, boolean active) throws CustomBankException {

		String status;
		if (active) {
			status = "ACTIVE";
		} else {
			status = "INACTIVE";
		}

		String getQuery = "SELECT * FROM ACCOUNTS JOIN USER ON USER.ID = ACCOUNTS.USER_ID WHERE USER_ID = " + userId + " AND ACCOUNTS.STATUS = '" + status+"'";

		return getAccountsWithUser(getQuery);

	}
	
	@Override
	public List<User> getUserAccounts(int accNo) throws CustomBankException {

		
		String getQuery = "SELECT * FROM ACCOUNTS JOIN USER ON USER.ID = ACCOUNTS.USER_ID WHERE ACC_NO = " + accNo ;

		return getAccountsWithUser(getQuery);

	}

	@Override
	public List<User> getBranchAccounts(int branchId, boolean active) throws CustomBankException {

		String status;
		if (active) {
			status = "ACTIVE";
		} else {
			status = "INACTIVE";
		}

		String getQuery = "SELECT * FROM ACCOUNTS JOIN USER ON USER.ID = ACCOUNTS.USER_ID WHERE BRANCH_ID = " + branchId + " AND ACCOUNTS.STATUS = '" + status+"'";

		return getAccountsWithUser(getQuery);

	}

	@Override
	public List<Account> getAllAccounts(int accNo, boolean active) throws CustomBankException {

		String status;
		String account;
		
		if(accNo!=0) {
			account = "AND ACC_NO = "+ accNo;
		}else {
			account = "";
		}
		
		if (active) {
			status = "ACTIVE";
		} else {
			status = "INACTIVE";
		}

		String getQuery = "SELECT * FROM ACCOUNTS WHERE ACCOUNTS.STATUS = '" + status + "'" + account;

		return getAccounts(getQuery);

	}

	
	@Override
	public void addAccounts(List<Account> accounts) throws CustomBankException {

		HelperUtils.nullCheck(accounts);

		try (PreparedStatement statement = connection.prepareStatement(addQuery)) {
			for (Account account : accounts) {
				HelperUtils.setParameter(statement, 1, account.getUserId());
				HelperUtils.setParameter(statement, 2, account.getBranchId());
				HelperUtils.setParameter(statement, 3, account.getType());
				HelperUtils.setParameter(statement, 4, account.getBalance());
				HelperUtils.setParameter(statement, 5, account.getStatus());

				statement.addBatch();
			}
			statement.executeBatch();

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

	}

	@Override
	public void updateAccount(Account account) throws CustomBankException {

		HelperUtils.nullCheck(account);

		StringBuilder updateQuery = new StringBuilder("UPDATE ACCOUNTS SET  ");

		updateQuery.append(buildSet(account)).append("WHERE ACC_NO = ?");

		try (PreparedStatement statement = connection.prepareStatement(updateQuery.toString())) {

			setValues(statement, account);

			statement.executeUpdate();

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

	}
	
	private List<Account> getAccounts(String query)throws CustomBankException{
		
		List<Account> accounts = new ArrayList<Account>();
		
		try (Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(query);
			
			Account account;
			while (resultSet.next()) {
				
				account = mapAccount(resultSet);
				accounts.add(account);
				
			}
			
			return accounts;
			}
	 catch (SQLException e) {
		throw new CustomBankException(e.getMessage());
	}
		
		
		
	}
	
	private List<User> getAccountsWithUser(String query) throws CustomBankException {

		Map<Integer, User> userMap = new HashMap<Integer, User>();

		try (Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {

				User user;
				Account account;

				int id = resultSet.getInt("USER_ID");

				if (!userMap.containsKey(id)) {

					user = mapUser(resultSet);
					user.setAccounts(new ArrayList<Account>());
					userMap.put(id, user);

				}
				id = resultSet.getInt("USER_ID");
				account = mapAccount(resultSet);
				userMap.get(id).addAccount(account);

			}

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

		return new ArrayList<User>(userMap.values());
	}


	private Account mapAccount(ResultSet resultSet) throws SQLException {
		Account account = new Account();

		account.setAccNo(resultSet.getInt("ACC_NO"));
		account.setUserId(resultSet.getInt("USER_ID"));
		account.setBranchId(resultSet.getInt("BRANCH_ID"));
		account.setType(resultSet.getString("TYPE"));
		account.setBalance(resultSet.getDouble("BALANCE"));
		account.setStatus(resultSet.getString("STATUS"));

		return account;
	}

	private String buildSet(Account account) {

		StringBuilder queryBuilder = new StringBuilder("  ");

		if (account.getUserId() != 0) {
			queryBuilder.append("USER_ID = ? , ");
		}
		if (account.getBranchId() != 0) {
			queryBuilder.append("BRANCH_ID = ? , ");
		}
		if (account.getType() != null) {
			queryBuilder.append("TYPE = ? , ");
		}
		if (account.getBalance() != -1) {
			queryBuilder.append("BALANCE = ? , ");
		}
		if (account.getStatus() != null) {
			queryBuilder.append("STATUS = ? , ");
		}

		queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
		return queryBuilder.toString();

	}

	private void setValues(PreparedStatement statement, Account account) throws SQLException {

		int index = 1;

		if (account.getUserId() != 0) {
			statement.setObject(index++, account.getUserId());
		}
		if (account.getBranchId() != 0) {
			statement.setObject(index++, account.getBranchId());
		}
		if (account.getType() != null) {
			statement.setObject(index++, account.getType());
		}
		if (account.getBalance() != -1) {
			statement.setObject(index++, account.getBalance());
		}
		if (account.getStatus() != null) {
			statement.setObject(index++, account.getStatus());
		}
		if (account.getAccNo() != 0) {
			statement.setInt(index, account.getAccNo() );
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
