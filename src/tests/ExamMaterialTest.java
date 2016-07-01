package tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import models.ExamMaterial;

public class ExamMaterialTest {

	ExamMaterial exMat = new ExamMaterial();
	
	@Test
	public void testSetsAndGets() {	
		exMat.setExamID(2);
		exMat.setLocation("\\:c");
		exMat.setMaterial("adams");
		exMat.setMaterialID(4);
		exMat.setMaterialType("Book");
		exMat.setVariant(2);
				
		assertEquals(exMat.getExamID(), 2);
		assertEquals(exMat.getLocation(), "\\:c");
		assertEquals(exMat.getMaterial(), "adams");
		assertEquals(exMat.getMaterialID(), 4);
		assertEquals(exMat.getMaterialType(), "Book");
		assertEquals(exMat.getVariant(), 2);
	}
	
	@Test
	public void testSetsAndGets2() {	
		exMat.setExamID(4);
		exMat.setLocation("");
		exMat.setMaterial("");
		exMat.setMaterialID(4);
		exMat.setMaterialType("Book");
		exMat.setVariant(3);
				
		assertEquals(exMat.getExamID(), 4);
		assertEquals(exMat.getLocation(), "");
		assertEquals(exMat.getMaterial(), "");
		assertEquals(exMat.getMaterialID(), 4);
		assertEquals(exMat.getMaterialType(), "Book");
		assertEquals(exMat.getVariant(), 3);
	}
	
	@Test
	public void testSetsAndGets3() {	
		exMat.setExamID(0);
		exMat.setLocation("\\cc");
		exMat.setMaterial("adams2");
		exMat.setMaterialID(23);
		exMat.setMaterialType("Book");
		exMat.setVariant(3);
				
		assertEquals(exMat.getExamID(), 0);
		assertEquals(exMat.getLocation(), "\\cc");
		assertEquals(exMat.getMaterial(), "adams2");
		assertEquals(exMat.getMaterialID(), 23);
		assertEquals(exMat.getMaterialType(), "Book");
		assertEquals(exMat.getVariant(), 3);
	}

}
