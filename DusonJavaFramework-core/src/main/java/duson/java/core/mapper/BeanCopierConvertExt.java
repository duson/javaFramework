
package mapper;

import org.apache.commons.lang3.EnumUtils;

import net.sf.cglib.core.Converter;

/**
 	BeanCopier coper = BeanCopier.create(source.class, target.class, true);
	coper.copy(source, desc, new BeanCopierConvertExt());
 * 
 * @version 1.0.0
 * 
 */
public class BeanCopierConvertExt implements Converter {
	/**
	 * value：源对象字段的值
	 * target：目标对象字段的类型
	 * context：目标对象字段的Set方法
	 */
	@Override
	public Object convert(Object value, Class target, Object context) {
		if(value == null) { // 如果源对象为空，使用目标对象的默认值
			try {
				return target.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		if(!value.getClass().equals(target)) { // 如果类型不匹配，使用目标对象的默认值
			try {
				return target.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		if (target.isEnum()) {
			return EnumUtils.getEnum(target, value.toString());
		} else if(value instanceof Enum) {
			return ((Enum)value).name();
		} else if (value instanceof Integer) {
            return (Integer) value;  
        /*} else if (value instanceof Timestamp) {  
            Timestamp date = (Timestamp) value;  
            return sdf.format(date);
        } else if (value instanceof BigDecimal) {  
            BigDecimal bd = (BigDecimal) value;  
            return bd.toPlainString(); */
        } 
		
        return value;
	}
}
