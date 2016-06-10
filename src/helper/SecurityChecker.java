package helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import interfaces.ISecure;
import models.Admin;
import models.EAUser;
import models.ExamBoard;
import models.Lecturer;
import models.Student;

/**
 * this is main class that checks for permission for different user on different
 * pages
 */
public class SecurityChecker {

	EAUser user = null;// takes given user for page
	ISecure SecurePage = null;// page which wants to be secured
	HttpServletRequest request = null;

	public SecurityChecker(HttpServletRequest request, ISecure page) {
		this.SecurePage = page;
		this.user = getUserFromRequest(request);
		this.request = request;
	}

	/**
	 * returns user for whom page was opened if it has permissions for given
	 * page
	 */
	public EAUser getUser() {
		return user;
	}

	/*
	 * gets user from request from accountManager asks Account Manager about it
	 */
	private EAUser getUserFromRequest(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AccountManager accountManager = AccountManager.getAccountManager(session);
		return accountManager.getCurrentUser(session);
	}

	/**
	 * checks if given @request is valid.
	 */
	public boolean CheckPermissions() {
		if (user == null)
			return false;
		String url = request.getRequestURI();
		url = url.substring(url.lastIndexOf("/") + 1);
		List<String> listToReadPermissions = getPermissionsForUser();
		boolean securePageChecked = true;
		if (SecurePage != null)
			securePageChecked = SecurePage.CheckInfo(user, request);
		return listToReadPermissions.contains(url) && (securePageChecked);
	}

	/* returns list of permissions for user */
	private List<String> getPermissionsForUser() {
		if (user instanceof Lecturer)
			return permissionsForLecturer;
		if (user instanceof Student)
			return permissionsForStudent;
		if (user instanceof ExamBoard)
			return permissionsForBoard;
		if (user instanceof Admin)
			return permissionsForAdmin;
		return null;
	}

	/**
	 * redirects user to valid page is its logedi then redirects to error page
	 * else to login page
	 */
	public void redirectToValidPage(HttpServletResponse response) {
		LogManager.logInfoMessage("Security Bridge! redirectiong to error message !");
		try {

			if (user == null) {
				response.sendRedirect("/ExamAssistant/Login");
			} else {
				response.sendRedirect("/ExamAssistant/ErrorPage.jsp");
			}
		} catch (IOException e) {
			LogManager.logErrorException("Error redirectiong to Error Page", e);
		}

	}

	/***********************************************/

	/****** static containers of permissions *****/
	/***********************************************/
	private static List<String> permissionsForStudent;
	private static List<String> permissionsForLecturer;
	private static List<String> permissionsForBoard;
	private static List<String> permissionsForAdmin;

	/* permissions for student */
	public static void initPermissionsForStudent() {
		permissionsForStudent = new ArrayList<String>();
		permissionsForStudent.add("Student");
		permissionsForStudent.add("Student.jsp");
	}

	/* permissions for student */
	public static void initPermissionsForLeqturer() {
		permissionsForLecturer = new ArrayList<String>();
		permissionsForLecturer.add("Lecturer");
		permissionsForLecturer.add("Lecturer.jsp");
		permissionsForLecturer.add("ModifyExam");
		permissionsForLecturer.add("ModifyExam.jsp");

	}

	/* permissions for student */
	public static void initPermissionsForBoard() {
		permissionsForBoard = new ArrayList<String>();
		permissionsForBoard.add("Board");
		permissionsForBoard.add("Board.jsp");
		permissionsForBoard.add("ModifyExam");
		permissionsForBoard.add("ModifyExam.jsp");
	}

	/* permissions for student */
	public static void initPermissionsForAdmin() {
		permissionsForAdmin = new ArrayList<String>();
		permissionsForAdmin.add("");
		permissionsForAdmin.add("");
	}

}
