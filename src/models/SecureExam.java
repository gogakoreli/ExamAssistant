package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import helper.LogManager;
import helper.OpResult;

/**
 * this is class which contains exam as data model and secures it after setting
 * its modifier up it allows any changes in exam only if user has permission. it
 * also returns values for modifyExam.jsp to display.
 * 
 * 
 * here is main logic about exam. Its statuses and fields
 */
public class SecureExam {

	private static final String DEFAULT_CREATOR_NAME = "";
	private static final String DEFAULT_EXAM_START_TIME = "UNDEFINED";
	private static final String DEFAULT_EXAM_NAME = "";
	private static final int DEFAULT_EXAM_DURATION = 0;
	private static final String DEFAULT_EXAM_START_DATE = "UNDEFINED";
	private static final int MIN_EXAM_NAME = 0;
	private static final int MIN_EXAM_DURATION = 0;

	/* model containing exam inforamtion */
	private Exam examToSecure;
	private EAUser editor;

	public SecureExam(Exam examToSecure) {
		this.examToSecure = examToSecure;
	}

	/* checks if exam is new exam */
	public boolean isExamNew() {
		return examToSecure.isEmptyExam();
	}

	/** returns name of creator of exam as whole for displaying */
	public String getCreatorName() {
		if (isExamNew())
			return DEFAULT_CREATOR_NAME;
		return examToSecure.getCreator().getFirstName() + " " + examToSecure.getCreator().getLastName();
	}

	/** returns only date when exam should be started format : YYYY-MM-DD */
	public String getExamStartDate() {
		if (isExamNew())
			return DEFAULT_EXAM_START_DATE;
		Calendar calendar = Calendar.getInstance();
		return "" + calendar.get(Calendar.YEAR) + "-" + getDateStringFromInt(calendar.get(Calendar.MONTH)) + "-"
				+ getDateStringFromInt(calendar.get(Calendar.DAY_OF_MONTH));
	}

	/*
	 * gets string which is 2 in length for month and day if they are between
	 * 1-9 we should write 08 not 8
	 */
	private String getDateStringFromInt(int x) {
		String s = "" + x;
		if (x <= 9)
			s = "0" + s;
		return s;
	}

	/** returns exam start time format : HH:MM */
	public String getExamStartTime() {
		if (isExamNew())
			return DEFAULT_EXAM_START_TIME;

		Calendar calendar = Calendar.getInstance();
		if (examToSecure.getStartDateTime() != null)
			calendar.setTime(examToSecure.getStartDateTime());
		return "" + getDateStringFromInt(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
				+ getDateStringFromInt(calendar.get(Calendar.MINUTE));
	}

	/** returs exam id of secure exam */
	public int getExamID() {
		return examToSecure.getExamID();
	}

	/** returs exam id of secure exam */
	public String getName() {
		if (isExamNew())
			return DEFAULT_EXAM_NAME;
		return examToSecure.getName();
	}

	/** returs exam id of secure exam */
	public int getDuration() {
		if (isExamNew())
			return DEFAULT_EXAM_DURATION;
		return examToSecure.getDuration();
	}

	/** returns the type of the exam. */
	public String getType() {
		return examToSecure.getType();
	}

	public Timestamp getStartDateTime() {
		return examToSecure.getStartDateTime();
	}

	public String getResourceType() {
		return examToSecure.getResourceType();
	}

	/**
	 * returns list of sublecturers
	 */
	public List<Lecturer> getSubLecturers() {
		if (isExamNew())
			return new ArrayList<Lecturer>();
		return examToSecure.getSubLecturers();
	}

	/********************************/
	/** permissions on setters ******/
	/********************************/

	/**
	 * sets editor user for given exam
	 */
	public void setExamEditor(EAUser editor) {
		this.editor = editor;
		// throw new SecurityException()
	}

	public String getExamStatus() {
		if (isExamNew())
			return Exam.ExamStatus.NEW;
		return examToSecure.getStatus();
	}

	public EAUser getExamEditor() {
		return editor;
		// throw new SecurityException()
	}

	public Exam getEditedExam() {
		return examToSecure;
	}

	/* checks if editor of exam is lecturer */
	private boolean isEditorLecturer() {
		return editor instanceof Lecturer;
	}

	/* checks if editor of exam is board */
	private boolean isEditorBoard() {
		return editor instanceof ExamBoard;
	}

	/* chekcs if given editor is admin or not */
	private boolean isEditorAdmin() {
		return editor instanceof Admin;
	}

	/* checks if status of exam is new */
	private boolean checkStatusNew() {
		return examToSecure.getStatus().equals(Exam.ExamStatus.NEW);
	}

	/* sets name of exam if editor has permission on it */
	public void setName(String examName) {
		if (!hasPermissionChangeName())
			return;
		examToSecure.setName(examName);
	}

	/* checks if editor has permission to change name of exam */
	private boolean hasPermissionChangeName() {
		if (isEditorAdmin())
			return true;
		boolean canChangeUser = isEditorLecturer();
		boolean isStatusNew = checkStatusNew();
		return canChangeUser && isStatusNew;
	}

	/* Sets the duration. */
	public void setDuration(int newDuration) {
		if (!hasPermissionChangeDuration())
			return;
		examToSecure.setDuration(newDuration);
	}

	/*
	 * checks if editor has permission to change Duration of exam it can change
	 * duration if editor is lecturer and status is still new
	 */
	private boolean hasPermissionChangeDuration() {
		if (isEditorAdmin())
			return true;
		boolean canChangeUser = isEditorLecturer();

		boolean isStatusNew = checkStatusNew();
		return canChangeUser && isStatusNew;
	}

	/* sets the type. */
	public void setType(String type) {
		if (!hasPermissionChangeType())
			return;
		examToSecure.setType(type);
	}

	/*
	 * checks if editor has permission to change Type of exam it can change Type
	 * if editor is lecturer
	 */
	private boolean hasPermissionChangeType() {
		if (isEditorAdmin())
			return true;
		boolean canChangeUser = isEditorLecturer();
		return canChangeUser;
	}

	public void setResourceType(String newNoteType) {
		if (!hasPermissionChangeNoteType())
			return;
		examToSecure.setResourceType(newNoteType);
	}

	/*
	 * checks if editor has permission to change Note Type of exam it can change
	 * Type if editor is lecturer
	 */
	private boolean hasPermissionChangeNoteType() {
		if (isEditorAdmin())
			return true;
		boolean canChangeUser = isEditorLecturer();
		return canChangeUser;
	}

	public void setStartTime(Timestamp newStartTime) {
		if (!hasPermissionChangeStartTime())
			return;
		examToSecure.setStartTime(newStartTime);
	}

	/*
	 * checks if editor has permission to change start time he/she has
	 * permission if its examboard and exam status is pending
	 */
	private boolean hasPermissionChangeStartTime() {
		if (isEditorAdmin())
			return true;
		boolean canChangeUser = isEditorBoard();
		boolean isStatusNew = examToSecure.getStatus().equals(Exam.ExamStatus.PENDING);
		return canChangeUser && isStatusNew;
	}

	/** sets subLecturers if you have permission on it */
	public void setSubLecturers(List<Lecturer> subLecturers) {
		if (!hasPermissionChangeSubLecturers())
			return;
		examToSecure.setSubLecturers(subLecturers);
	}

	/*
	 * checks if editor has permission to change sub Lecturers he/she can change
	 * it if its lecturer
	 */
	private boolean hasPermissionChangeSubLecturers() {
		if (isEditorAdmin())
			return true;
		boolean canChangeUser = isEditorLecturer();
		return canChangeUser;
	}

	// files attached to exam
	/*
	 * private List<String> variantUrls = new ArrayList<String>(); private
	 * List<String> materialsUrls = new ArrayList<String>(); private String
	 * studentsListUrl = "";
	 */

	/********************************/
	/** changing exam status *******/

	/********************************/

	/**
	 * checks if its possible to change status to next level and returns
	 * OpResult of boolean (true) if it's. returns unsucsessful OpResult in case
	 * of falture with message why Change of status was not avaliable
	 */
	public OpResult<Boolean> canChangeStatus() {
		OpResult<Boolean> result = null;// new OpResult<Boolean>();
		switch (examToSecure.getStatus()) {
		case Exam.ExamStatus.NEW:
			result = canChangeToPending();
			break;
		case Exam.ExamStatus.PENDING:
		case Exam.ExamStatus.LECTURER_READY:
		case Exam.ExamStatus.BOARD_READY:
			result = canChangeToReady();
		default:
			break;
		}
		return result;
	}

	/** gets what will became next status after submitting exam one */
	public String getNextStatus() {
		String result = "";
		switch (examToSecure.getStatus()) {
		case Exam.ExamStatus.NEW:
			result = Exam.ExamStatus.PENDING;
			break;
		case Exam.ExamStatus.PENDING:
			result = Exam.ExamStatus.LECTURER_READY;
			if (getExamEditor() instanceof ExamBoard)  
				result = Exam.ExamStatus.BOARD_READY;
			break;
		case Exam.ExamStatus.LECTURER_READY:
		case Exam.ExamStatus.BOARD_READY:
			result = Exam.ExamStatus.PUBLISHED;
			break;
		default:
			result = "pending";
			LogManager.logErrorException("Asked changing of status of examId= " + examToSecure.getExamID(),
					new Exception("unsupported next status"));
			break;
		}
		return result;
	}

	/*
	 * checks if status can be changed to pending and returns OpResult
	 * containing answer to it
	 */
	private OpResult<Boolean> canChangeToPending() {
		OpResult<Boolean> result = new OpResult<Boolean>();
		if (examToSecure.getName().length() <= MIN_EXAM_NAME) {
			// result.setResultObject(new Boolean(false));
			result.setError(3000, "Can't Change Status of Exam without correct name");
		} else if (examToSecure.getDuration() <= MIN_EXAM_DURATION) {
			// result.setResultObject(new Boolean(false));
			result.setError(3001, "Can't Change Status of Exam without correct duration");
		} else {
			result.setResultObject(new Boolean(true));
		}
		return result;
	}

	/*
	 * checks if current user can update status to next level by commiting his
	 * part as ready for publishing if user changes his/her status to ready all
	 * fields should be done and he/she can't make any further changes . in addition
	 * user can not change status anymore if he/she has already changed it to ready 
	 */
	private OpResult<Boolean> canChangeToReady() {
		OpResult<Boolean> result = new OpResult<Boolean>();
		if (isEditorBoard()) {
			// return null;
			// if (examToSecure.getStartDateTime().after(when) < MIN_EXAM_NAME)
			// {
		}
		result.setError(0, "Unimplimneted error");
		return result;
	}

}
