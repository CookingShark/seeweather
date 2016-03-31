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
	 * �����ʹ�����������صĳ�����Ϣ
	 */
	public  static boolean handleAreaResponse(SeeWeatherDB seeWeatherDB,String response) {
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject jsonObject = new JSONObject(response);// ͨ��JSONObject��ʽ����JSON����
				String errNum = jsonObject.getString("errNum");// JSON�����еĲ�ѯ����룺0��ʾ��ѯ�ɹ���-1��ʾδ��ѯ��
				String errMsg = jsonObject.getString("errMsg");// JSON�����еĲ�ѯ�����success��ʾ��ѯ�ɹ���ʧ����Ϊ����ѯ����[XX]�����ڡ�
				if (errMsg.equals("success")) {
					JSONArray jsonArray = jsonObject.getJSONArray("retData");// ����������Ϣ���Ǹ�JSON���飬����ÿһ�����һ��������Ϣ����ѯ�ɹ��򷵻ص��Ǹ��ݲ�ѯ���򷵻ص�����ƥ���������ɵļ���
					// ���ѯ���㽭�����򷵻��㽭����������������ɵļ��ϣ���81����
					// ���ѯ�����Ρ����򷵻�һ��
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
	 * �����ӷ��������ص�������Ϣ�����ݳ���id���������䱣����SharedPreferences������
	 * @param response
	 */
	public static void handleWeatherResponseOfName(Context context,String response){
		SharedPreferences sp = context.getSharedPreferences("weather",context.MODE_PRIVATE);
		Editor editor = sp.edit();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��", Locale.CHINA);
		try {
			JSONObject allInfo = new JSONObject(response);//��ȡ���������ص�json����
			JSONObject retData = allInfo.getJSONObject("retData");//��ȡretData��ɵ�JSON����			
			editor.putString("public_time", retData.getString("time"));//����ʱ��			
			editor.putString("date", sdf.format(new Date()));//����
			editor.putString("weather", retData.getString("weather"));//�������
			editor.putString("temp", retData.getString("temp"));//��ǰ�¶�						
			editor.putString("lTemp", retData.getString("l_tmp"));//����¶�
			editor.putString("hTemp", retData.getString("h_tmp"));//����¶�
			editor.commit();			
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}
}
