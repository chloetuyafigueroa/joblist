package rest.omms;

import java.io.File;

public class PortControl {
    static {
        System.loadLibrary("PortControl");
    }

    public static native boolean enablePort(String portName);
    public static native boolean disablePort(String portName);

    public static void main(String[] args) {
        String portName = "COM1";
        if (enablePort(portName)) {
            System.out.println(portName + " has been enabled.");
        } else {
            System.out.println("Failed to enable " + portName + ".");
        }
        if (disablePort(portName)) {
            System.out.println(portName + " has been disabled.");
        } else {
            System.out.println("Failed to disable " + portName + ".");
        }
    }
}
