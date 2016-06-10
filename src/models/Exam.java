package models;

import java.sql.Date;
import java.sql.ResultSet;

import helper.LogManager;

public class Exam {
	public static final String OPEN_BOOK = "Open book";
	public static final String EXAM_STATUS_DONE = "Done";
	public static final String EXAM_STATUS_PROCESSING = "Processing";
	public static final String EXAM_STATUS_NEW = "new";
	public static final String EXAM_TYPE_FINAL = "Final";
	public static final String EXAM_TYPE_MIDTERM = "Midterm";
	public static final String EXAM_TYPE_QUIZZ = "Quizz";
	public static final String EXAM_NAME_UNDEFINED = "Undefined";
	public static final int DEFAULT_EXAM_DUARTION = 100;
	public static final String EXAM_STATUS_WAITING= "Quizz";
	
	private int examID;
	private String name;
	private String type;
	private Date startTime;
	private int duration; // in minutes
	private String resourceType;
	private int numVariants;
	private String status;

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
				}
			} catch (Exception e) {
				LogManager.logErrorException(3000, "Error parsing ResultSet ", e);
			}
		}
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
	
	/** To string the exam object.*/
	public String toString() {
		String res = "[ Name: " + name + " " + "Type: " + type + " " + "StartTime: " + startTime + " " + "Duration: "
				+ duration + " " + "Resource: " + resourceType + " " + "NumVariants: " + numVariants + " " + "status: "
				+ status + " ]";
		return res;
	}

}
