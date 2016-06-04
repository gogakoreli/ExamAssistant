package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
	private Connection connection;

	public DBConnector() {
		connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + DBInfo.MYSQL_DATABASE_SERVER,
					DBInfo.MYSQL_USERNAME, DBInfo.MYSQL_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns new Connection to Database Shoud be closed after using
	 */
	public Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + DBInfo.MYSQL_DATABASE_SERVER,
					DBInfo.MYSQL_USERNAME, DBInfo.MYSQL_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	public SqlQueryResult getQueryResult(String query) {
		SqlQueryResult queryResult = null;
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				stmt.executeQuery("USE " + DBInfo.MYSQL_DATABASE_NAME);
				ResultSet rs = stmt.executeQuery(query);
				queryResult = new SqlQueryResult(rs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}

	public void updateDatabase(String query) {
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				stmt.executeQuery("USE " + DBInfo.MYSQL_DATABASE_NAME);
				stmt.executeUpdate(query);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void dispose() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class SqlQueryResult {

		boolean isSuccess = true;
		String errorMsg = "";
		int errorId = 0;
		ResultSet resultSet;

		public SqlQueryResult(ResultSet resultSet) {
			this.resultSet = resultSet;
			isSuccess = true;
		}

		public SqlQueryResult(String errorMsg) {
			this.errorMsg = errorMsg;
			this.isSuccess = false;
			this.errorId = 404;
		}

		public boolean isSuccess() {
			return isSuccess;
		}

		public ResultSet getResultSet() {
			return resultSet;
		}

		public String getErrorMsg() {
			return errorMsg;
		}
	}

}
