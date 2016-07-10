package com.siyuweather.app.util;

import android.text.TextUtils;
import android.util.Log;

import com.siyuweather.app.db.SiyuWeatherDB;
import com.siyuweather.app.model.City;
import com.siyuweather.app.model.County;
import com.siyuweather.app.model.Province;

public class Utility {

/**
 * 解析和处理服务器返回的省级数据
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
					//将解析出来的数据存储到Province表
					siyuWeatherDB.saveProvince(province);
				}
				return true;
			}
		}	
		return false;	
	}
	
	/**
	 * 解析和处理服务器返回的市级数据
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
				//	Log.d("执行到此：", "3333333333333333");
					//将解析出来的数据存储到City表
					siyuWeatherDB.saveCity(city);
				//	Log.d("执行到此：", "4444444444444444");
				}
				return true;
			}
		}	
		return false;	
	}
	
	/**
	 * 解析和处理服务器返回的县级数据
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
					//将解析出来的数据存储到County表
					siyuWeatherDB.saveCounty(county);
				}
				return true;
			}
		}	
		return false;	
	}
		
}
