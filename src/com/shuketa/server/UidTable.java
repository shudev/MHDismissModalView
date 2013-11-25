package com.shuketa.server;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.shuketa.db.DbTable;
import com.shuketa.http.HttpGet;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;

/*
 * モデル（データおよび構造を意識する層。ロジックを意識しない）
 */
public class UidTable extends AbstractServer implements HttpGet{

    public UidTable() {
    }
    
	@Override
	public String requestUrl() {
//		return "http://express.heartrails.com/api/json?method=getAreas";
		return "http://eu.battle.net/auction-data/258993a3c6b974ef3e6f22ea6f822720/auctions.json";		
	}

	@Override
	public void setRequestHeaderProperty(HttpURLConnection connection) {
//		connection.setRequestProperty("", "");
//		connection.setRequestProperty("Accept-Language", "");
	}
	
    @Override
    public boolean didReceiveData(InputStream inputStream) {
    	
        JsonParser parser =  Json.createParser(inputStream);

        while(parser.hasNext()){
            JsonParser.Event event = parser.next();
            switch(event){
                case KEY_NAME:
                    System.out.println("Key  =" + parser.getString());
                    break;
                case VALUE_STRING:
                    System.out.println("Value=" + parser.getString());
                    break;
                default:
                    break;
            }
        }	
        
        return true;
    }
   

	@Override
	public String requestGetParam() {
		// TODO Auto-generated method stub
		return null;
	}
}
