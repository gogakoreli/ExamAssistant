package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import helper.AccountManager;
import helper.ContextStartupListener;
import helper.OpResult;
import models.EAUser;
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
		ServletContext context = request.getServletContext();
		AccountManager manager = (AccountManager) context
				.getAttribute(ContextStartupListener.ACCOUNT_MANEGER_ATTRIBUTE_NAME);
		HttpSession session = request.getSession();
		if (isUserLogedIn(manager, session)) {
			EAUser user = manager.getCurrentUser(request.getSession());
			loggedUser(request, response, user);
		} else {
			request.getRequestDispatcher("Login.jsp").forward(request, response);
		}
	}

	/*
	 * Simply checks which type of user is logged in and sends it to the correct
	 * servlet.
	 */
	private void loggedUser(HttpServletRequest request, HttpServletResponse response, EAUser user) throws IOException {
		if (user instanceof Student) {
			response.sendRedirect("/ExamAssistant/Student");
		} else if (user instanceof Lecturer) {
			response.sendRedirect("/ExamAssistant/Lecturer");
		} else if (user instanceof ExamBoard) {
			response.sendRedirect("/ExamAssistant/Board");
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
	 * Checks if the error happens while getting information from the base, if
	 * it's so sends user to the correct error page.
	 */
	private void checkUserCreditials(HttpServletRequest request, HttpServletResponse response, AccountManager manager,
			String userName, String password) throws ServletException, IOException {
		OpResult<EAUser> result = manager.getEAUserForCreditials(userName, password, request.getSession());

		RequestDispatcher rd = null;
		if (!result.isSuccess()) {
			rd = request.getRequestDispatcher("ErrorPage.jsp");
			rd.forward(request, response);
		} else {
			checkUser(result, manager, rd, request, response);
		}
	}

	/*
	 * Checks if user's name and password is valid and if it's calls the method
	 * which sends user by it's type to the correct servlet. Else sends it again
	 * to the Login to try log again.
	 */
	private void checkUser(OpResult<EAUser> result, AccountManager manager, RequestDispatcher rd,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EAUser user = result.getOpResult();
		if (user == AccountManager.NO_USER_FOUND_CONSTANT) {
			String errorString = "Either your user name or password is incorrect. Please try again.";
			request.setAttribute("errorString", errorString);
			rd = request.getRequestDispatcher("Login.jsp");
			rd.forward(request, response);
		} else {
			loggedInUser(request, response, user);
		}
	}

	/* Simply calls the correct method for the EAUser by it's type. */
	private void loggedInUser(HttpServletRequest request, HttpServletResponse response, EAUser user)
			throws IOException {
		HttpSession session = request.getSession();
		if (user instanceof Student) {
			loggedInStudent(request, response, user);
		} else if (user instanceof Lecturer) {
			loggedInLecturer(request, response, user);
		} else if (user instanceof ExamBoard) {
			loggedInBoard(request, response, user);
		}
	}

	/* Sets the student to the request and passes it to the StudentServlet. */
	private void loggedInStudent(HttpServletRequest request, HttpServletResponse response, EAUser user)
			throws IOException {
		// request.setAttribute("student", (Student)user);
		response.sendRedirect("/ExamAssistant/Student");
	}

	/* Sets the board to the request and passes it to the BoardServlet. */
	private void loggedInBoard(HttpServletRequest request, HttpServletResponse response, EAUser user)
			throws IOException {
		// request.setAttribute("board", (ExamBoard) user);
		response.sendRedirect("/ExamAssistant/Board");
	}

	/* Sets the lecturer to the request and passes it to the LecturerServlet. */
	private void loggedInLecturer(HttpServletRequest request, HttpServletResponse response, EAUser user)
			throws IOException {
		// request.setAttribute("lecturer", (Lecturer) user);
		response.sendRedirect("/ExamAssistant/Lecturer");
	}

	/**
	 * Check if user is loged in or not : take isLogedIn attribute from session
	 * and check if it is null or value is true/false
	 */
	public static boolean isUserLogedIn(AccountManager manager, HttpSession session) {
		boolean result = false;
		result = manager.getCurrentUser(session) == null ? false : true;
		return result;
	}

}
