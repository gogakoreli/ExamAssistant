package models;

import java.sql.Date;
import java.sql.ResultSet;

import helper.LogManager;

public class Exam {
	private int examID;
	private String name;
	private String type;
	private Date startTime;
	private int duration; // in seconds or in minutes
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

	public int getExamID() {
		return examID;
	}

	public void setExamID(int examID) {
		this.examID = examID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getNumVariants() {
		return numVariants;
	}

	public void setNumVariants(int numVariants) {
		this.numVariants = numVariants;
	}

}
