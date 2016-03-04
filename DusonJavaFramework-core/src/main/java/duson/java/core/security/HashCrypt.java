package duson.java.core.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 哈希工具类
 */
public class HashCrypt {

	/**
	 * 散列, 支持md5与sha1算法.
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance(algorithm);

		if (salt != null) {
			digest.update(salt);
		}

		byte[] result = digest.digest(input);

		for (int i = 1; i < iterations; i++) {
			digest.reset();
			result = digest.digest(result);
		}
		return result;
	}

	public static byte[] digest(InputStream input, String algorithm) throws IOException, NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		int bufferLength = 8 * 1024;
		byte[] buffer = new byte[bufferLength];
		int read = input.read(buffer, 0, bufferLength);

		while (read > -1) {
			messageDigest.update(buffer, 0, read);
			read = input.read(buffer, 0, bufferLength);
		}

		return messageDigest.digest();
	}
	
}
