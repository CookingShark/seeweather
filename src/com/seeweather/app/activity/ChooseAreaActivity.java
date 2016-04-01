package com.seeweather.app.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seeweather.app.R;
import com.seeweather.app.db.SeeWeatherDB;
import com.seeweather.app.model.Area;
import com.seeweather.app.util.HttpCallbackListener;
import com.seeweather.app.util.HttpUtil;
import com.seeweather.app.util.Utility;

public class ChooseAreaActivity extends Activity {
	private String ADDRESS = "http://apis.baidu.com/apistore/weatherservice/citylist?cityname=";
	private String searchArea;
	private String url;
	private List<String> lists;
	private ListView listView;
	private EditText etInput;// 输入区域
	private String cityId;

	private Button btSearch;
	private ArrayAdapter<String> adapter;
	private SeeWeatherDB seeWeatherDB;// 获取数据库工具类
	private boolean isStartFormWeather;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final SharedPreferences sp = getSharedPreferences("weather",
				MODE_PRIVATE);
		isStartFormWeather = getIntent().getBooleanExtra("isStartFromWeather", false);
		if (sp.getBoolean("city_selected", false) && !isStartFormWeather) {
			Intent intent = new Intent(ChooseAreaActivity.this,
					WeatherActivity.class);
			cityId = sp.getString("cityId", "0");
			searchArea = sp.getString("cityName", "");
			intent.putExtra("cityId", cityId);
			intent.putExtra("cityName", searchArea);
			startActivity(intent);
			finish();
			return;
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		etInput = (EditText) findViewById(R.id.input);
		btSearch = (Button) findViewById(R.id.search);
		listView = (ListView) findViewById(R.id.list_view);
		lists = new ArrayList<String>();
		seeWeatherDB = SeeWeatherDB.getInstance(this);// 初始化数据库工具类

		adapter = new ArrayAdapter<String>(ChooseAreaActivity.this,
				android.R.layout.simple_list_item_1, lists);
		listView.setAdapter(adapter);// 通过ListView对象的setAdapter方法设置填充器对象

		btSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchArea = etInput.getText().toString();
				if (!TextUtils.isEmpty(searchArea)) {
					sp.edit().putString("cityName", searchArea);
					List<Area> list2 = seeWeatherDB.loadAreasFromDB(searchArea);
					if (list2.size() == 0) {
						try {
							url = ADDRESS
									+ URLEncoder.encode(searchArea, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						queryAreaFormServer(searchArea);
					}
					list2 = seeWeatherDB.loadAreasFromDB(searchArea);
					cityId = seeWeatherDB.getCityIdFromDb(searchArea);
					sp.edit().putString("cityId", cityId);
					lists.clear();
					for (Area area : list2) {
						lists.add(area.toString());
					}
					adapter.notifyDataSetChanged();
					listView.setSelection(0);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent intent = new Intent(ChooseAreaActivity.this,
									WeatherActivity.class);
							intent.putExtra("cityId", cityId);
							startActivity(intent);
						}
					});
				} else {
					Toast.makeText(ChooseAreaActivity.this, "查询失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	/**
	 * 查询所有省级信息列表，优先从数据库中查询，如果没有再去服务器桑查询
	 */

	private void queryAreaFormServer(final String searchArea) {
		HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				Utility.handleAreaResponse(seeWeatherDB, response);
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(ChooseAreaActivity.this, "加载失败",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});

	}
	
	
}
