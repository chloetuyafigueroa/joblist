package rest.omms;

import java.util.ArrayList;
import java.util.Iterator;


import org.json.JSONObject;

public class JavaPostgreSqlPrepared {

	public static void main(String[] args) throws IllegalAccessException {
    //PlottingHelper.CreateTableIfNotExist();
		JSONObject jsob=new JSONObject();
		jsob.put("geom","Point(2347 2144)");
		jsob.put("poleno", "3457488");
		String table= "poles";
		QueryStr(jsob,table);
  
    }
	static String QueryStr(JSONObject jsob, String table) throws IllegalAccessException {
    	StringBuilder buf1 = new StringBuilder("INSERT INTO ");
    	StringBuilder buf2 = new StringBuilder(") VALUES(");
		buf1.append(table+"(");
		buf2.append("? ");
		ArrayList<String> key_objArr=jItems(jsob);
		buf1.append(key_objArr.get(0));
		for(int i=1;i<key_objArr.size();i++) {			
			//System.out.println(key_objArr.get(i)+":"+jsob.get(key_objArr.get(i))); ///TABLE NAMES
	
			buf1.append(","+key_objArr.get(i));				
			buf2.append(", ?");
		}
		buf1.append(buf2+")");
		System.out.println(buf1);
		return buf1.toString();    	
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
