package chat_server;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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
import models.EAUser;

@ServerEndpoint(value = "/ChatroomServerEndpoint", configurator = ServletAwareConfig.class)
public class ChatroomServerEndpoint {
	
	private Set<Session> chatroomUsers;
	private HttpSession httpSession;

	public ChatroomServerEndpoint() {
		chatroomUsers = Collections.synchronizedSet(new HashSet<Session>());
	}

	@OnOpen
	public void onOpen(Session userSession, EndpointConfig config) {
		httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		chatroomUsers.add(userSession);
		Iterator<Session> it = chatroomUsers.iterator();
		while (it.hasNext()) {
			System.out.println(it.next().getId());
		}
	}

	@OnClose
	public void onClose(Session userSession) {
		chatroomUsers.remove(userSession);
	}

	@OnMessage
	public void onMessage(String msg, Session wsSession) throws IOException {
		AccountManager accountManager = AccountManager.getAccountManager(httpSession);
		EAUser user = accountManager.getCurrentUser(httpSession);
		//wsSession.getBasicRemote().sendText(user.getFirstName() + " :" + msg + httpSession.getId());
		Iterator<Session> it = chatroomUsers.iterator();
		while (it.hasNext()) {
			it.next().getBasicRemote().sendText(buildJson(user.getFirstName(), msg + httpSession.getId()));
			
		}
	}

	/*
	 * @OnMessage public void onMessage(Session userSession, String message)
	 * throws IOException {
	 * 
	 * //HttpSession httpSession = (HttpSession)
	 * config.getUserProperties().get("httpSession"); //HttpSession httpSession1
	 * = (HttpSession)userSession.getUserProperties().get("httpSession");
	 * 
	 * HttpSession httpSession2 = (HttpSession) config.getUserProperties()
	 * .get(HttpSession.class.getName()); userName = "giorgi"; // ServletContext
	 * servletContext = httpSession.getServletContext();
	 * //System.out.println(httpSession.getId());
	 * //System.out.println(httpSession1.getId());
	 * System.out.println(httpSession2.getId()); String userName = (String)
	 * userSession.getUserProperties().get("userName"); if (userName != null) {
	 * userSession.getUserProperties().put("userName", message);
	 * userSession.getBasicRemote().sendText(buildJson("System",
	 * "Your message is " + message)); } else { Iterator<Session> it =
	 * chatroomUsers.iterator(); while (it.hasNext())
	 * it.next().getBasicRemote().sendText(buildJson(userName, message)); } }
	 */

	private String buildJson(String userName, String message) {
		String json = new Gson().toJson(userName + " :" + message);
		return json;
	}

}
