package models;

import java.sql.ResultSet;

public class Lecturer extends EAUser {

	public Lecturer(String username) {
		super(username);
	}

	public Lecturer(ResultSet rs) {
		super(rs);
	}

}
