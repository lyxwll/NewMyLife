package com.yulinoo.plat.life.views.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.Category;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.MyNeighbourReq;
import com.yulinoo.plat.life.net.resbean.NeighbourResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.BackWidget.OnBackBtnClickListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.SharedPreferencesUtil;
import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.views.adapter.ZoneMainFunctionAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class MyNeighbourActivity extends Activity implements OnMarkerClickListener, OnInfoWindowClickListener, 
OnMapLoadedListener, OnClickListener,InfoWindowAdapter {
	public static final String AREAINDO="AREAINDO";
	private AreaInfo areainfo;//中心点小区
	private List<AreaInfo> neighbours;//小区的近邻小区
	private String[] zm=new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private AMap aMap;
	private MapView mapView;
	//private LocationManagerProxy mAMapLocManager = null;
	private Circle circle;
	//private double lat, lon;

	//private List<Marker> markers = new ArrayList<Marker>();
	//private List<LatLng> latLngs = null;
	//private LatLng latLng;
	private MyApplication myapp;

	private int queryType=Constant.AREA.QUERY_NEIB_AREAINFO;
	private int pageNo=0;
	//private MyListView listView;
	private BackWidget back_btn;
	private TextView mtitle;
	private View zb_live;
	private ImageView category_img;
	private ViewPager categoryViewPager;
	private View select_category_rl;
	private View go_search;
	//private RelativeLayout main_map;
	private LinearLayout category_fl;
	private LinearLayout page_indicator;//用于显示分类页码的
	private List<ImageView> listPages=new ArrayList<ImageView>();
	private Handler handler=new Handler();
	
	private View help_tip;
	private View close_neighbour_quan_tip;
	private View close_neighbour_category_tip;
	private View neighbour_quan_tip_fl;
	private View neighbour_category_tip_fl;
	private boolean closed_quan;
	private boolean closed_category;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_neighbourhood);
		myapp=(MyApplication)getApplication();
		areainfo=(AreaInfo) this.getIntent().getSerializableExtra(AREAINDO);
		mapView = (MapView) findViewById(R.id.map);
		//mAMapLocManager = LocationManagerProxy.getInstance(this);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		back_btn=(BackWidget)findViewById(R.id.back_btn);
		back_btn.setBackBtnClickListener(new OnBackBtnClickListener() {
			@Override
			public void onBackBtnClick() {
				//				Animation inani=AnimationUtils.loadAnimation(MyNeighbourActivity.this, R.anim.push_top_in);
				//				Animation outani=AnimationUtils.loadAnimation(MyNeighbourActivity.this, R.anim.push_bottom_out);
				//overridePendingTransition(R.anim.push_top_in, R.anim.push_bottom_out);
				finish();
			}
		});
//		select_category_rl=findViewById(R.id.select_category_rl);
//		category_img=(ImageView)findViewById(R.id.category_img);
//		zb_live=findViewById(R.id.zb_live);
//		zb_live.setOnClickListener(this);
//		categoryViewPager = (ViewPager) findViewById(R.id.categoryViewPager);
		page_indicator = (LinearLayout) findViewById(R.id.page_indicator);
		//main_map = (RelativeLayout) findViewById(R.id.main_map);
//		category_fl = (LinearLayout) findViewById(R.id.category_fl);
		go_search=findViewById(R.id.go_search);
		go_search.setOnClickListener(this);
		//setCategory();//一开始时就将其打开
		
		
		initTip();
	}
	//初始化提示
	private void initTip()
	{
//		help_tip=findViewById(R.id.help_tip);
//		neighbour_quan_tip_fl=findViewById(R.id.neighbour_quan_tip_fl);
//		neighbour_category_tip_fl=findViewById(R.id.neighbour_category_tip_fl);
//		String my_neighbour_tip=SharedPreferencesUtil.getString(getApplicationContext(), "my_neighbour_tip", "no");
//		if("no".equals(my_neighbour_tip))
//		{//说明没有显示过提示
//			help_tip.setVisibility(View.VISIBLE);
//			close_neighbour_quan_tip=findViewById(R.id.close_neighbour_quan_tip);
//			close_neighbour_category_tip=findViewById(R.id.close_neighbour_category_tip);
//			close_neighbour_quan_tip.setOnClickListener(this);
//			close_neighbour_category_tip.setOnClickListener(this);
//		}else
//		{
//			help_tip.setVisibility(View.GONE);
//		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		//aMap.moveCamera(CameraUpdateFactory.zoomTo(SizeUtil.my_neighbour_map_size(this)));
		//aMap.moveCamera(CameraUpdateFactory.zoomTo(SizeUtil.my_neighbour_map_size(this)));
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
	}

	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {
		drawMarkers();// 添加10个带有系统默认icon的marker
	}

	/**
	 * 绘制系统默认的1种marker背景图片
	 */
	public void drawMarkers() {
		Marker marker;
		if(neighbours!=null)
		{
			int neighbourSize=neighbours.size();
			if(neighbourSize>26)
			{
				neighbourSize=26;
			}
			for(int kk=0;kk<neighbourSize;kk++)
			{
				AreaInfo ai=neighbours.get(kk);
				LatLng latLng=new LatLng(ai.getLatItude(), ai.getLongItude());
				MarkerOptions markerOption =new MarkerOptions().position(latLng)
						.icon(BitmapDescriptorFactory.fromView(getView("小区"+zm[kk])))
						.title("")
						.draggable(false).visible(true);
				markerOption.anchor(0.5f, 0.5f);
				marker = aMap.addMarker(markerOption);
				marker.setObject(ai);

			}
		}
		//自己小区
		LatLng latLng=new LatLng(areainfo.getLatItude(), areainfo.getLongItude());
		MarkerOptions markerOption =new MarkerOptions().position(latLng)
				.icon(BitmapDescriptorFactory.fromView(getView(null)))
				.title("")
				.draggable(false).visible(true);
		markerOption.anchor(0.5f, 0.5f);
		marker = aMap.addMarker(markerOption);
		marker.setObject(areainfo);
		//		markers = new ArrayList<Marker>();
		//		Marker marker;
		//		// LatLng latlng = new LatLng(36.061, 103.834);
		//		for (int i = 0; i < latLngs.size(); i++) {
		//			Log.i("a", "i = " + i);
		//			latLng = latLngs.get(i);
		//			if (i == 0) {
		//
		//				marker = aMap
		//						.addMarker(new MarkerOptions()
		//								.position(
		//										new LatLng(latLng.latitude,
		//												latLng.longitude))
		//								.draggable(true)
		//								.icon(BitmapDescriptorFactory
		//										.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		//
		//			} else {
		//				marker = aMap.addMarker(new MarkerOptions()
		//						.position(latLng)
		//						// .period(i)
		//						// .title("好好学习")
		//						// .icon(BitmapDescriptorFactory
		//						// .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
		//						.icon(BitmapDescriptorFactory.fromView(getView("test",
		//								latLng.latitude + "")))
		//						.draggable(false).visible(true));
		//			}
		//
		//			marker.setObject(i);
		//			// marker.setRotateAngle(90);// 设置marker旋转90度
		//			marker.showInfoWindow();// 设置默认显示一个infowinfow
		//			markers.add(marker);
		//		}

	}

	/**
	 * 把一个xml布局文件转化成view
	 */
	public View getView(String title) {
		if(NullUtil.isStrNotNull(title))
		{
			View view = getLayoutInflater().inflate(R.layout.area_marker_other, null);
			ImageView area_image=(ImageView)view.findViewById(R.id.area_image);
			TextView text_title = (TextView)view.findViewById(R.id.areaName);
			text_title.setText(title);
			area_image.setBackgroundResource(R.drawable.icon_ar_popup_normal);
			return view;
		}else
		{
			View view = getLayoutInflater().inflate(R.layout.area_marker_me, null);
			return view;
		}
	}

	public View getClickView(String title, String text) {
		View view = getLayoutInflater().inflate(R.layout.area_marker_other, null);
		TextView text_text = (TextView) view.findViewById(R.id.areaName);
		text_text.setText(text);
		text_text.setTextColor(Color.RED);
		return view;
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
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

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}



	//	/**
	//	 * 定位成功后回调函数
	//	 */
	//	@Override
	//	public void onLocationChanged(AMapLocation aLocation) {
	//
	//		if (aLocation != null) {
	//			this.aMapLocation = aLocation;// 判断超时机制
	//			Double geoLat = aLocation.getLatitude();
	//			Double geoLng = aLocation.getLongitude();
	//			String cityCode = "";
	//			String desc = "";
	//			Bundle locBundle = aLocation.getExtras();
	//			if (locBundle != null) {
	//				cityCode = locBundle.getString("citycode");
	//				desc = locBundle.getString("desc");
	//			}
	//			String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
	//					+ "\n精    度    :" + aLocation.getAccuracy() + "米"
	//					+ "\n定位方式:" + aLocation.getProvider() + "\n定位时间:"
	//					+ AMapUtil.convertToTime(aLocation.getTime()) + "\n城市编码:"
	//					+ cityCode + "\n位置描述:" + desc + "\n省:"
	//					+ aLocation.getProvince() + "\n市:" + aLocation.getCity()
	//					+ "\n区(县):" + aLocation.getDistrict() + "\n区域编码:" + aLocation
	//					.getAdCode());
	//			Log.i("a", str);
	//
	//			// changeCamera(
	//			// CameraUpdateFactory.newCameraPosition(new CameraPosition(
	//			// new LatLng(geoLat, geoLng), 18, 0, 30)), null);
	//
	//			aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(geoLat,
	//					geoLng)));
	//
	//			aMap.clear();
	//			circle = aMap.addCircle(new CircleOptions()
	//					.center(new LatLng(geoLat, geoLng)).radius(500)
	//					.strokeColor(Color.argb(50, 1, 1, 1))
	//					.fillColor(Color.argb(50, 1, 1, 1)).strokeWidth(5));
	//			// aMap.addMarker(new MarkerOptions().position(
	//			// new LatLng(geoLat, geoLng)).icon(
	//			// BitmapDescriptorFactory
	//			// .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
	//			stopLocation();
	//
	//			latLngs = new ArrayList<LatLng>();
	//			latLngs.add(new LatLng(geoLat, geoLng));
	//			latLng = new LatLng(geoLat + 0.001, geoLng - 0.001);
	//			latLngs.add(latLng);
	//			latLng = new LatLng(geoLat + 0.001, geoLng + 0.002);
	//			latLngs.add(latLng);
	//			latLng = new LatLng(geoLat - 0.003, geoLng - 0.002);
	//			latLngs.add(latLng);
	//			latLng = new LatLng(geoLat + 0.004, geoLng + 0.005);
	//			latLngs.add(latLng);
	//			addMarkersToMap();
	//		}
	//	}

	/**
	 * 自定义infowinfow窗口
	 */
	public void render(final Marker marker, View view) {
		final AreaInfo ai=(AreaInfo)marker.getObject();
//		int[] info_window_size=SizeUtil.area_map_info_window_size(this);
//		view.findViewById(R.id.area_top_rl).getLayoutParams().width=info_window_size[0];
//		view.findViewById(R.id.info_rl).getLayoutParams().width=info_window_size[0];
		view.findViewById(R.id.close_infowindow).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				marker.hideInfoWindow();
			}
		});
		view.findViewById(R.id.goto_area).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ai.setAreaType(AreaInfo.AREATYPE_NOWSHOW);
				AppContext.setCurrentAreaInfo(ai);
				//MeLifeMainActivity.instance().onMenuSelected(MeLifeMainActivity.instance().index.getIndex());
				finish();
			}
		});
		((TextView)view.findViewById(R.id.area_name_)).setText(ai.getAreaName());
		((TextView)view.findViewById(R.id.area_instance)).setText("相距"+AppContext.getDistance(areainfo.getLatItude(), ai.getLatItude(), areainfo.getLatItude(), areainfo.getLatItude())+"米");
		((TextView)view.findViewById(R.id.enter_merchant_number)).setText("入驻商家："+ai.getEnterNum()+"家");
		((TextView)view.findViewById(R.id.enter_usr_number)).setText("关注人数："+ai.getAttNum()+"人");
		if(NullUtil.isStrNotNull(ai.getIcon()))
		{
			ImageView iv = (ImageView)view.findViewById(R.id.area_picture);
//			int[] size=SizeUtil.area_map_info_window_area_size(this);
//			iv.getLayoutParams().width=size[0];
//			iv.getLayoutParams().height=size[1];
			MeImageLoader.displayImage(ai.getIcon(),iv , myapp.getHeadIconOption());
		}

	}

	@Override
	public View getInfoContents(Marker marker) {
		View infoContent = getLayoutInflater().inflate(
				R.layout.area_marker_open, null);
		render(marker, infoContent);
		return infoContent;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = getLayoutInflater().inflate(
				R.layout.area_marker_open, null);

		render(marker, infoWindow);
		//		View view = getLayoutInflater().inflate(R.layout.area_marker_open, null);
		//		LayoutParams lp=new LayoutParams(250, 180);
		//		view.setLayoutParams(lp);
		return infoWindow;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.go_search:
		{
			startActivity(new Intent(MyNeighbourActivity.this, SearchActivity.class));
			break;
		}
		}
	}
	
	private void setCategory()
	{
		Animation outani=AnimationUtils.loadAnimation(this, R.anim.push_bottom_out);
		if(select_category_rl.getVisibility()==View.GONE)
		{
			Animation inani=AnimationUtils.loadAnimation(this, R.anim.push_bottom_in);
			select_category_rl.setVisibility(View.VISIBLE);
			category_fl.setVisibility(View.VISIBLE);
			category_fl.setAnimation(inani);
			inani.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					category_img.setImageResource(R.drawable.close);
				}
			});
			loadCityCategory();
		}else
		{
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					select_category_rl.setVisibility(View.GONE);
					category_img.setImageResource(R.drawable.more_function);
				}
			}, 200);
			category_fl.setVisibility(View.GONE);
			category_fl.setAnimation(outani);
		}
	}


	@Override
	public void onMapLoaded() {
		double lat=areainfo.getLatItude();
		double lng=areainfo.getLongItude();
		LatLng ll=new LatLng(lat,lng);
		//aMap.moveCamera(CameraUpdateFactory.changeLatLng(ll));
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, SizeUtil.my_neighbour_map_size(this)));// 设置指定的可视区域地图
		aMap.clear();
		circle = aMap.addCircle(new CircleOptions()
		.center(new LatLng(lat,lng)).radius(800)
		.strokeColor(getResources().getColor(R.color.myneighbour_stroke_color))
		.fillColor(getResources().getColor(R.color.myneighbour_fill_color)).strokeWidth(3));
		
		//		circle = aMap.addCircle(new CircleOptions()
		//		.center(ll).radius(500)
		//		.strokeColor(Color.argb(100, 1, 1, 1))
		//		.fillColor(Color.argb(50, 1, 1, 1)).strokeWidth(5));
		
		loadNeighbour();
	}

	//加载我的近邻小区
	private void loadNeighbour() {
		MyNeighbourReq myNeighbourReq = new MyNeighbourReq();
		myNeighbourReq.setSid(AppContext.currentAreaInfo().getSid());
		RequestBean<NeighbourResponse> requestBean = new RequestBean<NeighbourResponse>();
		requestBean.setRequestBody(myNeighbourReq);
		requestBean.setResponseBody(NeighbourResponse.class);
		requestBean.setURL(Constant.Requrl.getNearByAreaInfoList());
		MeMessageService.instance().requestServer(requestBean, new UICallback<NeighbourResponse>() {
			@Override
			public void onSuccess(NeighbourResponse respose) {
				try {
					neighbours = respose.getList();
					if (neighbours == null)
						return;
					addMarkersToMap();
				} catch (Exception e) {
				}
			}

			@Override
			public void onError(String message) {
				MeUtil.showToast(MyNeighbourActivity.this,message);
			}

			@Override
			public void onOffline(String message) {
				MeUtil.showToast(MyNeighbourActivity.this,message);
			}
		});
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		//Toast.makeText(MyNeighbourActivity.this, "click", 0).show();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		return false;
	}

	public void loadCityCategory() {
		if(AppContext.currentAreaInfo()==null)
		{
			return;
		}

		initCategory(AppContext.currentCategorys(AppContext.currentAreaInfo().getAlongCitySid()));
//		CategoryReq zoneMainFunctionReq = new CategoryReq();
//		zoneMainFunctionReq.setCitySid(AppContext.currentAreaInfo().getAlongCitySid());
//		RequestBean<ZoneMainFunctionResponse> requestBean = new RequestBean<ZoneMainFunctionResponse>();
//		requestBean.setRequestBody(zoneMainFunctionReq);
//		requestBean.setResponseBody(ZoneMainFunctionResponse.class);
//		requestBean.setURL(Constant.Requrl.getCategoryList());
//		MeLifeMainActivity.instance().requestServer(requestBean, new UICallback<ZoneMainFunctionResponse>() {
//
//			@Override
//			public void onSuccess(ZoneMainFunctionResponse respose) {
//
//				if (respose != null && respose.getList() != null) {
//					new Delete().from(Category.class).execute();
//					new Delete().from(ForumInfo.class).execute();
//					for (Category category : respose.getList()) {
//						category.save();
//					}
//					initCategory(respose.getList());
//				}
//			}
//
//			@Override
//			public void onError(String message) {
//
//			}
//
//			@Override
//			public void onOffline() {
//			}
//		});

	}
	private int categorySize=6;
	private void initCategory(List<Category> list)
	{
		if(listPages.size()>0)
		{
			return;
		}
		List<Category> okCategory=new ArrayList<Category>();
		for(Category category:list)
		{
			if(category.getHomeDisplay()==Constant.CATEGORY.NOT_SHOW_AS_NEIGHBOUR_TALK)
			{
				okCategory.add(category);
			}
		}
		LayoutInflater mInflater = getLayoutInflater();

		int lsize=okCategory.size();
		int page=1;
		if(lsize%categorySize==0)
		{
			page=lsize/categorySize;
		}else
		{
			page=lsize/categorySize+1;
		}
		if(page==1)
		{
			page_indicator.setVisibility(View.GONE);
		}else
		{
			page_indicator.setVisibility(View.VISIBLE);
		}
		page_indicator.removeAllViews();
		final List<View> mListViews=new ArrayList<View>();
		for(int kk=0;kk<page;kk++)
		{
			ImageView iv=new ImageView(this);
			android.widget.LinearLayout.LayoutParams vp=new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			iv.setLayoutParams(vp);
			vp.setMargins(5, 0, 5, 0);
			if(kk==0)
			{
				iv.setBackgroundResource(R.drawable.page_indicator_focused);
			}else
			{
				iv.setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			listPages.add(iv);
			page_indicator.addView(iv);


			View v = mInflater.inflate(R.layout.main_category_gridview, null);
			mListViews.add(v);
			GridView gridView = (GridView) v.findViewById(R.id.main_gridview);
			gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			ZoneMainFunctionAdapter zoneMainFunctionAdapter = new ZoneMainFunctionAdapter(this);
			gridView.setAdapter(zoneMainFunctionAdapter);
			List<Category> indexCategory=new ArrayList<Category>();
			int mk=kk*categorySize;
			int msize=(kk+1)*categorySize;
			if(msize>lsize)
			{
				msize=lsize;
			}
			for(;mk<msize;mk++)
			{
				indexCategory.add(okCategory.get(mk));
			}

			zoneMainFunctionAdapter.load(indexCategory, false);
			setOnGridViewItemListener(gridView, zoneMainFunctionAdapter);
		}
		categoryViewPager.setAdapter(new PagerAdapter() {
			@Override  
			public void destroyItem(ViewGroup container, int position, Object object)   {     
				container.removeView(mListViews.get(position));//删除页卡  
			}

			@Override  
			public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡         
				container.addView(mListViews.get(position), 0);//添加页卡  
				return mListViews.get(position);  
			}  

			@Override  
			public int getCount() {           
				return  mListViews.size();//返回页卡的数量  
			}  

			@Override  
			public boolean isViewFromObject(View arg0, Object arg1) {             
				return arg0==arg1;//官方提示这样写  
			}
		});
		categoryViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				int size=listPages.size();
				for(int kk=0;kk<size;kk++)
				{
					ImageView iv=listPages.get(kk);
					if(position==kk)
					{
						iv.setBackgroundResource(R.drawable.page_indicator_focused);
					}else
					{
						iv.setBackgroundResource(R.drawable.page_indicator_unfocused);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	//某个分类被选中了，则进入对应的栏目列表中
	private void setOnGridViewItemListener(GridView gridView, final ZoneMainFunctionAdapter zoneMainFunctionAdapter) {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				startActivity(new Intent(MyNeighbourActivity.this, ForumMerchantActivity.class).putExtra(Constant.ActivityExtra.CATEGORY, zoneMainFunctionAdapter.getItem(arg2)));
				hideCategory();
			}
		});
	}
	private void hideCategory()
	{
		if(select_category_rl!=null)
		{
			category_fl.setVisibility(View.GONE);
			category_img.setImageResource(R.drawable.more_function);
		}
	}

}
