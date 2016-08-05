package duson.java.core.configuration;

import duson.java.core.security.AESCrytp;

//import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 资源文件加密字段处理工具类
 */
public class PropertyPlaceholderConfigurerEncry /*extends PropertyPlaceholderConfigurer*/ {

	/**
	 * 需要加密的字段，用于标识那些字段是加过密的
	 */
	private String[] encryptPropNames;

	public String[] getEncryptPropNames() {
		return encryptPropNames;
	}

	public void setEncryptPropNames(String[] encryptPropNames) {
		this.encryptPropNames = encryptPropNames;
	}

	//@Override
	protected String convertProperty(String propertyName, String propertyValue) throws Exception {
		if (null == encryptPropNames)
			return propertyValue;
		if (isEncryptProp(propertyName)) {
			String decryptValue = AESCrytp.aesDecrypt(propertyValue, "密钥");
			return decryptValue;
		} else {
			return propertyValue;
		}
	}

	/**
	 * 判断属性是否为加密的属性
	 * 
	 * @author 彭彩云
	 * @param propertyName
	 * @return [参数说明]
	 * @return boolean [返回类型说明]
	 */
	private boolean isEncryptProp(String propertyName) {
		for (int i = 0; i < encryptPropNames.length; i++) {
			if (encryptPropNames[i].equals(propertyName)) {
				return true;
			}
		}
		return false;
	}
}
