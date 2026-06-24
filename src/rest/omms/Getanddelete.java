package rest.omms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Getanddelete {

	public static void main(String[] args) {

		String url = "jdbc:postgresql://localhost:5432/joblist";
        String user = "postgres";
        String password = "03_0431A";
        List<User> userList = new ArrayList<>();
        
        Connection c = null;
        //Statement stmt = null;
        try {
           //Class.forName("org.postgresql.Driver");
           c=DatabaseConnection.getInstance().getConnection();
           //c = DriverManager.getConnection(url,user, password);
           Statement stmt = null;
           stmt = c.createStatement();
           ResultSet rs = stmt.executeQuery( "SELECT * FROM SMS2;" );
           while ( rs.next() ) {
              int id = rs.getInt("id");
              String  phone = rs.getString("phone");
              String  message = rs.getString("message");
            
              System.out.println("id:"+id+",phone:"+phone+",message:"+message);
              
      		User p1 = new User();
      		p1.setId(id);
      		p1.setPhone(phone);
      		p1.setMessage(message);
    		userList.add(p1);
      		
    		PreparedStatement pstmt = c.prepareStatement("DELETE FROM SMS2 WHERE ID="+id);
    		pstmt.executeUpdate();
            pstmt.close();
           }
   	
   	   
           
           rs.close();
           //c.close();
     
        } catch ( Exception e ) {
    	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
    	         System.exit(0);
    	      }
    	      
    	      System.out.println("Operation done successfully");

    }
}
