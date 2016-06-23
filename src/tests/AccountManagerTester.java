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
	public void test1() {
		String userName = "patrick@freeuni.edu.ge";
		String password = "1234";
		String role = "student";
		String firstName = "Patrick";
		String lastName = "Querrel";
		boolean exists = false;
		if(accManagar.userExists(userName, password)){
			exists = true;
		}else{
			accManagar.addUser(userName, password, role, firstName, lastName);
		}
		OpResult<EAUser> res = accManagar.getEAUserForCreditials(userName, password, null);
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertEquals(user.getFirstName(), firstName);
		assertEquals(user.getLastName(), lastName);
		assertEquals(user.getMail(), userName);
		assertEquals(EAUser.getRoleByString(role), EAUser.EAUserRole.STUDENT);
		if(!exists){
			int userId = user.getUserID();
			accManagar.removeUserByID(userId);
		}
	}
	
	@Test
	/* test method getEAUserForCreditials() for user Molly */
	public void test2() {
		String userName = "Molly@freeuni.edu.ge";
		String password = "FloPup";
		String role = "lecturer";
		String firstName = "Molly";
		String lastName = "Smith";
		boolean exists = false;
		if(accManagar.userExists(userName, password)){
			exists = true;
		}else{
			accManagar.addUser(userName, password, role, firstName, lastName);
		}
		OpResult<EAUser> res = accManagar.getEAUserForCreditials(userName, password, null);
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertEquals(user.getFirstName(), firstName);
		assertEquals(user.getLastName(), lastName);
		assertEquals(user.getMail(), userName);
		assertEquals(EAUser.getRoleByString(role), EAUser.EAUserRole.LECTURER);
		if(!exists){
			int userId = user.getUserID();
			accManagar.removeUserByID(userId);
		}
	}
	
	@Test
	/* test method getEAUserForCreditials() for user natela */
	public void test3() {
		String userName = "natela@freeuni.edu.ge";
		String password = "natinati";
		String role = "board";
		String firstName = "Natela";
		String lastName = "Shixiashvili";
		boolean exists = false;
		if(accManagar.userExists(userName, password)){
			exists = true;
		}else{
			accManagar.addUser(userName, password, role, firstName, lastName);
		}
		OpResult<EAUser> res = accManagar.getEAUserForCreditials(userName, password, null);
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertEquals(user.getFirstName(), firstName);
		assertEquals(user.getLastName(), lastName);
		assertEquals(user.getMail(), userName);
		assertEquals(EAUser.getRoleByString(role), EAUser.EAUserRole.BOARD);
		if(!exists){
			int userId = user.getUserID();
			accManagar.removeUserByID(userId);
		}
	}
	
	@Test
	/* test method getEAUserForCreditials() for user whos username not existing in database */
	public void test4() {
		String userName = "shalva@freeuni.edu.ge";
		String password = "Natelashvili";
		if(!accManagar.userExists(userName, password)){
			OpResult<EAUser> res = accManagar.getEAUserForCreditials("shalva@freeuni.edu.ge", "Natelashvili", null);
			assertTrue(res.isSuccess());
			EAUser user = res.getOpResult();
			assertTrue(user == AccountManager.NO_USER_FOUND_CONSTANT);
		}
	}
	
	@Test
	/* test method getEAUserForCreditials() for user existing in database 
	 * but with wrong password */
	public void test5() {
		String userName = "Molly@freeuni.edu.ge";
		String password = "FloPup";
		String role = "lecturer";
		String firstName = "Molly";
		String lastName = "Smith";
		String wrongPassword = "1234";
		assertFalse(password.equals(wrongPassword));
		boolean exists = false;
		if(accManagar.userExists(userName, password)){
			exists = true;
		}else{
			accManagar.addUser(userName, password, role, firstName, lastName);
		}
		
		OpResult<EAUser> res = accManagar.getEAUserForCreditials(userName, wrongPassword, null);
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertTrue(user == AccountManager.NO_USER_FOUND_CONSTANT);
		if(!exists){
			int userId = user.getUserID();
			accManagar.removeUserByID(userId);
		}
	}
	
	@Test
	/* method getEAUserForCreditials() for user Molly 
	 * test CaseSenitivity of userName 
	 * 
	 * test CaseSenitivity of password */
	public void test6() {
		String userName = "Molly@freeuni.edu.ge";
		String password = "FloPup";
		String role = "lecturer";
		String firstName = "Molly";
		String lastName = "Smith";
		boolean exists = false;
		if(accManagar.userExists(userName, password)){
			exists = true;
		}else{
			accManagar.addUser(userName, password, role, firstName, lastName);
		}
		OpResult<EAUser> res = accManagar.getEAUserForCreditials("MOLLY@freEUni.eDu.ge", password, null);
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertEquals(user.getFirstName(), firstName);
		assertEquals(user.getLastName(), lastName);
		assertEquals(user.getMail(), userName);
		assertEquals(EAUser.getRoleByString(role), EAUser.EAUserRole.LECTURER);
		if(!exists){
			int userId = user.getUserID();
			accManagar.removeUserByID(userId);
		}
	
	}
	
	@Test
	/* method getEAUserForCreditials() for user Molly 
	 * test addUser and removeUser */
	public void test7() {
		String userName = "noUser@freeuni.edu.ge";
		String password = "noPassword";
		String role = "lecturer";
		String firstName = "noFirstName";
		String lastName = "noLastName";
		boolean exists = false;
		if(accManagar.userExists(userName, password)){
			exists = true;
		}else{
			accManagar.addUser(userName, password, role, firstName, lastName);
		}
		
		OpResult<EAUser> res = accManagar.getEAUserForCreditials(userName, password, null);
		assertTrue(res.isSuccess());
		EAUser user = res.getOpResult();
		assertEquals(user.getFirstName(), firstName);
		assertEquals(user.getLastName(), lastName);
		assertEquals(user.getMail(), userName);
		assertEquals(EAUser.getRoleByString(role), EAUser.EAUserRole.LECTURER);
		
		if(!exists){
			int userId = user.getUserID();
			accManagar.removeUserByID(userId);
		}
	
	}
	
	
	

}
