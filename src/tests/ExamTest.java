package tests;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.junit.Test;
import models.Exam;
import models.Exam.ExamStatus;
import models.Exam.ExamType;
import models.Exam.NoteType;
import models.Lecturer;

public class ExamTest {
	 
	Exam exam = new Exam();

	@Test
	public void test1() {	
		assertEquals(exam.getDuration(), 0);
		assertEquals(exam.getNoteType(), "");
		assertEquals(exam.getName(), "");
		assertEquals(exam.getResourceType(), "");
		assertEquals(exam.getNumVariants(), 0);
		assertEquals(exam.getType(), "");
		assertEquals(exam.getStatus(), "");
	}
	
	@Test
	public void testSetsAndGetsOne() {	
		EAUserTestHelper creator = new EAUserTestHelper();
		creator.setUserID(3);
		
		exam.setCreator(creator);
		exam.setCreatorId(1);
		exam.setDuration(120);
		exam.setExamID(3);
		exam.setName("Calculus");
		exam.setNoteType(NoteType.OPEN_BOOK);
		exam.setNumVariants(2);
		exam.setResourceType("Open");
		exam.setStartTime(new Timestamp(120));
		exam.setStatus(ExamStatus.NEW);
		exam.setType(ExamType.FINAL);
		
		assertEquals(exam.getCreator(), creator);		
		assertEquals(exam.getCreatorId(), 1);
		assertEquals(exam.getDuration(), 120);
		assertEquals(exam.getExamID(), 3);
		assertEquals(exam.getName(), "Calculus");
		assertEquals(exam.getNoteType(), NoteType.OPEN_BOOK);
		assertEquals(exam.getNumVariants(), 2);
		assertEquals(exam.getResourceType(), "Open");
		assertEquals(exam.getType(),ExamType.FINAL);
		assertEquals(exam.getStartTime(), new Timestamp(120));
		assertEquals(exam.getStartDateTime(), new Timestamp(120));
		assertEquals(exam.getStatus(), ExamStatus.NEW);
	}
	
	@Test
	public void testSetsAndGetsTwo() {	
		exam = new Exam(2, "c", "MidTerm", new Timestamp(10), 120, "Open", 2, ExamStatus.FINISHED);
				
		assertEquals(exam.getDuration(), 120);
		assertEquals(exam.getExamID(), 2);
		assertEquals(exam.getName(), "c");
		assertEquals(exam.getResourceType(), "Open");
		assertEquals(exam.getNumVariants(), 2);
		assertEquals(exam.getType(), "MidTerm");
		assertEquals(exam.getStartDateTime(), new Timestamp(10));
		assertEquals(exam.getStatus(), ExamStatus.FINISHED);
	}
	
	@Test
	public void testSetsAndGetsThree() {	
		exam = new Exam(3, "c1", "MidTerm", new Timestamp(10), 90, "Open", 2, ExamStatus.LIVE);
				
		assertEquals(exam.getDuration(), 90);
		assertEquals(exam.getExamID(), 3);
		assertEquals(exam.getName(), "c1");
		assertEquals(exam.getResourceType(), "Open");
		assertEquals(exam.getNumVariants(), 2);
		assertEquals(exam.getType(), "MidTerm");
		assertEquals(exam.getStartDateTime(), new Timestamp(10));
		assertEquals(exam.getStatus(), ExamStatus.LIVE);
	}
	
	
	@Test
	public void testSubLecturers() {	
		ArrayList<Lecturer> lect = new ArrayList<>();
		Lecturer lec = new Lecturer();
		lec.setUserID(1);
		Lecturer lec2 = new Lecturer();
		lec2.setUserID(2);
		lect.add(lec);
		lect.add(lec2);
		
		exam.setSubLecturers(lect);
		assertEquals(exam.getSubLecturers(), lect);
	}
	
	@Test
	public void testEmptyExam() {	
		Exam ex = new Exam();
		assertTrue(ex.isEmptyExam());
	}

}
