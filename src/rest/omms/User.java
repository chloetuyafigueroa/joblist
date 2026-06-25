package rest.omms;

import java.io.Serializable;  
import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlRootElement; 
@XmlRootElement(name = "user") 

public class User implements Serializable {  
   private static final long serialVersionUID = 1L; 
   private int id; 
   private String phone; 
   private String message;  
   public User(){} 
    
   public User(int id, String phone, String message){  
      this.id = id; 
      this.phone = phone;
      this.message = message; 
   }  
   public int getId() { 
      return id; 
   }  
   @XmlElement 
   public void setId(int id) { 
      this.id = id; 
   } 
   public String getPhone() { 
      return phone; 
   } 
   @XmlElement
   public void setPhone(String phone) { 
      this.phone = phone; 
   } 
 
   public String getMessage() { 
      return message; 
   } 
   @XmlElement 
   public void setMessage(String message) { 
      this.message = message; 
   }   
} 