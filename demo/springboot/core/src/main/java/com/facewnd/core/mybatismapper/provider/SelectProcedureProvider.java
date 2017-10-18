package com.facewnd.core.mybatismapper.provider;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

/**
 * @Description: TODO
 * @author 彭彩云
 * @date 2016年7月25日
 */
public class SelectProcedureProvider extends MapperTemplate {

	public SelectProcedureProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}
	
	/**
	 *  调用存储过程获取表主键
	 *  {call base_proc_get_index(
		#{tableName,mode=IN,jdbcType=VARCHAR},
		#{isBigInt,mode=IN,jdbcType=INTEGER},
		#{intID,mode=OUT,jdbcType=INTEGER},
		#{longID,mode=OUT,jdbcType=BIGINT}
		)}
	 * @param ms
	 */
	public String generatedIDByTableName(MappedStatement ms) {
		StringBuilder sql = new StringBuilder();
		sql.append("{call base_proc_get_index(").append("#{tableName,mode=IN,jdbcType=VARCHAR}");
		sql.append(",1,#{intID,mode=OUT,jdbcType=INTEGER},");
		sql.append("#{longID,mode=OUT,jdbcType=BIGINT})}");
		return sql.toString();
	}
	
	/**
	 * 调用存储过程获取表主键
	 *  {call base_proc_get_index(
		#{tableName,mode=IN,jdbcType=VARCHAR},
		#{isBigInt,mode=IN,jdbcType=INTEGER},
		#{intID,mode=OUT,jdbcType=INTEGER},
		#{longID,mode=OUT,jdbcType=BIGINT}
		)}
	 * @param ms
	 */
	public String generatedID(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append("{call base_proc_get_index('").append(tableName(entityClass));
		sql.append("',1,#{intID,mode=OUT,jdbcType=INTEGER},");
		sql.append("#{longID,mode=OUT,jdbcType=BIGINT})}");
		return sql.toString();
	}
	/**获取批量
	 * 使用方法：Long itemIdStart(第一个编号之前) = itemIdEnd(返回的值) - count(申请的数量);
	 * 第一个编号是itemIdStart++
	 * //比如申请4,返回最后一个id是5 那么第一个id=2 ..即5-4 + 1,但在这里不加1 让在for循环里，最前面 itemIdStart ++;
	 * @param ms
	 * @param count
	 * @return
	 */
	public String generatedIDByTableNameExt(MappedStatement ms) {
		StringBuilder sql = new StringBuilder();
		sql.append("{call base_proc_get_index_ext(#{tableName,mode=IN,jdbcType=VARCHAR}");
		sql.append(",1");
		sql.append(",#{idCount,mode=IN,jdbcType=INTEGER}");
		sql.append(",#{intID,mode=OUT,jdbcType=INTEGER}");
		sql.append(",#{longID,mode=OUT,jdbcType=BIGINT})}");
		return sql.toString();
	}
}
