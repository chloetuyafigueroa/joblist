package rest.omms;

import javax.servlet.ServletException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import jssc.SerialPortException;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/notifications")
public class NotificationServer {

    private static final Set<Session> clients = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
    }
    
    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        System.out.println("Session closed: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // You can process incoming messages from clients here if needed
    	//System.out.println("Sending Notification!");
    	JSONObject jObb=new JSONObject();
    	jObb.put("command", "gps");
    	
    	sendNotification(message.replace("\\", "\\\\"));
    	//sendNotification("Your notification message here");
    }

    public static void sendNotification(String message) {
    	System.out.println("websocket send notification!");
        synchronized (clients) {
            for (Session client : clients) {
            	if (client.isOpen()) {
                    try {
                        client.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        System.err.println("Error sending message to client: " + e.getMessage());
                        clients.remove(client);
                    }
                } else {
                    // Remove closed session
                    clients.remove(client);
                }
               
            }
            
        }
    }
    public static void main(String[] args)  {
    	sendNotification("Your notification message here");
	  }
    
}
