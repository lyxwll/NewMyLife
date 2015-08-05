package com.yulinoo.plat.life.views.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
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
import com.yulinoo.plat.life.bean.LocationPoint;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.BackWidget.OnBackBtnClickListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.melife.R;

public class MerchantChoiceMapActivity extends Activity implements OnMapClickListener, OnMarkerClickListener, OnMapLoadedListener, OnClickListener {
	public static final String AREAINDO="AREAINDO";
	private AMap aMap;
	private MapView mapView;
	private Circle circle;

	public static final int AMAP_SELECTED=1010;
	public static final String LOCAP="LOCAP";//传出的地图信息
	public static final String area="area";//传入的小区信息
	public static final String MERCHANT="merchant";//传入的商家信息

	private AreaInfo areaInfo;
	private Merchant merchant;
	private MyApplication myapp;

	private int queryType=Constant.AREA.QUERY_NEIB_AREAINFO;
	private int pageNo=0;
	//private MyListView listView;
	private BackWidget back_btn;
	private TextView mtitle;
	private ImageView zb_live;
	private ViewPager categoryViewPager;
	private View select_category_rl;
	private RelativeLayout main_map;
	private LinearLayout category_fl;
	private LinearLayout page_indicator;//用于显示分类页码的
	private List<ImageView> listPages=new ArrayList<ImageView>();
	private Handler handler=new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_map);
		myapp=(MyApplication)getApplication();
		areaInfo = (AreaInfo) this.getIntent().getSerializableExtra(area);
		merchant = (Merchant) this.getIntent().getSerializableExtra(MERCHANT);
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
		TextView title=(TextView)findViewById(R.id.title);
		title.setText("请选择您的位置坐标");

		TextView rightText=(TextView)findViewById(R.id.right_text);
		rightText.setText("完成");
		rightText.setVisibility(View.VISIBLE);
		rightText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(merchant.getLongItude()>0&&merchant.getLatItude()>0)
				{
					LocationPoint locationPoint=new LocationPoint();
					locationPoint.setLatitude(merchant.getLatItude());
					locationPoint.setLongitude(merchant.getLongItude());
					Intent intent = new Intent();
					intent.putExtra(LOCAP, locationPoint);
					setResult(AMAP_SELECTED, intent);
					finish();
				}else
				{
					MeUtil.showToast(MerchantChoiceMapActivity.this, "请选择店铺的地图位置");
				}
			}
		});	
		
		BackWidget back_btn=(BackWidget)findViewById(R.id.back_btn);
		if(back_btn!=null)
		{
			back_btn.setBackBtnClickListener(new OnBackBtnClickListener() {
				@Override
				public void onBackBtnClick() {
					finish();
				}
			});
		}

//		View left_head = findViewById(R.id.left_head);
//		if(left_head!=null)
//		{
//			left_head.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					finish();
//				}
//			});
//		}


		//		back_btn=(BackWidget)findViewById(R.id.back_btn);
		//		back_btn.setBackBtnClickListener(new OnBackBtnClickListener() {
		//			@Override
		//			public void onBackBtnClick() {
		////				Animation inani=AnimationUtils.loadAnimation(MyNeighbourActivity.this, R.anim.push_top_in);
		////				Animation outani=AnimationUtils.loadAnimation(MyNeighbourActivity.this, R.anim.push_bottom_out);
		//				//overridePendingTransition(R.anim.push_top_in, R.anim.push_bottom_out);
		//				finish();
		//				
		//			}
		//		});
		//		select_category_rl=findViewById(R.id.select_category_rl);
		//		zb_live=(ImageView)findViewById(R.id.zb_live);
		//		zb_live.setOnClickListener(this);
		//		categoryViewPager = (ViewPager) findViewById(R.id.categoryViewPager);
		//		page_indicator = (LinearLayout) findViewById(R.id.page_indicator);
		//		main_map = (RelativeLayout) findViewById(R.id.main_map);
		//		category_fl = (LinearLayout) findViewById(R.id.category_fl);
		//		initSize();
	}
	private void initSize()
	{
		//		((TextView)findViewById(R.id.mtitle)).setTextSize(SizeUtil.title_menu_text_size(this));//标题字体大小
		//		findViewById(R.id.main_title_rl).getLayoutParams().height=SizeUtil.title_height(this);//标题栏高度
		//		zb_live.getLayoutParams().width=SizeUtil.zhoubian_size(this);
		//		zb_live.getLayoutParams().height=SizeUtil.zhoubian_size(this);
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		aMap.moveCamera(CameraUpdateFactory.zoomTo(SizeUtil.my_neighbour_map_size(this)+2));
		aMap.setOnMapClickListener(this);
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
	}





	/**
	 * 绘制系统默认的1种marker背景图片
	 */
	public void drawMarkers() {
		LatLng ll=new LatLng(areaInfo.getLatItude(),areaInfo.getLongItude());
		aMap.moveCamera(CameraUpdateFactory.changeLatLng(ll));
		circle = aMap.addCircle(new CircleOptions()
		.center(ll).radius(200)
		.strokeColor(getResources().getColor(R.color.myneighbour_stroke_color))
		.fillColor(getResources().getColor(R.color.myneighbour_fill_color)).strokeWidth(5));
		Marker marker;

		if(merchant!=null&&merchant.getLatItude()>0&&merchant.getLongItude()>0)
		{
			//自己小区
			LatLng latLng=new LatLng(merchant.getLatItude(), merchant.getLongItude());
			MarkerOptions markerOption =new MarkerOptions().position(latLng)
					.icon(BitmapDescriptorFactory.fromView(getView(null)))
					.title("")
					.draggable(false).visible(true);
			markerOption.anchor(0.5f, 0.5f);
			marker = aMap.addMarker(markerOption);
			marker.setObject(merchant);
		}else
		{
			merchant = new Merchant();
			merchant.setLatItude(areaInfo.getLatItude());
			merchant.setLongItude(areaInfo.getLongItude());
			//自己小区
			LatLng latLng=new LatLng(merchant.getLatItude(), merchant.getLongItude());
			MarkerOptions markerOption =new MarkerOptions().position(latLng)
					.icon(BitmapDescriptorFactory.fromView(getView(null)))
					.title("")
					.draggable(false).visible(true);
			markerOption.anchor(0.5f, 0.5f);
			marker = aMap.addMarker(markerOption);
			marker.setObject(merchant);
		}
	}

	/**
	 * 把一个xml布局文件转化成view
	 */
	public View getView(String title) {

		if(NullUtil.isStrNotNull(title))
		{
			View view = getLayoutInflater().inflate(R.layout.area_marker_other, null);
			//ImageView area_image=(ImageView)view.findViewById(R.id.area_image);
//			int size[]=SizeUtil.my_neighbour_marker_size(this);
//			area_image.getLayoutParams().width=size[0];
//			area_image.getLayoutParams().height=size[1];
			TextView text_title = (TextView)view.findViewById(R.id.areaName);
			text_title.setText(title);
			//int txtpro[]=SizeUtil.my_neighbour_marker_txt(this);
			//text_title.setTextSize(txtpro[0]);
			//RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
			//lp.setMargins(txtpro[1], txtpro[2], 0, 0);  
			//text_title.setLayoutParams(lp);  
			return view;
		}else
		{
			View view = getLayoutInflater().inflate(R.layout.area_marker_me, null);
			return view;
		}
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






	@Override
	public void onClick(View v) {
	}


	@Override
	public void onMapLoaded() {
		
		

		aMap.clear();
		
		//		circle = aMap.addCircle(new CircleOptions()
		//		.center(ll).radius(500)
		//		.strokeColor(Color.argb(100, 1, 1, 1))
		//		.fillColor(Color.argb(50, 1, 1, 1)).strokeWidth(5));
		drawMarkers();
	}



	@Override
	public boolean onMarkerClick(Marker marker) {

		return false;
	}


	@Override
	public void onMapClick(LatLng point) {
		aMap.clear();
		merchant.setLatItude(point.latitude);
		merchant.setLongItude(point.longitude);
		drawMarkers();
	}



}


//extends BaseActivity implements OnMapLoadedListener,OnMapClickListener {
//	public static final int AMAP_SELECTED=1010;
//	public static final String LOCAP="LOCAP";//传出的地图信息
//	public static final String area="area";//传入的小区信息
//	public static final String MERCHANT="merchant";//传入的商家信息
//	private AMap aMap;
//	private MapView mapView;
//
//	private MyApplication myapp;
//
//	private int queryType=Constant.AREA.QUERY_NEIB_AREAINFO;
//	private int pageNo=0;
//	private MyListView listView;
//	private BackWidget back_btn;
//	private TextView mtitle;
//	private Handler handler=new Handler();
//	private AreaInfo areaInfo;
//	private Merchant merchant;
////	@Override
////	protected void onCreate(Bundle savedInstanceState) {
////		super.onCreate(savedInstanceState);
////		
////	}
//
//	/**
//	 * 初始化AMap对象
//	 */
//	private void init() {
//		if (aMap == null) {
//			aMap = mapView.getMap();
//			setUpMap();
//		}
//		initSize();
//	}
//	private void initSize()
//	{
////		((TextView)findViewById(R.id.mtitle)).setTextSize(SizeUtil.title_menu_text_size(this));//标题字体大小
////		findViewById(R.id.main_title_rl).getLayoutParams().height=SizeUtil.title_height(this);//标题栏高度
//	}
//
//	/**
//	 * 设置一些amap的属性
//	 */
//	private void setUpMap() {
//		// 自定义系统定位小蓝点
//		aMap.moveCamera(CameraUpdateFactory.zoomTo(SizeUtil.my_neighbour_map_size(this)+1));
//		aMap.setOnMapClickListener(this);
//		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
//	}
//
//	/**
//	 * 绘制系统默认的1种marker背景图片
//	 */
//	public void drawMarkers() {
//		Marker marker;			
//		LatLng ll=new LatLng(areaInfo.getLatItude(),areaInfo.getLongItude());
//		aMap.moveCamera(CameraUpdateFactory.changeLatLng(ll));
//		aMap.clear();
//		aMap.addCircle(new CircleOptions()
//		.center(ll).radius(300)
//		.strokeColor(getResources().getColor(R.color.main_color))
//		.fillColor(getResources().getColor(R.color.my_neighbour_bg_circle_color)).strokeWidth(5));
//		
//		if(merchant!=null&&merchant.getLatItude()>0&&merchant.getLongItude()>0)
//		{//商家自身
//			LatLng latLng=new LatLng(merchant.getLatItude(), merchant.getLongItude());
//			MarkerOptions markerOption =new MarkerOptions().position(latLng)
//					.icon(BitmapDescriptorFactory.fromView(getView(null)))
//					.title("")
//					.draggable(false).visible(true);
//			markerOption.anchor(0.5f, 0.5f);
//			marker = aMap.addMarker(markerOption);
//			marker.setObject(areaInfo);
//		}
//	}
//
//	/**
//	 * 把一个xml布局文件转化成view
//	 */
//	public View getView(String title) {
//		View view = getLayoutInflater().inflate(R.layout.area_marker, null);
//		ImageView area_image=(ImageView)view.findViewById(R.id.area_image);
//		int size[]=SizeUtil.my_neighbour_marker_size(this);
//		area_image.getLayoutParams().width=size[0];
//		area_image.getLayoutParams().height=size[1];
//		TextView text_title = (TextView)view.findViewById(R.id.areaName);
//		if(NullUtil.isStrNotNull(title))
//		{
//			text_title.setText(title);
//			int txtpro[]=SizeUtil.my_neighbour_marker_txt(this);
//			text_title.setTextSize(txtpro[0]);
//			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
//			lp.setMargins(txtpro[1], txtpro[2], 0, 0);  
//			text_title.setLayoutParams(lp);  
//		}else
//		{
//			text_title.setVisibility(View.GONE);
//			area_image.setImageResource(R.drawable.icon_map_pop_me);
//		}
//
//		return view;
//	}
//
//	public View getClickView(String title, String text) {
//		View view = getLayoutInflater().inflate(R.layout.area_marker, null);
//		TextView text_text = (TextView) view.findViewById(R.id.areaName);
//		text_text.setText(text);
//		text_text.setTextColor(Color.RED);
//		return view;
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	protected void onResume() {
//		super.onResume();
//		mapView.onResume();
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	protected void onPause() {
//		super.onPause();
//		mapView.onPause();
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		mapView.onSaveInstanceState(outState);
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		mapView.onDestroy();
//	}
//
//	@Override
//	public void onMapLoaded() {
//		drawMarkers();
//	}
//
//	
//	@Override
//	public void onMapClick(LatLng point) {
//		merchant.setLatItude(point.latitude);
//		merchant.setLongItude(point.longitude);
//		drawMarkers();
//	}
//	
//
//	@Override
//	protected void initView(Bundle savedInstanceState) {
////		setContentView(R.layout.merchant_map);
////		mapView = (MapView) findViewById(R.id.map);
////		mapView.onCreate(savedInstanceState); // 此方法必须重写
////		init();
////		areaInfo = (AreaInfo) this.getIntent().getSerializableExtra(area);
////		merchant = (Merchant) this.getIntent().getSerializableExtra(MERCHANT);
//		
//		setContentView(R.layout.merchant_map);
//		myapp=(MyApplication)getApplication();
//		mapView = (MapView) findViewById(R.id.map);
//		mapView.onCreate(savedInstanceState);// 此方法必须重写
//		areaInfo = (AreaInfo) this.getIntent().getSerializableExtra(area);
//		merchant = (Merchant) this.getIntent().getSerializableExtra(MERCHANT);
//		init();
//	}
//
//	@Override
//	protected void initComponent() {
//		
//	}
//
//	@Override
//	protected void initHead(TextView leftImg, TextView leftText,
//			TextView rightImg, TextView rightText, TextView title,
//			View titleLayout) {
//		title.setText("请选择店铺地图位置");
//		rightImg.setVisibility(View.GONE);
//		rightText.setText("完成");
//		rightText.setVisibility(View.VISIBLE);
//		rightText.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(merchant.getLongItude()>0&&merchant.getLatItude()>0)
//				{
//					LocationPoint locationPoint=new LocationPoint();
//					locationPoint.setLatitude(merchant.getLatItude());
//					locationPoint.setLongitude(merchant.getLongItude());
//					Intent intent = new Intent();
//					intent.putExtra(LOCAP, locationPoint);
//					setResult(AMAP_SELECTED, intent);
//					finish();
//				}else
//				{
//					showToast("请选择店铺的地图位置");
//				}
//				
//			}
//		});
//	}
//
//	
//	
//}















//extends MarkPointToMapActivity {
//	//已成功标识店铺地图
//	public static final int AMAP_SELECTED=1010;
//	public static final String LOCAP="LOCAP";//传出的地图信息
//	public static final String area="area";//传入的小区信息
//	public static final String MERCHANT="merchant";//传入的商家信息
//	private AreaInfo areaInfo;
//	private Merchant merchant;
//	private LocationPoint locationPoint =null;//所选择的地图位置
//	@Override
//	public void onClick(View v) {
//
//	}
//
//	@Override
//	protected List<LocationPoint> loadPointToMap() {
//		List<LocationPoint> locationPoints = new ArrayList<LocationPoint>();
////		if(areaInfo!=null&&areaInfo.getLongItude()>0&&areaInfo.getLatItude()>0)
////		{
////			locationPoint = new LocationPoint();
////			locationPoint.setAddress(areaInfo.getAreaName());
////			locationPoint.setLatitude(areaInfo.getLatItude());
////			locationPoint.setLongitude(areaInfo.getLongItude());
////			locationPoint.setName(areaInfo.getAreaName());
////			locationPoint.localType=LocationPoint.LOCAL_MERCHANT;
////			locationPoints.add(locationPoint);
////		}
//		
//		if(merchant!=null)
//		{
//			LocationPoint merchantLp=new LocationPoint();
//			merchantLp.setAddress(merchant.getAreaName());
//			merchantLp.setLatitude(merchant.getLatItude());
//			merchantLp.setLongitude(merchant.getLongItude());
//			merchantLp.setName(merchant.getMerchantName());
//			merchantLp.localType=LocationPoint.LOCAL_MERCHANT;
//			locationPoints.add(merchantLp);
//		}
//		return locationPoints;
//	}
//
//	@Override
//	protected void initView(Bundle savedInstanceState) {
//		setContentView(R.layout.merchant_map);
//		mapView = (MapView) findViewById(R.id.map);
//		mapView.onCreate(savedInstanceState); // 此方法必须重写
//		init();
//		areaInfo = (AreaInfo) this.getIntent().getSerializableExtra(area);
//		merchant = (Merchant) this.getIntent().getSerializableExtra(MERCHANT);
//	}
//
//	@Override
//	protected void initComponent() {
//
//	}
//
//	@Override
//	protected void initHead(TextView leftImg, TextView leftText, TextView rightImg, TextView rightText, TextView title, View titleLayout) {
//		title.setText("请选择店铺地图位置");
//		rightImg.setVisibility(View.GONE);
//		rightText.setText("完成");
//		rightText.setVisibility(View.VISIBLE);
//		rightText.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(locationPoint==null)
//				{
//					showToast("请选择店铺的地图位置");
//					return;
//				}
//				Intent intent = new Intent();
//				intent.putExtra(LOCAP, locationPoint);
//				setResult(AMAP_SELECTED, intent);
//				finish();
//			}
//		});
//	}
//
//	@Override
//	public void onMapClick(LatLng point) {
//		aMap.clear();
//		MarkerOptions markerOption = new MarkerOptions().position(point).title("XXX").snippet("KKK").icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)).draggable(false);
//		aMap.addMarker(markerOption);
//
//		LatLng latLng = new LatLng(locationPoint.getLatitude(), locationPoint.getLongitude());
//		latLngs.add(latLng);
//		MarkerOptions markerOptionArea = null;
//		markerOptionArea = new MarkerOptions().position(latLng).title(locationPoint.getName()).snippet(locationPoint.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)).draggable(false);
//		aMap.addMarker(markerOptionArea); // 设置此marker可以拖拽
//
//
//
//	}
//
//}
