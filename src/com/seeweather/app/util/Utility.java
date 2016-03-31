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
	 * �����ʹ�����������ص��б���Ϣ
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
}
