package com.seeweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.seeweather.app.model.City;
import com.seeweather.app.model.County;
import com.seeweather.app.model.Province;

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
	 * 
	 * @param context
	 */
	private SeeWeatherDB(Context context) {
		SeeWeatherOpenHelper dbHelper = new SeeWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	};

	/**
	 * 获取SeeWeatherDB实例
	 * 
	 * @return
	 */
	public static SeeWeatherDB getInstance(Context context) {
		if (seeWeatherDB == null) {
			seeWeatherDB = new SeeWeatherDB(context);
		}
		return seeWeatherDB;
	}

	/**
	 * 将province实例保存至数据库的Province表中
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}

	/**
	 * 从数据库中读取所有省份信息列表
	 */
	public List<Province> loadProvincese() {
		List<Province> provinceList = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				Province province = new Province();
				province.setId(cursor.getInt(cursor
						.getColumnIndex("province_id")));
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				provinceList.add(province);
			}
		}
		return provinceList;
	}

	/**
	 * 将City实例保存至数据库中的City表中
	 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}

	/**
	 * 从数据库中获取指定ProvinceId下的City列表
	 */
	public List<City> loadCities(int provinceId) {
		List<City> citiesList = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				city.setProvinceId(cursor.getInt(cursor
						.getColumnIndex("province_id")));
				citiesList.add(city);
			}
		}
		return citiesList;
	}

	/**
	 * 将County实例保存至数据库中的County表中
	 */
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}

	/**
	 * 根据CityId获取所有县、区列表
	 */
	public List<County> loadCouties(int cityId) {
		List<County> countyList = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id=?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		if(cursor.moveToFirst()){
			while(cursor.moveToNext()){
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName((cursor.getString(cursor.getColumnIndex("county_name"))));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
				countyList.add(county);
			}
		}
		return countyList;
	}
}
