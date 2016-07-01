package data_managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

//import com.sun.swing.internal.plaf.synth.resources.synth;

import listeners.ContextStartupListener;
import helper.DBConnector;
import helper.DBConnector.SqlQueryResult;
import listeners.ContextStartupListener;
import helper.LogManager;
import helper.OpResult;
import models.EAUser;
import models.Lecturer;
import models.EAUser.EAUserRole;
import models.ExamBoard;
import models.Student;
import servlets.LecturerServlet;

/** main class responsible for all operations over Accounts */
public class AccountManager {

	/**
	 * if retuned EAUser == NO_USER_FOUND_CONSTANT that means user wasnot found
	 * in db
	 */
	public static final EAUser NO_USER_FOUND_CONSTANT = new Student(null);
	public static final String USER_ID_IN_SESSION = "AccountManager.USERID";

	/* //how many lectueres should be displayed */
	private static final int LECTUER_SUGGESTIONS_LIMIT = 5;

	// hashmap storing logedinusers
	private Map<String, EAUser> logedUsers = new HashMap<String, EAUser>();

	public AccountManager() {

	}

	/******************************/
	/******** get user ***********/
	/******************************/

	/**
	 * returns EAUser for given httpsession if its not in logedinusers returns
	 * null
	 */
	public EAUser getCurrentUser(HttpSession httpSession) {
		if (logedUsers.containsKey(httpSession.getId()))
			return (EAUser) httpSession.getAttribute(USER_ID_IN_SESSION);
		return null;
	}

	/** removes user for given httpsession from loged in system */
	public void removeCurrentUser(HttpSession httpSession) {
		// TODO check if user quit without saving info (shemtxvevit tu gavarda )
		httpSession.removeAttribute(USER_ID_IN_SESSION);
		removeUserFromLogedUsers(httpSession.getId());

	}

	/* removes user from loged users (locks it first then removes it ) */
	private void removeUserFromLogedUsers(String id) {
		synchronized (logedUsers) {
			logedUsers.remove(id);
		}
	}

	/* puts user in loged in users (locks it first ) */
	private void putUserInLogedUsers(String id, EAUser user) {
		synchronized (logedUsers) {
			logedUsers.put(id, user);
		}
	}

	/**
	 * public static EAUser getCurrentUser(HttpSession httpSession) {
	 * AccountManager accountManager = (AccountManager)
	 * httpSession.getServletContext()
	 * .getAttribute(ContextStartupListener.ACCOUNT_MANEGER_ATTRIBUTE_NAME); }
	 */

	/**
	 * public static EAUser getCurrentUser(HttpServletRequest request) { return
	 * getCurrentUser(request.getSession()); }
	 */

	/**************************/
	/******** Login ***********/
	/**************************/

	/**
	 * For given @userName and @password returns OpResult containing EaUser In
	 * case of Sucess as an result. If No error happened but user wasnot
	 * found @NO_USER_FOUND_CONSTANT is returned as an result EAUser.
	 * 
	 * In case of Falture error is saved in OpResult
	 * 
	 * @param httpSession
	 */
	public OpResult<EAUser> getEAUserForCreditials(String userName, String password, HttpSession httpSession) {

		OpResult<EAUser> result = new OpResult<EAUser>();
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(getSqlQueryForUserInfo(userName, password));
		if (queryResult.isSuccess()) {
			ResultSet rs = queryResult.getResultSet();

			if (isResultSetEmpty(rs)) {// check if no user found
				result.setResultObject(NO_USER_FOUND_CONSTANT);
			} else {
				EAUser user = getEAUserType(rs);// get user
				result.setResultObject(user);// set return object
				saveUserInLocalCache(user, httpSession);
			}
		} else {
			// set error its same sqlqueryresult has
			result.setError(queryResult.getErrorId(), queryResult.getErrorMsg());
		}
		connector.dispose();
		return result;
	}

	/**
	 * returns OpResult<EAUser> from database using users id if user is not
	 * found returns result with NO_USER_FOUND_CONSTANT
	 * 
	 * @param userId
	 */
	public OpResult<EAUser> getUserById(int userId) {
		OpResult<EAUser> result = new OpResult<EAUser>();
		DBConnector connector = new DBConnector();
		String getUserQuery = "select * from user where UserID = " + userId + ";";
		SqlQueryResult queryResult = connector.getQueryResult(getUserQuery);

		if (queryResult.isSuccess()) {
			ResultSet rs = queryResult.getResultSet();
			if (isResultSetEmpty(rs)) {// check if no user found
				result.setResultObject(NO_USER_FOUND_CONSTANT);
			} else {
				EAUser user = getEAUserType(rs);// get user
				result.setResultObject(user);// set return object
			}
		}
		connector.dispose();
		return result;
	}

	/**
	 * adds user in user table in database ar itvaliswinebs aseti useri aris tu
	 * ara bazashi amis shesamowmeblad shegizliat gamoiyenot userExists() metodi
	 * 
	 * @param userName
	 * @param password
	 * @param role
	 * @param firstName
	 * @param lastName
	 */
	public OpResult<EAUser> addUser(String userName, String password, String role, String firstName, String lastName) {
		DBConnector connector = new DBConnector();
		String addUserQuery = "insert into user (Mail, UserPassword, FirstName, LastName, Role) " + "values ('"
				+ userName + "', '" + password + "', '" + firstName + "', '" + lastName + "', '" + role + "');";
		connector.updateDatabase(addUserQuery);
		String getLastIdQuery = "select LAST_INSERT_ID() as lastInsertId;";
		SqlQueryResult queryResult = connector.getQueryResult(getLastIdQuery);

		int lastInd = 0;

		if (queryResult.isSuccess()) {
			ResultSet res = queryResult.getResultSet();
			try {
				if (res.next())
					lastInd = res.getInt("lastInsertId");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		connector.dispose();
		return getUserById(lastInd);
	}

	/**
	 * returns true if we have such user in database
	 * 
	 * @param userName
	 * @param password
	 */

	public boolean userExists(String userName, String password) {
		DBConnector connector = new DBConnector();
		String findQuery = "select * from user where mail='" + userName + "'" + " and UserPassword='" + password + "';";
		SqlQueryResult queryResult = connector.getQueryResult(findQuery);

		if (queryResult.isSuccess()) {
			ResultSet res = queryResult.getResultSet();
			try {
				if (res.next())
					return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		connector.dispose();
		return false;
	}

	/**
	 * removes user from database using users id ar itvaliswinebs aseti useri
	 * aris tu ara bazashi amis shesamowmeblad shegizliat gamoiyenot
	 * userExists() metodi
	 * 
	 * @param userId
	 */
	public void removeUserByID(int userId) {
		DBConnector connector = new DBConnector();
		String removeQuery = "DELETE FROM user WHERE UserID=" + userId + ";";
		connector.updateDatabase(removeQuery);
		connector.dispose();
	}

	/* saves user in local cache by its session */
	private void saveUserInLocalCache(EAUser user, HttpSession httpSession) {
		if (httpSession == null) {
			LogManager.logInfoMessage("Null Http Session Are you testing ? ");
			return;
		}
		httpSession.setAttribute(USER_ID_IN_SESSION, user);
		putUserInLogedUsers(httpSession.getId(), user);

	}

	/*
	 * returns sql query for getting user info based on its username and
	 * password
	 */
	private String getSqlQueryForUserInfo(String userName, String Password) {
		return "select * from examassistant.user where Mail = " + "'" + userName + "'" + "and UserPassword = '"
				+ Password + "'";
	}

	/*
	 * for given resultset @rs returns EAUser type of instance of its Role.
	 * Admin/ExamBoard/Lecturer/Student
	 */
	private EAUser getEAUserType(ResultSet rs) {
		EAUser user = null;
		try {
			EAUserRole role = EAUserRole.NO_ROLE;
			if (rs.next()) {
				role = (EAUser.getRoleByString(rs.getString("Role")));
			}
			rs.beforeFirst();
			user = getUserByRole(role, rs);
			user.downloadAditionalInfo();
		} catch (SQLException e) {
			LogManager.logErrorException(1010, "Error creating EAUser from resultset", e);
		}
		return user;
	}

	/* returns EAUser instance based on its role */
	private EAUser getUserByRole(EAUserRole role, ResultSet rs) {
		switch (role) {
		case BOARD:
			return new ExamBoard(rs);
		case LECTURER:
			return new Lecturer(rs);
		case STUDENT:
			return new Student(rs);
		default:
			return null;
		}
	}

	/* checks if given resultset is empty and moves its cursor to its start */
	private boolean isResultSetEmpty(ResultSet rs) {
		boolean result = true;
		try {
			result = !rs.next();
			rs.beforeFirst();
		} catch (SQLException e) {
			LogManager.logErrorException(1010, "Error checking resultset empty", e);
		}
		return result;
	}

	/**
	 * Helper method to return account manager object which is general for all
	 * the sessions.
	 * 
	 * @param session
	 * @return
	 */
	public static AccountManager getAccountManager(HttpSession session) {
		AccountManager result = null;
		ServletContext context = session.getServletContext();
		Object object = context.getAttribute(ContextStartupListener.ACCOUNT_MANEGER_ATTRIBUTE_NAME);
		if (object instanceof AccountManager) {
			result = (AccountManager) object;
		}
		return result;
	}

	/**
	 * returns OpResult containing list of lecturers who's names start with
	 * 
	 * @nameStart this lectures will only contain basic informaiton like fname,
	 *            lname and Email.
	 */
	public OpResult<List<Lecturer>> getLecturersByNameStart(String nameStart) {
		OpResult<List<Lecturer>> result = new OpResult<List<Lecturer>>();
		DBConnector connector = new DBConnector();

		SqlQueryResult queryResult = connector.getQueryResult(getSqlQueryForLecturersSug(nameStart));
		if (queryResult.isSuccess()) {
			List<Lecturer> lecturers = null;
			try {
				lecturers = getLectuersFromResultSet(queryResult.getResultSet());
				result.setResultObject(lecturers);
			} catch (Exception e) {
				// data retrieved but error parsing
				LogManager.logErrorException(2000, "Error parsing data from lecturers suggestions", e);
				result.setError(2000, "Error parsing lecturers info");
			}
		} else {
			// set error its same SqlQueryResult has
			result.setError(queryResult.getErrorId(), queryResult.getErrorMsg());
		}
		connector.dispose();
		return result;
	}

	/* gets list of lecturers from result set throws @SQLException */
	private List<Lecturer> getLectuersFromResultSet(ResultSet rs) throws SQLException {
		List<Lecturer> lecturers = new ArrayList<Lecturer>();
		while (rs.next()) {
			Lecturer curLec = new Lecturer();
			curLec.setFirstName(rs.getString("fname"));
			curLec.setUserID(rs.getInt("uid"));
			curLec.setLastName(rs.getString("lname"));
			curLec.setMail(rs.getString("mail"));
			lecturers.add(curLec);
		}
		return lecturers;
	}

	/*
	 * returns query string for given name @nameStart which will retrive all
	 * lecturers starting on @nameStart
	 */
	private String getSqlQueryForLecturersSug(String nameStart) {
		return "SELECT UserID as uid, Mail as mail, FirstName as fname, LastName as lname FROM examassistant.user "
				+ "where role = 'lecturer'" + "and ((CONCAT(FirstName, ' ', LastName) LIKE '" + nameStart + "%') OR"
				+ "(CONCAT(LastName, ' ', FirstName) LIKE '" + nameStart + "%')) LIMIT " + LECTUER_SUGGESTIONS_LIMIT
				+ ";";
	}
}
