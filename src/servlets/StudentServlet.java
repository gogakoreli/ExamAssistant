package servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data_managers.AccountManager;
import data_managers.ExamManager;
import helper.DBConnector;
import helper.DBConnector.SqlQueryResult;
import listeners.ContextStartupListener;
import helper.DBInfo;
import helper.LogManager;
import models.EAUser;
import models.Exam;
import models.ExamMaterial;
import models.Student;

@WebServlet("/Student")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public StudentServlet() {
		super();
	}

	/**
	 * Check if user is loged in otherwise send him back to the login page then,
	 * extract student variable from the session and extract his latest exam
	 * from the database then, pass student and his exam to the jsp to draw
	 * according page
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		AccountManager accountManager = AccountManager.getAccountManager(session);
		ExamManager examManager = ExamManager.getExamManager(session);

		Student student = (Student) accountManager.getCurrentUser(session);
		if (student != null) {
			Exam exam = examManager.getExamForStudent(student);
			if (examManager.canStartExam(student, exam)) {
				request.setAttribute("student", student);
				request.setAttribute("exam", exam);

				RequestDispatcher dispatch = request.getRequestDispatcher("Student.jsp");
				dispatch.forward(request, response);
			} else {
				goToStartedExam(request, response, student, exam, examManager);
			}

		} else {
			response.sendRedirect("/ExamAssistant/Login");
		}
	}

	/**
	 * when start button is clicked user is redirected here; So exam is started
	 * and updated in the database and student starts the exam. timer starts.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		AccountManager accountManager = AccountManager.getAccountManager(session);
		ExamManager examManager = ExamManager.getExamManager(session);
		Student student = (Student) accountManager.getCurrentUser(session);

		if (student != null) {
			Exam exam = (Exam) session.getAttribute("exam");
			session.removeAttribute("exam");

			goToStartedExam(request, response, student, exam, examManager);
		} else {
			response.sendRedirect("/ExamAssistant/Login");
		}
	}

	private void goToStartedExam(HttpServletRequest request, HttpServletResponse response, Student student, Exam exam,
			ExamManager examManager) {
		if (exam != null) {
			request.setAttribute("exam", exam);
			request.setAttribute("student", student);
			examManager.startExam(student, exam);
			ArrayList<ExamMaterial> materials = examManager.getExamMaterialsForStudent(student, exam);
			request.setAttribute("materials", materials);
			RequestDispatcher dispatch = request.getRequestDispatcher("Exam.jsp");
			try {
				dispatch.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
