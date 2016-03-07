package duson.java.persistence.hibernate;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.dao.DataAccessResourceFailureException;

/**
 * 自定义主键生成策略
 */
public class IDGenerator implements IdentifierGenerator, Configurable {

	protected static final Logger LOGGER = Logger.getLogger(IDGenerator.class);
	
	// 生成主键值存储过程名称
	private String procName;
	// 生成主键值表名
	private String tableName;
	// 是否返回bigInt值 true:返回bigInt(64) false:返回int(32)
	private Boolean isBigInt = false;

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws HibernateException
	 */
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		Map<Integer, Integer> outMap = new HashMap<>();
		// 输出参数:int(32)
		outMap.put(3, Types.INTEGER);
		// 输出参数:bigInt(64)
		outMap.put(4, Types.BIGINT);
		int inType = this.isBigInt ? 1 : 0;
		List<?> list = this.findListByProc(session, this.procName, outMap, new Object[] { this.tableName, inType });
		if (list != null && list.size() > 0) {
			if (this.isBigInt) {
				return Long.valueOf(list.get(1).toString());
			} else {
				return Integer.valueOf(list.get(0).toString());
			}
		}
		return null;
	}

	@Override
	public void configure(Type arg0, Properties properties, ServiceRegistry arg2) throws MappingException {
		this.tableName = properties.getProperty("tableName");
		this.isBigInt = Boolean.parseBoolean(properties.getProperty("bigInt"));
		this.procName = properties.getProperty("procName");
		LOGGER.debug("初始化增加:tableName:" + this.tableName + ",procName:" + this.procName + ",isBigInt:" + this.isBigInt);
	}
	
	/**
	 * 调用执行存储过程
	 * @param queryObject
	 * @param map 输出参数据定义 key:index值 value:类型
	 */
	private List<?> findListByProc(SessionImplementor session, String sql, Map<Integer, Integer> map, Object... params) {
		List<Object> list = new ArrayList<Object>();
		int length = null == params ? 0 : params.length;
		try {
			Connection connection = session.connection();
			CallableStatement stmt = null;
			if (null != map) {
				stmt = connection.prepareCall(generationExcuteProdure(sql, length + map.size()));
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
				stmt = connection.prepareCall(generationExcuteProdure(sql, length));
				if (length > 0) {
					setStringParams(params, stmt);
				}
				stmt.execute();
			}
			stmt.close();
			// connection.close();
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
	 * sql参数设置
	 * 
	 * @param params
	 * @param pstmt
	 * @throws SQLException
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

}
