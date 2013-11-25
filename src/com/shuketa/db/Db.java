package com.shuketa.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.stream.JsonParser;

import com.shuketa.db.test.DbTestCase1;
import com.shuketa.util.Config;
import com.shuketa.util.KLogger;

public class Db {
	
	private static final String DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	
	static{
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static Connection getDBConnection() {
		KLogger.log().info("getDBConnection");
		Connection dbConnection = null;
		try {
			dbConnection = DriverManager.getConnection("jdbc:derby:"+ Config.getCurrentDir() + "db;create=true");
			KLogger.log().info("return dbConnectio");
			return dbConnection;
		} catch (SQLException e) {
			KLogger.log().info("DB_CONNECTION error");
			e.printStackTrace();
		}
		return dbConnection;
	}
	
	public static ResultSet findRecord(DbTable model) throws SQLException {

		Connection dbConnection = getDBConnection();
		PreparedStatement pstmt = createSqlSelectParam(model,dbConnection);
		ResultSet set = pstmt.executeQuery();
        
		return set;
	}
	

	public static boolean saveAll(DbTable... models) {
		KLogger.log().info("saveAll");
		
		Connection dbConnection = null;
		boolean updateResult = false;
		
		try{
			dbConnection = getDBConnection();
			if(dbConnection==null){
				KLogger.log().warning("dbConnection is null");
			}
			
			KLogger.log().info("dbConnection.setAutoCommit(false)");
			dbConnection.setAutoCommit(false);
			
			KLogger.log().info("(DbTable model : models)");
			for (DbTable model : models) {
				PreparedStatement pstmt = null;
				try{
					pstmt = dbConnection.prepareStatement( "drop table "+ model.tableName());
					KLogger.log().info("pstmt.executeUpdate()");
					pstmt.executeUpdate();
					dbConnection.commit();
				}catch(SQLException e){
					
				}

				KLogger.log().info("prepareStatement(model.createTableSql())");
				
				pstmt = dbConnection.prepareStatement(model.createTableSql());
				pstmt.executeUpdate();
				dbConnection.commit();
				pstmt = createSqlInsertParam(model,dbConnection);
				pstmt.executeUpdate();
				dbConnection.commit();
			}
			updateResult = true;
			
		} catch (SQLException e){
			updateResult = false;
			e.printStackTrace();
		} finally {
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return updateResult;
	}
	
	
	public static PreparedStatement createSqlInsertParam(
			DbTable model, Connection dbConnection) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ");
		sql.append(model.tableName());
		sql.append("(");
		for (String column : model.columns()) {
			sql.append(column);
			sql.append(", ");
		}
		int lastComma = sql.lastIndexOf(",");
		if (lastComma != -1) {
			sql.deleteCharAt(lastComma);
		}
		sql.append(") VALUES(");
		for (Object value : model.values()) {
			if (value instanceof Integer) {
				sql.append(value);sql.append(",");
			} else {
				sql.append("'");
				sql.append(value);
				sql.append("'");sql.append(",");
			}
		}
		lastComma = sql.lastIndexOf(",");
		if (lastComma != -1) {
			sql.deleteCharAt(lastComma);
		}
		sql.append(")");

		// String sql = "insert into kabukatable (code, company) values (?, ?)";
		KLogger.log().info(sql.toString());
		PreparedStatement pstmt = dbConnection.prepareStatement(sql.toString());
		model.setPreparedStatementParam(pstmt);
		return pstmt;
	}
	
	// ("SELECT id, name, value FROM data")
	public static PreparedStatement createSqlSelectParam(
			DbTable model, Connection dbConnection) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ");
		sql.append(model.tableName());
		String where = model.selectWhere();
		if( where != null ){
			sql.append(" ");
			sql.append(where);
		}

		// String sql = "insert into kabukatable (code, company) values (?, ?)";
		PreparedStatement pstmt = dbConnection.prepareStatement(sql.toString());
		return pstmt;
	}
	
	
	/*
	 * JSONのstreamを上から流してDatabaseのtableに流し込む
	 */
	public static void saveJsonToDbTable(InputStream isr, DbTable dbTable){
		
		Connection dbConnection = null;
		PreparedStatement sql = null;
		try {

			dbConnection = getDBConnection();
			dbConnection.setAutoCommit(false);

			JsonParser parser = Json.createParser(isr);

			String keyString = null;

			while (parser.hasNext()) {
				JsonParser.Event event = parser.next();
				switch (event) {

				case START_OBJECT:
					System.out.print(" KEY:" + keyString + " " + event + "-->");
					dbTable.didStartJsonObject();
					break;
				case END_OBJECT:
					System.out.print("<--" + event + " ");
					sql = dbTable.didEndJsonObject();
					if (sql != null) {
						sql.executeUpdate();
					}
					break;
				case START_ARRAY:
					System.out.print(" KEY:" + keyString + " " + event + "-->");
					dbTable.didStartJsonArray(keyString);
					break;
				case END_ARRAY:
					System.out.print("<--" + event + " ");
					sql = dbTable.didEndJsonArray();
					if (sql != null) {
						sql.executeUpdate();
					}
					break;
				case VALUE_NUMBER:
					System.out.println(keyString + " = " + parser.getInt());
					dbTable.didValueNumber(keyString,parser.getInt());
					break;
				case VALUE_STRING:
					System.out.println(keyString + " = " + parser.getString());
					dbTable.didValueString(keyString,parser.getString());
					break;
				case VALUE_TRUE:
					// System.out.print("true/*" + event + "*/, ");
					dbTable.didValueTrue();
					break;
				case VALUE_FALSE:
					// System.out.print("false/*" + event + "*/, ");
					dbTable.didValueFalse();
					break;
				case VALUE_NULL:
					// System.out.print("null/*" + event + "*/, ");
					break;
				case KEY_NAME:
					keyString = parser.getString();
					// System.out.println("Key  =" + parser.getString());
					dbTable.didKeyName(parser.getString());
					break;

				default:
					System.out.print("Unrecognized Event : ");
				}
			}
			
			if( sql != null ){
				sql.close();
			}
			dbConnection.commit();
			
		} catch( JsonException e1){
			try{
				dbConnection.rollback();
			}catch (SQLException e3){
				e3.printStackTrace();
			}
		} catch (SQLException e2) {
			try{
				dbConnection.rollback();
			}catch (SQLException e3){
				e3.printStackTrace();
			}
		} finally {
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static boolean validate(DbTestCase1 case1) {
		// TODO Auto-generated method stub
		return false;
	}

}
