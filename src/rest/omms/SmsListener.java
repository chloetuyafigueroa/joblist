package rest.omms;
import jssc.SerialPort;
import jssc.SerialPortException;
public class SmsListener {

    private SerialPort serialPort;
    private volatile boolean listening = true; // Flag to control listening

    public SmsListener(String portName) {
        serialPort = new SerialPort(portName);
    }

    public void connect() throws SerialPortException {
        serialPort.openPort();
        serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
                              SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        // Set SMS mode to text
        serialPort.writeString("AT+CMGF=1\r");
        delay(1000);
        // Set module to send notifications for new messages
        serialPort.writeString("AT+CNMI=2,1,0,0,0\r");
        delay(1000);
    }

    public void disconnect() throws SerialPortException {
        serialPort.closePort();
    }

    public void sendSms(String phoneNumber, String message) throws SerialPortException {
        serialPort.writeString("AT+CMGF=1\r");
        delay(1000);

        serialPort.writeString("AT+CMGS=\"" + phoneNumber + "\"\r");
        delay(1000);

        serialPort.writeString(message + "\r");
        delay(1000);

        serialPort.writeByte((byte) 26); // Ctrl+Z
        delay(3000); // Wait for the message to be sent
    }

    public String readSms() throws SerialPortException {
        serialPort.writeString("AT+CMGF=1\r");
        delay(1000);

        serialPort.writeString("AT+CMGL=\"ALL\"\r");
        delay(3000);

        return serialPort.readString();
    }

    public void deleteSms(int index) throws SerialPortException {
        serialPort.writeString("AT+CMGD=" + index + ",0\r");
        delay(1000);
    }

    public void startListening() {
    	System.out.println("Start listening...");
        Thread listenerThread = new Thread(() -> {
            while (listening) {
            	System.out.println("...");
                try {
                    String response = serialPort.readString();
                    System.out.println(response);
                    if (response != null && response.contains("+CMT:")) {
                        // Process incoming SMS
                        System.out.println("Incoming SMS: " + response);
                    }
                    delay(500); // Poll every 500 milliseconds
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        });
        listenerThread.setDaemon(true); // Allow application to exit while thread runs
        listenerThread.start();
    }

    public void stopListening() {
        listening = false;
    }

    private void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void runSMSListening() {
    	SmsListener smsListener = new SmsListener("COM4"); // Replace with your port
        try {
            smsListener.connect();
            smsListener.startListening();

            // Simulate some operations
            Thread.sleep(20000); // Listen for 20 seconds

            // Stop listening and perform other operations
            smsListener.stopListening();
            System.out.println("Stopped listening for SMS.");

            // Example to send an SMS
            smsListener.sendSms("+1234567890", "Hello from Java!");

            // Example to read and delete SMS
            String messages = smsListener.readSms();
            System.out.println("Messages: " + messages);

            // Example to delete the first SMS (index 1)
            smsListener.deleteSms(1);

        } catch (SerialPortException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                smsListener.disconnect();
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
    	runSMSListening();
    }
}
