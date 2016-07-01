package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import models.EAUser.EAUserRole;
import models.Student;


public class EAUserTest {
	
	EAUserTestHelper user = new EAUserTestHelper();
	
	
	@Test
	public void testSetsAndGetsOne() {	
		user.setUserID(1);
		user.setFirstName("kakha");
		user.setLastName("Beridze");
		user.setMail("b@gmai.com");
		user.setRole(EAUserRole.STUDENT);
		user.setImage(null);
		user.setGoogleID(null);

		assertEquals(user.getUserID(), 1);
		assertEquals(user.getFirstName(), "kakha");
		assertEquals(user.getLastName(), "Beridze");
		assertEquals(user.getMail(), "b@gmai.com");
		assertEquals(user.getRole(), EAUserRole.STUDENT);
		assertEquals(user.getImage(), null);
		assertEquals(user.getGoogleID(), null);
	}
	
	@Test
	public void testSetsAndGetsTwo() {	
		user.setUserID(-3);
		user.setFirstName("");
		user.setLastName("Smith");
		user.setMail("S@gmai.com");
		user.setRole(EAUserRole.LECTURER);
		user.setImage(null);
		user.setGoogleID("3ddd");

		assertEquals(user.getUserID(), -3);
		assertEquals(user.getFirstName(), "");
		assertEquals(user.getLastName(), "Smith");
		assertEquals(user.getMail(), "S@gmai.com");
		assertEquals(user.getRole(), EAUserRole.LECTURER);
		assertEquals(user.getImage(), null);
		assertEquals(user.getGoogleID(), "3ddd");
	}
	
	
	@Test
	public void testEquals1() {	
		this.user.setUserID(3);
		EAUserTestHelper user = new EAUserTestHelper();
		
		user.setUserID(-3);
		assertFalse(user.equals(this.user));
		
		user.setUserID(3);
		assertTrue(user.equals(this.user));
	}
	
	@Test
	public void testEquals2() {	
		this.user.setUserID(3);
		EAUserTestHelper user = null;
		
		assertEquals(false, this.user.equals(user));
		
		Student st = new Student();
		assertFalse(this.user.equals(st));
	}
	
	@Test
	public void testEquals3() {	
		user = new EAUserTestHelper(3, EAUserRole.BOARD, "board@gmail.com", "molly", "Smith", null, null);
		EAUserTestHelper user = new EAUserTestHelper();
		
		user.setUserID(3);
		assertTrue(user.equals(this.user));
	}

}
