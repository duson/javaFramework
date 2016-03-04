package duson.java.utils;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * 生成唯一性ID算法的工具类
 */

public class UUIDUtils {

	private static SecureRandom random = new SecureRandom();
	
	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}
	
	public static void main(String[] args) {
		System.out.println(UUIDUtils.uuid());
		System.out.println(UUIDUtils.uuid().length());
		System.out.println(new UUIDUtils().uuid());
	}

}
