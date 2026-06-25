package rest.omms;

import com.harshadura.gsm.smsdura.GsmModem;

/**
 * @author     : Harsha Siriwardena  <harshadura@gmail.com>
 * @copyrights : www.Durapix.org     <http://www.durapix.org>
 * @license    : GNU GPL v3          <http://www.gnu.org/licenses/>
 *
 * Example on how to simply send a SMS using the smsdura API Wrapper.
 */
public class TestSMS {

    /**private static String port = "COM7"; //Modem Port.
    private static int bitRate = 115200; //this is also optional. leave as it is.
    private static String modemName = ""; //this is optional.
    private static String modemPin = "0000"; //Pin code if any have assigned to the modem.
    private static String SMSC = "+639170000130"; //Message Center Number ex. Mobitel
**/
    public static void main(String[] args) throws Exception {
        //GsmModem gsmModem = new GsmModem();
        //GsmModem.configModem(port, bitRate, modemName, modemPin, SMSC);
        //gsmModem.Sender("+639778572404", "Test Message"); // (tp, msg)
    	System.out.println("hello world!");
    	System.out.println(sample(0));
    }
    static int sample(Integer i) {
    	int x=100;
    	if(i==1) {x=200;}
    	return x;
    }
}

