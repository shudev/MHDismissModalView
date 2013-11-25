package com.shuketa.server;

import java.io.InputStream;
import java.net.HttpURLConnection;

import com.shuketa.http.HttpGet;

public class City extends AbstractServer implements HttpGet{


	@Override
	public String requestUrl() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public boolean didReceiveData(InputStream inputStream) {
		// TODO Auto-generated method stub
		return false;
	}

}
