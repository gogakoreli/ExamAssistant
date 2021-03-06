package notification_server;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import chat_server.ChatroomServerEndpoint.GsonMessage;
import data_managers.AccountManager;
import data_managers.ExamManager;
import helper.DBConnector;
import models.EAUser;
import models.Exam;
import models.Lecturer;
import models.Student;

@ServerEndpoint(value = "/NotificationServerEndpoint", configurator = NotificationServletAwareConfig.class)
public class NotificationServerEndpoint {

	private static final Map<Session, HttpSession> sessions = Collections
			.synchronizedMap(new HashMap<Session, HttpSession>());

	@OnOpen
	public void onOpen(Session userSession, EndpointConfig config) {
		HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		sessions.put(userSession, httpSession);
		System.out.println("Opened" + userSession.getId() + " " + httpSession.getId());
	}

	@OnClose
	public void onClose(Session userSession) {
		sessions.remove(userSession);
		System.out.println("Closed" + userSession.getId());
	}

	@OnMessage
	public void onMessage(String gsonMessage, Session userSession) throws IOException {
		System.out.println(gsonMessage + " " + userSession.getId());

		NotiMessageForLecturer myMessage = fromGsonToGsonMessage(gsonMessage);

		if (!sessionIsLecturer(userSession)) {
			System.out.println("user is not lecturer");
		}

		HttpSession httpSession = sessions.get(userSession);
		AccountManager accountManager = AccountManager.getAccountManager(httpSession);

		Lecturer lecturer = (Lecturer) accountManager.getCurrentUser(httpSession);
		System.out.println(lecturer.getFirstName());

		ExamManager examManager = ExamManager.getExamManager(httpSession);

		Iterator<Session> it = sessions.keySet().iterator();
		while (it.hasNext()) {
			Session curSession = it.next();
			if (curSession.isOpen() && sessionIsStudent(curSession)) {
				Student student = (Student) accountManager.getCurrentUser(sessions.get(curSession));
				System.out.println("Checking for ");
				Exam curExam = examManager.getExamForStudent(student);
				DBConnector connector = new DBConnector();
				examManager.updateStudentExamInformation(student, curExam, connector);
				if (curExam.getExamID() == myMessage.examId
						&& correctVariant(myMessage.variants, student.getExamInformation().getVariant())) {
					curSession.getBasicRemote().sendText(NotiMessageForStudent(lecturer, myMessage.message));
				}
			}
		}

	}

	private boolean correctVariant(ArrayList<Integer> variants, int studentVariant) {
		for (int i = 0; i < variants.size(); i++) {
			if (variants.get(i) == studentVariant)
				return true;
		}
		return false;
	}

	public class NotiMessageForLecturer {
		public String message;
		public int examId;
		public ArrayList<Integer> variants;

		public NotiMessageForLecturer(String message, int examId, ArrayList<Integer> variants) {
			this.message = message;
			this.examId = examId;
			this.variants = new ArrayList<Integer>(variants);
		}
	}

	public class NotiMessageForStudent {
		public String message;
		public String name;

		public NotiMessageForStudent(String message, String name) {
			this.message = message;
			this.name = name;
		}
	}

	/**
	 * returns appropriate EAUser to session
	 * 
	 * @param session
	 * @return
	 */
	private EAUser getUserFromSession(Session session) {
		HttpSession httpSession = sessions.get(session);
		AccountManager accountManager = AccountManager.getAccountManager(httpSession);
		return accountManager.getCurrentUser(httpSession);
	}

	private boolean sessionIsStudent(Session session) {
		return getUserFromSession(session) instanceof Student;
	}

	private boolean sessionIsLecturer(Session session) {
		return getUserFromSession(session) instanceof Lecturer;
	}

	private NotiMessageForLecturer fromGsonToGsonMessage(String json) {
		Type gsonMessageType = new TypeToken<GsonMessage>() {
		}.getType();
		NotiMessageForLecturer msg = new Gson().fromJson(json, gsonMessageType);
		return msg;
	}

	private String NotiMessageForStudent(Lecturer lecturer, String message) {
		NotiMessageForStudent msg = new NotiMessageForStudent(lecturer.getFirstName(), message);
		String json = new Gson().toJson(msg);
		return json;
	}

}
