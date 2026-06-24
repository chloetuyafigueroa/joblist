package rest.omms;

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

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import jssc.SerialPortException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@WebServlet("/iGIS/*")
public class iGIS extends HttpServlet { 
	/**
	 * 
	 */
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
        MetaData.smsModule.disconnect();
        init();  // Reinitialize
    }
	
	static int counter=1;
	public void init() {
	/** SmsListener smsListener=new SmsListener("COM4");
		try {
			smsListener.connect();
			smsListener.startListening();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/**
        
		SmsModule smsModule = new SmsModule("COM4"); // Replace with your port
		MetaData.smsModule=smsModule;
    	 try {
			smsModule.connect();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/**/
    	//smsModule.startListening(); 
    	 //smsModule.startSMSListening();
		ruN() ; 
		 
	}
	  public static void main(String[] args) throws IllegalAccessException, IOException {
		  //ruN();
		  
		  /**  try {
			plotterJSONtoItemValues(jo);
		} catch (JSONException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		  /**  try {
			gpsJSONtoItemValues(gps);
		} catch (JSONException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/**/
		  wesm();
	  }
	  
	  public static void wesm() throws IOException {
		  String url = "https://www.iemop.ph/";

	        Document doc = Jsoup.connect(url).get();

	        Elements liElements = doc.select("li");

	        Pattern pattern = Pattern.compile("([A-Z\\s]+):\\s*P(\\d+)/MWH");

	        for (Element li : liElements) {

	            String text = li.text();

	            Matcher matcher = pattern.matcher(text);

	            while (matcher.find()) {

	                String area = matcher.group(1).trim();
	                String price = matcher.group(2);

	                System.out.println(area + " = " + price);
	            }
	        }
	  }
	  static String jsonString = "{\"title\":\"FCM\", \"body\":\"This is a custom notification body.\"}";
	  static JSONObject jObb= new JSONObject(jsonString);
	  public static String message="omms\\09778572405230630093941230630093955\\*ecf_chloe\\1120130400506007018090100200300400\\high\\*\\*\\*\\*\\10.85926957\\122.3694992\\*";
	  public static String phone="09778562402";
	  public static String sp="COM4";  
	  public static void ruN() {
		//initializeLogger(); 
		  //String sp=serialPort();
		  
		  
		  
		  ScheduledThreadPoolExecutor executor=new ScheduledThreadPoolExecutor(2);
		  	  executor.scheduleAtFixedRate(new Runnable() {
		  		  public void run() {
		  			//SMSTranceiver.deleteAllSMS(sp);
		  			System.out.println("From iGIS");		
		  			/**/
		  			//HikariCPStatus();			
	  			       //if(counter<5) {
	  			    	   System.out.println("reading inbox....");		
	  			            	try {
									UserService.readSMS(3000,sp);
								} catch (IllegalAccessException | SerialPortException | InterruptedException
										| SQLException e) {
									System.out.println("Error:"+e);	 
								}
	  			     /**/      	//counter++;
	  			       //}else {
	  			    	   		System.out.println("sending mode....");	
	  			            	System.out.println(sp);
		  			  			UserService.sendSMS(sp);
	  			            	//log.info("From iGIS"); 
	  			       //}	 /**/	   
		  							
		  			//FCMAsyncTasks.ruN("omms",phone,message,sp,jObb);
		  						
		  			  	}
		  		  
		  		  } , 1, 30, TimeUnit.SECONDS);
		  /**/
	  }
	  /**public static void HikariCPStatus(){
		  	DataSourceConfig.monitorAndResetIfNeeded();
		  	DataSource dataSource =  DataSourceConfig.getDataSource();
	        HikariPoolMXBean poolMXBean =  ((HikariDataSource) dataSource).getHikariPoolMXBean();

	        System.out.println("Total Connections: " + poolMXBean.getTotalConnections());
	        System.out.println("Active Connections: " + poolMXBean.getActiveConnections());
	        System.out.println("Idle Connections: " + poolMXBean.getIdleConnections());
	        System.out.println("Threads Awaiting Connection: " + poolMXBean.getThreadsAwaitingConnection());
	        
	        

	  }/**/
	  public static String serialPort() {
		    StringBuilder sb = new StringBuilder();
			try{
			FileReader fr = new FileReader("C:/Log4j/serialport.txt");
			
			              while (fr.ready()) {          
			                  char ch = (char)fr.read();
			                  sb.append(ch);
			              }
			}catch(Exception e) {e.getMessage();}
			System.out.println(sb);
			return sb.toString();
	  }
	  public static Boolean initializeLogger=false;
	  /**
	   * public static void initializeLogger(){  
	 	if(!initializeLogger) {
	 		String log4JPropertyFile = "C:/Log4j/log4j.properties";
	 		Properties p = new Properties();
	 	
	 		try {
	 		    p.load(new FileInputStream(log4JPropertyFile));
	 		    //PropertyConfigurator.configure(p);
	 		    initializeLogger=true;
	 		    //log.info("Wow! I'm configured!");
	 		} catch (IOException e) {
	 	
	 		} 
	 	}
	 }
	   */
	static PlottingHelper plottingHelper;
    @Override
	
	public void doGet(HttpServletRequest _req, HttpServletResponse _res) throws ServletException, IOException{
    	 
         //System.out.println("test");
    	StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = _req.getReader().readLine()) != null) {
	        sb.append(line);
	    }
	    String test="{'table':'poles', 'where':'true','limit':50}";
	    System.out.println("tesvcjbch"+_req.getParameter("where"));
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
				switch (table) {
				    case "asplan":
				        plotterJSONtoItemValues(sb.toString());
				        break;
	
				    case "gps":
				        System.out.println("GPS Table:");
				        gpsJSONtoItemValues(sb.toString());
				        break;
				        
				    case "members":
				    	membersJSONtoItemValues(sb.toString());
				        break;
				    case "deletePole":
				    	System.out.println("deletePole:"+sb);
				    	plottingHelper=new PlottingHelper();
				    	plottingHelper.deletePole(sb.toString(),"poles");
				    	plottingHelper.deletePole(sb.toString(),"transformer");
				    	plottingHelper.deletePole(sb.toString(),"jumper");
				    	plottingHelper.deletePole(sb.toString(),"guy");
				    	plottingHelper.deletePole(sb.toString(),"dead_end");
				    	plottingHelper.deleteLine(sb.toString(),"sdilines");
						break;
				    case "deleteLine":
				    	System.out.println("deleteLine:"+sb);
				    	plottingHelper=new PlottingHelper();
				    	plottingHelper.deleteLine(sb.toString(),"prilines");
				    	plottingHelper.deleteLine(sb.toString(),"seclines");
				        break;
				    case "deleteAss":
				    	JSONObject jsob=new JSONObject(sb.toString());
				    	System.out.println("deleteAss:"+jsob);
				    	plottingHelper=new PlottingHelper();
				    	plottingHelper.deleteAss(jsob.getInt("id"),jsob.getString("table"));
				        break;
				    default:
				        joJSONtoItemValues(sb.toString(), table, _req.getParameter("key"));
				        break;
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
				//System.out.println("key_objArr:"+key_objArr);
				for(int j=0;j<key_objArr.size();j++) {
					JSONArray objArr=objArr(createdArr,i,key_objArr.get(j),msg);
					System.out.println("objArr:"+objArr);
					/**/for(int k=0;k<objArr.length();k++) {
						JSONObject jsob=(JSONObject) objArr.get(k);
						
						ArrayList<String> key_jsob=jItems(jsob);
						//for(int l=0;l<key_jsob.size();l++) {
							//System.out.println(key_jsob.get(l)+":"+jsob.get(key_jsob.get(l)));///KEYS AND VALUES			
						//};
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
						case "seclines":
						case "prilines":
							plottingHelper.upsertLine(jsob, table);
							break;
						case "sdilines":						
							plottingHelper.upsertData(jsob, table,"topole");
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
					System.out.println("TABLE:"+ key_objArr.get(j)); ///TABLE NAMES
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
			for(int l=0;l<key_jsob.size();l++) {
				System.out.println(key_jsob.get(l)+":"+jsob.get(key_jsob.get(l)));///KEYS AND VALUES			
			}
			System.out.println("jsob:"+jsob);
			NotificationServer.sendNotification(jsob.toString());
			plottingHelper.insertGPS(jsob);
		}
		
		
	}
	public static void membersJSONtoItemValues(String msg) throws JSONException, ClassNotFoundException, IllegalAccessException {
		plottingHelper=new PlottingHelper();
		JSONArray objArr=new JSONArray(msg);
		
		for(int k=0;k<objArr.length();k++) {
			JSONObject jsob=(JSONObject) objArr.get(k);
			PlottingHelper.upsertMembers(jsob);
		          								
		}
	}
	public static void joJSONtoItemValues(String msg, String table, String key) throws JSONException, ClassNotFoundException {
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
	
	public static String jo= "{'09778572402250616132758':[{'poles':[{'_id':1,'geom':'POINT(432710.48230751423 1180125.7893118134)','poleno':'675130384717','priassembly':'','secassembly':'','miscellaneous':'','created':'09778572402250616132758'}],'prilines':[{'_id':1,'geom':'MULTILINESTRING((432661.8452408392 1180004.6421851658,432710.48230751423 1180125.7893118134))','frompole':'674034384275','topole':'675130384717','size':'2/0 ACSR','length':'129.69389757455116','created':'09778572402250616132758','phase':'3P'}],'seclines':[],'sdilines':[],'kwhmeters':[],'buildings':[],'transformer':[],'guy':[],'jumper':[],'dead_end':[]}]}";
	//public static String gps="[{'date':'09/17/25 08:30:04 AM','phone':'09778572402','message':'location\\\\10.6744549;122.3850843\\\\250917083004\\\\Warehouse\\\\96','geom':'POINT(432750.40153698064 1180050.9862186313)','battery':96,'updated':0,'crew':'Warehouse'}]";
	public static String gps="[{'date':'09/17/25 11:25:32 AM','phone':'09778572402','members':'[test, sample]','message':'location\\\\10.6744892;122.3850362\\\\250917112532\\\\Warehouse\\\\85','geom':'POINT(432745.14848296664 1180054.7893690818)','battery':85,'updated':0,'crew':'Warehouse'}]";

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
