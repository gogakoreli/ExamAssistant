package servlets;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import helper.DBConnector;
import helper.DBConnector.SqlQueryResult;
import helper.DBInfo;
import helper.LogManager;
import models.Exam;
import models.Student;

@WebServlet("/Student")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public StudentServlet() {
		super();
	}

	/*
	 * Check if user is loged in otherwise send him back to the login page Then,
	 * extract student variable from the session and extract his latest exam
	 * from the database Then, pass student and his exam to the jsp to draw
	 * according page
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (LoginServlet.isUserLogedIn(request)) {
			// Student student = getStudent(request);
			// Exam exam = getExamForStudent(student);
			//
			// request.setAttribute("student", student);
			// request.setAttribute("exam", exam);

			RequestDispatcher dispatch = request.getRequestDispatcher("Student.jsp");
			dispatch.forward(request, response);
		} else {
			response.sendRedirect("/ExamAssistant/Login");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// TODO: Not yet defined
	}

	/*
	 * get student which is passed to the servlet from the request
	 */
	public Student getStudent(HttpServletRequest request) {
		Student result = null;
		HttpSession session = request.getSession();
		Object student = session.getAttribute("student");
		result = student != null ? (Student) student : result;
		return result;
	}

	/*
	 * get exam for the student according to the query: take latest exam ordered
	 * by start time ascending and then pass the queryResult to the exam
	 * constructor to retrieve exam
	 */
	public Exam getExamForStudent(Student student) {
		Exam result = null;
		String getExamQuery = "SELECT e.* FROM user as u LEFT JOIN "
				+ "userexam as ue on ue.UserID = u.UserID LEFT JOIN "
				+ "exam as e on ue.ExamID = e.ExamID WHERE u.UserID = " + student.getUserID()
				+ " ORDER BY e.StartTime asc LIMIT 1";
		DBConnector connector = new DBConnector();
		SqlQueryResult queryResult = connector.getQueryResult(getExamQuery);
		connector.dispose();
		if (queryResult != null && queryResult.isSuccess()) {
			ResultSet rs = queryResult.getResultSet();
			result = new Exam(rs);
		} else {
			LogManager.logInfoMessage("QueryResult is null OR " + queryResult.getErrorMsg());
		}
		return result;
	}

}
