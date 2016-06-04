package models;

import java.sql.ResultSet;
import helper.DBConnector;
import helper.LogManager;
import helper.DBConnector.SqlQueryResult;

public class EAUser {
	public static final int NO_ROLE = -1;
	public static final int STUDENT = 0;
	public static final int LECTURER = 1;
	public static final int BOARD = 2;
	public static final int ADMIN = 3;

	public enum EAUserRole {
		NO_ROLE, ADMIN, STUDENT, LECTURER, BOARD
	}

	private int userID;
	private EAUserRole role;
	private String mail;
	private String firstName;
	private String lastName;
	private String image;
	private String googleID;

	public EAUser(String username) {
		this.mail = username;
		ResultSet rs = getUserResultSet(username);
		parseUserResultSet(rs);
	}

	public EAUser(ResultSet rs) {
		parseUserResultSet(rs);
	}

	/*
	 * Select data from database and return result set from the according query
	 * or log the error
	 */
	private ResultSet getUserResultSet(String username) {
		ResultSet result = null;
		String getUserQuery = "SELECT * FROM user WHERE Mail =" + username;
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(getUserQuery);
		connector.dispose();
		if (queryResult != null && queryResult.isSuccess()) {
			result = queryResult.getResultSet();
		} else {
			LogManager.logInfoMessage("QueryResult is null OR " + queryResult.getErrorMsg());
		}
		return result;
	}

	/*
	 * parse result set which contains data about user
	 */
	private void parseUserResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				while (rs.next()) {
					this.userID = rs.getInt("UserID");
					this.role = getRoleByString(rs.getString("Role"));
					this.mail = rs.getString("Mail");
					this.firstName = rs.getString("FirstName");
					this.lastName = rs.getString("LastName");
					this.image = rs.getString("Image");
					this.googleID = rs.getString("GoogleID");
				}
			} catch (Exception e) {
				LogManager.logErrorException(3000, "Error parsing ResultSet ", e);
			}
		}
	}

	/*
	 * get the object of EAUserRole type from the roleString; convert string to
	 * enum
	 */
	private EAUserRole getRoleByString(String roleString) {
		switch (roleString) {
		case "board":
			return EAUserRole.valueOf("BOARD");
		case "student":
			return EAUserRole.valueOf("STUDENT");
		case "lecturer":
			return EAUserRole.valueOf("LECTURER");
		case "admin":
			return EAUserRole.valueOf("ADMIN");
		default:
			return EAUserRole.valueOf("NO_ROLE");
		}
	}

	public EAUserRole getRole() {
		return this.role;
	}

	public String getMail() {
		return this.mail;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public int getUserID() {
		return this.userID;
	}

	public String getImage() {
		return this.image;
	}

	public String getGoogleID() {
		return this.googleID;
	}
}
