package rest.omms;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import rest.omms.SerialPort;
import javax.ws.rs.core.Application;



public class SerialHandler
extends Application {
    static final String DEBUG_TAG = "DEBUG_CLASS_APPLICATION";
    static final int RF_OFF = 6;
    static final int RF_ON = 5;
    public static SerialPort serial = null;

    public static SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
    	
    	SerialPort.SetIoState(5);
    	System.out.println("Serial port opened!");
        //Log.e((String)DEBUG_TAG, (String)"Scanner port opened!");
        serial = new SerialPort(new File("/dev/ttyMT1"), 9600, 8, 'E', 1, 0);
        //Log.e((String)DEBUG_TAG, (String)"Serial port opened!");
        
        return serial;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void closeIO() {
        if (serial != null) {
            try {
                SerialHandler.serial.mFileInputStream.close();
                //Log.d((String)DEBUG_TAG, (String)"Input stream closed!");
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
            try {
                SerialHandler.serial.mFileOutputStream.close();
                //Log.d((String)DEBUG_TAG, (String)"Output stream closed!");
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
            SerialHandler.serial.mFd = null;
            serial = null;
        }
    }

    public SerialPort closeSerialPort() {
        SerialPort.SetIoState(6);
        //Log.e((String)DEBUG_TAG, (String)"Scanner port closed!");
        serial.close();
        //Log.e((String)DEBUG_TAG, (String)"Serial port closed!");
        serial = null;
        return null;
    }
}
