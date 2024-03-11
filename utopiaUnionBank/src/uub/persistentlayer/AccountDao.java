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

import uub.enums.AccountStatus;
import uub.enums.AccountType;
import uub.model.Account;
import uub.persistentinterfaces.IAccountDao;
import uub.staticlayer.ConnectionManager;
import uub.staticlayer.CustomBankException;
import uub.staticlayer.HelperUtils;

public class AccountDao implements IAccountDao {

	@Override
	public Map<Integer, Account> getUserAccounts(int userId, AccountStatus status) throws CustomBankException {

		String getQuery = "SELECT * FROM ACCOUNTS WHERE USER_ID = " + userId + " AND ACCOUNTS.STATUS = "
				+ status.getStatus();

		return getAccountsWithUser(getQuery);

	}

	@Override
	public Map<Integer, Map<Integer, Account>> getBranchAccounts(int branchId, AccountStatus status, int limit,
			int offSet) throws CustomBankException {

		String getQuery = "SELECT * FROM ACCOUNTS WHERE  ACCOUNTS.STATUS = " + status.getStatus() + " AND BRANCH_ID = "
				+ branchId + " LIMIT " + limit + " OFFSET " + offSet;

		return getAccountsWithBranch(getQuery);
	}

	@Override
	public List<Account> getAccount(int accNo) throws CustomBankException {

		String getQuery = "SELECT * FROM ACCOUNTS WHERE ACC_NO = " + accNo;

		return getAccounts(getQuery);

	}

	@Override
	public void addAccounts(List<Account> accounts) throws CustomBankException {

		HelperUtils.nullCheck(accounts);

		String addQuery = "INSERT INTO ACCOUNTS (USER_ID,BRANCH_ID,TYPE,BALANCE,STATUS) VALUES (?,?,?,?,?)";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(addQuery)) {
			for (Account account : accounts) {
				statement.setObject(1, account.getUserId());
				statement.setObject(2, account.getBranchId());
				statement.setObject(3, account.getType().getType());
				statement.setObject(4, account.getBalance());
				statement.setObject(5, account.getStatus().getStatus());

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

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(updateQuery.toString())) {

			setValues(statement, account);

			return statement.executeUpdate();

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

	}

	private List<Account> getAccounts(String query) throws CustomBankException {

		HelperUtils.nullCheck(query);

		List<Account> accounts = new ArrayList<Account>();

		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement()) {

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

	private Map<Integer, Account> getAccountsWithUser(String query) throws CustomBankException {

		HelperUtils.nullCheck(query);

		Map<Integer, Account> accountMap = new HashMap<Integer, Account>();

		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {

				Account account;

				account = mapAccount(resultSet);
				accountMap.put(account.getAccNo(), account);

			}

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

		return accountMap;
	}

	private Map<Integer, Map<Integer, Account>> getAccountsWithBranch(String query) throws CustomBankException {

		HelperUtils.nullCheck(query);

		Map<Integer, Map<Integer, Account>> accountMap = new HashMap<Integer, Map<Integer, Account>>();

		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {

				Account account;

				int id = resultSet.getInt("USER_ID");

				if (!accountMap.containsKey(id)) {

					accountMap.put(id, new HashMap<Integer, Account>());

				}
				account = mapAccount(resultSet);
				accountMap.get(id).put(account.getAccNo(), account);

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
		account.setType(AccountType.valueOf(resultSet.getInt("TYPE")));
		account.setBalance(resultSet.getDouble("BALANCE"));
		account.setStatus(AccountStatus.valueOf(resultSet.getInt("STATUS")));

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
			statement.setObject(index++, account.getStatus().getStatus());
		}
		if (account.getAccNo() != 0) {
			statement.setInt(index, account.getAccNo());
		}

	}

}
