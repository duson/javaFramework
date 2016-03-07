package duson.java.persistence.hibernate;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StringType;

/**
 * 为Hibernate的HQL添加原生Mysql的内置函数
 *
 */
public class MySqlDialectExt extends MySQL5Dialect {
	public MySqlDialectExt() {
		super();
		
		super.registerFunction("group_concat", new StandardSQLFunction("group_concat", StringType.INSTANCE));
	}
}
