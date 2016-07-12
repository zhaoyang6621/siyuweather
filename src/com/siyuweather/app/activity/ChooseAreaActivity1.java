package com.siyuweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.siyuweather.app.R;
import com.siyuweather.app.db.SiyuWeatherDB;
import com.siyuweather.app.model.City;
import com.siyuweather.app.model.County;
import com.siyuweather.app.model.Province;
import com.siyuweather.app.util.HttpCallbackListener;
import com.siyuweather.app.util.HttpUtil;
import com.siyuweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity1 extends Activity {

	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private SiyuWeatherDB siyuWeatherDB;
	private List<String> dataList = new ArrayList<String>();

	/**
	 * 省列表
	 * */
	private List<Province> provinceList;

	/**
	 * 市列表
	 * */
	private List<City> cityList;

	/**
	 * 县列表
	 * */
	private List<County> countyList;

	/**
	 * 选中的省份
	 * */
	private Province selectedProvince;

	/**
	 * 选中的城市
	 * */
	private City selectedCity;

	/**
	 * 当前选中的级别
	 * */
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//boolean city_selected = true;
		//先从SharedPreferences 中读取city_selected标志位
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.getBoolean("city_selected", false)){
			//System.out.print("布尔输出真假值为"+ prefs.getBoolean("city_selected", false));
			Log.d("执行到此：", "000000000");
			Intent intent = new Intent(this,WeatherActivity.class);
			startActivity(intent);
			Log.d("执行到此：", "1111111111");
			finish();
			return;
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area1);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		siyuWeatherDB = SiyuWeatherDB.getInstance(this);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(index);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(index);
					queryCounties();
				} else if(currentLevel == LEVEL_COUNTY){
					Log.d("执行到此：", "22222222222");
					String countyCode = countyList.get(index).getCountyCode();
					Intent intent = new Intent(ChooseAreaActivity1.this,WeatherActivity.class);
					intent.putExtra("county_code", countyCode);
					startActivity(intent);
					Log.d("执行到此：", "3333333333333");
					finish();
				}
			}
		});
		queryProvinces(); // 加载省级数据
	}

	/**
	 * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
	 * */
	private void queryProvinces() {
		provinceList = siyuWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged(); // 当我们需要ListView进行刷新的时候，我们需要调用Adapter.notifyDataSetChanged()来让界面刷新。notifyDataSetChanged()动态更新ListView,它可以在修改适配器绑定的数组后，不用重新刷新Activity，通知Activity更新ListView。
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}

	/**
	 * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
	 * */
	private void queryCities() {
		cityList = siyuWeatherDB.loadCities(selectedProvince.getId());
		//Log.d("执9", "9999999999999");
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
				//Log.d("执1111", "1212121212");
			}
			adapter.notifyDataSetChanged(); // 当我们需要ListView进行刷新的时候，我们需要调用Adapter.notifyDataSetChanged()来让界面刷新。notifyDataSetChanged()动态更新ListView,它可以在修改适配器绑定的数组后，不用重新刷新Activity，通知Activity更新ListView。
			listView.setSelection(0);//表示将列表移动到指定的Position处。
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
			//Log.d("执10", "1010101010");
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}

	/**
	 * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
	 * */
	private void queryCounties() {
		countyList = siyuWeatherDB.loadCounties(selectedCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged(); // 当我们需要ListView进行刷新的时候，我们需要调用Adapter.notifyDataSetChanged()来让界面刷新。notifyDataSetChanged()动态更新ListView,它可以在修改适配器绑定的数组后，不用重新刷新Activity，通知Activity更新ListView。
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}

	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据。
	 * */
	private void queryFromServer(final String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
			Log.d("执行到此的address为：", address);
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvinceResponse(siyuWeatherDB,
							response);
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(siyuWeatherDB,
							response, selectedProvince.getId());
					//Log.d("执行到此：", "22222222222");
				} else if ("county".equals(type)) {
					result = Utility.handleCountiesResponse(siyuWeatherDB,
							response, selectedCity.getId());
				}
				if (result) {
					// 通过runOnUiThread()方法回到主线程处理逻辑
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								//Log.d("执行到此：", "5555555555");
								queryCities();
								//Log.d("执行到此：", "66666666666");
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}

					});
				}
			}

			@Override
			public void onError(Exception e) {
				// 通过runOnUiThread()方法回到主线程处理逻辑
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity1.this, "加载失败",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	/**
	 * 显示进度对话框。
	 * */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			//Log.d("执行到此", "11111");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭进度对话框。
	 * */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

	}

	/**
	 * 捕获back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出。
	 * */
	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
			queryCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			finish();
		}
	}

}
