package rest.omms;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import jssc.SerialPortException;


@WebServlet("/Chatbot/*")
public class Chatbot extends HttpServlet { 
	public static DataSource dataSource = ChatBotDataSourceConfig.getDataSource();
	public static Connection con=getConnection();
    public static Connection getConnection() {
    	try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
	private static final long serialVersionUID = 1L;
	//public static DataSource dataSource = DataSourceConfig.getDataSource();
	//private static final Logger log = Logger.getLogger( iGIS.class.getName());
	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Your initialization code here
    }
	public void restart() throws ServletException, SerialPortException {
		System.out.println("Restarting....");
        destroy();  // Clean up resources
        //MetaData.smsModule.disconnect();
        init();  // Reinitialize
    }
	public static void main(String[] args) throws IllegalAccessException, JSONException, ClassNotFoundException {
		String jsonString = "{\"id\":\"5\"}";
		 JSONObject jObb= new JSONObject(jsonString);
	
		delete(jObb);
	}
	
    @Override
	
	public void doGet(HttpServletRequest _req, HttpServletResponse _res) throws ServletException, IOException{
    	 
         
    	StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = _req.getReader().readLine()) != null) {
	        sb.append(line);
	    }
	    String test="{'table':'poles', 'where':'true','limit':50}";
	    System.out.println("tesvcjbch"+_req.getParameter("where"));
		//JSONObject msg= new JSONObject(sb.toString());
		//System.out.println("test:"+sb);
		
		
		JSONArray jArr = new JSONArray();
		/**/
		try {
			if(sb.length()>1){
				JSONObject msg= new JSONObject(sb.toString());	
				//JSONObject msg= new JSONObject(test);		
				jArr = new JSONArray(getjArrplotter(msg.getString("where").replace('"', (char)39),msg.getString("table"),msg.getInt("limit")));
			}else {
				jArr = new JSONArray(getjArrplotter(_req.getParameter("where").replace('"', (char)39), _req.getParameter("table").replace('"', (char)39),Integer.valueOf(_req.getParameter("limit"))));				
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**/
			// TODO Auto-generated catch block
		
		
	    _res.setContentType("text/html;charset=UTF-8");
	    _res.getWriter().write(jArr.toString());
		
	   
	    //return plottingHelper.getjArrplotter("09569652335220221212420", "poles");
		  
			
	}
	
    public void doPost (HttpServletRequest _req, HttpServletResponse _res)
		    throws ServletException, IOException {
    	System.out.println("from chatbot post method!");
    	StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = _req.getReader().readLine()) != null) {
	        sb.append(line);
	    }
	    JSONObject jsob=new JSONObject(sb.toString());
	    String action=_req.getParameter("action");
	    try {
			switch (action) {
		    case "upsert":
		    	 String table=_req.getParameter("table");
		    	 System.out.println("table:"+table);
		 		try {
		 			switch (table) {
			 		    case "status":
			 		    	upsertData(jsob, "status","id");
			 		        break;
	
			 		    case "omms":
			 		    	upsertData(jsob, "omms","id");
			 		        break;
	
			 		    default:
			 		        break;
		 			}
		 		} catch (Exception e) {
	                System.err.println("Error processing table upsert: " + e.getMessage());
	            }
		        break;

		    case "delete":
		    	delete(jsob);
		        break;

		    default:
		        break;
		}
		
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    public static void delete(JSONObject obj) {
        System.out.println("Deleting: " + obj.toString());
        String query = "DELETE FROM OMMS WHERE ID = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            // Ensure the 'id' field exists in the JSON object
            if (obj.has("id")) {
                pst.setInt(1, obj.getInt("id"));
                int rowsAffected = pst.executeUpdate();
                System.out.println("Rows deleted: " + rowsAffected);
            } else {
                System.err.println("Error: 'id' field is missing in the input JSON.");
            }
        } catch (Exception e) {
            System.err.println("Error executing delete query: " + e.getMessage());
            e.printStackTrace();
        }
    }


		
	public static ArrayList<String> jItems(JSONObject jsob) throws IllegalAccessException {
		ArrayList<String> arr=new ArrayList<String>();
		for (Iterator<String> key = jsob.keys(); key.hasNext();) {
	        String item=key.next();
	        arr.add(item);
	        
	    }

	    return arr;
	}
	
	public static JSONArray getjArrplotter(String where, String table, Integer limit) throws  ClassNotFoundException {
    	JSONArray jArr=new JSONArray();
        try {
			//con = dataSource.getConnection();		
	    	System.out.println("SELECT * FROM "+table+ " WHERE "+where+ " LIMIT "+limit);
	        Statement stmt = con.createStatement();
	        if (!where.equals("false")) {
				ResultSet rs = stmt.executeQuery("SELECT * FROM " + table + " WHERE " + where + " LIMIT " + limit);
				PreparedStatement pstmt = con.prepareStatement("SELECT * FROM " + table);
				ResultSetMetaData meta = pstmt.getMetaData();
				//System.out.println(meta.getColumnCount());
				while (rs.next()) {
					JSONObject jsob = new JSONObject();
	
					for (int i = 1; i <= meta.getColumnCount(); i++) {
						//System.out.println(meta.getColumnTypeName(i));
						Object value = rs.getObject(meta.getColumnName(i));
	
	                    if (value == null) {
	                        	jsob.put(meta.getColumnName(i), "");
	                    } else {
							switch (meta.getColumnTypeName(i)) {
								case "serial":
									jsob.put(meta.getColumnName(i), rs.getInt(meta.getColumnName(i)));
									break;
								case "geometry":
									jsob.put(meta.getColumnName(i), rs.getObject(meta.getColumnName(i)).toString());
									break;
								case "float8":
									jsob.put(meta.getColumnName(i), rs.getFloat(meta.getColumnName(i)));
									break;
								default:
									jsob.put(meta.getColumnName(i), rs.getString(meta.getColumnName(i)));
									break;
								}
	                    }
						//System.out.println("testGHKINKNDCJOPPPP:"+jsob);
					}
					jArr.put(jsob);
					
				}
				
				pstmt.close();
				stmt.close();
				rs.close();
				//con.close();
			} else {
				 
				PreparedStatement pstmt = con.prepareStatement("SELECT " + table);
				pstmt.execute();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("getjArrplotter:"+e);
		}
        System.out.println("GET:"+jArr);
		
        return jArr;
    }

	public static void upsertData(JSONObject jsob, String table, String pn) throws JSONException, IllegalAccessException, ClassNotFoundException {
        System.out.println("POST: "+jsob);
        String query = QueryStr(jsob,table,pn);
        try {
        	 PreparedStatement pst = con.prepareStatement(query);
	        
        	ArrayList<String> key_objArr=jItems(jsob);
        	
    		for(int i=0;i<key_objArr.size();i++) {	
    			String item=key_objArr.get(i);               
    			
		    			switch (item) {
		                    case "id":
		                    	pst.setInt(i+1, jsob.getInt(item));
		                    	break;
		                    case "selected":
		                        pst.setBoolean(i+1, jsob.getBoolean(item));
		                        break;
		                    default:
		                    	pst.setString(i+1, jsob.getString(item));		                		
		                        break;
		                }
    			
    		}
    	
	            pst.executeUpdate();
	            pst.close();
 	            //con.close();
	        } catch (SQLException e) {

	        	 System.err.println( e.getClass().getName()+": "+ e.getMessage() );
   	         
	        }
    }
	static String QueryStr(JSONObject jsob, String table, String pn) throws IllegalAccessException {
    	StringBuilder buf1 = new StringBuilder("INSERT INTO ");
    	StringBuilder buf2 = new StringBuilder(") VALUES(");
    	StringBuilder buf3 = new StringBuilder(" ON CONFLICT ("+pn+") DO UPDATE SET ");
		buf1.append(table+"(");
		ArrayList<String> key_objArr=jItems(jsob);
		buf1.append(key_objArr.get(0));		
		buf2.append("?");	
		buf3.append(key_objArr.get(0)+"=excluded."+key_objArr.get(0));		
		for(int i=1;i<key_objArr.size();i++) {			
			//System.out.println(key_objArr.get(i)+":"+jsob.get(key_objArr.get(i))); ///TABLE NAMES
			//System.out.println("QueryStr:"+key_objArr.get(i));
			
				buf1.append(","+key_objArr.get(i));				
				buf2.append(",?");	
			if(!key_objArr.get(i).equals("id")) {
				buf3.append(","+key_objArr.get(i)+"=excluded."+key_objArr.get(i));
			}
		}
		buf1.append(buf2+")").append(buf3);
		//System.out.println(buf1);
		return buf1.toString();    	
    }
}
