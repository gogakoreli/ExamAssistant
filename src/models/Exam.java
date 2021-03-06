package models;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import data_managers.ExamManager;
import helper.LogManager;

public class Exam {

	public final class NoteType {
		public static final String OPEN_BOOK = "Open Book";
		public static final String CLOSED_BOOK = "Closed Book";
		public static final String OPEN_NOTE = "Open Note";
	}

	/*
	 * enum for ExamStatus this names match with names we save exam status in db
	 */
	public final class ExamStatus {
		public static final String NEW = "new";
		public static final String PENDING = "pending";
		public static final String LECTURER_READY = "lecturer_ready";
		public static final String BOARD_READY = "board_ready";
		public static final String PUBLISHED = "published";
		public static final String LIVE = "live";
		public static final String FINISHED = "finished";
	}

	public final class ExamType {
		public static final String FINAL = "Final";
		public static final String MIDTERM = "Midterm";
		public static final String QUIZZ = "Quizz";
	}

	public static final String EXAM_NAME_UNDEFINED = "Undefined";
	public static final int DEFAULT_EXAM_DUARTION = 100;
	public static final String EXAM_STATUS_WAITING = "Quizz";

	private int examID;
	private String name = "";
	private String type = "";
	private Timestamp startTime;
	private int duration = 0; // in minutes
	private String resourceType = "";
	private int numVariants = 0;
	private String status = "";
	private int creatorId = -1;
	private EAUser creator;// user who created exam
	List<Lecturer> subLectuers = new ArrayList<Lecturer>();

	// files attached to exam
	private List<ExamMaterial> variantUrls = new ArrayList<ExamMaterial>();
	private List<ExamMaterial> materialsUrls = new ArrayList<ExamMaterial>();
	private List<ExamMaterial> studentsListUrls = new ArrayList<ExamMaterial>();

	public Exam(ResultSet rs) {
		if (rs != null) {
			try {
				if (rs.next()) {
					this.setExamID(rs.getInt("ExamID"));
					this.setName(rs.getString("Name"));
					this.setType(rs.getString("Type"));
					this.setStartTime(rs.getTimestamp("StartTime"));
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

	public Exam(int examID, String name, String type, Timestamp startTime, int duration, String resourceType,
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
		if (examID == ExamManager.NEW_EXAM_ID)
			this.status = Exam.ExamStatus.NEW;
	}

	public Exam() {

	}

	/** sets Note Type for given exam 

	/** sets Note Type for given exam 
	public void setNoteType(String type) {
		this.noteType = type;
	} */

	/** gets Note Type for given exam 
	public String getNoteType() {
		return this.noteType;
	}*/

	/** sets sub lecturers from db for current exam */
	public void setSubLecturers(List<Lecturer> subLectuers) {
		this.subLectuers = subLectuers;
	}

	/**
	 * gets sub lecturers from db for current exam subLecturers in db contains
	 * creator itself but we dont need to display it. as its model for
	 * modifyexam we remove itself from list and return others
	 */
	public List<Lecturer> getSubLecturers() {
		// copy list/ remove creator
		List<Lecturer> lecList = new ArrayList<Lecturer>();
		lecList.addAll(subLectuers);
		lecList.remove((Lecturer) getCreator());

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
		return getExamID() == ExamManager.NEW_EXAM_ID;
	}
	
	public boolean isWrongExam(){
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
	public Timestamp getStartTime() {
		return startTime;
	}

	/** Sets the start time of the exam. */
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndDateTime() {
		long t = startTime.getTime();
		long m = duration * 60 * 1000;
		Timestamp targetDeliveryStamp = new Timestamp(t + m);
		return targetDeliveryStamp;
	}

	/** Sets the start time of the exam. */
	public Timestamp getStartDateTime() {
		return this.startTime;
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
		return 2;
		//return numVariants;
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
	
	/** gets materials list for exam */
	public List<ExamMaterial> getMaterialsList(){
		return materialsUrls;
	}
	
	/** sets materials list for exam*/
	public void setMaterialsList (List<ExamMaterial> materials){
		this.materialsUrls = materials;
	}
	
	/** gets materials variants list for exam */
	public List<ExamMaterial> getMaterialVariantsList(){
		return variantUrls;
	}
	
	/** sets materials variants list for exam*/
	public void setMaterialVariantsList (List<ExamMaterial> materialsVar){
		this.variantUrls = materialsVar;
	}
	
	public List<ExamMaterial> getStudentsListForExam(){
		return studentsListUrls; 
	}
	
	public void setStudentsListForExam(List<ExamMaterial> listFile){
		studentsListUrls = listFile; 
	}

	/** To string the exam object. */
	public String toString() {
		String res = "[ ExamId:" + examID + "Name:" + name + " " + "Type:" + type + " " + "StartTime:" + startTime + " "
				+ "Duration:" + duration + " " + "Resource:" + resourceType + " " + "NumVariants:" + numVariants + " "
				+ "status: " + status + " ]";
		return res;
	}

}


