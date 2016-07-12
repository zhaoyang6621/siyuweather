package com.siyuweather.app.activity;

import com.siyuweather.app.R;
import com.siyuweather.app.util.HttpCallbackListener;
import com.siyuweather.app.util.HttpUtil;
import com.siyuweather.app.util.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{

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
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		//从Intent中取出县级代号
		String countyCode = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			//有县级代号时就去查询天气
			publishText.setText("同步中。。。");
			weatherInfoLayout.setVisibility(View.INVISIBLE);//设置不显示，但仍然占据可见时的大小和位置。
			cityNameText.setVisibility(View.INVISIBLE);
			Log.d("执行到此：", "444444444444");
			queryWeatherCode(countyCode);
		}else{
			//没有县级代号时就直接显示本地存储的天气
			showWeather();
			Log.d("执行到此：", "13131323134");
		}
	}
	
	/**
	 * 查询县级代号所对应的天气代号
	 * */
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
		//Log.d("所要查询的县所对应的address为：", address);
		Log.d("执行到此：", "55555555555");
		queryFromServer(address,"countyCode");
	}

	/**
	 * 查询天气代号所对应的天气
	 * */
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		queryFromServer(address,"weatherCode");
	}
	
	/**
	 * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
	 * */
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(final String response) {
				if ("countyCode".equals(type)) {
					if(!TextUtils.isEmpty(response)){
						//从服务器返回的数据中解析出天气代号
						String[] array = response.split("\\|");
						if(array != null && array.length == 2){
							String weatherCode = array[1];
							Log.d("执行到此：", "6666666666666");
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)) {
					Log.d("执行到此：", "7777777");
					//处理服务器返回的天气信息
					Utility.handleWeatherResponse(WeatherActivity.this,response);
					Log.d("执行到此：", "88888888");
					// 通过runOnUiThread()方法回到主线程处理逻辑
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							showWeather();
						}
						
					});
				} 
			}

			@Override
			public void onError(Exception e) {
				// 通过runOnUiThread()方法回到主线程处理逻辑
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						publishText.setText("同步失败。。。");
					}
					
				});
				
			}
			
		});
	}

	/**
	 * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上。
	 * */
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.INVISIBLE);
		cityNameText.setVisibility(View.INVISIBLE);
		Log.d("执行到此：", "15151515155");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
