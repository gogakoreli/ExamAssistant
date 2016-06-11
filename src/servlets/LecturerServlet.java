package servlets;

import java.io.IOException;
import java.net.ResponseCache;
import java.util.ArrayList;

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
import helper.ExamManager;
import helper.LogManager;
import helper.SecurityChecker;
import models.EAUser;
import models.Exam;
import models.Lecturer;
import models.Student;

/**
 * Servlet implementation class LecturerServlet
 */
@WebServlet("/Lecturer")
public class LecturerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LecturerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//sdoPost(request, response);
		HttpSession session = request.getSession();
		ExamManager examManager = ExamManager.getExamManager(session);
		SecurityChecker checker = new SecurityChecker(request, null);
		if (checker.CheckPermissions()) {
			Lecturer lecturer = (Lecturer)checker.getUser();
			request.setAttribute("lecturer", lecturer);
			
			ArrayList<Exam> exams = examManager.getAllExamsForLecturer(lecturer);
			request.setAttribute("exams", exams);
			
			RequestDispatcher dispatch = request.getRequestDispatcher("Lecturer.jsp");
			dispatch.forward(request, response);
		}else{
			checker.redirectToValidPage(response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SecurityChecker checker = new SecurityChecker(request, null);
		if (checker.CheckPermissions()) {
			if (request.getParameter("newExam") != null) {// new exam
				newExamClicked(request, response);
			}
		} else {
			checker.redirectToValidPage(response);
		}

	}

	private void newExamClicked(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setAttribute("status", ModifyExamServlet.NEW_EXAM_STATUS);
		response.sendRedirect("/ExamAssistant/ModifyExam");
	}

}
