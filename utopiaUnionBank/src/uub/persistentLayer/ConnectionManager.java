package uub.persistentLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import uub.staticLayer.CustomBankException;



public class ConnectionManager {

	private static final String url = "jdbc:mysql://localhost:3306/UUB";
	private static final String userName = "new_user";
	private static final String password = "password";

	private static Connection connection;

	public static Connection getConnection() throws CustomBankException  {
		
		try {
		if (connection == null) {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, userName, password);
		}
		return connection;
	}
	catch(ClassNotFoundException | SQLException e) {
		
		throw new CustomBankException("Problem on getting Connection",e);
		
	}
		
	}

}
