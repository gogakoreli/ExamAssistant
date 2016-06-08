package servlets;

import java.io.IOException;
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ServletContext context = request.getServletContext();
		AccountManager accountManager = (AccountManager) context
				.getAttribute(ContextStartupListener.ACCOUNT_MANEGER_ATTRIBUTE_NAME);
		ExamManager examManager = (ExamManager) context
				.getAttribute(ContextStartupListener.EXAM_MANEGER_ATTRIBUTE_NAME);
		Lecturer lecturer = getLecturer(accountManager, session);
		if (lecturer != null) {
			ArrayList<Exam> allExams = examManager.getAllExamsForLecturer(lecturer);

			request.setAttribute("lecturer", lecturer);
			request.setAttribute("exams", allExams);

			RequestDispatcher dispatch = request.getRequestDispatcher("Lecturer.jsp");
			dispatch.forward(request, response);
		} else {
			response.sendRedirect("/ExamAssistant/Login");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private Lecturer getLecturer(AccountManager manager, HttpSession session) {
		Object currentUser = manager.getCurrentUser(session);
		Lecturer result = currentUser instanceof Lecturer ? (Lecturer) currentUser : null;
		return result;
	}

}
