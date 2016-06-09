package helper;

import javax.servlet.ServletContext;

/** Main class used for logging Errors and info messages in Apache system */
public class LogManager {

	private static ServletContext contextForLogging = null;
	private static boolean EnibleLogging  = true;
	private static int NO_ERROR_ID = 0; // used when no error id is passed 
	
	/**
	 * sets ServletContext for LogManager, its used to log messages in Apache
	 * Log system
	 */
	public static void setServletContext(ServletContext context) {
		LogManager.contextForLogging = context;
	}

	/** Logs given message in Apache log system as an Info Message */
	public static void logInfoMessage(String message) {
		if (checkLogAvaliable())
			contextForLogging.log(message);
		System.out.println(message);
	}

	/**
	 * Logs given message and exception in Apache log system as an Error Message
	 * without showing Error id. Passing Error id is recomended.
	 * if null passed as an exception Logs Just given Message as an Error
	 */
	public static void logErrorException(String message, Exception error) {
		logErrorException(NO_ERROR_ID, message, error);
	}
	
	/**
	 * Logs given message and exception in Apache log system as an Error Message
	 * with its id @ErrorId
	 * if null passed as an exception Logs Just given Message as an Error
	 */
	public static void logErrorException(int ErrorId, String message, Exception error) {
		//print error id if passed 
		if (ErrorId != NO_ERROR_ID)
			message += "Error id = " + ErrorId + "  Message : " + message;
		if (checkLogAvaliable())
			contextForLogging.log(message, error);
		System.out.println(message);
	}
	
	/* checks if logging system is avaliable its if someone forgot to set 
	 * servlet context for logger or log is disabled by default */
	private static boolean checkLogAvaliable(){
		return EnibleLogging && contextForLogging != null;
	}

}
