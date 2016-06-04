package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import helper.DBConnector;
import helper.DBInfo;
import helper.DBConnector.SqlQueryResult;

public class EAUser {
	private int userID;
	//alfnjhsvdjahsvd
	public static final int NO_ROLE = -1;
	public static final int STUDENT = 0;
	public static final int LECTURER = 1;
	public static final int BOARD = 2;
	public static final int ADMIN = 3;
	
	
	public EAUser(int userID) {
		this.userID = userID;
	}

	public int getRole() {
		String role = (String)getUserInfo("role"); 
		switch(role){
			case "board": 
				return BOARD;
			case "student":
				return STUDENT;
			case "Lecturer":
				return LECTURER;
			case "Admin":
				return ADMIN;
			default: return NO_ROLE;
		}
	}
	
	/*
	 * This method receives column name of user table in database
	 * and returns userId corresponding value from userColumn
	 */
	private Object getUserInfo(String userColumn){
		Object res="";
		String getRole = "SELECT "+userColumn+ " from user where UserId=" + userID;
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(getRole);
		if (queryResult != null && queryResult.isSuccess()) {
			try {
				while(queryResult.getResultSet().next()){
					res = queryResult.getResultSet().getString(userColumn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		connector.dispose();
		return res;
	}
	
	public String getMail(){
		return (String)getUserInfo("mail");
	}
	
	public String getUserFirstName(){
		return (String)getUserInfo("firstname");
	}
	
	public String getUserLastName(){
		return (String)getUserInfo("lastname");
	}

	public int getUserID() {
		return userID;
	}
	
	// public void fillUserInfoById(int userID){}
}
