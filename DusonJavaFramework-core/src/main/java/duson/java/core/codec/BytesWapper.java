package duson.java.core.codec;

import org.apache.commons.codec.binary.Hex;

public class BytesWapper {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	/**
	 * 将字节数组转成十六进制的字段串
	 */
	public static String byte2HexString(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}
	
	/**
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

}
