package com.yulinoo.plat.life.views.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.LocationPoint;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.AreaInfoDetailReq;
import com.yulinoo.plat.life.net.resbean.AreaInfoDetailResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.life.views.activity.MarkPointToMapActivity.MapMarkListener;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class MerchantMapActivity extends MarkPointToMapActivity implements MapMarkListener,OnRouteSearchListener {
	private Merchant merchant;
	private MerchantMapActivity activity;
	//private AreaInfo ai;

	//商家的坐标
	private double merchantLat;
	private double merchantLng;
	private LatLonPoint merchantLatLonPoint;
	//小区的坐标
	private double areaLat;
	private double areaLng;
	private LatLonPoint areaLatLonPoint;

	//步行路径规划图
	private RouteSearch walkRouteSearch;
	private WalkRouteResult walkRouteResult;

	@Override
	public void onClick(View v) {

	}

	@Override
	protected List<LocationPoint> loadPointToMap() {
		LocationPoint locationPoint = new LocationPoint();
		locationPoint.setAddress(merchant.getAreaName());
		locationPoint.setLatitude(merchant.getLatItude());
		locationPoint.setLongitude(merchant.getLongItude());
		locationPoint.setName(merchant.getMerchantName());
		locationPoint.localType=LocationPoint.LOCAL_MERCHANT;
		List<LocationPoint> locationPoints = new ArrayList<LocationPoint>();
		locationPoints.add(locationPoint);
		return locationPoints;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.merchant_map);
		activity=this;
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
		init();
		merchant = (Merchant) this.getIntent().getSerializableExtra(Constant.ActivityExtra.MERCHANT);
		//商家的坐标
		merchantLat=merchant.getLatItude();
		merchantLng=merchant.getLongItude();
		merchantLatLonPoint=new LatLonPoint(merchantLat, merchantLng);
		//		merchantLatLonPoint.setLatitude(merchantLat);
		//		merchantLatLonPoint.setLongitude(merchantLng);

		if (aMap != null) {
			aMap.setOnMarkerClickListener(null);
		}
	}

	@Override
	public void onMapLoaded() {
		super.onMapLoaded();
		//以小区中心为圆心画圆,并标记小区mark
		setCircle();
	}

	private void setWalkRoute(){
		final RouteSearch.FromAndTo fromAndTo=new RouteSearch.FromAndTo(areaLatLonPoint, merchantLatLonPoint);
		walkRouteSearch=new RouteSearch(this);
		walkRouteSearch.setRouteSearchListener(this);
		WalkRouteQuery query=new WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
		walkRouteSearch.calculateWalkRouteAsyn(query);
	}

	private void setCircle() {
		if (merchant != null) {
			AreaInfo currentAi=AppContext.currentAreaInfo();
			if(currentAi!=null)
			{
				areaLat = currentAi.getLatItude();
				areaLng = currentAi.getLongItude();
				LatLng latLng = new LatLng(areaLat, areaLng);
				areaLatLonPoint=new LatLonPoint(areaLat, areaLng);
				//							areaLatLonPoint.setLatitude(areaLat);
				//							areaLatLonPoint.setLongitude(areaLng);

				// 以小区为中心画圈
				aMap.addCircle(new CircleOptions()
				.center(latLng)
				.radius(500)
				.strokeColor(getResources().getColor(R.color.myneighbour_stroke_color))
				.fillColor(getResources().getColor(R.color.myneighbour_fill_color))
				.strokeWidth(3));
				// 标记小区的mark
				//注意MarkerOptions中的.title("")
				//MarkerOptions markerOption = new MarkerOptions()
				//.position(latLng)
				//.icon(BitmapDescriptorFactory.fromView(getView(ai.getAreaName())))
				//.title("").draggable(false);
				//markerOption.anchor(0.5f, 0.5f);
				//aMap.addMarker(markerOption);

				MarkerOptions markerOption = new MarkerOptions()
				.position(latLng)
				.icon(BitmapDescriptorFactory.fromView(getView(currentAi.getAreaName())))
				.draggable(true);
				//aMap.addMarker(markerOption);

				//设置步行路径规划图
				setWalkRoute();
			}
			
//			long areaSid = merchant.getAlongAreaSid();
//			AreaInfoDetailReq areaDetailReq = new AreaInfoDetailReq();
//			areaDetailReq.setAreaSid(areaSid);
//			RequestBean<AreaInfoDetailResponse> requestBean = new RequestBean<AreaInfoDetailResponse>();
//			requestBean.setRequestBody(areaDetailReq);
//			requestBean.setResponseBody(AreaInfoDetailResponse.class);
//			requestBean.setURL(Constant.Requrl.getAreaDetail());
//			// ProgressUtil.showProgress(activity, "处理中...");
//			activity.requestServer(requestBean,
//					new UICallback<AreaInfoDetailResponse>() {
//				@Override
//				public void onSuccess(AreaInfoDetailResponse respose) {
//					try {
//						// ProgressUtil.dissmissProgress();
//						if (respose.isSuccess()) {
//							ai = respose.getArea();
//							areaLat = ai.getLatItude();
//							areaLng = ai.getLongItude();
//							LatLng latLng = new LatLng(areaLat, areaLng);
//							areaLatLonPoint=new LatLonPoint(areaLat, areaLng);
//							//							areaLatLonPoint.setLatitude(areaLat);
//							//							areaLatLonPoint.setLongitude(areaLng);
//
//							// 以小区为中心画圈
//							aMap.addCircle(new CircleOptions()
//							.center(latLng)
//							.radius(500)
//							.strokeColor(getResources().getColor(R.color.myneighbour_stroke_color))
//							.fillColor(getResources().getColor(R.color.myneighbour_fill_color))
//							.strokeWidth(3));
//							// 标记小区的mark
//							//注意MarkerOptions中的.title("")
//							//MarkerOptions markerOption = new MarkerOptions()
//							//.position(latLng)
//							//.icon(BitmapDescriptorFactory.fromView(getView(ai.getAreaName())))
//							//.title("").draggable(false);
//							//markerOption.anchor(0.5f, 0.5f);
//							//aMap.addMarker(markerOption);
//
//							MarkerOptions markerOption = new MarkerOptions()
//							.position(latLng)
//							.icon(BitmapDescriptorFactory.fromView(getView(ai.getAreaName())))
//							.draggable(true);
//							//aMap.addMarker(markerOption);
//
//							//设置步行路径规划图
//							setWalkRoute();
//						} else {
//							activity.showToast(respose.getMsg());
//						}
//					} catch (Exception e) {
//					}
//				}
//
//				@Override
//				public void onError(String message) {
//					ProgressUtil.dissmissProgress();
//					activity.showToast(message);
//				}
//
//				@Override
//				public void onOffline(String message) {
//					ProgressUtil.dissmissProgress();
//					activity.showToast(message);
//				}
//			});
		}
	}
//	private void setCircle() {
//		if (merchant != null) {
//			long areaSid = merchant.getAlongAreaSid();
//			AreaInfoDetailReq areaDetailReq = new AreaInfoDetailReq();
//			areaDetailReq.setAreaSid(areaSid);
//			RequestBean<AreaInfoDetailResponse> requestBean = new RequestBean<AreaInfoDetailResponse>();
//			requestBean.setRequestBody(areaDetailReq);
//			requestBean.setResponseBody(AreaInfoDetailResponse.class);
//			requestBean.setURL(Constant.Requrl.getAreaDetail());
//			// ProgressUtil.showProgress(activity, "处理中...");
//			activity.requestServer(requestBean,
//					new UICallback<AreaInfoDetailResponse>() {
//				@Override
//				public void onSuccess(AreaInfoDetailResponse respose) {
//					try {
//						// ProgressUtil.dissmissProgress();
//						if (respose.isSuccess()) {
//							ai = respose.getArea();
//							areaLat = ai.getLatItude();
//							areaLng = ai.getLongItude();
//							LatLng latLng = new LatLng(areaLat, areaLng);
//							areaLatLonPoint=new LatLonPoint(areaLat, areaLng);
//							//							areaLatLonPoint.setLatitude(areaLat);
//							//							areaLatLonPoint.setLongitude(areaLng);
//							
//							// 以小区为中心画圈
//							aMap.addCircle(new CircleOptions()
//							.center(latLng)
//							.radius(500)
//							.strokeColor(getResources().getColor(R.color.myneighbour_stroke_color))
//							.fillColor(getResources().getColor(R.color.myneighbour_fill_color))
//							.strokeWidth(3));
//							// 标记小区的mark
//							//注意MarkerOptions中的.title("")
//							//MarkerOptions markerOption = new MarkerOptions()
//							//.position(latLng)
//							//.icon(BitmapDescriptorFactory.fromView(getView(ai.getAreaName())))
//							//.title("").draggable(false);
//							//markerOption.anchor(0.5f, 0.5f);
//							//aMap.addMarker(markerOption);
//							
//							MarkerOptions markerOption = new MarkerOptions()
//							.position(latLng)
//							.icon(BitmapDescriptorFactory.fromView(getView(ai.getAreaName())))
//							.draggable(true);
//							//aMap.addMarker(markerOption);
//							
//							//设置步行路径规划图
//							setWalkRoute();
//						} else {
//							activity.showToast(respose.getMsg());
//						}
//					} catch (Exception e) {
//					}
//				}
//				
//				@Override
//				public void onError(String message) {
//					ProgressUtil.dissmissProgress();
//					activity.showToast(message);
//				}
//				
//				@Override
//				public void onOffline(String message) {
//					ProgressUtil.dissmissProgress();
//					activity.showToast(message);
//				}
//			});
//		}
//	}

	/**
	 * 把一个xml布局文件转化成view
	 */
	private View getView(String title) {
		if (NullUtil.isStrNotNull(title)) {
			View view = LayoutInflater.from(mContext).inflate(R.layout.area_marker_other, null);
			TextView text_title = (TextView) view.findViewById(R.id.areaName);
			text_title.setText(title);
			return view;
		} else {
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.area_marker_me, null);
			return view;
		}
	}

	@Override
	protected void initComponent() {

	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {
		title.setText(merchant.getMerchantName());
		rightText.setVisibility(View.INVISIBLE);
		rightImg.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onMapMark(Marker marker) {
		LocationPoint merchant = ((LocationPoint)marker.getObject());
		showToast(merchant.getAddress());
	}

	@Override
	public void onMapClick(LatLng point) {
		//		showToast("point="+point);
		//		aMap.clear();
		//		MarkerOptions markerOption = new MarkerOptions().position(point).title("XXX").snippet("KKK").icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)).draggable(false);
		//		// markerOption.
		//		aMap.addMarker(markerOption);

	}

	@Override
	public void onBusRouteSearched(BusRouteResult result, int rCode) {

	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult arg0, int arg1) {

	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
		LatLng latLng;
		PolylineOptions options;
		List<LatLng> latLngs = new ArrayList<LatLng>();
		if (rCode==0) {
			if (result!=null &&result.getPaths()!=null && result.getPaths().size()>0) {
				WalkPath walkPath = result.getPaths().get(0);
				List<WalkStep> lstSteps=walkPath.getSteps();
				for(WalkStep walkStep:lstSteps)
				{
					List<LatLonPoint> lstLines=walkStep.getPolyline();
					for(LatLonPoint llp:lstLines)
					{
						latLng=new LatLng(llp.getLatitude(), llp.getLongitude());
						latLngs.add(latLng);
					}
				}
				options=new PolylineOptions();
				options.width(7.0f);
				options.color(getResources().getColor(R.color.my_yellow));
				options.addAll(latLngs);
				aMap.addPolyline(options);
				

				//				//aMap.clear();//清理之前的图标
				//				WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
				//						this, aMap, walkPath,result.getStartPos(),
				//						result.getTargetPos());
				//				walkRouteOverlay.removeFromMap();
				//				walkRouteOverlay.addToMap();

				//walkRouteOverlay.zoomToSpan();
			}else {
				Toast.makeText(this, "线路查找失败!", Toast.LENGTH_SHORT).show();
			}
		}else {
			Toast.makeText(this, "线路查找失败!", Toast.LENGTH_SHORT).show();
		}
	}
}
