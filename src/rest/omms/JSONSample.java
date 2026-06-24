package rest.omms;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class JSONSample {
	static PlottingHelper plottingHelper;
	public static JSONObject created(JSONObject msg){
		String jo= "{'09569652335220323231348':[{'poles':[{'geom':'POINT(416711.9608999643 1176758.2319302477)','poleno':'644354238515','priassembly':'1-miscellaneous-1-A1, ','secassembly':'2-miscellaneous-2-J2, ','miscellaneous':'1-transformer-1-5kVA, '}],'buildings':[{'geom':'POINT 416659.5962857035 1176780.1250413752)','poleno':'644354238514','bldgno':'644551238035','bearing':0,'cust_class':'Residential','cust_type':'Residential_BAPA'}]}],'09569652335220221212420':[{'poles':[{'geom':'POINT(416721.3372868334 1176753.350760595)','poleno':'644310238600','priassembly':'1-miscellaneous-1-A1, 	','secassembly':'2-miscellaneous-2-J2,','miscellaneous':'1-transformer-1-5kVA, '},{'geom':'POINT(416696.3886283564 1176788.779341479)','poleno':'644630238371','priassembly':'', 'secassembly':'','miscellaneous':''},{'geom':'POINT(416660.4922637022 1176826.3723842707)','poleno':'644969238042','priassembly':'','secassembly':'','miscellaneous':''}],'buildings':[{'geom':'POINT(416686.19498850114 1176699.8030034567)','poleno':'644310238600','bldgno':'643825238280','bearing':0,'cust_class':'Low Voltage','cust_type':'Industrial'}]}]}";
		JSONObject post_jo= new JSONObject(jo);
		return post_jo;
	}	
	
	public static void main(String[] args) throws SQLException, JSONException, ClassNotFoundException{
		plottingHelper=new PlottingHelper();
		//plottingHelper.CreateTableIfNotExist();
		//JSONtoItemValues(created(null));
			//plottingHelper.asd();
		/**/try {
			System.out.println(PlottingHelper.getjArrplotter("created='09569652335220221212420'", "poles", 100));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public static void JSONtoItemValues(JSONObject msg) throws JSONException, ClassNotFoundException {
		
		
		//System.out.println(created());
		try {
			ArrayList<String> createdArr=jItems(created(msg));
			for(int i=0;i<createdArr.size();i++) {
				ArrayList<String> key_objArr=jItems(createdJSON(createdArr, i,msg));
				for(int j=0;j<key_objArr.size();j++) {
					JSONArray objArr=objArr(createdArr,i,key_objArr.get(j),msg);
					
					for(int k=0;k<objArr.length();k++) {
						JSONObject jsob=(JSONObject) objArr.get(k);
						ArrayList<String> key_jsob=jItems(jsob);
						for(int l=0;l<key_jsob.size();l++) {
							System.out.println(key_jsob.get(l)+":"+jsob.get(key_jsob.get(l)));///KEYS AND VALUES			
						}
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
							plottingHelper.upsertData(jsob, table,"topole");
							break;
						case "transformer":
							plottingHelper.upsertData(jsob, table,"topole");
							break;
						case "guy":
							plottingHelper.insertData(jsob, table);
							break;
						case "jumper":
							plottingHelper.insertData(jsob, table);
							break;
						case "dead_end":
							plottingHelper.insertData(jsob, table);
							break;
						default:
							break;
						}	                								
					}
					//System.out.println("TABLE:"+ key_objArr.get(j)); ///TABLE NAMES
				}
				//System.out.println("CREATED:"+createdArr.get(i));	///CREATED (unique ID's)
				 
				    
			}
			
			
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static JSONObject createdJSON(ArrayList<String> createdArr, int i, JSONObject msg) {
		
		JSONObject createdJSON=created(msg).getJSONArray(createdArr.get(i)).getJSONObject(0);
		//System.out.println("createdJSON(createdArr): "+createdJSON);
		return createdJSON;
		
	}
	public static JSONArray objArr(ArrayList<String> createdArr, int i, String table, JSONObject msg) throws IllegalAccessException {
		
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
	
}
