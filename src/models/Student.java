package models;

import java.sql.ResultSet;

public class Student extends EAUser {

	private ExamInformation examInformation;

	public Student(ResultSet rs) {
		super(rs);
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

}