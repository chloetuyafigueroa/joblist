package rest.omms;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.TopicManagementResponse;

import jssc.SerialPortException;

@WebServlet("/FCMServlet/*")
public class FCMServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();
        String relativePath = "/google-services.json";
        String absolutePath = getServletContext().getRealPath(relativePath);
        initializeFirebase(absolutePath);
    }

    @SuppressWarnings("deprecation") void initializeFirebase(String absolutePath) {
        if (FirebaseApp.getApps().isEmpty()) {
            try (FileInputStream serviceAccount = new FileInputStream(absolutePath)) {
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://omms-cdce8.appspot.com") // Replace with your database URL
                        .build();
                FirebaseApp.initializeApp(options);
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize Firebase", e);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> data = new HashMap<>();
        data.put("title", "FCM");
        data.put("body", "This is a custom notification body.");

        Message message = Message.builder()
                .setTopic("omms")
                .putAllData(data)
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            resp.getWriter().println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            resp.getWriter().println("Error sending message: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String command = req.getParameter("command");
        if (command.equals("sms")) {
        	String phone=req.getParameter("phone");
        	String message=req.getParameter("message");
        	String response="failed";
        	
        	Boolean sent=false;
        	if(sent) {
	        		response="sent";
        		}else {response="unsent";}
	        	try {
	    			System.out.println("Sending SMS...");
	    			
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	//}
        	
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write(response);
       }
        
    }

    public static void main(String[] args) throws IOException {
        // This method is mainly for testing purposes
        FCMServlet servlet = new FCMServlet();
        //servlet.initializeFirebase();
        String absolutePath = "WebContent/google-services.json";
        servlet.initializeFirebase(absolutePath);
        Map<String, String> data = new HashMap<>();
        data.put("title", "FCM");
        data.put("body", "This is a custom notification body.");

        sendNotification("omms", data);
    }

    public static void sendNotification(String topic, Map<String, String> data) {
        Message message = Message.builder()
                .setTopic("online_"+topic)
                .putAllData(data)
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
