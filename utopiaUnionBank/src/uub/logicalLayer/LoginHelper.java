package uub.logicalLayer;

import uub.model.User;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HashEncoder;
import uub.staticLayer.HelperUtils;

public class LoginHelper {

	public void passwordValidate(User user, String password) throws CustomBankException {

		HelperUtils.nullCheck(user);
		HelperUtils.nullCheck(password);

		password = HashEncoder.encode(password);

		if (!user.getPassword().equals(password)) {

			throw new CustomBankException("Password Wrong");

		}

	}

	public int login(int id, String password) throws CustomBankException {

		HelperUtils.nullCheck(password);
		
		try {
			UserHelper userHelper = new UserHelper();

			User user = userHelper.getUser(id);

			passwordValidate(user, password);

			String userType = user.getUserType();

			if (userType.equals("Customer")) {

				return 1;

			} else {

				return 2;

			}
		} catch (CustomBankException e) {
			throw new CustomBankException("Login Failed", e);
		}

	}

}
