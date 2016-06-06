package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import helper.AccountManager;
import helper.OpResult;
import models.EAUser;

public class AccountManagerTester {
	AccountManager am;

	@Before
	public void setUp() throws Exception {
		am = new AccountManager();
	}

	@Test
	public void test1() {
		OpResult<EAUser> res = am.getEAUserForCreditials("patrick@freeuni.edu.ge", "1234");
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertEquals(user.getFirstName(), "Patrick");
		assertEquals(user.getLastName(), "Querrel");
		assertEquals(user.getMail(), "patrick@freeuni.edu.ge");
		assertEquals(user.getRoleByString("student"), EAUser.EAUserRole.STUDENT);
	}
	
	@Test
	public void test2() {
		OpResult<EAUser> res = am.getEAUserForCreditials("Molly@freeuni.edu.ge", "FloPup");
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertEquals(user.getFirstName(), "Molly");
		assertEquals(user.getLastName(), "Smith");
		assertEquals(user.getMail(), "Molly@freeuni.edu.ge");
		assertEquals(user.getRoleByString("lecturer"), EAUser.EAUserRole.LECTURER);
	}
	
	@Test
	public void test3() {
		OpResult<EAUser> res = am.getEAUserForCreditials("natela@freeuni.edu.ge", "natinati");
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertEquals(user.getFirstName(), "Natela");
		assertEquals(user.getLastName(), "Shixiashvili");
		assertEquals(user.getMail(), "natela@freeuni.edu.ge");
		assertEquals(user.getRoleByString("board"), EAUser.EAUserRole.BOARD);
	}
	
	@Test
	public void test4() {
		OpResult<EAUser> res = am.getEAUserForCreditials("Shalva", "Natelashvili");
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertTrue(user == AccountManager.NO_USER_FOUND_CONSTANT);
	}
	
	

}
