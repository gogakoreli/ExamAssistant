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
import models.Exam;
import models.Lecturer;

/**
 * Servlet implementation class CreateExamServlet
 */
@WebServlet("/ModifyExamServlet")
public class ModifyExamServlet extends HttpServlet {
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO sheamowme lektoria tu sagamocdo
		//tu lektoria da statusia new mashin createNewExam gamovizaxo
		
		RequestDispatcher dispatch = request.getRequestDispatcher("ModifyExam.jsp");
		dispatch.forward(request, response);
		
		String examName = request.getParameter("examName");
		
		
		String res = request.getParameter("examType");
		System.out.println(examName);
		
		
	}

}
