package chat_server;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import data_managers.AccountManager;
import data_managers.ExamManager;
import helper.DBConnector;
import models.EAUser;
import models.Exam;
import models.Lecturer;
import models.Student;

@ServerEndpoint(value = "/ChatroomServerEndpoint", configurator = ServletAwareConfig.class)
public class ChatroomServerEndpoint {
	private static final Map<Session, HttpSession> sessions = Collections
			.synchronizedMap(new HashMap<Session, HttpSession>());

	@OnOpen
	public void onOpen(Session userSession, EndpointConfig config) {
		HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		sessions.put(userSession, httpSession);
	}

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

	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
	}

	private GsonMessage fromGsonToGsonMessage(String json) {
		System.out.println(json);
		Type gsonMessageType = new TypeToken<GsonMessage>() {}.getType();
		GsonMessage msg = new Gson().fromJson(json, gsonMessageType);
		return msg;
	}

	@OnMessage
	public void onMessage(String json, Session session) throws IOException {
		GsonMessage myMessage = fromGsonToGsonMessage(json);
		EAUser user = getUserFromSession(session);
		HttpSession httpSession = sessions.get(session);
		ExamManager examManager = ExamManager.getExamManager(httpSession);
		AccountManager accountManager = AccountManager.getAccountManager(httpSession);
		Iterator<Session> it = sessions.keySet().iterator();
		while (it.hasNext()) {
			Session curSession = it.next();
			// TODO ak sheileba sheicvalos da bazashi werdes
			if (curSession.isOpen()) {
				// TODO ak unda chaisvas generateMessage()
				generateMessage(session, curSession, user, myMessage, examManager, accountManager);
			}
		}

	}

	/**
	 * generates message from mainSession to curSession if it is valid user is
	 * user which entered from mainSession
	 */

	private void generateMessage(Session mainSession, Session curSession, EAUser user, GsonMessage myMessage,
			ExamManager examManager, AccountManager accountManager) throws IOException {
		// TODO javascriptshi unda daiweros you: da texti rac gaagzavne
		if (curSession.equals(mainSession))
			return;
		System.out.println(myMessage.toString());
		HttpSession httpSession = sessions.get(mainSession);
		if (sessionIsStudent(mainSession) && sessionIsLecturer(curSession)) {
			Student student = (Student) accountManager.getCurrentUser(httpSession);
			Lecturer curLecturer = (Lecturer) accountManager.getCurrentUser(sessions.get(curSession));
			Exam curExam = examManager.getExamForStudent(student);
			List<Lecturer> lecturers = examManager.downloadSubLecturers(curExam);
			for (Lecturer lecturer : lecturers) {
				if (lecturer.equals(curLecturer)) {
					// TODO gaugzavne am lektors message
					curSession.getBasicRemote().sendText(buildJson(user, myMessage.message));
					break;
				}
			}
		} else if (sessionIsLecturer(mainSession) && sessionIsStudent(curSession)) {
			Student student = (Student) accountManager.getCurrentUser(sessions.get(curSession));
			if (student.getUserID() == myMessage.fromId) {
				curSession.getBasicRemote().sendText(buildJson(user, myMessage.message));
			}
			System.out.println(myMessage.toString()+"  :shemovida"+myMessage.fromId+" "+student.getUserID());
			System.out.println(myMessage.toString()+"  :shemovida");
		}
	}

	private void updateMessageTableInDB(int fromId, int toId, String msg) {
		String updateQuery = "insert into message (fromId, toId, messageText, time) values(" + fromId + ", " + toId
				+ ", '" + msg + "', now());";
		DBConnector connector = new DBConnector();
		connector.updateDatabase(updateQuery);
		connector.dispose();
	}

	public class GsonMessage {
		public int fromId;
		public String name;
		public String message;

		public GsonMessage(EAUser user, String message) {
			fromId = user.getUserID();
			name = user.getFirstName();
			this.message = message;
		}

		@Override
		public String toString() {
			return message + " " + name + fromId;
		}
	}

	private String buildJson(EAUser user, String message) {
		GsonMessage msg = new GsonMessage(user, message);
		String json = new Gson().toJson(msg);
		return json;
	}

}
