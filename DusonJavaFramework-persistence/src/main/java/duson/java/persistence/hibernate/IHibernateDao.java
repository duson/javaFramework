package duson.java.persistence.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

import duson.java.core.persistence.PageList;
import duson.java.core.persistence.PageQuery;

/**
 * 基础数据库操作类接口 支持HQL、原生SQL、QBC、存储过程 4种查询方式
 */
public interface IHibernateDao<T, PK> {
	
	/**
	 * 根据主键获得对象
	 */
	public T get(PK id);

	/**
	 * 根据主键查询是否存在对应数据
	 */
	public boolean exists(PK id);
	
	/**
	 * 删除实体
	 */
	public void delete(T entity);

	/**
	 * 根据主键ID删除（支持批量）
	 */
	public void deleteById(PK... id);

	/**
	 * 更新操作:支持持久化状态下更新
	 */
	public void update(T entity);

	/**
	 * 批量新增和修改
	 */
	public void saveOrUpdateList(List<T> entityList);

	/**
	 * 更新操作:支持脱管状态下更新
	 */
	public void merge(T entity);

	/**
	 * 保存实体对象并返回数据库中的主键
	 */
	public PK save(T entity);

	/**
	 * 保存或修改,不明确操作是保存还是修改时使用此方法
	 */
	public void saveOrUpdate(T entity);

	/**
	 * 将session的缓存中的数据与数据库同步
	 */
	public void flush();

	/**
	 * 清除session中的缓存数据（不管缓存与数据库的同步）
	 */
	public void clear();

	/* HQL
	 *******************************************************************/

	/**
	 * 根据HQL查询满足该SQL的对象
	 */
	public T find(String hql, Object... params);

	/**
	 * HQL查询
	 */
	public List<T> findByHQL(String hql, Object... params);

	/**
	 * 根据HQL获得查询结果的行数
	 */
	public Long getCountByHQL(PageQuery queryObject, Object... params);
	
	/**
	 * HQL分页查询
	 */
	public PageList findPageByHQL(PageQuery queryObject, Object[] params);
	
	/**
	 * 执行Update或Delete的HQL语句
	 */
	public int executeHql(String hql, Object... params);

	/* SQL
	 *******************************************************************/

	/**
	 * 原生SQL查询
	 */
	public List findBySQL(String sql, Object... params);

	/**
	 * 根据原生SQL获得查询结果行数
	 */
	public Long getCountBySQL(PageQuery queryObject, Object... params);

	/**
	 * 原生SQL分页查询
	 */
	public PageList findPageBySQL(PageQuery queryObject, Object... params);
	
	/**
	 * 执行原生SQL
	 */
	public void executeSQL(String sql);
	
	/**
	 * 执行原生SQL
	 */
	public void executeSQL(String sql, Object[] values);
	
	/* QBC
	 *******************************************************************/

	/**
	 * 查询单个对象 根据DetachedCriteria对象返回符条件的单个对象
	 */
	public T find(DetachedCriteria detachedCriteria);

	/**
	 * QBC查询
	 */
	public List<T> findByQBC(DetachedCriteria detachedCriteria);

	/**
	 * 根据QBC获得查询结果的行数
	 */
	public Long getCountByQBC(DetachedCriteria detachedCriteria);

	/**
	 * 
	 * QBC分页查询
	 * 
	 * @param queryObject
	 * @return PageList [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public PageList findPageByQBC(PageQuery queryObject);

	/* 存储过程
	 *******************************************************************/

	/**
	 * 调用执行存储过程
	 * 
	 * @param queryObject
	 * @param map 输出参数据定义 key:index值 value:类型
	 * Usage:
	 * // 输出参数:int(32)
		outMap.put(3, Types.INTEGER);
		// 输出参数:bigInt(64)
		outMap.put(4, Types.BIGINT);
		List list = this.findListByProc(PageQuery, outMap, new Object[] { tableName, inType });
	 */
	public List findListByProc(PageQuery queryObject, Map<Integer, Integer> map, Object... params);
}
