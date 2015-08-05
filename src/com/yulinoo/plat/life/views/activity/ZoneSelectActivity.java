package com.yulinoo.plat.life.views.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.LocationSource;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.CityInfo;
import com.yulinoo.plat.life.bean.DistrictInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.AreaLaxReq;
import com.yulinoo.plat.life.net.reqbean.AreainfoReq;
import com.yulinoo.plat.life.net.reqbean.CityInfoReq;
import com.yulinoo.plat.life.net.reqbean.DistrictReq;
import com.yulinoo.plat.life.net.resbean.AreaInfoResponse;
import com.yulinoo.plat.life.net.resbean.CityInfoResponse;
import com.yulinoo.plat.life.net.resbean.DistrictInfoResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.BackWidget.OnBackBtnClickListener;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.life.views.adapter.ZoneSelectAreaAdapter;
import com.yulinoo.plat.life.views.adapter.ZoneSelectAreaAdapter.OnAreaInfoConcernedListener;
import com.yulinoo.plat.life.views.adapter.ZoneSelectAreaAdapter.OnAreaInfoSelectedListener;
import com.yulinoo.plat.life.views.adapter.ZoneSelectCityAdapter;
import com.yulinoo.plat.life.views.adapter.ZoneSelectCityAdapter.OnCitySelectedListener;
import com.yulinoo.plat.life.views.adapter.ZoneSelectDistrictAdapter;
import com.yulinoo.plat.life.views.adapter.ZoneSelectDistrictAdapter.OnDistrictSelectedListener;
import com.yulinoo.plat.melife.R;

public class ZoneSelectActivity extends BaseActivity implements
OnClickListener, IXListViewListener, OnCitySelectedListener,
OnDistrictSelectedListener, OnAreaInfoConcernedListener,
OnAreaInfoSelectedListener, AMapLocationListener {
	// 已成功选择小区
	public static final int AREA_SELECTED = 101;
	// 是否是小区开店
	public static final String isOpenShop = "isopenshop";

	// private MyListView myListView;
	// private MeNormalListView mListView;
	// private SwipeRefreshLayout mSwipeLayout;
	private XListView mListView;

	private ZoneSelectCityAdapter zoneSelectCityAdapter;// 城市选择器
	private ZoneSelectDistrictAdapter zoneSelectDistinctAdapter;// 区县选择器
	private ZoneSelectAreaAdapter zoneSelectAreaAdapter;// 小区选择器

	// 0表示点击城市 1表示点击区县2表示选中了小区
	private static final int FOCUS_ON_CITY = 0;
	private static final int FOCUS_ON_DISTRICT = 1;
	private static final int FOCUS_ON_AREA = 2;
	private int mflag = FOCUS_ON_CITY;// 默认关注点在城市选择上
	private CityInfo mSelectedCity;// 所选择的城市
	private DistrictInfo mSelectedDistrict;// 所选择的区县
	private AreaInfo mSelectedArea;// 所选择的小区
	private int pageNo = 0;

	private View click_city;
	private View click_district;
	private View click_area;

	private TextView mCity;
	private TextView mDistinct;
	private TextView mArea;

	private boolean openShop;// 是否小区开店

	private boolean isEnd = false;
	private boolean isLocate = false;// 是否是定位了的

	// 定位
	private LocationManagerProxy locationManagerProxy = null;
	private AMapLocation aMapLocation;// 用于判断定位超时

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.zone_select);
		openShop = getIntent().getBooleanExtra(isOpenShop, false);
	}

	@SuppressLint({ "InlinedApi", "ResourceAsColor" })
	@Override
	protected void initComponent() {
		zoneSelectCityAdapter = new ZoneSelectCityAdapter(this, this);
		zoneSelectDistinctAdapter = new ZoneSelectDistrictAdapter(this, this);
		zoneSelectAreaAdapter = new ZoneSelectAreaAdapter(this, openShop);
		if (openShop) {// 是小区开店的功能
			zoneSelectAreaAdapter.setOnAreaInfoSelectedListener(this);
		} else {// 是关联小区的功能
			zoneSelectAreaAdapter.setOnAreaInfoConcernedListener(this);
		}

		mCity = (TextView) findViewById(R.id.city);
		mDistinct = (TextView) findViewById(R.id.distinct);
		mArea = (TextView) findViewById(R.id.area);

		click_city = findViewById(R.id.click_city);
		click_city.setOnClickListener(this);
		click_district = findViewById(R.id.click_district);
		click_district.setOnClickListener(this);
		click_area = findViewById(R.id.click_area);
		click_area.setOnClickListener(this);

		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		initZm();
		// 开始定位
		if (!openShop) {//是小区开店的时候不执行
			startLocation();
		}
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView ,TextView rightText, TextView title, View titleLayout) {
		rightImg.setVisibility(View.GONE);
		rightText.setVisibility(View.GONE);
		// rightText.setText(R.string.suggestion_areainfo);
		// rightText.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if(mSelectedCity==null)
		// {
		// showToast("请选择小区所在城市,然后再"+getString(R.string.suggestion_areainfo)+",谢谢");
		// return;
		// }
		// mContext.startActivity(new Intent(mContext, SuggestionActivity.class)
		// .putExtra(Constant.ActivityExtra.SUGGESTION_TYPE,
		// Constant.SuggestionType.LOSE_AREA)
		// .putExtra(Constant.ActivityExtra.SUGGESTION_TYPE,
		// Constant.SuggestionType.RECOMMEND_MERCHANT)
		// );
		// }
		// });
		title.setText("请选择您的小区");
		back_btn.setBackBtnClickListener(new OnBackBtnClickListener() {
			@Override
			public void onBackBtnClick() {
				finish();
			}
		});

	}

	// 加载城市列表
	private void loadCity() {
		mSelectedCity = null;
		mSelectedDistrict = null;
		mSelectedArea = null;

		mCity.setText(getString(R.string.select_city));
		mDistinct.setText(getString(R.string.select_district));
		mArea.setText(getString(R.string.select_areainfo));
		mflag = FOCUS_ON_CITY;

		// myListView.initFreshing();
		// mSwipeLayout.setRefreshing(true);
		RequestBean<CityInfoResponse> requestBean = new RequestBean<CityInfoResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		String pyval = pyszm.getText().toString();
		if (!pyval.startsWith(getString(R.string.pyszm))) {// 说明有输入的拼音
			CityInfoReq cityReq = new CityInfoReq();
			cityReq.setPinyin(pyval);
			requestBean.setRequestBody(cityReq);
		}
		requestBean.setResponseBody(CityInfoResponse.class);
		requestBean.setURL(Constant.Requrl.getCityList());
		requestServer(requestBean, new UICallback<CityInfoResponse>() {
			@Override
			public void onSuccess(CityInfoResponse respose) {
				synchronized (zoneSelectCityAdapter) {
					try {
						onLoad();
						click_city
						.setBackgroundResource(R.drawable.zone_select_border_focus);
						click_district
						.setBackgroundResource(R.drawable.zone_select_border_unfocus);
						click_area
						.setBackgroundResource(R.drawable.zone_select_border_unfocus);
						mListView.setAdapter(zoneSelectCityAdapter);
						if (respose.getList() != null) {
							zoneSelectCityAdapter.clear();
							zoneSelectCityAdapter.load(respose.getList());
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onError(String message) {
				// mSwipeLayout.setRefreshing(false);
				onLoad();
				showToast(message);
			}

			@Override
			public void onOffline(String message) {
				// mSwipeLayout.setRefreshing(false);
				onLoad();
				showToast(message);
			}
		});

	}

	// 加载区县
	private void loadDistrict() {
		mSelectedDistrict = null;
		mSelectedArea = null;
		if (mSelectedCity == null) {
			showToast("请选择" + getString(R.string.select_city));
			onLoad();
			return;
		}
		mDistinct.setText(getString(R.string.select_district));
		mArea.setText(getString(R.string.select_areainfo));

		// mSwipeLayout.setRefreshing(true);
		// mListView.autoRefresh();
		DistrictReq distinctReq = new DistrictReq();
		distinctReq.setCitySid(mSelectedCity.getSid());
		String pyval = pyszm.getText().toString();
		if (!pyval.startsWith(getString(R.string.pyszm))) {// 说明有输入的拼音
			distinctReq.setPinyin(pyval);
		}
		RequestBean<DistrictInfoResponse> requestBean = new RequestBean<DistrictInfoResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(distinctReq);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(DistrictInfoResponse.class);
		requestBean.setURL(Constant.Requrl.getDistrictList());
		requestServer(requestBean, new UICallback<DistrictInfoResponse>() {

			@Override
			public void onSuccess(DistrictInfoResponse respose) {
				synchronized (zoneSelectDistinctAdapter) {
					try {
						// mSwipeLayout.setRefreshing(false);
						onLoad();
						click_district
						.setBackgroundResource(R.drawable.zone_select_border_focus);
						click_area
						.setBackgroundResource(R.drawable.zone_select_border_unfocus);
						click_city
						.setBackgroundResource(R.drawable.zone_select_border_unfocus);
						mListView.setAdapter(zoneSelectDistinctAdapter);
						if (respose.getList() != null) {
							zoneSelectDistinctAdapter.clear();
							zoneSelectDistinctAdapter.load(respose.getList());
						}
					} catch (Exception e) {
					}

				}

			}

			@Override
			public void onError(String message) {
				// mSwipeLayout.setRefreshing(false);
				onLoad();
				showToast(message);
			}

			@Override
			public void onOffline(String message) {
				// mSwipeLayout.setRefreshing(false);
				onLoad();
				showToast(message);
			}
		});
	}

	// 加载小区
	private void loadArea(final boolean isUp) {
		if (mSelectedDistrict == null) {
			showToast("请" + getString(R.string.select_district));
			return;
		}
		mSelectedArea = null;
		mArea.setText(getString(R.string.select_areainfo));
		if (isUp) {
			pageNo = 0;
			isEnd = false;
		} else {
			if (isEnd) {
				// mSwipeLayout.setLoading(false);
				onLoad();
				return;
			}
			pageNo++;
		}

		AreainfoReq areainfoReq = new AreainfoReq();
		areainfoReq.setAlongCitySid(mSelectedCity.getSid());
		areainfoReq.setAlongDistrictSid(mSelectedDistrict.getSid());
		areainfoReq.setPageNo(pageNo);
		String pyval = pyszm.getText().toString();
		if (!pyval.startsWith(getString(R.string.pyszm))) {// 说明有输入的拼音
			areainfoReq.setPinyin(pyval);
		}
		RequestBean<AreaInfoResponse> requestBean = new RequestBean<AreaInfoResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(areainfoReq);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(AreaInfoResponse.class);
		requestBean.setURL(Constant.Requrl.getAreaInfoList());
		requestServer(requestBean, new UICallback<AreaInfoResponse>() {
			@Override
			public void onSuccess(AreaInfoResponse respose) {
				// if(isUp)
				// {
				// mSwipeLayout.setRefreshing(false);
				// }else
				// {
				// mSwipeLayout.setLoading(false);
				// }
				onLoad();
				synchronized (zoneSelectAreaAdapter) {
					if (respose.isSuccess()) {
						try {
							click_area
							.setBackgroundResource(R.drawable.zone_select_border_focus);
							click_district
							.setBackgroundResource(R.drawable.zone_select_border_unfocus);
							click_city
							.setBackgroundResource(R.drawable.zone_select_border_unfocus);
							if (pageNo == 0) {
								mListView.setAdapter(zoneSelectAreaAdapter);
								zoneSelectAreaAdapter.clear();
							}
							if (respose.getList() != null) {
								if (respose.getList().size() == 0) {
									isEnd = true;
								}
								zoneSelectAreaAdapter.load(respose.getList());
							}
						} catch (Exception e) {
						}

					} else {
						showToast(respose.getMsg());
					}
				}

			}

			@Override
			public void onError(String message) {
				isEnd = true;
				// if(isUp)
				// {
				// mSwipeLayout.setRefreshing(false);
				// }else
				// {
				// mSwipeLayout.setLoading(false);
				// }
				onLoad();
				showToast(message);
			}

			@Override
			public void onOffline(String message) {
				isEnd = true;
				// if(isUp)
				// {
				// mSwipeLayout.setRefreshing(false);
				// }else
				// {
				// mSwipeLayout.setLoading(false);
				// }
				onLoad();
				showToast(message);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.click_city: {
			pyszm.setText(getString(R.string.pyszm));
			mflag = FOCUS_ON_CITY;
			isLocate=false;
			mListView.autoRefresh();
			// loadCity();
			break;
		}

		case R.id.click_district: {
			pyszm.setText(getString(R.string.pyszm));
			// loadDistrict();
			mflag = FOCUS_ON_DISTRICT;
			isLocate=false;
			mListView.autoRefresh();
			break;
		}

		case R.id.click_area:
			pageNo = 0;
			pyszm.setText(getString(R.string.pyszm));
			mflag = FOCUS_ON_AREA;
			isLocate=false;
			mListView.autoRefresh();
			// loadArea(true);
			break;
		}
	}

	private void loadListData() {
		if (mflag == FOCUS_ON_CITY) {
			loadCity();
		} else if (mflag == FOCUS_ON_DISTRICT) {
			loadDistrict();
		} else {
			pageNo = 0;
			loadArea(true);
		}
	}

	private FrameLayout del_zm;
	private TextView pyszm;

	private void initZm() {
		pyszm = (TextView) findViewById(R.id.pyszm);
		del_zm = (FrameLayout) findViewById(R.id.del_zm);
		del_zm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String pzv = pyszm.getText().toString();
				if (!pzv.startsWith(getString(R.string.pyszm))) {// 说明之前有输入字母
					if (pzv.length() == 1) {// 说明只有一个字母
						pyszm.setText(getString(R.string.pyszm));
					} else {// 有多个字母，则去掉最后一个字母
						pyszm.setText(pzv.subSequence(0, pzv.length() - 1));
					}
					loadListData();
				}
			}
		});

		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				String pzv = pyszm.getText().toString();
				if (pzv.startsWith(getString(R.string.pyszm))) {
					pyszm.setText("");
				} else {
					if (pzv.length() > 10) {
						showToast("拼音字母太长");
						return;
					}
				}
				pyszm.setText(pyszm.getText().toString()
						+ ((TextView) v).getText().toString());
				loadListData();
			}
		};

		TextView zma = (TextView) findViewById(R.id.zma);
		zma.setOnClickListener(clickListener);
		TextView zmb = (TextView) findViewById(R.id.zmb);
		zmb.setOnClickListener(clickListener);
		TextView zmc = (TextView) findViewById(R.id.zmc);
		zmc.setOnClickListener(clickListener);
		TextView zmd = (TextView) findViewById(R.id.zmd);
		zmd.setOnClickListener(clickListener);
		TextView zme = (TextView) findViewById(R.id.zme);
		zme.setOnClickListener(clickListener);
		TextView zmf = (TextView) findViewById(R.id.zmf);
		zmf.setOnClickListener(clickListener);
		TextView zmg = (TextView) findViewById(R.id.zmg);
		zmg.setOnClickListener(clickListener);
		TextView zmh = (TextView) findViewById(R.id.zmh);
		zmh.setOnClickListener(clickListener);
		TextView zmi = (TextView) findViewById(R.id.zmi);
		zmi.setOnClickListener(clickListener);
		TextView zmj = (TextView) findViewById(R.id.zmj);
		zmj.setOnClickListener(clickListener);
		TextView zmk = (TextView) findViewById(R.id.zmk);
		zmk.setOnClickListener(clickListener);
		TextView zml = (TextView) findViewById(R.id.zml);
		zml.setOnClickListener(clickListener);
		TextView zmm = (TextView) findViewById(R.id.zmm);
		zmm.setOnClickListener(clickListener);
		TextView zmn = (TextView) findViewById(R.id.zmn);
		zmn.setOnClickListener(clickListener);
		TextView zmo = (TextView) findViewById(R.id.zmo);
		zmo.setOnClickListener(clickListener);
		TextView zmp = (TextView) findViewById(R.id.zmp);
		zmp.setOnClickListener(clickListener);
		TextView zmq = (TextView) findViewById(R.id.zmq);
		zmq.setOnClickListener(clickListener);
		TextView zmr = (TextView) findViewById(R.id.zmr);
		zmr.setOnClickListener(clickListener);
		TextView zms = (TextView) findViewById(R.id.zms);
		zms.setOnClickListener(clickListener);
		TextView zmt = (TextView) findViewById(R.id.zmt);
		zmt.setOnClickListener(clickListener);
		TextView zmu = (TextView) findViewById(R.id.zmu);
		zmu.setOnClickListener(clickListener);
		TextView zmv = (TextView) findViewById(R.id.zmv);
		zmv.setOnClickListener(clickListener);
		TextView zmw = (TextView) findViewById(R.id.zmw);
		zmw.setOnClickListener(clickListener);
		TextView zmx = (TextView) findViewById(R.id.zmx);
		zmx.setOnClickListener(clickListener);
		TextView zmy = (TextView) findViewById(R.id.zmy);
		zmy.setOnClickListener(clickListener);
		TextView zmz = (TextView) findViewById(R.id.zmz);
		zmz.setOnClickListener(clickListener);
	}

	@Override
	public void onAreaInfoSelected(AreaInfo areaInfo) {
		Intent intent = new Intent();
		intent.putExtra("AreaInfo", areaInfo);
		intent.putExtra("mSelectCity", mSelectedCity);
		intent.putExtra("mDistrictInfo", mSelectedDistrict);
		setResult(AREA_SELECTED, intent);
		finish();
	}

	@Override
	public void onAreaInfoConcerned(AreaInfo areaInfo, boolean isConcerned) {
		if (isConcerned) {// 关注成功
			// Intent intent = new Intent();
			// intent.setAction(Constant.BroadType.AREA_CHANGED);
			// mContext.sendBroadcast(intent);
			MeLifeMainActivity.instance().setNeedRefreshIndex(true);
			finish();
		}
	}

	@Override
	public void onDistrictSelected(DistrictInfo district) {
		mSelectedDistrict = district;
		mDistinct.setText(district.getDistrictName());
		pageNo = 0;
		pyszm.setText(getString(R.string.pyszm));
		// loadArea(true);
		mflag = FOCUS_ON_AREA;
		mListView.autoRefresh();
	}

	private Handler handler = new Handler();

	@Override
	public void onCitySelected(CityInfo city) {
		mSelectedCity = city;
		mCity.setText(mSelectedCity.getCityName());
		pyszm.setText(getString(R.string.pyszm));
		// loadDistrict();
		mflag = FOCUS_ON_DISTRICT;
		handler.post(new Runnable() {

			@Override
			public void run() {
				mListView.autoRefresh();
			}
		});

	}

	@Override
	public void onRefresh() {
		if (isLocate) {
			onLoad();
			return;
		}
		loadListData();
	}

	@Override
	public void onLoadMore() {
		if (mflag == FOCUS_ON_AREA) {
			loadArea(false);
		} else {
			onLoad();
		}
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getTime());
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
		.format(new Date());
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			mListView.autoRefresh();
		}
	}

	@Override
	public void onLocationChanged(Location location) {

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

	private Double geoLat;
	private Double geoLng;

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if (aMapLocation != null) {
			this.aMapLocation = aMapLocation;// 判断超时机制
			geoLat = aMapLocation.getLatitude();
			geoLng = aMapLocation.getLongitude();
			// 获取小区信息
			getAreaInfoLax();
		}
	}

	/**
	 * 如果开启了网络,先提示正在获取临近小区
	 */
	private void getAreaInfoLax() {
		if (MeMessageService.instance().hasInternet(this) == true) {
			System.out.println("有网!!!开始获取小区信息!!!");
			AreaLaxReq areaLaxReq = new AreaLaxReq();
			areaLaxReq.setLongItude(geoLng);
			areaLaxReq.setLatItude(geoLat);
			RequestBean<AreaInfoResponse> requestBean = new RequestBean<AreaInfoResponse>();
			requestBean.setHttpMethod(HTTP_METHOD.POST);
			requestBean.setRequestBody(areaLaxReq);
			requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
			requestBean.setResponseBody(AreaInfoResponse.class);
			requestBean.setURL(Constant.Requrl.getAreaLax());
			//requestBean.setURL("http://admin.yulinoo.com/app/v1/areainfo/getAreaLax.do");
			requestServer(requestBean, new UICallback<AreaInfoResponse>() {
				@Override
				public void onSuccess(AreaInfoResponse respose) {
					ProgressUtil.dissmissProgress();
					onLoad();
					synchronized (zoneSelectAreaAdapter) {
						try {
							click_area.setBackgroundResource(R.drawable.zone_select_border_focus);
							click_district.setBackgroundResource(R.drawable.zone_select_border_unfocus);
							click_city.setBackgroundResource(R.drawable.zone_select_border_unfocus);
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
					ProgressUtil.dissmissProgress();
					isLocate = false;
					isEnd = true;
					onLoad();
					showToast(message);
				}

				@Override
				public void onOffline(String message) {
					ProgressUtil.dissmissProgress();
					isLocate = false;
					isEnd = true;
					onLoad();
					showToast(message);
				}
			});
		} else {// 如果没打开网络,跳出方法
			isLocate = false;
			return;
		}

	}

	/**
	 * 初始化定位
	 */
	private void startLocation() {
		if (MeMessageService.instance().hasInternet(this)) {
			ProgressUtil.showProgress(this, "正在获取小区列表");
			isLocate = true;
			locationManagerProxy = LocationManagerProxy.getInstance(this);
			locationManagerProxy.requestLocationData(
					LocationProviderProxy.AMapNetwork, 60 * 10000, 10, this);
		} else {// 如果没网,那么就停止定位
			stopLocation();
			isLocate = false;
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
	protected void onPause() {
		super.onPause();
		stopLocation();
	}

}
