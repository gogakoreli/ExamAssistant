package chat_server;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import helper.DBConnector.SqlQueryResult;
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

	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
	}

	/**
	 * returns GsonMessage from json string
	 * 
	 * @param json
	 * @return
	 */
	private GsonMessage fromGsonToGsonMessage(String json) {
		Type gsonMessageType = new TypeToken<GsonMessage>() {
		}.getType();
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
		
		if(myMessage.updateChatWindowText){
			int userId = myMessage.fromId;
			updateChatWindowForUser(userId, accountManager, session);
			return;
		}
		
		Iterator<Session> it = sessions.keySet().iterator();
		while (it.hasNext()) {
			Session curSession = it.next();
			if (curSession.isOpen()) {
				// if address user is offline notify him
				if (!generateMessage(session, curSession, user, myMessage, examManager, accountManager)
						|| sessions.size() == 1) {
					curSession.getBasicRemote()
							.sendText(buildJson(user, "System: Address user is offline, please try later"));
				}
			}
		}

	}

	/*
	 * users ucvlis chatis windows
	 * bazidan moakvs mistvis gankutvnili chati da uxatavs
	 */
	private void updateChatWindowForUser(int userId, AccountManager accountManager, Session session) {
		ArrayList<String> gsons = getLatestMessagesForUser(userId, accountManager);
		for(int i=0; i<gsons.size(); i++){
			try {
				session.getBasicRemote().sendText(gsons.get(i));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * generates message from mainSession to curSession if it is valid user is
	 * user which entered from mainSession.
	 * 
	 */

	private boolean generateMessage(Session mainSession, Session curSession, EAUser user, GsonMessage myMessage,
			ExamManager examManager, AccountManager accountManager) throws IOException {
		if (curSession.equals(mainSession))
			return true;

		HttpSession httpSession = sessions.get(mainSession);
		if (sessionIsStudent(mainSession) && sessionIsLecturer(curSession)) {
			Student student = (Student) accountManager.getCurrentUser(httpSession);
			Lecturer curLecturer = (Lecturer) accountManager.getCurrentUser(sessions.get(curSession));
			Exam curExam = examManager.getExamForStudent(student);
			List<Lecturer> lecturers = examManager.downloadSubLecturers(curExam);
			for (Lecturer lecturer : lecturers) {
				if (lecturer.equals(curLecturer)) {
					curSession.getBasicRemote().sendText(buildJson(user, myMessage.message));
					//bazashi chaematos message
					updateMessageTableInDB(student.getUserID(), lecturer.getUserID(), myMessage.message);
					return true;
				}
			}
		} else if (sessionIsLecturer(mainSession) && sessionIsStudent(curSession)) {
			Student student = (Student) accountManager.getCurrentUser(sessions.get(curSession));
			if (student.getUserID() == myMessage.fromId) {
				curSession.getBasicRemote().sendText(buildJson(user, myMessage.message));
			}
			Lecturer lecturer = (Lecturer) accountManager.getCurrentUser(httpSession);
			updateMessageTableInDB(lecturer.getUserID(),student.getUserID(), myMessage.message);
			return true;
		}
		return false;
	}

	/**
	 * This method saves in database messages
	 */
	private void updateMessageTableInDB(int fromId, int toId, String msg) {
		//TODO uncomment and run in different thread 
		
		String updateQuery = "insert into message (fromId, toId, messageText, time) values(" + fromId + ", " + toId
				+ ", '" + msg + "', now());";
		DBConnector connector = new DBConnector();
		//connector.updateDatabase(updateQuery);
		connector.dispose();
	}

	/**
	 * helper class to save information from google gson
	 */
	public class GsonMessage {
		public int fromId;
		public String name;
		public String message;
		boolean updateChatWindowText;
		
		public GsonMessage(EAUser user, String message) {
			fromId = user.getUserID();
			name = user.getFirstName();
			this.message = message;
			updateChatWindowText = false;
		}
	}

	/**
	 * generates String which is according to GsonMessage
	 */
	private String buildJson(EAUser user, String message) {
		GsonMessage msg = new GsonMessage(user, message);
		String json = new Gson().toJson(msg);
		return json;
	}

	/**
	 * bazidan abrunebs useristvis gankutvnil chats
	 * gamoizaxeba gverdis refreshis dros, 
	 * radgan userma ar dakargos chatis monacemebi
	 */
	private ArrayList<String> getLatestMessagesForUser(int userId, AccountManager acM){
		ArrayList<String> result = new ArrayList<String>(); 
		DBConnector connector = new DBConnector();
		String getMessagesQuery ="select * from message where fromId="+userId+" or toId="+userId+
				" order by time asc LIMIT 10;";
		SqlQueryResult queryResult = connector.getQueryResult(getMessagesQuery);
		if (queryResult.isSuccess()) {
			ResultSet rs = queryResult.getResultSet();
			try {
				while (rs.next()) {
					int fromId = rs.getInt("fromId");
					//int toId = rs.getInt("toId");
					String msg = rs.getString("messageText");
					EAUser user = acM.getUserById(fromId).getOpResult();
					result.add(buildJson(user, msg));
					System.out.println(buildJson(user, msg));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		connector.dispose();
		return result;
	}

}
