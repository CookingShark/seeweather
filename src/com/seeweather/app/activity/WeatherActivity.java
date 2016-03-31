package com.seeweather.app.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.seeweather.app.R;
import com.seeweather.app.util.HttpCallbackListener;
import com.seeweather.app.util.HttpUtil;
import com.seeweather.app.util.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class WeatherActivity extends Activity {

	private String httpUrl = "http://apis.baidu.com/apistore/weatherservice/cityid";
	private String httpArg = "cityid=";
	private TextView cityName, publicTime, currentData, weatherDesp,
			weatherTemp, weatherTempRange;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		cityName = (TextView) findViewById(R.id.city_name);
		publicTime = (TextView) findViewById(R.id.public_time);
		currentData = (TextView) findViewById(R.id.current_date);
		weatherDesp = (TextView) findViewById(R.id.weather_desp);
		weatherTemp = (TextView) findViewById(R.id.weather_temp);
		weatherTempRange = (TextView) findViewById(R.id.weather_temp_range);
		String cityId = (String) getIntent().getExtras().get("cityId");
		cityName.setText((String) getIntent().getExtras().get("cityName"));
		httpArg = httpArg + cityId;
		String address = httpUrl + "?" + httpArg;
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				Utility.handleWeatherResponseOfName(getApplicationContext(),
						response);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						SharedPreferences sp = getSharedPreferences("weather",
								MODE_PRIVATE);
						publicTime.setText("发布时间："
								+ sp.getString("public_time", "00"));// 更新发布时间
						currentData.setText(sp.getString("date",
								new SimpleDateFormat().format(new Date())));
						weatherDesp.setText(sp.getString("weather", "null"));
						weatherTemp.setText(sp.getString("temp", "null"));
						weatherTempRange.setText(sp.getString("lTemp", "0")
								+ "℃~" + sp.getString("hTemp", "0") + "℃");
					}
				});
			}

			@Override
			public void onError(Exception e) {

			}
		});

	}
}
