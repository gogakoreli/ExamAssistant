package models;

import java.sql.ResultSet;

public class Student extends EAUser {
	private Exam exam;

	public Student(ResultSet rs) {
		super(rs);
	}

	@Override
	public void downloadAditionalInfo() {
	}

	public Exam getExam() {
		return this.exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

}