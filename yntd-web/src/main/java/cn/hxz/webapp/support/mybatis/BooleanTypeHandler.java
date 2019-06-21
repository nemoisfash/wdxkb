package cn.hxz.webapp.support.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * BooleanTypeHandler.java Create on 2017年2月21日 下午4:27:25
 *
 * @author cn.feeboo
 * @version 1.0
 */
@MappedTypes({Boolean.class})  
@MappedJdbcTypes({JdbcType.CHAR})
public class BooleanTypeHandler extends BaseTypeHandler<Boolean> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, parameter ? "T": "F");
	}

	@Override
	public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return getNullableBoolean(rs.getString(columnName));
	}

	@Override
	public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		 return getNullableBoolean(rs.getString(columnIndex));
	}

	@Override
	public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return getNullableBoolean(cs.getString(columnIndex));
	}
	
	private Boolean getNullableBoolean(String jdbcValue){
		Boolean javaValue = null;
		if (jdbcValue!=null){
			if ("T".equalsIgnoreCase(jdbcValue))
				javaValue = true;
			if ("F".equalsIgnoreCase(jdbcValue))
				javaValue = false;
		}
		return javaValue;
	}

}
