package uub.staticlayer;

import java.util.regex.Pattern;

public class EmployeeUtils {
	public static boolean validateEmail(String email) throws CustomBankException {

		HelperUtils.nullCheck(email);
		boolean ans = Pattern.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,5}$", email);

		if (ans) {
			return ans;
		} else {
			throw new CustomBankException("Email invalid !");
		}
	}

	public static boolean validatePhone(String phoneNo) throws CustomBankException {

		HelperUtils.nullCheck(phoneNo);
		boolean ans = Pattern.matches("[789]{1}\\d{9}", phoneNo);

		if (ans) {
			return ans;
		} else {
			throw new CustomBankException("Phone no. invalid !");
		}

	}

	public static boolean validatePass(String password) throws CustomBankException {

		HelperUtils.nullCheck(password);

		String regex = "^(?=.*[A-Z])" + "(?=.*[a-z])" + "(?=.*\\d)"
				+ "(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\\\"\\\\|,.<>/?])" + "(?=.*[0-9])" + ".{8,}$";

		boolean ans = Pattern.matches(regex, password);

		if (ans) {
			return ans;
		} else {
			throw new CustomBankException("Password invalid !");
		}
	}
}
