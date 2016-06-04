package models;

public class ExamBoard extends EAUser {

	public ExamBoard(int userID) {
		super(userID);
	}
	
	public int getRole() {
		return super.BOARD;
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
