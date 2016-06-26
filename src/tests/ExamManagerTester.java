package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data_managers.AccountManager;
import data_managers.ExamManager;
import helper.OpResult;
import models.EAUser;
import models.Exam;
import models.Lecturer;
import models.Student;

public class ExamManagerTester {
	ExamManager exManager;
	AccountManager acMan;
	EAUser lecturer;
	EAUser student1;
	EAUser student2;
	boolean existsLecturer;
	boolean existsStudent1;
	boolean existsStudent2;

	@Before
	public void setUp() throws Exception {
		exManager = new ExamManager();
		acMan = new AccountManager();

		initLecturer();

		String userName = "ameli14@freeuni.edu.ge";
		String password = "amimelia";
		String role = "student";
		String firstName = "Amiran";
		String lastName = "Melia";

		existsStudent1 = false;
		if (acMan.userExists(userName, password)) {
			existsStudent1 = true;
		} else {
			acMan.addUser(userName, password, role, firstName, lastName);
		}

		OpResult<EAUser> res2 = acMan.getEAUserForCreditials(userName, password, null);
		student1 = res2.getOpResult();

		userName = "ggona14@freeuni.edu.ge";
		password = "giogona";
		role = "student";
		firstName = "Giorgi";
		lastName = "Gonashvili";

		existsStudent2 = false;
		if (acMan.userExists(userName, password)) {
			existsStudent2 = true;
		} else {
			acMan.addUser(userName, password, role, firstName, lastName);
		}

		OpResult<EAUser> res3 = acMan.getEAUserForCreditials(userName, password, null);
		student2 = res3.getOpResult();

	}

	@After
	public void doFinally() {
		if (!existsLecturer) {
			int userId = lecturer.getUserID();
			acMan.removeUserByID(userId);
		}

		if (!existsStudent1) {
			int userId = student1.getUserID();
			acMan.removeUserByID(userId);
		}

		if (!existsStudent2) {
			int userId = student2.getUserID();
			acMan.removeUserByID(userId);
		}

	}

	private void initLecturer() {
		String userName = "a.mosidze@freeuni.edu.ge";
		String password = "alMosi";
		String role = "lecturer";
		String firstName = "Aleksandre";
		String lastName = "Mosidze";
		existsLecturer = false;
		if (acMan.userExists(userName, password)) {
			existsLecturer = true;
		} else {
			acMan.addUser(userName, password, role, firstName, lastName);
		}

		OpResult<EAUser> res1 = acMan.getEAUserForCreditials(userName, password, null);
		lecturer = res1.getOpResult();
	}

	/*
	 * Test createNewExam() and getExamByExamId().
	 */
	@Test
	public void test1() {
		String examName = "albatoba";
		String openBook = Exam.CLOSED_BOOK;
		String[] subLecturers = null;
		File[] materials = null;
		int examDuration = 100;
		int numVariants = 1;
		String examType = Exam.EXAM_TYPE_FINAL;
		int examId = exManager.createNewExam(lecturer.getUserID(), examName, openBook, subLecturers, materials,
				examDuration, numVariants, examType);
		Exam myExam = exManager.getExamByExamId(examId);
		assertEquals(examId, myExam.getExamID());
		assertEquals(examName, myExam.getName());
		assertEquals(openBook, myExam.getResourceType());
		assertEquals(examType, myExam.getType());
		assertEquals(examDuration, myExam.getDuration());
		assertEquals(numVariants, myExam.getNumVariants());
		exManager.deleteExam(examId);
	}
	
	@Test
	public void testGetExamById(){
		//we asume that exam by id 1231231 is not in db if this test fails we should check if it was created 
		Exam myExam = exManager.getExamByExamId(1231231);
		assertEquals(true, myExam == ExamManager.EMPTY_EXAM);
		
		Exam myExam1 = exManager.getExamByExamId(1231232);
		assertEquals(true, myExam1 == ExamManager.EMPTY_EXAM);
	}
	
	
	@Test
	public void TestCanUserAccessExam(){
		assertEquals(true, exManager.CanUserAccessExam(lecturer, 1));//check user has permission as creator
		assertEquals(true, exManager.CanUserAccessExam(lecturer, 2));//check user has permission as sublectuer
		assertEquals(false, exManager.CanUserAccessExam(lecturer, 113));//check user has not permission on lecture 
	}


	@Test
	public void TestDownloadSubLecturers(){
		Exam noSubsExam = exManager.getExamByExamId(130);
		assertEquals(true, exManager.downloadSubLecturers(noSubsExam).isEmpty());//exam with not sub lecturers 
		
		Exam examSubCurrent = exManager.getExamByExamId(131);
		assertEquals(true, exManager.downloadSubLecturers(examSubCurrent).contains(lecturer));//exam with sub lecturer current lecturer
		
		Exam examSubMult = exManager.getExamByExamId(132);
		assertEquals(true, exManager.downloadSubLecturers(examSubMult).size() == 2);//exam with multiple sub lecturer 
		
		Exam examSubMult2 = exManager.getExamByExamId(2);
		assertEquals(true, exManager.downloadSubLecturers(examSubMult2).size() == 4);//exam with multiple sub lecturer 
	}
	
	
	
	/*
	 * Test getExamForStudent(). Simply check it by adding new exam with current
	 * time
	 */
	@Test
	public void test2() {
		String examName = "albatoba";
		String openBook = Exam.CLOSED_BOOK;
		String[] subLecturers = null;
		File[] materials = null;
		int examDuration = 100;
		int numVariants = 1;
		String examType = Exam.EXAM_TYPE_FINAL;
		int examId = exManager.createNewExam(lecturer.getUserID(), examName, openBook, subLecturers, materials,
				examDuration, numVariants, examType);
		exManager.addExamForStudent(student1.getUserID(), examId);
		Exam myExam = exManager.getExamForStudent((Student) student1);
		assertEquals(examId, myExam.getExamID());
		assertEquals(examName, myExam.getName());
		assertEquals(openBook, myExam.getResourceType());
		assertEquals(examType, myExam.getType());
		assertEquals(examDuration, myExam.getDuration());
		assertEquals(numVariants, myExam.getNumVariants());
		exManager.deleteExam(examId);
	}

	/*
	 * Test modifyExam().
	 */
	@Test
	public void test3() {
		String examName = "albatoba";
		String openBook = Exam.CLOSED_BOOK;
		String[] subLecturers = null;
		File[] materials = null;
		int examDuration = 100;
		int numVariants = 1;
		String examType = Exam.EXAM_TYPE_FINAL;
		int examId = exManager.createNewExam(lecturer.getUserID(), examName, openBook, subLecturers, materials,
				examDuration, numVariants, examType);

		String newExamName = "kalkulusi";
		String newOpenBook = Exam.OPEN_BOOK;
		String[] newSubLecturers = null;
		File[] newMaterials = null;
		int newExamDuration = 200;
		int newNumVariants = 2;
		String newExamType = Exam.EXAM_TYPE_MIDTERM;
		String newExamStatus = Exam.EXAM_STATUS_WAITING;
		Exam modExam = exManager.modifyExam(examId, newExamName, newOpenBook, newSubLecturers, newMaterials,
				newExamDuration, newNumVariants, newExamType, newExamStatus);
		assertEquals(examId, modExam.getExamID());
		assertEquals(newExamName, modExam.getName());
		assertEquals(newOpenBook, modExam.getResourceType());
		assertEquals(newExamType, modExam.getType());
		assertEquals(newExamDuration, modExam.getDuration());
		assertEquals(newNumVariants, modExam.getNumVariants());
		exManager.deleteExam(examId);
	}

	/*
	 * Test getAllExamsForLecturer().
	 */
	@Test
	public void test4() {
		String examName = "albatoba";
		String openBook = Exam.CLOSED_BOOK;
		String[] subLecturers = null;
		File[] materials = null;
		int examDuration = 100;
		int numVariants = 1;
		String examType = Exam.EXAM_TYPE_FINAL;
		int albatobaExamId = exManager.createNewExam(lecturer.getUserID(), examName, openBook, subLecturers, materials,
				examDuration, numVariants, examType);
		//Exam albatobaExam = exManager.getExamByExamId(albatobaExamId);

		String newExamName = "kalkulusi";
		String newOpenBook = Exam.OPEN_BOOK;
		String[] newSubLecturers = null;
		File[] newMaterials = null;
		int newExamDuration = 200;
		int newNumVariants = 2;
		String newExamType = Exam.EXAM_TYPE_MIDTERM;

		int kalkulusiExamId = exManager.createNewExam(lecturer.getUserID(), newExamName, newOpenBook, newSubLecturers,
				newMaterials, newExamDuration, newNumVariants, newExamType);
		//Exam kalkulusiExam = exManager.getExamByExamId(kalkulusiExamId);
		
		ArrayList<Exam> exams = exManager.getAllExamsForLecturer((Lecturer) lecturer);
		
		
		Exam curExam = exams.get(0);
		assertEquals(albatobaExamId, curExam.getExamID());
		assertEquals(examName, curExam.getName());
		assertEquals(openBook, curExam.getResourceType());
		assertEquals(examType, curExam.getType());
		assertEquals(examDuration, curExam.getDuration());
		assertEquals(numVariants, curExam.getNumVariants());
		
		curExam = exams.get(1);
		assertEquals(kalkulusiExamId, curExam.getExamID());
		assertEquals(newExamName, curExam.getName());
		assertEquals(newOpenBook, curExam.getResourceType());
		assertEquals(newExamType, curExam.getType());
		assertEquals(newExamDuration, curExam.getDuration());
		assertEquals(newNumVariants, curExam.getNumVariants());
		
		exManager.deleteExam(albatobaExamId);
		exManager.deleteExam(kalkulusiExamId);
	}

	/*
	 * Test getAllExamsForBoard().
	 */
	@Test
	public void test5() {
		// TODO test getAllExamsForBoard()
	}

	/*
	 * Test startExam().
	 */
	@Test
	public void test6() {
		// TODO test startExam()
	}

	/*
	 * Test canStartExam().
	 */
	@Test
	public void test7() {
		// TODO test canStartExam()
	}

	/*
	 * Test updateStudentExamInformation().
	 */
	@Test
	public void test8() {
		// TODO test updateStudentExamInformation()
	}

}
