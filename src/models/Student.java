package models;

import java.sql.ResultSet;

public class Student extends EAUser {

	public Student(String username) {
		super(username);
	}

	public Student(ResultSet rs) {
		super(rs);
	}

}