package com.shuketa.server;

import java.net.HttpURLConnection;

import com.shuketa.http.HttpGet;

public class Prefectures extends AbstractServer implements HttpGet{

	@Override
	public String requestUrl() {
		// TODO Auto-generated method stub
		return "json/Prefectures.json";
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

}
