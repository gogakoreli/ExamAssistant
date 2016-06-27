package chat_server;

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
import models.EAUser;
import models.Exam;
import models.Lecturer;
import models.Student;

@ServerEndpoint(value = "/ChatroomServerEndpoint", configurator = ServletAwareConfig.class)
public class ChatroomServerEndpoint {
	private static final Map<Session, HttpSession> sessions = Collections.synchronizedMap(new HashMap<Session, HttpSession>());
	
	@OnOpen
	public void onOpen(Session userSession, EndpointConfig config) {
		HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		sessions.put(userSession, httpSession);
		/*
		AccountManager accountManager = AccountManager.getAccountManager(httpSession);
		EAUser user = accountManager.getCurrentUser(httpSession);
		chatroomUsers.put(userSession, user);
		if (user instanceof Student) {
			ExamManager examManager = ExamManager.getExamManager(httpSession);
			Student student = (Student) accountManager.getCurrentUser(httpSession);
			studentExam.put(student, examManager.getExamForStudent(student));
		} 
		*/
	}
	
	private EAUser getUserFromSession(Session session){
		HttpSession httpSession = sessions.get(session);
		AccountManager accountManager = AccountManager.getAccountManager(httpSession);
		return accountManager.getCurrentUser(httpSession);
	}
	
	private boolean sessionIsStudent(Session session){
		return getUserFromSession(session) instanceof Student;
	}
	
	private boolean sessionIsLecturer(Session session){
		return getUserFromSession(session) instanceof Lecturer;
	}

	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
	}

	@OnMessage
	public void onMessage(String msg, Session session) throws IOException {
		EAUser user = getUserFromSession(session);
		
		Iterator<Session> it = sessions.keySet().iterator();
		while (it.hasNext()) {
			Session s = it.next();
			if(s.isOpen()){
				//TODO ak unda chaisvas generateMessage()
				if(s.equals(session)){
					s.getBasicRemote().sendText(buildJson("You", msg));
					continue;
				}else{
					s.getBasicRemote().sendText(buildJson(user.getFirstName(), msg));
				}
			}
		}
		
	}
	
	private void generateMessage(Session curSession, Session mainSession, EAUser user, String msg) throws IOException{
		mainSession.getBasicRemote().sendText(buildJson("You", msg));
		if(curSession.equals(mainSession)) return;
			
		HttpSession httpSession = sessions.get(curSession);
		ExamManager examManager = ExamManager.getExamManager(httpSession);
		AccountManager accountManager = AccountManager.getAccountManager(httpSession);
		if(sessionIsStudent(curSession)){
			Student student = (Student) accountManager.getCurrentUser(httpSession);
			Exam curExam = examManager.getExamForStudent(student);
			//List<Lecturer> lecturers = curExam.getSubLecturers();
		}else if (sessionIsLecturer(curSession)){
			
		}
		//s.getBasicRemote().sendText(buildJson(user.getFirstName(), msg));
	}
	
	
	private String buildJson(String userName, String message) {
		String json = new Gson().toJson(userName + " :" + message);
		return json;
	}

}
