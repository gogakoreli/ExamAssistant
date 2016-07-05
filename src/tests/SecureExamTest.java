package tests;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.junit.Test;

import helper.OpResult;
import models.Admin;
import models.EAUser.EAUserRole;
import models.Exam;
import models.Exam.ExamStatus;
import models.Exam.ExamType;
import models.Exam.NoteType;
import models.ExamBoard;
import models.Lecturer;
import models.SecureExam;
import models.Student;


public class SecureExamTest {
	Exam examToSecure = new Exam();
	SecureExam exam = new SecureExam(examToSecure);

	
	@Test
	public void test0() {	
		Exam ex = exam.getEditedExam();
		assertEquals(ex.getName(), "");
		assertEquals(ex.getExamID(), 0);
	}
	
	
	@Test
	public void test1() {	
		assertEquals(exam.isExamNew(), true);
		assertEquals(exam.getCreatorName(), "");
		assertEquals(exam.getDuration(), 0);
		assertEquals(exam.getName(), "");
		assertEquals(exam.getType(), "");
		assertEquals(exam.getExamStatus(), "new");
		assertEquals(exam.getExamEditor(), null);
	}
	
	@Test
	public void test2() {	
		EAUserTestHelper creator = new EAUserTestHelper();
		creator.setUserID(1);
		examToSecure.setCreator(creator);
		
		assertEquals(exam.getCreatorName(), "");
		assertEquals(exam.getExamStartDate(), "UNDEFINED");
		assertEquals(exam.getExamStartTime(), "UNDEFINED");
		assertEquals(exam.getDuration(), 0);
		assertEquals(exam.getName(), "");
		assertEquals(exam.getType(), "");
		
		examToSecure.setStatus(ExamStatus.PENDING);
		examToSecure.setExamID(2);
		assertEquals(exam.getExamStatus(), "pending");
		exam.getExamStartDate();
	}
	
	
	@Test
	public void test3() {	
		EAUserTestHelper creator = new EAUserTestHelper();
		creator.setUserID(1);
		creator.setFirstName("Aleqsandre");
		creator.setLastName("Meskhi");
		examToSecure.setCreator(creator);
		examToSecure.setExamID(2);
		examToSecure.setName("calculus");
		examToSecure.setDuration(120);
		
		exam = new SecureExam(examToSecure);
		
		assertEquals(exam.getCreatorName(), "Aleqsandre Meskhi");
		assertEquals(exam.getExamID(), 2);
		assertEquals(exam.getDuration(), 120);
		assertEquals(exam.getName(), "calculus");
	}
	
	@Test
	public void test4() {	
		examToSecure.setStartTime(new Timestamp(20));
		examToSecure.setExamID(2);

		exam = new SecureExam(examToSecure);
		assertEquals(exam.getExamStartTime(), "04:00" );
		assertEquals(exam.getStartDateTime(), new Timestamp(20));
		assertEquals(exam.getStartDateTime(), new Timestamp(20));
	}
	
	@Test
	public void test5() {
		Lecturer editor = new Lecturer();

		Student editor1 = new Student();
		examToSecure.setStatus(ExamStatus.NEW);
		exam = new SecureExam(examToSecure);
		exam.setExamEditor(editor1);
		exam.setName("Calculus");
		assertEquals(exam.getName(), "");
		
		examToSecure.setStatus(ExamStatus.LIVE);
		exam = new SecureExam(examToSecure);
		exam.setExamEditor(editor);
		exam.setName("Calculus");
		assertEquals(exam.getName(), "");
		
		examToSecure.setStatus(ExamStatus.LIVE);
		exam = new SecureExam(examToSecure);
		exam.setExamEditor(editor1);
		exam.setName("Calculus");
		assertEquals(exam.getName(), "");
		
		examToSecure.setStatus(ExamStatus.NEW);
		examToSecure.setExamID(1);
		exam = new SecureExam(examToSecure);
		exam.setExamEditor(editor);
		exam.setName("Calculus");
		assertEquals(exam.getName(), "Calculus");
		
		Admin admin = new Admin();
		exam = new SecureExam(examToSecure);
		exam.setExamEditor(admin);
		exam.setName("Calculus");
		assertEquals(exam.getName(), "Calculus");
	}
	
	@Test
	public void test6() {	
		Lecturer editor = new Lecturer();
				
		exam.setExamEditor(editor);
		exam.setDuration(120);
		assertEquals(exam.getDuration(), 0);
		
		editor.setRole(EAUserRole.BOARD);
		examToSecure.setStatus(ExamStatus.NEW);
		exam.setExamEditor(editor);
		exam.setDuration(120);
		assertEquals(exam.getDuration(), 0);
		
		editor.setRole(EAUserRole.LECTURER);
		examToSecure.setStatus(ExamStatus.LIVE);
		exam.setExamEditor(editor);
		exam.setDuration(120);
		assertEquals(exam.getDuration(), 0);
		
		editor.setRole(EAUserRole.STUDENT);
		examToSecure.setStatus(ExamStatus.LIVE);
		exam.setExamEditor(editor);
		exam.setDuration(120);
		assertEquals(exam.getDuration(), 0);
		
		Admin admin = new Admin();
		examToSecure.setStatus(ExamStatus.LIVE);
		examToSecure.setExamID(2);
		exam.setExamEditor(admin);
		exam.setDuration(120);
		assertEquals(exam.getDuration(), 120);
	}  
	
	
	@Test
	public void test7() {	
		ExamBoard editor1 = new ExamBoard();
		exam.setExamEditor(editor1);
		exam.setType(ExamType.FINAL);
		assertEquals(exam.getType(), "");
		
		Lecturer editor = new Lecturer();
		exam.setExamEditor(editor);
		exam.setType(ExamType.FINAL);
		assertEquals(exam.getType(), ExamType.FINAL);
		
		Admin admin = new Admin();
		exam.setExamEditor(admin);
		exam.setType(ExamType.FINAL);
		assertEquals(exam.getType(), ExamType.FINAL);
	}		
	
	
	@Test
	public void test8() {	
		ExamBoard editor1 = new ExamBoard();
		exam.setExamEditor(editor1);
		exam.setResourceType(NoteType.OPEN_BOOK);
		assertEquals(exam.getResourceType(), "");
		
		Lecturer editor = new Lecturer();
		exam.setExamEditor(editor);
		exam.setResourceType(NoteType.OPEN_BOOK);
		assertEquals(exam.getResourceType(), NoteType.OPEN_BOOK);
		
		Admin admin = new Admin();
		exam.setExamEditor(admin);
		exam.setResourceType(NoteType.OPEN_NOTE);
		assertEquals(exam.getResourceType(), NoteType.OPEN_NOTE);
	}	
	
	@Test
	public void test9() {	
		Lecturer editor = new Lecturer();
		exam.setExamEditor(editor);
		exam.setStartTime(new Timestamp(12));
		assertEquals(exam.getStartDateTime(), null);
		
		exam.setExamEditor(editor);
		examToSecure.setStatus(ExamStatus.PENDING);
		exam.setStartTime(new Timestamp(12));
		assertEquals(exam.getStartDateTime(), null);
		
		ExamBoard editor1 = new ExamBoard();
		examToSecure.setStatus(ExamStatus.NEW);
		exam.setStartTime(new Timestamp(12));
		assertEquals(exam.getStartDateTime(), null);
		
		examToSecure.setStatus(ExamStatus.PENDING);
		exam.setExamEditor(editor1);
		exam.setStartTime(new Timestamp(12));
		assertEquals(exam.getStartDateTime(),new Timestamp(12));
		
		Admin admin = new Admin();
		exam.setExamEditor(admin);
		exam.setStartTime(new Timestamp(14));
		assertEquals(exam.getStartDateTime(),new Timestamp(14));
	}	
	
	@Test
	public void test10() {	
		ArrayList<Lecturer> lect = new ArrayList<>();
		Lecturer lec = new Lecturer();
		lec.setUserID(1);
		lec.setFirstName("A");
		Lecturer lec2 = new Lecturer();
		lec2.setUserID(2);
		lec.setFirstName("C");
		lect.add(lec);
		lect.add(lec2);
		
		ExamBoard editor1 = new ExamBoard();
		exam = new SecureExam(examToSecure);
		exam.setExamEditor(editor1);
		exam.setSubLecturers(lect);
		assertEquals(exam.getSubLecturers().size(), 0);
		
		Lecturer editor = new Lecturer();
		exam.setExamEditor(editor);
		exam.setSubLecturers(lect);
		assertEquals(exam.getSubLecturers().size(), 0);
		
		examToSecure.setStatus(ExamStatus.PENDING);
		examToSecure.setExamID(1);
		exam.setExamEditor(editor);
		exam.setSubLecturers(lect);
		assertEquals(exam.getSubLecturers().size(), 2);
		
		Admin admin = new Admin();
		exam.setExamEditor(admin);
		Lecturer lec3 = new Lecturer();
		lec3.setUserID(3);
		lec3.setFirstName("C");
		lect.add(lec3);
		exam.setSubLecturers(lect);
		assertEquals(exam.getSubLecturers().size(), 3);
	}	
	
	
	@Test
	public void test11() throws SQLException {	
		OpResult<Boolean> res =  exam.canChangeStatus();
		
		examToSecure.setStatus(ExamStatus.NEW);
		res =  exam.canChangeStatus();
		assertEquals(res.getOpResult(), null);
		
		examToSecure.setName("OOP");
		res =  exam.canChangeStatus();
		assertEquals(res.getOpResult(), null);
		
		examToSecure.setDuration(120);
		res =  exam.canChangeStatus();
		assertEquals(res.getOpResult(), true);
		
		examToSecure.setStatus(ExamStatus.PENDING);
		res = exam.canChangeStatus();
		assertEquals(res.getOpResult(), null);

		examToSecure.setStatus(ExamStatus.LECTURER_READY);
		res = exam.canChangeStatus();
		assertEquals(res.getOpResult(), null);

		examToSecure.setStatus(ExamStatus.BOARD_READY);
		res = exam.canChangeStatus();
		assertEquals(res.getOpResult(), null);
	}
	
	@Test
	public void test12() {	
		String res = exam.getNextStatus();
		assertEquals(res,  ExamStatus.PENDING);
		
		examToSecure.setStatus(ExamStatus.NEW);
		res = exam.getNextStatus();
		assertEquals(res,  ExamStatus.PENDING);
		
		examToSecure.setStatus(ExamStatus.PENDING);
		res = exam.getNextStatus();
		assertEquals(res,  ExamStatus.LECTURER_READY);
		
		examToSecure.setStatus(ExamStatus.LECTURER_READY);
		res = exam.getNextStatus();
		assertEquals(res,  ExamStatus.PUBLISHED);
		
		examToSecure.setStatus(ExamStatus.BOARD_READY);
		res = exam.getNextStatus();
		assertEquals(res,  ExamStatus.PUBLISHED);
	}
	
	@Test
	public void test13() {	
		ExamBoard editor = new ExamBoard();
		exam.setExamEditor(editor);
		examToSecure.setStatus(ExamStatus.PENDING);
		String res = exam.getNextStatus();
		assertEquals(res,  ExamStatus.BOARD_READY);
	}
}
