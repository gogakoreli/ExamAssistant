package models;

import java.sql.ResultSet;

public class ExamBoard extends EAUser {

	public ExamBoard(ResultSet rs) {
		super(rs);
		
	}
	
	public ExamBoard() {
		super();
		
	}
	
	public ExamBoard(int userID, EAUserRole role, String mail, String firstName, String lastName, String image,
			String googleID) {
		super(userID, role, mail, firstName, lastName, image, googleID);
	}


	@Override
	public void downloadAditionalInfo() {
		// TODO Auto-generated method stub
		
	}
	

}
