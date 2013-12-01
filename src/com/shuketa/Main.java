package com.shuketa.httpjsondb;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.shuketa.httpjsondb.db.DbJson;
import com.shuketa.httpjsondb.db.table.DbTableCity;
import com.shuketa.httpjsondb.http.Http;
import com.shuketa.httpjsondb.server.UidTable;
import com.shuketa.httpjsondb.util.Config;
import com.shuketa.httpjsondb.util.KLogger;
import com.shuketa.httpjsondb.util.Mail;
import com.shuketa.httpjsondb.util.PreventMultiProcess;

public class Main {

	static protected boolean todaysCheckResult = true;
	static protected Calendar previouEndTime = null; 
	static protected Calendar kanshiStartTime = null;
	
	public static void main(String[] args) {
		
		kanshiStartTime = Calendar.getInstance();
		testProgram("20131203","110000", 10);
		kanshiStartTime = Calendar.getInstance();
		testProgram("20131203","220000", 5);
		kanshiStartTime = Calendar.getInstance();
//		testProgram("20131202","230000", 1);
//		kanshiStartTime = Calendar.getInstance();
//		testProgram("20131202","205000", 1);
//		kanshiStartTime = Calendar.getInstance();
		
		if( previouEndTime == null ){
			todaysCheckResult = false;
			System.out.println("previouEndTime==null");
		} else {
			Integer lastHour = previouEndTime.get(Calendar.HOUR_OF_DAY);
			Integer lastMinute = previouEndTime.get(Calendar.MINUTE);
			if(( todaysCheckResult != false ) && (  (lastHour+4) < Config.checkEndHour()) ||
				( todaysCheckResult != false ) && (  (lastHour+4) == Config.checkEndHour() && (lastMinute==0))) {
				todaysCheckResult = false;
				System.out.println("lastHour is bad: " + lastHour);
			}
		}
	}
	
	static void testProgram(String dateStr, String minuteStr, Integer duration ) {
		System.out.println("CheckStart: " + dateStr+minuteStr);
		
		if (todaysCheckResult != false) {
			try {
				Calendar nowProgramStartTime = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date nowTime = sdf.parse(dateStr + minuteStr);
				nowProgramStartTime.setTime(nowTime);
				
				if (validateStartTime(nowProgramStartTime) == false) {
					todaysCheckResult = false;
				} else {
					calcProgramEndTime(dateStr,minuteStr,duration);
				}
			} catch (ParseException e) {
				e.printStackTrace();
				todaysCheckResult = false;
			}
		}
	}
	
	
	static boolean validateStartTime(Calendar nowProgramStartTime ){
		
		boolean checkResult = true;
		
		System.out.println("nowProgramStartTime: " + nowProgramStartTime.getTime());
		System.out.println("kanshiStartTime: " + kanshiStartTime.getTime());
		
		if( nowProgramStartTime.compareTo(kanshiStartTime) < 0)  {
			// 1.現在時刻よりも過去ならチェック対象外
			System.out.println("1.現在時刻よりも過去ならチェック対象外");
		} else if (nowProgramStartTime.get(Calendar.HOUR_OF_DAY) < Config.checkStartHour()) {
			// 2.チェック開始時刻よりも前ならチェック対処外
			System.out.println("2.チェック開始時刻よりも前ならチェック対処外");
		} else if ((nowProgramStartTime.get(Calendar.HOUR_OF_DAY) >= Config.checkStartHour()) && 
				(nowProgramStartTime.get(Calendar.HOUR_OF_DAY) < Config.checkEndHour())) {
			// 3.チェック対象範囲内だった場合
			if (previouEndTime==null) {
				// 3.1.前回がないなら今回のが9時から4時間の空きがないかチェックする
				Integer startHour = nowProgramStartTime.get(Calendar.HOUR_OF_DAY);
				startHour -= 4;
				if( startHour >= Config.checkStartHour()) {
					checkResult = false;
					System.out.println("3.1.チェックNG");
				}
			} else {
				// 3.2.前回があるなら前回の終了時刻をStartPointまでずらした上で今回のと差分チェック
				Integer previousEnd = previouEndTime.get(Calendar.HOUR_OF_DAY);
				if ( previousEnd < Config.checkStartHour() ) {
					previouEndTime.set(Calendar.HOUR_OF_DAY, Config.checkStartHour());
					previouEndTime.set(Calendar.MINUTE, 0);
					previouEndTime.set(Calendar.SECOND, 0);
					previouEndTime.add(Calendar.HOUR_OF_DAY, 4);
				} else {
					previouEndTime.add(Calendar.HOUR_OF_DAY, 4);
				}
				
				System.out.println("previouEndTime: " + previouEndTime.getTime());
				if (previouEndTime.compareTo(nowProgramStartTime) <= 0) {
					checkResult = false;
					System.out.println("3.2.チェックNG");
				}
			}
		} else if (nowProgramStartTime.get(Calendar.HOUR_OF_DAY) >= Config.checkEndHour()) {
			// 4.監視範囲よりも後ろから開始されてる場合
			if(previouEndTime==null) {
				checkResult = false;
				System.out.println("4.1.チェックNG");
			} else {
				// 4.2.チェック終了時刻よりも後なら、チェック終了時刻までの差分間隔をチェックする
				nowProgramStartTime.set(Calendar.HOUR_OF_DAY, Config.checkEndHour());
				nowProgramStartTime.set(Calendar.MINUTE, 0);
				nowProgramStartTime.set(Calendar.SECOND, 0);
				previouEndTime.add(Calendar.HOUR_OF_DAY,4);
				if(previouEndTime.compareTo(nowProgramStartTime) <= 0) {
					checkResult = false;
					System.out.println("4.2.チェックNG");
				}
			}
		}	
		return checkResult;
	}
	
	
	static void calcProgramEndTime(String dataStr, String minuteStr, Integer duration ){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date endTime = sdf.parse(dataStr + minuteStr);
			if( endTime != null ) {
				if( previouEndTime == null) {
					previouEndTime = Calendar.getInstance();
				}
				previouEndTime.setTime(endTime);
				Integer beforeHour = previouEndTime.get(Calendar.DAY_OF_MONTH);
				previouEndTime.add(Calendar.HOUR_OF_DAY,duration);
				Integer afterHour = previouEndTime.get(Calendar.DAY_OF_MONTH);
				if(afterHour != beforeHour) {
					Date lastHour = sdf.parse(dataStr + "235959");
					previouEndTime.setTime(lastHour);
				}
				System.out.println("previouEndTime: " + previouEndTime.getTime());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
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
