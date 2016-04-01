package com.seeweather.app.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.seeweather.app.R;
import com.seeweather.app.receiver.AutoUpdateReceiver;
import com.seeweather.app.util.HttpCallbackListener;
import com.seeweather.app.util.HttpUtil;
import com.seeweather.app.util.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity implements OnClickListener {

	private String httpUrl = "http://apis.baidu.com/apistore/weatherservice/cityid";
	private String httpArg = "cityid=";
	private TextView cityName, publicTime, currentData, weatherDesp,
			weatherTemp, weatherTempRange;
	private SharedPreferences sp;
	private Button changeCiyt, reflash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		sp = getSharedPreferences("weather", MODE_PRIVATE);
		cityName = (TextView) findViewById(R.id.city_name);
		publicTime = (TextView) findViewById(R.id.public_time);
		currentData = (TextView) findViewById(R.id.current_date);
		weatherDesp = (TextView) findViewById(R.id.weather_desp);
		weatherTemp = (TextView) findViewById(R.id.weather_temp);
		weatherTempRange = (TextView) findViewById(R.id.weather_temp_range);
		changeCiyt = (Button) findViewById(R.id.change_city);
		changeCiyt.setOnClickListener(this);
		reflash = (Button) findViewById(R.id.reflash_weather);
		reflash.setOnClickListener(this);
		String cityId = getIntent().getExtras().getString("cityId");
		sp.edit().putString("cityId", cityId);
		httpArg = httpArg + cityId;
		String address = httpUrl + "?" + httpArg;
		getWeatherInfoFromServer(address);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.change_city:
			Intent intent = new Intent(WeatherActivity.this,
					ChooseAreaActivity.class);
			intent.putExtra("isStartFromWeather", true);
			startActivity(intent);
			break;
		case R.id.reflash_weather:
			sp = getSharedPreferences("weather", MODE_PRIVATE);
			String cityId = sp.getString("cityId", "0");
			String newAaddress = httpUrl + "?" + cityId;
			getWeatherInfoFromServer(newAaddress);
			Toast.makeText(WeatherActivity.this, "更新成功！", Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			break;
		}

	}

	private void getWeatherInfoFromServer(String address) {

		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				Utility.handleWeatherResponseOfName(getApplicationContext(),
						response);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						cityName.setText(sp.getString("cityName", "0"));
						publicTime.setText("发布时间："
								+ sp.getString("public_time", "00"));// 更新发布时间
						currentData.setText(sp.getString("date",
								new SimpleDateFormat().format(new Date())));
						weatherDesp.setText(sp.getString("weather", "null"));
						weatherTemp.setText(sp.getString("temp", "null"));
						weatherTempRange.setText(sp.getString("lTemp", "0")
								+ "℃~" + sp.getString("hTemp", "0") + "℃");
						Intent i = new Intent(WeatherActivity.this,AutoUpdateReceiver.class);//启动自动更新服务
						startService(i);
					}
				});
			}

			@Override
			public void onError(Exception e) {

			}

		});
	}
}
