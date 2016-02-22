package duson.java.persistence.mybatis.enumeration;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * Mybatis 支持Java枚举类型
 * @author protruly-cpm
 *
 * @param <E>
 */
public class IntEnumTypeHandler<E extends Enum<E> & IntEnum<E>> extends BaseTypeHandler<E> {

	  private Class<E> type;
	  private final E[] enums;
	   
	  public IntEnumTypeHandler(Class<E> type) {
	    if (type == null) throw new IllegalArgumentException("Type argument cannot be null");
	    this.type = type;
	    this.enums = type.getEnumConstants();
	    if (this.enums == null) throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
	  }
	  
	  @Override
	  public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
	    ps.setInt(i, parameter.getIntValue());
	  }

	  @Override
	  public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
	    int i = rs.getInt(columnName);
	    if (rs.wasNull()) {
	      return null;
	    } else {
	      try {
	        return enums[i];
	      } catch (Exception ex) {
	        throw new IllegalArgumentException("Cannot convert " + i + " to " + type.getSimpleName() + " by int value.", ex);
	      }
	    }
	  }

	  @Override
	  public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
	    int i = rs.getInt(columnIndex);
	    if (rs.wasNull()) {
	      return null;
	    } else {
	      try {
	        return enums[i];
	      } catch (Exception ex) {
	        throw new IllegalArgumentException("Cannot convert " + i + " to " + type.getSimpleName() + " by int value.", ex);
	      }
	    }
	  }

	  @Override
	  public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
	    int i = cs.getInt(columnIndex);
	    if (cs.wasNull()) {
	      return null;
	    } else {
	      try {
	        return enums[i];
	      } catch (Exception ex) {
	        throw new IllegalArgumentException("Cannot convert " + i + " to " + type.getSimpleName() + " by int value.", ex);
	      }
	    }
	  }
}
