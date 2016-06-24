package duson.java.persistence.mongodb.repository.support;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Update.Position;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;



/**
 * 
 * 对MongoRepository接口增加额外常用的操作,如insert
 * 
 * @version 2013-02-26
 * 
 * @author KEN
 *
 * @param <T>
 * @param <ID>
 */

@NoRepositoryBean
public interface MongoExtRepository<T, ID extends Serializable> extends MongoRepository<T, ID>
{
	/**
	 * Insert the object into the collection for the entity type of the object to save.
	 * <p/>
	 * The object is converted to the MongoDB native representation using an instance of {@see MongoConverter}.
	 * <p/>
	 * If you object has an "Id' property, it will be set with the generated Id from MongoDB. If your Id property is a
	 * String then MongoDB ObjectId will be used to populate that string. Otherwise, the conversion from ObjectId to your
	 * property type will be handled by Spring's BeanWrapper class that leverages Spring 3.0's new Type Conversion API.
	 * See <a href="http://static.springsource.org/spring/docs/3.0.x/reference/validation.html#core-convert">Spring 3 Type
	 * Conversion"</a> for more details.
	 * <p/>
	 * <p/>
	 * Insert is used to initially store the object into the database. To update an existing object use the save method.
	 * 
	 * 
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 * 
	 * @param entity
	 * @return the inserted entity
	 */
	<S extends T> S insert(S entity);
	
	
	
	/**
	 * Insert a Collection of Entities into a collection in a single batch write to the database.
	 * 
	 * 批量插入实体s
	 * 
	 * @param batchToInsert the list of Entities to insert.
	 * 
	 * @return the inserted Collection
	 */
	<S extends T> Collection<S> insert(Collection<S> batchToInsert);
	
	
	
	
	/**
	 * 根据对象的属性值来查询实体
	 * 
	 * @param key 属性名
	 * @param value 属性值
	 * @return 查询到的对象,若符合条件的对象有多个,则只返回第一个
	 */
	T findOne(String key, Object value);
	
	
	/**
	 * 根据query来查询符合条件的第一个实体
	 * @param query
	 * @return
	 */
	T findOne(Query query);
	
	/**
	 * 根据对象的属性值来查询实体s
	 * 
	 * @param key 属性名
	 * @param value 属性值
	 * @return 查询到的对象列表
	 */
	List<T> findAll(String key, Object value);
	

	/**
	 * 根据Query查询实体
	 * @param query
	 * @return
	 */
	List<T> findAll(Query query);
	
	
	/**
	 * 根据指定条件查询实体s,并根据分页要求来返回分页结果
	 * 
	 * @param criteria
	 * @param pageable
	 * @return
	 */
	Page<T> findAll(Criteria criteria,Pageable pageable);
	

	
	/**
	 * 查找所有key != value 的对象
	 * 
	 * @param key 属性名
	 * @param value 属性值
	 * @return 查询到的对象列表
	 */
	List<T> findAllNotEqual(String key, Object value);
	
	
	
	/**
	 * 统计指定属性值的对象的数量
	 * 
	 * @param key 属性名
	 * @param value 属性值
	 * @return 指定属性值的对象的数量
	 */
	long count(String key, Object value);
	
	/**
	 * 统计指定条件的对象的数量
	 * 
	 * @param criteria
	 * @return
	 */
	long count(Criteria criteria);
	
	/**
	 * 判断指定属性值的对象是否存在
	 * 
	 * @param key 属性名
	 * @param value 属性值
	 * @return 指定属性值的对象是否存在
	 */
	boolean exists(String key, Object value);
	

	
	
	
	/**
	 * 删除指定属性值的所有对象
	 * 
	 * @param key 属性名
	 * @param value 属性值
	 */
	void delete(String key, Object value); 
	
	
	
	
	/**
	 * 根据ID查找实体,并执行更新操作
	 * Finds the first document in the query and updates it. the old document is returned!
	 * @param id
	 * @param update 
	 * @return	
	 * 			返回更新前的实体
	 */
	T findAndUpdate(ID id, Update update);

	
	/**
	 * 根据查询条件查找第一个实体,并执行更新操作
	 * 
	 * Finds the first document in the query and updates it. the old document is returned!
	 * 
	 * @param key   属性名
	 * @param value  属性值 
	 * @param update 
	 * @return 
	 * 			返回更新前的实体
	 */
	T findAndUpdate(String key, Object value, Update update);
	
	
	
	/**
	 * Updates the first object that is found in the specified collection that matches the query document criteria with
	 * the provided updated document.
	 * 
	 * 根据ID查询实体并执行更新!
	 * 
	 * @param update the update document that contains the updated object or $ operators to manipulate the existing
	 *          object.
	 * 
	 * 
	 */
	void update(ID id, Update update);

	
	
	/**
	 * Updates the first object that is found in the specified collection that matches the query document criteria with
	 * the provided updated document.
	 * 
	 * 根据对象的属性值查找实体,并执行更新操作,注意,这会更新所以匹配条件的每一个实体!
	 * @param key   属性名
	 * @param value  属性值 
	 * 
	 * @param update the update document that contains the updated object or $ operators to manipulate the existing
	 *          object.
	 */
	void update(String key, Object value, Update update);
	
	
	
	/**
	 * Updates all objects that are found in the specified collection that matches the query document criteria with the
	 * provided updated document.
	 * 
	 * 根据对象的属性值查找实体,并执行更新操作,注意,这会更新所以匹配条件的实体!
	 * @param key   属性名
	 * @param value  属性值 
	 * 
	 * @param update the update document that contains the updated object or $ operators to manipulate the existing
	 *          object.
	 */
	void updateMulti(String key, Object value, Update update);
	
	
	/**
	 * Updates all objects that are found in the collection for the entity class that matches the query document criteria
	 * with the provided updated document.
	 * 
	 * @param query the query document that specifies the criteria used to select a record to be updated
	 * @param update the update document that contains the updated object or $ operators to manipulate the existing
	 *          object.
	 */
	void updateMulti(Query query, Update update);
	
	//更新单属性操作-------------------------------------------------------------------------
	
	/**
	 * Update using the $set update modifier
	 * 
	 * 更新属性key的值 
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	void set(ID id,String key, Object value);

	
	/**
	 * Update using the $unset update modifier
	 * 
	 * 顾名思义，就是删除字段了
	 * 
	 * @param key
	 * @return
	 */
	void unset(ID id,String key);
	
	
	/**
	 * 使指定ID的实体的的指定属性key的值加上特定的值inc
	 * 
	 * @param id
	 * @param key 属性
	 * @param inc 
	 */
	void inc(ID id,String key, Number inc);


	/**
	 * Update using the $push update modifier
	 * 
	 * 把value追加到属性key(线性表)里面去，field一定要是线性表类型才行例：
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	void push(ID id, String key, Object value);
	
	/**
	 * Update using the $pushAll update modifier
	 * 
	 * 同$push,只是一次可以追加多个值到一个线性表字段内
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	void pushAll(ID id,String key, Object[] values);

	/**
	 * Update using the $addToSet update modifier
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	void addToSet(ID id,String key, Object value);
	
	/**
	 * Update using the $pop update modifier
	 * 
	 * 删除线性表的首或尾元素
	 *  
	 * @param key
	 * @param pos
	 * @return
	 */
	void pop(ID id,String key, Position pos);

	/**
	 * Update using the $pull update modifier
	 * 
	 * 	从线性表key内删除一个等于value值
	 * @param key
	 * @param value
	 * @return
	 */
	void pull(ID id,String key, Object value);

	/**
	 * Update using the $pullAll update modifier
	 * 
	 * 同$pull,可以一次删除线性表内的多个值
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	 void pullAll(ID id,String key, Object[] values);

	/**
	 * Update using the $rename update modifier
	 * 修改字段oldName的名字为newName
	 * @param oldName
	 * @param newName
	 * @return
	 */
	 void rename(ID id,String oldName, String newName);



	 
	T findAndRemove(ID id);



	T findAndRemove(String key, Object value);


	/**
	 * Map the results of an ad-hoc query on the collection for the entity type to a single instance of an object of the
	 * specified type. The first document that matches the query is returned and also removed from the collection in the
	 * database.
	 * <p/>
	 * The object is converted from the MongoDB native representation using an instance of {@see MongoConverter}.
	 * <p/>
	 * The query is specified as a {@link Query} which can be created either using the {@link BasicQuery} or the more
	 * feature rich {@link Query}.
	 * 
	 * @param query the query class that specifies the criteria used to find a record and also an optional fields
	 *          specification
	 * @return the converted object
	 */
	T findAndRemove(Query query);
}
