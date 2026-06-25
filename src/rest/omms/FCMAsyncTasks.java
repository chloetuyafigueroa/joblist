package rest.omms;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.FirebaseMessagingException;

public class FCMAsyncTasks {

    private static ConcurrentHashMap<String, CompletableFuture<String>> responseMap = new ConcurrentHashMap<>();
    static String jsonString = "{\"name\":\"FCM\", \"unique_id\":\"123127391274\"}";
    static JSONObject jObb= new JSONObject(jsonString);
    public static String message="omms\\09778572405230630093941230630093955\\*ecf_chloe\\1120130400506007018090100200300400\\high\\*\\*\\*\\*\\10.85926957\\122.3694992\\*";
    public static String phone="09778562402";
    static String sp="COM4";
    public static void main(String[] args) throws IllegalAccessException, InterruptedException,  IOException, ServletException {
    	//ruN("omms",phone,message,sp,jObb);
    	sendFCMGPS(false,"Alimodian");
    }
    @SuppressWarnings("unchecked")
	public static void ruN(String notificationId,String phone1,String message1,String sp1,JSONObject data) {
    	phone=phone1;message=message1;sp=sp1;
    	Map<String, String> map = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            map = objectMapper.readValue(data.toString(), Map.class);
            System.out.println(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    	CompletableFuture<String> fcmNotification = sendFCMNotification(notificationId,map);

        CompletableFuture<Void> task1 = fcmNotification
                .thenCompose(notId -> waitForAndroidResponse(notId))
              /**/  .thenCompose(response -> {
                    // Step 3: Implement the third task if successfully received
                    System.out.println("Response received, proceeding with the third task...");
                    return implementThirdTask();
                });/**/

        // Simulate the reception of the response for demonstration
        /**fcmNotification.thenAccept(notId -> {
            new Thread(() -> {
                notifyResponse(notId, "success");                
            }).start();
        });/**/

        // Wait for all tasks to complete
        task1.join();
    }

    public static CompletableFuture<String> sendFCMNotification(String notificationId,Map<String, String> map) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                runFCM(notificationId,map);
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            } // Simulate delay
            return "uniqueNotificationId"; // Return notification ID
        });
    }

    public static CompletableFuture<String> waitForAndroidResponse(String notificationId) {
        System.out.println("Waiting for response for notificationId: " + notificationId);
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        responseMap.put(notificationId, responseFuture);
        return responseFuture;
    }

    public static void notifyResponse(String notificationId, String response) {
        System.out.println("notificationId: " + notificationId);
        CompletableFuture<String> responseFuture = responseMap.get(notificationId);
        if (responseFuture != null) {
            responseFuture.complete(response);
            System.out.println("Response received for notificationId: " + notificationId + ", response: " + response);
        }
    }

    public static CompletableFuture<Void> implementThirdTask() {
        return CompletableFuture.runAsync(() -> {
            // Your third task implementation
        	//SMSTranceiver.sendSMS(phone,message,sp);
            System.out.println("Implementing the third task...");
        });
    }

    public static void runFCM(String notificationId,Map<String, String> map) throws IOException, ServletException {
    	FCMServlet servlet = new FCMServlet();
        //servlet.initializeFirebase();
        String absolutePath = "WebContent/google-services.json";
        servlet.initializeFirebase(absolutePath);
        FCMServlet.sendNotification(notificationId, map);
    }
    /**
     * @throws IOException
     * @throws ServletException
     */
    public static void sendFCMGPS(boolean online,String topic) throws IOException, ServletException {

    String absolutePath = "WebContent/google-services.json";    
   
        try (FileInputStream serviceAccount = new FileInputStream(absolutePath)) {
            @SuppressWarnings("deprecation")
			FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://omms-cdce8.appspot.com") // Replace with your database URL
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
           
    	Map<String, String> data = new HashMap<>();
    	 data.put("command", "gps");
    	 data.put("distance", "0");
         data.put("time", "100");
         data.put("monitor_time", "10000");
        String topicon = topic;
        if(online) {topicon="online_"+topic;}
        Message message = Message.builder()
                //.setCondition(condition)
                .setTopic(topicon)
                .putAllData(data)
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.NORMAL) // NORMAL priority to avoid sound or vibration
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
