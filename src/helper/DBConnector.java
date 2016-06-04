package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** main class responsible for connection with database */
public class DBConnector {
	private Connection connection;

	/**
	 * Creates DBConnector Object and automatically initializes new connection
	 * with database. This connection will be alive until class dies and will
	 * automatically be disposed. Closing this connection manually is not
	 * recommended.
	 * 
	 * If single connection is needed use static getNewDbConnection function
	 * instead.
	 */
	public DBConnector() {
		connection = getNewDbConnection();
	}

	/**
	 * Creates and returns new Connection with database. Connection should be
	 * closed Manually after using with conn.Close() method.
	 * 
	 * Null will be returned if Error occurs while create connection
	 */
	public static Connection getNewDbConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + DBInfo.MYSQL_DATABASE_SERVER,
					DBInfo.MYSQL_USERNAME, DBInfo.MYSQL_PASSWORD);
		} catch (Exception e) {
			LogManager.logErrorException(1000, "Error Connection to Database ", e);
			return null;
		}
		return connection;
	}

	/** checks if database connection is still alive */
	public boolean isDBConnection() {
		try {
			return (connection != null) && (connection.isValid(0));
		} catch (SQLException e) {
			// error validation connection
			return false;
		}
	}

	/**
	 * occures before object is destroyed by gb if connection is not closed at
	 * this time its
	 */
	@Override
	public void finalize() {
		if (isDBConnection()) {
			try {
				connection.close();
			} catch (SQLException ignored) {
			}
			LogManager.logErrorException(1001, "Db connection not closed properly ", new Exception("Db Not Closed"));
		}
	}

	/** closes database connection */
	public void dispose() {
		try {
			if (isDBConnection())
				connection.close();
		} catch (Exception e) {
			LogManager.logErrorException(1002, "Error Closing db Connection", new Exception("Error"));
		}
	}

	public SqlQueryResult getQueryResult(String query) {
		SqlQueryResult queryResult = null;
		if (isDBConnection()) {
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
		if (isDBConnection()) {
			try {
				Statement stmt = connection.createStatement();
				stmt.executeQuery("USE " + DBInfo.MYSQL_DATABASE_NAME);
				stmt.executeUpdate(query);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
