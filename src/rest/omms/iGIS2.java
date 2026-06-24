package rest.omms;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jssc.SerialPortException;


@WebServlet("/iGIS2/*")
public class iGIS2 extends HttpServlet { 
	
	  public static void main(String[] args) throws IllegalAccessException {
		  try {
			gpsJSONtoItemValues(gps);
		} catch (JSONException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

	static PlottingHelper plottingHelper;
    @Override
	
	public void doGet(HttpServletRequest _req, HttpServletResponse _res) throws ServletException, IOException{
    	StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = _req.getReader().readLine()) != null) {
	        sb.append(line);
	    }
	    String test="{'table':'poles', 'where':'true','limit':50}";
	    //System.out.println("tesvcjbch"+_req.getParameter("table"));
		//JSONObject msg= new JSONObject(sb.toString());
		//System.out.println("test:"+sb);
		plottingHelper=new PlottingHelper();
		//plottingHelper.CreateTableIfNotExist();
		
		JSONArray jArr = new JSONArray();
		/**/
		try {
			if(sb.length()>1){
				JSONObject msg= new JSONObject(sb.toString());	
				//JSONObject msg= new JSONObject(test);		
				jArr = new JSONArray(PlottingHelper.getjArrplotter(msg.getString("where").replace('"', (char)39),msg.getString("table"),msg.getInt("limit")));
			}else {
				jArr = new JSONArray(PlottingHelper.getjArrplotter(_req.getParameter("where").replace('"', (char)39), _req.getParameter("table").replace('"', (char)39),Integer.valueOf(_req.getParameter("limit"))));				
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
		StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = _req.getReader().readLine()) != null) {
	        sb.append(line);
	    }
		//JSONObject msg= new JSONObject(sb);
		System.out.println("From SB"+sb);
		String table=_req.getParameter("table");
		System.out.println(table);
			try {
				if(table.equals("asplan")) {					
					plotterJSONtoItemValues(sb.toString());}
				else if(table.equals("gps")) {	
					System.out.println("GPS Table:");
					gpsJSONtoItemValues(sb.toString());
				}else {
					tableJSONtoItemValues(sb.toString(), table, _req.getParameter("key"));
				}				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			_res.setContentType("text/html;charset=UTF-8");
		    _res.getWriter().write("SUCCESS!");
	}
	public static void asd() throws SQLException, ClassNotFoundException{
		plottingHelper=new PlottingHelper();
		System.out.println(PlottingHelper.getjArrplotter("created='09569652335220221212420'", "poles",100));
	
	}
	public static void plotterJSONtoItemValues(String msg) throws JSONException, ClassNotFoundException {
		plottingHelper=new PlottingHelper();
		
		//System.out.println(created());
		try {
			ArrayList<String> createdArr=jItems(created(msg));
			
			for(int i=0;i<createdArr.size();i++) {
				
				ArrayList<String> key_objArr=jItems(createdJSON(createdArr, i,msg));
				System.out.println("key_objArr:"+key_objArr);
				for(int j=0;j<key_objArr.size();j++) {
					JSONArray objArr=objArr(createdArr,i,key_objArr.get(j),msg);
					System.out.println("objArr:"+objArr);
					/**/for(int k=0;k<objArr.length();k++) {
						JSONObject jsob=(JSONObject) objArr.get(k);
						
						ArrayList<String> key_jsob=jItems(jsob);
						for(int l=0;l<key_jsob.size();l++) {
							System.out.println(key_jsob.get(l)+":"+jsob.get(key_jsob.get(l)));///KEYS AND VALUES			
						};
							jsob.put("created", createdArr.get(i));
							System.out.println("created:"+createdArr.get(i));///KEYS AND VALUES			
						
						String table=key_objArr.get(j);
						switch(table) {
						case "poles":
							plottingHelper.upsertData(jsob, table,"poleno");
							break;
						case "buildings":
							plottingHelper.upsertData(jsob, table,"bldgno");
							break;
						case "prilines":
						case "seclines":
						case "sdilines":						
							plottingHelper.upsertData_id(jsob, table,"topole");
							break;
						case "transformer":
						case "guy":
						case "dead_end":
						case "jumper":
							plottingHelper.upsertData_id(jsob, table,"poleno");
							break;
						default:
							plottingHelper.insertData(jsob, table);
							break;						
						}	                								
					}/**/
					//System.out.println("TABLE:"+ key_objArr.get(j)); ///TABLE NAMES
				}
				//System.out.println("CREATED:"+createdArr.get(i));	///CREATED (unique ID's)
				 
				    
			}
			
			
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void gpsJSONtoItemValues(String msg) throws JSONException, ClassNotFoundException, IllegalAccessException {
		plottingHelper=new PlottingHelper();
		
		System.out.println(msg);
		JSONArray objArr=new JSONArray(msg);
		for(int k=0;k<objArr.length();k++) {
			JSONObject jsob=(JSONObject) objArr.get(k);
			ArrayList<String> key_jsob=jItems(jsob);
			//for(int l=0;l<key_jsob.size();l++) {
				//System.out.println(key_jsob.get(l)+":"+jsob.get(key_jsob.get(l)));///KEYS AND VALUES			
			//}
			System.out.println("jsob:"+jsob);
			plottingHelper.insertGPS(jsob);
		}
		
		
	}
	public static void tableJSONtoItemValues(String msg, String table, String key) throws JSONException, ClassNotFoundException {
		plottingHelper=new PlottingHelper();
		try {
					JSONArray objArr=new JSONArray(msg);
					
					for(int k=0;k<objArr.length();k++) {
						JSONObject jsob=(JSONObject) objArr.get(k);
						ArrayList<String> key_jsob=jItems(jsob);
						for(int l=0;l<key_jsob.size();l++) {
							//System.out.println(key_jsob.get(l)+":"+jsob.get(key_jsob.get(l)));///KEYS AND VALUES			
						}
						
						switch(key) {
						case "0":
							plottingHelper.insertData(jsob, table);
					
						default:
							plottingHelper.upsertData(jsob, table,key);
							break;						
						}	                								
					}
					
		
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public JSONArray test(){
		String jo= "[{'09569652335220323231348':[{'poles':[{'geom':'POINT(416711.9608999643 1176758.2319302477)','poleno':'644354238514','priassembly':'1-miscellaneous-1-A1, ','secassembly':'2-miscellaneous-2-J2, ','miscellaneous':'1-transformer-1-5kVA, '}],'buildings':[{'geom':'POINT(416659.5962857035 1176780.1250413752)','poleno':'644354238514','bldgno':'644551238035','bearing':0,'cust_class':'Residential','cust_type':'Residential_BAPA'}]}],'09569652335220221212420':[{'poles':[{'geom':'POINT(416721.3372868334 1176753.350760595)','poleno':'644310238600','priassembly':'1-miscellaneous-1-A1, 	','secassembly':'2-miscellaneous-2-J2,','miscellaneous':'1-transformer-1-5kVA, '},{'geom':'POINT(416696.3886283564 1176788.779341479)','poleno':'644630238371','priassembly':'', 'secassembly':'','miscellaneous':''},{'geom':'POINT(416660.4922637022 1176826.3723842707)','poleno':'644969238042','priassembly':'','secassembly':'','miscellaneous':''}],'buildings':[{'geom':'POINT(416686.19498850114 1176699.8030034567)','poleno':'644310238600','bldgno':'643825238280','bearing':0,'cust_class':'Low Voltage','cust_type':'Industrial'}]}]}]";
		JSONArray post_jo= new JSONArray(jo);
		return post_jo;
	}
	public static JSONObject created(String msg){
		JSONObject post_jo= new JSONObject(msg);
		return post_jo;
	}
	
	public static String jo= "{'09190641656230619063844':[{'poles':[{'geom':'POINT(416711.9608999643 1176758.2319302477)','poleno':'644354238515','priassembly':'1-miscellaneous-1-A1, ','secassembly':'2-miscellaneous-2-J2, ','miscellaneous':'1-transformer-1-5kVA, '}],'buildings':[{'geom':'POINT(416659.5962857035 1176780.1250413752)','poleno':'644354238514','bldgno':'644551238035','bearing':0,'cust_class':'Residential','cust_type':'Residential_BAPA'}]}],'09778572402230331131618':[{'poles':[{'geom':'POINT(416721.3372868334 1176753.350760595)','poleno':'644310238600','priassembly':'1-miscellaneous-1-A1, 	','secassembly':'2-miscellaneous-2-J2,','miscellaneous':'1-transformer-1-5kVA, '},{'geom':'POINT(416696.3886283564 1176788.779341479)','poleno':'644630238371','priassembly':'', 'secassembly':'','miscellaneous':''},{'geom':'POINT(416660.4922637022 1176826.3723842707)','poleno':'644969238042','priassembly':'','secassembly':'','miscellaneous':''}],'buildings':[{'geom':'POINT(416686.19498850114 1176699.8030034567)','poleno':'644310238600','bldgno':'643825238280','bearing':0,'cust_class':'Low Voltage','cust_type':'Industrial'}]}]}";
	public static String gps="[{'crew':'Alimodian','message':'test','phone':'09770000000','date':'06/04/24  02:04:26 PM','geom':'POINT(432745.82222203753 1180058.1052659745)','updated':0}]";
	public static JSONObject createdJSON(ArrayList<String> createdArr, int i, String msg) {
		
		JSONObject createdJSON=created(msg).getJSONArray(createdArr.get(i)).getJSONObject(0);
		//System.out.println("createdJSON(createdArr): "+createdJSON);
		return createdJSON;
		
	}
	public static JSONArray objArr(ArrayList<String> createdArr, int i, String table, String msg) throws IllegalAccessException {
		
		JSONArray objArr=createdJSON(createdArr,i,msg).getJSONArray(table);
		
		return objArr;
		
	}
	public static ArrayList<String> jItems(JSONObject jsob) throws IllegalAccessException {
		ArrayList<String> arr=new ArrayList<String>();
		for (Iterator<String> key = jsob.keys(); key.hasNext();) {
	        String item=key.next();
	        arr.add(item);
	        
	    }

	    return arr;
	}
	
	public static String smstest="omms\\09778572402221227084240221227084200\\*Lorem ipsum dolor sit amet consectetur adipisicing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident sunt in culpa qui officia deserunt mollit anim id est laborum\\1120130400506007018090100200300400\\high\\*\\*\\*\\*\\10.85926957\\122.3694992\\*";

}
