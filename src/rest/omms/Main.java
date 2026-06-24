package rest.omms;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import jssc.Exception;



//@WebServlet("/Main/*")

public class Main extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static String uid;
	private static String pwd;
	//private SmsModule smsModule;
	Connection dbCon;
	  //DataSource ds;
	  HttpSession session;
	  //String dbURL = UserService.url+"?user="+UserService.user+"&password="+UserService.password;//jdbc:postgresql:joblist?user=postgres&password=ileco1_amfm
	  public static ResultSet rs =null;
		//private static final long serialVersionUID = 1L;
		//public static String dbURL = "jdbc:postgresql:joblist?user=postgres&password=03_0431A"; //ileco1_amfm

  /* Initialize servlet. Use JNDI to look up a DataSource */
	  
  public void doPost (HttpServletRequest _req, HttpServletResponse _res)
    throws ServletException, IOException {
	    
	  //smsModule=MetaData.smsModule; 
    /* Refresh session attributes */
    session = _req.getSession();
    session.removeAttribute("loginError");
    session.removeAttribute("submitError");

    String action = _req.getParameter("action");
    //Storage storage = getStorage(); 
    
    
    if (action.equals("login")) {
    	 	uid = _req.getParameter("UID");
    	    pwd = _req.getParameter("PWD");
    	    //System.out.println("log-in:"+pwd);
      if (authenticate(uid, pwd)) {
        session.setAttribute("validUser", "y");
        session.setAttribute("loginError", "n");
        session.setAttribute("uid", uid);

      _res.setContentType("text/html;charset=UTF-8");
  	  
 	  _res.getWriter().write(uid);
 	    //System.out.println(uid);
        gotoPage("/SMS.html", _req, _res);
        }
       else {
        loginError(_req, _res);
      }
    }

  
     else if (action.equals("read")) {
    	 System.out.println(_req.getReader());
    	 JsonParser parser = new JsonParser();
 		 JsonArray arr = (JsonArray) parser.parse(_req.getReader());
 		 comPort=arr.get(0).toString().replace("\"", "");
 		
		 //yString = smsModule.readSMS(3000,comPort);	
          
    	 	
    	 _res.setContentType("text/html;charset=UTF-8");
    	 _res.getWriter().write(yString);
       	 	
      }
     else if (action.equals("listen")) {
    	 System.out.println("..listening...");
    	 //smsModule = new SmsModule("COM4"); // Replace with your port
    	
    	 
	 	try {
			//smsModule.startListening();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());;
		}         
    	
       	 	
      }
     else if (action.equals("stoplisten")) {
    	 System.out.println(_req.getReader());
    	 try {
			//smsModule.stopListening();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());;
		} 
    	 _res.setContentType("text/html;charset=UTF-8");
    	 _res.getWriter().write("stoped!");
       	 	
      }
     else if (action.equals("debug")) {
    	 System.out.println(_req.getReader());
    	 //smsModule.debugConnection(); 
    	 _res.setContentType("text/html;charset=UTF-8");
    	 _res.getWriter().write("debuged!");
       	 	
      }
     else if (action.equals("readserial")) {
    	 _res.setContentType("text/html;charset=UTF-8");
    	 //_res.getWriter().write(smsModule.readSerial(comPort));
     }
     else if (action.equals("delete")) {
    	 JsonParser parser = new JsonParser();
 		 JsonArray arr = (JsonArray) parser.parse(_req.getReader());
 		
 			//yString = smsModule.deleteSMS(Integer.valueOf(arr.get(0).toString().replace("\"", "")), Integer.valueOf(arr.get(1).toString().replace("\"", "")), arr.get(2).toString().replace("\"", ""));
 		
 		 _res.setContentType("text/html;charset=UTF-8");
    	 _res.getWriter().write(yString);
       	 	
      }
	else if (action.equals("sendPDU")) {
		JsonParser parser = new JsonParser();
		JsonArray arr = (JsonArray) parser.parse(_req.getReader());
		Boolean sent = null;
		try {
			//sent = smsModule.sendSMS(arr.get(0).toString().replace("\"", ""), arr.get(1).toString().replace("\"", ""), arr.get(2).toString().replace("\"", ""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());;
		}
		System.out.println(arr); 
		String textString="Please wait while sending...";
		 if(sent) {textString="Message Sent...";}
			 _res.setContentType("text/html;charset=UTF-8");
	    	 _res.getWriter().write(textString);
       	 	
      }
     else if (action.equals("readPDU")) {
    	 System.out.println(_req.getReader());
    	 JsonParser parser = new JsonParser();
 		 JsonArray arr = (JsonArray) parser.parse(_req.getReader());
 		 comPort=arr.get(0).toString().replace("\"", "");
    	
		try {
			try {
				//UserService.storeSMS(smsModule.readSMSPdu(3000,comPort),null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.toString());;
			}
			//xString = String.valueOf(smsModule.readSMSPdu(3000,comPort));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());;
		}
  
		_res.setContentType("text/html;charset=UTF-8");
    	_res.getWriter().write(xString);
       	 	
      }
     else if (action.equals("open")) {    
    	 System.out.println(_req.getReader());
    	 //smsModule = new SmsModule("COM4"); // Replace with your port
    	 //smsModule=MetaData.smsModule;
    	 	
    	
        	 	
       }
     else if (action.equals("close")) {    	 
    	 
    	 try {
			//smsModule.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());;
		}  
        
     	  		
 		_res.setContentType("text/html;charset=UTF-8");
     	_res.getWriter().write("Closed");
     	 	
    }
    
    }
  private  String xString="...";
  private  String yString="...";
  private  String comPort;
    /* Send request to a different page */
    private void gotoPage(String _page, HttpServletRequest _req, HttpServletResponse _res)
      throws IOException, ServletException {

      RequestDispatcher dispatcher = _req.getRequestDispatcher(_page);
      if (dispatcher != null) {
         dispatcher.forward(_req, _res);}
      if (dispatcher != null) {
    	  System.out.println(_page);}
      
    }

    /* Set error attributes in session and return to Login page */
    private void loginError(HttpServletRequest _req, HttpServletResponse _res)
      throws IOException, ServletException {

      session.setAttribute("validUser", "n");
      session.setAttribute("loginError", "y");
      gotoPage("/login.html", _req, _res);

    }

    /* Check if the user is valid */
    private boolean authenticate(String _uid, String _pwd) {

  	    Connection dbCon = null;
  	    ResultSet rs = null;
  	    //Connection c = null;
  	    //String dbURL = "jdbc:postgresql:joblist?user=postgres&password=03_0431A";
  	   
      
      
      return false;

    }

    

    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String action = request.getParameter("action");
        //Storage storage = getStorage(); 
    	// Authenticate user if request comes from login page
      
	}
    
  
   
    public static String smstest="omms\\09173144410230208161528230208161500\\*SESE RIO\\2421930400506007148090100200300400\\low\\*LOW VOLTAGE 180V LNG SECONDARY TERMINAL LOOSE CONNECTION SECONDARY TERMINAL OF TRANSFORMER\\*TUBANG BASKETBALL COURT\\*\\*\\10.7001467\\122.3901127\\*";
	     
    
    public void destroy() {}

  

  

}
