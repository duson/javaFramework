package json;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * 
 */
@Component("customJsonMapper")
public class CustomJsonMapper extends ObjectMapper {
	public JsonMapper() {
		this.setSerializationInclusion(Include.NON_NULL);
	}
}
