package duson.java.core.codec;

import org.apache.commons.codec.binary.Hex;

public class BytesWapper {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	/**
	 * 实现1
	 * 将字节数组转成十六进制的字段串
	 */
	public static String byte2HexString(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() < 2) {  
				sb.append(0);  
	        }  
			sb.append(hex);
		}
		return sb.toString();
	}

	// 实现2
	public static String toHexString(byte data[])
    {
        if(data == null)
            return null;
        StringBuilder r = new StringBuilder(data.length * 2);
        byte arr$[] = data;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            byte b = arr$[i$];
            r.append(hexDigits[b >> 4 & 15]);
            r.append(hexDigits[b & 15]);
        }

        return r.toString();
    }
	
	/**
	 * 实现3
	 * 字节数组转成十六进制的字段串
	 * Commons-Codec库的实现
	 * @param buf
	 * @return
	 */
	public static String byte2HexStringByCommonsCodec(byte buf[]) {
		return Hex.encodeHexString(buf);
	}
	
	/**
	 * 将二进制转成16进制的字符串，所有字母大写
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString().toUpperCase();
	}
	
	/**
	 * 将字节转换成16进制
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static byte[] long2Byte(long longValue){
		byte[] b = new byte[8];
		for(int i = 0; i < 8; i++){
			b[i] = (byte) (longValue >> 8 * (7 - i) & 0xFF);
		}
		return b;
	}
	
	public static long byte2Long(byte[] b){
		long longValue = 0;
		int nLen = (b.length > 8 ? 8 : b.length);
		for(int i = 0; i < nLen; i++){
			longValue += (b[i] & 0xFF) << (8 * (nLen - 1 - i));
		}
		return longValue;
	}
	
	public static byte[] int2Byte(int intValue){
		byte[] b= new byte[4];
		for(int i = 0; i < 4; i++){
			b[i] = (byte) (intValue >> 8 * (3 - i) & 0xFF);
		}
		return b;
	}
	
	public static int byte2Int(byte[] b) {
		int intValue = 0;
		int nLen = (b.length > 4) ? 4 : b.length;

		for (int i = 0; i < nLen; i++) {
			intValue += (b[i] & 0xFF) << (8 * (3 - i));
		}

		return intValue;
	}

	public static byte[] char2Byte(char chValue) {
		byte[] b = new byte[2];

		b[0] = (byte) ((char) (chValue >> 8) & 0xFF);
		b[1] = (byte) (chValue & 0xFF);

		return b;
	}

	public static char byte2Char(byte[] b) {
		char chValue = 0;

		chValue += (b[0] & 0xFF) << 8;
		chValue += b[1] & 0xFF;

		return chValue;
	}
	
}
