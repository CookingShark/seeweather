package com.seeweather.app.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
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
	private List<Area> areaList;
	private List<String> lists;
	private ListView listView;
	private TextView tvTitle;// 标题
	private ListView list;// 列表
	private EditText etInput;// 输入区域

	private Button btSearch;
	private ArrayAdapter<String> adapter;
	private SeeWeatherDB seeWeatherDB;// 获取数据库工具类
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		tvTitle = (TextView) findViewById(R.id.title_text);
		etInput = (EditText) findViewById(R.id.input);
		btSearch = (Button) findViewById(R.id.search);
		listView = (ListView) findViewById(R.id.list_view);
		lists = new ArrayList<String>();
		seeWeatherDB = SeeWeatherDB.getInstance(this);// 初始化数据库工具类

		adapter = new ArrayAdapter<String>(ChooseAreaActivity.this,
				android.R.layout.simple_list_item_1, lists);
		listView.setAdapter(adapter);// 通过ListView对象的setAdapter方法设置填充器对象
		// list.setOnItemClickListener(new OnItemClickListener() {// 设置列表点击事件监听器
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		//
		// }
		// });
		btSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchArea = etInput.getText().toString();
				if (!TextUtils.isEmpty(searchArea)) {
					List<Area> list2 = seeWeatherDB.loadAreasFromDB(searchArea);
					if(list2.size() == 0){
						try {
							url = ADDRESS + URLEncoder.encode(searchArea,"UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						queryAreaFormServer(searchArea);
						list2 = seeWeatherDB.loadAreasFromDB(searchArea);
					}
					lists.clear();
					for(Area area : list2){
						lists.add(area.toString());						
					}						
					adapter.notifyDataSetChanged();
					listView.setSelection(0);
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
