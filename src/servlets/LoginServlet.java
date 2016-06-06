package servlets;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hamcrest.core.IsInstanceOf;

import helper.AccountManager;
import helper.DBConnector;
import helper.LogManager;
import helper.OpResult;
import helper.DBConnector.SqlQueryResult;
import models.EAUser;
import models.EAUser.EAUserRole;
import models.Exam;
import models.ExamBoard;
import models.Lecturer;
import models.Student;

/**
 * Servlet implementation class LogInServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String ACCOUNT_MANEGER_ATTRIBUTE_NAME = "ContextStartupListener.AccountManagerAttribute";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (isUserLogedIn(request)) {
			ServletContext ctx = request.getServletContext();
			AccountManager manager = (AccountManager) ctx.getAttribute(ACCOUNT_MANEGER_ATTRIBUTE_NAME);
			EAUser user = manager.getCurrentUser();

			if (user instanceof Student) {
				response.sendRedirect("/ExamAssistant/Student"); 
			} else if (user instanceof Lecturer) {
				response.sendRedirect("/ExamAssistant/Lecturer"); 
			} else if (user  instanceof ExamBoard) {
				response.sendRedirect("/ExamAssistant/Board"); 
			} 
		} else {
			request.getRequestDispatcher("Login.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext ctx = request.getServletContext();
		AccountManager manager = (AccountManager) ctx.getAttribute(ACCOUNT_MANEGER_ATTRIBUTE_NAME);

		String userName = request.getParameter("username");
		String password = request.getParameter("password");

		// Checks if the user's name and password is valid.
		checkUserCreditials(request, response, manager, userName, password);
	}

	/*
	 * Checks if the user is valid and if it's so by its type sends it to the
	 * correct servlet. If user isn't valid sends him to the ErrorPage.
	 */
	private void checkUserCreditials(HttpServletRequest request, HttpServletResponse response, AccountManager manager,
			String userName, String password) throws ServletException, IOException {
		OpResult<EAUser> result = manager.getEAUserForCreditials(userName, password);
		
		RequestDispatcher rd;
		EAUser user = null;
		if (!result.isSuccess()) {
			rd = request.getRequestDispatcher("ErrorPage.jsp");
			rd.forward(request, response);
		} else {
			user = result.getOpResult();
			if (user == manager.NO_USER_FOUND_CONSTANT) {
				String errorString = "Either your user name or password is incorrect. Please try again.";
				request.setAttribute("errorString", errorString);
				rd = request.getRequestDispatcher("Login.jsp");
				rd.forward(request, response);
			} else {
				HttpSession session = request.getSession();
				session.setAttribute("isLogedIn", true);
				if (user instanceof Student) {
					loggedInStudent(request, response, user);
				} else if (user instanceof Lecturer) {
					loggedInLecturer(request, response, user);
				} else if (user  instanceof ExamBoard) {
					loggedInBoard(request, response, user);
				} 
			}
		}	
	}
	

	/* Sets the student to the request and passes it to the StudentServlet. */
	private void loggedInStudent(HttpServletRequest request, HttpServletResponse response, EAUser user)
			throws IOException {
		request.setAttribute("student", user);
		response.sendRedirect("/ExamAssistant/Student");
	}

	/* Sets the board to the request and passes it to the BoardServlet. */
	private void loggedInBoard(HttpServletRequest request, HttpServletResponse response, EAUser user)
			throws IOException {
		request.setAttribute("board", (ExamBoard) user);
		response.sendRedirect("/ExamAssistant/Board");
	}

	/* Sets the lecturer to the request and passes it to the LecturerServlet. */
	private void loggedInLecturer(HttpServletRequest request, HttpServletResponse response, EAUser user)
			throws IOException {
		request.setAttribute("lecturer", (Lecturer) user);
		response.sendRedirect("/ExamAssistant/Lecturer");
	}

	/*
	 * Check if user is loged in or not : take isLogedIn attribute from session
	 * and check if it is null or value is true/false
	 */
	public static boolean isUserLogedIn(HttpServletRequest request) {
		boolean result = false;
		HttpSession session = request.getSession();
		Object isLogedIn = session.getAttribute("isLogedIn");
		result = isLogedIn != null ? (boolean) isLogedIn : result;
		return result;
	}

}
