package rest.omms;

import java.io.Serializable;  
import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlRootElement; 
@XmlRootElement(name = "user") 

public class User4 implements Serializable {  
   private static final long serialVersionUID = 1L;  
   private String id;
  
   private String password;

   private String phone;
   private String role;

   
   public User4(){} 
    
   public User4(String id, String password,
		  String phone,String role){  
	   
	   this.id= id;
	   this.password= password;
	   this.phone= phone;
	   this.role= role;
	   
	
   }  
   public String getID() { return id; } @XmlElement public void setID(String id) {this.id = id;} 
   public String getPassword() { return password; } @XmlElement public void setPassword(String password) {this.password =password;}
   public String getPhone() { return phone; } @XmlElement public void setPhone(String phone) {this.phone =phone;}
   public String getRole() { return role; } @XmlElement public void setRole(String role) {this.role =role;}
  
   
} 