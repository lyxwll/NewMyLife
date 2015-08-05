package com.yulinoo.plat.life.views.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.LatLngBounds.Builder;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.yulinoo.plat.life.bean.LocationPoint;
import com.yulinoo.plat.melife.R;

/**
 * 地图展示定位点
 * 
 * @author jefry
 * 
 */
public abstract class MarkPointToMapActivity extends BaseActivity implements OnMapClickListener, OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener, OnMapLoadedListener, OnClickListener, InfoWindowAdapter {
	protected AMap aMap;
	protected MapView mapView;
	List<LatLng> latLngs = new ArrayList<LatLng>();
	private TextView right_btn2;

	protected abstract List<LocationPoint> loadPointToMap();
	
	private MapMarkListener mapMarkListener;
	
	public interface MapMarkListener {
		public void onMapMark(Marker marker);
	}
	
	public void setMapMarkListener(MapMarkListener mapMarkListener) {
		this.mapMarkListener = mapMarkListener;
	}

	/**
	 * 初始化AMap对象
	 */
	protected void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	private void setUpMap() {
		//aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		//aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		//aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		//aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		//aMap.setOnMapClickListener(this);
		// addMarkersToMap(employeeLocationInfos);// 往地图上添加marker
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

	/**
	 * 绘制系统默认的10种marker背景图片
	 */
	public void drawMarkers(List<LocationPoint> locationPoints) {
		if(locationPoints == null) return;
		latLngs.clear();
		aMap.clear();
		if (locationPoints == null || locationPoints.size() == 0) {
			return;
		}

		if (locationPoints != null && locationPoints.size() == 1) {
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationPoints.get(0).getLatitude(), locationPoints.get(0).getLongitude()), 16));
		}
		
		{
//			try
//			{
//			List<Route> rts = Route.calculateRoute(mAct,new FromAndTo(new GeoPoint(40101523,116321978),new GeoPoint(39991569,116337744), FromAndTo.NoTrans), 
//			  Route.BusDefault); // 计算路径 
//			  RouteOverlay ol = new RouteOverlay(this, rts.get(0));   //获取第一条路径的Overlay 
//			  ol.registerRouteMessage(this); //注册消息处理函数  ol.addToMap(mMapView); // 加入到地图  
//			 
//			 } catch (Exception e) {  e.printStackTrace(); 
//			 
//			}
			
			
		}

		int count = 1;
		for (LocationPoint locationPoint : locationPoints) {
			
			LatLng latLng = new LatLng(locationPoint.getLatitude(), locationPoint.getLongitude());
			latLngs.add(latLng);
			MarkerOptions markerOption = null;
			if (locationPoint.isMe()) {
				markerOption = new MarkerOptions().position(latLng).title(locationPoint.getName()).snippet(locationPoint.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)).draggable(false);
			} else {
				locationPoint.setNumber(count);
				//markerOption = new MarkerOptions().position(latLng).title(locationPoint.getName()).snippet(locationPoint.getName()).icon(BitmapDescriptorFactory.fromView(getView(count + "",R.drawable.icon_blue_location,locationPoint.getName()))).perspective(true).draggable(true);
				//markerOption = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromView(getView(count + "",R.drawable.icon_blue_location,locationPoint.getName()))).perspective(true).draggable(true);
				markerOption = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromView(getView(locationPoint))).draggable(true);
				count ++;
			}
			// markerOption.
			aMap.addMarker(markerOption).setObject(locationPoint); // 设置此marker可以拖拽
			
		}
		
		if (latLngs != null && latLngs.size() > 1) {
			Builder builder = new LatLngBounds.Builder();
			for (LatLng latLng : latLngs) {
				builder.include(latLng);
			}
			LatLngBounds bounds = builder.build();
			aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
		} else if(locationPoints != null && locationPoints.size() == 1){
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationPoints.get(0).getLatitude(), locationPoints.get(0).getLongitude()), 16));
		}
		

	}

	private LocationPoint me;
	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(final Marker marker) {

		LocationPoint locationPoint = (LocationPoint) marker.getObject();
		if (lastMarker != null && !((LocationPoint)lastMarker.getObject()).isMe()) {
			// lastMarker.set
			//View view = getView(((LocationPoint)lastMarker.getObject()).getNumber() + "",R.drawable.icon_blue_location,locationPoint.getName());
			View view = getView(locationPoint);
			
		   // AnimationUtil.startShakeAlphaAnimation(getBaseContext(), view);
			lastMarker.setIcon(BitmapDescriptorFactory.fromView(view));
			// lastMarker.icon();
		}
		if (!locationPoint.isMe()) {
			lastMarker = marker;
			me=locationPoint;
			//marker.setIcon(BitmapDescriptorFactory.fromView(getView(locationPoint.getNumber() + "",R.drawable.icon_red_location,locationPoint.getName())));
			marker.setIcon(BitmapDescriptorFactory.fromView(getView(locationPoint)));
		} else {
			lastMarker = null;
		}
	
		//CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(locationPoint.getLatitude(),locationPoint.getLongitude()), 16, 0, 30)), null);
		CameraUpdate cameraUpdate =CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(locationPoint.getLatitude(),locationPoint.getLongitude()), 16, 0, 30));
		aMap.animateCamera(cameraUpdate, 1000, null);
		
		//aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationPoint.getLatitude(),locationPoint.getLongitude()), 14));

		putData(marker.getTitle(), locationPoint, marker);
		
		if(mapMarkListener != null) {
			mapMarkListener.onMapMark(marker);
		}
		
		return true;
	}

	//
	Marker lastMarker = null;

	/**
	 * 监听点击infowindow窗口事件回调
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		String object = (String) marker.getObject();
		if (marker.getPosition() != null) {
			Toast.makeText(this, object, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 监听拖动marker时事件回调
	 */
	@Override
	public void onMarkerDrag(Marker marker) {
		String curDes = marker.getTitle() + "拖动时当前位置:(lat,lng)\n(" + marker.getPosition().latitude + "," + marker.getPosition().longitude + ")";
	}

	/**
	 * 监听拖动marker结束事件回调
	 */
	@Override
	public void onMarkerDragEnd(Marker marker) {

	}

	/**
	 * 监听开始拖动marker事件回调
	 */
	@Override
	public void onMarkerDragStart(Marker marker) {
	}

	/**
	 * 监听amap地图加载成功事件回调
	 */
	@Override
	public void onMapLoaded() {
		// 设置地图边界
		drawMarkers(loadPointToMap());
//		if (latLngs != null && latLngs.size() > 1) {
//			Builder builder = new LatLngBounds.Builder();
//			for (LatLng latLng : latLngs) {
//				builder.include(latLng);
//			}
//			LatLngBounds bounds = builder.build();
//			aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
//		} else {
//			
//		}
	}

	/**
	 * 监听自定义infowindow窗口的infocontents事件回调
	 */
	@Override
	public View getInfoContents(Marker marker) {
		View infoContent = getLayoutInflater().inflate(R.layout.area_marker_open, null);
		render(marker, infoContent);
		return infoContent;
		
	}

	/**
	 * 监听自定义infowindow窗口的infowindow事件回调
	 */
	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = getLayoutInflater().inflate(R.layout.area_marker_open, null);

		render(marker, infoWindow);
		return infoWindow;
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		if (mPop != null) {
			mPop.dismiss();
			mPop = null;
		}
	}

	/**
	 * 自定义infowinfow窗口
	 */
	public void render(Marker marker, View view) {
//		String title = marker.getTitle();
//		TextView titleUi = ((TextView) view.findViewById(R.id.title));
//		if (title != null) {
//			SpannableString titleText = new SpannableString(title);
//			titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
//			titleUi.setTextSize(12);
//			titleUi.setText(titleText);
//
//		} else {
//			titleUi.setText("");
//		}
//		String snippet = marker.getSnippet();
//		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
//		if (snippet != null) {
//			SpannableString snippetText = new SpannableString(snippet);
//			snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, snippetText.length(), 0);
//			snippetUi.setTextSize(10);
//			snippetUi.setText(snippetText);
//		} else {
//			snippetUi.setText("");
//		}
	}



	public static Bitmap getBitmapFromResources(Activity act, int resId) {
		Resources res = act.getResources();
		return BitmapFactory.decodeResource(res, resId);
	}

	public static PopupWindow mPop;

	String lastName = null;

	String lastAddress = null;

	@SuppressLint("InlinedApi")
	public void putData(String name, LocationPoint locationPoint, Marker marker) {
		if (mPop != null && mPop.isShowing()) {
			mPop.dismiss();
			mPop = null;

			// return;
		}
				
		if (lastName != null && lastAddress != null) {
			if (lastName.equals(name) && lastAddress.equals(locationPoint.getAddress())) {
				if (!locationPoint.isMe())
				{
					//marker.setIcon(BitmapDescriptorFactory.fromView(getView(locationPoint.getNumber() + "",R.drawable.position,locationPoint.getName())));
					marker.setIcon(BitmapDescriptorFactory.fromView(getView(locationPoint)));
				}
					
				lastName = null;
				lastAddress = null;
				return;
			}
		}

		lastName = name;
		lastAddress = locationPoint.getAddress();

//		View convertView = LayoutInflater.from(this).inflate(R.layout.pop_map, null);
//		TextView nameTextView = (TextView) convertView.findViewById(R.id.name);
//		TextView addressTextView = (TextView) convertView.findViewById(R.id.address);
//		TextView meterTextView   = (TextView)convertView.findViewById(R.id.meter);
//		nameTextView.setText(name);
//		addressTextView.setText(locationPoint.getAddress());
//		try {
//		StringBuffer sb = new StringBuffer();
//		int meter = Integer.parseInt(locationPoint.getObj1());
//		int km = meter / 1000 ;
//		if(km > 0 ) {
//			sb.append(km);
//			int me = meter % 1000;
//			if(me > 0) {
//				if(me >= 100) {
//			      sb.append("." + me);
//				} else if(me >= 10) {
//					sb.append(".").append("0").append(me);
//				} else {
//					sb.append(".").append("0").append("0").append(me);
//				}
//			}
//			sb.append("公里");
//		} else {
//			sb.append(meter + "米");
//		}
//		meterTextView.setText("距离我" + sb.toString());
//		} catch (Exception e) {
//		}
//		mPop = new PopupWindow(convertView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false);
//		//mPop.setAnimationStyle(R.style.AnimBottom);
//		mPop.setOutsideTouchable(true);
//		mPop.setFocusable(false);
//		mPop.showAsDropDown(findViewById(R.id.map_show), 1, 0);
	//	mPop.showAtLocation(findViewById(R.id.map_show), /*Gravity.END |*/ Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if (mPop != null) {
			mPop.dismiss();
			mPop = null;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (mPop != null) {
			mPop.dismiss();
			mPop = null;
		}
		return true;
	}
	
	/**
	 * 把一个xml布局文件转化成view
	 */
	public View getView(/*String number,int resourceId,String title*/LocationPoint location) {
//		View view = getLayoutInflater().inflate(R.layout.marker, null);
//		TextView numberTV = (TextView) view.findViewById(R.id.number);
//		//TextView titleTV = (TextView) view.findViewById(R.id.title);
//		numberTV.setBackgroundResource(R.drawable.position);
//		//numberTV.setText(number);
//		numberTV.setText(title);
//		//titleTV.setText(title);
//		return view;
		View view = getLayoutInflater().inflate(R.layout.marker, null);
		LinearLayout areainfo=(LinearLayout)view.findViewById(R.id.areainfo);
		areainfo.setBackgroundResource(R.drawable.neighbour_info_window);
		TextView areaName = (TextView) view.findViewById(R.id.areaName);
		areaName.setText(location.getName());
		//TextView areadesc = (TextView) view.findViewById(R.id.areadesc);
//		if(LocationPoint.LOCAL_AREA==location.localType)
//		{
//			areadesc.setText(getString(R.string.clickinarea));	
//		}else
//		{
//			areadesc.setText(getString(R.string.clickinmerchant));
//		}
		
		return view;
	}

}