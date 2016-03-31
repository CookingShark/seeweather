package com.seeweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import com.seeweather.app.model.Area;

import android.text.TextUtils;

public class HttpUtil {

	/**
	 * 从服务器加载area列表：
	 * http://apis.baidu.com/apistore/weatherservice/citylist?cityname=URL格式的区域名
	 * 返回的JSON数据格式为：{ errNum: 0, //返回结果状态码 errMsg: "success", //返回结果描述 retData:
	 * [ { province_cn: "北京", //省 district_cn: "北京", //市 name_cn: "朝阳", //区、县
	 * name_en: "chaoyang", //城市拼音 area_id: "101010300" //城市代码 }, { province_cn:
	 * "辽宁", district_cn: "朝阳", name_cn: "朝阳", name_en: "chaoyang", area_id:
	 * "101071201" }, ... ] 其中retData:中的JSON数组就是需要的数据
	 * 
	 * @param address
	 * @param listener
	 */
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
//		HttpURLConnection conn = null;
//		
//		try {
//			URL url = new URL(address);
//			conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("GET");
//			conn.setRequestProperty("Accept-Encoding", "UTF-8");
//			conn.setRequestProperty("apikey",
//					"92a890df6546b2cdd32abb55cbcc80b3");
//			conn.setConnectTimeout(8000);
//			conn.setReadTimeout(8000);
//			InputStream inputStream = conn.getInputStream();
//			BufferedReader reader = new BufferedReader(
//					new InputStreamReader(inputStream,"UTF-8"));
//			StringBuffer response = new StringBuffer();
//			String line = null;
//			while((line = reader.readLine()) != null){
//				response.append(line);
//			}
//			if(listener != null){
//				listener.onFinish(response.toString());//回调onFinish()方法
//			}
//		} catch (Exception e) {
//			if(listener != null){
//				listener.onError(e);//回调onError方法
//			}
//		}finally{
//			if(conn != null){
//				conn.disconnect();
//			}
//		}

		new Thread(new Runnable() {
          HttpURLConnection conn = null;
			@Override
			public void run() {
				HttpURLConnection conn = null;
				try {
					URL url = new URL(address);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Accept-Encoding", "UTF-8");
					conn.setRequestProperty("apikey",
							"92a890df6546b2cdd32abb55cbcc80b3");
					conn.setConnectTimeout(8000);
					conn.setReadTimeout(8000);
					InputStream inputStream = conn.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream,"UTF-8"));
					StringBuffer response = new StringBuffer();
					String line = null;
					while((line = reader.readLine()) != null){
						response.append(line);
					}
					if(listener != null){
						listener.onFinish(response.toString());//回调onFinish()方法
					}
				} catch (Exception e) {
					if(listener != null){
						listener.onError(e);//回调onError方法
					}
				}finally{
					if(conn != null){
						conn.disconnect();
					}
				}
			}
		}).start();
	}
}
