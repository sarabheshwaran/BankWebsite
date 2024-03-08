package uub.logicallayer;

import uub.enums.UserType;
import uub.model.User;
import uub.staticlayer.CustomBankException;
import uub.staticlayer.HashEncoder;
import uub.staticlayer.HelperUtils;

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

			UserType type = user.getUserType();
					
			return type.getType();

			
		} catch (CustomBankException e) {
			throw new CustomBankException("Login Failed", e);
		}

	}

}
