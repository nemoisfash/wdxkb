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
 * LongArrayTypeHandler.java Create on 2017年2月21日 下午5:02:42
 *
 * @author cn.feeboo
 * @version 1.0
 */
@MappedTypes({ Long[].class })
@MappedJdbcTypes({ JdbcType.VARCHAR })
public class LongArrayTypeHandler extends BaseTypeHandler<Long[]> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Long[] parameter, JdbcType jdbcType)
			throws SQLException {
		StringBuffer result = new StringBuffer();
		for (Long value : parameter)
			result.append(value).append(",");
		result.deleteCharAt(result.length() - 1);
		ps.setString(i, result.toString());
	}

	@Override
	public Long[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return getLongArray(rs.getString(columnName));
	}

	@Override
	public Long[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return getLongArray(rs.getString(columnIndex));
	}

	@Override
	public Long[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return getLongArray(cs.getString(columnIndex));
	}

	private Long[] getLongArray(String columnValue) {
		if (columnValue == null)
			return null;
		String[] arrStr = columnValue.split(",");
		Long[] arrLong = new Long[arrStr.length];
		for (int i = 0; i < arrStr.length; i++) {
			arrLong[i] = Long.parseLong(arrStr[i]);
		}
		return arrLong;
	}
}
