package com.siyuweather.app.activity;

import com.siyuweather.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity {

	private LinearLayout weatherInfoLayout;
	
	/**
	 * 用于显示城市名
	 * */
	private TextView cityNameText;
	
	/**
	 * 用于显示发布时间
	 * */
	private TextView publishText;
	
	/**
	 * 用于显示天气描述信息
	 * */
	private TextView weatherDespText;
	/**
	 * 用于显示气温1
	 * */
	private TextView temp1Text;
	
	/**
	 * 用于显示气温2
	 * */
	private TextView temp2Text;
	
	/**
	 * 用于显示当前日期
	 * */
	private TextView currentDateText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		//初始化各控件
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = 
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

	
}
