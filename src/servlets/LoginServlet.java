package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.EAUser;

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
		
	}
	
	private int checkUserCreditials(String userame, String password){
		return NO_USER_FOUND_ID;
	}
	
	private EAUser getEAUserById(int userId){
		return null;
	}
	
	private void loggedInStudent(){
		
	}
	
	private void loggedInLecturer(){
	
	}
	
	private void loggedInBoard(){
		 
	}

}
