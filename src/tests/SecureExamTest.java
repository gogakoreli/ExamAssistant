package tests;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.ArrayList;

import org.junit.Test;

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
	public void test1() {	
		assertEquals(exam.isExamNew(), true);
		assertEquals(exam.getCreatorName(), "");
		assertEquals(exam.getDuration(), 0);
		assertEquals(exam.getNoteType(), "");
		assertEquals(exam.getName(), "");
		assertEquals(exam.getType(), "");
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
		assertEquals(exam.getNoteType(), "");
		assertEquals(exam.getName(), "");
		assertEquals(exam.getType(), "");
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
		assertEquals(exam.getNoteType(), "");
		assertEquals(exam.getName(), "calculus");
	}
	
	@Test
	public void test4() {	
		examToSecure.setStartTime(new Date(20));
		examToSecure.setExamID(2);

		exam = new SecureExam(examToSecure);
		
		assertEquals(exam.getExamStartTime(), "04:00" );
		assertEquals(exam.getExamStartDate(), "2016-06-02" );
		assertEquals(exam.getStartDateTime(), new Date(20));
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
		
		editor.setRole(EAUserRole.LECTURER);
		examToSecure.setStatus(ExamStatus.NEW);
		examToSecure.setExamID(1);

		exam = new SecureExam(examToSecure);
		exam.setExamEditor(editor);
		exam.setName("Calculus");
		assertEquals(exam.getName(), "Calculus");
	}
	
	@Test
	public void test6() {	
		Lecturer editor = new Lecturer();
		editor.setRole(EAUserRole.LECTURER);
		examToSecure.setStatus(ExamStatus.NEW);
		
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
	}		
	
	
	@Test
	public void test8() {	
		ExamBoard editor1 = new ExamBoard();
		exam.setExamEditor(editor1);
		exam.setNoteType(NoteType.OPEN_BOOK);
		
		assertEquals(exam.getNoteType(), "");
		
		Lecturer editor = new Lecturer();
		exam.setExamEditor(editor);
		exam.setNoteType(NoteType.OPEN_BOOK);
		
		assertEquals(exam.getNoteType(), NoteType.OPEN_BOOK);
	}	
	
	@Test
	public void test9() {	
		Lecturer editor = new Lecturer();
		exam.setExamEditor(editor);
		exam.setStartTime(new Date(12));
		assertEquals(exam.getStartDateTime(), null);
		
		exam.setExamEditor(editor);
		examToSecure.setStatus(ExamStatus.PENDING);
		exam.setStartTime(new Date(12));
		assertEquals(exam.getStartDateTime(), null);
		
		ExamBoard editor1 = new ExamBoard();
		exam.setExamEditor(editor);
		examToSecure.setStatus(ExamStatus.NEW);
		exam.setStartTime(new Date(12));
		assertEquals(exam.getStartDateTime(), null);
		
		examToSecure.setStatus(ExamStatus.PENDING);
		exam.setExamEditor(editor1);
		exam.setStartTime(new Date(12));
		assertEquals(exam.getStartDateTime(), new Date(12));
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
	}	

}
