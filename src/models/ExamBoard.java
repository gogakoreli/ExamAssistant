package models;

import java.sql.ResultSet;

public class ExamBoard extends EAUser {

	public ExamBoard(ResultSet rs) {
		super(rs);
		
	}
	
	public ExamBoard() {
		super();
		
	}

	@Override
	public void downloadAditionalInfo() {
		// TODO Auto-generated method stub
		
	}
	

}
