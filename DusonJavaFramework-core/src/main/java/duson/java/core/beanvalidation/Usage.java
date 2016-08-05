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
	<bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean" />
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
