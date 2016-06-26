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

import data_managers.AccountManager;
import listeners.ContextStartupListener;
import data_managers.ExamManager;
import helper.LogManager;
import helper.SecurityChecker;
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
		LogManager.logInfoMessage("Board asked");
		SecurityChecker sChecker = new SecurityChecker(request, null);
		if (sChecker.CheckPermissions()){
			LogManager.logInfoMessage("Board validated");
			sChecker.getUser();
			RequestDispatcher dispatch = request.getRequestDispatcher("Board.jsp");
			dispatch.forward(request, response);
		}else{
			sChecker.redirectToValidPage(response);
		}
		
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String butt = request.getParameter("but");
		HttpSession session = request.getSession();
		ExamManager examManager = ExamManager.getExamManager(session);
		if (butt.equals("Exam List")) {
			showList(examManager, request, response);
		} 
	}
	
	
	/**
	 * Sets the ArrayList<Exam> to the request to display the list of the exams in the board jsp. 
	 * This happens while entering the button Exam List.
	 * @param examManager
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showList(ExamManager examManager, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;
		ArrayList<Exam> exams = null;
		exams = examManager.getAllExamsForBoard();
		request.setAttribute("exams", exams);
		rd = request.getRequestDispatcher("Board.jsp");
		rd.forward(request, response);
	}

}
