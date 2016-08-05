package duson.java.persistence.mybatis.tk_mybatis;

import org.apache.ibatis.annotations.SelectProvider;

public interface ExtendMapper<T> {

	@SelectProvider(type = ExtendMapperProvider.class, method = "dynamicSQL")
	String extendMethod(String params);
}
