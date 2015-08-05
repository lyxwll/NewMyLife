package com.yulinoo.plat.life.ui.widget;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.MyNeighbourReq;
import com.yulinoo.plat.life.net.resbean.NeighbourResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.views.activity.NeighbourTalkActivity;
import com.yulinoo.plat.life.views.activity.NewZoneSelectActivity;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class NeighbourCircleWidget extends LinearLayout implements
		OnMarkerClickListener, OnInfoWindowClickListener, OnMapLoadedListener,
		OnClickListener, InfoWindowAdapter {
	private Context mContext;
	private MyApplication myapp;

	public NeighbourCircleWidget(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public NeighbourCircleWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private AMap aMap;
	private MapView mapView;

	private List<AreaInfo> neighbours;// 小区的近邻小区
	// private String[] zm=new
	// String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private AreaInfo lastArea;// 上次小区,主要考虑优化查询,用户可能会切换小区,如果存在切换小区的情况,那么就需要重新更新近邻,如果没有,则只需要取一次近邻即可
	public ImageView indirector_selector;// 第一次引导帮助

	private void init() {
		myapp = (MyApplication) mContext.getApplicationContext();
		final View convertView = (View) LayoutInflater.from(mContext).inflate(
				R.layout.my_neighbourhood, null);
		mapView = (MapView) convertView.findViewById(R.id.neibur_map);
		indirector_selector = (ImageView) convertView
				.findViewById(R.id.neibur_indirector_selector);
		if (AppContext.currentAreaInfo() != null) {
			indirector_selector.setVisibility(View.GONE);
			mapView.setVisibility(View.VISIBLE);
		} else {
			
			indirector_selector.setVisibility(View.VISIBLE);
			mapView.setVisibility(View.INVISIBLE);
			indirector_selector.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// startActivityForResult(new Intent(getActivity(),
					// ZoneSelectActivity.class), 100);
					mContext.startActivity(new Intent(mContext,
							NewZoneSelectActivity.class));
				}
			});
		}
		this.addView(convertView);
	}

	/**
	 * 初始化AMap对象
	 */
	public void initMap() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		// aMap.moveCamera(CameraUpdateFactory.zoomTo(SizeUtil.my_neighbour_map_size(this)));
		// aMap.moveCamera(CameraUpdateFactory.zoomTo(SizeUtil.my_neighbour_map_size(this)));
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
	}

	public MapView getMapView() {
		return mapView;
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public View getInfoContents(Marker marker) {
		View infoContent = LayoutInflater.from(mContext).inflate(
				R.layout.area_marker_open, null);
		render(marker, infoContent);
		return infoContent;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = LayoutInflater.from(mContext).inflate(
				R.layout.area_marker_open, null);
		render(marker, infoWindow);
		return infoWindow;
	}

	/**
	 * 自定义infowinfow窗口
	 */
	public void render(final Marker marker, View view) {
		final AreaInfo ai = (AreaInfo) marker.getObject();
		view.findViewById(R.id.close_infowindow).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						marker.hideInfoWindow();
					}
				});
		view.findViewById(R.id.goto_area).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						ai.setAreaType(AreaInfo.AREATYPE_NOWSHOW);
						AppContext.setCurrentAreaInfo(ai);
						Intent intent = new Intent();
						intent.setAction(Constant.BroadType.AREA_CHANGED);
						mContext.sendBroadcast(intent);
						marker.hideInfoWindow();
						mContext.startActivity(new Intent(mContext,
								NeighbourTalkActivity.class));
					}
				});
		AreaInfo areainfo = AppContext.currentAreaInfo();
		if (areainfo != null) {
			((TextView) view.findViewById(R.id.area_name_)).setText(ai
					.getAreaName());
			((TextView) view.findViewById(R.id.area_instance)).setText("相距"
					+ AppContext.getDistance(areainfo.getLatItude(),
							ai.getLatItude(), areainfo.getLatItude(),
							areainfo.getLatItude()) + "米");
			((TextView) view.findViewById(R.id.enter_merchant_number))
					.setText("入驻商家：" + ai.getEnterNum() + "家");
			((TextView) view.findViewById(R.id.enter_usr_number))
					.setText("关注人数：" + ai.getAttNum() + "人");
			if (NullUtil.isStrNotNull(ai.getIcon())) {
				ImageView iv = (ImageView) view.findViewById(R.id.area_picture);
				MeImageLoader.displayImage(ai.getIcon(), iv,
						myapp.getHeadIconOption());
			}
		}
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		return false;
	}

	@Override
	public void onMapLoaded() {
		loadNeighbour();
	}

	public void showNeiburCircle() {
		loadNeighbour();
	}

	public void refreshMap() {
		AreaInfo areainfo = AppContext.currentAreaInfo();
		if (areainfo != null) {
			if (neighbours != null) {
				double lat = areainfo.getLatItude();
				double lng = areainfo.getLongItude();
				LatLng ll = new LatLng(lat, lng);
				// aMap.moveCamera(CameraUpdateFactory.changeLatLng(ll));
				aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll,
						SizeUtil.my_neighbour_map_size(mContext)));// 设置指定的可视区域地图
				aMap.clear();
				aMap.addCircle(new CircleOptions()
						.center(new LatLng(lat, lng))
						.radius(500)
						.strokeColor(
								getResources().getColor(
										R.color.myneighbour_stroke_color))
						.fillColor(
								getResources().getColor(
										R.color.myneighbour_fill_color))
						.strokeWidth(3));
				addMarkersToMap();
			}
		}
	}

	// 加载我的近邻小区
	private void loadNeighbour() {
		AreaInfo areainfo = AppContext.currentAreaInfo();
		if (areainfo != null) {
			if (indirector_selector != null) {
				indirector_selector.setVisibility(View.GONE);
				mapView.setVisibility(View.VISIBLE);
			}
			if (lastArea != null) {
				if (lastArea.getSid() == areainfo.getSid()) {// 还是同一个小区,则不需要再次加载近邻
					return;
				}
			}
			lastArea = areainfo;
			double lat = areainfo.getLatItude();
			double lng = areainfo.getLongItude();
			LatLng ll = new LatLng(lat, lng);
			// aMap.moveCamera(CameraUpdateFactory.changeLatLng(ll));
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll,
					SizeUtil.my_neighbour_map_size(mContext)));// 设置指定的可视区域地图
			aMap.clear();
			aMap.addCircle(new CircleOptions()
					.center(new LatLng(lat, lng))
					.radius(500)
					.strokeColor(
							getResources().getColor(
									R.color.myneighbour_stroke_color))
					.fillColor(
							getResources().getColor(
									R.color.myneighbour_fill_color))
					.strokeWidth(3));

			MyNeighbourReq myNeighbourReq = new MyNeighbourReq();
			myNeighbourReq.setSid(AppContext.currentAreaInfo().getSid());
			RequestBean<NeighbourResponse> requestBean = new RequestBean<NeighbourResponse>();
			requestBean.setHttpMethod(HTTP_METHOD.POST);
			requestBean.setRequestBody(myNeighbourReq);
			requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
			requestBean.setResponseBody(NeighbourResponse.class);
			requestBean.setURL(Constant.Requrl.getNearByAreaInfoList());
			MeMessageService.instance().requestServer(requestBean,
					new UICallback<NeighbourResponse>() {
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
							MeUtil.showToast(mContext, message);
						}

						@Override
						public void onOffline(String message) {
							MeUtil.showToast(mContext, message);
						}
					});
		}
	}

	/**
	 * 绘制系统默认的1种marker背景图片
	 */
	public void addMarkersToMap() {
		Marker marker;
		if (neighbours != null) {
			int neighbourSize = neighbours.size();

			for (int kk = 0; kk < neighbourSize; kk++) {
				AreaInfo ai = neighbours.get(kk);
				LatLng latLng = new LatLng(ai.getLatItude(), ai.getLongItude());

				MarkerOptions markerOption = new MarkerOptions()
						.position(latLng)
						.icon(BitmapDescriptorFactory.fromView(getView(
								ai.getAreaName(), false))).title("")
						.draggable(false).visible(true);
				markerOption.anchor(0.5f, 0.5f);
				marker = aMap.addMarker(markerOption);
				marker.setObject(ai);
			}
		}
		// 自己小区
		AreaInfo areainfo = AppContext.currentAreaInfo();
		if (areainfo != null) {
			LatLng latLng = new LatLng(areainfo.getLatItude(),
					areainfo.getLongItude());
			MarkerOptions markerOption = new MarkerOptions()
					.position(latLng)
					.icon(BitmapDescriptorFactory.fromView(getView(
							areainfo.getAreaName(), true))).title("")
					.draggable(false).visible(true);
			markerOption.anchor(0.5f, 0.5f);
			marker = aMap.addMarker(markerOption);
			marker.setObject(areainfo);
		}
	}

	/**
	 * 把一个xml布局文件转化成view
	 */
	public View getView(String title, boolean isMe) {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.area_marker_other, null);
		// ImageView area_image=(ImageView)view.findViewById(R.id.area_image);
		TextView text_title = (TextView) view.findViewById(R.id.areaName);
		text_title.setText(title);
		if (isMe == true) {
			view.setBackgroundResource(R.drawable.icon_ar_popup_me);
		}
		return view;
	}

}
