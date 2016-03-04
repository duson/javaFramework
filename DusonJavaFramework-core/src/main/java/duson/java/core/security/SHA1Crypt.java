package duson.java.core.security;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.Validate;

import duson.java.core.codec.BytesWapper;

/**
 * 支持HMAC-SHA1消息签名 及 DES/AES对称加密的工具类.
 * 
 * 支持Hex与Base64两种编码方式
 */
public class SHA1Crypt {

	private static final String HMACSHA1 = "HmacSHA1";
	private static final String SHA1 = "SHA-1";

	private static final int DEFAULT_HMACSHA1_KEYSIZE = 160; // RFC2401
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	/**
	 * 对输入字符串进行sha1散列.
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[] sha1(byte[] input) throws NoSuchAlgorithmException {
		return HashCrypt.digest(input, SHA1, null, 1);
	}

	public static byte[] sha1(byte[] input, byte[] salt) throws NoSuchAlgorithmException {
		return HashCrypt.digest(input, SHA1, salt, 1);
	}

	public static byte[] sha1(byte[] input, byte[] salt, int iterations) throws NoSuchAlgorithmException {
		return HashCrypt.digest(input, SHA1, salt, iterations);
	}
	
	/**
	 * 使用HMAC-SHA1进行消息签名, 返回字节数组,长度为20字节.
	 * 
	 * @param input
	 *            原始输入字符数组
	 * @param key
	 *            HMAC-SHA1密钥
	 * @throws InvalidKeyException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[] hmacSha1(byte[] input, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
		SecretKey secretKey = new SecretKeySpec(key, HMACSHA1);
		Mac mac = Mac.getInstance(HMACSHA1);
		mac.init(secretKey);
		return mac.doFinal(input);
	}

	/**
	 * 校验HMAC-SHA1签名是否正确.
	 * 
	 * @param expected
	 *            已存在的签名
	 * @param input
	 *            原始输入字符串
	 * @param key
	 *            密钥
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public static boolean isMacValid(byte[] expected, byte[] input, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
		byte[] actual = hmacSha1(input, key);
		return Arrays.equals(expected, actual);
	}

	/**
	 * 生成HMAC-SHA1密钥,返回字节数组,长度为160位(20字节). HMAC-SHA1算法对密钥无特殊要求, RFC2401建议最少长度为160位(20字节).
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[] generateHmacSha1Key() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(HMACSHA1);
		keyGenerator.init(DEFAULT_HMACSHA1_KEYSIZE);
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey.getEncoded();
	}

	/**
	 * 生成安全的密钥，生成随机的16位salt并经过1024次 sha-1 hash
	 * @throws NoSuchAlgorithmException 
	 */
	public static String entryptKey(String text) throws NoSuchAlgorithmException {
		byte[] salt = generateSalt(SALT_SIZE);
		byte[] hashPassword = HashCrypt.digest(text.getBytes(), SHA1, salt, HASH_INTERATIONS);
		return BytesWapper.byte2HexStringByCommonsCodec(salt) + BytesWapper.byte2HexStringByCommonsCodec(hashPassword);
	}
	
	/**
	 * 生成随机的Byte[]作为salt.
	 * 
	 * @param numBytes byte数组的大小
	 */
	public static byte[] generateSalt(int numBytes) {
		Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);

		byte[] bytes = new byte[numBytes];
		SecureRandom random = new SecureRandom();
		random.nextBytes(bytes);
		return bytes;
	}

}