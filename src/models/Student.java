package models;

import java.sql.ResultSet;

public class Student extends EAUser {

	public Student(ResultSet rs) {
		super(rs);
	}

	@Override
	public void downloadAditionalInfo() {
		// TODO Auto-generated method stub
		
	}

}