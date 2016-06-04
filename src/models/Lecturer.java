package models;

public class Lecturer extends EAUser{
	

	public Lecturer(int userID) {
		super(userID);
	}
	
	public int getRole() {
		return super.LECTURER;
	}

	public String getMail(){
		return super.getMail();
	}
	
	public String getUserFirstName(){
		return super.getUserFirstName();
	}
	
	public String getUserLastName(){
		return super.getUserLastName();
	}

	public int getUserID() {
		return super.getUserID();
	}
}
