package duson.java.solutionConf.springmvc.converter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;

/**
 	<mvc:annotation-driven conversion-service="conversionService">
		<mvc:message-converters register-defaults="true">
			<!-- 启动JSON格式的配置 -->
            <bean class="org.springframework.http.converter.json.MappingJacksonHttpMessageConverter">
		        <property name="objectMapper">
		        	<bean class="com.cnit.pubds.web.springext.converter.CustomObjectMapper" />
		        </property>
            </bean>
		</mvc:message-converters>
	</mvc:annotation-driven>
 *
 */
public class CustomObjectMapper extends ObjectMapper {
	public CustomObjectMapper(){  
        CustomSerializerFactory factory = new CustomSerializerFactory();  
        factory.addGenericMapping(Date.class, new JsonSerializer<Date>(){

			@Override
			public void serialize(Date value, JsonGenerator jsonGenerator,
					SerializerProvider provider) throws IOException,
					JsonProcessingException {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                jsonGenerator.writeString(sdf.format(value)); 
			}  
        });  
        this.setSerializerFactory(factory);  		
    }

}
