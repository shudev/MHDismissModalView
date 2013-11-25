package com.shuketa.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public interface DbTable {
	String createTableSql();
	String tableName();
	String[] columns();
	Object[] values();
	String selectWhere();
	void setPreparedStatementParam(PreparedStatement pstmt)  throws SQLException;
	void didStartJsonObject(String key);
	PreparedStatement didEndJsonObject();
	void didStartJsonArray(String key);
	PreparedStatement didEndJsonArray();
	void didValueNumber(String key,Number value);
	void didValueString(String key,String value);
	void didValueTrue();
	void didValueFalse();
	void didKeyName(String key);
	void didStartJsonObject();
	
	
	
}
