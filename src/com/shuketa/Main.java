package com.shuketa;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.shuketa.db.Db;
import com.shuketa.db.table.DbTableChList;
import com.shuketa.db.table.DbTableCity;
import com.shuketa.db.table.DbTableCourse;
import com.shuketa.db.table.DbTableDate;
import com.shuketa.db.test.DbTestCase1;
import com.shuketa.http.Http;
import com.shuketa.server.City;
import com.shuketa.server.Prefectures;
import com.shuketa.server.UserId;
import com.shuketa.util.Config;
import com.shuketa.util.KLogger;
import com.shuketa.util.Mail;
import com.shuketa.util.PreventMultiProcess;

public class Main {

	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {

		KLogger.log().info("start main");
	
		for (Object arg : args) {
			if ((arg != null) && (arg.equals("PingFailed"))) {
				KLogger.log().info("PingFailed");
				Mail.sendMessage();
				return;
			}
		}
		
		Mail.testTest();
		if(multiProcessRunning()){
			return;
		}
		/*
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DAY_OF_MONTH, 1);
    	String tommorowStr = f.format(calendar.getTime());
    	if(tommorowStr.compareToIgnoreCase("20131122112501") < 0){
    		KLogger.log().info("期限切れ");
    	}
    	
//	    for (int i = 0 ; i < 18 ; i++){
//	    	calendar.add(Calendar.DAY_OF_MONTH, 1);
//	    	KLogger.log().info(f.format(calendar.getTime()));
//	    }
	    
	    
		KLogger.log().info("sendMailTo:" + Config.mailTo());
		
		Config.ignoreList();
		
		UserId userId = new UserId();
		Http.get(userId,userId,null);
		
		Db.saveAll(userId);
		
		Mail.sendMessage();
		
		 Thread.sleep(1000);
		 return;*/

		/*
		Prefectures prefectures = new Prefectures();
		DbTableCity cityTable = new DbTableCity();
        if( Http.get(prefectures,cityTable,null) == false ){

        }
        
        City city = new City();
        DbTableCourse courseTable = new DbTableCourse();
        if( Http.getWithDbTable(city, cityTable, courseTable) == false ){
        	
        }
        
        DbTableChList chList = new DbTableChList();
        if( Http.getWithDbTableWhere(city, chList, cityTable, null) == false ){
        	
        }
        
        DbTestCase1 case1 = new DbTestCase1();
  //      if(Db.test(case1) == false ){
        	
  //      }
        
        
        System.exit(0);
        */
	}
	
	
	static boolean multiProcessRunning(){
		PreventMultiProcess pmi;
		try {
			pmi = new PreventMultiProcess(new File("."));
			try {
				if (pmi.tryLock()) {
					pmi.release();
				} else {
					return true;// 既に起動中なので続行中止
				}
			} finally {
				pmi.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
		
		return false;
	}
	
}
