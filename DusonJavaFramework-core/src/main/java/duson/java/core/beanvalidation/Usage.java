package duson.java.core.beanvalidation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.constraints.Size;

public class Usage {
	// 错误信息可用I18n文件
	@Size(min=1, max=30, message="{items.name.length.error}", groups={AddGroup.class})
	private String example;
	
	/* 注入
	 * <!-- 配置 JSR303 Bean Validator 定义 -->
	<bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean">
		<property name="providerClass" value="org.hibernate.validator.HibernateValidator"/>  
	    <!-- 如果不加默认到 使用classpath下的 ValidationMessages.properties -->  
	    <property name="validationMessageSource" ref="messageSource"/>  
	</bean>

	<!-- 国际化的消息资源文件（本系统中主要用于显示/错误消息定制） -->  
	<bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource">  
	    <property name="basenames">  
	        <list>  
	            <!-- 在web环境中一定要定位到classpath 否则默认到当前web应用下找  -->  
	            <value>classpath:messages</value>  
	            <value>classpath:org/hibernate/validator/ValidationMessages</value>  
	        </list>  
	    </property>  
	    <property name="useCodeAsDefaultMessage" value="false"/>  
	    <property name="defaultEncoding" value="UTF-8"/>  
	    <property name="cacheSeconds" value="60"/>  
	</bean>

	NotEmpty(message = "{user.name.null}")  
    @Length(min = 5, max = 20, message = "{user.name.length.illegal}")  
    @Pattern(regexp = "[a-zA-Z]{5,20}", message = "{user.name.illegal}")  
    private String name;
    
	 */
	private Validator validator;
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 错误信息列表
	 */
	protected List<String> beanValidator(Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			return list;
		}
		return new ArrayList<String>();
	}
	
	 /* Spring Mvc集成
	  * @RequestMapping("/editItemsSubmit") 
	  *  public void editItemsSubmit(@Valid(value={AddGroup.class}) Usage item, BindingResult result) { 
	  *  	result.hasErrors(); 
	  *  }
	  */
	    
}
