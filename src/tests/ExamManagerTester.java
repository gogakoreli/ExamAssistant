package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import helper.AccountManager;
import helper.ExamManager;
import helper.OpResult;
import models.EAUser;
import models.Exam;
import models.ExamInformation;
import models.Student;

public class ExamManagerTester {
	ExamManager exManager;
	EAUser lecturer;
	EAUser student;

	int examId;
	Exam exam;


	@Before
	public void setUp() throws Exception {
		exManager = new ExamManager();
		AccountManager acMan = new AccountManager();
		OpResult<EAUser> res1 = acMan.getEAUserForCreditials("a.meskhi@freeuni.edu.ge", "al", null);
		OpResult<EAUser> res2 = acMan.getEAUserForCreditials("g", "1", null);
		
		student = res2.getOpResult();
		lecturer = res1.getOpResult();
	}
	
	/*Test getExamById. Simply check it by getting already added objects from the base. */
	@Test
	public void test1() {
		Exam ex2 = exManager.getExamByExamId(2);
		Exam ex3 = exManager.getExamByExamId(3);

		assertTrue(ex2 != null);
		assertTrue(ex3 != null);
		assertEquals(2, ex2.getExamID());
		assertEquals(3, ex3.getExamID());
	}
	
	
	/*Test getExamById. Simply check it's edge cases. */
	@Test
	public void test2() {
		Exam ex1 = exManager.getExamByExamId(9); // no such exam in the base.
		Exam ex4 = exManager.getExamByExamId(-1); //edge case.

		assertTrue(ex4 == null);
		assertEquals(ex1.getExamID(), 0);
	}
	
	/*Test createNewExam, creates new exam (done in the @before) and after that checks if exam is in the database.*/
	@Test
	public void test3() {
		examId = exManager.createNewExam(lecturer.getUserID(), 
				"Linear Algebra", "Open note", null, null, 120, 2,"Midterm" );
		exam = exManager.getExamByExamId(examId);
		
		assertTrue(exam != null);
		assertEquals(exam.getName(), "Linear Algebra");
		assertEquals(exam.getType(), "Midterm");
		assertEquals(exam.getDuration(), 120);
	}
	
	/*Test createNewExam, checks if exam is in the database.*/
	@Test
	public void test4() {
		int examId = exManager.createNewExam(lecturer.getUserID(), 
				"Linear Algebra", "Open note", null, null, 120, 2, "Final" );
		Exam ex = exManager.getExamByExamId(examId);
		
		assertTrue(ex != null);
		assertEquals(ex.getName(), "Linear Algebra");
		assertEquals(ex.getType(), "Final");
		assertEquals(ex.getDuration(), 120);
	}
	
	/*Test modifyExam.*/
	@Test
	public void test5() {
		Exam test = exManager.modifyExam(examId, "Linear Algebra", "Open note", 
				null, null, 90, 2, "Midterm", "Proccesing");
		Exam ex = exManager.getExamByExamId(examId);

		System.out.println(test.toString());
		assertTrue(ex != null);
		assertEquals(ex.getName(), "Linear Algebra");
		assertEquals(ex.getType(), "Midterm");
		assertEquals(ex.getDuration(), 90);
		assertEquals(ex.getStatus(), "Proccesing");
	}
	

}
