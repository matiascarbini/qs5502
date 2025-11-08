package android.zyapi;



import java.io.ByteArrayOutputStream;

public class Conversion {
	/* 
	 * 16杩涘埗鏁板瓧瀛楃锟�? 
	 */ 
	private static String hexString="0123456789ABCDEF"; 
	/* 
	 * 灏嗗瓧绗︿覆缂栫爜锟�?16杩涘埗鏁板瓧,閫傜敤浜庢墍鏈夊瓧绗︼紙鍖呮嫭涓枃锟�? 
	 */ 
	public static String encode(String str) 
	{ 
		//鏍规嵁榛樿缂栫爜鑾峰彇瀛楄妭鏁扮粍 
		byte[] bytes=str.getBytes(); 
		StringBuilder sb=new StringBuilder(bytes.length*2); 
		//灏嗗瓧鑺傛暟缁勪腑姣忎釜瀛楄妭鎷嗚В锟�?2锟�?16杩涘埗鏁存暟 
		for(int i=0;i<bytes.length;i++) 
		{ 
			sb.append(hexString.charAt((bytes[i]&0xf0)>>4)); 
			sb.append(hexString.charAt((bytes[i]&0x0f)>>0)); 
		} 
		return sb.toString(); 
	} 


	/* 
	 * 锟�?16杩涘埗鏁板瓧瑙ｇ爜鎴愬瓧绗︿覆,閫傜敤浜庢墍鏈夊瓧绗︼紙鍖呮嫭涓枃锟�? 
	 */ 
	public static String decode(String bytes) 
	{ 
		ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2); 
		//灏嗘瘡2锟�?16杩涘埗鏁存暟缁勮鎴愪竴涓瓧锟�? 
		for(int i=0;i<bytes.length();i+=2) 
			baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1)))); 
		return new String(baos.toByteArray()); 
	} 
	/**  
	 * 灏嗕袱涓狝SCII瀛楃鍚堟垚锟�?涓瓧鑺傦紱  
	 * 濡傦細"EF"--> 0xEF  
	 * @param src0 byte  
	 * @param src1 byte  
	 * @return byte  
	 */  
	public static byte uniteBytes(byte src0, byte src1) {   
		byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();   
		_b0 = (byte)(_b0 << 4);   
		byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();   
		byte ret = (byte)(_b0 ^ _b1);   
		return ret;   
	}    
	/**  
	 * 灏嗘寚瀹氬瓧绗︿覆src锛屼互姣忎袱涓瓧绗﹀垎鍓茶浆鎹负16杩涘埗褰㈠紡  
	 * 濡傦細"2B44EFD9" 锟�?> byte[]{0x2B, 0脳44, 0xEF, 0xD9}  
	 * @param src String  
	 * @return byte[]  
	 */  
	public static byte[] HexString2Bytes(String src){   
		byte[] ret = new byte[src.length()/2];   
		byte[] tmp = src.getBytes();   
		for(int i=0; i< tmp.length/2; i++){   
			ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]);   
		}   
		return ret;   
	}   

	/**  
	 * 灏嗘寚瀹歜yte鏁扮粍锟�?16杩涘埗鐨勫舰寮忔墦鍗板埌鎺у埗锟�?  
	 * @param hint String  
	 * @param b byte[]  
	 * @return void  
	 */  
	public static void printHexString(String hint, byte[] b) {   
		System.out.print(hint);   
		for (int i = 0; i < b.length; i++) {   
			String hex = Integer.toHexString(b[i] & 0xFF);   
			if (hex.length() == 1) {   
				hex = '0' + hex;   
			}   
			System.out.print(hex.toUpperCase() + " ");   
		}   
		System.out.println("");   
	}   

	/**  
	 *  灏哹yte鏁扮粍杞崲鎴�16杩涘埗
	 * @param b byte[]  
	 * @return String  
	 */  
	public static String Bytes2HexString(byte[] b) {   
		String ret = "";   
		for (int i = 0; i < b.length; i++) {   
			String hex = Integer.toHexString(b[i] & 0xFF);   
			if (hex.length() == 1) {   
				hex = '0' + hex;   
			}   
			ret += hex.toUpperCase();   
		}   
		return ret;   
	}  


	public static String oneBytes2HexString(byte b) {   
		String ret = "";   
		String hex = Integer.toHexString(b & 0xFF);   
		if (hex.length() == 1) {   
			hex = '0' + hex;   
		}   
		ret += hex.toUpperCase();     
		return ret;   
	}



	/** 
	 * @鍔熻兘: BCD鐮佽浆锟�?10杩涘埗锟�?(闃挎媺浼暟锟�?) 
	 * @鍙傛暟: BCD锟�? 
	 * @缁撴灉: 10杩涘埗锟�? 
	 */  
	public static String bcd2Str(byte[] bytes) {  
		StringBuffer temp = new StringBuffer(bytes.length * 2);  
		for (int i = 0; i < bytes.length; i++) {  
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));  
			temp.append((byte) (bytes[i] & 0x0f));  
		}  
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp  
				.toString().substring(1) : temp.toString();  
	}  

	 //锟街凤拷锟斤拷锟斤拷转锟斤拷为16锟斤拷锟斤拷锟街凤拷
    public static String bytesToHexString(byte[] src) {  
        StringBuilder stringBuilder = new StringBuilder("0x");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        char[] buffer = new char[2];  
        for (int i = 0; i < src.length; i++) {  
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);  
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);  
            //System.out.println(buffer); 
            stringBuilder.append(buffer);  
        }  
        return stringBuilder.toString();  
    }
}

