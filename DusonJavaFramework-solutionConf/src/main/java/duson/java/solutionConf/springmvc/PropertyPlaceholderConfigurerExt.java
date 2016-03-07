package duson.java.solutionConf.springmvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 资源文件加密字段处理
 * Usage:
 	<bean id="propertyConfigurer" class="*.PropertyPlaceholderConfigurerExt">
		<property name="locations">
			<list>
				<value>classpath*:config.properties</value>
			</list>
		</property>
		<!-- encryptPropNames配置需解密字段名称 -->
		<property name="encryptPropNames">
			<list>
				<value>jdbc.password</value>
			</list>
		</property>
	</bean>
	
	PropertyPlaceholderConfigurerExt.getContextProperty()取得上下文中的properties的值
 */
public class PropertyPlaceholderConfigurerExt extends PropertyPlaceholderConfigurer {
	
	private static Map<String, Object> ctxPropertiesMap;
	
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

	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if (null == encryptPropNames) return propertyValue;
		if (isEncryptProp(propertyName)) {
			String decryptValue = ""; // 解密
			return decryptValue;
		} else {
			return propertyValue;
		}
	}

	@Override  
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)throws BeansException {  
  
        super.processProperties(beanFactory, props);
        
        ctxPropertiesMap = new HashMap<String, Object>();  
        for (Object key : props.keySet()) {  
            String keyStr = key.toString();  
            String value = props.getProperty(keyStr);  
            ctxPropertiesMap.put(keyStr, value);  
        }  
    }  
  
    public static Object getContextProperty(String name) {  
        return ctxPropertiesMap.get(name);  
    }
    
	/**
	 * 判断属性是否为加密的属性
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
