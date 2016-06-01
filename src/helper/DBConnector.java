package helper;

import java.sql.Connection;

public class DBConnector {

	
	/** returns new Connection to Database 
	 *  Shoud be closed after using */
	public Connection getConnection(){
		return null;
	}
	
	
	public SqlQueryResult getQueryResult(String result){
		return null;
	}
	
	
	public class SqlQueryResult{
		
		boolean isSucess = true;
		String errorMsg = "";
		int errorId = 0;
		
		public boolean isSucess(){
			return isSucess;
		}
		
		public void setError(String errorMsg, int errorId){
			isSucess = false;
			this.errorId = errorId;
			this.errorMsg =errorMsg;
		}
	}
	

	
	
}
