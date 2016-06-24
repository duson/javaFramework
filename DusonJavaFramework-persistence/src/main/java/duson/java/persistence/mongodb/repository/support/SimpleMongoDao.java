package duson.java.persistence.mongodb.repository.support;

import java.io.Serializable;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;


/**
 * 
 * ExtSimpleMongoRepository的一个更易用的版本,仅通过mongoTemplate和实体的类类型Clazz就可以实例化SimpleMongoRepository
 * 
 * @author KEN
 */
@NoRepositoryBean
public class SimpleMongoDao<T, ID extends Serializable> extends SimpleMongoExtRepository<T, ID>
{
	
	/**
	 * Creates a ew {@link SimpleMongoRepository} for the given  {@link MongoTemplate} and EntityClazz.
	 * 
	 * @param mongoTemplate 
	 * @param entityClazz 
	 */
	public SimpleMongoDao(MongoTemplate mongoTemplate, Class<T> entityClazz) 
	{
		super(	
				new MappingMongoEntityInformation(
					mongoTemplate.getConverter().getMappingContext().getPersistentEntity(entityClazz)
					),
					 mongoTemplate
				);
		
		
	}

}
