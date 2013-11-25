package com.shuketa.http;

import java.io.InputStream;
import java.net.HttpURLConnection;

public interface HttpGet {
	String requestUrl();
    String requestGetParam();
    void setRequestHeaderProperty(HttpURLConnection connection);
    boolean didReceiveData(InputStream inputStream);
}
