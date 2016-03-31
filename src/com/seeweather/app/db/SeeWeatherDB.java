package com.seeweather.app.db;

import java.util.ArrayList;
import java.util.List;
import com.seeweather.app.model.Area;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SeeWeatherDB {
	public static final String DB_NAME = "see_weather";// 数据库名称
	public static final int VERSION = 1;// 数据版本
	private SQLiteDatabase db;
	private static SeeWeatherDB seeWeatherDB;

	/**
	 * 将构造方法私有化
	 * @param context
	 */
	private SeeWeatherDB(Context context) {
		SeeWeatherOpenHelper dbHelper = new SeeWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	};

	/**
	 * 获取SeeWeatherDB实例
	 * @return
	 */
	public static SeeWeatherDB getInstance(Context context) {
		if (seeWeatherDB == null) {
			seeWeatherDB = new SeeWeatherDB(context);
		}
		return seeWeatherDB;
	}

	/**
	 * 将area实例保存至数据库中
	 */
	public void saveArea(Area area) {
		if (area != null) {
			ContentValues values = new ContentValues();
			values.put("province_cn", area.getProvinceName());
			values.put("district_cn", area.getDistrictName());
			values.put("name_cn", area.getAreaName());
			values.put("name_en", area.getNameEn());
			values.put("area_id", area.getAreaId());
			db.insert("Area", null, values);
		}
	}

	/**
	 * 根据输入的区域名称从数据库中读取相匹配的信息列表
	 */
	public List<Area> loadAreasFromDB(String areaName) {
		List<Area> areaList = new ArrayList<Area>();
		Cursor cursor = db.query("Area", null, "name_cn=?", new String[]{areaName}, null, null, null);
		while(cursor.moveToNext()){
			Area area = new Area();
			area.setId(cursor.getInt(cursor.getColumnIndex("id")));
			area.setProvinceName(cursor.getString(cursor.getColumnIndex("province_cn")));
			area.setDistrictName(cursor.getString(cursor.getColumnIndex("district_cn")));
			area.setAreaName(cursor.getString(cursor.getColumnIndex("name_cn")));
			area.setNameEn(cursor.getString(cursor.getColumnIndex("name_en")));
			area.setAreaId(cursor.getString(cursor.getColumnIndex("area_id")));			
			areaList.add(area);
		}
		return areaList;
	}
}
