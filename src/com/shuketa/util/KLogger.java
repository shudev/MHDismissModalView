package com.shuketa.util;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.LogManager;

public class KLogger {
	
	private static final String LOGGER_NAME = "TestkunLogger";
    private static final Logger LOGGER;
    private static FileHandler handler = null;
    private static boolean outputFile = false;
	static {
		LOGGER = Logger.getLogger(LOGGER_NAME); 
        
        //ファイルに出力するハンドラーを生成
        
		try {
			Properties prop = new Properties();
			FileInputStream fis = null;
			
			fis = new FileInputStream(Config.getCurrentDir() + "Config/testkun.properties");

			if (fis != null) {

				InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				prop.load(isr);
				isr.close();
				String pattern = null;
				
				try{
					// ログファイル名
					pattern = prop.getProperty("java.util.logging.FileHandler.pattern");
					if (pattern != null && !prop.isEmpty()) {
						String[] tokens = pattern.split("\\/");
						for (int i = 0; i < tokens.length - 1; i++) {
							String token = tokens[i];
							File file = new File(Config.getCurrentDir() + token);
							if (!file.isFile() && !file.exists()) {
								file.mkdir();
							}
						}
					}
					
					pattern =  Config.getCurrentDir() + pattern;
						
				}catch(Exception e){
 
				}
				
				// ログファイルの上限数
				int count = 10;	
				try{
					count = Integer.parseInt(prop.getProperty("java.util.logging.FileHandler.count"));
				} catch(Exception e){
					
				}
				
				// カスタマイズしたFileHandler
				handler = new FileHandler(pattern, 0, count, false);
				
				handler.setFormatter(new SimpleFormatter());
				
				try{
					// #ログファイル出力レベル(FINE:詳細 INFO:情報  WARNING:警告  SEVERE:致命的)
					String level = prop.getProperty("java.util.logging.FileHandler.level");
					if(level==null){
						handler.setLevel(Level.WARNING);
						LOGGER.setLevel(Level.WARNING);
					}else if(level.equals("FINE")){
						handler.setLevel(Level.FINE);
						LOGGER.setLevel(Level.FINE);
					}else if(level.equals("INFO")){
						handler.setLevel(Level.INFO);
						LOGGER.setLevel(Level.INFO);
					}else if(level.equals("WARNING")){
						handler.setLevel(Level.WARNING);
						LOGGER.setLevel(Level.WARNING);
					}else if(level.equals("SEVERE")){
						handler.setLevel(Level.SEVERE);
						LOGGER.setLevel(Level.SEVERE);
					} else {
						handler.setLevel(Level.WARNING);
						LOGGER.setLevel(Level.WARNING);
					}
				}catch(Exception e){
					handler.setLevel(Level.WARNING);
					LOGGER.setLevel(Level.WARNING);
				}
				
				if(pattern!=null){
					if(outputFile){
					// "MyLogger"に作成したハンドラーを設定
					LOGGER.addHandler(handler);
					// 親ロガーへ通知しない
					LOGGER.setUseParentHandlers(false);
					}
				} else {
					handler = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			handler = null;
		}        
    }
    
	
	static public Logger log(){
		return LOGGER;
	}
}
