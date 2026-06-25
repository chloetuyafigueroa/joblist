package rest.omms;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;


public class SerialPort
implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TAG = "SerialPort";
    public FileDescriptor mFd;
    public FileInputStream mFileInputStream;
    public FileOutputStream mFileOutputStream;

    static {
        System.loadLibrary((String)"serial_port");
    }

    public SerialPort(File file, int n, int n2, char c, int n3, int n4) throws SecurityException, IOException {
        if (!file.canRead() || !file.canWrite()) {
            try {
                Process process = Runtime.getRuntime().exec("/system/bin/su");
                String string2 = "chmod 666 " + file.getAbsolutePath() + "\nexit\n";
                process.getOutputStream().write(string2.getBytes());
                if (process.waitFor() != 0 || !file.canRead() || !file.canWrite()) {
                    throw new SecurityException();
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new SecurityException();
            }
        }
        this.mFd = SerialPort.open(file.getAbsolutePath(), n, n2, c, n3, n4);
        if (this.mFd == null) {
           // Log.e((String)TAG, (String)"native open returns null");
            throw new IOException();
        }
        this.mFileInputStream = new FileInputStream(this.mFd);
        this.mFileOutputStream = new FileOutputStream(this.mFd);
    }

    public static native int GetPlatform();

    public static native int SetIoState(int var0);

    private static native FileDescriptor open(String var0, int var1, int var2, char var3, int var4, int var5);

    public native void close();

    public InputStream getInputStream() {
        return this.mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return this.mFileOutputStream;
    }
}
