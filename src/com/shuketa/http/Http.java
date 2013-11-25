package com.shuketa.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shuketa.db.Db;
import com.shuketa.db.DbTable;


public class Http {
	
	private static HttpURLConnection mConnection = null;
	
	/*
	 * 
	 */
    public static boolean get(HttpGet httpGet, DbTable saveDbTable, DbTable whereDbTable) {
   
//        try {
        	String requesUrl = httpGet.requestUrl();
        	
            //URL url = new URL(requesUrl);
        	/*
            try {
            	mConnection = (HttpURLConnection) url.openConnection();
            	mConnection.setRequestMethod("GET");
                httpGet.setRequestHeaderProperty(mConnection);
                mConnection.setConnectTimeout(60000);
                mConnection.setReadTimeout(60000);
				
                if (mConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                	
                    try {
                    	validate = httpGet.didReceiveData(mConnection.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                if (mConnection != null) {
                	mConnection.disconnect();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
        	try {
        		FileInputStream fs = new FileInputStream(requesUrl);
        		if( saveDbTable == null ){
            		InputStreamReader isr = new InputStreamReader(fs, "UTF8");
        		} else {
        	   		Db.saveJsonToDbTable(fs,saveDbTable);
        		}
//				httpGet.didReceiveData(fs);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        return false;
    }
    
    
    /*
     * DBから取得した結果を元にhttpGetを逐次実行して、DBに保存する
     */
    public static boolean getWithDbTable(HttpGet httpGet, DbTable selectTable, DbTable saveTable) {
    
		try {
			ResultSet record = Db.findRecord(selectTable);
			while (record.next()) {
				//System.out.println(record.getInt(1) + "\t" + record.getString(2));
				get(httpGet, saveTable,null);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
    }
    
    
	/*
	 * DBから取得した結果を元にhttpGetを逐次実行して、DBに保存する（insert条件付き）
	 */
	public static boolean getWithDbTableWhere(HttpGet httpGet,
			DbTable selectTable, DbTable saveTable, DbTable whereTable) {

		try {
			ResultSet record = Db.findRecord(selectTable);
			while (record.next()) {
				System.out.println(record.getInt(1) + "\t" +record.getString(2));
				get(httpGet, saveTable, whereTable);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
    
}
