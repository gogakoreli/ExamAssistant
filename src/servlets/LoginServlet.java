package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.EAUser;
import helper.AccountManager;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet
{
	private static int NO_USER_FOUND_ID = -1;
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public LoginServlet()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getOutputStream().println("<html>  <body> Mushaobs </body> </html>"); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("aaaaaa");
//		ServletContext ctx = request.getServletContext();
//		AccountManager manager = (AccountManager) ctx.getAttribute("Manager");
//		//Gets information from the user.
//		String userName = request.getParameter("username"); 
//		String password = request.getParameter("password");
//		request.setAttribute("username", userName); // Sets attribute in the request to see in later in the jsp files.
		//EAUser currentUser = manager.checkPassword(userName, password);
		
		//if (currentUser == null) { // Checks if user's password is correct.
//		} else {
//			//If entered information is incorrect webpage shows up try again window.
//			rd = request.getRequestDispatcher("StartExam.jsp");
//
//		}
		
		RequestDispatcher rd = request.getRequestDispatcher("UserWelcome.jsp");
		rd.forward(request,response); 
		
	}
	
	
	
	private int checkUserCreditials(String userame, String password){
		return NO_USER_FOUND_ID;
	}
	
	private EAUser getEAUserById(int userId){
		return null;
	}
	
	private void loggedInStudent(RequestDispatcher rd){
		
	}
	
	private void loggedInLecturer(){
	
	}
	
	private void loggedInBoard(){
		 
	}

}
