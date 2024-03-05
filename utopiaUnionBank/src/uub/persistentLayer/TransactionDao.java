package uub.persistentLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uub.model.Transaction;
import uub.staticLayer.ConnectionManager;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HelperUtils;

public class TransactionDao implements ITransactionDao {

	private static final String addQuery = "INSERT INTO TRANSACTION (ID, USER_ID, ACC_NO, TRANSACTION_ACC, TYPE, AMOUNT, OPENING_BAL, CLOSING_BAL, DESCRIPTION, TIME, STATUS) "
			+ "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	@Override
	public List<Transaction> getTransactions(int accNo, long from, int limit, int offSet) throws CustomBankException {

		String getQuery = "SELECT * FROM TRANSACTION WHERE ACC_NO =" + accNo + " AND TIME > " + from + " LIMIT  "
				+ limit + " OFFSET " + offSet;

		return getTransactions(getQuery);

	}

	@Override
	public List<Transaction> getTransactionsOfUser(int userId, long from, int limit, int offSet)
			throws CustomBankException {

		String getQuery = "SELECT * FROM TRANSACTION WHERE ACC_NO =" + userId + " AND TIME > " + from + " LIMIT  "
				+ limit + " OFFSET " + offSet;

		return getTransactions(getQuery);

	}

	private List<Transaction> getTransactions(String query) throws CustomBankException {
		List<Transaction> transactions = new ArrayList<>();

		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement();) {

			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				Transaction transaction = mapTransaction(resultSet);
				transactions.add(transaction);
			}

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

		return transactions;
	}

	@Override
	public void addTransaction(Transaction transaction) throws CustomBankException {

		HelperUtils.nullCheck(transaction);

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(addQuery)) {
			statement.setObject(1, transaction.getId());
			statement.setObject(2, transaction.getUserId());
			statement.setObject(3, transaction.getAccNo());
			statement.setObject(4, transaction.getTransactionAcc());
			statement.setObject(5, transaction.getType());
			statement.setObject(6, transaction.getAmount());
			statement.setObject(7, transaction.getOpeningBal());
			statement.setObject(8, transaction.getClosingBal());
			statement.setObject(9, transaction.getDesc());
			statement.setObject(10, transaction.getTime());
			statement.setObject(11, transaction.getStatus());

			System.out.println(statement);
			statement.executeUpdate();

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}
	}

	@Override
	public int getLastId() throws CustomBankException {

		int lastId = -1;

		String query = "SELECT MAX(ID) AS max_id FROM TRANSACTION";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				lastId = resultSet.getInt("max_id");
			}

		} catch (SQLException e) {
			throw new CustomBankException(e.getMessage());
		}

		return lastId;
	}

	private Transaction mapTransaction(ResultSet resultSet) throws SQLException {
		Transaction transaction = new Transaction();

		transaction.setId(resultSet.getInt("ID"));
		transaction.setUserId(resultSet.getInt("USER_ID"));
		transaction.setAccNo(resultSet.getInt("ACC_NO"));
		transaction.setTransactionAcc(resultSet.getInt("TRANSACTION_ACC"));
		transaction.setType(resultSet.getString("TYPE"));
		transaction.setAmount(resultSet.getDouble("AMOUNT"));
		transaction.setOpeningBal(resultSet.getDouble("OPENING_BAL"));
		transaction.setClosingBal(resultSet.getDouble("CLOSING_BAL"));
		transaction.setDesc(resultSet.getString("DESCRIPTION"));
		transaction.setTime(resultSet.getLong("TIME"));
		transaction.setStatus(resultSet.getString("STATUS"));

		return transaction;
	}

}
