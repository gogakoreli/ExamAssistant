package models;

import java.sql.ResultSet;

public class Admin extends EAUser {

	public Admin(String username) {
		super(username);
	}

	public Admin(ResultSet rs) {
		super(rs);
	}

}
