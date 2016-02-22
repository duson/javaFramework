package duson.java.solutionConf.springmvc.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 *     
 	<bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
		<property name="converters">
			<list>
				<bean class="com.*.DateConverter"></bean>
			</list>
		</property>
	</bean>
 *
 */
public class DateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    dateFormat.setLenient(false);  
	    try {
	        return dateFormat.parse(source);  
	    } catch (ParseException e) {  
	        e.printStackTrace();  
	    }         
	    return null; 
	}

}
