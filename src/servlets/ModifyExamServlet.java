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

import org.eclipse.jdt.internal.compiler.lookup.ExtraCompilerModifiers;

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
@WebServlet("/ModifyExam")
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
			if (checkNewExam(request)) {
				request.setAttribute("newExam", "Create New Exam");
			}
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
			if (checkExamSavedButtonCliqued(request)) {
				EAUser user = checker.getUser();
				ExamManager manager = ExamManager.getExamManager(request.getSession());
				saveModifiedExam(request, manager, user);
				// TODO Auto-generated constructor stub
				// gadavushveb tavis sawyis gverdze ogond ak marto
				// lektoristvisaa
				response.sendRedirect("/ExamAssistant/Lecturer");
			}

		} else {
			checker.redirectToValidPage(response);
		}

	}

	private int getExamDuration(HttpServletRequest request) {
		if (request.getParameter("examDuration") != null) {
			return Integer.parseInt(request.getParameter("examDuration"));
		} else
			return Exam.DEFAULT_EXAM_DUARTION;

	}

	private String getExamResourceType(HttpServletRequest request) {
		if (request.getParameter("examType") != null) {
			return Exam.OPEN_BOOK;
		} else
			return Exam.EXAM_NAME_UNDEFINED;
	}

	private String getExamName(HttpServletRequest request) {
		if (request.getParameter("examName") != null) {
			return request.getParameter("examName");
		} else
			return Exam.EXAM_NAME_UNDEFINED;
	}

	private boolean checkExamSavedButtonCliqued(HttpServletRequest request) {
		return request.getParameter("saveExam") != null;
	}

	public static boolean checkNewExam(HttpServletRequest request) {
		return request.getParameter("newExam") != null || request.getSession().getAttribute("newExam") != null;
	}

	private void saveModifiedExam(HttpServletRequest request, ExamManager manager, EAUser user) {
		String examName = getExamName(request);
		String openBook = getExamResourceType(request);
		String[] subLecturers = null;
		File[] materials = null;
		int examDuration = getExamDuration(request);
		int numVariants = 1;
		String examType = getExamType(request);
		if (checkNewExam(request)) {
			manager.createNewExam(user.getUserID(), examName, openBook, subLecturers, materials, examDuration,
					numVariants, examType);
		} else {
			int examID = Integer.parseInt(request.getParameter("id"));
			String examStatus = getExamStatus(request);
			Exam exam = manager.modifyExam(examID, examName, openBook, subLecturers, materials, examDuration,
					numVariants, examType, examStatus);
		}
	}

	private String getExamStatus(HttpServletRequest request) {
		// TODO am unda shevcvalo examis statusi new aris processing to done
		return Exam.EXAM_STATUS_WAITING;
	}

	private String getExamType(HttpServletRequest request) {
		// TODO dasamatebelia checkBox ModifyExam.jsp rom achvenos statusi
		return Exam.EXAM_TYPE_FINAL;
	}

	@Override
	public boolean CheckInfo(EAUser user, HttpServletRequest request) {
		if (checkNewExam(request)) {
			return user instanceof Lecturer;
		}
		return true;
	}

}
