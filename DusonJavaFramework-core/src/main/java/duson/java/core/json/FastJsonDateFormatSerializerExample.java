package duson.java.core.json;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;


/**
 * 
 *
 */
public class FastJsonDateFormatSerializerExample {

    // 一种方法是通过注解
    @JSONField (format="yyyy-MM-dd HH:mm:ss")  
    public Date birthday; 

    /** 
     * 默认的处理时间 YYYY-MM-DD HH:mm:ss
     *  
     * @param jsonText 
     * @return 
     */  
    public static String toJSON(Object jsonText) {  
        return JSON.toJSONString(jsonText, SerializerFeature.WriteDateUseDateFormat);  
    }  
  
    /** 
     * 自定义时间格式 
     *  
     * @param jsonText 
     * @return 
     */  
    public static String toJSON(String dateFormat, String jsonText) {  
        SerializeConfig mapping = new SerializeConfig();  
        mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));  
        return JSON.toJSONString(jsonText, mapping);  
    }  
}