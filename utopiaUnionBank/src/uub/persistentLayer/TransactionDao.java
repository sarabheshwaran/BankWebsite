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

	private Connection connection;

	public TransactionDao()throws CustomBankException {
		connection = ConnectionManager.getConnection();
	}

	private static final StringBuilder getQuery1 = new StringBuilder("SELECT * FROM TRANSACTION WHERE ");

	
	private static final String addQuery = "INSERT INTO TRANSACTION (USER_ID, ACC_NO, TRANSACTION_ACC, TYPE, AMOUNT, OPENING_BAL, CLOSING_BAL, DESCRIPTION, TIME, STATUS) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	@Override
	public List<Transaction> getTransactions (int accNo, long time) throws CustomBankException{
		
		String getQuery = "SELECT * FROM TRANSACTION WHERE ACC_NO ="+accNo+" AND TIME > "+ time;
		
		return getTransactions(getQuery);
		
	}
	
	
	private List<Transaction> getTransactions(String query) throws CustomBankException {
		List<Transaction> transactions = new ArrayList<>();


		try (Statement statement = connection.createStatement()) {

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
	public List<Transaction> getTransactions(Transaction transaction) throws CustomBankException {
		List<Transaction> transactions = new ArrayList<>();

		HelperUtils.nullCheck(transaction);

		StringBuilder getQuery = new StringBuilder("SELECT * FROM TRANSACTION WHERE");

		getQuery.append(getFieldList(transaction, " AND "));
		

		try (PreparedStatement statement = connection.prepareStatement(getQuery.toString())) {
			
			setValues(statement, transaction);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				transaction = mapTransaction(resultSet);
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

		   
		    try (PreparedStatement statement = connection.prepareStatement(addQuery)) {
		    	statement.setObject( 1, transaction.getUserId());
		    	statement.setObject( 2, transaction.getAccNo());
		    	statement.setObject( 3, transaction.getTransactionAcc());
		    	statement.setObject( 4, transaction.getType());
		    	statement.setObject( 5, transaction.getAmount());
		    	statement.setObject( 6, transaction.getOpeningBal());
		    	statement.setObject( 7, transaction.getClosingBal());
		    	statement.setObject( 8, transaction.getDesc());
		    	statement.setObject( 9, transaction.getTime());
		    	statement.setObject( 10, transaction.getStatus());


		        statement.executeUpdate();


		    } catch (SQLException e) {
				throw new CustomBankException(e.getMessage());
			}
	}


	@Override
	public int getLastId() throws CustomBankException {
		
		int lastId = -1; 

	    String query = "SELECT MAX(ID) AS max_id FROM TRANSACTION";

	    try (PreparedStatement statement = connection.prepareStatement(query)) {
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


private String getFieldList(Transaction transaction,String delimeter ) {
		
		
		StringBuilder queryBuilder = new StringBuilder("  ");
        
		if (transaction.getId() != 0) {
		    queryBuilder.append("CUSTOMER.ID = ? " + delimeter );
		}
		if (transaction.getUserId() != 0) {
		    queryBuilder.append("NAME = ? " + delimeter );
		}
		if (transaction.getAccNo() != 0) {
		    queryBuilder.append("ACC_NO = ? " + delimeter );
		}
		if (transaction.getTransactionAcc() != 0) {
		    queryBuilder.append("PHONE = ? " + delimeter );
		}
		if (transaction.getType() != null) {
		    queryBuilder.append("DOB = ? " + delimeter );
		}
		if (transaction.getAmount() != 0) {
		    queryBuilder.append("GENDER = ? " + delimeter );
		}
		if (transaction.getOpeningBal() != -1) {
		    queryBuilder.append("PASSWORD = ?" + delimeter );
		}
		if (transaction.getClosingBal() != -1) {
		    queryBuilder.append("USER_TYPE = ? " + delimeter );
		}
		if (transaction.getStatus() != null) {
		    queryBuilder.append("STATUS = ? " + delimeter );
		}
		if (transaction.getDesc() != null) {
		    queryBuilder.append("AADHAR_NO = ? " + delimeter );
		}
		if (transaction.getTime() != 0) {
		    queryBuilder.append("PAN = ? " + delimeter );
		}

		 queryBuilder.delete(queryBuilder.length() - (delimeter.length() + 1), queryBuilder.length());
		 return queryBuilder.toString();
		
	}
	
	
	private void setValues(PreparedStatement statement, Transaction transaction) throws SQLException {
		
		int index = 1;
		
		if (transaction.getId() != 0) {
		    statement.setObject(index++, transaction.getId());
		}
		if (transaction.getUserId() != 0) {
		    statement.setObject(index++, transaction.getUserId());
		}
		if (transaction.getAccNo() != 0) {
		    statement.setObject(index++, transaction.getAccNo());
		}
		if (transaction.getTransactionAcc() != 0) {
		    statement.setObject(index++, transaction.getTransactionAcc());
		}
		if (transaction.getType() != null) {
		    statement.setObject(index++, transaction.getType());
		}
		if (transaction.getAmount() != 0) {
		    statement.setObject(index++, transaction.getAmount());
		}
		if (transaction.getOpeningBal() != -1) {
		    statement.setObject(index++, transaction.getOpeningBal());
		}
		if (transaction.getClosingBal() != -1) {
		    statement.setObject(index++, transaction.getClosingBal());
		}
		if (transaction.getStatus() != null) {
		    statement.setObject(index++, transaction.getStatus());
		}
		if (transaction.getDesc() != null) {
		    statement.setObject(index++, transaction.getDesc());
		}
		if (transaction.getTime() != 0) {
		    statement.setObject(index++, transaction.getTime());
		}
		if (transaction.getStatus() != null) {
		    statement.setObject(index++, transaction.getStatus());
		}
	}


	@Override
	public List<Transaction> getTransactions(String field, Object value) throws CustomBankException {
		// TODO Auto-generated method stub
		return null;
	}
	
	






	
}
