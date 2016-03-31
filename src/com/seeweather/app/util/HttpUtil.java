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
	 * �ӷ���������area�б�
	 * http://apis.baidu.com/apistore/weatherservice/citylist?cityname=URL��ʽ��������
	 * ���ص�JSON���ݸ�ʽΪ��{ errNum: 0, //���ؽ��״̬�� errMsg: "success", //���ؽ������ retData:
	 * [ { province_cn: "����", //ʡ district_cn: "����", //�� name_cn: "����", //������
	 * name_en: "chaoyang", //����ƴ�� area_id: "101010300" //���д��� }, { province_cn:
	 * "����", district_cn: "����", name_cn: "����", name_en: "chaoyang", area_id:
	 * "101071201" }, ... ] ����retData:�е�JSON���������Ҫ������
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
//				listener.onFinish(response.toString());//�ص�onFinish()����
//			}
//		} catch (Exception e) {
//			if(listener != null){
//				listener.onError(e);//�ص�onError����
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
						listener.onFinish(response.toString());//�ص�onFinish()����
					}
				} catch (Exception e) {
					if(listener != null){
						listener.onError(e);//�ص�onError����
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
