package notification_server;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/NotificationServerEndpoint")
public class NotificationServerEndpoint {

	private Set<Session> chatroomUsers;

	@OnOpen
	public void onOpen(Session userSession) {
		System.out.println("Opened: " + userSession.getId());
		//chatroomUsers.add(userSession);
		System.out.println("Opened2");
	}

	@OnClose
	public void onClose(Session userSession) {
		System.out.println("Closed1");
		//chatroomUsers.remove(userSession);
		System.out.println("Closed2");
	}

	@OnMessage
	public void onMessage(Session userSession, String message) throws IOException {

		String userName = (String) userSession.getUserProperties().get("userName");
		userName = "giorgi";
		if (userName != null) {
			userSession.getUserProperties().put("userName", message);
			userSession.getBasicRemote().sendText(message);
		} else {
			Iterator<Session> it = chatroomUsers.iterator();
			while (it.hasNext())
				it.next().getBasicRemote().sendText(message + " :" + userSession.getId());
		}

	}

}
