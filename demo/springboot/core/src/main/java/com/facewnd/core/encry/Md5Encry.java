package com.facewnd.core.encry;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Encry {

	public static String md5(String str) throws NoSuchAlgorithmException {
		if (str == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();

		MessageDigest code = MessageDigest.getInstance("MD5");
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
