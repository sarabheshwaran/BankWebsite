package uub.logicalLayer;



import uub.model.User;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HashEncoder;

public class LoginHelper {
	

	public int login(String userName, String passWord) throws CustomBankException {

		
		UserHelper userHelper = new UserHelper();
		
		passWord = HashEncoder.encode(passWord);

		
		User user = userHelper.getUser(userName);

	

			if (user.getPassword().equals(passWord)) {

				String userType = user.getUserType();

				if (userType.equals("Customer")) {
					
					return 1;
					
				} else {
					
					return 2;

				}

			} else {

				throw new CustomBankException("Wrong Password !");

			}
		

	}

}
