package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import data_managers.AccountManager;
import helper.LogManager;
import helper.OpResult;
import helper.SecurityChecker;
import models.Lecturer;

/**
 * main class for running operations from outside project and returning its
 * result.
 * 
 * Mainly used to Run Javascript requests and return JSON resultset to it .
 */
@WebServlet("/getjsresult")
public class JsOperationsRunner extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/* name of opeartion to identify request of list of lecturers */
	public static final String GET_LECTURERS_SUGGESTIONS_OP = "getlecsuggestions";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public JsOperationsRunner() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String operation = request.getParameter("opname");
		if (operation == null) return;
		if (operation.equals(GET_LECTURERS_SUGGESTIONS_OP)) {
			returnLecturersSuggestions(request, response);
		} else {
			response.getWriter().append("Served at: ").append(request.getContextPath());
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/* writes list of lecturers suggestions to responce */
	private void returnLecturersSuggestions(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		SecurityChecker sChecker = new SecurityChecker(request, null);
		if (!sChecker.checkExtraPermissions(SecurityChecker.LECTURER_SUGGESTIONS_PERMISSION))
			return;

		//LogManager.logInfoMessage("Securty passed");
		
		// getting list of lecturers
		String startPattern = request.getParameter("st_pattern");
		OpResult<List<Lecturer>> opResult = AccountManager.getAccountManager(request.getSession())
				.getLecturersByNameStart(startPattern);

		if (opResult.isSuccess()) {
			String json = new Gson().toJson(opResult.getOpResult());
			response.getWriter().write(json);
		} else {
			LogManager.logErrorException(opResult.getErrorId(), opResult.getErrorMsg(),
					new Exception(opResult.getErrorMsg()));
		}
	}
}
