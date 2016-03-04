package duson.java.core.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import duson.java.core.codec.BytesWapper;
import duson.java.core.codec.HexWapper;

/**
 * 加密码工具类
 */
public class MD5Crypt {
	private static final String MD5 = "MD5";
	
	/**
	 * MD5加密,无密钥
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static String md5Encode(String origin) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] input = origin.getBytes("UTF-8");
		byte[] md5Hash = MD5Encode(input);
		return BytesWapper.byte2HexString(md5Hash);
	}

	/**
	 * MD5加密,密钥
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static String md5Encode(String origin, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		origin = origin + key;
		byte[] input = origin.getBytes("UTF-8");
		byte[] md5Hash = MD5Encode(input);
		return BytesWapper.byte2HexString(md5Hash);
	}

	public static String encodeByMD5(String str) throws NoSuchAlgorithmException {
		return encode(str, MD5);
	}

	private static byte[] MD5Encode(byte[] origin) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(MD5);
		return md.digest(origin);
	}

	private static String encode(String str, String algorithm) throws NoSuchAlgorithmException {
		if (str == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();

		MessageDigest code = MessageDigest.getInstance(algorithm);
		code.update(str.getBytes());
		byte[] bs = code.digest();
		for (int i = 0; i < bs.length; i++) {
			int v = bs[i] & 0xFF;
			if (v < 16) {
				sb.append(0);
			}
			sb.append(Integer.toHexString(v));
		}

		return sb.toString();
	}
	
}
