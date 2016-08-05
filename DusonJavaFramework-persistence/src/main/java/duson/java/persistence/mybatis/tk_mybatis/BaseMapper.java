
package duson.java.persistence.mybatis.tk_mybatis;

import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface BaseMapper<T> extends ExtendMapper<T>, Mapper<T>, MySqlMapper<T> {
	/* 分页
	 * com.github.pagehelper.Page<T> page1 = PageHelper.startPage(pageNo, pageSize);
	 * 获取记录：mapper.select(entity)
	 * 获取总数：page1.getTotal()
	 */
}
