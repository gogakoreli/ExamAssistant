package models;

import java.sql.ResultSet;


public class Lecturer extends EAUser{

	
	public Lecturer(ResultSet rs) {
		super(rs);
	}
	
	public Lecturer() {
		super();
	}
	
	public Lecturer(int userID, EAUserRole role, String mail, String firstName, String lastName, String image,
			String googleID) {
		super(userID, role, mail, firstName, lastName, image, googleID);
	}

	@Override
	public void downloadAditionalInfo() {
		// TODO Auto-generated method stub
		
	}

}
