package helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import helper.DBConnector;
import helper.DBConnector.SqlQueryResult;
import helper.LogManager;
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

	// TODO make threadsafe account manager xeli ar axlot aravin !!!!!!

	/******************************/
	/******** get user ***********/
	/******************************/

	/** returns EAUser for given httpsession */
	public EAUser getCurrentUser(HttpSession httpSession) {
		return (EAUser) httpSession.getAttribute(USER_ID_IN_SESSION);
	}

	/** removes user for given httpsession from loged in system */
	public void removeCurrentUser(HttpSession httpSession) {
		httpSession.removeAttribute(USER_ID_IN_SESSION);
		logedUsers.remove(httpSession.getId());

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

	/* saves user in local cache by its session */
	private void saveUserInLocalCache(EAUser user, HttpSession httpSession) {
		if (httpSession == null) {
			LogManager.logInfoMessage("Null Http Session Are you testing ? ");
			return;
		}
		httpSession.setAttribute(USER_ID_IN_SESSION, user);
		logedUsers.put(httpSession.getId(), user);
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
