package com.shuketa.db.table;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.shuketa.db.DbTable;

public class DbTableDate implements DbTable{

	public String DefaultInsertValue(){
		 SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");

		 java.util.Date today0 = new java.util.Date();
		 System.out.println("今日 = "+ f.format(today0));
		 java.util.Calendar cal = Calendar.getInstance();
		 cal.setTime(today0);
		 cal.add(Calendar.DAY_OF_MONTH, 1);
		 System.out.println("明日 = "+ f.format(cal.getTime()));
		
		return "INSERT INTO";
	}

	@Override
	public String tableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] columns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String selectWhere() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPreparedStatementParam(PreparedStatement pstmt)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didStartJsonObject() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PreparedStatement didEndJsonObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement didEndJsonArray() {
		// TODO Auto-generated method stub
		return null;
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
