package rest.omms;

import java.util.concurrent.TimeUnit;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class MultipartSmsReader implements SerialPortEventListener {
	public static SerialPort serialPort;

    public static void main(String[] args) throws SerialPortException {
        MultipartSmsReader smsReader = new MultipartSmsReader();
        smsReader.initialize("COM3"); // Replace "COM3" with the actual serial port of your modem
        //sendSMS("+639932844568", "Hello, this is a test SMS!");
        //readSMS();
        //serialPort.closePort();
    }
    static char ch = '"';
	static String ctz= Character.toString ((char) 26)+ "\r";//
    public void initialize(String portName) {
        serialPort = new SerialPort(portName);
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
             
            serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
    private static void sendSMS(String phoneNumber, String message) throws SerialPortException {
        // Use AT commands to send SMS
    	try {
    		TimeUnit.MILLISECONDS.sleep(100); 
			serialPort.writeString("AT+CMGF=1\r"); // Set SMS text mode
			TimeUnit.MILLISECONDS.sleep(1000); 
			serialPort.writeString("AT+CMGS=\"" + phoneNumber + "\"\r"); // Send SMS to the given phone number
			TimeUnit.MILLISECONDS.sleep(100); 
			serialPort.writeString(message + "\r"); // SMS message content
			TimeUnit.MILLISECONDS.sleep(100);       
			serialPort.writeString(Character.toString ((char) 26)); // Send Ctrl+Z to indicate the end of the SMS
			TimeUnit.MILLISECONDS.sleep(5000); 
			
		} catch (InterruptedException | SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(serialPort.readString());
    }
    private static void readSMS() throws SerialPortException {
        // Use AT commands to send SMS
    	try {
    		TimeUnit.MILLISECONDS.sleep(100); //+639934444402
			serialPort.writeString("AT+CSCA=\"" + "+639934444402"+ "\"\r"); // Set SMS text mode
			TimeUnit.MILLISECONDS.sleep(100); //09934444400
			serialPort.writeString("AT+CMGF=1\r"); // Set SMS text mode
			TimeUnit.MILLISECONDS.sleep(1000);
	        serialPort.writeString("AT+CMGL=" +ch+"ALL"+ch+ "\r"); // Send SMS to the given phone number
			TimeUnit.MILLISECONDS.sleep(500); 
			
			
		} catch (InterruptedException | SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(serialPort.readString());
    }
    @Override
    public void serialEvent(SerialPortEvent event) {
        //if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                byte[] receivedData = serialPort.readBytes();
                String receivedMessage = new String(receivedData);

                System.out.println("Received message: " + receivedMessage);

                // Add your parsing logic here to handle multipart SMS
                // For multipart SMS, the messages may be split across multiple lines
                // You need to combine the lines to get the complete message.
                // Check for UDH (User Data Header) to detect multipart SMS.

            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        //}
    }
}
