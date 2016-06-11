package models;

import java.sql.ResultSet;

public class Admin extends EAUser {


	public Admin(ResultSet rs) {
		super(rs);
	}
	
	public Admin() {
		super();
	}

	@Override
	public void downloadAditionalInfo() {
		// TODO Auto-generated method stub
		
	}

}
