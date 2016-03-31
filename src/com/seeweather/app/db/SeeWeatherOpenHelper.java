package com.seeweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SeeWeatherOpenHelper extends SQLiteOpenHelper {
	
	/**
	 * 创建区域数据库(id, province_cn, district_cn, name_cn, name_en,area_id
	 */
	public static final String CREATE_AREA = "create table Area("+
			"id integer primary key autoincrement,"+
			"province_cn text," +
			"district_cn text," +
			"name_cn text," +
			"name_en text," +
			"area_id text)";
	
	public SeeWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_AREA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
