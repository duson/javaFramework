package duson.java.persistence.hibernate.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;

import duson.java.core.persistence.PageList;
import duson.java.core.persistence.PageQuery;
import duson.java.persistence.hibernate.IHibernateDao;


public class HibernateDaoImpl<T extends java.io.Serializable, PK extends java.io.Serializable> implements IHibernateDao<T, PK> {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 获取当前数据库session对象
	 */
	private Session getCurrentSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return this.sessionFactory.getCurrentSession();
	}

	/**
	 * 查询数据对象
	 */
	private Class<T> getEntityClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * 获取主键字段名称
	 */
	private String getPkName() {
		Field[] fields = this.getEntityClass().getDeclaredFields();
		for (Field f : fields) {
			if (f.isAnnotationPresent(Id.class)) {
				return f.getName();
			}
		}
		return null;
	}

	/**
	 * 获取查询所有数据HQL语句
	 */
	private String getHqlListAll() {
		return "from " + this.getEntityClass().getSimpleName() + " order by " + getPkName() + " desc";
	}

	/**
	 * 获取查询所有数据记录数据HQL语句
	 */
	private String getHqlCountAll() {
		return " select count(*) from " + this.getEntityClass().getSimpleName();
	}

	/**
	 * 删除实体
	 */
	public void delete(T entity) {
		if (null != entity) {
			this.getCurrentSession().delete(entity);
		}
	}

	/**
	 * 根据主键ID删除
	 */
	public void deleteById(PK... ids) {
		for(PK id : ids)
			this.getCurrentSession().delete(this.get(id));
	}

	/**
	 * 更新操作:支持持久化状态下更新
	 */
	public void update(T entity) {
		this.getCurrentSession().update(entity);
	}

	/**
	 * 更新操作:支持脱管状态下更新
	 */
	public void merge(T entity) {
		this.getCurrentSession().merge(entity);
	}

	/**
	 * 批量新增和修改
	 */
	public void saveOrUpdateList(List<T> entityList) {
		for (T t : entityList) {
			this.saveOrUpdate(t);
		}
	}

	/**
	 * 保存实体对象并返回数据库中的主键
	 */
	public PK save(T entity) {
		return (PK) this.getCurrentSession().save(entity);
	}

	/**
	 * 保存或修改,不明确操作是保存还是修改时使用此方法
	 */
	public void saveOrUpdate(T entity) {
		this.getCurrentSession().saveOrUpdate(entity);
	}

	/**
	 * 执行Update或Delete的HQL语句
	 */
	public int executeHql(String hql, Object... params) {
		Query query = this.getCurrentSession().createQuery(hql);
		if (null != params && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		return query.executeUpdate();
	}

	/**
	 * 执行原生SQL
	 */
	public void executeSQL(String sql) {
		this.jdbcTemplate.execute(sql);
	}

	/**
	 * 执行原生SQL
	 */
	public void executeSQL(String sql, Object[] values) {
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				sqlQuery.setParameter(i, values[i]);
			}
		}
		sqlQuery.executeUpdate();
	}

	/**
	 * HQL查询
	 */
	public List<T> findByHQL(String hql, Object... params) {
		Query query = this.getCurrentSession().createQuery(hql);
		if (null != params && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}

		return (List<T>) query.list();
	}

	/**
	 * 原生SQL查询
	 */
	public List findBySQL(String sql, Object... params) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
		if (null != params && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		return query.list();
	}

	/**
	 * QBC查询
	 */
	public List<T> findByQBC(DetachedCriteria detachedCriteria) {
		return (List<T>) detachedCriteria.getExecutableCriteria(this.getCurrentSession()).list();
	}

	/**
	 * HQL分页查询
	 */
	public PageList findPageByHQL(PageQuery queryObject, Object[] params) {
		final String hql = queryObject.getFilter().toString();
		final int curPage = queryObject.getPageIndex();
		final int pageSize = queryObject.getPageSize();
		Query query = this.getCurrentSession().createQuery(hql);
		if (null != params && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		Long totals = getCountByHQL(queryObject, params);
		query.setFirstResult(getFirstResult(curPage, pageSize));
		query.setMaxResults(pageSize);
		List<T> list = query.list();
		PageList page = new PageList();
		page.setList(list);
		page.setTotalRecord(totals.intValue());
		return page;
	}

	private int getFirstResult(int curPage, int pageSize) {
		if (curPage < 1) {
			curPage = 1;
		}
		if (pageSize < 1) {
			pageSize = 10;
		}
		return (curPage - 1) * pageSize;
	}

	/**
	 * 原生SQL分页查询
	 */
	public PageList findPageBySQL(PageQuery queryObject, Object[] params) {
		final String sql = queryObject.getFilter().toString();
		final int curPage = queryObject.getPageIndex();
		final int pageSize = queryObject.getPageSize();

		Query query = this.getCurrentSession().createSQLQuery(sql);
		if (null != params && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		Long totals = getCountBySQL(queryObject, params);
		query.setFirstResult(getFirstResult(curPage, pageSize));
		query.setMaxResults(pageSize);
		List list = query.list();
		PageList page = new PageList();
		page.setList(list);
		page.setTotalRecord(totals.intValue());
		return page;
	}

	/**
	 * QBC分页查询
	 */
	public PageList findPageByQBC(PageQuery queryObject) {
		DetachedCriteria detachedCriteria = (DetachedCriteria) queryObject.getFilter();
		int curPage = queryObject.getPageIndex();
		int pageSize = queryObject.getPageSize();

		Criteria criteria = detachedCriteria.getExecutableCriteria(this.getCurrentSession());
		Object o = criteria.setProjection(Projections.rowCount()).uniqueResult();
		Long totals = new Long(0);
		if (null != o) {
			totals = Long.valueOf(o.toString());
		}

		criteria.setProjection(null);
		criteria.setFirstResult(getFirstResult(curPage, pageSize));
		criteria.setMaxResults(pageSize);
		PageList page = new PageList();
		page.setList(criteria.list());
		page.setTotalRecord(totals.intValue());
		return page;
	}

	/**
	 * 根据主键获得对象
	 */
	public T get(PK id) {
		return (T) this.getCurrentSession().get(this.getEntityClass(), id);
	}

	/**
	 * 根据HQL查询满足该SQL的对象
	 */
	public T find(String hql, Object... params) {
		Query query = getCurrentSession().createQuery(hql);
		setParameters(query, params);
		return (T) query.setMaxResults(1).uniqueResult();
	}

	/**
	 * 查询单个对象 根据DetachedCriteria对象返回符条件的单个对象
	 */
	public T find(DetachedCriteria detachedCriteria) {
		return (T) detachedCriteria.getExecutableCriteria(getCurrentSession()).uniqueResult();
	}

	/**
	 * HQL记录数据查询 如果queryCountString存在的话，以queryCountString优先
	 */
	public Long getCountByHQL(PageQuery queryObject, Object[] params) {
		String hql = queryObject.getFilter().toString();

		int indexfrom = hql.indexOf("from");
		int indexFROM = hql.indexOf("FROM");
		if (indexfrom != -1 && indexFROM != -1) {
			hql = indexfrom > indexFROM ? hql.substring(hql.indexOf("FROM")) : hql.substring(hql.indexOf("from"));
		} else if (indexfrom != -1) {
			hql = hql.substring(hql.indexOf("from"));
		} else {
			hql = hql.substring(hql.indexOf("FROM"));
		}
		String queryString = "select count(*) " + hql;

		Query query = this.getCurrentSession().createQuery(queryString);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		return (Long) query.uniqueResult();
	}

	/**
	 * QBC记录数据查询
	 */
	public Long getCountByQBC(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(this.getCurrentSession());
		Object o = criteria.setProjection(Projections.rowCount()).uniqueResult();
		return Long.valueOf(o.toString());
	}

	/**
	 * 原生SQL记录数据查询
	 */
	public Long getCountBySQL(PageQuery queryObject, Object[] params) {
		String sql = queryObject.getFilter().toString();
		final String queryString = "select count(1) from (" + sql + ") t";
		Query query = this.getCurrentSession().createSQLQuery(queryString);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		return ((BigInteger) query.uniqueResult()).longValue();
	}

	/**
	 * 调用执行存储过程
	 * @param queryObject
	 * @param map 输出参数据定义 key:index值 value:类型
	 */
	public List findListByProc(PageQuery queryObject, Map<Integer, Integer> map, Object... params) {
		List<Object> list = new ArrayList<Object>();
		int length = null == params ? 0 : params.length;
		try {
			Connection connection = this.jdbcTemplate.getDataSource().getConnection();
			CallableStatement stmt = null;
			if (null != map) {
				stmt = connection.prepareCall(generationExcuteProdure(queryObject.getFilter().toString(), length + map.size()));
				Set<Map.Entry<Integer, Integer>> set = map.entrySet();
				for (Iterator<Map.Entry<Integer, Integer>> it = set.iterator(); it.hasNext();) {
					Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) it.next();
					// 设置输出参数
					stmt.registerOutParameter(entry.getKey().intValue(), entry.getValue().intValue());
				}
				setStringParams(params, stmt);
				stmt.execute();

				for (Iterator<Map.Entry<Integer, Integer>> it = set.iterator(); it.hasNext();) {
					Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) it.next();
					list.add(stmt.getObject(entry.getKey().intValue()));
				}
			} else {
				stmt = connection.prepareCall(generationExcuteProdure(queryObject.getFilter().toString(), length));
				if (length > 0) {
					setStringParams(params, stmt);
				}
				stmt.execute();
			}
			stmt.close();
			connection.close();
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * sql参数设置
	 */
	private void setStringParams(Object[] params, PreparedStatement pstmt) throws SQLException {
		if (null != params) {
			if (params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i + 1, params[i]);
				}
			}
		}
	}

	/**
	 * 设置查询条件
	 */
	protected void setParameters(Query query, Object[] paramlist) {
		if (paramlist != null) {
			for (int i = 0; i < paramlist.length; i++) {
				if (paramlist[i] instanceof Date) {
					query.setTimestamp(i, (Date) paramlist[i]);
				} else {
					query.setParameter(i, paramlist[i]);
				}
			}
		}
	}

	/**
	 * 生成存储过程的方法
	 * 
	 * @param procedureName 存储过程的名称
	 * @param length 参数的个数
	 * @return 生成的执行的存储过程
	 */
	private String generationExcuteProdure(String procedureName, int length) {
		StringBuilder sql = new StringBuilder();
		sql.append("{Call ");
		sql.append(StringUtils.trim(procedureName));
		if (length > 0) {
			sql.append("(");
			for (int i = 0; i < length; i++) {
				sql.append("?");
				if (i != length - 1) {
					sql.append(",");
				}
			}
			sql.append(")");
		}
		sql.append("}");
		return sql.toString();
	}

	/**
	 * 查询所有数据记录数
	 */
	public Long countAll() {
		return (Long) this.find(this.getHqlCountAll());
	}

	/**
	 * 查询所有数据
	 */
	public List<T> listAll() {
		return this.findByHQL(this.getHqlListAll());
	}

	/**
	 * 根据主键查询是否存在对应数据
	 */
	public boolean exists(PK id) {
		return get(id) != null;
	}

	/**
	 * 将session的缓存中的数据与数据库同步
	 */
	public void flush() {
		getCurrentSession().flush();
	}

	/**
	 * 清除session中的缓存数据（不管缓存与数据库的同步）
	 */
	public void clear() {
		getCurrentSession().clear();
	}

}
