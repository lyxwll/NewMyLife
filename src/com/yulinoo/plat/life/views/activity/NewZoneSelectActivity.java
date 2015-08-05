package com.yulinoo.plat.life.views.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.CityInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.AreaLaxReq;
import com.yulinoo.plat.life.net.reqbean.AreainfoReq;
import com.yulinoo.plat.life.net.reqbean.CityInfroReqByLL;
import com.yulinoo.plat.life.net.resbean.AreaInfoResponse;
import com.yulinoo.plat.life.net.resbean.CityInfoResponse;
import com.yulinoo.plat.life.net.resbean.CityInfroByLLResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.BackWidget.OnBackBtnClickListener;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.ui.widget.sidelist.Content;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.Constant.URL;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.views.adapter.ZoneSelectAreaAdapter;
import com.yulinoo.plat.life.views.adapter.ZoneSelectAreaAdapter.OnAreaInfoConcernedListener;
import com.yulinoo.plat.life.views.adapter.ZoneSelectAreaAdapter.OnAreaInfoSelectedListener;
import com.yulinoo.plat.melife.R;

import config.AppContext;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class NewZoneSelectActivity extends FragmentActivity implements OnClickListener,IXListViewListener,OnAreaInfoConcernedListener,OnAreaInfoSelectedListener,AMapLocationListener{

	public static final int SELECT_CITY=1110;
	// 已成功选择小区
	public static final int AREA_SELECTED = 101;
	public static final String SUUFIX="/app/v2/areainfo/searchArea.do";
	// 是否是小区开店
	public static final String isOpenShop = "isopenshop";

	private boolean isEnd = false;
	private int pageNo = 0;

	private BackWidget backWidget;
	private TextView title;
	private XListView mListView;
	private EditText searchEditText;
	private String areaName;
	private View searchView;

	private String cityDomin;
	private String firstpinyin;
	private String request_url;
	private String cityName;
	public static final String DOT=".";

	private ZoneSelectAreaAdapter zoneSelectAreaAdapter;
	private boolean openShop;// 是否小区开店

	// 定位
	private LocationManagerProxy locationManagerProxy = null;
	private AMapLocation aMapLocation;// 用于判断定位超时
	private Double geoLat;
	private Double geoLng;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.new_zone_select_layout);
		openShop = getIntent().getBooleanExtra(isOpenShop, false);
		title=(TextView) findViewById(R.id.nzs_title);
		title.setOnClickListener(this);

		mListView=(XListView) findViewById(R.id.nzs_list_view);
		//mListView.setPullLoadEnable(false);
		//mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);

		searchEditText=(EditText) findViewById(R.id.edittext);//输入要搜索小区的edittext
		searchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				areaName=searchEditText.getText().toString();
				//loadArea(true);
				mListView.autoRefresh();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		searchView=findViewById(R.id.search_rl);//搜索的view
		searchView.setOnClickListener(this);

		backWidget=(BackWidget) findViewById(R.id.nzs_back_btn);
		backWidget.setBackBtnClickListener(new OnBackBtnClickListener() {

			@Override
			public void onBackBtnClick() {
				finish();
			}
		});
		startLocation();
		zoneSelectAreaAdapter=new ZoneSelectAreaAdapter(this, openShop);
		zoneSelectAreaAdapter.setOnAreaInfoConcernedListener(this);
		zoneSelectAreaAdapter.setOnAreaInfoSelectedListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.nzs_title:
			startActivityForResult(new Intent(NewZoneSelectActivity.this, SelectCityActivity.class), SELECT_CITY);
			break;
		case R.id.search_rl:
			if (cityName==null) {
				MeUtil.showToast(NewZoneSelectActivity.this, "请先选择城市");
				return;
			}else {
				//根据用户输入查询小区
				areaName=searchEditText.getText().toString();
				//loadArea(true);
				mListView.autoRefresh();
			}
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==NewZoneSelectActivity.SELECT_CITY) {//选择了城市
			Content content=(Content) data.getSerializableExtra("select_city");
			cityName=content.getName();
			title.setText(cityName);
			cityDomin=content.getCityDomain();
			firstpinyin=content.getFirstpinyin();
			areaName=searchEditText.getText().toString();
			request_url=URL.HTTP+cityDomin+DOT+ URL.BASE_DOMAIN+SUUFIX;
			//请求网络
			//loadArea(true);
			mListView.autoRefresh();
		}
	}

	/**
	 * 根据经纬度获取临近小区
	 * @param isUp
	 * @param geoLat
	 * @param geoLng
	 */
	private void loadAreaByLL(final boolean isUp,Double geoLat,Double geoLng){
		if (isUp) {
			pageNo = 0;
			isEnd = false;
		} else {
			if (isEnd) {
				onLoad();
				return;
			}
			pageNo++;
		}
		AreaLaxReq areaLaxReq = new AreaLaxReq();
		areaLaxReq.setLongItude(geoLng);
		areaLaxReq.setLatItude(geoLat);
		RequestBean<AreaInfoResponse> requestBean = new RequestBean<AreaInfoResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(areaLaxReq);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(AreaInfoResponse.class);
		requestBean.setURL(Constant.Requrl.getAreaLax());
		MeMessageService.instance().requestServer(requestBean, new UICallback<AreaInfoResponse>() {

			@Override
			public void onSuccess(AreaInfoResponse respose) {
				ProgressUtil.dissmissProgress();
				onLoad();
				synchronized (zoneSelectAreaAdapter) {
					try {
						mListView.setAdapter(zoneSelectAreaAdapter);
						zoneSelectAreaAdapter.clear();
						if (respose.getList() != null) {
							if (respose.getList().size() == 0) {
								isEnd = true;
							}
							zoneSelectAreaAdapter.load(respose.getList());
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onError(String message) {
				isEnd = true;
				onLoad();
				MeUtil.showToast(NewZoneSelectActivity.this,message);
				ProgressUtil.dissmissProgress();
			}

			@Override
			public void onOffline(String message) {
				isEnd = true;
				onLoad();
				MeUtil.showToast(NewZoneSelectActivity.this,message);
				ProgressUtil.dissmissProgress();
			}
		});

	}


	/**
	 * 加载小区
	 */
	private void loadArea(final boolean isUp){
		if (isUp) {
			pageNo = 0;
			isEnd = false;
		} else {
			if (isEnd) {
				onLoad();
				return;
			}
			pageNo++;
		}
		AreainfoReq areainfoReq=new AreainfoReq();
		areainfoReq.setWord(areaName);
		//areainfoReq.setPageNo(pageNo);
		RequestBean<AreaInfoResponse> requestBean = new RequestBean<AreaInfoResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(areainfoReq);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(AreaInfoResponse.class);
		requestBean.setURL(request_url);
		MeMessageService.instance().requestServer(requestBean, new UICallback<AreaInfoResponse>() {

			@Override
			public void onSuccess(AreaInfoResponse respose) {
				ProgressUtil.dissmissProgress();
				onLoad();
				synchronized (zoneSelectAreaAdapter) {
					if (respose.isSuccess()) {
						try {
							mListView.setAdapter(zoneSelectAreaAdapter);
							zoneSelectAreaAdapter.clear();
							if (respose.getList() != null) {
								if (respose.getList().size() == 0) {
									isEnd = true;
								}
								zoneSelectAreaAdapter.load(respose.getList());
							}
						} catch (Exception e) {
						}

					} else {
						MeUtil.showToast(NewZoneSelectActivity.this,respose.getMsg());
					}
				}
			}

			@Override
			public void onError(String message) {
				isEnd = true;
				onLoad();
				MeUtil.showToast(NewZoneSelectActivity.this,message);
				ProgressUtil.dissmissProgress();
			}

			@Override
			public void onOffline(String message) {
				isEnd = true;
				onLoad();
				MeUtil.showToast(NewZoneSelectActivity.this,message);
				ProgressUtil.dissmissProgress();
			}
		});
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getTime());
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
	}

	@Override
	public void onRefresh() {
		loadArea(true);
	}

	@Override
	public void onLoadMore() {
		loadArea(false);
	}

	@Override
	public void onAreaInfoConcerned(AreaInfo areaInfo, boolean isConcerned) {
		AppContext.setCurrentAreaInfo(areaInfo);
		Intent intent = new Intent();  
		intent.setAction(Constant.BroadType.AREA_CHANGED);  
		NewZoneSelectActivity.this.sendBroadcast(intent);
		finish();
	}

	@Override
	public void onAreaInfoSelected(AreaInfo areaInfo) {
		Intent intent = new Intent();
		intent.putExtra("AreaInfo", areaInfo);
		//intent.putExtra("mSelectCity", mSelectedCity);
		//intent.putExtra("mDistrictInfo", mSelectedDistrict);
		setResult(AREA_SELECTED, intent);
		finish();
	}

	/**
	 * 初始化定位
	 */
	private void startLocation() {
		if (MeMessageService.instance().hasInternet(this)) {
			ProgressUtil.showProgress(this, "正在定位");
			locationManagerProxy = LocationManagerProxy.getInstance(this);
			locationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 10000, 10, this);
		} else {// 如果没网,那么就停止定位
			stopLocation();
			return;
		}

	}

	/**
	 * 销毁定位
	 */
	private void stopLocation() {
		if (locationManagerProxy != null) {
			locationManagerProxy.removeUpdates(this);
			locationManagerProxy.destory();
		}
		locationManagerProxy = null;
	}



	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if (aMapLocation != null) {
			this.aMapLocation = aMapLocation;// 判断超时机制
			geoLat = aMapLocation.getLatitude();
			geoLng = aMapLocation.getLongitude();
			// 根据经纬度获取城市信息
			getCityInfro(geoLat,geoLng);
		}
	}

	/**
	 * 根据经纬度获取城市信息
	 */
	private void getCityInfro(final Double geoLat,final Double geoLng){
		CityInfroReqByLL reqByLL=new CityInfroReqByLL();
		reqByLL.setLongitude(geoLng);
		reqByLL.setLatitude(geoLat);
		RequestBean<CityInfroByLLResponse> requestBean=new RequestBean<CityInfroByLLResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(reqByLL);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(CityInfroByLLResponse.class);
		requestBean.setURL(Constant.Requrl.getCityBylt());
		MeMessageService.instance().requestServer(requestBean, new UICallback<CityInfroByLLResponse>() {

			@Override
			public void onSuccess(CityInfroByLLResponse respose) {
				onLoad();
				if (respose.isSuccess()) {
					cityName=respose.getCityName();
					cityDomin=respose.getCityDomain();
					title.setText(cityName);
					request_url=URL.HTTP+cityDomin+DOT+ URL.BASE_DOMAIN+SUUFIX;
					loadAreaByLL(true,geoLat,geoLng);
					//loadArea(true);
					//mListView.autoRefresh();
					ProgressUtil.dissmissProgress();
				}else {
					MeUtil.showToast(NewZoneSelectActivity.this, "网络请求出错!");
					ProgressUtil.dissmissProgress();
					return;
				}
			}

			@Override
			public void onError(String message) {
				ProgressUtil.dissmissProgress();
				MeUtil.showToast(NewZoneSelectActivity.this, "网络请求出错!");
			}

			@Override
			public void onOffline(String message) {
				//ProgressUtil.dissmissProgress();
				MeUtil.showToast(NewZoneSelectActivity.this, "网络请求出错!");
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopLocation();
	}



}
