package uub.staticLayer;

//import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//import java.util.logging.ConsoleHandler;
//import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
//import java.util.logging.SimpleFormatter;

public class HelperUtils {
	private static final String nullErrorMessage = "Input is null !";
	private static final String sizeErrorMessage = "Bound size violated !";

	public static void nullCheck(Object input) throws CustomBankException {

		if (input == null) {
			throw new CustomBankException(nullErrorMessage);
		}

	}

	public static void sizeCheck(int collectionSize, int index) throws CustomBankException {

		if (collectionSize < index || index < 0) {
			throw new CustomBankException(sizeErrorMessage);
		}

	}

	public static double doubleFormat(double value) {

		return Math.round(value * 100.0) / 100.0;

	}

	public static int lengthOfString(String input) throws CustomBankException {

		nullCheck(input);

		return input.length();
	}

	public static void formatLogger() {

//		nullCheck(fileName);

		Logger logger = Logger.getGlobal();

		ColouredLogger console = new ColouredLogger();

//        FileHandler file = new FileHandler(fileName+".log",true);

		console.setFormatter(new Formatter() {
			@Override
			public String format(LogRecord record) {
				return record.getMessage();
			}
		});

//        file.setFormatter(new SimpleFormatter());

		logger.addHandler(console);
		logger.setUseParentHandlers(false);

//        logger.addHandler(file);

	}

	public static void setParameter(PreparedStatement preparedStatement, int parameterIndex, Object value)
			throws SQLException {
		if (value instanceof Integer) {
			preparedStatement.setInt(parameterIndex, (Integer) value);
		} else if (value instanceof String) {
			preparedStatement.setString(parameterIndex, (String) value);
		} else if (value instanceof Double) {
			preparedStatement.setDouble(parameterIndex, (Double) value);
		} else if (value instanceof Long) {
			preparedStatement.setLong(parameterIndex, (Long) value);
		} else {
			throw new SQLException("Unsupported datatype for parameter");
		}
	}

}