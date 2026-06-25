package rest.omms;



import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

//import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jssc.SerialPort;
import jssc.SerialPortException;
import rest.omms.PDUConverter.PDUEncoded;
import rest.omms.gsm3040.Pdu;
import rest.omms.gsm3040.PduParser;
import rest.omms.gsm3040.PduUtils;

public class SMSTranceiver {
	//private static final Logger log = Logger.getLogger( SMSTranceiver.class.getName());
	 static String sp="COM4";
	 static char ch = '"';
	 static String ctz= Character.toString ((char) 26)+ "\r";//
	public static SerialPort serialPort;
	public static void main(String args[]) throws IllegalAccessException, SerialPortException, InterruptedException {
	String sms= "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	sms="test";	
    //readSMSPdu(1000,"COM3");
	readSMS(500,"COM3");
	//serialPort();
	//open("COM3");
	//close("COM3");
    
	JSONArray millis0=new JSONArray("[\"1673783064570\",\"1673783064575\"]");
	JSONArray CMGL0=new JSONArray("[8,9]");
	JSONArray MpSeqNo0=new JSONArray("[1,2]");
	JSONArray data0=new JSONArray("[a,b]");
	//Pdu pdu=new PduParser().parsePdu(smsPdu);//pduEnc.getPduEncoded()"001100098121436587F900000B029B17"
    //System.out.println(pdu);
	
	//System.out.println(ArrToString(upsertConcat("1673783064580","3","5","c",millis0,CMGL0,MpSeqNo0,data0).getJSONArray(3)));
//printSMS(pDUArray(readString()));
	//System.out.println(readSMSPdu(3000,"COM7")); 
     //log.info("Hello this is an info message2");
	//sendSMS("+639932844568","test","COM3");
	//deleteAllSMS("COM3");
	//deleteSMS(26,0,"COM3");
	
	
	}
	public static String ArrToString(JSONArray jArr) {
		String x="";
		for(int i=0;i<jArr.length();i++) {
			x=x+jArr.get(i);
		}
		return x;
	}
	public static Boolean initializeLogger=false;
	/**public static String serialPort() {
    StringBuilder sb = new StringBuilder();
	try{
	FileReader fr = new FileReader("WebContent/serialport.txt");
	
	              while (fr.ready()) {          
	                  char ch = (char)fr.read();
	                  sb.append(ch);
	              }
	}catch(Exception e) {e.getMessage();}
	System.out.println(sb);
	return sb.toString();
}
public static String readString() {
    StringBuilder sb = new StringBuilder();
	try{
	FileReader fr = new FileReader("C:/Log4j/pdu.txt");
	
	              while (fr.ready()) {          
	                  char ch = (char)fr.read();
	                  sb.append(ch);
	              }
	}catch(Exception e) {e.getMessage();}
	//System.out.println(sb);
	return sb.toString();
}**/
 /**
  * public static void initializeLogger(){  
	if(!initializeLogger) {
		String log4JPropertyFile = "C:/Log4j/log4j.properties";
		Properties p = new Properties();
	
		try {
		    p.load(new FileInputStream(log4JPropertyFile));
		    //PropertyConfigurator.configure(p);
		    initializeLogger=true;
		    //log.info("From SMSTransciever:Wow! I'm configured!");
		} catch (IOException e) {
	
		} 
	}
}
  */
	public static Boolean isOpened=false;									
	public static void open( String sp) {
		//initializeLogger();
		serialPort = new SerialPort(sp);
		
		/**/if(!isOpened) {
			try {
							
				serialPort.openPort();
				serialPort.setParams(SerialPort.BAUDRATE_9600, 
	                SerialPort.DATABITS_8,
	                SerialPort.STOPBITS_1,
	                SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
			TimeUnit.MILLISECONDS.sleep(100);
			isOpened=serialPort.isOpened();
			System.out.println(sp+":is opened:"+isOpened);
			} catch (SerialPortException | InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}//Open serial port
		}/**/
    }
	public static void close(String sp)  {
		//serialPort = new SerialPort(sp);
		if(isOpened) {
			try {
				
				serialPort.closePort();
				TimeUnit.MILLISECONDS.sleep(200);
				isOpened=serialPort.isOpened();
				System.out.println(sp+":is closed:"+isOpened);
			} catch (SerialPortException | InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	
	public static String deleteSMS(int x, int y, String sp) {
		 System.out.println("From deleteSMS SP:"+sp);/**/
		
			    try {		        	
			        serialPort.writeString("AT+CMGD="+x+","+y+"\r");
			        TimeUnit.MILLISECONDS.sleep(1000);	
			        smsPdu=serialPort.readString();
			        System.out.println("From deleteSMS SerialPort.readString():");
			        System.out.println("deleting:"+smsPdu);
		        
		        } catch (jssc.SerialPortException e) {
		        System.out.println("From deleteSMS:"+e);/**/
		       // log.debug("From deleteSMS:"+e);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//log.debug("From deleteSMS:"+e);
				}			
		
		 return smsPdu;
	}
	public static void deleteSMS(JSONArray cMList,String sp) {
		   System.out.println("From UserService.deleteSMS()");
		   for(int i=0;i<cMList.length();i++) {
			   System.out.println(cMList.get(i));
			   String cMGL=cMList.get(i).toString();
			   Boolean deleted=false;
			   while(!deleted) {
		            deleted=deleted(SMSTranceiver.deleteSMS(Integer.valueOf(cMGL), 0,sp));
			   //     SMSTranceiver.deleteSMS(Integer.valueOf(cMGL), 0,sp);
			   }
		   }
	   }
	   public static Boolean deleted(String xString)  {
	   	Boolean deleted=false;
	   	int okCount=0;
	   	System.out.println((xString));
	   	if(xString!=null) {
		    	Scanner scanner = new Scanner(xString);
		    	while (scanner.hasNextLine()) {
		    		String line = scanner.nextLine();
			         if(line.equals("OK")) { 
			        	 okCount++;
			         }   
		        }       
		        scanner.close();
		        if(okCount>0) {deleted=true;}
		        System.out.println("deleted:"+deleted);
	   	}
	   	return deleted;
	   }
	public static void deleteAllSMS(String sp) {
		 System.out.println("From deleteAllSMS SP:"+sp);/**/
		 
		 if(isOpened) {
			 System.out.println("Please wait while running!");
	     }else {	 
			open(sp);	
			    try {		        	
			        serialPort.writeString("AT+CMGL=ALL \r");
			        TimeUnit.MILLISECONDS.sleep(1000);	
			        smsPdu=serialPort.readString();
			        System.out.println("deleting:"+smsPdu);
		        
		        } catch (jssc.SerialPortException e) {
		        System.out.println("From deleteSMS:"+e);/**/
		        //log.debug("From deleteSMS:"+e);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//log.debug("From deleteSMS:"+e);
				}
			 close(sp);
	     }
	        
	}
	public static String smsReadString="";
	public static JSONArray pDUArray(String xString) {
		//log.info(xString);
		jsonArray=new JSONArray();
		if(xString!=null) {
			Scanner scanner = new Scanner(xString);
	        String pduString="";
	        while (scanner.hasNextLine()) {
	        	/**/String line = scanner.nextLine();
	          
	          JSONObject jsonObject=new JSONObject();
	          if(line.contains("+CMGL:")) { 
	        	  int s=line.indexOf(":");
	             List<String> CMGList=CMGList(line.substring(s+2, line.length()));
	        	  jsonObject.put("CMGL",new JSONArray("[\""+CMGList.get(0)+"\"]"));//
	        	  pduString=scanner.nextLine();
	        	  //System.out.println(pduString.length()/2);
	        	 
	        	  if((pduString.length()/2)==Integer.valueOf(CMGList.get(3))+8) {
	        		  
		        	  
					try {
						 Pdu pdu = new PduParser().parsePdu(pduString);
					
			        	  long millis = ((Calendar)pdu.getSmscTimeStamp()).getTimeInMillis();
			          
			        	  jsonObject.put("sender",pdu.getAddress());
			        	  //jsonObject.put("rawpdu",pdu.getRawPdu());
			        	  jsonObject.put("millis",new JSONArray("[\""+String.valueOf(millis)+"\"]"));
			        	  jsonObject.put("isConcat",pdu.isConcatMessage());
			        	  jsonObject.put("MpMaxNo",1);
			        	  jsonObject.put("MpSeqNo",new JSONArray("[\"1\"]"));
			        	 if(pdu.isConcatMessage()) {
				        	  jsonObject.put("UDH",toHexString(pdu.getUDHData(), 4));
				        	  jsonObject.put("MpMaxNo",pdu.getMpMaxNo());
				        	  jsonObject.put("MpSeqNo",new JSONArray("[\""+pdu.getMpSeqNo()+"\"]"));
			        	 }
			        	 JSONArray jData=new JSONArray();
			        	 jsonObject.put("data",jData.put(pdu.getDecodedText()));
			        	 UpsertSMS(jsonObject);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						//deleteSMS(jsonObject,sp);
					}
		        	 //jsonArray.put(jsonObject);
					//System.out.println(jsonArray);
	        	  }
	          }       
	        }
	        System.out.println(jsonArray);
	        scanner.close();
		}
	     
		return jsonArray;		
	}
	static int cnt=0;
	public static JSONArray readSMSPdu(int TD,String sp) throws IllegalAccessException, SerialPortException {
		JSONArray readSMSPdu=new JSONArray();		
		/**/if(isOpened) {
			System.out.println("Please wait while running!");
			/**/JSONObject jsonObject=new JSONObject();
			jsonObject.put("CMGL",new JSONArray("[\"0\"]"));
			jsonObject.put("sender","Joblist");
			jsonObject.put("millis",new JSONArray("[\"123456\"]"));
			jsonObject.put("isConcat",true);
			jsonObject.put("MpMaxNo",3);
			jsonObject.put("MpSeqNo",new JSONArray("[\"1\"]"));
			jsonObject.put("UDH","0000");
			jsonObject.put("data",new JSONArray("[\"Please wait while running!\"]"));
			readSMSPdu.put(jsonObject);/**/
			
        }else {	
			open(sp);	    	
		    /**/try {  
		    	System.out.println("SMSTranceiver.readSMSPdu(TD)");
			    serialPort.writeString("AT+CMGF=0" + "\r");
		        TimeUnit.MILLISECONDS.sleep(1000);
		        serialPort.writeString("AT+CMGL=4" + "\r");	  
		        //serialPort.writeString("AT+CMGR=12" + "\r");	    
		        
		        TimeUnit.MILLISECONDS.sleep(TD);
		        smsReadString="";
		        Boolean completed=false;
		        
		        while(!completed) {
		        	String rS=serialPort.readString();
		        	smsReadString=smsReadString+rS;
		        	completed=completed(rS);
		        	TimeUnit.MILLISECONDS.sleep(1000);
		        	cnt++;
		        	if(cnt>=25) {completed=true;cnt=0;}
		        	System.out.println("smsReadString count:"+cnt);
		        }
		    	System.out.println("smsReadString completed!");
			    
		    	        
		  } catch (jssc.SerialPortException|InterruptedException e) {
		        System.out.println("From readSMSPdu:"+e);
		        //log.debug("From readSMSPdu:"+e);
		   } /**/
		    close(sp);
		    try {
				readSMSPdu=pDUArray(smsReadString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				readSMSPdu=new JSONArray();
			}
		    //System.out.println("From readSMSPdu:"+readSMSPdu);
		}	/**/
	    return readSMSPdu;//readSMSPdu;
	}
	
	public static String readSMS(int TD, String sp) {
		if(isOpened) {
			System.out.println("Please wait while running!");
			smsReadString="Please wait while running!";
        }else {
        	open(sp);
		try {
			System.out.println("SMSTranceiver.readSMS(TD):"+TD+":"+sp);
	    	serialPort.writeString("AT+CMGF=1" + "\r");
	        TimeUnit.MILLISECONDS.sleep(1000);
	        serialPort.writeString("AT+CMGL=" +ch+"ALL"+ch+ "\r");
	        TimeUnit.MILLISECONDS.sleep(TD);
	        smsReadString="";
	        Boolean completed=false;
	        while(!completed) {
	        	String rS=serialPort.readString();
	        	System.out.println("rS: "+rS);
	        	smsReadString=smsReadString+rS;
	        	completed=completed(rS);
	        	
	        	TimeUnit.MILLISECONDS.sleep(1000);
	        }
	       System.out.println("test: "+smsReadString);
	   /**/ } catch (jssc.SerialPortException|InterruptedException e) {
	        	System.out.println("From readSMS:"+e);/**/
	        	//log.debug("From readSMS:"+e);
	   		}
			close(sp);
        }
		
		return smsReadString;
	}
	public static String readSerial(String sp) {
		String xString="";
		if(isOpened) {
			System.out.println("Please wait while running!");
        }else {
        	open(sp);	
			
			try {
				xString=serialPort.readString();
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//log.debug("From readSerial:"+e);
			}
			close(sp);
		}
		
		return xString;
	}
	public static String CSMS="00";
    public static Boolean sendSMS(String address, String data, String sp)   {
    	System.out.println("From sendSMS data:"+data);    	
    	data=data.replace("\\\\", "\\");
    	float i=(float)data.length()/145;
        int s=0;
        int t=145;
        int p=0;
        if(isOpened) {
        	System.out.println("Please wait while running!");
        }else {	
	    	try {
	    		open(sp);
	    		serialPort.writeString("AT+CMGF=0" + "\r");
		        TimeUnit.MILLISECONDS.sleep(1000);
		   	    
		        CSMS=CSMSconverter();
		        //CSMS="00";
		        if(t>data.length()) {
	        		t=data.length();
	        	}
		        do {
		        	try {
		        		
		        		PDUEncoded pduEnc=PDUConverter.encodeSend(address, data.substring(s, t),(int) Math.ceil(i),p+1,CSMS);
						serialPort.writeString("AT+CMGS=" + pduEnc.getPduLength() + "\r");
				        TimeUnit.MILLISECONDS.sleep(100); 	      
				        serialPort.writeString(pduEnc.getPduEncoded() + ctz);
				        //Pdu pdu=new PduParser().parsePdu(pduEnc.getPduEncoded());//pduEnc.getPduEncoded()"001100098121436587F900000B029B17"
				        //System.out.println(pdu);
				        
						TimeUnit.MILLISECONDS.sleep(5000);/**/
						  
		        	} catch (ParseException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
		        	p++;
		        	s=t;
		        	t=t+145;
		        	if(t>data.length()) {
		        		t=data.length();
		        	}
		        	
		         } while((int) Math.ceil(i)>p);
		        sentData=serialPort.readString();
		        close(sp);
			} catch (InterruptedException | SerialPortException e) {
				// TODO Auto-generated catch block
				System.out.println("From sendSMS:"+e);
				//log.debug("From sendSMS:"+e);
			}
    	}
    	return sent((int)Math.ceil(i), sentData);
    }
    public static void sendSMS2(String address, String data, String sp)   {
    	data=data.replace("\\\\", "\\");
    	System.out.println("From sendSMS2 data:"+data);    	
    	float i=(float)data.length()/145;
        int s=0;
        int t=145;
        int p=0;
      	
	    	try {
	    		
		        CSMS=CSMSconverter();
		        //CSMS="00";Xj
		        if(t>data.length()) {
	        		t=data.length();
	        	}
		        do {
		        	try {
		        		
		        		PDUEncoded pduEnc=PDUConverter.encodeSend(address, data.substring(s, t),(int) Math.ceil(i),p+1,CSMS);
						 //Pdu pdu=new PduParser().parsePdu(pduEnc.getPduEncoded());//pduEnc.getPduEncoded()"001100098121436587F900000B029B17"
				        System.out.println("AT+CMGS=" + pduEnc.getPduLength() + "\r");
				        System.out.println(pduEnc.getPduEncoded() + ctz);
						TimeUnit.MILLISECONDS.sleep(5000);/**/
						  
		        	} catch (ParseException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
		        	p++;
		        	s=t;
		        	t=t+145;
		        	if(t>data.length()) {
		        		t=data.length();
		        	}
		        	
		         } while((int) Math.ceil(i)>p);
		       
			} catch (InterruptedException  e) {
				// TODO Auto-generated catch block
				
			}
    	
    	
    }
       public static Boolean sent(int i, String xString)  {
    	Boolean sent=false;
    	System.out.println((xString));
    	if(xString!=null) {
	    	Scanner scanner = new Scanner(xString);
	    	int count=0;
	        while (scanner.hasNextLine()) {
	          String line = scanner.nextLine();
	          if(line.contains("OK")) { 
	        	 count++;
	          }        
	        }
	         if(i==count-1) {sent=true;}
	         else {System.out.println(xString);}
	        scanner.close();
	        System.out.println(sent);
    	}
    	return sent;
    }
    public static int okCount=0;
    public static int readingCount=0;
    public static Boolean completed(String xString)  {
    	Boolean completed=false;
    	System.out.println("xString: "+xString);
    	if(xString!=null) {
	    	Scanner scanner = new Scanner(xString);
	    	while (scanner.hasNextLine()) {
	          String line = scanner.nextLine();
	         if(line.equals("OK")) { 
	        	 okCount++;
	         }   
	        }
	    	readingCount++;
	         scanner.close();
	         if(okCount>1) {completed=true;okCount=0;}
	         if(readingCount<5) {completed=true;readingCount=0;}
	         System.out.println("readingCount"+":"+readingCount);   
	        System.out.println(completed+":"+okCount);
    	}
    	return completed;
    }
   
    public static void sendSMS2() {
    	try {
	    	serialPort.writeString("AT+CMGF=0" + "\r");
	        TimeUnit.MILLISECONDS.sleep(1000);
	   	   
    		serialPort.writeString("AT+CMGS=130"  + "\r");//154
	        TimeUnit.MILLISECONDS.sleep(100); 	      
	        //serialPort.writeString("0051000C9136798775422000000BA0050003000301986F79B90D4AC3E7F53688FC66BFE5A0799A0E0AB7CB741668FC76CFCB637A995E9783C2E4343C3D4F8FD3EE33A8CC4ED359A079990C22BF41E5747DDE7E9341F4721BFE9683D2EE719A9C26D7DD74509D0E6287C56F791954A683C86FF65B5E06B5C36777181466A7E3F5B00B54A583CAEE741B142683DA6977BA0DB297DDE9709B058AD7D3" + ctz);
	        serialPort.writeString(smsPdu + ctz);
			TimeUnit.MILLISECONDS.sleep(5000);
			
			/**serialPort.writeString("AT+CMGS=154"  + "\r");
	        TimeUnit.MILLISECONDS.sleep(100); 	      
	        serialPort.writeString("0051000C9136798775422000000BA0050003000302E620F77B4E97D7C9A032BE2C1FA7E9617AFAED06D5D9EC707BFC06B1C3E2B73C3D07B9D3F334A84E0785D9E9783D0D0795F1A07218347EB7DB6FF21B347EBBE7E5783D4C778188F5F41C14AED3CBA0B4BC2E2F83C86FF65B0E4ABB41F2325C5E4697DDE4B23C4D07A5DD20FB9B5D87D3C3F432C85E66A7E9A0F27C5E068DD36C76BD0D22BFD9" + ctz);
			TimeUnit.MILLISECONDS.sleep(5000);
			
			serialPort.writeString("AT+CMGS=143"  + "\r");
	        TimeUnit.MILLISECONDS.sleep(100); 	      
	        serialPort.writeString("0051000C9136798775422000000B93050003000303DEF232A85C0799EBE774980E72D7D9EC30081E96A7C3F4BADC052AE2C76538BD5C9783E669371DF41E8FC3E571980E1AD7E16972981EA683DC6F37082E7FA7C965379D059AD7DD7450DA0D1AD7D9F030285E4F83DE66737A9C0E83C8E579595E77D341ED379B9DA683C2EE741B942683CA733A881D16BFE5F5B60B" + ctz);
			TimeUnit.MILLISECONDS.sleep(5000);**/
	        
	        	
	        System.out.println(serialPort.readString());			
			//serialPort.closePort();//Close serial port  
	        
	   /**/ } catch (jssc.SerialPortException e) {
	        System.out.println(e);/**/
	    
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    public static JSONArray jsonArray;
	public static String PDUtoText(String resultMessage) {
			Pdu pdu = new PduParser().parsePdu(resultMessage);
			System.out.println("PDU:"+pdu);	       
			 byte[] bytes = pdu.getUserDataAsBytes();
	        //System.out.println("Address:"+pdu.getAddress());
	        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm ss's' a");
	        //System.out.println("Timestamp:"+ df.format(pdu.getSmscTimeStamp().getTime()));
	       
	      // System.out.println("PDU:"+SmsDeliveryPdu);
		     
	        String decodedMessage = null;
	        int dataCodingScheme = pdu.getDataCodingScheme();
	        if (dataCodingScheme == PduUtils.DCS_ENCODING_7BIT) {
	            decodedMessage = PduUtils.decode7bitEncoding(null, bytes);
	            //.out.println("7bit");
	        } else if (dataCodingScheme == PduUtils.DCS_ENCODING_8BIT) {
	            decodedMessage = PduUtils.decode8bitEncoding(null, bytes);
	            //System.out.println("8bit");
	        } else if (dataCodingScheme == PduUtils.DCS_ENCODING_UCS2) {
	            decodedMessage = PduUtils.decodeUcs2Encoding(null, bytes);
	            //System.out.println("ucs2");
	        } else {
	            System.out.println("Unknown DataCodingScheme!");
	           
	        }
			return decodedMessage;
		}
	   static String formatTimestamp(Calendar timestamp)
		{
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("EEE dd-MMM-yyyy HH:mm:ss z");
			sdf.setTimeZone(timestamp.getTimeZone());
			return sdf.format(timestamp.getTime());
		}
	   public static String toHexString(byte[] arrby, int n) {
	        if (arrby == null || arrby.length < 1) {
	            throw new IllegalArgumentException("this byteArray must not be null or empty");
	        }
	        StringBuilder stringBuilder = new StringBuilder(n * 2);
	        for (int i = 0; i < n; ++i) {
	            if ((255 & arrby[i]) < 16) {
	                stringBuilder.append("0");
	            }
	            stringBuilder.append(Integer.toHexString((int)(255 & arrby[i])));
	            if (i == -1 + arrby.length) continue;
	            stringBuilder.append("");
	        }
	        return stringBuilder.toString().toUpperCase();
	    }
	   static String CSMSconverter()
		{
			Date now = Calendar.getInstance().getTime();
			DateFormat df = new SimpleDateFormat("MMddmmss");
			String x=df.format(now);
			
	        String csms = intToHex(Math.round((Float.valueOf(Integer.valueOf(x.substring(4,6))*60+Integer.valueOf(x.substring(6,8)))*250)/3600));
	        //System.out.println(csms);
			return csms;
		}
	   private static String intToHex(int i) {
	        String hex = Integer.toHexString(i);
	        if (hex.length() % 2 != 0) hex = 0 + hex;
	        return hex.toUpperCase();
	    }
	   public static void UpsertSMS(JSONObject jObject) throws JSONException {
		   boolean found = false;
		   int i;int j=0;String UDH1="";String millis1="";String MpSeqNo="0";
		   Boolean isConcat=jObject.getBoolean("isConcat");
		   String data=jObject.getJSONArray("data").get(0).toString();		   
		   String CMGL=jObject.getJSONArray("CMGL").get(0).toString();
		   if(isConcat) {
			   MpSeqNo=jObject.getJSONArray("MpSeqNo").get(0).toString();
			   UDH1=jObject.getString("UDH");		            
	           millis1=jObject.getJSONArray("millis").get(0).toString();
	       }
		   for (i = 0; i < jsonArray.length(); i++) {
	            JSONObject obj =jsonArray.getJSONObject(i);
	            if(isConcat && obj.getBoolean("isConcat")) {
		            String UDH0=obj.getString("UDH");
		            String millis2=obj.getJSONArray("millis").get(0).toString();  
		            
		            JSONArray millis0=obj.getJSONArray("millis");
		            JSONArray CMGL0=obj.getJSONArray("CMGL");
		            JSONArray MpSeqNo0=obj.getJSONArray("MpSeqNo");
		            JSONArray data0=obj.getJSONArray("data");
		            if(UDH0.equals(UDH1) && Math.abs(Long.valueOf(millis2)-Long.valueOf(millis1))<60000*60) {	
		            	JSONArray jArr=upsertConcat(millis1,CMGL,MpSeqNo,data,millis0,CMGL0,MpSeqNo0,data0);
		                jObject.put("data", jArr.get(3));
		            	jObject.put("MpSeqNo", jArr.get(2));
		                jObject.put("CMGL", jArr.get(1));
		                jObject.put("millis", jArr.get(0));/**/
		            	found=true;j=i;
		            }		            
	            }            
	        }
		   if(found){
			   jsonArray.put(j,jObject);
			    found=false;
           }
		   else{
	        	jsonArray.put(jObject);
	        }	
	    }
	 
	  public static JSONArray upsertConcat(String millis,String CMGL,String MpSeqNo,String data,JSONArray millis0,JSONArray CMGL0,JSONArray MpSeqNo0,JSONArray data0){
		JSONArray millis1=new JSONArray();
		JSONArray CMGL1=new JSONArray();
		JSONArray MpSeqNo1=new JSONArray();
		JSONArray data1=new JSONArray();
		JSONArray ALL=new JSONArray();
		int count=0;
		int xl=MpSeqNo0.length();
		Boolean flag=true;
		if(MpSeqNo0.isEmpty()) {
			millis1.put(millis);
			CMGL1.put(CMGL);
			MpSeqNo1.put(MpSeqNo);
			data1.put(data);
		}else {
			if(xl>0) {
				for (int i=0;i < xl;i++) {
			        if (Integer.valueOf(MpSeqNo) > MpSeqNo0.getInt(i)) {
			        	count++;		        	
			        }
			        if (Integer.valueOf(MpSeqNo) == MpSeqNo0.getInt(i)) {
			        	flag=false;
			        	//System.out.println("test");	        	
			        }
			    }
			}
			for (int i=0;i < xl;i++) {
				if(flag) {
					if(count==i && xl!=1) {
						millis1.put(millis);
						CMGL1.put(CMGL);
						MpSeqNo1.put(MpSeqNo);
						data1.put(data);
					}
					if(count==0 && xl==1) {
						millis1.put(millis);
						CMGL1.put(CMGL);
						MpSeqNo1.put(MpSeqNo);
						data1.put(data);
					}
				}else {
					if(count==i && xl!=1) {						
						CMGL1.put(CMGL);
					}
				}
						millis1.put(millis0.get(i));
						CMGL1.put(CMGL0.get(i));
						MpSeqNo1.put(MpSeqNo0.get(i));
						data1.put(data0.get(i));
				
		    }
			
			if(count==xl) {
				if(flag) {
					millis1.put(millis);
					CMGL1.put(CMGL);
					MpSeqNo1.put(MpSeqNo);
					data1.put(data);
				}else {
					CMGL1.put(CMGL);
				}
			}
		}
		
		ALL.put(millis1);
		ALL.put(CMGL1);
		ALL.put(MpSeqNo1);
		ALL.put(data1);
		//System.out.println(ALL);
		return ALL;
	}
	public static void printSMS(JSONArray arr) throws IllegalAccessException {
		   for (int i = 0; i < arr.length(); i++) {
			   JSONObject obj =arr.getJSONObject(i);
			   Boolean complete=false;
			   if(obj.getInt("MpMaxNo")==obj.getJSONArray("MpSeqNo").length()) {complete=true;}
			   System.out.println(obj.getInt("MpMaxNo")+":"+complete);
			   System.out.println("+"+obj.getString("sender"));//.substring(2,12)
			   System.out.println("CMGL:"+obj.getJSONArray("CMGL"));
			   System.out.println(ArrToString(obj.getJSONArray("data")));
		   }		  
      }
	  public static List<String> CMGList(String str){
		  return Arrays.asList(str.split(","));
	  }
	   public static ArrayList<String> jItems(JSONObject jsob) throws IllegalAccessException {
			ArrayList<String> arr=new ArrayList<String>();
			for (Iterator<String> key = jsob.keys(); key.hasNext();) {
	            String item=key.next();
	            arr.add(item);
	            
	        }
	        return arr;
	    }
	   public static String JArrayString="[{\"CMGL\":\"4,5,6\",\"isConcat\":true,\"MpSeqNo\":\"123\",\"data\":\"abc\",\"sender\":\"639778572402\",\"UDH\":\"05000332\",\"MpMaxNo\":3,\"millis\":\"1671523439832,1671523440833,1671523442833\"}]";
	   public static JSONArray xcv=new JSONArray(JArrayString);
	   public static String readString="AT+CMGL=4\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 1,1,\"\",30\r\n"
	   		+ "0791361907002059040C913679874023540000321051911593230CC8329BFD065DDF72363904\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 2,1,\"\",105\r\n"
	   		+ "0791361907002059600C91361925367429000032106161832323620500039502025C361B0E77B3E136AF984CE69ACD6636986CF35241EB6C36E85DA783DA657A590E32BFE5A071989D16CBC3F4F4DB0D0ABBC9207979FC76BBCB637AFAED06BDCD2077F90E4ACFE7F53219D42ED3CB7210\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 3,1,\"\",159\r\n"
	   		+ "0791361907002059640C91361925367429000032107161436023A0050003A30201DEEDF67CF382E56235996D46BBE564B2192C16BBC56C331AAC269BC162B15BCC36A3C16A9B976A1A66BFF32FD0F0ED9ED7DB65F9E6159BC964B6D98C26B3D5603618EC069BE166395B0C3693C16A33588D16836D5EED32395D6F6F5E2A6639EC4EBBCF20F89B5DDEBC549B976AF3521DCBEEF37B0E9AD241C77ABA2D0EB3419B570CE6B2D96E\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 4,1,\"\",159\r\n"
	   		+ "0791361907002059600C91361925367429000032106161736423A0050003950201DEEDF67CF382E56235996D46BBE564B2192C16B3C56CB31BAD269BC162315BCC36BBD16A9B976A583697E569F71B744C8FDF9B576C2693C966339A4C5683D960B01B6C869BE56C31D84C06ABCD64311A2CB779B5CBE474BDBD79A9DA657A590E1A87D969B13C4C4FBFDDA0CD4BB579A9362FD5941E7681A4EF791874ACA7DBE2301BB479C560\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 5,1,\"\",159\r\n"
	   		+ "0791361907002059600C91361925367429000032106161831323A0050003950201DEEDF67CF382E56235996D46BBE564B2192C16B3C56CB31BAD269BC162315BCC36BBD16A9B976A583697E569F71B744C8FDF9B576C2693C966339A4C5683D960B01B6C869BE56C31D84C06ABCD64311A2CB779B5CBE474BDBD79A9DA657A590E1A87D969B13C4C4FBFDDA0CD4BB579A9362FD5941E7681A4EF791874ACA7DBE2301BB479C560\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 6,1,\"\",60\r\n"
	   		+ "0791361908007403440C913609571200230000321071115573232E0500030F0202A05526B348043DAB54D0F28A04358BD4A214F4025D9354244898643241552734982402\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 7,1,\"\",159\r\n"
	   		+ "0791361907002059640C91361925367429000032107161435523A0050003A30201DEEDF67CF382E56235996D46BBE564B2192C16BBC56C331AAC269BC162B15BCC36A3C16A9B976A1A66BFF32FD0F0ED9ED7DB65F9E6159BC964B6D98C26B3D5603618EC069BE166395B0C3693C16A33588D16836D5EED32395D6F6F5E2A6639EC4EBBCF20F89B5DDEBC549B976AF3521DCBEEF37B0E9AD241C77ABA2D0EB3419B570CE6B2D96E\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 8,1,\"\",152\r\n"
	   		+ "0791361907002059440C9136798740235400003210519144912398050003B90401986F79B90D4AC3E7F53688FC66BFE5A0799A0E0AB7CB741668FC76CFCB637A995E9783C2E4343C3D1FA7DD6750999DA6B340F33219447E83CAE9FABCFD2683E8E536FC2D07A5DDE334394DAEBBE9A03A1DC40E8BDFF232A84C0791DFECB7BC0C6A87CFEE3028CC4EC7EB6117A84A0795DDE936284C06B5D3EE741B642FBBD3\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 9,1,\"\",152\r\n"
	   		+ "0791361907002059440C9136798740235400003210519144422398050003B90402C26D16285E4FCF41EEF79C2EAF9341657C593E4ED3C3F4F4DB0DAAB3D9E1F6F80D6287C56F797A0E72A7E769509D0E0AB3D3F17A1A0E2AE341E53068FC6EB7DFE43768FC76CFCBF17A98EE0211EBE939285CA7974169795D5E0691DFECB71C947683E465B8BC8C2EBBC965799A0E4ABB41F637BB0EA787E96590BDCC4ED341\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 10,1,\"\",152\r\n"
	   		+ "0791361907002059440C9136798740235400003210519144922398050003B90403CAF37919344EB3D9F53688FC66BFE56550B90E32D7CFE9301DE4AEB3D961103C2C4F87E975B90B54C48FCB707AB92E07CDD36E3AE83D1E87CBE3301D34AEC3D3E4303D4C07B9DF6E105CFE4E93CB6E3A0B34AFBBE9A0B41B34AEB3E16150BC9E06BDCDE6F4381D0691CBF3B2BCEEA683DA6F363B4D0785DDE936284D0695E7\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 11,1,\"\",34\r\n"
	   		+ "0791361907002059440C9136798740235400003210519144332311050003B90404E8207658FC96D7DB2E\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 12,1,\"\",75\r\n"
	   		+ "0791361907002059640C913619253674290000321071614355233F050003A302026031D9E61593C95CB3592C269B6D5E2A789DCD7ED7E9A0BABB3E2F9341F037BB0CA297DD6410FD0D1ABFD9EC307C5E268300\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 13,1,\"\",75\r\n"
	   		+ "0791361907002059640C913619253674290000321071615374233F050003A302026031D9E61593C95CB3592C269B6D5E2A789DCD7ED7E9A0BABB3E2F9341F037BB0CA297DD6410FD0D1ABFD9EC307C5E268300\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 14,1,\"\",159\r\n"
	   		+ "0791361907002059640C91361925367429000032107161635023A0050003A50201DEEDF67CF382E56235996D46BBE564B2192C16BBC56C331B8C269BC162B15BCC36B3C1689B976AF876CFEBEDB27CF38ACD6432DB6C4693D96A301B0C7683CD70B39C2D069BC960B5596C4683E536AF76999CAEB7372F55BB4C2FCB372FD5E6A5DABC54C7B2FBFC9E83A674D0B19E6E8BC36CD0E61583B96C36DC2D579B6D5E3199CC359BD166\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 15,1,\"\",35\r\n"
	   		+ "0791361907002059600C91361925367429000032106161647423120500039B020264AED96C6683C9362F15\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 16,1,\"\",159\r\n"
	   		+ "0791361907002059640C91361925367429000032107161636523A0050003A50201DEEDF67CF382E56235996D46BBE564B2192C16BBC56C331B8C269BC162B15BCC36B3C1689B976AF876CFEBEDB27CF38ACD6432DB6C4693D96A301B0C7683CD70B39C2D069BC960B5596C4683E536AF76999CAEB7372F55BB4C2FCB372FD5E6A5DABC54C7B2FBFC9E83A674D0B19E6E8BC36CD0E61583B96C36DC2D579B6D5E3199CC359BD166\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 17,1,\"\",68\r\n"
	   		+ "0791361907002059640C9136192536742900003210716163752338050003A5020262B1CD4B459587DD7373590E0ABBC9A031BB3EA797E569F719F43683DA657A590EA2BF41EEF21D047FB3CB\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 18,1,\"\",68\r\n"
	   		+ "0791361907002059040C9136192536742900003210713230652337ECF7384C4FBFDD9B570CE6B2D97035192EC78AC964AED96C96ABDD649B976C068BC56EB2196C56A36D5EC77ABA2D0EB301\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 19,1,\"\",68\r\n"
	   		+ "0791361908007146440C913639602318760000321071319275233705000330020240737AB9CC06C1D36E50DA3DAFB3C3F4B7DC059297E1ECF0B80C2287DBE1731964AECFCBA0582DDC86BB00\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 20,1,\"\",68\r\n"
	   		+ "0791361907002059040C9136192536742900003210713240252337ECF7384C4FBFDD9B570CE6B2D97035192EC78AC964AED96C96ABDD649B976C068BC56EB2196C56A36D5EC77ABA2D0EB301\r\n"
	   		+ "\r\n"
	   		+ "OK";
	   public static String readString1=""
	   		+ "AT+CMGL=4\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 4,1,\"\",159\r\n"
	   		+ "0791361907002059440C91367987754220000022212270540123A0050003330301986F79B90D4AC3E7F53688FC66BFE5A0799A0E0AB7CB741668FC76CFCB637A995E9783C2E4343C3D4F8FD3EE33A8CC4ED359A079990C22BF41E5747DDE7E9341F4721BFE9683D2EE719A9C26D7DD74509D0E6287C56F791954A683C86FF65B5E06B5C36777181466A7E3F5B00B54A583CAEE741B142683DA6977BA0DB297DDE9709B058AD7D3\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 11,1,\"\",159\r\n"
	   		+ "0791361907002059440C91367987754220000022210261309523A0050003320301986F79B90D4AC3E7F53688FC66BFE5A0799A0E0AB7CB741668FC76CFCB637A995E9783C2E4343C3D1FA7DD6750999DA6B340F33219447E83CAE9FABCFD2683E8E536FC2D07A5DDE334394DAEBBE9A03A1DC40E8BDFF232A84C0791DFECB7BC0C6A87CFEE3028CC4EC7EB6117A84A0795DDE936284C06B5D3EE741B642FBBD3E1360B14AFA7E7\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 12,1,\"\",159\r\n"
	   		+ "0791361907002059400C91367987754220000022210261400023A005000332030240EEF79C2EAF9341657C593E4ED3C3F4F4DB0DAAB3D9E1F6F80D6287C56F797A0E72A7E769509D0E0AB3D3F17A1A0E2AE341E53068FC6EB7DFE43768FC76CFCBF17A98EE0211EBE939285CA7974169795D5E0691DFECB71C947683E465B8BC8C2EBBC965799A0E4ABB41F637BB0EA787E96590BDCC4ED341E5F9BC0C1AA7D9EC7A1B447EB3DF\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 13,1,\"\",147\r\n"
	   		+ "0791361907002059640C9136798775422000002221026140202392050003320303E46550B90E32D7CFE9301DE4AEB3D961103C2C4F87E975B90B54C48FCB707AB92E07CDD36E3AE83D1E87CBE3301D34AEC3D3E4303D4C07B9DF6E105CFE4E93CB6E3A0B34AFBBE9A0B41B34AEB3E16150BC9E06BDCDE6F4381D0691CBF3B2BCEEA683DA6F363B4D0785DDE936284D0695E774103B2C7ECBEB6D17\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 5,1,\"\",159\r\n"
	   		+ "0791361907002059400C91367987754220000022212270541123A0050003330302E620F77B4E97D7C9A032BE2C1FA7E9617AFAED06D5D9EC707BFC06B1C3E2B73C3D07B9D3F334A84E0785D9E9783D0D0795F1A07218347EB7DB6FF21B347EBBE7E5783D4C778188F5F41C14AED3CBA0B4BC2E2F83C86FF65B0E4ABB41F2325C5E4697DDE4B23C4D07A5DD20FB9B5D87D3C3F432C85E66A7E9A0F27C5E068DD36C76BD0D22BFD9\r\n"
	   		+ "\r\n"
	   		+ "+CMGL: 6,1,\"\",148\r\n"
	   		+ "0791361907002059640C9136798775422000002221227054312393050003330303DEF232A85C0799EBE774980E72D7D9EC30081E96A7C3F4BADC052AE2C76538BD5C9783E669371DF41E8FC3E571980E1AD7E16972981EA683DC6F37082E7FA7C965379D059AD7DD7450DA0D1AD7D9F030285E4F83DE66737A9C0E83C8E579595E77D341ED379B9DA683C2EE741B942683CA733A881D16BFE5F5B60B\r\n"
	   		+ "\r\n"
	   		+ "OK\r\n";
	   public static String smstest="omms\\09302395699230220102647230220103000\\*MERLINDA PATNONGON\\2420430400506007148090100200300400\\low\\*METER REPLACEMENT - STUCK UP METER\\*50900343  TG0372 SO. FLORES\\*09661642833\\*\\10.6772626\\122.4001828\\*";
	   public static String dataPdu="EF767BBE79C172B71BAE7693D16032992C2693DD60389A8C0693C96232D90D86A3C960B0CD4B350FB7E1ECF2E6158BC960B1198C0683D5603618EC068BE16039580C0693C16033188C06836D5EE8F419BD79A9362FD5E6A5DABC549B570CE6C2D572325BAE76DBBC6232996B66CBD17239D9E6A502";
	   public static String smsPdu="0051000C9136798775422000000BA2050003BA0201DEEDF67CF3DABC60B9D86D16A3D16831986C0693C170315BAC26C3C96630190C17B3C56A30D8E6B579A9A6C56911244D3E37AFCD4B4693C57233180D06ABC16C30D82D46C3C172B0180C2683C16630180D06DBBC362FF6FBBE796D5E2AE6F30AB23E99D4E0B1088AE1605610D379044D8BC3A79318946641D4A2B49974069920E6F3392D82864F27";
	   public static String smsPdu2="0791361907002059400C91367987754220000022213241258223A00500033A0301";//986F79B90D4AC3E7F53688FC66BFE5A0799A0E0AB7CB741668FC76CFCB637A995E9783C2E4343C3D4F8FD3EE33A8CC4ED359A079990C22BF41E5747DDE7E9341F4721BFE9683D2EE719A9C26D7DD74509D0E6287C56F791954A683C86FF65B5E06B5C36777181466A7E3F5B00B54A583CAEE741B142683DA6977BA0DB297DDE9709B058AD7D3";
	   public static String sentData="AT+CMGF=0\r\n"
	   		+ "\r\n"
	   		+ "OK\r\n"
	   		+ "AT+CMGS=154\r\n"
	   		+ "\r\n"
	   		+ "> 0051000C9136798775422000000BA00500038C0301986F79B90D4AC3E7F53688FC66BFE5A0799A0E0AB7CB741668FC76CFCB637A995E9783C2E4343C3D4F8FD3EE33A8CC4ED359A079990C22BF41E5747DDE7E9341F4721BFE9683D2EE719A9C26D7DD74509D0E6287C56F791954A683C86FF65B5E06B5C36777181466A7E3F5B00B54A583CAEE741B142683DA6977BA0DB297DDE9709B058AD7D3\r\n"
	   		+ "> \r\n"
	   		+ "+CMGS: 241\r\n"
	   		+ "\r\n"
	   		+ "OK\r\n"
	   		+ "AT+CMGS=154\r\n"
	   		+ "\r\n"
	   		+ "> 0051000C9136798775422000000BA00500038C0302E620F77B4E97D7C9A032BE2C1FA7E9617AFAED06D5D9EC707BFC06B1C3E2B73C3D07B9D3F334A84E0785D9E9783D0D0795F1A07218347EB7DB6FF21B347EBBE7E5783D4C778188F5F41C14AED3CBA0B4BC2E2F83C86FF65B0E4ABB41F2325C5E4697DDE4B23C4D07A5DD20FB9B5D87D3C3F432C85E66A7E9A0F27C5E068DD36C76BD0D22BFD9\r\n"
	   		+ "> \r\n"
	   		+ "+CMGS: 242\r\n"
	   		+ "\r\n"
	   		+ "OK\r\n"
	   		+ "AT+CMGS=143\r\n"
	   		+ "\r\n"
	   		+ "> 0051000C9136798775422000000B930500038C0303DEF232A85C0799EBE774980E72D7D9EC30081E96A7C3F4BADC052AE2C76538BD5C9783E669371DF41E8FC3E571980E1AD7E16972981EA683DC6F37082E7FA7C965379D059AD7DD7450DA0D1AD7D9F030285E4F83DE66737A9C0E83C8E579595E77D341ED379B9DA683C2EE741B942683CA733A881D16BFE5F5B60B\r\n"
	   		+ "> \r\n"
	   		+ "+CMGS: 243\r\n"
	   		+ "\r\n"
	   		+ "OK";	
	   
	   
	   
}
