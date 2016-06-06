package helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import helper.DBConnector;
import helper.DBConnector.SqlQueryResult;
import helper.LogManager;
import models.EAUser;
import models.Lecturer;
import models.EAUser.EAUserRole;
import models.ExamBoard;
import models.Student;

/** main class responsible for all operations over Accounts */
public class AccountManager {

	/** if retuned EAUser == NO_USER_FOUND_CONSTANT that means user wasnot found
	 *  in db*/
	public static final EAUser NO_USER_FOUND_CONSTANT = new Student(null); 
	//public static final String USER_ID_IN_SESSION = "AccountManager.USERID"; 
	
	
	public AccountManager() {

	}
	
	
	/******************************/
	/******** get user  ***********/
	/******************************/


	/** returns EAUser for given httpsession */
	public EAUser getCurrentUser(HttpSession httpSession) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**************************/
	/******** Login ***********/
	/**************************/

	/**
	 * For given @userName and @password returns OpResult containing EaUser In
	 * case of Sucess as an result.
	 * If No error happened but user wasnot found @NO_USER_FOUND_CONSTANT is returned 
	 * as an result EAUser.
	 * 
	 * In case of Falture error is saved in OpResult
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
				EAUser user = getEAUserType(rs);//get user
				result.setResultObject(user);//set return object 
				saveUserInLocalCache(user, httpSession);
			}
		} else {
			//set error its same sqlqueryresult has 
			result.setError(queryResult.getErrorId(), queryResult.getErrorMsg());
		}
		connector.dispose();
		return result;
	}
	
	/* saves user in local cache by its session */
	private void saveUserInLocalCache(EAUser user, HttpSession httpSession){
		
	}

	/*
	 * returns sql query for getting user info based on its username and
	 * password
	 */
	private String getSqlQueryForUserInfo(String userName, String Password) {
		return "select * from examassistant.user where Mail = " + "'" + userName + "'" + "and UserPassword = '"
				+ Password + "'";
	}

	/* for given resultset @rs returns EAUser type of instance of its 
	 *  Role. Admin/ExamBoard/Lecturer/Student */
	private EAUser getEAUserType(ResultSet rs) {
		EAUser user = null;
		try {
			EAUserRole role = EAUserRole.NO_ROLE;
			if (rs.next()) {
				role = (EAUser.getRoleByString(rs.getString("Role")));
			}
			rs.beforeFirst();
			user = getUserByRole(role, rs);
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

}
