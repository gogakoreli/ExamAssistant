package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import helper.AccountManager;
import helper.OpResult;
import models.EAUser;

public class AccountManagerTester {
	AccountManager accManagar;

	@Before
	public void setUp() throws Exception {
		accManagar = new AccountManager();
	}

	@Test
	/* test method getEAUserForCreditials() for user patrick*/
	public void testGetUserCreditials1() {
		OpResult<EAUser> res = accManagar.getEAUserForCreditials("patrick@freeuni.edu.ge", "1234", null);
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertEquals(user.getFirstName(), "Patrick");
		assertEquals(user.getLastName(), "Querrel");
		assertEquals(user.getMail(), "patrick@freeuni.edu.ge");
		assertEquals(EAUser.getRoleByString("student"), EAUser.EAUserRole.STUDENT);
	}
	
	@Test
	/* test method getEAUserForCreditials() for user Molly */
	public void testGetUserCreditials2() {
		OpResult<EAUser> res = accManagar.getEAUserForCreditials("Molly@freeuni.edu.ge", "FloPup", null);
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertEquals(user.getFirstName(), "Molly");
		assertEquals(user.getLastName(), "Smith");
		assertEquals(user.getMail(), "Molly@freeuni.edu.ge");
		assertEquals(EAUser.getRoleByString("lecturer"), EAUser.EAUserRole.LECTURER);
	}
	
	@Test
	/* test method getEAUserForCreditials() for user natela */
	public void testGetUserCreditials3() {
		OpResult<EAUser> res = accManagar.getEAUserForCreditials("natela@freeuni.edu.ge", "natinati", null);
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertEquals(user.getFirstName(), "Natela");
		assertEquals(user.getLastName(), "Shixiashvili");
		assertEquals(user.getMail(), "natela@freeuni.edu.ge");
		assertEquals(EAUser.getRoleByString("board"), EAUser.EAUserRole.BOARD);
	}
	
	@Test
	/* test method getEAUserForCreditials() for user whos username not existing in database */
	public void testGetUserCreditials4() {
		OpResult<EAUser> res = accManagar.getEAUserForCreditials("Shalva", "Natelashvili", null);
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertTrue(user == AccountManager.NO_USER_FOUND_CONSTANT);
	}
	
	@Test
	/* test method getEAUserForCreditials() for user existing in database 
	 * but with wrong password */
	public void testGetUserCreditials5() {
		OpResult<EAUser> res = accManagar.getEAUserForCreditials("Molly@freeuni.edu.ge", "1234", null);
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertTrue(user == AccountManager.NO_USER_FOUND_CONSTANT);
	}
	
	@Test
	/* method getEAUserForCreditials() for user Molly 
	 * test CaseSenitivity of userName 
	 * 
	 * test CaseSenitivity of password */
	public void testGetUserCreditials6() {
		OpResult<EAUser> res = accManagar.getEAUserForCreditials("MOLLY@freEUni.eDu.g ", "FloPup", null);
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertEquals(user.getMail(), null);
	
	}
	
	

}
