package chat_server;


import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

@ServerEndpoint("/ChatroomServerEndpoint")
public class ChatroomServerEndpoint {
	private Set<Session> chatroomUsers;

	public ChatroomServerEndpoint() {
		chatroomUsers = Collections.synchronizedSet(new HashSet<Session>());
	}

	@OnOpen
	public void onOpen(Session userSession) {
		chatroomUsers.add(userSession);
	}

	@OnClose
	public void onClose(Session userSession) {
		chatroomUsers.remove(userSession);
	}

	@OnMessage
	public void onMessage(Session userSession, String message) throws IOException {
		String userName = (String) userSession.getUserProperties().get("userName");
		userName = "giorgi";
		if (userName != null) {
			userSession.getUserProperties().put("userName", message);
			userSession.getBasicRemote().sendText(buildJson("System", "Your message is " + message));
		} else {
			Iterator<Session> it = chatroomUsers.iterator();
			while (it.hasNext())
				it.next().getBasicRemote().sendText(buildJson(userName, message));
		}
	}

	private String buildJson(String userName, String message) {
		String json = new Gson().toJson(userName + " :" + message);
		return json;
	}

}

