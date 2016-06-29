package servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data_managers.AccountManager;
import helper.LogManager;
import listeners.ContextStartupListener;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LogoutServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext context = request.getServletContext();
		AccountManager manager = (AccountManager) context
				.getAttribute(ContextStartupListener.ACCOUNT_MANEGER_ATTRIBUTE_NAME);
		manager.removeCurrentUser(request.getSession());
		HttpSession session = request.getSession(false);
		//LogManager.logInfoMessage(session.getId());
		if (session != null)
			session.invalidate();
		request.getSession(true);
		//LogManager.logInfoMessage(session.getId());
		resetCookies(request, response);
		response.sendRedirect("/ExamAssistant/Login");
	}

	private void resetCookies(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Cookie[] cookies = request.getCookies();
	    for (Cookie cookie : cookies) {
	        cookie.setMaxAge(0);
	        cookie.setValue(null);
	        cookie.setPath("/");
	        response.addCookie(cookie);
	    }
	    
	    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
	    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
	    response.setDateHeader("Expires", 0); // Proxies.
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
