package models;

import java.sql.Date;
import java.sql.ResultSet;

import helper.LogManager;

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

	public ExamInformation(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				if (resultSet.next()) {
					this.setUserPlaceID(resultSet.getInt("UserPlaceID"));
					this.setUserExamID(resultSet.getInt("UserExamID"));
					this.setPlaceID(resultSet.getInt("PlaceID"));
					this.setVariant(resultSet.getInt("Variant"));
					this.setStartTime(resultSet.getDate("StartTime"));
					this.setEndTime(resultSet.getDate("EndDate"));
					this.setIp(resultSet.getString("IP"));
					this.setPlaceNumber(resultSet.getInt("Number"));
					this.setWorking(resultSet.getBoolean("IsWorking"));
				}
			} catch (Exception e) {
				LogManager.logErrorException(3000, "Error parsing ResultSet ", e);
			}
		}
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

}
