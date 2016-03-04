package duson.java.core.codec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class HexWapper {
	
	/**
	 * 将十六进制的字符串转成二进制数组
	 * @param hexStr 16进制字符串
	 * @return 二进制数组
	 */
	public static byte[] hexString2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
	
	/**
	 * 十六进制的字符串转成二进制数组
	 * Commons-Codec库的实现
	 * @param hexString
	 * @return
	 * @throws DecoderException
	 */
	public static byte[] hexString2ByteByCommonsCodec(String hexString) throws DecoderException {
		return Hex.decodeHex(hexString.toCharArray());
	}
	
}
