package rest.omms;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.jsmpp.bean.OptionalParameter.Int;

import rest.omms.gsm3040.Pdu;

import rest.omms.gsm3040.PduParser;
import rest.omms.gsm3040.PduUtils;

/**
 * Author: Eric Kurzhals <ek@attyh.com>
 * converts text to SMS-PDU. Information about PDU format can be found at http://www.gsm-modem.de/sms-pdu-mode.html
 */
public class PDUConverter {

    public static PDUEncoded encodeReceive(String smsc, String destination, String data) throws ParseException {

        String encSmsc = encodePhoneNumber(smsc);
        String encSmscLength = smscLength(encSmsc);

        String encSmsDeliver = "04";

        String encReceiverLength = destinationNumberLength(destination);
        String encReceiver = encodePhoneNumber(destination);

        String encProtocolEncScheme = "0000";

        String encTimestamp = getEncodedTimestamp();

        String encUserData = encodeMessage(data);
      
        return new PDUEncoded(encSmscLength +encSmsc + encSmsDeliver + encReceiverLength + encReceiver + encProtocolEncScheme + encTimestamp + encUserData,
                (encSmsDeliver.length()
                        + encReceiverLength.length()
                        + encReceiver.length()
                        + encProtocolEncScheme.length()
                        + encTimestamp.length()
                        + encUserData.length()) / 2);
    }
    public static String CSMS="00";
    public static PDUEncoded encodeSend( String destination, String data, int nParts, int p, String csms) throws ParseException {
    	//nParts=2;	
    	String encPDUType = "01";
    	if(nParts>1) {
    		encPDUType="51"	;
    		}
        String encSmsMR = "00";

        String encReceiverLength = "0C";
        String encReceiver = encodePhoneNumber(destination);

        String encProtocolEncScheme = "0000";
        CSMS=csms;
       
        
        String encUserData = stringTo7bitHex2(data,nParts,p);//encodeMessage(data);
        //System.out.println(encUserData);
        	
      
        return new PDUEncoded("00" + encPDUType+encSmsMR + encReceiverLength + encReceiver + encProtocolEncScheme + encUserData,
                (encPDUType.length()+encSmsMR.length()
                        + encReceiverLength.length()
                        + encReceiver.length()
                        + encProtocolEncScheme.length()
                       + encUserData.length()) / 2);
        

    }
    public static PDUEncoded encodeSend2( String destination, String uData, int uL,int nParts, int p, String csms) throws ParseException {
    	//nParts=2;	
    	String encPDUType = "01";
    	if(nParts>1) {
    		encPDUType="51"	;
    		}
        String encSmsMR = "00";

        String encReceiverLength = "0C";
        String encReceiver = encodePhoneNumber(destination);

        String encProtocolEncScheme = "0000";
        CSMS=csms;
       
        
        String encUserData = stringTo7bitHex(uData,uL,nParts,p);//encodeMessage(data);
        //System.out.println(encUserData);
        	
      
        return new PDUEncoded("00" + encPDUType+encSmsMR + encReceiverLength + encReceiver + encProtocolEncScheme + encUserData,
                (encPDUType.length()+encSmsMR.length()
                        + encReceiverLength.length()
                        + encReceiver.length()
                        + encProtocolEncScheme.length()
                       + encUserData.length()) / 2);
        

    }
    /**
     * encodes a text-message into 7bit PDU format.
     *
     * @param message
     * @return
     * @throws IllegalArgumentException
     */
    public static byte[] padding(byte[] source) {
		byte[] result = new byte[source.length];
		for (int i = (source.length -1); i >= 0; i--) {
			result[i] = (byte)(source[i]<<1);
			if (i > 0) {
				result[i] = (byte)(result[i]|((source[i-1]&0x80)>>7));
			}
		}
	return result;
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
    private static  byte[] hexStringToByteArray(String string2) {
        int n = string2.length();
       // System.out.println(string2);
        byte[] arrby = new byte[n / 2];
        for (int i = 0; i < n; i += 2) {
            arrby[i / 2] = (byte)((Character.digit((char)string2.charAt(i), (int)16) << 4) + Character.digit((char)string2.charAt(i + 1), (int)16));
        }
   
        return arrby;
    }
  
    public static String stringTo7bitHex(String encUserData, int uL,int nParts,int p) {
    	String encHD = "";
        String encHDl = "";
        int hdl=0;
        if(nParts>1) {
    		encHD="050003"+CSMS+"0"+nParts+"0"+p;//"05000332"
    		hdl=7;
    		encHDl="0B";
    		//hexStringToByteArray(encUserData);
			encUserData = toHexString(padding(hexStringToByteArray(encUserData)),padding(hexStringToByteArray(encUserData)).length);

    	}
        String udLength = Integer.toHexString(uL+hdl).toUpperCase();
        //System.out.println(udLength);
        if (udLength.length() < 2) {
            udLength = "0" + udLength;
        }
        return encHDl+udLength+encHD+encUserData;
    }
    
    public static String stringTo7bitHex2(String text, int nParts,int p) {
        String encHD = "";
        String encHDl = "";
        String encUserData = PduUtils.stringToPdu(text);
        int hdl=0;
        if(nParts>1) {
    		encHD="050003"+CSMS+"0"+nParts+"0"+p;//"05000332"
    		hdl=7;
    		encHDl="0B";
    		//hexStringToByteArray(encUserData);
			encUserData = toHexString(padding(hexStringToByteArray(encUserData)),padding(hexStringToByteArray(encUserData)).length);

    	}
        String udLength = Integer.toHexString(PduUtils.stringToUnencodedSeptets(text).length+hdl).toUpperCase();
        //System.out.println(udLength);
        if (udLength.length() < 2) {
            udLength = "0" + udLength;
        }
        return encHDl+udLength+encHD+encUserData;
    }

    private static String intToHex(int i) {
        String hex = Integer.toHexString(i);
        if (hex.length() % 2 != 0) hex = 0 + hex;
        return hex.toUpperCase();
    }
    //////
    private static String getBinaryInt(String s) {
	    byte[] bytes = s.getBytes();
	    String binary = "";
	    for (byte b : bytes) {
	        int val = b;
	        for (int i = 0; i < 7; i++) {
	            binary = binary+((val & 64) == 0 ? 0 : 1);
	            val <<= 1;
	        }
	    }
	    return binary;
	}
    private static int binaryToDecimal(long binary){
    	int decimalNumber = 0, i = 0;
    	while (binary > 0) {
    		decimalNumber
                += Math.pow(2, i++) * (binary % 10);
    		binary /= 10;
        }
     return decimalNumber;
    }
   ///
    public static String encodeMessage2(String message) {
    	 long bint=Long.valueOf(getBinaryInt(message));
    	 return intToHex(binaryToDecimal(bint));
    }
    public static String encodeMessage(String message) throws IllegalArgumentException {
        if (message.length() < 1)
            throw new IllegalArgumentException();
        Alphabet alphabet = new Alphabet();

        int[] z = new int[message.length()];
        for (int x = 0; x < message.length(); x++) {
            z[x] = alphabet.get(message.charAt(x));
        }

        int[] ez = new int[(int) (Math.ceil(((double) 7) * z.length / 8))];
        int i = 0;
        int i_shift = 0;
        int[] bit_ands = {0x1, 0x3, 0x7, 0xF, 0x1F, 0x3F, 0x7F};

        for (int x = 0; x < ez.length; x++) {
            if (x % 7 == 0) {
                i_shift = 0;
                if ((i + 1) < z.length) {
                    i += (i > 0) ? 1 : 0;
                    if ((i + 1) < z.length)
                        ez[x] = (z[i] >> 0 | ((z[++i] & bit_ands[(i_shift)]) << 7 - i_shift++));
                } else {
                    ez[x] = (z[i] >> 0);
                    break;
                }
            } else if ((i + 1) < z.length) {
                ez[x] = (z[i] >> (i_shift) | ((z[++i] & bit_ands[(i_shift)]) << (7 - i_shift++)));
            } else {

                ez[x] = (z[i] >> (i_shift));
                break;
            }
        }

        String output = "";

        for (int x = 0; (x + 1) < ez.length; x++) {
            output += Integer.toHexString(ez[x]);
        }

        if (ez[ez.length - 1] < 10) {
            output += Integer.toHexString(ez[ez.length - 1]);
        } else {
            output += Integer.toHexString(ez[ez.length - 1]);
        }

        String udLength = Integer.toHexString(message.length());

        if (udLength.length() < 2) {
            udLength = "0" + udLength;
        }

        return (udLength + output).toUpperCase();
    }

    /**
     * encodes a phonenumber into PDU phonenumber to send via AT commands
     *
     * @param phoneNumber
     * @return
     */
    
    public static String encodePhoneNumber(String phoneNumber) {
    	if (phoneNumber.length() == 11) {
    		phoneNumber="+63"+phoneNumber.substring(1,phoneNumber.length());
    	}
    	if (phoneNumber.length() == 12) {
    		phoneNumber="+"+phoneNumber;
    	}
        if (phoneNumber.length() < 1) {
            throw new IllegalArgumentException();
        }
        PhoneNumberType numberType;
        if (phoneNumber.charAt(0) == '+') {
            numberType = PhoneNumberType.INTERNATIONAL;
            phoneNumber = phoneNumber.substring(1); // remove leading +
        } else {
            numberType = PhoneNumberType.ISDN;
        }

        phoneNumber = phoneNumber.replace("[^\\d]", "");
        char tmp;
        char[] pn = phoneNumber.toCharArray();

        for (int x = 0; (x + 1) < pn.length; x += 2) {
            tmp = pn[x];
            pn[x] = pn[(x + 1)];
            pn[x + 1] = tmp;
        }

        // adding trailing F if needed.
        if (pn.length % 2 != 0) {
            tmp = pn[pn.length - 1];
            pn[pn.length - 1] = 'f';
            phoneNumber = new String(pn) + tmp;
        } else {
            phoneNumber = new String(pn);
        }

        return (numberType.getValue() + phoneNumber).toUpperCase();
    }

    /**
     * returns the (hex)-length of encoded SMSC
     *
     * @param enc_smsc
     * @return
     */
    private static String smscLength(String enc_smsc) {
        return stringHexLength(enc_smsc.length() / 2);
    }

    private static String destinationNumberLength(String number) {
        number = number.replace("[^\\d]", "");
        return stringHexLength(number.length());
    }

    /**
     * returns the length of given length l as hexvalue String.
     *
     * @param l
     * @return
     */
    private static String stringHexLength(int l) {
        String length = Integer.toHexString(l);
        if (length.length() < 2)
            length = "0" + length;

        return length;
    }

    /**
     * encodes the message timestamp
     *
     * @return
     * @throws ParseException 
     */
    private static String getEncodedTimestamp() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyMMdd");
        DateFormat dt = new SimpleDateFormat("hh:mm:ss aa");
        Date now = Calendar.getInstance().getTime();
        DateFormat test=new SimpleDateFormat("mm/dd/yyyy hh:mm:ss");
       // Date date1=test.parse("");
        String date = df.format(now);
        String time = dt.format(now);

        char[] encoded = new char[14];

        encoded[0] = date.charAt(1);
        encoded[1] = date.charAt(0);
        encoded[2] = date.charAt(3);
        encoded[3] = date.charAt(2);
        encoded[4] = date.charAt(5);
        encoded[5] = date.charAt(4);

        encoded[6] = time.charAt(1);
        encoded[7] = time.charAt(0);
        encoded[8] = time.charAt(4);
        encoded[9] = time.charAt(3);
        encoded[10] = time.charAt(7);
        encoded[11] = time.charAt(6);

        int offsetMinutes = TimeZone.getDefault().getOffset(new Date().getTime()) / 1000 / 60;

        if (TimeZone.getDefault().inDaylightTime(new Date())) {
            if (offsetMinutes > 0)
                offsetMinutes -= 60;
            else
                offsetMinutes += 60;
        }

        offsetMinutes /= 15;
        if (offsetMinutes < 0) {
            offsetMinutes *= -1;
            offsetMinutes |= 0x80;
        }

        String tz = Integer.toHexString(offsetMinutes);
        if (tz.length() == 2)
            encoded[12] = tz.charAt(1);
        else
            encoded[12] = '0';

        encoded[13] = tz.charAt(0);

        return new String(encoded);
    }

    /**
     * Phone Number Type, international (with leading '+', or national like 0176...)
     */
    private enum PhoneNumberType {
        INTERNATIONAL("91"),
        ISDN("21");
        private final String value;

        private PhoneNumberType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Alphabet to Byte mapping
     */
    private static class Alphabet {
        private HashMap<Character, Byte> alphabet;

        public Alphabet() {
            alphabet = new HashMap<Character, Byte>();
            alphabet.put('£', (byte) 1);
            alphabet.put('$', (byte) 2);
            alphabet.put('¥', (byte) 3);
            alphabet.put('è', (byte) 4);
            alphabet.put('é', (byte) 5);
            alphabet.put('ù', (byte) 6);
            alphabet.put('ì', (byte) 7);
            alphabet.put('ò', (byte) 8);
            alphabet.put('Ç', (byte) 9);
            alphabet.put('Ø', (byte) 11);
            alphabet.put('ø', (byte) 12);
            alphabet.put('Å', (byte) 14);
            alphabet.put('å', (byte) 15);
            alphabet.put('?', (byte) 16);
            alphabet.put('_', (byte) 17);
            alphabet.put('?', (byte) 18);
            alphabet.put('?', (byte) 19);
            alphabet.put('?', (byte) 20);
            alphabet.put('?', (byte) 21);
            alphabet.put('?', (byte) 22);
            alphabet.put('?', (byte) 23);
            alphabet.put('?', (byte) 24);
            alphabet.put('?', (byte) 25);
            alphabet.put('?', (byte) 26);
            alphabet.put('^', (byte) 20);
            alphabet.put('{', (byte) 40);
            alphabet.put('}', (byte) 41);
            alphabet.put('\\', (byte) 47);
            alphabet.put('[', (byte) 60);
            alphabet.put('~', (byte) 61);
            alphabet.put(']', (byte) 62);
            alphabet.put('|', (byte) 64);
            alphabet.put('€', (byte) 101);
            alphabet.put('Æ', (byte) 28);
            alphabet.put('æ', (byte) 29);
            alphabet.put('ß', (byte) 30);
            alphabet.put('É', (byte) 31);
            alphabet.put(' ', (byte) 32);
            alphabet.put('!', (byte) 33);
            alphabet.put('"', (byte) 34);
            alphabet.put('#', (byte) 35);
            alphabet.put('¤', (byte) 36);
            alphabet.put('%', (byte) 37);
            alphabet.put('&', (byte) 38);
            alphabet.put('\'', (byte) 39);
            alphabet.put('(', (byte) 40);
            alphabet.put(')', (byte) 41);
            alphabet.put('*', (byte) 42);
            alphabet.put('+', (byte) 43);
            alphabet.put(',', (byte) 44);
            alphabet.put('-', (byte) 45);
            alphabet.put('.', (byte) 46);
            alphabet.put('/', (byte) 47);
            alphabet.put('0', (byte) 48);
            alphabet.put('1', (byte) 49);
            alphabet.put('2', (byte) 50);
            alphabet.put('3', (byte) 51);
            alphabet.put('4', (byte) 52);
            alphabet.put('5', (byte) 53);
            alphabet.put('6', (byte) 54);
            alphabet.put('7', (byte) 55);
            alphabet.put('8', (byte) 56);
            alphabet.put('9', (byte) 57);
            alphabet.put(':', (byte) 58);
            alphabet.put(';', (byte) 59);
            alphabet.put('<', (byte) 60);
            alphabet.put('=', (byte) 61);
            alphabet.put('>', (byte) 62);
            alphabet.put('?', (byte) 63);
            alphabet.put('¡', (byte) 64);
            alphabet.put('A', (byte) 65);
            alphabet.put('B', (byte) 66);
            alphabet.put('C', (byte) 67);
            alphabet.put('D', (byte) 68);
            alphabet.put('E', (byte) 69);
            alphabet.put('F', (byte) 70);
            alphabet.put('G', (byte) 71);
            alphabet.put('H', (byte) 72);
            alphabet.put('I', (byte) 73);
            alphabet.put('J', (byte) 74);
            alphabet.put('K', (byte) 75);
            alphabet.put('L', (byte) 76);
            alphabet.put('M', (byte) 77);
            alphabet.put('N', (byte) 78);
            alphabet.put('O', (byte) 79);
            alphabet.put('P', (byte) 80);
            alphabet.put('Q', (byte) 81);
            alphabet.put('R', (byte) 82);
            alphabet.put('S', (byte) 83);
            alphabet.put('T', (byte) 84);
            alphabet.put('U', (byte) 85);
            alphabet.put('V', (byte) 86);
            alphabet.put('W', (byte) 87);
            alphabet.put('X', (byte) 88);
            alphabet.put('Y', (byte) 89);
            alphabet.put('Z', (byte) 90);
            alphabet.put('Ä', (byte) 91);
            alphabet.put('Ö', (byte) 92);
            alphabet.put('Ñ', (byte) 93);
            alphabet.put('Ü', (byte) 94);
            alphabet.put('§', (byte) 95);
            alphabet.put('¿', (byte) 96);
            alphabet.put('a', (byte) 97);
            alphabet.put('b', (byte) 98);
            alphabet.put('c', (byte) 99);
            alphabet.put('d', (byte) 100);
            alphabet.put('e', (byte) 101);
            alphabet.put('f', (byte) 102);
            alphabet.put('g', (byte) 103);
            alphabet.put('h', (byte) 104);
            alphabet.put('i', (byte) 105);
            alphabet.put('j', (byte) 106);
            alphabet.put('k', (byte) 107);
            alphabet.put('l', (byte) 108);
            alphabet.put('m', (byte) 109);
            alphabet.put('n', (byte) 110);
            alphabet.put('o', (byte) 111);
            alphabet.put('p', (byte) 112);
            alphabet.put('q', (byte) 113);
            alphabet.put('r', (byte) 114);
            alphabet.put('s', (byte) 115);
            alphabet.put('t', (byte) 116);
            alphabet.put('u', (byte) 117);
            alphabet.put('v', (byte) 118);
            alphabet.put('w', (byte) 119);
            alphabet.put('x', (byte) 120);
            alphabet.put('y', (byte) 121);
            alphabet.put('z', (byte) 122);
            alphabet.put('ä', (byte) 123);
            alphabet.put('ö', (byte) 124);
            alphabet.put('ñ', (byte) 125);
            alphabet.put('ü', (byte) 126);
            alphabet.put('à', (byte) 127);
        }

        public byte get(Character k) {
            return alphabet.get(k);
        }
    }

    public static class PDUEncoded {
        private String pduEncoded, length;
        static String ctz= Character.toString ((char) 26)+ "\r";
        public PDUEncoded(String pduEncoded, String length) {
        	
            this.pduEncoded = pduEncoded;
            this.length = length;
        }

        public PDUEncoded(String pduEncoded, int length) {
        	//System.out.println(pduEncoded);
            this.pduEncoded = pduEncoded;
            this.length = Integer.toString(length);
        }

        public String getPduEncoded() {
            return pduEncoded;
        }

        public String getPduLength() {
            return length;
        }

        public String getPduCommandReceive() {
            return "AT+CMGW=" + length + "\n" +
                    pduEncoded + "\n";
        }
        public String getPduCommandSend() {
            return "AT+CMGS=" + length + "\r\n" +
                    pduEncoded + ctz;
        }
    }
    public static void main(String args[]) {
    	String xString="\\";
    	byte[] bytes = xString.getBytes();
    	bytes=padding(bytes);
    	System.out.println(toHexString(bytes, bytes.length));
    }
}
