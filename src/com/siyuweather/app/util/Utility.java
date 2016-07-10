package com.siyuweather.app.util;

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
					city.setId(provinceId);
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
					county.setId(cityId);
					//���������������ݴ洢��County��
					siyuWeatherDB.saveCounty(county);
				}
				return true;
			}
		}	
		return false;	
	}
		
}
