package rest.omms;


import java.io.IOException;
import javax.ws.rs.core.Application;

import com.fazecast.jSerialComm.SerialPort;



public class SerialPortEvent2 extends Application{
	
	public static void main(String args[]) throws IOException {
		 SerialPort port = SerialPort.getCommPort("COM10");
	        port.openPort();

	        // Configure the CP210x device for SPI communication
	        port.writeBytes(new byte[]{0x00, 0x00, 0x00, 0x01}, 4); // Set GPIO0 as SCLK
	        port.writeBytes(new byte[]{0x01, 0x00, 0x00, 0x01}, 4); // Set GPIO1 as MOSI
	        port.writeBytes(new byte[]{0x02, 0x00, 0x00, 0x01}, 4); // Set GPIO2 as MISO
	        port.writeBytes(new byte[]{0x03, 0x00, 0x00, 0x01}, 4); // Set GPIO3 as CS

	        // Delay for a short time to allow the Si4431 module to initialize
	        try {
	            Thread.sleep(100);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	        // Send SPI command to read the configuration from the Si4431 module
	        port.writeBytes(new byte[]{(byte) 0x81, 0x01}, 2); // Command to read from a register, address 0x00

	        // Delay for a short time to allow the Si4431 module to process the command and respond
	        try {
	            Thread.sleep(100);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	        // Read the response from the Si4431 module
	        byte[] response = new byte[2];
	        
	        System.out.println(port.readBytes(response, response.length));
	        // Process and interpret the received data
	        // ...



	        // Close the CP210x device
	        port.closePort();
	}

	
}
