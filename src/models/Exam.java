package models;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import data_managers.ExamManager;
import helper.LogManager;

public class Exam {
	public static final String OPEN_BOOK = "Open book";
	public static final String CLOSED_BOOK = "Closed book";
	public static final String EXAM_STATUS_DONE = "Done";
	public static final String EXAM_STATUS_PROCESSING = "Processing";
	public static final String EXAM_STATUS_NEW = "new";
	public static final String EXAM_TYPE_FINAL = "Final";
	public static final String EXAM_TYPE_MIDTERM = "Midterm";
	public static final String EXAM_TYPE_QUIZZ = "Quizz";
	public static final String EXAM_NAME_UNDEFINED = "Undefined";
	public static final int DEFAULT_EXAM_DUARTION = 100;
	public static final String EXAM_STATUS_WAITING = "Quizz";

	private int examID;
	private String name = "";
	private String type = "";
	private Date startTime;
	private int duration = 0; // in minutes
	private String resourceType = "";
	private int numVariants = 0;
	private String status = "";
	private int creatorId;
	private EAUser creator;
	List<Lecturer> subLectuers = new ArrayList<Lecturer>();

	public Exam(ResultSet rs) {
		if (rs != null) {
			try {
				if (rs.next()) {
					this.setExamID(rs.getInt("ExamID"));
					this.setName(rs.getString("Name"));
					this.setType(rs.getString("Type"));
					this.setStartTime(rs.getDate("StartTime"));
					this.setDuration(rs.getInt("Duration"));
					this.setResourceType(rs.getString("ResourceType"));
					this.setNumVariants(rs.getInt("NumVariants"));
					this.setStatus(rs.getString("Status"));
					this.setCreatorId(rs.getInt("CreatedBy"));
				} else {
					// case empty resultset was passed
					this.examID = ExamManager.NO_EXAM_ID;
				}
			} catch (Exception e) {
				LogManager.logErrorException(3000, "Error parsing ResultSet ", e);
			}
		}
	}

	public Exam(int examID, String name, String type, Date startTime, int duration, String resourceType,
			int numVariants, String status) {
		this.examID = examID;
		this.name = name;
		this.type = type;
		this.startTime = startTime;
		this.duration = duration;
		this.resourceType = resourceType;
		this.numVariants = numVariants;
		this.status = status;
	}

	public Exam(int examId) {
		this.examID = examId;
	}

	public Exam() {

	}

	/** returns name of creator of exam as whole for displaying */
	public String getCreatorName() {
		return this.getCreator().getFirstName() + " " + this.getCreator().getLastName();
	}

	/** sets sub lecturers from db for current exam */
	public void setSubLecturers(List<Lecturer> subLectuers) {
		this.subLectuers = subLectuers;
	}

	/** returns only date when exam should be started format : YYYY-MM-DD */
	public String getExamStartDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
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
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		return "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
	}

	/** gets sub lecturers from db for current exam 
	 *  subLecturers in db contains creator itself but we dont
	 *  need to display it. as its model for modifyexam we remove itself from list
	 *  and return others */
	public List<Lecturer> getSubLecturers() {
		//copy list/ remove creator 
		List<Lecturer> lecList = new ArrayList<Lecturer>();
		lecList.addAll(subLectuers);
		lecList.remove((Lecturer)getCreator());
		
		return lecList;
	}

	public void setCreator(EAUser creator) {
		this.creator = creator;
	}

	public EAUser getCreator() {
		return creator;
	}

	/** checks if given exam is empty end not valid for precessiong */
	public boolean isEmptyExam() {
		return getExamID() == ExamManager.NO_EXAM_ID;
	}

	/** Returns the id of the exam. */
	public int getExamID() {
		return examID;
	}

	/** Sets the id of the exam. */
	public void setExamID(int examID) {
		this.examID = examID;
	}

	/** Returns the name of the exam. */
	public String getName() {
		return name;
	}

	/** Sets the name of the exam. */
	public void setName(String name) {
		this.name = name;
	}

	/** Gets the type of the exam. */
	public String getType() {
		return type;
	}

	/** Sets the type of the exam. */
	public void setType(String type) {
		this.type = type;
	}

	/** Gets the start time of the exam. */
	public Date getStartTime() {
		return startTime;
	}

	/** Sets the start time of the exam. */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/** Gets the duration of the exam. */
	public int getDuration() {
		return duration;
	}

	/** Sets the duration of the exam. */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/** Gets the resource types of the exam. */
	public String getResourceType() {
		return resourceType;
	}

	/** Sets the resource types of the exam. */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/** Gets the status of the exam. */
	public String getStatus() {
		return status;
	}

	/** Sets the status of the exam. */
	public void setStatus(String status) {
		this.status = status;
	}

	/** Gets the number of variants of the exam. */
	public int getNumVariants() {
		return numVariants;
	}

	/** Sets the number of variants of the exam. */
	public void setNumVariants(int numVariants) {
		this.numVariants = numVariants;
	}

	/** Sets creator id for exam */
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	/** Gets creator id for exam */
	public int getCreatorId() {
		return creatorId;
	}

	/** To string the exam object. */
	public String toString() {
		String res = "[ ExamId:" + examID + "Name:" + name + " " + "Type:" + type + " " + "StartTime:" + startTime + " "
				+ "Duration:" + duration + " " + "Resource:" + resourceType + " " + "NumVariants:" + numVariants + " "
				+ "status: " + status + " ]";
		return res;
	}

	public boolean equals(Exam ex) {
		if (ex.getName().equals(this.getName()) && ex.getExamID() == this.getExamID()) {
			return true;
		}
		return false;
	}
}
