package models;

import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * this is class which contains exam as data model and secures it after setting
 * its modifier up it allows any changes in exam only if user has permission. it
 * also returns values for modifyExam.jsp to display.
 */
public class SecureExam {

	private static final String DEFAULT_CREATOR_NAME = "";
	private static final String DEFAULT_EXAM_START_TIME = "UNDEFINED";
	private static final String DEFAULT_EXAM_NAME = "";
	private static final int DEFAULT_EXAM_DURATION = 0;
	private static final String DEFAULT_EXAM_START_DATE = "UNDEFINED";
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

	/** returns the note type of the exam. */
	public String getNoteType() {
		return examToSecure.getNoteType();
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
	
	/** sets editor user for given exam */
	public void setExamEditor(EAUser editor) {
		this.editor = editor;
	}

	/* sets name of exam if editor has permission on it */
	public void setName(String examName) {
		if (!hasPermissionChangeName())
			return;
		examToSecure.setName(examName);
	}

	/* checks if editor has permission to change name of exam */
	private boolean hasPermissionChangeName() {
		boolean canChangeUser = editor instanceof Lecturer;
		boolean isStatusNew = examToSecure.getStatus() == Exam.ExamStatus.NEW;
		return canChangeUser && isStatusNew;
	}

	
	/* Sets the duration.*/
	public void setDuration(int newDuration) {
		if (!hasPermissionChangeDuration()) 
			return;		
		examToSecure.setDuration(newDuration);
	}
	
	/* checks if editor has permission to change Duration of exam 
	 * it can change duration if editor is lecturer and status is still new */
	private boolean hasPermissionChangeDuration() {
		boolean canChangeUser = editor instanceof Lecturer;
		boolean isStatusNew = examToSecure.getStatus().equals(Exam.ExamStatus.NEW);
		return canChangeUser && isStatusNew;
	}

	/* sets the type.*/
	public void setType(String type) {
		if (!hasPermissionChangeType()) 
			return;		
		examToSecure.setType(type);
	}

	/* checks if editor has permission to change Type of exam 
	 * it can change Type if editor is lecturer */
	private boolean hasPermissionChangeType() {
		boolean canChangeUser = editor instanceof Lecturer;
		return canChangeUser;
	}
	
	/* sets the note type. */
	public void setNoteType(String newNoteType) {
		if (!hasPermissionChangeNoteType())
			return;		
		examToSecure.setNoteType(newNoteType);		
	}
	
	/* checks if editor has permission to change Note Type of exam 
	 * it can change Type if editor is lecturer */
	private boolean hasPermissionChangeNoteType() {
		boolean canChangeUser = editor instanceof Lecturer;
		return canChangeUser;
	}

	public void setStartTime(Timestamp newStartTime) {
		if (!hasPermissionChangeStartTime())
			return;		
		examToSecure.setStartTime(newStartTime);		
	}

	/* checks if editor has permission to change start time 
	 * he/she has permission if its examboard and exam status is pending */
	private boolean hasPermissionChangeStartTime() {
		boolean canChangeUser = editor instanceof ExamBoard;
		boolean isStatusNew = examToSecure.getStatus().equals(Exam.ExamStatus.PENDING);
		return canChangeUser && isStatusNew;
	}
	
	/** sets subLecturers if you have permission on it */
	public void setSubLecturers(List<Lecturer> subLecturers) {
		if (!hasPermissionChangeSubLecturers())
			return;		
		examToSecure.setSubLecturers(subLecturers);
	}

	/* checks if editor has permission to change sub Lecturers 
	 * he/she can change it if its lecturer */
	private boolean hasPermissionChangeSubLecturers() {
		boolean canChangeUser = editor instanceof Lecturer;
		return canChangeUser;
	}
	
	
	//files attached to exam 
		/*private List<String> variantUrls = new ArrayList<String>();
		private List<String> materialsUrls =  new ArrayList<String>();
		private String studentsListUrl = ""; */

	
	
}
