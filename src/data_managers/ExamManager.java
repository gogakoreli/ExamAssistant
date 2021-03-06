
package data_managers;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import helper.DBConnector;
import helper.DBConnector.SqlQueryResult;
import listeners.ContextStartupListener;
import models.EAUser;
import models.Exam;
import models.ExamInformation;
import models.ExamMaterial;
import models.Lecturer;
import models.Student;

public class ExamManager {

	public static final int NO_EXAM_ID = -1;// id of exam which is not found in
											// db
	public static final Exam WRONG_EXAM = new Exam(NO_EXAM_ID);

	public static final int NEW_EXAM_ID = 0;// id of new empty exam
	public static final Exam EMPTY_EXAM = new Exam(NEW_EXAM_ID);

	public static final String MATERIAL_BOOKS = "book";
	public static final String MATERIAL_VARIANTS = "variant";
	public static final String MATERIAL_STUDENTS_LIST = "student_list";

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
		String getExamQuery = "SELECT e.* FROM user as u LEFT JOIN userexam as ue on ue.UserID = u.UserID LEFT JOIN "
				+ "exam as e on ue.ExamID = e.ExamID WHERE u.UserID = " + student.getUserID()
				+ " AND Status != 'Processing'"
				+ " AND now() between StartTime and date_add(StartTime, interval e.Duration minute)"
				+ " ORDER BY e.StartTime asc LIMIT 1";
		// AND now() between date_add(StartTime, interval -1 minute) and date_add(StartTime, interval 15 minute) 
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
	 * First check if the user has started exam before or not. Start exam :
	 * update info in the database that user clicked start exam button so timer
	 * begins from now
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
		updateStudentExamInformation(student, exam, connector);
		if (student.getExamStartTime() == null) {
			connector.updateDatabase(startExamQuery);
		}
		connector.dispose();
	}

	/**
	 * Changes exams status to started. This happens, when exam actually starts.
	 * 
	 * @param exam
	 * @return
	 */
	public void startingExam(Exam exam) {
		String startExamQuery = "UPDATE exam SET status = \"live\" WHERE ExamID =" + exam.getExamID();
		DBConnector connector = new DBConnector();
		connector.updateDatabase(startExamQuery);
		connector.dispose();
	}

	/**
	 * Gets exam and changes its status to finished.
	 * 
	 * @param exam
	 */
	public void finishingExam(Exam exam) {
		String startExamQuery = "UPDATE exam SET status = \"finished\" WHERE ExamID =" + exam.getExamID();
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
		DBConnector connector = new DBConnector();
		updateStudentExamInformation(student, exam, connector);
		if (student.getExamStartTime() == null) {
			result = true;
		}
		connector.dispose();
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
	public static void updateStudentExamInformation(Student student, Exam exam, DBConnector connector) {
		ExamInformation examInfo = null;
		String examInformationQuery = "SELECT up.*, p.IP, p.Number, p.IsWorking FROM userplace as up JOIN userexam as ue on ue.UserExamID = up.UserExamID"
				+ " JOIN user as u on u.UserID = ue.UserID JOIN exam as e on e.ExamID = ue.ExamID JOIN place as p on p.PlaceID = up.PlaceID WHERE u.UserID = "
				+ student.getUserID() + " AND e.ExamID = " + exam.getExamID() + ";";
		SqlQueryResult queryResult = connector.getQueryResult(examInformationQuery);
		if (queryResult.isSuccess()) {
			examInfo = new ExamInformation(queryResult.getResultSet());
			student.setExamInformation(examInfo);
		}
	}

	/**
	 * Simply gets the exam by its id. if this exam doesnot exists returns
	 * WRONG_EXAM;
	 */
	public Exam getExamByExamId(int examID) {
		Exam result = WRONG_EXAM;
		if (examID > 0) {
			String getExamQuery = "select * from exam where ExamID =" + examID + ";";
			DBConnector connector = new DBConnector();
			SqlQueryResult queryResult = connector.getQueryResult(getExamQuery);
			if (queryResult.isSuccess()) {
				result = new Exam(queryResult.getResultSet());
				if (result.isWrongExam()) // if no Exam found
					result = WRONG_EXAM;
			}
			connector.dispose();
		}
		return result;
	}

	/**
	 * Modify exam : updates basic info of exam for lecturers
	 * 
	 * returns Exam which was added for some tests
	 */
	public void modifyExamBasicForLecturer(int examID, String examName, String openBook, int examDuration,
			String examType, int numVariants) {
		String updateQuery = "update exam set exam.Name='" + examName + "', exam.Type='" + examType + "', "
				+ "exam.Duration=" + examDuration + ", exam.ResourceType='" + openBook + "', exam.NumVariants="
				+ numVariants + " where ExamID=" + examID + ";";

		DBConnector connector = new DBConnector();
		connector.updateDatabase(updateQuery);
		connector.dispose();
	}

	/**
	 * Modify exam : updates basic info of exam for board
	 * 
	 */
	public void modifyExamBasicForBoard(int examID, Timestamp startTime) {
		String updateQuery = "update exam set exam.StartTime='" + startTime.toString() + "' where exam.ExamID='"
				+ examID + "'";
		DBConnector connector = new DBConnector();
		connector.updateDatabase(updateQuery);
		connector.dispose();
	}

	/**
	 * updates status of given exam @examID from statusFrom to statusTo if
	 * before update old status does not match with new that means someone
	 * changed before us and we couldnot update it soon enought so status wont
	 * be changed and user will have to change again
	 */
	public void updateExamStatus(int examID, String statusTo, String statusFrom) {
		String updateQuery = "update exam set exam.status='" + statusTo + "' where (ExamID=" + examID
				+ ") and (exam.status='" + statusFrom + "')  ;";

		DBConnector connector = new DBConnector();
		connector.updateDatabase(updateQuery);
		connector.dispose();
	}

	/**
	 * Creates new exam : insert into exam new value with setted parameter new
	 * 
	 * returns ExamID which was added
	 */
	public int createNewExam(int lecId, String examName, String openBook, int examDuration, int numVariants,
			String examType, int CreatedBy) {
		int examId = 0;
		String insertNewExamQuery = "INSERT INTO exam (exam.status, exam.Name, exam.Type, exam.Duration, "
				+ "exam.ResourceType, exam.NumVariants, exam.CreatedBy) VALUES ('new', '" + examName + "', '" + examType
				+ "', " + examDuration + ", '" + openBook + "', " + numVariants + ", ' " + CreatedBy + "');";
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

	/** deletes all subLecturers for exam */
	public void clearUserExamForExam(int examID) {
		String updateQuery = "DELETE ue	FROM examassistant.userexam as ue left join examassistant.user as  u on ue.UserID = u.UserID"
				+ " Where (ue.ExamID = " + examID + ") AND (u.Role = 'lecturer');";
		DBConnector connector = new DBConnector();
		connector.updateDatabase(updateQuery);
		connector.dispose();
	}

	/** deletes all materials for examID where material Type = @materialType */
	public void clearUserMaterialsForExam(int examID, String materialType) {
		String updateQuery = "delete from examassistant.exammaterial where (ExamID = " + examID
				+ ") and (MaterialType='" + materialType + "')";
		DBConnector connector = new DBConnector();
		connector.updateDatabase(updateQuery);
		connector.dispose();
	}

	/** deletes all materials for examID where material Type = @materialType */
	public void addUserMaterialsForExam(int examID, String materialName, String materialType, int materialVar,
			String location) {
		String updateQuery = "insert into examassistant.exammaterial (ExamID, Material, MaterialType, Variant, Location)"
				+ "values(" + examID + ", '" + materialName + "', '" + materialType + "', " + materialVar + ", '"
				+ location + "');";
		DBConnector connector = new DBConnector();
		connector.updateDatabase(updateQuery);
		connector.dispose();
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
	 * Adds new row in the base in the userexam table, which connects each user
	 * to the exam.
	 * 
	 * @param userId
	 * @param examId
	 */
	public void addExamForStudent(int userId, int examId) {
		addRowInUserExam(userId, examId);
	}

	/**
	 * Adds new row in the base in the userexam table, which connects each user
	 * to the exam.
	 * 
	 * @param userId
	 * @param examId
	 */
	public void addSubLecturerToExam(int userId, int examId) {
		addRowInUserExam(userId, examId);
	}

	/**
	 * Adds new row in the base in the userexam table, which connects each user
	 * to the exam.
	 * 
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
	 * deletes exam by id from exam table also updates userexam table by
	 * deleting all exam user pairs
	 * 
	 * @param examId
	 */
	public void deleteExam(int examId) {
		deleteUserPlaces(examId);
		deleteAllExamsInUserExam(examId);
		DBConnector connector = new DBConnector();
		String removeQuery = "delete from exam where ExamID =" + examId + ";";
		connector.updateDatabase(removeQuery);
		connector.dispose();
	}

	/* updates userexam table by deleting all exam user pairs */
	private void deleteAllExamsInUserExam(int examId) {
		DBConnector connector = new DBConnector();
		String removeQuery = "delete from userexam where ExamID =" + examId + ";";
		connector.updateDatabase(removeQuery);
		connector.dispose();
	}

	/*
	 * urodesac exam ishleba mashi unda waishalos UserPlaces cxrilidanac
	 * shesabamisi monacemebi
	 */
	private void deleteUserPlaces(int examId) {
		DBConnector connector = new DBConnector();
		String sqlQuery = "select UserExamID from userexam where ExamID=" + examId + ";";
		SqlQueryResult queryResult = connector.getQueryResult(sqlQuery);
		ArrayList<Integer> userExamIDs = new ArrayList<Integer>();
		if (queryResult.isSuccess()) {
			ResultSet res = queryResult.getResultSet();
			try {
				while (res.next()) {
					userExamIDs.add(res.getInt("UserExamID"));
				}
			} catch (SQLException e) {
				connector.dispose();
				e.printStackTrace();
			}

		}
		connector.dispose();
		removeFromUserPlaces(userExamIDs);
	}

	private void removeFromUserPlaces(ArrayList<Integer> userExamIDs) {
		DBConnector connector = new DBConnector();
		for (int i = 0; i < userExamIDs.size(); i++) {
			int userExamID = userExamIDs.get(i);
			String updateQuery = "delete from userplace where UserExamID=" + userExamID + ";";
			connector.updateDatabase(updateQuery);
		}
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

	/**
	 * checks if user id can access exam @examid if exam doesnot exists with
	 * that id false is returned
	 */
	public boolean CanUserAccessExam(EAUser user, int examId) {
		boolean result = false;
		String getExamQuery = getSqlQueryForUserAccessExam(user.getUserID(), examId);
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(getExamQuery);
		if (queryResult.isSuccess()) {
			result = !queryResult.isResultEmpty();
		}
		connector.dispose();
		// return result;
		return result;
	}

	/* returns sql Query for CanUserAcessExam */
	private String getSqlQueryForUserAccessExam(int userId, int examId) {
		String sqlQuery = "select CreatedBy from examassistant.exam " + "where (ExamID = " + examId
				+ ") and (CreatedBy = " + userId + ") " + "union all select UserID from examassistant.userexam "
				+ "where 	(ExamID = " + examId + ") and (UserID = " + userId + ")";
		return sqlQuery;
	}

	/* for given Exam @exam returns list of its subLecturers */
	public List<Lecturer> downloadSubLecturers(Exam exam) {
		List<Lecturer> lecturers = new ArrayList<Lecturer>();
		String getExamSubLecQuery = getSqlQueryForExamSubLecturers(exam.getExamID());
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(getExamSubLecQuery);
		if (queryResult.isSuccess()) {
			ResultSet rs = queryResult.getResultSet();
			while (true) {
				Lecturer lec = new Lecturer(rs);
				if (lec.getUserID() == EAUser.NO_USER_ID)
					break;// means end of resultset found
				lecturers.add(lec);
			}
		}
		connector.dispose();
		return lecturers;
	}

	/* returns sql Query for CanUserAcessExam */
	private String getSqlQueryForExamSubLecturers(int examId) {
		String sqlQuery = "select u.* from examassistant.user as u join (select UserID FROM examassistant.userexam where ExamID = "
				+ examId + ") as e on e.UserID = u.UserID and u.Role='lecturer'";

		return sqlQuery;
	}

	/**
	 * Get exam materials for student and the exam which he is writing right
	 * now. If variant is -1 it means that material is general for all students
	 * and it is book, pdf, or other type. If variant is more than 0 it means
	 * that it is the variant which has to be written by the student
	 * 
	 * @param student
	 * @param exam
	 * @return list of materials which are useful for student. Those are books
	 *         and specific exam variant which is created by lecturer
	 */
	public ArrayList<ExamMaterial> getExamMaterialsForStudent(Student student, Exam exam) {
		ArrayList<ExamMaterial> result = null;
		String sqlQuery = "select * from examassistant.exammaterial where ExamID = " + exam.getExamID()
				+ " AND (Variant = " + student.getExamInformation().getVariant() + " OR Variant = -1)";
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(sqlQuery);
		ResultSet rs = queryResult.getResultSet();
		try {
			if (rs.next()) {
				rs.beforeFirst();
				result = new ArrayList<ExamMaterial>();
				while (!rs.isLast()) {
					ExamMaterial tmpExamMaterial = new ExamMaterial(queryResult.getResultSet());
					result.add(tmpExamMaterial);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connector.dispose();
		return result;
	}

	/**
	 * gets list of materilas for exam @param exam with materialsTypeId
	 * id @param id list of. returns empty list in case of error
	 */
	public ArrayList<ExamMaterial> downloadMaterialsList(Exam exam, String materialType) {
		ArrayList<ExamMaterial> result = new ArrayList<ExamMaterial>();
		String sqlQuery = "select * from examassistant.exammaterial where ExamID = " + exam.getExamID()
				+ " AND (MaterialType = '" + materialType + "');";
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(sqlQuery);
		if (queryResult.isSuccess()) {
			ResultSet rs = queryResult.getResultSet();
			try {
				if (rs.next()) {
					rs.beforeFirst();
					while (!rs.isLast()) {
						ExamMaterial tmpExamMaterial = new ExamMaterial(queryResult.getResultSet());
						result.add(tmpExamMaterial);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		connector.dispose();
		return result;
	}

	/** downlaods materials for given exam @param exam */
	public List<ExamMaterial> downloadMaterialsList(Exam examToEdit) {
		return downloadMaterialsList(examToEdit, MATERIAL_BOOKS);
	}

	/** downlaods variants for given exam @param exam */
	public List<ExamMaterial> downloadVariantsList(Exam examToEdit) {
		return downloadMaterialsList(examToEdit, MATERIAL_VARIANTS);
	}

	/** downlaods file name for list of students taking exam */
	public List<ExamMaterial> downloadStudentsList(Exam examToEdit) {
		return downloadMaterialsList(examToEdit, MATERIAL_STUDENTS_LIST);
	}

	/**
	 * @param exam
	 * @return list of students who write this exam
	 */
	public ArrayList<Student> getAllStudentWhoWritesThisExam(Exam exam) {
		ArrayList<Student> students = new ArrayList<Student>();
		String sqlQuery = "select user.UserID as UserID from userexam join user on userexam.UserID=user.UserID and ExamID="
				+ exam.getExamID() + " where Role='student';";
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(sqlQuery);
		if (queryResult.isSuccess()) {
			ResultSet rs = queryResult.getResultSet();
			AccountManager acm = new AccountManager();
			try {
				while (rs.next()) {
					int userId = rs.getInt("UserID");
					Student student = (Student) acm.getUserById(userId).getOpResult();
					students.add(student);
				}
			} catch (SQLException e) {
				connector.dispose();
				e.printStackTrace();
			}

		}
		connector.dispose();
		return students;
	}

	/**
	 * 
	 * @return array list of exams for the present day.
	 */
	public ArrayList<Exam> getExamsForEachDay() {
		ArrayList<Exam> res = new ArrayList<Exam>();
		String st = "SELECT * FROM examassistant.exam "
				+ " where  StartTime between curdate() and date_add(curdate(), interval 1 day);";
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

}
