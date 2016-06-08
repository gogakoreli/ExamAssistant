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

import helper.AccountManager;
import helper.ContextStartupListener;
import helper.DBConnector;
import helper.DBConnector.SqlQueryResult;
import helper.DBInfo;
import helper.ExamManager;
import helper.LogManager;
import models.EAUser;
import models.Exam;
import models.Student;

@WebServlet("/Student")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public StudentServlet() {
		super();
	}

	/*
	 * Check if user is loged in otherwise send him back to the login page then,
	 * extract student variable from the session and extract his latest exam
	 * from the database then, pass student and his exam to the jsp to draw
	 * according page
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		ServletContext context = request.getServletContext();
		AccountManager accountManager = (AccountManager) context
				.getAttribute(ContextStartupListener.ACCOUNT_MANEGER_ATTRIBUTE_NAME);
		ExamManager examManager = (ExamManager) context
				.getAttribute(ContextStartupListener.EXAM_MANEGER_ATTRIBUTE_NAME);
		Student student = getStudent(accountManager, session);
		if (student != null) {
			examManager.setExamForStudent(student);
			Exam exam = student.getExam();

			request.setAttribute("student", student);
			request.setAttribute("exam", exam);

			RequestDispatcher dispatch = request.getRequestDispatcher("Student.jsp");
			dispatch.forward(request, response);
		} else {
			response.sendRedirect("/ExamAssistant/Login");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/*
	 * get student which is passed to the servlet from the request
	 */
	public Student getStudent(AccountManager manager, HttpSession session) {
		Object currentUser = manager.getCurrentUser(session);
		Student result = currentUser instanceof Student ? (Student) currentUser : null;
		return result;
	}

}
