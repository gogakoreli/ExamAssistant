package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import helper.AccountManager;
import helper.ExamManager;
import helper.OpResult;
import models.EAUser;
import models.Exam;
import models.Student;

public class ExamManagerTester {
	ExamManager exManager;
	

	@Before
	public void setUp() throws Exception {
		exManager = new ExamManager();
	}
	
	
	/*Test getExamForStudent.*/
	@Test
	public void test1() {
		AccountManager acMan = new AccountManager();
		OpResult<EAUser> res = acMan.getEAUserForCreditials("ammeli14@freeuni.edu.ge", "amimelia", null);
		EAUser user = res.getOpResult();
		
		Exam ex =  exManager.getExamForStudent((Student)user);
		
		
	}

}
