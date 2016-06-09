package servlets;

import java.io.IOException;
import java.sql.SQLException;
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
import models.Student;

/**
 * Servlet implementation class BoardServlet
 */
@WebServlet("/Board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ServletContext context = request.getServletContext();
		AccountManager manager = (AccountManager) context.getAttribute(ContextStartupListener.ACCOUNT_MANEGER_ATTRIBUTE_NAME);

		if (LoginServlet.isUserLogedIn(manager, session)) {
			RequestDispatcher dispatch = request.getRequestDispatcher("Board.jsp");
			dispatch.forward(request, response);
		} else {
			response.sendRedirect("/ExamAssistant/Login");
		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String butt = request.getParameter("but");
		HttpSession session = request.getSession();
		ExamManager examManager = ExamManager.getExamManager(session);
		RequestDispatcher rd = null;
		if (butt.equals("Exam List")) {
			System.out.println("aaaaaaaaaaaaaaa");
			ArrayList<Exam> exams = null;
			try {
				exams = examManager.getAllExamsForBoard();
			} catch (SQLException e) {				
				e.printStackTrace();
			}
			request.setAttribute("exams", exams);
			rd = request.getRequestDispatcher("Board.jsp");
			rd.forward(request, response);
			
		} else if (butt.equals("Create Exam")) {
			System.out.println("aaaaaaaaaaaaaaa");

			response.sendRedirect("/ExamAssistant/Board");
		} 
	}

}
