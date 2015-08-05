package com.yulinoo.plat.life.views.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Advertise;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.AdvertiseReq;
import com.yulinoo.plat.life.net.resbean.AdvertiseListResponse;
import com.yulinoo.plat.life.net.resbean.VersionResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.ConfirmCloseDialog;
import com.yulinoo.plat.life.ui.widget.ConfirmCloseDialog.FinishCallback;
import com.yulinoo.plat.life.ui.widget.MainMenuWidget.OnMainMenuSelectedListener;
import com.yulinoo.plat.life.ui.widget.MulinListView;
import com.yulinoo.plat.life.ui.widget.NeighbourCircleWidget;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.utils.DensityUtil;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.life.views.adapter.MainListViewAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;

/**
 * 启动主程序
 * 
 * @author Administrator
 * 
 */
public class MeLifeMainActivity extends Activity implements OnClickListener,
		OnMainMenuSelectedListener/* , OnMessageBoxArrivedListener */{

	private static MeLifeMainActivity instance;

	public static final boolean isInstanciated() {
		return instance != null;
	}

	public static final MeLifeMainActivity instance() {
		if (instance != null)
			return instance;
		throw new RuntimeException("MeLifeMainActivity not instantiated yet");
	}

	LinearLayout mRadioGroup_content;
	private ViewPager mViewPager;
	private TextView mtitle;

	private ConfirmCloseDialog confirmCloseDialog;

	private LayoutInflater inflater = null;

	LinearLayout.LayoutParams lp;

	private MyApplication myapp;
	private NeighbourCircleWidget neighbour_circle;// 近邻

	private MapView mapView;// 高德地图相关
	private MulinListView mListView;
	private MainListViewAdapter adapter;

	private View go_search;
	private View more_function;
	private LinkedArea linkedArea;
	private MoreFunction moreFunction;
	

	// //功能部分
	// private View bottomFunction;
	// private View around;//周边生活
	// private View neibour;//邻里之间
	// private ImageView neibour_rh;//邻里之间的红点
	// private View mConcren;//我的关注
	// private ImageView concern_rh;//我的关注的红点
	// private View mIndex;//咨询动态
	// private View usrZone;//个人中心
	// private ImageView usrZone_rh;//个人中心的红点
	// private View myArea;//我的小区

	// 天气
	private WeatherMoreFunction weatherMoreFunction;

	private View weahter_ll;

	private View undo_mapview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		myapp = (MyApplication) getApplication();
		inflater = LayoutInflater.from(this);

		initBroadCastReceiver();

		if (MeMessageService.isReady()) {
			// MeMessageService.instance().addOnMessageBoxArrivedListener(this);
			MeMessageService.instance().startGetMessage();
		}
		mtitle = (TextView) findViewById(R.id.mktitle);
		mtitle.setOnClickListener(this);

		// 初始化neibourCircleWidget相关内容
		neighbour_circle = (NeighbourCircleWidget) findViewById(R.id.neighbour_circle);
		mapView = neighbour_circle.getMapView();
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		neighbour_circle.initMap();
		// neighbour_circle.showNeiburCircle();//显示近邻

		mListView = (MulinListView) findViewById(R.id.main_list_view);
		adapter = new MainListViewAdapter(MeLifeMainActivity.this, mListView);
		List<Advertise> list = new ArrayList<Advertise>();
		list.add(null);
		adapter.load(list);

		// //初始化底部function相关内容
		// //最外面的layout的view
		// bottomFunction=findViewById(R.id.main_function_ll_1);
		// //周边生活
		// around=findViewById(R.id.around_rl);
		// around.setOnClickListener(this);
		// //邻里之间
		// neibour=findViewById(R.id.neibour_rl);
		// neibour.setOnClickListener(this);
		// neibour_rh=(ImageView) findViewById(R.id.neibour_rh);
		// //我的关注
		// mConcren= findViewById(R.id.concern_rl);
		// mConcren.setOnClickListener(this);
		// concern_rh=(ImageView) findViewById(R.id.concern_rh);
		// //生活百科
		// mIndex=findViewById(R.id.index_rl);
		// mIndex.setOnClickListener(this);
		// //个人中心
		// usrZone=findViewById(R.id.usr_zone_rl);
		// usrZone.setOnClickListener(this);
		// usrZone_rh=(ImageView) findViewById(R.id.usr_zone_rh);
		// //我的小区
		// myArea=findViewById(R.id.my_area_ll);
		// myArea.setOnClickListener(this);

		instance = this;
		if (!MeMessageService.instance().hasInternet(this)) {
			confirmCloseDialog = new ConfirmCloseDialog(this,
					"\n检查到你的手机未开启网络,你是否需要打开?\n", "请打开网络", "我去打开", "不想打开",
					new FinishCallback() {
						@Override
						public void confirmThisOperation() {
							openInternet();
							confirmCloseDialog.dismiss();
							confirmCloseDialog = null;
						}

						@Override
						public void cancle() {
							confirmCloseDialog.dismiss();
							confirmCloseDialog = null;
						}
					});
			confirmCloseDialog.show();
		}

		if (AppContext.currentAreaInfo() == null) {
			MeUtil.showToast(MeLifeMainActivity.this,
					getString(R.string.needconcerarea));
		}

		go_search = findViewById(R.id.go_search_rl);
		go_search.setOnClickListener(this);
		more_function = findViewById(R.id.more_function);
		more_function.setOnClickListener(this);

		// //设置我的关注的红点
		// if (concern_rh!=null) {
		// if (ConcernUtil.isMenuShowRedHot()) {
		// concern_rh.setVisibility(View.VISIBLE);
		// }else {
		// concern_rh.setVisibility(View.GONE);
		// }
		// }

		linkedArea = new LinkedArea(this, inflater, mtitle);
		moreFunction = new MoreFunction(this, inflater, more_function);
		weatherMoreFunction = new WeatherMoreFunction(this, inflater,
				more_function);
		{
			Rect frame = new Rect();
			this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			int statusBarHeight = frame.top;
			mListView.setStatusHeight(statusBarHeight);
			LayoutParams lp = (LayoutParams) mListView.getLayoutParams();
			ktop = AppContext.getScreenHeight(this)
					- (int) getResources().getDimension(R.dimen.header_height)
					- DensityUtil.dip2px(this, 130) - statusBarHeight;
			lp.topMargin = ktop;
			mListView.setLayoutParams(lp);
		}

		weahter_ll = findViewById(R.id.weahter_ll);
		weatherMoreFunction.getWeather(weahter_ll);

		undo_mapview = findViewById(R.id.undo_mapview);
		undo_mapview.setOnClickListener(this);
		// 请求网络,看主页的广告是否有
		getAdvertiseInfro();
	}

	private int ktop = 0;

	private void getAdvertiseInfro() {
		if (MeMessageService.instance().hasInternet(this)) {// 有网的话才请求
			if (AppContext.currentAreaInfo() != null) {// 当前有小区信息
				AreaInfo areaInfo = AppContext.currentAreaInfo();
				AdvertiseReq advertiseReq = new AdvertiseReq();
				advertiseReq.setAreaSid(areaInfo.getSid());
				advertiseReq.setPosition(1);
				RequestBean<AdvertiseListResponse> requestBean = new RequestBean<AdvertiseListResponse>();
				requestBean.setHttpMethod(HTTP_METHOD.POST);
				requestBean.setRequestBody(advertiseReq);
				requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
				requestBean.setResponseBody(AdvertiseListResponse.class);
				requestBean.setURL(Constant.Requrl.getAdvertList());
				MeMessageService.instance().requestServer(requestBean,
						new UICallback<AdvertiseListResponse>() {

							@Override
							public void onSuccess(AdvertiseListResponse respose) {
								if (respose.getList() != null
										&& respose.getList().size() > 0) {
									List<Advertise> advertises = respose
											.getList();
									adapter.load(advertises);
									int height = mListView.getLayoutParams().height;
									int[] sizes = SizeUtil
											.getAdvertiseSizeNoMargin(MeLifeMainActivity.this);
									int mHeight = advertises.size()
											* (sizes[1] + (int) MeLifeMainActivity.this
													.getResources()
													.getDimension(
															R.dimen.main_advertise_padding_top));
									mListView.getLayoutParams().height = height
											+ mHeight
											+ DensityUtil
													.dip2px(getApplicationContext(),
															10);
								}
							}

							@Override
							public void onError(String message) {

							}

							@Override
							public void onOffline(String message) {

							}
						});
			}
		} else {
			return;
		}
	}

	private MyBroadcastReceiver areaChangedBroadcastReceiver;
	private MyBroadcastReceiver newVersionBroadcastReceiver;

	// 初始化广播接收者
	private void initBroadCastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.BroadType.AREA_CHANGED);
		areaChangedBroadcastReceiver = new MyBroadcastReceiver();
		this.registerReceiver(areaChangedBroadcastReceiver, intentFilter);

		IntentFilter newVersionReadedFilter = new IntentFilter();
		newVersionReadedFilter.addAction(Constant.BroadType.NEW_VERSION);
		newVersionBroadcastReceiver = new MyBroadcastReceiver();
		this.registerReceiver(newVersionBroadcastReceiver,
				newVersionReadedFilter);
	}

	// 更新首页的标题
	public void updateAreaTitle() {
		if (AppContext.currentAreaInfo() != null) {
			mtitle.setText(AppContext.currentAreaInfo().getAreaName());
		}
	}

	private boolean isFirstLoad = true;

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();

		if (isFirstLoad) {
			isFirstLoad = false;
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					final LayoutParams lp = (LayoutParams) mListView
							.getLayoutParams();
					for (int kk = 0; kk < 50; kk++) {
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								ktop = ktop - 1;
								lp.topMargin = ktop;
								mListView.setLayoutParams(lp);
							}
						}, (kk + 1) * 20);
					}

					for (int kk = 51; kk < 100; kk++) {
						final int tmp = kk + 1;
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								ktop = ktop + 1;
								lp.topMargin = ktop;
								mListView.setLayoutParams(lp);
							}
						}, tmp * 20);
					}
				}
			}, 500);
		}

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	private void openInternet() {
		Intent intent = null;
		// 判断手机系统的版本 即API大于10 就是3.0或以上版本
		if (android.os.Build.VERSION.SDK_INT > 10) {
			intent = new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		} else {
			intent = new Intent();
			ComponentName component = new ComponentName("com.android.settings",
					"com.android.settings.WirelessSettings");
			intent.setComponent(component);
			intent.setAction("android.intent.action.VIEW");
		}
		this.startActivity(intent);
	}

	// 点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mktitle: {
			linkedArea.linkedAreas();
			break;
		}
		case R.id.more_function: {
			moreFunction.showMoreFunctionPopupWindow();
			break;
		}
		case R.id.go_search_rl: {
			if (AppContext.currentAreaInfo() == null) {
				MeUtil.showToast(MeLifeMainActivity.this,
						getString(R.string.needconcerarea));
				startActivity(new Intent(MeLifeMainActivity.this,
						NewZoneSelectActivity.class));
				return;
			}
			startActivity(new Intent(MeLifeMainActivity.this,
					SearchActivity.class));
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_right_out);
			break;
		}
		case R.id.undo_mapview: {

			neighbour_circle.refreshMap();
			break;
		}
		/*
		 * case R.id.around_rl://周边生活 if (AppContext.currentAreaInfo()==null) {
		 * MeUtil.showToast(MeLifeMainActivity.this,
		 * getString(R.string.needconcerarea)); return; } startActivity(new
		 * Intent(MeLifeMainActivity.this, AroundCategoryActivity.class));
		 * overridePendingTransition
		 * (R.anim.slide_left_in,R.anim.slide_right_out); break; case
		 * R.id.neibour_rl://邻里之间 if (AppContext.currentAreaInfo()==null) {
		 * MeUtil.showToast(MeLifeMainActivity.this,
		 * getString(R.string.needconcerarea)); return; } startActivity(new
		 * Intent(MeLifeMainActivity.this, NeighbourTalkActivity.class)); break;
		 * case R.id.concern_rl:{//我的关注 Account
		 * account=AppContext.currentAccount(); if (account!=null &&
		 * account.getIsUsrLogin()) { startActivity(new
		 * Intent(MeLifeMainActivity.this, ConcernActivity.class));
		 * overridePendingTransition
		 * (R.anim.slide_left_in,R.anim.slide_right_out); }else {
		 * MeUtil.showToast(MeLifeMainActivity.this, "请先登录"); return; } break; }
		 * case R.id.index_rl://生活百科 if (AppContext.currentAreaInfo()!=null)
		 * {//如果有小区信息 startActivity(new Intent(MeLifeMainActivity.this,
		 * IndexActivity.class));
		 * overridePendingTransition(R.anim.slide_left_in,
		 * R.anim.slide_right_out); }else {
		 * MeUtil.showToast(MeLifeMainActivity.this, "您还未关注小区,请先关注小区!"); return;
		 * } break; case R.id.usr_zone_rl:{//个人中心 Account account =
		 * AppContext.currentAccount(); if (account != null &&
		 * account.getIsUsrLogin()) {// 用户已登录 if (AppContext.currentFocusArea()
		 * != null && AppContext.currentFocusArea().size() > 0) {//用户关注了小区
		 * AreaInfo ai = AppContext.currentFocusArea().get(0);
		 * account.setAreaInfo(ai); startActivity(new
		 * Intent(MeLifeMainActivity.this, UsrZoneActivity.class).putExtra(
		 * Constant.ActivityExtra.ACCOUNT, account).putExtra( "rh_isShowing",
		 * rh_isShowing)); } else { startActivity(new Intent(this,
		 * LoginActivity.class)); } } else { startActivity(new Intent(this,
		 * LoginActivity.class)); }
		 * overridePendingTransition(R.anim.slide_left_in
		 * ,R.anim.slide_right_out); break; } case R.id.my_area_ll://我的小区 { if
		 * (AppContext.currentAreaInfo()!=null) { startActivity(new Intent(this,
		 * MyAreaActivity.class)); }else { MeUtil.showToast(this, "请先关注小区"); }
		 * break; }
		 */
		}
	}

	long lastPressTime = 0L;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			long thisTime = System.currentTimeMillis();
			if (lastPressTime != 0L) {
				if (thisTime - lastPressTime < 5000) {
					if (MeMessageService.isReady()) {
						stopService(new Intent(Intent.ACTION_MAIN).setClass(
								this, MeMessageService.class));// 停止服务
					}
					System.exit(0);
				} else {
					MeUtil.showToast(MeLifeMainActivity.this, "再按一次退出与邻生活");
				}
			} else {
				MeUtil.showToast(MeLifeMainActivity.this, "再按一次退出与邻生活");
			}
			lastPressTime = thisTime;
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onMenuSelected(int index) {
		mViewPager.setCurrentItem(index);
	}

	private boolean needRefreshMap = false;

	public void setNeedRefreshIndex(boolean needRefreshIndex) {
		this.needRefreshMap = needRefreshIndex;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			if (AppContext.currentAreaInfo() != null) {
				updateAreaTitle();
				if (needRefreshMap) {
					needRefreshMap = false;
					neighbour_circle.showNeiburCircle();
					weatherMoreFunction.getWeather(weahter_ll);
				}
			}
		}
		// adapter.notifyDataSetChanged();
	}

	// @Override
	// public void onMessageBoxArrived(MessageBox messageBox) {
	// int chatBoxNumber = messageBox.getChatBoxNumber();
	// if (chatBoxNumber > 0) {// 有聊天室的记录
	// neibour_rh.setVisibility(View.VISIBLE);
	// } else {
	// neibour_rh.setVisibility(View.GONE);
	// }
	//
	// int letterNumber = messageBox.getLetterNumber();
	// if (letterNumber > 0) {//关注有记录
	// concern_rh.setVisibility(View.VISIBLE);
	// }
	//
	// int commentNumber = messageBox.getCommentNumber();
	// int praiseNumber = messageBox.getPraiseNumber();
	// if (commentNumber == 0 && praiseNumber == 0) {//个人中心
	// if (usrZone_rh.getVisibility() == View.VISIBLE) {
	// usrZone_rh.setVisibility(View.GONE);
	// rh_isShowing = false;
	// }
	// } else {
	// if (usrZone_rh.getVisibility() == View.GONE) {
	// usrZone_rh.setVisibility(View.VISIBLE);
	// rh_isShowing = true;
	// }
	// }
	//
	// if (messageBox.getAttUpdate() != null) {// 说明有关注的商家/好友有状态变更
	// concern_rh.setVisibility(View.VISIBLE);
	// }
	// }

	@Override
	protected void onDestroy() {
		try {
			this.unregisterReceiver(areaChangedBroadcastReceiver);
			this.unregisterReceiver(newVersionBroadcastReceiver);
		} catch (Exception e) {
		}
		super.onDestroy();
		mapView.onDestroy();
	}

	public ProgressDialog pBar;

	private void showNewVersionUpdate(final VersionResponse version) {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("\n最新版本:" + version.getVerName() + "\n");
		sbuf.append("新版本大小:" + version.getVerSize() + "\n\n");
		sbuf.append("更新内容\n");
		sbuf.append(version.getDesc() + "\n");
		confirmCloseDialog = new ConfirmCloseDialog(this, sbuf.toString(),
				"发现新版本", "现在更新", "暂不更新", new FinishCallback() {
					@Override
					public void confirmThisOperation() {
						Constant.Requrl.getVersion();
						String geturl = "http://www.yulinoo.com/app/android/"
								+ version.getApkname();
						downFile(geturl, version.getApkname());
					}

					@Override
					public void cancle() {
						confirmCloseDialog.dismiss();
						confirmCloseDialog = null;
					}
				});
		confirmCloseDialog.show();
	}

	void downFile(final String url, final String apkname) {
		// pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					final long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								apkname);
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
								final int tmp = count;
								handler.post(new Runnable() {
									@Override
									public void run() {
										confirmCloseDialog.downLoadPlus(tmp,
												length);
									}
								});
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					confirmCloseDialog.dismiss();
					// confirmCloseDialog = null;
					down(apkname);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	protected Handler handler = new Handler();

	private void down(final String apkname) {
		handler.post(new Runnable() {
			public void run() {
				// pBar.cancel();
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), apkname)),
						"application/vnd.android.package-archive");
				startActivity(intent);
			}
		});
	}

	private class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constant.BroadType.AREA_CHANGED)) {// 小区受到改变
				if (neighbour_circle != null) {
					updateAreaTitle();
					weatherMoreFunction.getWeather(weahter_ll);
					neighbour_circle.showNeiburCircle();
				} else if (action.equals(Constant.BroadType.NEW_VERSION)) {// 有新版本
					VersionResponse version = (VersionResponse) intent
							.getSerializableExtra(Constant.ActivityExtra.NEW_VERSION);
					if (version != null) {
						showNewVersionUpdate(version);
					}
				}
			}
		}
	}

}