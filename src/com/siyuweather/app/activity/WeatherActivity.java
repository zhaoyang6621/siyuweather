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
	 * ������ʾ������
	 * */
	private TextView cityNameText;
	
	/**
	 * ������ʾ����ʱ��
	 * */
	private TextView publishText;
	
	/**
	 * ������ʾ����������Ϣ
	 * */
	private TextView weatherDespText;
	/**
	 * ������ʾ����1
	 * */
	private TextView temp1Text;
	
	/**
	 * ������ʾ����2
	 * */
	private TextView temp2Text;
	
	/**
	 * ������ʾ��ǰ����
	 * */
	private TextView currentDateText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		//��ʼ�����ؼ�
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		//��Intent��ȡ���ؼ�����
		String countyCode = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			//���ؼ�����ʱ��ȥ��ѯ����
			publishText.setText("ͬ���С�����");
			weatherInfoLayout.setVisibility(View.INVISIBLE);//���ò���ʾ������Ȼռ�ݿɼ�ʱ�Ĵ�С��λ�á�
			cityNameText.setVisibility(View.INVISIBLE);
			Log.d("ִ�е��ˣ�", "444444444444");
			queryWeatherCode(countyCode);
		}else{
			//û���ؼ�����ʱ��ֱ����ʾ���ش洢������
			showWeather();
			Log.d("ִ�е��ˣ�", "13131323134");
		}
	}
	
	/**
	 * ��ѯ�ؼ���������Ӧ����������
	 * */
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
		//Log.d("��Ҫ��ѯ��������Ӧ��addressΪ��", address);
		Log.d("ִ�е��ˣ�", "55555555555");
		queryFromServer(address,"countyCode");
	}

	/**
	 * ��ѯ������������Ӧ������
	 * */
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		queryFromServer(address,"weatherCode");
	}
	
	/**
	 * ���ݴ���ĵ�ַ������ȥ���������ѯ�������Ż���������Ϣ
	 * */
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(final String response) {
				if ("countyCode".equals(type)) {
					if(!TextUtils.isEmpty(response)){
						//�ӷ��������ص������н�������������
						String[] array = response.split("\\|");
						if(array != null && array.length == 2){
							String weatherCode = array[1];
							Log.d("ִ�е��ˣ�", "6666666666666");
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)) {
					Log.d("ִ�е��ˣ�", "7777777");
					//������������ص�������Ϣ
					Utility.handleWeatherResponse(WeatherActivity.this,response);
					Log.d("ִ�е��ˣ�", "88888888");
					// ͨ��runOnUiThread()�����ص����̴߳����߼�
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
				// ͨ��runOnUiThread()�����ص����̴߳����߼�
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						publishText.setText("ͬ��ʧ�ܡ�����");
					}
					
				});
				
			}
			
		});
	}

	/**
	 * ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ�������ϡ�
	 * */
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("����" + prefs.getString("publish_time", "") + "����");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.INVISIBLE);
		cityNameText.setVisibility(View.INVISIBLE);
		Log.d("ִ�е��ˣ�", "15151515155");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
