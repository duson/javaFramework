package duson.java.persistence.mybatis.tk_mybatis;

import org.apache.ibatis.annotations.SelectProvider;

public interface ExtendMapper<T> {

	@Options(statementType = StatementType.CALLABLE, flushCache = FlushCachePolicy.TRUE, useCache = false) // 调用储存过程
	@SelectProvider(type = ExtendMapperProvider.class, method = "dynamicSQL")
	String extendMethod(String params);
}
