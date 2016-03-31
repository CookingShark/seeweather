package com.seeweather.app.util;

public interface HttpCallbackListener {
	
	void onFinish(String reponse);
	
	void onError(Exception e);
}
