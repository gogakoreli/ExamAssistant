package models;

import java.sql.ResultSet;

public class Lecturer extends EAUser{

	
	public Lecturer(ResultSet rs) {
		super(rs);
	}
	
	
	public Lecturer() {
		super();
	}


	@Override
	public void downloadAditionalInfo() {
		// TODO Auto-generated method stub
		
	}

}
