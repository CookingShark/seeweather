package com.seeweather.app.service;

import com.seeweather.app.receiver.AutoUpdateReceiver;
import com.seeweather.app.util.HttpCallbackListener;
import com.seeweather.app.util.HttpUtil;
import com.seeweather.app.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
/**
 * �Զ�������������
 * @author л����
 */
public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				updateWeather();
			}
		}).start();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 1000 * 60 * 60 * 8;// 8Сʱ
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;// ����ϵͳʱ��
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);//�������õ�ϵͳʱ�ӷ��͹㲥
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * ��������
	 */
	protected void updateWeather() {
		SharedPreferences sp = getSharedPreferences("weather",
				Context.MODE_PRIVATE);
		String cityId = sp.getString("cityId", "0");
		String url = "http://apis.baidu.com/apistore/weatherservice/cityid?cityid="
				+ cityId;
		HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				Utility.handleWeatherResponseOfName(getApplicationContext(),
						response);
			}
			@Override
			public void onError(Exception e) {

			}
		});
	}
}
