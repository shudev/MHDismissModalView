package com.shuketa.db.test;

import java.util.HashMap;

import com.shuketa.db.DbTest;
import com.shuketa.db.DbTestErrorOperator;


public class DbTestCase1 implements DbTest{

	static protected HashMap<String,DbTestErrorOperator> mErrorOperator = new HashMap<String,DbTestErrorOperator>();

	@Override
	public HashMap<String,DbTestErrorOperator> getOperatorList() {
		return mErrorOperator;
	}

	
	void didDetectErrorRecord(){
		
		if(mErrorOperator.containsKey("999")){

			DbTestErrorOperator operator = mErrorOperator.get("999");
			operator.addErrorDetail("aaa");
		} else {
			DbTestErrorOperator operator = new DbTestErrorOperator();
			mErrorOperator.put("999", operator);
		}
	}
}
