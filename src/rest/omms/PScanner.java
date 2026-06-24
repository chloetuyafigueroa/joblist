package rest.omms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ajwcc.pduUtils.gsm3040.PduFactory;
import org.ajwcc.pduUtils.gsm3040.PduGenerator;
import org.ajwcc.pduUtils.gsm3040.SmsDeliveryPdu;
import org.ajwcc.pduUtils.gsm3040.SmsSubmitPdu;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import rest.omms.gsm3040.Pdu;
import rest.omms.gsm3040.PduParser;
import rest.omms.gsm3040.PduUtils;

public class PScanner implements SerialPortEventListener {

    CommPortIdentifier pid = null;
    SerialPort sp;
    BufferedReader input;
    OutputStream output;
   // public static Calendar timeStamp=now();


        static char ch = '"';
        static String dest = ch + "09778572402" + ch;
        String line1 = "AT+CMGF=0\r\n";
        static String line2 = "AT+CMGS=154" + "\r\n";
        //static String line3 = "0011000C913679877542200000FF04F4F29C0E" + "\r\n";
        //String line2 ="AT+CMGR=1"+ "\r\n";
        static String line3 = "0051000C913679877542200000FFA0050003000301986F79B90D4AC3E7F53688FC66BFE5A0799A0E0AB7CB741668FC76CFCB637A995E9783C2E4343C3D4F8FD3EE33A8CC4ED359A079990C22BF41E5747DDE7E9341F4721BFE9683D2EE719A9C26D7DD74509D0E6287C56F791954A683C86FF65B5E06B5C36777181466A7E3F5B00B54A583CAEE741B142683DA6977BA0DB297DDE9709B058AD7D3"+ "\r\n";
        
        static String line4 = "AT+CMGS=154" + "\r\n";
        static String line5 = "0051000C9136798775422000000BA0050003000302E620F77B4E97D7C9A032BE2C1FA7E9617AFAED06D5D9EC707BFC06B1C3E2B73C3D07B9D3F334A84E0785D9E9783D0D0795F1A07218347EB7DB6FF21B347EBBE7E5783D4C778188F5F41C14AED3CBA0B4BC2E2F83C86FF65B0E4ABB41F2325C5E4697DDE4B23C4D07A5DD20FB9B5D87D3C3F432C85E66A7E9A0F27C5E068DD36C76BD0D22BFD9" + "\r\n";
        		
        static String line6 = "AT+CMGS=143" + "\r\n";
        static String line7 = "0051000C9136798775422000000B93050003000303DEF232A85C0799EBE774980E72D7D9EC30081E96A7C3F4BADC052AE2C76538BD5C9783E669371DF41E8FC3E571980E1AD7E16972981EA683DC6F37082E7FA7C965379D059AD7DD7450DA0D1AD7D9F030285E4F83DE66737A9C0E83C8E579595E77D341ED379B9DA683C2EE741B942683CA733A881D16BFE5F5B60B" + "\r\n";
       	
        private byte[] buffer = new byte[1024];
        int data;
        static PScanner pScanner=new PScanner();
        public static void main(String[] args) throws ParseException{
     	   
        	//String message="hello";
  	       //line2=PDUConverter.encodeSend("+639170000130", "+639778572402", message).getPduCommandSend();
     	   //System.out.println(((line7.length()-15)/2)-1);
     	   
     	    pScanner.sendSMS();
        	/**System.out.println(PDUConverter.encodeSend("+639170000130", "+639778572402", message).getPduCommandSend());
        	String pduReceiving="0791198954800710040C9119992051289900000130225103412209CA305A7D5E9FD761";
        	String pduSending="0041000C911999083196770000A0050003000301986F79B90D4AC3E7F53688FC66BFE5A0799A0E0AB7CB741668FC76CFCB637A995E9783C2E4343C3D4F8FD3EE33A8CC4ED359A079990C22BF41E5747DDE7E9341F4721BFE9683D2EE719A9C26D7DD74509D0E6287C56F791954A683C86FF65B5E06B5C36777181466A7E3F5B0AB4A0795DDE936284C06B5D3EE741B642FBBD3E1360B14AFA7E7";
        	Pdu pdu2=new PduParser().parsePdu(pduReceiving);
        	SmsSubmitPdu pdu3=new SmsSubmitPdu();
        	pdu3.setAddress("+639778572402");
        	pdu3.setSmscAddress("+639170000130");
        	pdu3.setDecodedText("So that at the end of this day and at the end &*()");
        	pdu3.setRawPdu(pduSending);
        	//pdu3.setDataBytes(PduUtils.stringToUnencodedSeptets("hello world"));
        	byte[] x=PduUtils.stringToUnencodedSeptets("hello");
        	pdu3.setUDData(PduUtils.unencodedSeptetsToEncodedSeptets(x));
        	PduGenerator pduGenerator=new PduGenerator();
        	//System.out.println(pduGenerator.generatePduString(pdu3));
        	System.out.println(pdu3.getRawPdu());
        	
        	
        	System.out.println(PDUtoText(pduReceiving)); **/
        	//System.out.println(PDUConverter.encodePhoneNumber("+639778572402"));
        	 
        	
          }

       
        public void sendSMS() {
        try {

            Enumeration e = CommPortIdentifier.getPortIdentifiers();
            while (e.hasMoreElements()) {
                CommPortIdentifier cpi = (CommPortIdentifier) e.nextElement();
                
                if (cpi.getName().equals("COM7")) {
                    pid = cpi;
                    break;
                }
            }
            sp = (SerialPort) pid.open(getClass().getName(), 2000);
            
            sp.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            InputStream is = sp.getInputStream();
            input = new BufferedReader(new InputStreamReader(is));
            
            output = sp.getOutputStream();
            output.write(line1.getBytes());
                Thread.sleep(100); 
                output.flush();
                
             output.write(line2.getBytes());
             Thread.sleep(100); 
             output.flush();             
         output.write(line3.getBytes());
            Thread.sleep(500); 
            output.flush();
            output.write(26);
            Thread.sleep(100); 
            output.flush(); 
          /**/ 
            output.write(line4.getBytes());
            Thread.sleep(100); 
            output.flush();
         output.write(line5.getBytes());
            Thread.sleep(500); 
            output.flush();
            output.write(26);
            Thread.sleep(100);
            output.flush();
            
            output.write(line6.getBytes());
            Thread.sleep(100); 
            output.flush();
         output.write(line7.getBytes());
            Thread.sleep(500); 
            output.flush();    
            output.write(26);
            Thread.sleep(100);
           output.flush(); 
             /**/
            System.out.println("complete");
            
            sp.addEventListener(this);
            sp.notifyOnDataAvailable(true);
            sp.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        System.out.println("serialEvent CallBack");
        try{
         if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE){
            String line = "";
            while ((line = input.readLine())!= null) {
                System.out.println(line);
            }
        }   
        }catch(Exception ex){
            ex.printStackTrace();
        }



    }

    public synchronized void close() {
        if (sp != null) {
            System.out.println("not null");
            sp.removeEventListener();
            sp.close();
        }
    }
    
    public static String PDUtoText1(String resultMessage) {
		Pdu pdu = new PduParser().parsePdu(resultMessage);
        byte[] bytes = pdu.getUserDataAsBytes();
        String decodedMessage = null;
        int dataCodingScheme = pdu.getDataCodingScheme();
        if (dataCodingScheme == PduUtils.DCS_ENCODING_7BIT) {
            decodedMessage = PduUtils.decode7bitEncoding(null, bytes);
            System.out.println("7bit");
        } else if (dataCodingScheme == PduUtils.DCS_ENCODING_8BIT) {
            decodedMessage = PduUtils.decode8bitEncoding(null, bytes);
            System.out.println("8bit");
        } else if (dataCodingScheme == PduUtils.DCS_ENCODING_UCS2) {
            decodedMessage = PduUtils.decodeUcs2Encoding(null, bytes);
            System.out.println("ucs2");
        } else {
            System.out.println("Unknown DataCodingScheme!");
           
        }
		return decodedMessage;
	}
    public static String PDUtoText(String resultMessage) {
		Pdu pdu = new PduParser().parsePdu(resultMessage);
		System.out.println("PDU:"+pdu);	       
		 byte[] bytes = pdu.getUserDataAsBytes();
        System.out.println("Address:"+pdu.getAddress());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm ss's' a");
        System.out.println("Timestamp:"+ df.format(pdu.getSmscTimeStamp().getTime()));
       
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
    public static String TexttoPDU(String stringPDU) {		
		return String.valueOf(PduUtils.stringToPdu(stringPDU));
    }
    
}
