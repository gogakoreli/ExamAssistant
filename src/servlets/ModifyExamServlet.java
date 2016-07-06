package servlets;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import models.ExamMaterial;
import models.Lecturer;
import models.SecureExam;

/**
 * Servlet implementation class CreateExamServlet
 */
@WebServlet("/ModifyExam")
public class ModifyExamServlet extends HttpServlet implements ISecure {
	private static final long serialVersionUID = 1L;

	public static final String EXAM_ID_PARAM_NAME = "examID";
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
			fillExamWithAditionalInfo(examToEdit, request);
			request.setAttribute("exam", examToEdit);
			request.setAttribute("user", checker.getUser());
			RequestDispatcher dispatch = request.getRequestDispatcher("ModifyExam.jsp");
			dispatch.forward(request, response);
		} else {
			checker.redirectToValidPage(response);
		}
	}

	// TODO needs testing for security

	/*
	 * adds aditional info to exam which we wont to display like sublecturers or
	 * creator info
	 */
	private void fillExamWithAditionalInfo(Exam examToEdit, HttpServletRequest request) {
		if (examToEdit.isEmptyExam())
			return;
		setCreatorNameForExam(examToEdit, request);
		setSubLecturersForExam(examToEdit, request);
		setMaterialsForExam(examToEdit, request);
	}

	@Override
	/**
	 * checks if user has permission to access exam or create new one. if
	 * CheckInfo returns true this means Exam was detected which needs to be
	 * created or edited
	 */
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

		boolean isNewExamFromGet = (request.getParameter("newExam") != null);

		int examID = getExamIdFromRequest(request);
		boolean isNewExamFromPost = (examID == ExamManager.NEW_EXAM_ID);

		return isNewExamFromGet || isNewExamFromPost;
	}

	/*
	 * retrives exam id from request we are working on if error occured while
	 * retriving NO_EXAM_ID is returned
	 */
	private int getExamIdFromRequest(HttpServletRequest request) {
		String examIdstr = getParametherFromRequest(request, EXAM_ID_PARAM_NAME, "" + ExamManager.NO_EXAM_ID);
		int ExamID = getIntFromString(examIdstr, ExamManager.NO_EXAM_ID);
		return ExamID;
	}

	/*
	 * gets integer from string if error happened during parsing defVal is
	 * returned
	 */
	private int getIntFromString(String toParse, int defVal) {
		try {
			return Integer.parseInt(toParse);
		} catch (Exception e) {
			return defVal;
		}
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
	 * 
	 * returns new instance of Exam with id EMPTY_EXAM_ID if is new exam
	 */
	private Exam getEditExam(HttpServletRequest request) {
		if (isNewExamRequest(request))
			return new Exam(ExamManager.NEW_EXAM_ID);
		ExamManager examManager = ExamManager.getExamManager(request.getSession());
		Exam examToEdit = examManager.getExamByExamId(getExamIdFromRequest(request));
		if (examToEdit == ExamManager.WRONG_EXAM)
			return null;
		return examToEdit;
	}

	/* sets sub lecturers for exam */
	private void setSubLecturersForExam(Exam examToEdit, HttpServletRequest request) {
		ExamManager examManager = ExamManager.getExamManager(request.getSession());
		List<Lecturer> subLecs = examManager.downloadSubLecturers(examToEdit);
		examToEdit.setSubLecturers(subLecs);
	}

	/* sets sub lecturers for exam */
	private void setMaterialsForExam(Exam examToEdit, HttpServletRequest request) {
		ExamManager examManager = ExamManager.getExamManager(request.getSession());
		List<ExamMaterial> materialsList = examManager.downloadMaterialsList(examToEdit);
		List<ExamMaterial> variantsList = examManager.downloadVariantsList(examToEdit);
		List<ExamMaterial> sudetnsListURL = examManager.downloadStudentsList(examToEdit);
		examToEdit.setMaterialsList(materialsList);
		examToEdit.setMaterialVariantsList(variantsList);
		examToEdit.setStudentsListForExam(sudetnsListURL);
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		SecurityChecker checker = new SecurityChecker(request, this);
		if (checker.CheckPermissions()) {
			if (isSaveCommand(request) || isSaveAndChangeStatusCommand(request)) {
				Exam examToEdit = getEditExam(request);
				if (examToEdit == null) {
					response.sendRedirect("/ExamAssistant/ErrorPage.jsp");
				}
				SecureExam sExam = new SecureExam(examToEdit);
				// here we have valid exam to edit and paramethers for this
				// editing.
				sExam.setExamEditor(checker.getUser());
				int changedExamId = editAndSaveExam(sExam, request);
				handleUplaodedFiles(request, changedExamId, checker.getUser(), sExam);
				if (isSaveAndChangeStatusCommand(request)) {
					// exam updated now we change status
					changeExamStatus(sExam, request, changedExamId);
				}
				// update finished redirect to new page
				redirectToResultPage(response, changedExamId);
			}
		} else {
			checker.redirectToValidPage(response);
		}

	}

	/* handels file uploads and saves them to db */
	private void handleUplaodedFiles(HttpServletRequest request, int changedExamId, EAUser user, SecureExam sExam) {
		if (user instanceof Lecturer) {
			List<String> uploadedMaterials = saveUploadedFiles(request, "materialsmult[]");
			uploadedMaterials.addAll(getListOfValues("materialslist", request));
			updateMaterialsListInDb(request, uploadedMaterials, ExamManager.MATERIAL_BOOKS, changedExamId);
			List<String> uploadedVariants = saveUploadedFiles(request, "variantssmult[]");
			uploadedVariants.addAll(getListOfValues("variantsslist", request));
			updateMaterialsListInDb(request, uploadedVariants, ExamManager.MATERIAL_VARIANTS, changedExamId);
			sExam.setIsMaaterials(!uploadedMaterials.isEmpty());
			sExam.setIsVariants(!uploadedMaterials.isEmpty());
		} else {
			List<String> uploadedStudentsList = saveUploadedFiles(request, "studentsList[]");
			uploadedStudentsList.addAll(getListOfValues("studentslist", request));
			updateMaterialsListInDb(request, uploadedStudentsList, ExamManager.MATERIAL_STUDENTS_LIST, changedExamId);
			sExam.setIsStudentsList(!uploadedStudentsList.isEmpty());
		}
	}
	
	private void updateMaterialsListInDb(HttpServletRequest request, List<String> uploadedMaterials, String materialType, int examID){
		ExamManager eManager = ExamManager.getExamManager(request.getSession());
		eManager.clearUserMaterialsForExam(examID, materialType);
		int i = 0;
		for (String fileName : uploadedMaterials){
			i++;
			int val = -1;
			if (materialType.equals(ExamManager.MATERIAL_VARIANTS))
				val = i;
			eManager.addUserMaterialsForExam(examID, fileName, materialType, val, "\\Download\\" + fileName);
		}
	}

	/*
	 * handles file uploads saves them ftp server and returns list of filenames
	 */
	private List<String> saveUploadedFiles(HttpServletRequest request, String filedName) {
		List<String> filenames = new ArrayList<String>();
		// Check that we have a file upload request
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		///if (!isMultipart)
		//	return filenames;
		int maxFileSize = 50 * 1024 * 1024;
		String filePath = getServletContext().getInitParameter("file-upload");

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxFileSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File("c:\\temp"));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize);

		try {
			// Parse the request to get file items.
			List<FileItem> fileItems = upload.parseRequest(request);

			// Process the uploaded file items
			Iterator<FileItem> i = fileItems.iterator();
			File file;
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				if (!fi.isFormField()) {
					// Get the uploaded file parameters
					String fieldName = fi.getFieldName();
					if (fieldName.equals(filedName)) {
						String fileName = fi.getName();
						// Write the file
						if (fileName.lastIndexOf("\\") >= 0) {
							file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\")));
						} else {
							file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\") + 1));
						}

						fi.write(file);
						filenames.add(fileName);
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return filenames;
	}

	private boolean isSaveAndChangeStatusCommand(HttpServletRequest request) {
		String submit = getParametherFromRequest(request, "saveAndSubmitButton", null);
		// LogManager.logInfoMessage("submit pressed " + submit);
		return (submit != null);
	}

	private boolean isSaveCommand(HttpServletRequest request) {
		String save = getParametherFromRequest(request, "saveButton", null);
		// LogManager.logInfoMessage("save pressed " + save);
		return (save != null);
	}

	/* redirects page to see changed exam */
	private void redirectToResultPage(HttpServletResponse response, int ExamId) throws IOException {
		response.sendRedirect("ModifyExam?" + EXAM_ID_PARAM_NAME + "=" + ExamId);
	}

	/***************************************************************/

	/*********** functions for modifying exam and saving ************/
	/***************************************************************/

	/*
	 * filles examtoEdit with new values and processes its save in database
	 * after its mofified/created returns its id
	 */
	private int editAndSaveExam(SecureExam examToEdit, HttpServletRequest request) {
		setNewBasicValues(examToEdit, request);
		int updatedExamId = updateExamInDb(examToEdit, request);
		updateSubLecturersInDb(updatedExamId, request);
		return updatedExamId;
	}

	/* updates list of sublecturers for exam @examId */
	private void updateSubLecturersInDb(int examId, HttpServletRequest request) {

		ExamManager exManager = ExamManager.getExamManager(request.getSession());
		exManager.clearUserExamForExam(examId);
		// adding yourself as sublecturer
		exManager.addSubLecturerToExam(exManager.getExamByExamId(examId).getCreatorId(), examId);
		String[] subLecturers = request.getParameterValues("sublec[]");
		if (subLecturers == null) return;
		for (int i = 0; i < subLecturers.length; i++) {
			int lecturerId = getIntFromString(subLecturers[i], 0);
			if (validateIsLecturer(lecturerId, request))
				exManager.addSubLecturerToExam(lecturerId, examId);
		}
	}

	private ArrayList<String> getListOfValues(String paramether, HttpServletRequest request) {
		ArrayList<String> result = new ArrayList<String>();
		String[] values = request.getParameterValues(paramether);
		if(values == null) return result;
		for (int i = 0; i < values.length; i++) {
			result.add(values[i]);
		}
		return result;
	}

	/* validates given id is lecturers or not */
	private boolean validateIsLecturer(int lecturerId, HttpServletRequest request) {
		AccountManager accManager = AccountManager.getAccountManager(request.getSession());
		return ((accManager.getUserById(lecturerId).getOpResult()) instanceof Lecturer);
	}

	/*
	 * sets new values of Exam before processing it to save in db. we try to set
	 * every value Editable value of new Exam but some of them may not be
	 * changeble for given user or at all in that case fields are saved
	 * unchanged
	 */
	private void setNewBasicValues(SecureExam examToEdit, HttpServletRequest request) {
		examToEdit.setName(getParametherFromRequest(request, "examName", examToEdit.getName()));
		examToEdit.setType(getParametherFromRequest(request, "examType", examToEdit.getType()));
		examToEdit.setDuration(getNewDuration(request, examToEdit.getDuration()));
		examToEdit.setStartTime(getNewStartTime(request, examToEdit.getStartDateTime()));
		examToEdit.setResourceType(getNewNoteType(request, examToEdit.getResourceType()));
	}

	private String getNewNoteType(HttpServletRequest request, String noteType) {
		String isOpenBook = getParametherFromRequest(request, "openbookcb", Exam.NoteType.CLOSED_BOOK);
		String isOpenNote = getParametherFromRequest(request, "openNote", isOpenBook);
		return isOpenNote;
	}

	/*
	 * returns new starttime for given exam by commbining its start date and
	 * start clock
	 */
	private Timestamp getNewStartTime(HttpServletRequest request, Timestamp defaultDate) {
		String startDate = getParametherFromRequest(request, "examStartDate", "");
		String startTime = getParametherFromRequest(request, "examStartTime", "");
		if (startDate.equals("") || startTime.equals(""))
			return defaultDate;

		SimpleDateFormat format = new SimpleDateFormat("MM/DD/YYYY hh:mm");
		java.util.Date parsed = null;
		try {
			parsed = format.parse(startDate + " " + startTime);
		} catch (ParseException e) {
			return defaultDate;
		}
		Timestamp newDate = new Timestamp(parsed.getTime());
		return newDate;
	}

	/* returns new duration set in request */
	private int getNewDuration(HttpServletRequest request, int defaultduration) {
		return getIntFromString(getParametherFromRequest(request, "examDuration", "" + defaultduration),
				defaultduration);
	}

	/*
	 * passes this exam @examToEdit to ExamManager to process update of given
	 * exam in db
	 */
	private int updateExamInDb(SecureExam examToEdit, HttpServletRequest request) {
		int changedExamId = examToEdit.getExamID();
		ExamManager examManager = ExamManager.getExamManager(request.getSession());
		Exam editedExam = examToEdit.getEditedExam();
		EAUser editor = examToEdit.getExamEditor();
		if (examToEdit.needCreateExam()) {
			// creates new exam
			changedExamId = examManager.createNewExam(editor.getUserID(), editedExam.getName(),
					editedExam.getResourceType(), editedExam.getDuration(), editedExam.getNumVariants(),
					editedExam.getType(), editor.getUserID());
		} else {
			if (editor instanceof Lecturer) {
				examManager.modifyExamBasicForLecturer(editedExam.getExamID(), editedExam.getName(),
						editedExam.getResourceType(), editedExam.getDuration(), editedExam.getType(),
						editedExam.getNumVariants());
			} else {
				examManager.modifyExamBasicForBoard(editedExam.getExamID(), editedExam.getStartDateTime());
			}
		}
		return changedExamId;
	}

	/*
	 * gets paramether @parametherName from given request @request if it's not
	 * exists returns @deaultValue
	 */
	private String getParametherFromRequest(HttpServletRequest request, String parametherName, String defaultValue) {
		String[] paramVals = request.getParameterValues(parametherName);
		// String paramVal = request.getParameter(parametherName);
		if (paramVals == null || (paramVals.length == 0))
			return defaultValue;
		return paramVals[0];
	}

	/**************************/

	/*** changing status ******/
	/**************************/
	/* changes exam status after user presses submit */
	private void changeExamStatus(SecureExam sExam, HttpServletRequest request, int examId) {
		OpResult<Boolean> result = sExam.canChangeStatus();
		if (result.isSuccess()) {
			ExamManager eManager = ExamManager.getExamManager(request.getSession());
			String nextStatus = sExam.getNextStatus();
			eManager.updateExamStatus(examId, sExam.getNextStatus(), sExam.getExamStatus());
			if (nextStatus.equals(Exam.ExamStatus.PUBLISHED)){
				//ExamPublisher publisher = new ExamPublisher()
			}
		}
	}

	/***************************************************************/

	/*********** end of functions for modifying exam ***************/
	/***************************************************************/

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
		/*
		 * if (request.getParameter("examStatus") != null) { return
		 * Exam.OPEN_BOOK; } else return Exam.CLOSED_BOOK;
		 */
		return null;
	}

	private String getExamName(HttpServletRequest request) {
		if (request.getParameter("examName") != null) {
			return request.getParameter("examName");
		} else
			return Exam.EXAM_NAME_UNDEFINED;
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
			// manager.createNewExam(user.getUserID(), examName, openBook,
			// subLecturers, materials, examDuration,
			// numVariants, examType);
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
			// return Exam.EXAM_TYPE_FINAL;
		} else if (request.getParameter("examType").equals("Midterm")) {
			// return Exam.EXAM_TYPE_MIDTERM;
		}
		return null;
		// return Exam.EXAM_TYPE_QUIZZ;
	}

}
