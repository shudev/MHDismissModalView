package com.shuketa.db;

public class DbTestErrorOperator {
	
	private String mOperatorCode = null;
	private String companyName = null;
	private StringBuilder mErrorDetail = new StringBuilder();
	
	public String getOperatorCode() {
		return mOperatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.mOperatorCode = operatorCode;
	}
	
	public String getErrorDetail() {
		return mErrorDetail.toString();
	}
	public void addErrorDetail(String errorDetail) {
		this.mErrorDetail.append(errorDetail);
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
	boolean equals(String s1, String s2){
	    if(this.mOperatorCode.equals(s1)){
	    	return true;
	    } else {
	    	return false;
	    }
	  }

	@Override
	public boolean equals(Object cp) {
		if (cp instanceof DbTestErrorOperator == false)
			return false;
		if (this.mOperatorCode.equals(((DbTestErrorOperator) cp).mOperatorCode)) {
			return true;
		} else {
			return false;
		}
	}

	  
}
