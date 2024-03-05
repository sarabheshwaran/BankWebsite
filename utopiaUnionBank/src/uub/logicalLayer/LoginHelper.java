package uub.logicalLayer;



import uub.model.User;
import uub.staticLayer.CustomBankException;
import uub.staticLayer.HashEncoder;

public class LoginHelper {
	

	public void passwordValidate(String givenPassword, String userPassword) throws CustomBankException {
		
		givenPassword = HashEncoder.encode(givenPassword);
		
		if(givenPassword != userPassword) {
			
			throw new CustomBankException("Wrong password");
		}
		
	}
	

	public int login(String userName, String passWord) throws CustomBankException {

		
		UserHelper userHelper = new UserHelper();
		

		
		User user = userHelper.getUser(userName);

	
		passwordValidate(passWord, user.getPassword());
			
		
				String userType = user.getUserType();

				if (userType.equals("Customer")) {
					
					return 1;
					
				} else {
					
					return 2;

				}

			}
		

	}


