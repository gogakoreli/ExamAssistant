package helper;

/** class used for communication between any 2 methods. 
 *  used when requesting any operation result from one method for another
 *  to it knows if something was wrong and knows its Error Message  */
public class OpResult<T> {

	
	private boolean isSuccess = true;//store op success
	private String errorMsg = "";
	private int errorId = 0;
	private T result;//in case of success stores result 

	public OpResult() {
	}

	/** checks if operation returned with success */
	public boolean isSuccess() {
		return isSuccess;
	}

	/** retuns result of operation in case of success */
	public T getOpResult() {
		return result;
	}

	/** returns error message in case of falture */
	public String getErrorMsg() {
		return "Error Id = " + errorId + ". " + errorMsg;
	}
	
	/** returns Error Id in case of falture*/
	public int getErrorId(){
		return errorId;
	}
	
	/** sets error of OpResult and its flag of sucess to false */
	public void setError(int errorId, String ErrorMsg){
		isSuccess = false;
		this.errorId = errorId;
		errorMsg = ErrorMsg;
	}
	
	/** sets result object of OpOperation in case of success*/
	public void setResultObject (T result){
		this.result = result;
	}
	
	
	
}
