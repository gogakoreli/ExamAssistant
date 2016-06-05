package helper;

import java.sql.ResultSet;

import helper.DBConnector.SqlQueryResult;
import models.EAUser;
import models.Exam;

public class AccountManager {

	public AccountManager() {

	}

	public EAUser getEAUserForCreditials(String userName, String password) {
		EAUser result = null;
		String getUserQuery = "SELECT * FROM user WHERE Mail = '" + userName + "'";
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(getUserQuery);
		if (queryResult != null && queryResult.isSuccess()) {
			ResultSet rs = queryResult.getResultSet();
			result = new EAUser(rs);
		} else {
			LogManager.logInfoMessage("QueryResult is null OR " + queryResult.getErrorMsg());
		}
		connector.dispose();
		return result;
	}
}
