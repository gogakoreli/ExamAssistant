package tests;

import models.EAUser;

public class EAUserTestHelper extends EAUser {

	public EAUserTestHelper() {
		// TODO Auto-generated constructor stub
	}
	
	
	public EAUserTestHelper(int userID, EAUserRole role, String mail, String firstName, String lastName, String image,
			String googleID) {
		super(userID, role, mail, firstName, lastName, image, googleID);
	}

	@Override
	public void downloadAditionalInfo() {
		// TODO Auto-generated method stub
		
	}

}
