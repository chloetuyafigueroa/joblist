package rest.omms;


import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Application;
//import com.fazecast.jSerialComm.SerialPort;

import jssc.SerialPortException;
import jssc.SerialPort;


public class SerialPortEvent extends Application{
	private static String checkCodeSuffix = "160D";
    private static String endChar = "68";
    private static String filler = "FEFEFEFE";
    private static String serialSuffix = "AA";
    private static String startChar = "68";
    
    int numberOfEmptyIds = 0;
    int maxNumberOfAttempts = 5;
    boolean urlSent = false;
    long timeoutInMillis = 10000; // let's say 10000 millis, equivalent to 10 seconds
    Timer timer = null;
    static char ch = '"';

    static String dest = ch + "+639778572402" + ch;
    static String line1 = "AT+CMGF=1\r\n";
    //String line2 = "AT+CMGS=" + dest + "\r\n";
    //String line3 = "hello" + "\r\n";
    static String line2 ="AT+CMGL=ALL"+ "\r\n";
    String line3 ="";
	public static void main(String args[]) throws IOException {
		//FE FE FE FE 68 86 37 08 68 04 32 61 16 0D
		//FE FE FE FE 68 86 42 37 00 08 AA 68 11 04 33 32 33 33 61 16 0D
		final byte[] test = new byte[] { (byte)0xFE, (byte) 0xFE, (byte)0xFE,
			    (byte)0xFE, (byte)0x68, (byte)0x86, (byte)0x42, (byte)0x37, (byte)0x00, (byte)0x08, (byte)0xAA,
			    (byte)0x68, (byte)0x11, (byte)0x04, (byte)0x33, (byte)0x32, (byte)0x33, (byte)0x33, (byte)0x61
			    , (byte)0x16, (byte)0x0D};
		final byte[] sample = new byte[] { (byte) 0x88, (byte)0x01 } ;
		//final byte[] command = { 0x21 };
		
		//String deviceId = "USB\\VID_10C4&PID_EA60&REV_0100"; // Replace with the actual device ID
		//String command = "devcon remove \"" + deviceId + "\"";
		//Runtime.getRuntime().exec(command);
		
		//ScheduledThreadPoolExecutor executor=new ScheduledThreadPoolExecutor(2);
		  //executor.scheduleAtFixedRate(new Runnable() {
			  //public void run() {
				
				  //sendData(sendEnergyCommand(line1));
				  //sendData(sendEnergyCommand(line2));
				  //sendData(sendEnergyCommand("801466816"));
				  sendData(sample);
				  
				  //System.out.println(getMtrno(test));
				  //sendData(sendEnergyCommand("800963457"));
				  //sendData(sendEnergyCommand("800379279"));	
				  //readEnergy("800963457");
				 // readData();
				  //System.out.println(checkCode(startChar + reverseSerial("800963457") + serialSuffix + endChar + "110433323333"));
				  
				  //	}
			  
			 // } , 1, 1000, TimeUnit.MILLISECONDS);
		//FE FE FE FE 68 86 42 37 00 08 AA 68 11 04 33 32 33 33 61 16 0D
		//toHexString("FEFEFEFE685734960008AA6811043332333383160D".getBytes(), 16);
		//System.out.println(Arrays.toString(hexStringToByteArray("FEFEFEFE685734960008AA6811043332333383160D")));
		
		
		
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
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().toUpperCase();
    }
    
    public static String getMtrno(byte[] arrby) {
        if (arrby == null || arrby.length < 1) {
            throw new IllegalArgumentException("this byteArray must not be null or empty");
        }
        StringBuilder stringBuilder = new StringBuilder(10 * 2);
        for (int i = 9; i > 4; --i) {
            if ((255 & arrby[i]) < 16) {
                stringBuilder.append("0");
            }
            stringBuilder.append(Integer.toHexString((int)(255 & arrby[i])));
            if (i == -1 + arrby.length) continue;            
        }
        return stringBuilder.toString().toUpperCase();
    }

  
	public static void readData() {
		
		SerialPort serialPort = new SerialPort("COM9");
		//SerialPort serialPort = new SerialPort("COM5");
	    try {
	        serialPort.openPort();//Open serial port
	        serialPort.setParams(SerialPort.BAUDRATE_9600, 
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_EVEN);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
	        byte[] buffer = serialPort.readBytes(56);//Read 10 bytes from serial port
	        System.out.println(Arrays.toString(buffer));
	        System.out.println(toHexString(buffer,56));
	        System.out.println(displaykWh(toHexString(buffer,56)));
	        
	        serialPort.closePort();//Close serial port
	    }
	    catch (SerialPortException ex) {
	        System.out.println(ex);
	    }
	}
	
	/**
	 * @param bs
	 */
	public static void sendData(byte[] bs) {
		//System.out.println(Arrays.toString(bs));
		//CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		SerialPort serialPort = new SerialPort("COM10");
	    try {
	        serialPort.openPort();//Open serial port
	        serialPort.setParams(SerialPort.BAUDRATE_9600, 
	                             SerialPort.DATABITS_8,
	                             SerialPort.STOPBITS_1,
	                             SerialPort.PARITY_EVEN);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
	        
	        TimeUnit.MILLISECONDS.sleep(100);
	        System.out.println("test");
	        serialPort.writeBytes(new byte[]{0x00, 0x00, 0x00, 0x01}); // Set GPIO0 as SCLK
	        serialPort.writeBytes(new byte[]{0x01, 0x00, 0x00, 0x01}); // Set GPIO1 as MOSI
	        serialPort.writeBytes(new byte[]{0x02, 0x00, 0x00, 0x01}); // Set GPIO2 as MISO
	        serialPort.writeBytes(new byte[]{0x03, 0x00, 0x00, 0x01}); // Set GPIO3 as CS

	        // Delay for a short time to allow the Si4431 module to initialize
	        try {
	            Thread.sleep(100);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        serialPort.writeBytes(new byte[]{(byte) 0x81, (byte)0x01}); // Command to read from a register, address 0x00

	        // Delay for a short time to allow the Si4431 module to process the command and respond
	        try {
	            Thread.sleep(100);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	        
	        //serialPort.writeBytes(bs);//Write data to port
	        //System.out.println(Arrays.toString(bs));
	        //System.out.println(toHexString(bs,21));
	       //serialPort.closePort();//Close serial port 
           // byte[] response = serialPort.readBytes(1);
            //System.out.println("Response: " + response[0]);
            System.out.println(serialPort.readBytes());
			if (serialPort.readBytes()!=null) {
				//System.out.println(Arrays.toString(serialPort.readBytes(21)));
				byte[] buffer = serialPort.readBytes(21);
				System.out.println(toHexString(buffer,21));
				//System.out.print(displaykWh(toHexString(buffer,56)));
				//System.out.println();
			}
					//System.out.println(displaykWh(toHexString(buffer,56)));
			serialPort.closePort();//Close serial port
	        		
					
					
				        
		   
				
	        //System.out.println(Arrays.toString(buffer));
	        
	       
	        
	        //readData();**/
	    }
	    catch (jssc.SerialPortException e) {
	        System.out.println(e);
	    
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String displaykWh(String string2) {
        String string3 = string2.replace((CharSequence)" ", (CharSequence)"");
        int n = string3.indexOf("91");
        if (n > -1) {
            String string4;
            String string5;
            String string6;
            String string7 = string3.substring(n + 18, n + 20);
            String string8 = string3.substring(n + 16, n + 18);
            String string9 = string3.substring(n + 14, n + 16);
            String string10 = string3.substring(n + 12, n + 14);
            int n2 = Integer.parseInt((String)"33", (int)16);
            int n3 = Integer.parseInt((String)string7, (int)16);
            int n4 = Integer.parseInt((String)string8, (int)16);
            int n5 = Integer.parseInt((String)string9, (int)16);
            int n6 = Integer.parseInt((String)string10, (int)16);
            int n7 = n3 - n2;
            int n8 = n4 - n2;
            int n9 = n5 - n2;
            int n10 = n6 - n2;
            String string11 = Integer.toHexString((int)n7);
            if (string11.length() == 1) {
                string11 = "0" + string11;
            }
            if ((string4 = Integer.toHexString((int)n8)).length() == 1) {
                string4 = "0" + string4;
            }
            if ((string5 = Integer.toHexString((int)n9)).length() == 1) {
                string5 = "0" + string5;
            }
            if ((string6 = Integer.toHexString((int)n10)).length() == 1) {
                string6 = "0" + string6;
            }
            String string12 = string11 + string4 + string5 + string6;
            
            return String.valueOf((double)(Double.parseDouble((String)string12) / 100.0));
        }
        return "Incomplete";
    }
	  private static String readEnergy(String string2) {
	        String string3 = reverseSerial(string2);
	        String string4 = checkCode(startChar + string3 + serialSuffix + endChar + "110433323333");
	        //System.out.println("test");
	        return filler +startChar + string3 + serialSuffix + endChar + "110433323333" + string4 + checkCodeSuffix;
	    }
	   public static String reverseSerial(String string2) {
	        if (!string2.startsWith("0")) {
	            string2 = "0" + string2;
	        }
	        int n = -2 + string2.length();
	        String string3 = "";
	        for (int i = 0; i <= -1 + string2.length() / 2; ++i) {
	            string3 = string3 + string2.substring(n, n + 2);
	            n -= 2;
	        }
	        return string3;
	    }
	   public static String checkCode(String string2) {
	        int n = 0;
	        int n2 = 0;
	        for (int i = 0; i <= -1 + string2.length() / 2; ++i) {
	            n= Integer.parseInt((String)string2.substring(n2 , n2 + 2), (int)16)+ n;
	            n2=n2+2;
	            if (n>256) {
	            	n= n-256;
	            	}
	            
	           
	        }
	        String string3 = Integer.toHexString((int)n).toUpperCase();
	        if (string3.length() == 1) {
	            string3 = "0" + string3;
	        }
	        return string3;
	    }
	   private static byte[] hexStringToByteArray(String string2) {
	        int n = string2.length();
	       // System.out.println(string2);
	        byte[] arrby = new byte[n / 2];
	        for (int i = 0; i < n; i += 2) {
	            arrby[i / 2] = (byte)((Character.digit((char)string2.charAt(i), (int)16) << 4) + Character.digit((char)string2.charAt(i + 1), (int)16));
	        }
	   
	        return arrby;
	    }
	   public static byte[] sendEnergyCommand(String string2) {
		  // System.out.println(readEnergy(string2));
	        return hexStringToByteArray(readEnergy(string2));
	    }
}
