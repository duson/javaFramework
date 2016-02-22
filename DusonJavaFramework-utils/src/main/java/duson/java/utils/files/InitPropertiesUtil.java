package duson.java.utils.files;

import java.util.ResourceBundle;

/**
 * 读取init.properties文件工具类
 * 
 */
public class InitPropertiesUtil {

	private static final ResourceBundle bundle = ResourceBundle.getBundle("config");

	/**
	 * 通过键获取值
	 */
	public static final String get(String key) {
		try {
			return bundle.getString(key);
		} catch (Exception e) {
			return null;
		}

	}

}
