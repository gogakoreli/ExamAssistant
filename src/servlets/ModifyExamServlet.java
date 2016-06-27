package servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import data_managers.AccountManager;
import data_managers.ExamManager;
import helper.DBConnector;
import helper.DBConnector.SqlQueryResult;
import helper.LogManager;
import helper.OpResult;
import interfaces.ISecure;
import listeners.ContextStartupListener;
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
	private static final int MAX_UPLOAD_FILE_SIZE = 4 * 1024;
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
		/* edit of exam requested */
		SecurityChecker checker = new SecurityChecker(request, this);
		if (checker.CheckPermissions()) {
			Exam examToEdit = getEditExam(request);
			request.setAttribute("exam", examToEdit);
			RequestDispatcher dispatch = request.getRequestDispatcher("ModifyExam.jsp");
			dispatch.forward(request, response);
		} else {
			checker.redirectToValidPage(response);
		}
	}

	// TODO needs testing for security

	@Override
	/** checks if user has permission to access exam or create new one */
	public boolean CheckInfo(EAUser user, HttpServletRequest request) {
		if (isNewExamRequest(request)) {
			// ensure its lecturer who creates new exam
			return (user instanceof Lecturer);
		}

		if (canAccessExam(user, request)) {
			return true;
		}

		return false;
	}

	/* checks if its request for new exam or not */
	private boolean isNewExamRequest(HttpServletRequest request) {
		return (request.getParameter("newExam") != null);
	}

	/* retrives exam id from request we are working on */
	private int getExamIdFromRequest(HttpServletRequest request) {
		int examID = ExamManager.NO_EXAM_ID;
		Object param = request.getParameter("examID");
		try {
			examID = Integer.parseInt((String) param);
		} catch (Exception ignored) {
		}
		return examID;
	}

	/* checks if user @user has permission to view exam @examId */
	private boolean canAccessExam(EAUser user, HttpServletRequest request) {
		int examId = getExamIdFromRequest(request);
		return ExamManager.getExamManager(request.getSession()).CanUserAccessExam(user, examId);
	}

	/*
	 * Retrives and returns Exam user wants to edit
	 * 
	 * @param request - we take exam id from request
	 * 
	 * @return Exam
	 * 
	 * returns null if there was error reading examId from request or error
	 * happened while reading exam info from exam manager
	 */
	private Exam getEditExam(HttpServletRequest request) {
		if (isNewExamRequest(request))
			return ExamManager.EMPTY_EXAM;
		ExamManager examManager = ExamManager.getExamManager(request.getSession());
		Exam examToEdit = examManager.getExamByExamId(getExamIdFromRequest(request));

		setCreatorNameForExam(examToEdit, request);
		setSubLecturersForExam(examToEdit, request);
		return examToEdit;
	}

	/* sets sub lecturers for exam */
	private void setSubLecturersForExam(Exam examToEdit, HttpServletRequest request) {
		ExamManager examManager = ExamManager.getExamManager(request.getSession());
		List<Lecturer> subLecs = examManager.downloadSubLecturers(examToEdit);
		examToEdit.setSubLecturers(subLecs);
	}

	/* sets creator name for given exam */
	private void setCreatorNameForExam(Exam examToEdit, HttpServletRequest request) {
		OpResult<EAUser> creatorResult = AccountManager.getAccountManager(request.getSession())
				.getUserById(examToEdit.getCreatorId());
		if (creatorResult.isSuccess()) {
			EAUser examCreator = creatorResult.getOpResult();
			examToEdit.setCreator(examCreator);
		}
	}
	
	/* handles file uploads saves them ftp server and on writes url in db */
	private void handleFileUplaods(HttpServletRequest request){
		  // Check that we have a file upload request
	      boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	      int maxFileSize = 50 * 1024 * 1024;
	      int maxMemSize = 4 * 1024;
	      String  filePath = 
	              getServletContext().getInitParameter("file-upload"); 
	      
	      DiskFileItemFactory factory = new DiskFileItemFactory();
	      // maximum size that will be stored in memory
	      factory.setSizeThreshold(maxFileSize);
	      // Location to save data that is larger than maxMemSize.
	      factory.setRepository(new File("c:\\temp"));

	      // Create a new file upload handler
	      ServletFileUpload upload = new ServletFileUpload(factory);
	      // maximum file size to be uploaded.
	      upload.setSizeMax( maxFileSize );

	      try{ 
	      // Parse the request to get file items.
	      List<FileItem> fileItems = upload.parseRequest(request);
		
	      // Process the uploaded file items
	      Iterator<FileItem> i = fileItems.iterator();
	      File file ;
	      while ( i.hasNext () ) 
	      {
	         FileItem fi = (FileItem)i.next();
	         if ( !fi.isFormField () )	
	         {
	            // Get the uploaded file parameters
	            String fieldName = fi.getFieldName();
	            String fileName = fi.getName();
	            String contentType = fi.getContentType();
	            boolean isInMemory = fi.isInMemory();
	            long sizeInBytes = fi.getSize();
	            // Write the file
	            if( fileName.lastIndexOf("\\") >= 0 ){
	               file = new File( filePath + 
	               fileName.substring( fileName.lastIndexOf("\\"))) ;
	            }else{
	               file = new File( filePath + 
	               fileName.substring(fileName.lastIndexOf("\\")+1)) ;
	            }
	            fi.write( file ) ;
	         }
	      }
	   
	   }catch(Exception ex) {
	       System.out.println(ex);
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
			
			handleFileUplaods(request);
			if (true) return;//testing purpose 
			if (checkExamSavedButtonCliqued(request)) {
				EAUser user = checker.getUser();
				ExamManager manager = ExamManager.getExamManager(request.getSession());
				saveModifiedExam(request, manager, user);
				// TODO Auto-generated constructor stub
				// gadavushveb tavis sawyis gverdze ogond ak marto
				// lektoristvisaa
				response.sendRedirect("/ExamAssistant/Lecturer");
			} else if (saveFileButtonClicked(request)) {
				Part filePart = null;
				try {
					filePart = request.getPart("uploadFile"); // Retrieves
																// <input
																// type="file"
																// name="file">
					String fileName = filePart.getSubmittedFileName();
					InputStream fileContent = filePart.getInputStream();
					System.out.println(fileName);
				} catch (Exception e) {
					LogManager.logErrorException("Error Uploading file", e);
				}
			}

		} else {
			checker.redirectToValidPage(response);
		}

	}

	private boolean saveFileButtonClicked(HttpServletRequest request) {
		request.getParameter("uploadFile");
		return request.getParameter("saveFile") != null;
	}

	private int getExamDuration(HttpServletRequest request) {
		if (request.getParameter("examDuration") != null) {
			try {
				return Integer.parseInt(request.getParameter("examDuration"));
			} catch (NumberFormatException e) {
				return Exam.DEFAULT_EXAM_DUARTION;
			}
		} else
			return Exam.DEFAULT_EXAM_DUARTION;
	}

	private String getExamResourceType(HttpServletRequest request) {
		if (request.getParameter("examStatus") != null) {
			return Exam.OPEN_BOOK;
		} else
			return Exam.CLOSED_BOOK;
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
		if (request.getParameter("examType").equals("Final")) {
			return Exam.EXAM_TYPE_FINAL;
		} else if (request.getParameter("examType").equals("Midterm")) {
			return Exam.EXAM_TYPE_MIDTERM;
		} else
			return Exam.EXAM_TYPE_QUIZZ;
	}

}
