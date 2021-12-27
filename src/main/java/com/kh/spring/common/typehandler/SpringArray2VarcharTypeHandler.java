package com.kh.spring.common.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * Array2Varchar = ArrayToVarchar
 * String[](vo필드) / varchar2(sql타입) 변환필요
 * 
 * setNonNullParameter : vo필드 -> pstmt.setter
 * 
 * getNullableResult : rset(column name) -> vo필드
 * getNullableResult : rset(column index) -> vo필드
 * getNullableResult : callable statement용
 *
 */
@MappedTypes(String[].class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class SpringArray2VarcharTypeHandler extends BaseTypeHandler<String[]> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType)
			throws SQLException {
		// String[] --> String --> varchar2
		// parameter를 콤마로 조인해서 i번째에 넣어달라. (i는 sql작성했을 때의 ?와 같음)
		ps.setString(i, String.join(",", parameter));
		
	}

	@Override
	public String[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String value = rs.getString(columnName); // varchar2 --> String
		return value != null ? value.split(",") : null; // String --> String[]
	}

	@Override
	public String[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String value = rs.getString(columnIndex); // varchar2 --> String
		return value != null ? value.split(",") : null; // String --> String[]
	}

	@Override
	public String[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String value = cs.getString(columnIndex); // varchar2 --> String
		return value != null ? value.split(",") : null; // String --> String[]
	}

	
}
