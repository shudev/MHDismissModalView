package com.shuketa.server;

import java.net.HttpURLConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.shuketa.db.DbTable;
import com.shuketa.http.HttpGet;
import com.shuketa.util.Config;
import com.shuketa.util.KLogger;

public class UserId extends AbstractServer implements HttpGet,DbTable{

	String mUserID;

	@Override
	public String tableName() {
		return "m_userid";
	}

	@Override
	public String[] columns() {
		return new String[]{"userid"};
	}

	@Override
	public Object[] values() {
		return new Object[]{"kasai"};
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
	public String requestUrl() {
		return "json/userid.json";
	}

	@Override
	public String requestGetParam() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRequestHeaderProperty(HttpURLConnection connection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String selectWhere() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createTableSql() {
		return "create table m_userid " +
				"(" + "userid varchar(5) )";
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
		KLogger.log().info(key+":"+value);
		if(key.equals("userid")){
			mUserID = value;
		}
		
	}

}
