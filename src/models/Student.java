package models;

import java.util.Date;
import java.sql.ResultSet;

public class Student extends EAUser {

	private ExamInformation examInformation;

	public Student(ResultSet rs) {
		super(rs);
	}

	public Student() {
		super();
	}

	public Student(int userID, EAUserRole role, String mail, String firstName, String lastName, String image,
			String googleID) {
		super(userID, role, mail, firstName, lastName, image, googleID);
	}

	@Override
	public void downloadAditionalInfo() {
	}

	/**
	 * @return the examInformation
	 */
	public ExamInformation getExamInformation() {
		return examInformation;
	}

	/**
	 * @param examInformation
	 *            the examInformation to set
	 */
	public void setExamInformation(ExamInformation examInformation) {
		this.examInformation = examInformation;
	}

	/**
	 * if student has already set exam information, then return his start time
	 * 
	 * @return
	 */
	public Date getExamStartTime() {
		Date result = null;
		if (examInformation != null) {
			result = examInformation.getStartTime();
		}
		return result;
	}

}