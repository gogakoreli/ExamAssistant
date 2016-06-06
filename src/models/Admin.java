package models;

import java.sql.ResultSet;

public class Admin extends EAUser {


	public Admin(ResultSet rs) {
		super(rs);
	}

	@Override
	public void downloadAditionalInfo() {
		// TODO Auto-generated method stub
		
	}

}
