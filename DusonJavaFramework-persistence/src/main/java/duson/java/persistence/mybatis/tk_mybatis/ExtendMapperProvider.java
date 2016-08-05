package duson.java.persistence.mybatis.tk_mybatis;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

public class ExtendMapperProvider extends MapperTemplate {

	public ExtendMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	// 构造Mapper Xml的格式
	public String extendMethod(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		return "";
	}
}
