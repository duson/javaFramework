package duson.java.core.json;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.DateFormatDeserializer;

/**
 * FastJson自定义日期格式识别，否则只识别：1.yyyy-MM-dd 2. yyyy/MM/dd
 * 使用：
   ParserConfig config = ParserConfig.getGlobalInstance();
   config.putDeserializer(Date.class, new FastJsonDateFormatDeserializerExt("yyyy_MM_dd"));
   JSON.parseObject(json, Test.class, config, JSON.DEFAULT_PARSER_FEATURE, new Feature[0]); 
 *
 */
public class FastJsonDateFormatDeserializerExt extends DateFormatDeserializer {

    private String format;

    public FastJsonDateFormatDeserializerExt(String format) {
        super();
        this.format = format;
    }

    @Override
    protected <Date> Date cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
        if (format == null) {
            return null;
        }
        if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }

            try {
                return (Date) new SimpleDateFormat(format).parse((String)val);
            } catch (ParseException e) {
                throw new JSONException("parse error");
            }
        }
        throw new JSONException("parse error");
    }
}