package helper;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import helper.DBConnector.SqlQueryResult;
import models.Exam;
import models.ExamInformation;
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

	/**
	 * gets all the correct exams for the lecturer. Selects only the ones is
	 * related to the given lecturer.
	 * 
	 * @param lecturer
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Exam> getAllExamsForLecturer(Lecturer lecturer) {
		ArrayList<Exam> result = new ArrayList<Exam>();
		String res = "SELECT e.* FROM user as u JOIN userexam as ue on ue.UserID = u.UserID JOIN "
				+ "exam as e on ue.ExamID = e.ExamID WHERE u.UserID = " + lecturer.getUserID()
				+ " ORDER BY e.StartTime";
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(res);
		if (queryResult.isSuccess()) {
			ResultSet rs = queryResult.getResultSet();
			try {
				if (rs.next()) {
					rs.beforeFirst();
					while (!rs.isLast()) {
						Exam exam = new Exam(rs);
						result.add(exam);
					}
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		connector.dispose();
		return result;
	}

	/**
	 * Gets all exams for the exam board. Select every exam from the database,
	 * to be displayed on the exam board servlet.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Exam> getAllExamsForBoard() {
		ArrayList<Exam> res = new ArrayList<Exam>();
		String st = "select * from exam";
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(st);
		if (queryResult.isSuccess()) {
			ResultSet rs = queryResult.getResultSet();
			try {
				while (!rs.isLast()) {
					Exam exam = new Exam(rs);
					res.add(exam);
				}
			} catch (SQLException e) {
				e.printStackTrace();
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
		String startExamQuery = "UPDATE userplace SET StartTime = NOW() WHERE"
				+ " UserExamID IN (SELECT ue.UserExamID FROM userexam as ue"
				+ " JOIN user as u on u.UserID = ue.UserID JOIN exam as e on e.ExamID = ue.ExamID WHERE u.UserID = "
				+ student.getUserID() + " AND e.ExamID = " + exam.getExamID() + ")";
		DBConnector connector = new DBConnector();
		connector.updateDatabase(startExamQuery);
		connector.dispose();
	}

	/**
	 * Get the exam from the database which is in 30 minutes or closer. If
	 * student didn't clicked start exam earlier he is allowed to do so. Else he
	 * has already started exam and should be redirected to the exam page
	 * 
	 * @param student
	 * @param exam
	 * @return
	 */
	public boolean canStartExam(Student student, Exam exam) {
		boolean result = false;
		ExamInformation examInformation = getStudentExamInformation(student, exam);
		if (examInformation.getStartTime() == null) {
			result = true;
		}
		student.setExamInformation(examInformation);
		return result;
	}

	/**
	 * Gets the information about the exam which student is going to write or is
	 * writing now. The method returns Information about exam which the student
	 * is writing for instance the place where he needs to write exam, the ip of
	 * place, variant number and so on
	 * 
	 * @param student
	 * @param exam
	 * @return
	 */
	public ExamInformation getStudentExamInformation(Student student, Exam exam) {
		ExamInformation result = null;
		String examInformationQuery = "SELECT up.*, p.IP, p.Number, p.IsWorking FROM userplace as up JOIN userexam as ue on ue.UserExamID = up.UserExamID"
				+ " JOIN user as u on u.UserID = ue.UserID JOIN exam as e on e.ExamID = ue.ExamID JOIN place as p on p.PlaceID = up.PlaceID WHERE u.UserID = "
				+ student.getUserID() + " AND e.ExamID = " + exam.getExamID() + ";";
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(examInformationQuery);
		if (queryResult.isSuccess()) {
			result = new ExamInformation(queryResult.getResultSet());
		}
		connector.dispose();
		return result;
	}

	/**
	 * Simply gets the exam by its id. returns exam by examID
	 */
	public Exam getExamByExamId(int examID) {
		Exam result = null;
		if (examID > 0) {
			String getExamQuery = "select * from exam where ExamID =" + examID + ";";
			DBConnector connector = new DBConnector();
			SqlQueryResult queryResult = connector.getQueryResult(getExamQuery);
			if (queryResult.isSuccess()) {
				result = new Exam(queryResult.getResultSet());
			}
			connector.dispose();
		}
		return result;
	}

	/**
	 * Modify exam : update info in the exam table after board or lecturer
	 * pressed modify button
	 * 
	 * returns Exam which was added for some tests
	 */
	public Exam modifyExam(int examID, String examName, String openBook, String[] subLecturers, File[] materials,
			int examDuration, int numVariants, String examType, String examStatus) {
		String updateQuery = "update exam set exam.status='" + examStatus + "', exam.Name='" + examName
				+ "', exam.Type='" + examType + "', " + "exam.Duration=" + examDuration + ", exam.ResourceType='"
				+ openBook + "', exam.NumVariants=" + numVariants + " where ExamID=" + examID + ";";

		DBConnector connector = new DBConnector();
		connector.updateDatabase(updateQuery);
		connector.dispose();

		return getExamByExamId(examID);
	}

	/**
	 * Creates new exam : insert into exam new value with setted parameter new
	 * 
	 * returns ExamID which was added
	 */
	public int createNewExam(int lecId, String examName, String openBook, String[] subLecturers, File[] materials,
			int examDuration, int numVariants, String examType) {
		int examId = 0;
		String insertNewExamQuery = "INSERT INTO exam (exam.status, exam.Name, exam.Type, exam.Duration, "
				+ "exam.ResourceType, exam.NumVariants) VALUES ('new', '" + examName + "', '" + examType + "', "
				+ examDuration + ", '" + openBook + "', " + numVariants + ");";
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
	

	/**
	 * Adds new row in the base in the userexam table, which connects each user to the exam.
	 * @param userId
	 * @param examId
	 */
	private void addRowInUserExam(int userId, int examId) {
		String insertQuery = "insert into userexam (UserID, ExamID) values(" + userId + ", " + examId + ");";
		DBConnector connector = new DBConnector();
		connector.updateDatabase(insertQuery);
		connector.dispose();
	}

	/**
	 * Helper method to return exam manager object which is general for all the
	 * sessions. Exam manager object is stored in servlet context and it is
	 * reachable throught http session
	 * 
	 * @param session
	 * @return
	 */
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
