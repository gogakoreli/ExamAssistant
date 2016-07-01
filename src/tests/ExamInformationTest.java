package tests;

import static org.junit.Assert.assertEquals;
import java.sql.Date;
import org.junit.Test;
import models.ExamInformation;

public class ExamInformationTest {
	
	ExamInformation exInf = new ExamInformation();
	
	@Test
	public void testSetsAndGetsOne() {	
		exInf.setEndTime(new Date(10));
		exInf.setIp("223p");
		exInf.setPlaceID(120);
		exInf.setPlaceNumber(120);
		exInf.setStartTime(new Date(20));
		exInf.setUserExamID(12);
		exInf.setUserPlaceID(13);
		exInf.setVariant(1);
		exInf.setWorking(true);
		
		assertEquals(exInf.getEndTime(), new Date(10));
		assertEquals(exInf.getIp(), "223p");
		assertEquals(exInf.getPlaceID(), 120);
		assertEquals(exInf.getPlaceNumber(), 120);
		assertEquals(exInf.getStartTime(), new Date(20));
		assertEquals(exInf.getUserExamID(), 12);
		assertEquals(exInf.getUserPlaceID(),13);
		assertEquals(exInf.getVariant(), 1);
		assertEquals(exInf.isWorking(), true);
		
		exInf.setWorking(false);
		assertEquals(exInf.isWorking(), false);
	}
	
	@Test
	public void testSetsAndGetsOne1() {	
		exInf.setEndTime(new Date(100));
		exInf.setIp("223p3");
		exInf.setPlaceID(1);
		exInf.setPlaceNumber(23);
		exInf.setStartTime(new Date(30));
		exInf.setUserExamID(122);
		exInf.setUserPlaceID(213);
		exInf.setVariant(2);		
			
		assertEquals(exInf.getEndTime(), new Date(100));
		assertEquals(exInf.getIp(), "223p3");
		assertEquals(exInf.getPlaceID(), 1);
		assertEquals(exInf.getPlaceNumber(), 23);
		assertEquals(exInf.getStartTime(), new Date(30));
		assertEquals(exInf.getUserExamID(), 122);
		assertEquals(exInf.getUserPlaceID(), 213);
		assertEquals(exInf.getVariant(), 2);
	}
	
	@Test
	public void testSetsAndGetsOne2() {	
		exInf.setEndTime(new Date(50));
		exInf.setIp("2");
		exInf.setPlaceID(34);
		exInf.setPlaceNumber(23);
		exInf.setStartTime(new Date(60));
		exInf.setUserExamID(12);
		exInf.setUserPlaceID(23);
		exInf.setVariant(2);		
			
		assertEquals(exInf.getEndTime(), new Date(50));
		assertEquals(exInf.getIp(), "2");
		assertEquals(exInf.getPlaceID(), 34);
		assertEquals(exInf.getPlaceNumber(), 23);
		assertEquals(exInf.getStartTime(), new Date(60));
		assertEquals(exInf.getUserExamID(), 12);
		assertEquals(exInf.getUserPlaceID(), 23);
		assertEquals(exInf.getVariant(), 2);
	}
	
}
