package helper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import helper.DBConnector.SqlQueryResult;
import models.Exam;
import models.Lecturer;
import models.Student;

public class ExamManager {

	private Map<Integer, Exam> exams;

	public ExamManager() {
		exams = new HashMap<Integer, Exam>();
	}

	public Exam getExamForStudent(Student student) {
		Exam result = null;
		String getExamQuery = "SELECT e.* FROM user as u LEFT JOIN "
				+ "userexam as ue on ue.UserID = u.UserID LEFT JOIN "
				+ "exam as e on ue.ExamID = e.ExamID WHERE u.UserID = " + student.getUserID()
				+ " ORDER BY e.StartTime asc LIMIT 1";
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(getExamQuery);
		if (queryResult.isSuccess()) {
			Exam tmpExam = new Exam(queryResult.getResultSet());
			if (tmpExam != null) {
				result = tmpExam;
			}
		}
		connector.dispose();
		return result;
	}

	private synchronized void addExamToMap(Exam exam) {
		
	}
	
	public ArrayList<Exam> getAllExamsForLecturer(Lecturer lecturer)
	{
		ArrayList<Exam> result = new ArrayList<Exam>();
		
		return result;
	}
	
	public ArrayList<Exam> getAllExamsForBoard(Lecturer lecturer)
	{
		ArrayList<Exam> result = new ArrayList<Exam>();
		
		return result;
	}

	// public Exam getExamQuery(Student student) {
	//
	// DBConnector connector = new DBConnector();
	// SqlQueryResult queryResult = connector.getQueryResult(getExamQuery);
	// connector.dispose();
	// if (queryResult != null && queryResult.isSuccess()) {
	// ResultSet rs = queryResult.getResultSet();
	// result = new Exam(rs);
	// } else {
	// LogManager.logInfoMessage("QueryResult is null OR " +
	// queryResult.getErrorMsg());
	// }
	// return result;
	// }
}
