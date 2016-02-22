package duson.java.solutionConf.springmvc.converter;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 	Http请求时，日期类型的转化，在每个Controller中添加：
    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        dateFormat.setLenient(false);  
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
 *
 */
public class DateEditor extends PropertyEditorSupport {

    private SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && text.trim().length() > 0) {
            try {
                setValue(formater.parse(text));
            } catch (ParseException ex) {
            	ex.printStackTrace();
            }
        }
    }

    @Override
    public String getAsText() {
        return (getValue() == null) ? "" : formater.format(getValue());
    }
}
