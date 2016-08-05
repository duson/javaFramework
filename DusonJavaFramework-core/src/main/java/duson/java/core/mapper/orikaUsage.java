package duson.java.core.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * 对象映射
 * 依赖：orika-core
 *
 */
public class orikaUsage {
	public static void main(String[] args) {
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		MapperFacade mapper = mapperFactory.getMapperFacade();
		
		// T desc = mapper.map(obj, T.class);
	}
}
