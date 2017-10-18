package com.facewnd.core.mybatismapper;

import com.facewnd.core.mybatismapper.base.SelectProcedureMapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Description: 扩展通用Mapper接口
 * @author 彭彩云
 * @date 2016年7月21日
 */
public interface BaseMapper<T> extends SelectProcedureMapper<T>, Mapper<T>, MySqlMapper<T> {

}
