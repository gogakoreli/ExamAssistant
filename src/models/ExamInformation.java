package models;

import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.sql.ResultSet;

import helper.LogManager;
import helper.TimeSpan;

public class ExamInformation {

	private int userPlaceID;
	private int userExamID;
	private int placeID;
	private int variant;
	private Date startTime;
	private Date endTime;
	private String ip;
	private int placeNumber;
	private boolean isWorking;

	private ArrayList<File> uploadedFiles;

	/**
	 * Constructor using resultset
	 * 
	 * @param resultSet
	 */
	public ExamInformation(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				if (resultSet.next()) {
					this.setUserPlaceID(resultSet.getInt("UserPlaceID"));
					this.setUserExamID(resultSet.getInt("UserExamID"));
					this.setPlaceID(resultSet.getInt("PlaceID"));
					this.setVariant(resultSet.getInt("Variant"));
					this.setStartTime(resultSet.getDate("StartTime"));
					this.setEndTime(resultSet.getDate("EndTime"));
					this.setIp(resultSet.getString("IP"));
					this.setPlaceNumber(resultSet.getInt("Number"));
					this.setWorking(resultSet.getBoolean("IsWorking"));
					uploadedFiles = new ArrayList<File>();
				}
			} catch (Exception e) {
				LogManager.logErrorException(3000, "Error parsing ResultSet ", e);
			}
		}

	}

	/**
	 * Constructor.
	 */
	public ExamInformation() {
		uploadedFiles = new ArrayList<File>();

	}

	/*****************************/
	/********** methods **********/
	/*****************************/

	/**
	 * get time left from the currrent time to the finishing of the exam
	 * 
	 * @param exam
	 * @return
	 */
	public Date getTimeLeft(Exam exam) {
		Date result = null;
		Date finishTime = new Date(exam.getStartTime().getTime() + (long) exam.getDuration() * 60 * 1000);
		Date now = new Date();
		result = new Date(finishTime.getTime() - now.getTime());
		return result;

	}

	/**
	 * store uploaded files in the arraylist , because I need reference to
	 * delete, update in the future
	 * 
	 * @param file
	 */
	public void addUploadedFile(File file) {
		uploadedFiles.add(file);

	}

	/*****************************/
	/***** getters/setters ******/
	/*****************************/

	/**
	 * @return the userPlaceID
	 */
	public int getUserPlaceID() {
		return userPlaceID;
	}

	/**
	 * @param userPlaceID
	 *            the userPlaceID to set
	 */
	public void setUserPlaceID(int userPlaceID) {
		this.userPlaceID = userPlaceID;
	}

	/**
	 * @return the userExamID
	 */
	public int getUserExamID() {
		return userExamID;
	}

	/**
	 * @param userExamID
	 *            the userExamID to set
	 */
	public void setUserExamID(int userExamID) {
		this.userExamID = userExamID;
	}

	/**
	 * @return the placeID
	 */
	public int getPlaceID() {
		return placeID;
	}

	/**
	 * @param placeID
	 *            the placeID to set
	 */
	public void setPlaceID(int placeID) {
		this.placeID = placeID;
	}

	/**
	 * @return the variant
	 */
	public int getVariant() {
		return variant;
	}

	/**
	 * @param variant
	 *            the variant to set
	 */
	public void setVariant(int variant) {
		this.variant = variant;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the placeNumber
	 */
	public int getPlaceNumber() {
		return placeNumber;
	}

	/**
	 * @param placeNumber
	 *            the placeNumber to set
	 */
	public void setPlaceNumber(int placeNumber) {
		this.placeNumber = placeNumber;
	}

	/**
	 * @return the isWorking
	 */
	public boolean isWorking() {
		return isWorking;
	}

	/**
	 * @param isWorking
	 *            the isWorking to set
	 */
	public void setWorking(boolean isWorking) {
		this.isWorking = isWorking;
	}

	/**
	 * @return the uploadedFiles
	 */
	public ArrayList<File> getUploadedFiles() {
		return uploadedFiles;

	}

	/**
	 * @param uploadedFiles
	 *            the uploadedFiles to set
	 */
	public void setUploadedFiles(ArrayList<File> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;

	}

}
