package servlets;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;

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
import helper.ExamManager;
import helper.LogManager;
import interfaces.ISecure;
import helper.SecurityChecker;
import models.EAUser;
import models.Exam;
import models.Lecturer;

/**
 * Servlet implementation class CreateExamServlet
 */
@WebServlet("/ModifyExamServlet")
public class ModifyExamServlet extends HttpServlet implements ISecure {
	private static final long serialVersionUID = 1L;
	public static final String NEW_EXAM_STATUS = "newexam";

	public static final String MODIFY_EXAM_STATUS = "modifyexam";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ModifyExamServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SecurityChecker checker = new SecurityChecker(request, null);
		if (checker.CheckPermissions()) {
			int examId = Integer.parseInt(request.getParameter("id"));
			ExamManager manager = ExamManager.getExamManager(request.getSession());
			Exam exam = manager.getExamByExamId(examId);
			request.setAttribute("exam", exam);
			RequestDispatcher dispatch = request.getRequestDispatcher("ModifyExam.jsp");
			dispatch.forward(request, response);
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
			EAUser user = checker.getUser();
			if (checkNewExam(request)) {
				ExamManager manager = ExamManager.getExamManager(request.getSession());
				int examId = manager.createNewExam((Lecturer) user);
				response.sendRedirect("/ExamAssistant/ModifyExamServlet?id=" + examId);
			}
			
			 if (checkExamSavedButtonCliqued(request)){
				 saveModifiedExam(request);
				// TODO Auto-generated constructor stub
				//gadavushveb tavis sawyis gverdze ogond ak marto lektoristvisaa
				 response.sendRedirect("/ExamAssistant/LecturerServlet");
			 }
			 
		} else {
			checker.redirectToValidPage(response);
		}

	}

	private boolean checkExamSavedButtonCliqued(HttpServletRequest request) {
		return request.getAttribute("saveExam") != null;
	}

	private boolean checkNewExam(HttpServletRequest request) {
		return request.getAttribute("status") != null && 
				request.getAttribute("status").equals(NEW_EXAM_STATUS);
	}

	private void saveModifiedExam(HttpServletRequest request) {
		// TODO save exam

	}

	@Override
	public boolean CheckInfo(EAUser user, HttpServletRequest request) {
		if (checkNewExam(request)) {
			return user instanceof Lecturer;
		}
		return true;
	}

}
