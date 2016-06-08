package helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import helper.DBConnector.SqlQueryResult;
import models.Exam;
import models.Lecturer;
import models.Student;

public class ExamManager {

	private Map<Integer, Exam> exams;

	public ExamManager() {
		exams = new HashMap<Integer, Exam>();
	}

	/**
	 * Gets exam for the parameter student; Retrieves data from database and
	 * select exam which is the closest to now and between specific date stamp;
	 * Exams are stored in hashmap and students writing same exam have same exam
	 * object
	 * 
	 * @param student
	 * @return Exam which student has to write
	 */
	public Exam getExamForStudent(Student student) {
		Exam result = null;
		String getExamQuery = getExamForStudentQuery(student.getUserID());
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(getExamQuery);
		if (queryResult.isSuccess()) {
			Exam tmpExam = new Exam(queryResult.getResultSet());
			if (tmpExam != null) {
				result = getExamFromHashMap(tmpExam);
			}
		}
		connector.dispose();
		return result;
	}

	/**
	 * This is synchronized method that adds exam to the exam list if it is not
	 * there or returns the exam from the exam list if it contains exam; So that
	 * every student doesn't have own exam object
	 * 
	 * @param exam
	 *            which needs to be checked
	 * @return Exam which is stored in the exam list (hashmap) inside
	 *         ExamManager
	 */
	private synchronized Exam getExamFromHashMap(Exam exam) {
		Exam result = exam;
		int examID = result.getExamID();
		if (exams.containsKey(examID)) {
			result = exams.get(examID);
		} else {
			exams.put(examID, result);
		}
		return result;
	}

	/**
	 * Query string for selecting closest exam specific to the userID
	 * 
	 * @param userID
	 * @return String of the query which is sent to DB
	 */
	private String getExamForStudentQuery(int userID) {
		String result = "SELECT e.* FROM user as u LEFT JOIN userexam as ue on ue.UserID = u.UserID LEFT JOIN "
				+ "exam as e on ue.ExamID = e.ExamID WHERE u.UserID = " + userID
				+ " AND Status != 'Processing' ORDER BY e.StartTime asc LIMIT 1";
		return result;
	}

	public ArrayList<Exam> getAllExamsForLecturer(Lecturer lecturer) {
		ArrayList<Exam> result = new ArrayList<Exam>();

		return result;
	}

	public ArrayList<Exam> getAllExamsForBoard(Lecturer lecturer) {
		ArrayList<Exam> result = new ArrayList<Exam>();

		return result;
	}

	/**
	 * Start exam : update info in the database that user clicked start exam
	 * button so timer begins from now
	 * 
	 * @param student
	 * @param exam
	 */
	public void startExam(Student student, Exam exam) {

	}

	public static ExamManager getExamManager(HttpSession session) {
		ExamManager result = null;
		ServletContext context = session.getServletContext();
		Object object = context.getAttribute(ContextStartupListener.EXAM_MANEGER_ATTRIBUTE_NAME);
		if (object instanceof ExamManager) {
			result = (ExamManager) object;
		}
		return result;
	}
}
