package com.seeweather.app.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.text.TextUtils;
import com.seeweather.app.db.SeeWeatherDB;
import com.seeweather.app.model.Area;

public class Utility {
	private static List<Area> areaList = null;
	/**
	 * 解析和处理服务器返回的列表信息
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
}
