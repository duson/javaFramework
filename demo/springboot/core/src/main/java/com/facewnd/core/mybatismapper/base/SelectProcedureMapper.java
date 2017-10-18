package com.facewnd.core.mybatismapper.base;

import java.util.Map;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Options.FlushCachePolicy;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.StatementType;

import com.facewnd.core.mybatismapper.provider.SelectProcedureProvider;

/**
 * @Description: 通用Mapper接口,其他接口继承该接口即可
 * @author 彭彩云
 * @date 2016年7月25日
 */
public interface SelectProcedureMapper<T> {
	
	/**
	 * 调用存储过程生成表主键ID返回 默认为当前实体类对应表名
	 *
	 * @param key
	 * @return
	 */
	@Options(statementType = StatementType.CALLABLE, flushCache = FlushCachePolicy.TRUE, useCache = false)
	@SelectProvider(type = SelectProcedureProvider.class, method = "dynamicSQL")
	void generatedIDByTableName(Map<String, Object> params);
	/**
	 * 调用存储过程生成表主键ID返回 默认为当前实体类对应表名
	 *
	 * @param key
	 * @return
	 */
	@Options(statementType = StatementType.CALLABLE, flushCache = FlushCachePolicy.TRUE, useCache = false)
	@SelectProvider(type = SelectProcedureProvider.class, method = "dynamicSQL")
	void generatedIDByTableNameExt(Map<String, Object> params);
	
	/**
	 * 调用存储过程生成表主键ID返回 默认为当前实体类对应表名
	 *
	 * @param key
	 * @return
	 */
	@Options(statementType = StatementType.CALLABLE, flushCache = FlushCachePolicy.TRUE, useCache = false)
	@SelectProvider(type = SelectProcedureProvider.class, method = "dynamicSQL")
	void generatedID(Map<String, Object> params);
}
