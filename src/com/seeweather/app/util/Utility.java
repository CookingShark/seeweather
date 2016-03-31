package com.seeweather.app.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.seeweather.app.db.SeeWeatherDB;
import com.seeweather.app.model.Area;

public  class Utility {
	private static List<Area> areaList = null;
	/**
	 * 解析和处理服务器返回的城市信息
	 */
	public  static boolean handleAreaResponse(SeeWeatherDB seeWeatherDB,String response) {
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject jsonObject = new JSONObject(response);// 通过JSONObject方式解析JSON数据
				String errNum = jsonObject.getString("errNum");// JSON数据中的查询结果码：0表示查询成功，-1表示未查询到
				String errMsg = jsonObject.getString("errMsg");// JSON数据中的查询结果：success表示查询成功，失败则为“查询城市[XX]不存在”
				if (errMsg.equals("success")) {
					JSONArray jsonArray = jsonObject.getJSONArray("retData");// 这是区域信息，是个JSON数组，里面每一项代表一个区域信息，查询成功则返回的是根据查询区域返回的所有匹配的区域组成的集合
					// 如查询“浙江”，则返回浙江下属所有区、县组成的集合，共81个，
					// 如查询“永嘉”，则返回一个
					for(int i = 0; i < jsonArray.length(); i++){
						JSONObject json = (JSONObject) jsonArray.opt(i);
						Area area = new Area();
						area.setProvinceName(json.getString("province_cn"));
						area.setDistrictName(json.getString("district_cn"));
						area.setAreaName(json.getString("name_cn"));
						area.setNameEn(json.getString("name_en"));
						area.setAreaId(json.getString("area_id"));
						seeWeatherDB.saveArea(area);
						return true;
					}
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;

			}
		}
		return false;
	}
	/**
	 * 解析从服务器返回的天气信息（根据城市id），并将其保存至SharedPreferences对象中
	 * @param response
	 */
	public static void handleWeatherResponseOfName(Context context,String response){
		SharedPreferences sp = context.getSharedPreferences("weather",context.MODE_PRIVATE);
		Editor editor = sp.edit();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		try {
			JSONObject allInfo = new JSONObject(response);//获取服务器返回的json数据
			JSONObject retData = allInfo.getJSONObject("retData");//获取retData组成的JSON数据			
			editor.putString("public_time", retData.getString("time"));//发布时间			
			editor.putString("date", sdf.format(new Date()));//日期
			editor.putString("weather", retData.getString("weather"));//天气情况
			editor.putString("temp", retData.getString("temp"));//当前温度						
			editor.putString("lTemp", retData.getString("l_tmp"));//最低温度
			editor.putString("hTemp", retData.getString("h_tmp"));//最高温度
			editor.commit();			
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}
}
