package com.siyuweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.siyuweather.app.db.SiyuWeatherDB;
import com.siyuweather.app.model.City;
import com.siyuweather.app.model.County;
import com.siyuweather.app.model.Province;

public class Utility {

/**
 * �����ʹ�����������ص�ʡ������
 * */
	public synchronized static boolean handleProvinceResponse(SiyuWeatherDB siyuWeatherDB, String response){
		if( !TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length > 0){
				for(String p : allProvinces){
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//���������������ݴ洢��Province��
					siyuWeatherDB.saveProvince(province);
				}
				return true;
			}
		}	
		return false;	
	}
	
	/**
	 * �����ʹ�����������ص��м�����
	 * */
	public static boolean handleCitiesResponse(SiyuWeatherDB siyuWeatherDB, String response,int provinceId){
		if( !TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length > 0){
				for(String c : allCities){
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
				//	Log.d("ִ�е��ˣ�", "3333333333333333");
					//���������������ݴ洢��City��
					siyuWeatherDB.saveCity(city);
				//	Log.d("ִ�е��ˣ�", "4444444444444444");
				}
				return true;
			}
		}	
		return false;	
	}
	
	/**
	 * �����ʹ�����������ص��ؼ�����
	 * */	
	public static boolean handleCountiesResponse(SiyuWeatherDB siyuWeatherDB, String response,int cityId){
		if( !TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if(allCounties != null && allCounties.length > 0){
				for(String c : allCounties){
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					//���������������ݴ洢��County��
					siyuWeatherDB.saveCounty(county);
				}
				return true;
			}
		}	
		return false;	
	}
	
	/**
	 * �������������ص�JSON���ݣ����������������ݴ洢������
	 * */	
	public static void handleWeatherResponse(Context context,String response){
		try{
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			Log.d("ִ�е��ˣ�", "9999999999999");
			//�������������ݴ洢������SharedPreferences�ļ���
			saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
			Log.d("ִ�е��ˣ�", "12121212121212");
		}catch(JSONException e){
			e.printStackTrace();
		}
	}

	/**
	 * �����������ص�����������Ϣ�洢��SharedPreferences�ļ���
	 * */	
	public static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyy��M��d��",Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);//��־λ�����ǰ�Ƿ���ѡ��һ������
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));// java.util.Date��java.sql.Date��������໥ת��
		Log.d("ִ�е��ˣ�", "10101010101");
		editor.commit();
	}
}


