package models;

import java.sql.ResultSet;

public class ExamBoard extends EAUser {

	public ExamBoard(String username) {
		super(username);
	}

	public ExamBoard(ResultSet rs) {
		super(rs);
	}

}
