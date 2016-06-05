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
				while (rs.next()) {
					this.examID = rs.getInt("ExamID");
					this.name = rs.getString("Name");
					this.type = rs.getString("Type");
					this.startTime = rs.getDate("StartTime");
					this.duration = rs.getInt("Duration");
					this.resourceType = rs.getString("ResourceType");
					this.numVariants = rs.getInt("NumVariants");
					this.status = rs.getString("Status");
				}
			} catch (Exception e) {
				LogManager.logErrorException(3000, "Error parsing ResultSet ", e);
			}
		}
	}
	

}
