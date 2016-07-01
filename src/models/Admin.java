package models;

import java.sql.ResultSet;

public class Admin extends EAUser {


	public Admin(ResultSet rs) {
		super(rs);
	}
	
	public Admin() {
		super();
	}
	
	public Admin(int userID, EAUserRole role, String mail, String firstName, String lastName, String image,
			String googleID) {
		super(userID, role, mail, firstName, lastName, image, googleID);
	}

	@Override
	public void downloadAditionalInfo() {
		// TODO Auto-generated method stub
		
	}

}
