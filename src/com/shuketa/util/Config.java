package com.shuketa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Config {
	
	/**
	 * ログ設定プロパティファイルのファイル名
	 */
	protected static final String CONFIG_FILE = "Config/testkun.properties";

	/**
	 * 自分自身（プログラム自身）が置かれているディレクトリへのパスを取得
	 * jar にして動かしている時は、/hoge/fuga/program.jar
	 * Eclipseからクラスファイルを実行している時は/fuge/workspace/prjname/bin
	 * コンソールなどから java -jar を使って、同じディレクトリで動かしたりすると、相対パスしか得られない
	 * を考慮
	 */
	static public String getCurrentDir(){

		String cp=System.getProperty("java.class.path");
		String fs=System.getProperty("file.separator");

		String acp=(new File(cp)).getAbsolutePath();

		File file = new File(acp);
		if (file.exists()==false){
			return "";
		}
		
		int p,q;
		for(p=0;(q=acp.indexOf(fs,p))>=0;p=q+1);

		return acp.substring(0,p);
	}
	static public int retryCount() {

		int retryConunt = 1;
		try {
			Properties prop = new Properties();
	        FileInputStream fis = new FileInputStream(getCurrentDir()+CONFIG_FILE); 
	        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
	        prop.load(isr);
	        retryConunt = Integer.parseInt(prop.getProperty("retrycount"));	         
	        isr.close();  
    
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		return retryConunt;
	}

	static public String mailTemplateFile() {

		String mailTemplateFile = null;

		try {
			Properties prop = new Properties();
	        FileInputStream fis = new FileInputStream(getCurrentDir()+CONFIG_FILE); 
	        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
	        prop.load(isr);
	        mailTemplateFile = prop.getProperty("mailTemplateFile");       
	        isr.close();  
     
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		return mailTemplateFile;
	}

	static public ArrayList<String> ignoreList() {
		
		ArrayList<String> list = null;
		
		try {
			Properties prop = new Properties();
	        FileInputStream fis = new FileInputStream(getCurrentDir()+CONFIG_FILE); 
	        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
	        prop.load(isr);
 
	        list=new ArrayList<String>();
	        String ignoreStr = prop.getProperty("ignoreGcc"); 
	        if(ignoreStr!=null){
				StringTokenizer st = new StringTokenizer(ignoreStr, ",");
				while (st.hasMoreTokens()) {
					String str = st.nextToken();
					list.add(str);
				}
			}
	        isr.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		return list;
	}
	
}
