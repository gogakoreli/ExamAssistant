package models;

import java.sql.ResultSet;
import helper.LogManager;

public abstract class EAUser {

	/*
	 * user id for not valid EaUser this means that user was not created
	 * successfully
	 */
	public static final int NO_USER_ID = -1;

	/** enum for roles of Ea User */
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

	/**
	 * Downlaods additional information based on what kind of user it is For
	 * Example for Leqturer it may be phone number and so and for Student single
	 * exam he needs to Attand
	 */
	public abstract void downloadAditionalInfo();

	/**
	 * for given resultset fills its inside paramethers using them this is
	 * common data same for all kinds of users like username, mail and so. if
	 * null passed creates simple instance
	 */
	public EAUser(ResultSet rs) {
		if (rs != null) {
			parseUserResultSet(rs);
		}
	}

	public EAUser(int userID, EAUserRole role, String mail, String firstName, String lastName, String image,
			String googleID) {
		this.userID = userID;
		this.role = role;
		this.mail = mail;
		this.firstName = firstName;
		this.lastName = lastName;
		this.image = image;
		this.googleID = googleID;
	}

	public EAUser() {

	}

	/*
	 * if null isnot returned parse result set which contains data about user
	 */
	private void parseUserResultSet(ResultSet rs) {
		try {
			if (rs.next()) {
				setFirstName(rs.getString("FirstName"));
				setGoogleID(rs.getString("GoogleID"));
				setImage(rs.getString("Image"));
				setLastName(rs.getString("LastName"));
				setMail(rs.getString("Mail"));
				setRole(getRoleByString(rs.getString("Role")));
				setUserID(rs.getInt("UserID"));
			} else {
				setUserID(NO_USER_ID);
			}
		} catch (Exception e) {
			LogManager.logErrorException(3000, "Error parsing ResultSet ", e);
		}
	}

	/**
	 * get the object of EAUserRole type from the roleString; convert string to
	 * enum
	 */
	public static EAUserRole getRoleByString(String roleString) {
		if (roleString == null)
			return EAUserRole.valueOf("NO_ROLE");
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

	/*****************************/
	/***** getters/setters ******/
	/*****************************/

	/** set EaUserId for EaUser */
	public void setUserID(int userID) {
		this.userID = userID;
	}

	/** setRole for EaUser */
	public void setRole(EAUserRole role) {
		this.role = role;
	}

	/** set Mail for EaUser */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/** set First Name for EaUser */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/** set Last Name for EaUser */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/** set Image for EaUser */
	public void setImage(String image) {
		this.image = image;
	}

	/** setGoogleId for EaUser */
	public void setGoogleID(String googleID) {
		this.googleID = googleID;
	}

	/** gets role of EaUser */
	public EAUserRole getRole() {
		return this.role;
	}

	/** gets Mail of EaUser */
	public String getMail() {
		return this.mail;
	}

	/** gets First Name of EaUser */
	public String getFirstName() {
		return this.firstName;
	}

	/** gets Last Name of EaUser */
	public String getLastName() {
		return this.lastName;
	}

	/** gets id of EaUser */
	public int getUserID() {
		return this.userID;
	}

	/** gets imagePath of EaUser */
	public String getImage() {
		return this.image;
	}

	/** gets googleId of EaUser */
	public String getGoogleID() {
		return this.googleID;
	}

	/**
	 * override of toString() writes its user and eMAIL
	 */
	@Override
	public String toString() {
		return "username = " + getFirstName() + "  Email = " + getMail();
	}

	/**
	 * override of equals method we suppose 2 acc are equal if they have same id
	 */
	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || (obj.getClass() != this.getClass())) {
			return false;
		}

		return ((Lecturer) obj).getUserID() == this.getUserID();
	}

	@Override
	public int hashCode() {
		return ("" + getUserID()).hashCode();
	}

}
