package rest.omms;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class PlottingHelper implements ServletContextListener {
	public static DataSource dataSource = DataSourceConfig.getDataSource();
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
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            DriverManager.deregisterDriver(DriverManager.getDriver("jdbc:postgresql://"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }  
    //public static String dbURL = "jdbc:postgresql://localhost:5432/joblist?user=postgres&password=03_0431A"; //ileco1_amfm
    public static ResultSet rs =null;
	public String test() {
		return "dsfhcdoho";
	}
    public static  void CreateTableIfNotExist() {
        String joborder="CREATE TABLE IF NOT EXISTS joborder (_id SERIAL PRIMARY KEY,unique_id CHARACTER VARYING (254), UNIQUE (unique_id) );";
        String poles="CREATE TABLE IF NOT EXISTS poles (_id SERIAL PRIMARY KEY, geom GEOMETRY, poleno CHARACTER VARYING (254), priassembly CHARACTER VARYING (254), secassembly CHARACTER VARYING (254), miscellaneous CHARACTER VARYING (254),created CHARACTER VARYING (254), UNIQUE (poleno));";
        String prilines="CREATE TABLE IF NOT EXISTS prilines (_id SERIAL PRIMARY KEY, geom GEOMETRY, frompole CHARACTER VARYING (254), topole CHARACTER VARYING (254), phase CHARACTER VARYING (254), size CHARACTER VARYING (254), length CHARACTER VARYING (254), created CHARACTER VARYING (254), UNIQUE (topole) );";
        String seclines="CREATE TABLE IF NOT EXISTS seclines (_id SERIAL PRIMARY KEY, geom GEOMETRY, frompole CHARACTER VARYING (254), topole CHARACTER VARYING (254), phase CHARACTER VARYING (254), size CHARACTER VARYING (254), length CHARACTER VARYING (254), created CHARACTER VARYING (254), UNIQUE (topole) );";
        String sdilines="CREATE TABLE IF NOT EXISTS sdilines (_id SERIAL PRIMARY KEY, geom GEOMETRY, frompole CHARACTER VARYING (254), topole CHARACTER VARYING (254), phase CHARACTER VARYING (254), size CHARACTER VARYING (254), length CHARACTER VARYING (254), created CHARACTER VARYING (254), UNIQUE (topole) );";
        String kwhmeters="CREATE TABLE IF NOT EXISTS kwhmeters (_id SERIAL PRIMARY KEY,geom GEOMETRY, poleno CHARACTER VARYING (254), meter_phase CHARACTER VARYING (254), meter_brand CHARACTER VARYING (254), meter_no CHARACTER VARYING (254), multiplier INTEGER, created CHARACTER VARYING (254) , UNIQUE (meter_no) );";
        String buildings="CREATE TABLE IF NOT EXISTS buildings (_id SERIAL PRIMARY KEY, geom GEOMETRY, poleno CHARACTER VARYING (254), bldgno CHARACTER VARYING (254), bearing FLOAT, cust_class CHARACTER VARYING (254), cust_type CHARACTER VARYING (254), created CHARACTER VARYING (254), UNIQUE (bldgno) );";
        String transformer="CREATE TABLE IF NOT EXISTS transformer(_id SERIAL PRIMARY KEY, geom GEOMETRY, poleno CHARACTER VARYING (254), bearing FLOAT, phase CHARACTER VARYING (254), size CHARACTER VARYING (254), created CHARACTER VARYING (254) );";
        String guy="CREATE TABLE IF NOT EXISTS guy (_id SERIAL PRIMARY KEY, poleno CHARACTER VARYING (254), type INTEGER, geom1 GEOMETRY, geom2 GEOMETRY, bearing1 FLOAT, bearing2 FLOAT, created CHARACTER VARYING (254) );";
        String jumper="CREATE TABLE IF NOT EXISTS jumper (_id SERIAL PRIMARY KEY, poleno CHARACTER VARYING (254),type INTEGER, b4prevpole CHARACTER VARYING (254), prevpole CHARACTER VARYING (254), created CHARACTER VARYING (254) );";
        String dead_end="CREATE TABLE IF NOT EXISTS dead_end(_id SERIAL PRIMARY KEY, poleno CHARACTER VARYING (254),type INTEGER, prevpole CHARACTER VARYING (254), created CHARACTER VARYING (254) );";
        String gps="CREATE TABLE IF NOT EXISTS gps(_id integer NOT NULL DEFAULT nextval('sms_id_seq'::regclass), phone CHARACTER VARYING (254),message CHARACTER VARYING (254),geom GEOMETRY, date TIMESTAMP, crew CHARACTER VARYING (254), battery INTEGER);";
        
        ArrayList<String> table=new ArrayList<String>();
        table.add(joborder);
        table.add(poles);
        table.add(prilines);
        table.add(seclines);
        table.add(sdilines);
        table.add(kwhmeters);
        table.add(buildings);
        table.add(transformer);
        table.add(guy);
        table.add(jumper);
        table.add(dead_end);
        table.add(gps);
        //DataSource dataSource = DataSourceConfig.getDataSource();
        for(int i=0;i<table.size();i++) {
        	System.out.println(table.get(i));
        	
        	  try  {
              	//Connection con = DriverManager.getConnection(dbURL);
        		  //Connection con = dataSource.getConnection();
              	PreparedStatement pst = con.prepareStatement(table.get(i));
              	pst.executeUpdate();              	
                pst.close();
                //con.close();

              } catch (SQLException ex) {

                  Logger lgr = Logger.getLogger(JavaPostgreSqlPrepared.class.getName());
                  lgr.log(Level.SEVERE, ex.getMessage(), ex);
              }
		}
      
     }

    public static void main(String[] args) {
    	CreateTableIfNotExist();
    	/**
    	  try {
			getjArrplotter("true","converter.transcale_node('f1','F1T-CA1098', ST_GeomFromText('POINT(122.3933511 10.68200695)'), ST_GeomFromText('POINT(.0001 .0001)'))",5);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 /**/
    	/**
  	  try {
			getjArrplotter("true","converter.get_table('Alimodian',null,null,'2024-06-20','Monthly')",50);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	 /**/
    }
    public static JSONArray getjArrplotter(String where, String table, Integer limit) throws  ClassNotFoundException {
    	JSONArray jArr=new JSONArray();
        //Class.forName("org.postgresql.Driver");
    	//con = DriverManager.getConnection(dbURL);
    	//Connection con;
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
								case "boolean":
								case "bool":
									jsob.put(meta.getColumnName(i), rs.getBoolean(meta.getColumnName(i)));
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
  
    public void upsertJO(JSONObject jsob) throws JSONException, IllegalAccessException, ClassNotFoundException {
    	 System.out.print("jsob:");
    	 System.out.println(jsob);
         String query = "INSERT INTO joborder(unique_id) VALUES(?) ON CONFLICT (unique_id) DO NOTHING";
         try {
        	 	//Class.forName("org.postgresql.Driver");
        	 	//con = DriverManager.getConnection(dbURL);
        	 //DataSource dataSource = DataSourceConfig.getDataSource();
        	 	//Connection con = dataSource.getConnection();
		        PreparedStatement pst = con.prepareStatement(query);
         		pst.setString(1,jsob.getString("unique_id"));
 	            pst.executeUpdate();
 	            pst.close();
 	            //con.close();
 	        } catch (SQLException e) {

 	        	 System.err.println( e.getClass().getName()+": "+ e.getMessage() );
    	         
 	        }
    }
    public void syn_upsert(JSONObject jsob) throws JSONException, IllegalAccessException, ClassNotFoundException {
   	// System.out.println("insert "+"joborder");
        String query = "INSERT INTO joborder(created) VALUES(?) ON CONFLICT (created) DO NOTHING";
        //DataSource dataSource = DataSourceConfig.getDataSource();
        try {
       	 	//Class.forName("org.postgresql.Driver");
       	 	//con = DriverManager.getConnection(dbURL);
        	 //Connection con = dataSource.getConnection();
		        PreparedStatement pst = con.prepareStatement(query);
        		pst.setString(1,jsob.getString("created"));
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
		
		if(key_objArr.get(0)=="geom") {
			buf2.append("ST_GeomFromText(?)");
		}else {
			buf2.append("?");			
		}
		buf3.append(key_objArr.get(0)+"=excluded."+key_objArr.get(0));		
		for(int i=1;i<key_objArr.size();i++) {			
			//System.out.println(key_objArr.get(i)+":"+jsob.get(key_objArr.get(i))); ///TABLE NAMES
			//System.out.println("QueryStr:"+key_objArr.get(i));
			
				buf1.append(","+key_objArr.get(i));				
				if(key_objArr.get(i).equals("geom")) {
					
					buf2.append(", ST_GeomFromText(?)");
				}else {
					buf2.append(",?");			
				}
			if(!key_objArr.get(i).equals("_id")) {
				buf3.append(","+key_objArr.get(i)+"=excluded."+key_objArr.get(i));
			}
		}
		buf1.append(buf2+")").append(buf3);
		System.out.println(buf1);
		return buf1.toString();    	
    }
    static String QueryStr2(JSONObject jsob, String table) throws IllegalAccessException {
    	StringBuilder buf1 = new StringBuilder("INSERT INTO ");
    	StringBuilder buf2 = new StringBuilder(") VALUES(");
    		buf1.append(table+"(");
		ArrayList<String> key_objArr=jItems(jsob);
		
		if(key_objArr.get(0)=="geom") {
			buf2.append("ST_GeomFromText(?)");
		}else {
			buf2.append("?");			
		}
		buf1.append(key_objArr.get(0));
		for(int i=1;i<key_objArr.size();i++) {			
			//System.out.println(key_objArr.get(i)+":"+jsob.get(key_objArr.get(i))); ///TABLE NAMES
			//if(!key_objArr.get(i).equals("_id")) {
				buf1.append(","+key_objArr.get(i));				
				if(key_objArr.get(i)=="geom") {
					buf2.append(", ST_GeomFromText(?)");
				}else {
					buf2.append(",?");			
				}
			//}
		}
		buf1.append(buf2+")");
		//System.out.println(buf1);
		return buf1.toString();    	
    }
    static String QueryStr3(JSONObject jsob, String table, String pn) throws IllegalAccessException {
    	StringBuilder buf1 = new StringBuilder("INSERT INTO ");
    	StringBuilder buf2 = new StringBuilder(") VALUES(");
    	StringBuilder buf3 = new StringBuilder(" ON CONFLICT (_id,"+pn+") DO UPDATE SET ");
		buf1.append(table+"(");
		ArrayList<String> key_objArr=jItems(jsob);
		buf1.append(key_objArr.get(0));
		
		if(key_objArr.get(0)=="geom") {
			buf2.append("ST_GeomFromText(?)");
		}else {
			buf2.append("?");			
		}
		buf3.append(key_objArr.get(0)+"=excluded."+key_objArr.get(0));		
		for(int i=1;i<key_objArr.size();i++) {			
			System.out.println(key_objArr.get(i)+":"+jsob.get(key_objArr.get(i))); ///TABLE NAMES
			buf1.append(","+key_objArr.get(i));				
			if(key_objArr.get(i)=="geom") {
				buf2.append(",ST_GeomFromText(?)");
			}else {
				buf2.append(",?");			
			}
			buf3.append(","+key_objArr.get(i)+"=excluded."+key_objArr.get(i));
		}
		buf1.append(buf2+")").append(buf3);
		System.out.println(buf1);
		return buf1.toString();    	
    }
    static String QueryStr4(JSONObject jsob, String table) throws IllegalAccessException {
    	StringBuilder buf1 = new StringBuilder("INSERT INTO ");
    	StringBuilder buf2 = new StringBuilder(") VALUES(");
    	StringBuilder buf3 = new StringBuilder(" ON CONFLICT (LEAST(frompole, topole), GREATEST(frompole, topole)) DO UPDATE SET ");
		buf1.append(table+"(");
		ArrayList<String> key_objArr=jItems(jsob);
		buf1.append(key_objArr.get(0));
		
		if(key_objArr.get(0)=="geom") {
			buf2.append("ST_GeomFromText(?)");
		}else {
			buf2.append("?");			
		}
		buf3.append(key_objArr.get(0)+"=excluded."+key_objArr.get(0));		
		for(int i=1;i<key_objArr.size();i++) {			
			//System.out.println(key_objArr.get(i)+":"+jsob.get(key_objArr.get(i))); ///TABLE NAMES
			//System.out.println("QueryStr:"+key_objArr.get(i));
			
				buf1.append(","+key_objArr.get(i));				
				if(key_objArr.get(i).equals("geom")) {
					
					buf2.append(", ST_GeomFromText(?)");
				}else {
					buf2.append(",?");			
				}
			if(!key_objArr.get(i).equals("_id")&&!key_objArr.get(i).equals("frompole")&&!key_objArr.get(i).equals("topole")) {
				buf3.append(","+key_objArr.get(i)+"=excluded."+key_objArr.get(i));
			}
		}
		buf1.append(buf2+")").append(buf3);
		System.out.println(buf1);
		return buf1.toString();    	
    }
	public static ArrayList<String> jItems(JSONObject jsob) throws IllegalAccessException {
		ArrayList<String> arr=new ArrayList<String>();
		for (Iterator<String> key = jsob.keys(); key.hasNext();) {
            String item=key.next();
            arr.add(item);
            
        }
		//System.out.println(arr);
        return arr;
    }
	public static void upsertMembers(JSONObject obj) {
		 String query = 
				    "INSERT INTO members(unique_id, jo, followed,members) " +
				    "SELECT ?, COALESCE(?,TO_CHAR(?::date, 'YYMMDD')  || COALESCE(("+ 
				    "SELECT COUNT(DISTINCT unique_id) + 1 "+ 
				    "FROM members WHERE followed::date=TO_DATE(?, 'MM/DD/YY')"+ 
				    "),1)) AS jo,?,? ON CONFLICT (unique_id, followed) DO UPDATE"+ 
				    " SET jo = EXCLUDED.jo";

		   try {
			   	PreparedStatement pst = con.prepareStatement(query);
		       	pst.setString(1,obj.getString("unique_id"));
		       	if (obj.isNull("jo")) {
		       	    pst.setNull(2, java.sql.Types.VARCHAR);
		       	} else {
		       	    pst.setString(2, obj.getString("jo"));
		       	}
		    	pst.setString(3,obj.getString("followed"));
		    	pst.setString(4,obj.getString("followed"));
		       	pst.setString(5,obj.getString("followed"));
		       	pst.setString(6,obj.getString("members"));
	            pst.executeUpdate();
	            pst.close();
	            //con.close();
	            System.out.println("test");
	        } catch (SQLException e) {

	        	 System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         
	        }
	 }
	public void deletePole(String poleno, String table)
	        throws JSONException {
		String query = "DELETE FROM " + table + " WHERE poleno = ?";

	    try (PreparedStatement pst = con.prepareStatement(query)) {

	        pst.setString(1, poleno); 
	        pst.executeUpdate();

	    } catch (SQLException e) {
	        System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    }
	}
	public void deleteAss(Integer id, String table)
	        throws JSONException {
		String query = "DELETE FROM " + table + " WHERE _id = ?";

	    try (PreparedStatement pst = con.prepareStatement(query)) {

	        pst.setLong(1, id); 
	        pst.executeUpdate();

	    } catch (SQLException e) {
	        System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    }
	}
	public void deleteLine(String topole, String table)
	        throws JSONException {
		String query = "DELETE FROM " + table + " WHERE topole = ?";

	    try (PreparedStatement pst = con.prepareStatement(query)) {

	        pst.setString(1, topole); 
	        pst.executeUpdate();

	    } catch (SQLException e) {
	        System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    }
	}

    public void upsertData(JSONObject jsob, String table, String pn) throws JSONException, IllegalAccessException, ClassNotFoundException {
        System.out.println("POST: "+jsob);
        //upsertJO(jsob);
        String query = QueryStr(jsob,table,pn);
        try {
        	//Class.forName("org.postgresql.Driver");
    	 	//con = DriverManager.getConnection(dbURL);
        	//DataSource dataSource = DataSourceConfig.getDataSource();
        	//Connection con = dataSource.getConnection();
	        PreparedStatement pst = con.prepareStatement(query);
	        
        	ArrayList<String> key_objArr=jItems(jsob);
        	
    		for(int i=0;i<key_objArr.size();i++) {	
    			String item=key_objArr.get(i);
                
    			switch (table) {
	    			case "converted":
	    				pst.setString(i+1, jsob.getString(item));
	    				break;
	    			default:
		    			switch (item) {
		                    case "type":
		                    case "inclination":
		                    case "_id":
		                    	pst.setInt(i+1, jsob.getInt(item));
		                    	break;
		                    case "existing":
		                        boolean value;

		                        Object raw = jsob.get(item);

		                        if (raw instanceof Boolean) {
		                            value = (Boolean) raw;
		                        } else if (raw instanceof Number) {
		                            value = ((Number) raw).intValue() == 1;
		                        } else {
		                            // optional: default or throw
		                            value = false;
		                        }

		                        pst.setBoolean(i + 1, value);
		                        break;
		                    case "bearing":
		                    case "bearing1":
		                    case "bearing2":
		                    case "length":
		                        pst.setDouble(i+1, jsob.getDouble(item));
		                        break;
		                    default:
		                    	pst.setString(i+1, jsob.getString(item));
		                		
		                        break;
		                }
    			}
    		}
    	
	            pst.executeUpdate();
	            pst.close();
 	            //con.close();
	        } catch (SQLException e) {

	        	 System.err.println( e.getClass().getName()+": "+ e.getMessage() );
   	         
	        }
    }
    public void upsertLine(JSONObject jsob, String table) throws JSONException, IllegalAccessException, ClassNotFoundException {
        System.out.println("POST: "+jsob);
        //upsertJO(jsob);
        String query = QueryStr4(jsob,table);
        try {
        	//Class.forName("org.postgresql.Driver");
    	 	//con = DriverManager.getConnection(dbURL);
        	//DataSource dataSource = DataSourceConfig.getDataSource();
        	//Connection con = dataSource.getConnection();
	        PreparedStatement pst = con.prepareStatement(query);
	        
        	ArrayList<String> key_objArr=jItems(jsob);
        	
    		for(int i=0;i<key_objArr.size();i++) {	
    			String item=key_objArr.get(i);
                
    			switch (table) {
	    			case "converted":
	    				pst.setString(i+1, jsob.getString(item));
	    				break;
	    			default:
		    			switch (item) {
		                    case "type":
		                    case "inclination":
		                    case "_id":
		                    	pst.setInt(i+1, jsob.getInt(item));
		                    	break;
		                    case "bearing":
		                    case "bearing1":
		                    case "bearing2":
		                    case "length":
		                        pst.setDouble(i+1, jsob.getDouble(item));
		                        break;
		                    case "existing":
		                    	boolean value;

		                        Object raw = jsob.get(item);

		                        if (raw instanceof Boolean) {
		                            value = (Boolean) raw;
		                        } else if (raw instanceof Number) {
		                            value = ((Number) raw).intValue() == 1;
		                        } else {
		                            // optional: default or throw
		                            value = false;
		                        }

		                        pst.setBoolean(i + 1, value);
		                    	break;
		                    default:
		                    	pst.setString(i+1, jsob.getString(item));
		                		
		                        break;
		                }
    			}
    		}
    	
	            pst.executeUpdate();
	            pst.close();
 	            //con.close();
	        } catch (SQLException e) {

	        	 System.err.println( e.getClass().getName()+": "+ e.getMessage() );
   	         
	        }
    }
   
    public void insertData(JSONObject jsob, String table) throws JSONException, IllegalAccessException, ClassNotFoundException {
    	 //System.out.println("insert "+table);
         //upsertJO(jsob);
         String query = QueryStr2(jsob,table);
         //DataSource dataSource = DataSourceConfig.getDataSource();
         try {
        	//Class.forName("org.postgresql.Driver");
     	 	//con = DriverManager.getConnection(dbURL);
        	 
        	 //Connection con = dataSource.getConnection();
		        PreparedStatement pst = con.prepareStatement(query);
		        //PGgeometry geom = (PGgeometry)rs.getObject(2);
         	ArrayList<String> key_objArr=jItems(jsob);
     		for(int i=0;i<key_objArr.size();i++) {			
     			String item=key_objArr.get(i);
     		    switch (item) {
	     		   	 case "type": 
	     		   	 case "_id":
	     		   		pst.setInt(i+1, jsob.getInt(item));
	     		   		break;
                     case "bearing":
                     case "bearing1":
                     case "bearing2":
                        pst.setDouble(i+1, jsob.getDouble(item));
                     	System.out.println(item+":"+jsob.getDouble(item));
                        
                         break;
                     default:
                     	pst.setString(i+1, jsob.getString(item));
                    	System.out.println(item+":"+jsob.getString(item));
                        
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
    public void insertGPS(JSONObject jsob) throws JSONException, IllegalAccessException, ClassNotFoundException {
   	 System.out.println("insert GPS:"+jsob);
   	 //DataSource dataSource = DataSourceConfig.getDataSource();
        String query = "INSERT INTO gps(date,phone,message,geom,crew,battery,members) VALUES(TO_TIMESTAMP(?, 'MM/DD/YY HH12:MI:SS PM'),?,?,ST_GeomFromText(?,4321),?,?,?)";
        String query1 = "DELETE FROM gps WHERE _id NOT IN (SELECT _id FROM (SELECT _id,ROW_NUMBER() OVER (PARTITION BY crew ORDER BY date::timestamp WITHOUT TIME ZONE DESC) AS row_num FROM gps) AS ranked WHERE row_num <= 1000)";
        try {
        	//Connection con = dataSource.getConnection();
		        PreparedStatement pst = con.prepareStatement(query);		        
		        pst.setString(1, jsob.getString("date"));
		        pst.setString(2, jsob.getString("phone"));
		        pst.setString(3, jsob.getString("message"));
		        pst.setString(4, jsob.getString("geom")); 
		        pst.setString(5, jsob.getString("crew"));        	
		        pst.setLong(6, jsob.getInt("battery"));
		        pst.setString(7, jsob.has("members") ? jsob.getString("members") : ""); 
		        int rowsInserted=pst.executeUpdate();
	            pst.close();
	            System.out.println("Rows Inserted GPS:"+rowsInserted);
	            if (rowsInserted > 0) {
	            PreparedStatement pstmt = con.prepareStatement(query1);
	            pstmt.executeUpdate();
	            pstmt.close();
	            }
	            
	            //con.close();
	        } catch (SQLException e) {

	        	 System.err.println( e.getClass().getName()+": "+ e.getMessage() );
   	         
	        }
        
    
   }
    public void upsertData_id(JSONObject jsob, String table, String pn) throws JSONException, IllegalAccessException, ClassNotFoundException {
    	 System.out.println("insert "+jsob);
         //upsertJO(jsob);
         String query = QueryStr3(jsob,table,pn);
         //DataSource dataSource = DataSourceConfig.getDataSource();
         try {
        	//Class.forName("org.postgresql.Driver");
     	 	//con = DriverManager.getConnection(dbURL);
        	// Connection con = dataSource.getConnection();
		    PreparedStatement pst = con.prepareStatement(query);
         	
         	ArrayList<String> key_objArr=jItems(jsob);
     		for(int i=0;i<key_objArr.size();i++) {			
     			String item=key_objArr.get(i);
     			//System.out.println(item+":---"+jsob.get(item));
     			switch (item) {
	                case "type":
	                case "_id":
	                   pst.setInt(i+1, jsob.getInt(item));
	                	System.out.println(item+":"+jsob.getInt(item));                   
	                    break;
	                case "existing":
                        boolean value;

                        Object raw = jsob.get(item);

                        if (raw instanceof Boolean) {
                            value = (Boolean) raw;
                        } else if (raw instanceof Number) {
                            value = ((Number) raw).intValue() == 1;
                        } else {
                            // optional: default or throw
                            value = false;
                        }

                        pst.setBoolean(i + 1, value);
                        break;
	                case "bearing":
	                case "bearing1":
	                case "bearing2":
	                   pst.setDouble(i+1, jsob.getDouble(item));
	                	System.out.println(item+":"+jsob.getDouble(item));                   
	                    break;
	                default:
	                	pst.setString(i+1, jsob.getString(item));
	               	System.out.println(item+":"+jsob.getString(item));                   
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
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
    }
