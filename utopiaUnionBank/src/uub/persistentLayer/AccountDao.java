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
import uub.staticLayer.ConnectionManager;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HelperUtils;

public class AccountDao implements IAccountDao {

	private Connection connection;

	public AccountDao() throws CustomBankException {
		connection = ConnectionManager.getConnection();
	}

	private static final String addQuery = "INSERT INTO ACCOUNTS (USER_ID,BRANCH_ID,TYPE,BALANCE,STATUS) VALUES (?,?,?,?,?)";

	@Override
	public List<Account> getUserAccounts(int userId, String status) throws CustomBankException {

		String getQuery = "SELECT * FROM ACCOUNTS JOIN USER ON USER.ID = ACCOUNTS.USER_ID WHERE USER_ID = " + userId
				+ " AND ACCOUNTS.STATUS = '" + status + "'";

		return getAccounts(getQuery);

	}

	@Override
	public Map<Integer, List<Account>> getBranchAccounts(int branchId, String status) throws CustomBankException {

		String getQuery = "SELECT * FROM ACCOUNTS JOIN USER ON USER.ID = ACCOUNTS.USER_ID WHERE  ACCOUNTS.STATUS = '"
				+ status + "' AND BRANCH_ID = " + branchId;

		return getAccountsWithUser(getQuery);

	}

	@Override
	public Map<Integer, List<Account>> getBranchAccounts(String status) throws CustomBankException {

		String getQuery = "SELECT * FROM ACCOUNTS JOIN USER ON USER.ID = ACCOUNTS.USER_ID WHERE  ACCOUNTS.STATUS = '"
				+ status + "'";

		return getAccountsWithUser(getQuery);

	}

	@Override
	public List<Account> getAccount(int accNo) throws CustomBankException {

		String getQuery = "SELECT * FROM ACCOUNTS WHERE ACC_NO = " + accNo;

		return getAccounts(getQuery);

	}

	@Override
	public List<Account> getAllAccounts(String status) throws CustomBankException {

		String getQuery = "SELECT * FROM ACCOUNTS WHERE ACCOUNTS.STATUS = '" + status + "'";

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
	public int updateAccount(Account account) throws CustomBankException {

		HelperUtils.nullCheck(account);

		StringBuilder updateQuery = new StringBuilder("UPDATE ACCOUNTS SET  ");

		updateQuery.append(buildSet(account)).append("WHERE ACC_NO = ?");

		try (PreparedStatement statement = connection.prepareStatement(updateQuery.toString())) {

			setValues(statement, account);

			return statement.executeUpdate();

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

	}

	private List<Account> getAccounts(String query) throws CustomBankException {

		HelperUtils.nullCheck(query);

		List<Account> accounts = new ArrayList<Account>();

		try (Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(query);

			Account account;
			while (resultSet.next()) {

				account = mapAccount(resultSet);
				accounts.add(account);

			}

			return accounts;
		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

	}

	private Map<Integer, List<Account>> getAccountsWithUser(String query) throws CustomBankException {

		Map<Integer, List<Account>> accountMap = new HashMap<Integer, List<Account>>();

		try (Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {

				Account account;

				int id = resultSet.getInt("USER_ID");

				if (!accountMap.containsKey(id)) {

					accountMap.put(id, new ArrayList<Account>());

				}
				id = resultSet.getInt("USER_ID");
				account = mapAccount(resultSet);
				accountMap.get(id).add(account);

			}

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

		return accountMap;
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
			statement.setInt(index, account.getAccNo());
		}

	}

}
