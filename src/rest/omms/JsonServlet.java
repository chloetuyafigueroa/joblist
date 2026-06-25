package rest.omms;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;

import jssc.SerialPortException;


//@WebServlet("/JsonServlet/*")
public class JsonServlet extends HttpServlet { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// JDBC URL, username, and password of PostgreSQL server
    private static final String URL = "jdbc:postgresql://localhost:5432/joblist";
    private static final String USER = "postgres";
    private static final String PASSWORD = "03_0431A";

    // SQL query to call the function
    //private static final String SQL_QUERY = "SELECT converter.get_towns()";
	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Your initialization code here
    }
	public void restart() throws ServletException, SerialPortException {
		System.out.println("Restarting....");
        destroy();  // Clean up resources
        
    }
	
	static int counter=1;
	
	  public static void main(String[] args) throws IllegalAccessException {
		  
		  get_jArr("Select converter.get_towns()");
	  }
	  
    @Override
	
	public void doGet(HttpServletRequest _req, HttpServletResponse _res) throws ServletException, IOException{
    	String req=_req.getParameter("req");
    	 switch (req) {
         case "v":
        	_res.setContentType("text/html;charset=UTF-8");
     	    _res.getWriter().write(get_version());
             break;
         case "towns":
         	_res.setContentType("text/html;charset=UTF-8");
      	    _res.getWriter().write(get_jArr("Select converter.get_towns()").toString());
              break;
         default:
        	_res.setContentType("text/html;charset=UTF-8");
     	    _res.getWriter().write("Please check your parameters!");
     	   break;
     }
	    
		
	    
				
	}
	
	public void doPost (HttpServletRequest _req, HttpServletResponse _res)
		    throws ServletException, IOException {
		
			_res.setContentType("text/html;charset=UTF-8");
		    _res.getWriter().write("SUCCESS!");
	}
	public static JSONArray get_jArr(String SQL_QUERY) {
    	// Load PostgreSQL JDBC Driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        String jsonResult = null;
        // Establish the connection
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY)) {

               // Set the function parameter
              // preparedStatement.setShort(1, (short) yr); // Example year

               try (ResultSet resultSet = preparedStatement.executeQuery()) {
                   // Process the result set
                   if (resultSet.next()) {
                       jsonResult = resultSet.getString(1);
                       System.out.println("JSON Result: " + jsonResult);
                      
                   }
               }

           } catch (SQLException e) {
               e.printStackTrace();
           }
        return new JSONArray(jsonResult);
		
    	
    }
	public static String get_version() {
    	// Load PostgreSQL JDBC Driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            //return (Integer) null;
        }
        String version = null;
        // Establish the connection
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT converter.get_towns_version()")) {

               // Set the function parameter
              // preparedStatement.setShort(1, (short) yr); // Example year

               try (ResultSet resultSet = preparedStatement.executeQuery()) {
                   // Process the result set
                   if (resultSet.next()) {
                	   version = resultSet.getString(1);
                       System.out.println("Town version: " + version);
                      
                   }
               }

           } catch (SQLException e) {
               e.printStackTrace();
           }
        return version;
		
    	
    }
}
