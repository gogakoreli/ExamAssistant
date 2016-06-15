package servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import helper.LogManager;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/FileUploadServlet")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUploadServlet() {
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
		// TODO Auto-generated method stub
		if (saveVariantsFileButtonClicked(request)) {
			Part filePart=null;
			try{
				filePart = request.getPart("uploadFile"); // Retrieves <input type="file" name="file">
				String fileName = filePart.getSubmittedFileName();
			    InputStream fileContent = filePart.getInputStream();
			    System.out.println(fileName);
			} catch (Exception e){
				LogManager.logErrorException("Error Uploading file", e);
			}
		}
	}
		
		private boolean saveVariantsFileButtonClicked(HttpServletRequest request) {
			request.getParameter("uploadFile");
			request.getParameter("file");
			return request.getParameter("saveFile") != null;
		}

}
