package tests;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helper.AccountManager;
import helper.LogManager;
import models.EAUser;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/test")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		/* for testing purposes writes its url, session id and user logged in */
		
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: "
				+ request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1) + "   ");
		String message = "paramether test = " + request.getParameter("test") + " session ID = " + request.getSession().getId();
		printTestMessage(message, response);
		
		String userInfo = "User at session = ";
		EAUser user = AccountManager.getAccountManager(request.getSession()).getCurrentUser(request.getSession());
		if (user != null)
			userInfo += user.toString();
		printTestMessage(userInfo, response);
	}
	
	/* prints given test message on responce and in log*/
	private void printTestMessage(String message, HttpServletResponse response) throws IOException{
		response.getWriter().append(message);
		LogManager.logInfoMessage(message);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
