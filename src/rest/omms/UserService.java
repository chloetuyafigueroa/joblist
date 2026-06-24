package rest.omms;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.sql.DataSource;
import org.json.JSONArray;
import org.json.JSONObject;
import jssc.SerialPortException;


public class UserService  {
    
    public static DataSource dataSource = DataSourceConfig.getDataSource();
    public static Connection con;;
	static String url = "jdbc:postgresql://localhost:5432/joblist";//jdbc:postgresql://172.21.102.53:5432/joblist
	static String user = "postgres";
	static String password = "03_0431A";//ileco1_amfm 03_0431A 
	static int rowsAffected=0;
	
   public List<User> getUsers(){ 
	      return getAllUsers(); 
	}
   //public static String unique_id =null;
  
   

   public List<User> getUsers2(){ 
	     return getAllUsers2(); 
	}

  
   
   public String postStrMsg(User msg)  throws SQLException {
	   System.out.println("Opened database successfully");
	   saveSMS(msg);
	   
	   int id = msg.getId();
       String phone = msg.getPhone();
       String message = msg.getMessage();
       
       System.out.println(phone+","+message);
	       return String.valueOf(id);
      
   }
 public static Boolean complete;

 public static void saveSMS(User msg)  throws SQLException {
	   
	 System.out.println("From UserService.saveSMS");
       
	   int id = msg.getId();
       String phone = msg.getPhone();
       String message = msg.getMessage();
       String query = "INSERT INTO SMS(phone,message) VALUES(?, ?)";//INSERT INTO joborder(created) VALUES(?) ON CONFLICT (created) DO NOTHING
       String queryGPS = "SELECT converter.insert_gps(?, ?, ?, ?, ?, ?, ?)";
       //Connection con =null;
       rowsAffected=0;
       //DataSource dataSource = DataSourceConfig.getDataSource();
       if (message.contains("omms\\")){
    	   
	       try {
	    		 //Class.forName("org.postgresql.Driver");
	    		 //con1 = DriverManager.getConnection(url, user, password);
	    		 //con2 = DriverManager.getConnection(url, user, password);
	    		Connection con=dataSource.getConnection();
	    	    //con2=dataSource.getConnection();
	            PreparedStatement pst = con.prepareStatement(query);
	    	    //Statement stmt = con2.createStatement();
	    	   // ResultSet rx1 = stmt.executeQuery( "SELECT COUNT(*) FROM SMS WHERE MESSAGE='"+message.toString()+"'" +" AND PHONE='"+phone.toString()+"'"); 
		          
		        	   //pst.setInt(1, id);
			           pst.setString(1, phone);
			           pst.setString(2, message);
			           //pst.executeUpdate(); //DONT FORGET THIS ONE
			           pst.executeUpdate();
			           try {
			        	   rowsAffected = upsert(message, phone); //CONVERTER
			           } catch (Exception e) {
						System.err.println( "SMS conversion Error:"+ e.getMessage() );
						rowsAffected =0;
					}
			           //System.out.println("dsgdg");
		         
			       pst.close();
			       con.close();
			       //con2.close();
			       System.out.println("From Save SMS,rows affected: " +rowsAffected);
	       } catch (Exception e) {
	    	   System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	    	   rowsAffected =0;
	       }       
	   }
       if (message.contains("location\\")){
	       try {
	    		 //Class.forName("org.postgresql.Driver");
	    		 //con1 = DriverManager.getConnection(url, user, password);
	    		 //con2 = DriverManager.getConnection(url, user, password);
	    		 Connection con=dataSource.getConnection();
	    		 //con2=DatabaseConnection.getInstance().getConnection();
	            PreparedStatement pst = con.prepareStatement(queryGPS);
	            StringTokenizer st=new StringTokenizer(message,"\\");		          
		          String filter = st.nextToken();
		          String gps = st.nextToken();
		          StringTokenizer gpst=new StringTokenizer(gps,";");
		          String lat=gpst.nextToken();
		          String lon=gpst.nextToken();
		          String date = st.nextToken();
		          String crew = st.nextToken();
		          Integer battery=Integer.valueOf(st.nextToken());
		         	   //pst.setInt(1, id);
			           pst.setString(1, phone);
			           pst.setString(2, message);
			           pst.setString(3, lat);
			           pst.setString(4, lon);
			           pst.setString(5, date);
			           pst.setString(6, crew);
			           pst.setInt(7, battery);
			           System.out.println("saff:"+pst);
				    	
			           pst.executeUpdate(); //DONT FORGET THIS ONE
			           //insertGPS(message, phone); //CONVERTER
			            
			       pst.close();
			       con.close();
			       //con2.close();
	
	       } catch (Exception e) {
	    	   //System.err.println( e.getClass().getName()+": "+ e.getMessage() );
		         //System.exit(0)
	       }       
	   } 
       
       System.out.println(phone+","+message);
   }
   
   public static void storeSMS(JSONArray arr, String sp) throws IllegalAccessException, SQLException, InterruptedException {
	   System.out.println("From UserService.storeSMS");
	   System.out.println(arr.length());
	   //if(arr.length()==0) {complete=false;}
	   for (int i = 0; i < arr.length(); i++) {
		   JSONObject obj =arr.getJSONObject(i);
		   JSONArray CMGList=obj.getJSONArray("CMGL");
		   if(obj.getInt("MpMaxNo")==obj.getJSONArray("MpSeqNo").length()) {
			   complete=true;			  
			   User user=new User();
			   user.setMessage(ArrToString(obj.getJSONArray("data")));
			   user.setPhone(obj.getString("sender"));
			   saveSMS(user);
			   if (user.getMessage().contains("omms\\")||arr.length()>50){
				   if (rowsAffected > 0) {
			           deleteSMS(CMGList,sp);
				   }
			   }
			   if (user.getMessage().contains("location\\")||arr.length()>50){
				   //if (rowsAffected > 0) {
			           deleteSMS(CMGList,sp);
				  // }
			   }
			   
		   }else {complete=false;}
		   Date date = new Date();
		   long msec = date.getTime();
		   if(Long.valueOf(obj.getJSONArray("millis").getString(0))<(msec-60000*60*12)) {
			   if (ArrToString(obj.getJSONArray("data")).contains("omms\\")||ArrToString(obj.getJSONArray("data")).contains("location\\")||arr.length()>5){
				   deleteSMS(CMGList,sp);
			}
		   }
		   if(Long.valueOf(obj.getJSONArray("millis").getString(0))<(msec-60000*60*24*15)) {
				   deleteSMS(CMGList,sp);
			}
		   System.out.println(obj.getInt("MpMaxNo")+":"+complete);
		   System.out.println("+"+obj.getString("sender"));//.substring(2,12)
		   System.out.println("CMGL:"+obj.getJSONArray("CMGL"));
		   System.out.println(ArrToString(obj.getJSONArray("data")));
	   }		  
  }
   
   
   public static List<String> CMGList(String str){
		  return Arrays.asList(str.split(","));
	  }
   
   static String jsonString = "{\"title\":\"FCM\", \"body\":\"This is a custom notification body.\"}";
   static JSONObject jObb= new JSONObject(jsonString);
   public static String message="omms\\09778572405230630093941230630093955\\*ecf_chloe\\1120130400506007018090100200300400\\high\\*\\*\\*\\*\\10.85926957\\122.3694992\\*";
   public static String phone="09778562402";
   static String sp="COM4";
   public static void main(String args[]) throws IllegalAccessException, SerialPortException, InterruptedException, SQLException, IOException {
	   //sendFCM();
	   
	   FCMAsyncTasks.ruN("omms",phone,message,sp,jObb);
	}
      
   public List<User> getAllUsers(){ 
    
    List<User> userList = new ArrayList<>();
    //Connection con = null;
    Statement stmt = null;
   // DataSource dataSource = DataSourceConfig.getDataSource();
    try {
       //Class.forName("org.postgresql.Driver");
       //c = DriverManager.getConnection(url,user, password);
    	Connection con=dataSource.getConnection();
       //c.setAutoCommit(false);
      System.out.println("Opened database successfully34");

       stmt = con.createStatement();
       ResultSet rs = stmt.executeQuery( "SELECT * FROM SMS2;" );
       while ( rs.next() ) {
          int id = rs.getInt("id");
          String  phone = rs.getString("phone");
          String  message = rs.getString("message");
     
  		User p1 = new User();
  		p1.setId(id);
  		p1.setPhone(phone);
  		p1.setMessage(message);
  		userList.add(p1);
  	   
  		PreparedStatement pstmt = con.prepareStatement("DELETE FROM SMS2 WHERE ID="+id);
   		pstmt.executeUpdate();
  		pstmt.close();
       }
  
       rs.close();
       stmt.close();
       con.close();
 
    } catch ( Exception e ) {
	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         //System.exit(0);
	         DataSourceConfig.resetDataSource();
	      }
	      
	      //System.out.println("Operation done successfully");
    return userList; 
 }
public static List<User> getAllUsers2(){ 
    
    List<User> userList = new ArrayList<>();
    //Connection con = null;
    Statement stmt = null;
    //DataSource dataSource = DataSourceConfig.getDataSource();
    try {
       //Class.forName("org.postgresql.Driver");
       //c = DriverManager.getConnection(url,user, password);
    	Connection con=dataSource.getConnection();
       //c.setAutoCommit(false);
      System.out.println("Opened database successfully");

       stmt = con.createStatement();
       ResultSet rs = stmt.executeQuery( "SELECT * FROM SMS LIMIT 2;" );
       while ( rs.next() ) {
          int id = rs.getInt("id");
          String  phone = rs.getString("phone");
          String  message = rs.getString("message");
          System.out.println(phone+"-"+message);
  		User p1 = new User();
  		p1.setId(id);
  		p1.setPhone(phone);
  		p1.setMessage(message);
  		userList.add(p1);
  	   
  		
       }
  
       rs.close();
       stmt.close();
       con.close();
 
    } catch ( Exception e ) {
	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         //System.exit(0);
	      }
	      
	      //System.out.println("Operation done successfully");
    return userList; 
 }
public static Integer upsert(String Msg, String Addrs) {
    System.out.println("From UserService.upsert()");
    System.out.println(Msg);

    String query = "{ ? = call converter.upsert_sms(?, ?) }";
    int rowsAffected = 0;

    try (Connection con = dataSource.getConnection();
            CallableStatement cst = con.prepareCall(query)) {
           cst.registerOutParameter(1, java.sql.Types.INTEGER);
           cst.setString(2, Addrs);
           cst.setString(3, Msg);
           cst.execute();
           rowsAffected = cst.getInt(1);
           cst.close();
           con.close();
           
    } catch (SQLException ex) {
           rowsAffected = 0;
    }


    return rowsAffected;
}


public static Integer insert(String Msg, String Addrs) {
	System.out.println("From UserService.insert()");
	System.out.println(Msg);
    try{
    StringTokenizer st=new StringTokenizer(Msg,"\\");
   
    String filter = st.nextToken();
    String complete_id = st.nextToken();
    String unique_id = complete_id.substring(0, 23);
    String creator = complete_id.substring(0, 11);
    String created = complete_id.substring(11, 23);
    String follower = Addrs;
    if (Addrs.length()==13) {
    	follower = "0"+ Addrs.substring(3,13);
    }
    if (Addrs.length()==12) {
    	follower = "0"+ Addrs.substring(2,12);
    }
    System.out.println("follower:"+follower);
    String followed = complete_id.substring(23, 35);
    System.out.println("followed:"+followed);

    
    Date createdDate2 = null;
    Date followedDate2 = null;
    SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
    try {
        createdDate2 = df.parse(created);
        followedDate2 = df.parse(followed);
    } catch (ParseException e) {
        e.printStackTrace();
    }
    SimpleDateFormat dateFormat= new SimpleDateFormat("MM/dd/yy  hh:mm:ss a");
    created= dateFormat.format(createdDate2).toUpperCase();
    followed= dateFormat.format(followedDate2);
    
    
    
    
        String name = st.nextToken();
            name = name.substring(1, name.length());
        String spinners = st.nextToken();
        String town0 = converter(spinners.substring(0, 2));
        String brgy0= converter(spinners.substring(0, 5));//1
        String town= converter(String.valueOf(Integer.valueOf(spinners.substring(5, 7))-20));//6
        String brgy= converter(String.valueOf(Integer.valueOf(spinners.substring(5, 10))-20200));//8
        String town2= converter(String.valueOf(Integer.valueOf(spinners.substring(10, 12))-40));//7
        String brgy2= converter(String.valueOf(Integer.valueOf(spinners.substring(10, 15))-40400));//9
        String assignedto= converter(spinners.substring(15, 18));//12
        String status= converter(spinners.substring(18, 20));//13
        String subs= converter(spinners.substring(20, 22));//2
        String feeder= converter(spinners.substring(20, 25));//3
        String section= converter(spinners.substring(25, 28));//4
        //String O= cause_msg(Integer.valueOf(spinners.substring(27, 30)) - 300, type);//10
        String equip= converter(spinners.substring(31, 34));//5                                                          //4
        String message= Msg;
        //String location= "";                                                //11
		String type = st.nextToken();
			
			String typeref= "101";
			if (type.equals("high")){
				typeref= "101";
			}else	if (type.equals("medium")){
				typeref= "102";
			}else	if (type.equals("low")){
				typeref= "103";
			}
    
        String cause= converter(typeref + spinners.substring(28, 31));//10
    String notes = st.nextToken();
        notes = notes.substring(1, notes.length());
    String landmark= st.nextToken();    //16
        landmark = landmark.substring(1, landmark.length());
    String phone= st.nextToken();
       phone = phone.substring(1, phone.length());
           String location= st.nextToken(); //activate if corrected
        location = location.substring(1, location.length());
    String latitude= st.nextToken();                                             //15
    String longitude= st.nextToken();
    String actiontaken= st.nextToken();    //14
        actiontaken = actiontaken.substring(1, actiontaken.length());

        
        //String url = "jdbc:postgresql://localhost:5432/joblist";
        //String user = "postgres";
        //String password = "ileco1_amfm";
        System.out.println("name:"+name);
       
        String query2 = "INSERT INTO CONVERTED(unique_id, creator, created, follower, followed, name, spinners, town0, brgy0, town, brgy, town2, brgy2, assignedto, status, subs, feeder, section, cause, equip, type, notes, landmark, phone, location, latitude, longitude, actiontaken)" + 
        		"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        //DataSource dataSource = DataSourceConfig.getDataSource();
        
        try (//Connection con = DriverManager.getConnection(url, user, password);
        		Connection con =dataSource.getConnection();
        		
             PreparedStatement pst = con.prepareStatement(query2)) {
        	//System.out.println(query2);
            //pst.setInt(1, id);
            pst.setString(1, unique_id);
            pst.setString(2, creator);
            pst.setString(3, created);
            pst.setString(4, follower);
            pst.setString(5, followed);
            pst.setString(6, name);
            pst.setString(7, spinners);
            pst.setString(8, town0);
            pst.setString(9, brgy0);
            pst.setString(10, town);
            pst.setString(11, brgy);
            pst.setString(12, town2);
            pst.setString(13, brgy2);
            pst.setString(14, assignedto);
            pst.setString(15, status);
            pst.setString(16, subs);
            pst.setString(17, feeder);
            pst.setString(18, section);
            pst.setString(19, cause);
            pst.setString(20, equip);
            pst.setString(21, type);
            pst.setString(22, notes);
            pst.setString(23, landmark);
            pst.setString(24, phone);
            pst.setString(25, location);
            pst.setString(26, latitude);
            pst.setString(27, longitude);
            pst.setString(28, actiontaken);
            //System.out.println(pst.toString());
            rowsAffected=pst.executeUpdate();
            pst.close();
            //con.close();
        } catch (SQLException ex) {
        	rowsAffected=0;
        	System.out.println("test:"+ex);
            //Logger lgr = Logger.getLogger(JavaPostgreSqlPrepared.class.getName());
            //lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        System.out.println("dsfdsf:"+con);
        
        
    }catch (Exception c){rowsAffected=0;System.out.println("test:"+c);};
    return rowsAffected;
}

public static String converter(String i) {
	List<User> userList = new ArrayList<>();
    //Connection con = null;
    Statement stmt = null;
    
    String  description = null;
    String converted=null;
    try {
       //Class.forName("org.postgresql.Driver");
       //c = DriverManager.getConnection(url,user, password);
       Connection con=dataSource.getConnection();
       con.setAutoCommit(false);
       //System.out.println("Opened database successfully");

       stmt = con.createStatement();
       
       ResultSet rs = stmt.executeQuery( "SELECT * FROM TOWNS;" );
       while ( rs.next() ) {
          int id = rs.getInt("id");
          String  serial_no = rs.getString("serial_no");
          String  category = rs.getString("category");
          description = rs.getString("description");
          if(i.equals(serial_no)) {
        	  converted=description;
        	  }
       }
       
       
       rs.close();
       stmt.close();
       //c.close();
 
    } catch ( Exception e ) {
	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         System.exit(0);
	      }
	      
	      //System.out.println("Operation done successfully");

	return converted;
	}


/**/public static void runFCM() throws IOException, ServletException {
	FCMServlet servlet = new FCMServlet();
    //servlet.initializeFirebase();
    String absolutePath = "WebContent/google-services.json";
    servlet.initializeFirebase(absolutePath);
    Map<String, String> data = new HashMap<>();
    data.put("title", "FCM");
    data.put("body", "This is a custom notification body.");

    FCMServlet.sendNotification("omms", data);
}/**/
public static String smstest="omms\\09778572405230630093941230630093955\\*ecf_chloe\\1120130400506007018090100200300400\\high\\*\\*\\*\\*\\10.85926957\\122.3694992\\*";
public static String GPStest="location\\10.6676623;122.332172\\230420133221\\Cabatuan";
public static void testRead() throws SQLException {
	User user=new User();
	   user.setMessage(smstest);
	   user.setPhone("639778572402");
	   saveSMS(user);
}




}
