package duson.java.persistence.mongodb.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Update.Position;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.util.Assert;

import duson.java.persistence.mongodb.repository.support.MongoExtRepository;


public class SimpleMongoRepositoryEx<T, ID extends Serializable> implements MongoExtRepository<T, ID> {
	private MongoOperations mongoOperations = null;
	private MongoEntityInformation entityInformation = null;
	private Class<T> entityClazz;
	
	public SimpleMongoRepositoryEx(MongoOperations mongoOperations, Class<T> entityClazz) {
		this.mongoOperations = mongoOperations;
		this.entityClazz = entityClazz;
	}
	
    /*public Object save(Object entity)
    {
        Assert.notNull(entity, "Entity must not be null!");
        if(getMongoEntityInformation().isNew(entity))
            mongoOperations.insert(entity, getMongoEntityInformation().getCollectionName());
        else
            mongoOperations.save(entity, getMongoEntityInformation().getCollectionName());
        return entity;
    }*/

	@Override
	public <S extends T> S save(S entity) {
        Assert.notNull(entity, "Entity must not be null!");
        if(getMongoEntityInformation().isNew(entity))
            mongoOperations.insert(entity, getMongoEntityInformation().getCollectionName());
        else
            mongoOperations.save(entity, getMongoEntityInformation().getCollectionName());
        return entity;
	}
	
    public List save(Iterable entities)
    {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        List result = convertIterableToList(entities);
        boolean allNew = true;
        Iterator iterator = entities.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            Object entity = iterator.next();
            if(allNew && !getMongoEntityInformation().isNew(entity))
                allNew = false;
        } while(true);
        if(allNew)
        {
            mongoOperations.insertAll(result);
        } else
        {
            Object entity;
            for(Iterator iterator1 = result.iterator(); iterator1.hasNext(); save((T)entity))
                entity = iterator1.next();

        }
        return result;
    }

    /*public Object findOne(Serializable id)
    {
        Assert.notNull(id, "The given id must not be null!");
        return mongoOperations.findById(id, getMongoEntityInformation().getJavaType(), getMongoEntityInformation().getCollectionName());
    }*/

    private Query getIdQuery(Object id)
    {
        return new Query(getIdCriteria(id));
    }

    private Criteria getIdCriteria(Object id)
    {
        return Criteria.where(getMongoEntityInformation().getIdAttribute()).is(id);
    }

    public boolean exists(Serializable id)
    {
        Assert.notNull(id, "The given id must not be null!");
        return mongoOperations.exists(getIdQuery(id), getMongoEntityInformation().getJavaType(), getMongoEntityInformation().getCollectionName());
    }

    public long count()
    {
        return mongoOperations.getCollection(getMongoEntityInformation().getCollectionName()).count();
    }

    public void delete(Serializable id)
    {
        Assert.notNull(id, "The given id must not be null!");
        mongoOperations.remove(getIdQuery(id), getMongoEntityInformation().getJavaType(), getMongoEntityInformation().getCollectionName());
    }

    public void delete(Object entity)
    {
        Assert.notNull(entity, "The given entity must not be null!");
        delete(getMongoEntityInformation().getId(entity));
    }

    public void delete(Iterable entities)
    {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        Object entity;
        for(Iterator iterator = entities.iterator(); iterator.hasNext(); delete(entity))
            entity = iterator.next();

    }

    public void deleteAll()
    {
        mongoOperations.remove(new Query(), getMongoEntityInformation().getCollectionName());
    }

    public List findAll()
    {
        return findAll(new Query());
    }

    public Iterable findAll(Iterable ids)
    {
        Set parameters = new HashSet(tryDetermineRealSizeOrReturn(ids, 10));
        Serializable id;
        for(Iterator iterator = ids.iterator(); iterator.hasNext(); parameters.add(id))
            id = (Serializable)iterator.next();

        return findAll(new Query((new Criteria(getMongoEntityInformation().getIdAttribute())).in(parameters)));
    }

    public Page findAll(Pageable pageable)
    {
        Long count = Long.valueOf(count());
        List list = findAll((new Query()).with(pageable));
        return new PageImpl(list, pageable, count.longValue());
    }

    public List findAll(Sort sort)
    {
        return findAll((new Query()).with(sort));
    }

    /*public Object insert(Object entity)
    {
        Assert.notNull(entity, "Entity must not be null!");
        mongoOperations.insert(entity, getMongoEntityInformation().getCollectionName());
        return entity;
    }*/

    public List insert(Iterable entities)
    {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        List list = convertIterableToList(entities);
        if(list.isEmpty())
        {
            return list;
        } else
        {
            mongoOperations.insertAll(list);
            return list;
        }
    }

    private static List convertIterableToList(Iterable entities)
    {
        if(entities instanceof List)
            return (List)entities;
        int capacity = tryDetermineRealSizeOrReturn(entities, 10);
        if(capacity == 0 || entities == null)
            return Collections.emptyList();
        List list = new ArrayList(capacity);
        Object entity;
        for(Iterator iterator = entities.iterator(); iterator.hasNext(); list.add(entity))
            entity = iterator.next();

        return list;
    }

    private static int tryDetermineRealSizeOrReturn(Iterable iterable, int defaultSize)
    {
        return iterable != null ? (iterable instanceof Collection) ? ((Collection)iterable).size() : defaultSize : 0;
    }

	private MongoEntityInformation getMongoEntityInformation() {
		if(entityInformation == null) {
			entityInformation = new MappingMongoEntityInformation(
					mongoOperations.getConverter().getMappingContext().getPersistentEntity(entityClazz)
					);
		}
		
		return entityInformation;
	}

	@Override
	public <S extends T> S insert(S entity) {
        Assert.notNull(entity, "Entity must not be null!");
        mongoOperations.insert(entity, getMongoEntityInformation().getCollectionName());
        return entity;
	}
	
	@Override
	public <S extends T> Collection<S> insert(Collection<S> batchToInsert) {
		Assert.notNull(batchToInsert, "The given Collection<S> batchToInsert not be null!");
		
		mongoOperations.insert(batchToInsert, getMongoEntityInformation().getCollectionName());

		return batchToInsert;
	}
	
	@Override
	public T findOne(String key, Object value) {
		Assert.hasText(key, "The given key must not be empty!");
		
		if(value != null)
		{
			return (T) mongoOperations.findOne(	getEqualQuery(key,value).limit(1), getMongoEntityInformation().getJavaType(), getMongoEntityInformation().getCollectionName());
		}
		else	//findOne(key,null) 等价于 key: { $exists: false}
		{
			return (T) mongoOperations.findOne(	getKeyExistsQuery(key,false).limit(1), getMongoEntityInformation().getJavaType(), getMongoEntityInformation().getCollectionName());
		}
	}

	@Override
	public T findOne(Query query) {
		Assert.notNull(query, "The given query must not be null!");
		
		query.limit(1); //反正只要1个结果,所以limit一下,提高性能
		
		return (T) mongoOperations.findOne(query, getMongoEntityInformation().getJavaType(), getMongoEntityInformation().getCollectionName());
	}
	
	@Override
	public T findOne(ID id) {
		return (T) mongoOperations.findById(id, getMongoEntityInformation().getJavaType(), getMongoEntityInformation().getCollectionName());
	}

	@Override
	public List<T> findAll(String key, Object value) {
		Assert.hasText(key, "The given key must not be empty!");

		if(value != null)
		{
			return findAll( getEqualQuery(key,value) );
		}
		else//findAll(KEY,null) 等价于 key: { $exists: false}
		{
			return findAll( getKeyExistsQuery(key,false) );
		}
	}

	@Override
	public List<T> findAll(Query query) {
		if (query == null) {
			return Collections.emptyList();
		}

		return mongoOperations.find(query, getMongoEntityInformation().getJavaType(), getMongoEntityInformation().getCollectionName());
	}

	@Override
	public Page<T> findAll(Criteria criteria, Pageable pageable) {
		Long count = count(criteria);
		
		List<T> list = findAll(query(criteria).with(pageable));

		return new PageImpl<T>(list, pageable, count);
	}

	@Override
	public List<T> findAllNotEqual(String key, Object value) {
		Assert.hasText(key, "The given key must not be empty!");
		Assert.notNull(value, "The given value must not be null!");
		
		return findAll( query( where(key).ne(value) ) );
	}

	@Override
	public long count(String key, Object value) {
		Assert.hasText(key, "The given key must not be empty!");
		
		if(value != null)
		{
			return mongoOperations.count(getEqualQuery(key,value), getMongoEntityInformation().getJavaType());
		}
		else	//count(KEY,null) 等价于 key: { $exists: false}
		{
			return mongoOperations.count(getKeyExistsQuery(key,false), getMongoEntityInformation().getJavaType());
		}
	}

	@Override
	public long count(Criteria criteria) {
		return mongoOperations.count(query(criteria), getMongoEntityInformation().getJavaType());
	}

	@Override
	public boolean exists(String key, Object value) {
		return count(key,value) > 0;
	}

	@Override
	public void delete(String key, Object value) {
		Assert.hasText(key, "The given key must not be empty!");
		
		if(value != null)
		{
			mongoOperations.remove( getEqualQuery(key,value), getMongoEntityInformation().getJavaType());
		}
		else //delete(KEY,null) 等价于 key: { $exists: false}
		{
			mongoOperations.remove( getKeyExistsQuery(key,false), getMongoEntityInformation().getJavaType());
		}
	}

	@Override
	public T findAndUpdate(ID id, Update update) {
		Assert.notNull(id, "The given id must not be null!");
		Assert.notNull(update, "The given update must not be null!");
		
		return findAndUpdate( getIdQuery(id), update);
	}

	@Override
	public T findAndUpdate(String key, Object value, Update update) {
		Assert.hasText(key, "The given key must not be empty!");
		Assert.notNull(update, "The given update must not be null!");

		if(value != null)
		{
			return findAndUpdate( getEqualQuery(key,value), update);
		}
		else //KEY : null 等价于 KEY : { $exists: false}
		{
			return findAndUpdate( getKeyExistsQuery(key,false), update);
		}
	}
	
	 /**
	  * 根据查询条件查找第一个实体,并执行更新操作
	  * 
	  * Finds the first document in the query and updates it. the old document is returned!
	  *  
	  * @param query
	  * @param update
	  * @return
	  * 		返回更新前的实体
	  */		
	
	private T findAndUpdate(Query query, Update update)
	{
		return (T) mongoOperations.findAndModify(query, update, getMongoEntityInformation().getJavaType(), getMongoEntityInformation().getCollectionName());
	}
	
	@Override
	public void update(ID id, Update update) {
		Assert.notNull(id, "The given id must not be null!");
		Assert.notNull(update, "The given update must not be null!");
		
		mongoOperations.updateFirst( getIdQuery(id), update, getMongoEntityInformation().getJavaType());
	}

	@Override
	public void update(String key, Object value, Update update) {
		Assert.hasText(key, "The given key must not be empty!");
		Assert.notNull(value, "The given value must not be null!");
		Assert.notNull(update, "The given update must not be null!");
		
		mongoOperations.updateFirst( getEqualQuery(key,value), update, getMongoEntityInformation().getJavaType());
	}

	@Override
	public void updateMulti(String key, Object value, Update update) {
		Assert.hasText(key, "The given key must not be empty!");
		Assert.notNull(value, "The given value must not be null!");
		Assert.notNull(update, "The given update must not be null!");
		
		mongoOperations.updateMulti( getEqualQuery(key,value), update, getMongoEntityInformation().getJavaType());
	}

	@Override
	public void updateMulti(Query query, Update update) {
		Assert.notNull(query, "The given query must not be null!");
		Assert.notNull(update, "The given update must not be null!");
		
		mongoOperations.updateMulti( query, update, getMongoEntityInformation().getJavaType());
	}

	@Override
	public void set(ID id, String key, Object value) {
		Assert.notNull(id, "The given id must not be null!");
		Assert.hasText(key, "The given key must not be empty!");
		
		if(value == null) //set(ID,key,null) 等价于 unset(ID,key)
			unset(id,key);
		else
			update(id, new Update().set(key, value));
	}

	@Override
	public void unset(ID id, String key) {
		Assert.notNull(id, "The given id must not be null!");
		Assert.hasText(key, "The given key must not be empty!");
		
		update(id, new Update().unset(key));
	}

	@Override
	public void inc(ID id, String key, Number inc) {
		Assert.notNull(id, "The given id must not be null!");
		Assert.hasText(key, "The given key must not be empty!");
		Assert.notNull(inc, "The given inc must not be null!");
		
		update(id, new Update().inc(key, inc));
	}

	@Override
	public void push(ID id, String key, Object value) {
		Assert.notNull(id, "The given id must not be null!");
		Assert.hasText(key, "The given key must not be empty!");
		Assert.notNull(value, "The given value must not be null!");
		
		update(id,new Update().push(key, value));
	}

	@Override
	public void pushAll(ID id, String key, Object[] values) {
		Assert.notNull(id, "The given id must not be null!");
		Assert.hasText(key, "The given key must not be empty!");
		Assert.notNull(values, "The given values must not be null!");
		
		update(id,new Update().pushAll(key, values));
	}

	@Override
	public void addToSet(ID id, String key, Object value) {
		Assert.notNull(id, "The given id must not be null!");
		Assert.hasText(key, "The given key must not be empty!");
		Assert.notNull(value, "The given value must not be null!");
		
		update(id,new Update().addToSet(key, value));
	}

	@Override
	public void pop(ID id, String key, Position pos) {
		Assert.notNull(id, "The given id must not be null!");
		Assert.hasText(key, "The given key must not be empty!");
		Assert.notNull(pos, "The given pos must not be null!");
		
		update(id,new Update().pop(key, pos));
	}

	@Override
	public void pull(ID id, String key, Object value) {
		Assert.notNull(id, "The given id must not be null!");
		Assert.hasText(key, "The given key must not be empty!");
		Assert.notNull(value, "The given value must not be null!");
		
		update(id,new Update().pull(key, value));
	}

	@Override
	public void pullAll(ID id, String key, Object[] values) {
		Assert.notNull(id, "The given id must not be null!");
		Assert.hasText(key, "The given key must not be empty!");
		Assert.notNull(values, "The given values must not be null!");
		
		update(id,new Update().pullAll(key, values));
	}

	@Override
	public void rename(ID id, String oldName, String newName) {
		Assert.notNull(id, "The given id must not be null!");
		Assert.hasText(oldName, "The given oldName must not be empty!");
		Assert.hasText(newName, "The given newName must not be empty!");

		update(id,new Update().rename(oldName, newName));
	}

	@Override
	public T findAndRemove(ID id) {
		Assert.notNull(id, "The given id must not be null!");
		
		return findAndRemove( getIdQuery(id) );
	}

	@Override
	public T findAndRemove(String key, Object value) {
		Assert.hasText(key, "The given key must not be empty!");
		
		if(value != null)
		{
			return findAndRemove( getEqualQuery(key,value) );
		}
		else	//KEY : null 等价于 KEY : { $exists: false}
		{
			return findAndRemove( getKeyExistsQuery(key,false) );
		}
	}

	@Override
	public T findAndRemove(Query query) {
		Assert.notNull(query, "The given query must not be null!");
		
		return (T) mongoOperations.findAndRemove(query, getMongoEntityInformation().getJavaType(), getMongoEntityInformation().getCollectionName());
	}

	private Query getEqualQuery(String key,Object value) {
		return new Query(getEqualCriteria(key, value));
	}
	private Criteria getEqualCriteria(String key,Object value) {
		return where(key).is(value);
	}
	
	private Query getKeyExistsQuery(String key,boolean b) {
		return new Query(getKeyExistsCriteria(key, b));
	}
	private Criteria getKeyExistsCriteria(String key,boolean b) {
		return where(key).exists(b);
	}

}
