package helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.spi.DirStateFactory.Result;
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

	public ArrayList<Exam> getAllExamsForBoard() throws SQLException {
		ArrayList<Exam> res = new ArrayList<Exam>();
		String st = "select * from exam";
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(st);

		if (queryResult.isSuccess()) {
			ResultSet rs = queryResult.getResultSet();

			while (!rs.isLast()) {
				Exam exam = new Exam(rs);
				res.add(exam);
			}
		}
		connector.dispose();
		return res;
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
	
	/**
	 * returns exam by examID
	 */
	public Exam getExamByExamId(int examId){
		Exam result = null;
		String getExamQuery = "select * from exam where ExamID ="+examId+";";
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(getExamQuery);
		if (queryResult.isSuccess()) {
			result = new Exam(queryResult.getResultSet());
		}
		connector.dispose();
		return result;
	}

	/**
	 * Modify exam : update info in the exam table after board or lecturer
	 * pressed modify button
	 * 
	 * returns Exam which was added for some tests
	 */
	public Exam modifyExam(int examID, boolean openBook, String[] subLecturers, File[] materials, Time startTime,
			double examDuration) {
		// TODO Auto-generated constructor stub
		
		return null;
	}

	/**
	 * Creates new exam : insert into exam new value with setted parameter new
	 * 
	 * returns ExamID which was added
	 */
	public int createNewExam(Lecturer lec) {
		int lecId = lec.getUserID();
		int examId = 0;
		String insertNewExamQuery = "INSERT INTO exam (exam.status) VALUES ('new');";
		DBConnector connector = new DBConnector();
		connector.updateDatabase(insertNewExamQuery);

		String getLastIdQuery = "select LAST_INSERT_ID() as lastInsertId;";
		SqlQueryResult queryResult = connector.getQueryResult(getLastIdQuery);
		if (queryResult.isSuccess()) {
			ResultSet res = queryResult.getResultSet();
			try {
				if (res.next())
					examId = res.getInt("lastInsertId");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		connector.dispose();

		addRowInUserExam(lecId, examId);
		return examId;
	}

	private void addRowInUserExam(int userId, int examId) {
		String insertQuery = "insert into userexam (UserID, ExamID) values(" + userId + ", " + examId + ");";
		DBConnector connector = new DBConnector();
		connector.updateDatabase(insertQuery);
		connector.dispose();
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
