package com.shuketa.db.table;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.shuketa.db.DbTable;

public class DbTableCity implements DbTable {

	@Override
	public String tableName() {
		return "CityTable";
	}

	@Override
	public String[] columns() {
		return new String[]{"CityCode","CityName","CaId","MType"};
	}

	@Override
	public String[] values() {
		return null;
	}

	@Override
	public void setPreparedStatementParam(PreparedStatement pstmt)
			throws SQLException {
		
	}

	@Override
	public void didStartJsonObject() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PreparedStatement didEndJsonObject() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public PreparedStatement didEndJsonArray() {
		return null;
		// TODO Auto-generated method stub
		
	}


	@Override
	public void didValueTrue() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didValueFalse() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didKeyName(String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String selectWhere() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createTableSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void didStartJsonObject(String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didStartJsonArray(String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didValueNumber(String key, Number value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didValueString(String key, String value) {
		// TODO Auto-generated method stub
		
	}

}
