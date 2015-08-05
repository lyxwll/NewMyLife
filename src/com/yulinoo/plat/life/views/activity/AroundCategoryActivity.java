package com.yulinoo.plat.life.views.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Advertise;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.AdvertiseReq;
import com.yulinoo.plat.life.net.resbean.AdvertiseListResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.views.adapter.AroundCategoryAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class AroundCategoryActivity extends BaseActivity {

	private XListView mListView;
	private AroundCategoryAdapter adapter;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.around_category_layout);
		mListView = (XListView) findViewById(R.id.around_Xlist_view);
		adapter = new AroundCategoryAdapter(this, mListView);
		List<Advertise> advertises = new ArrayList<Advertise>();
		advertises.add(null);
		adapter.load(advertises);
		mListView.setAdapter(adapter);
	}

	@Override
	protected void initComponent() {
		getAdvertiseInfro();
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,
			View RightImaView, TextView rightText, TextView title,
			View titleLayout) {
		title.setText("周边生活");
	}

	private void getAdvertiseInfro() {
		if (MeMessageService.instance().hasInternet(this)) {// 有网的话才请求
			if (AppContext.currentAreaInfo() != null) {// 当前有小区信息
				AreaInfo areaInfo = AppContext.currentAreaInfo();
				AdvertiseReq advertiseReq = new AdvertiseReq();
				advertiseReq.setAreaSid(areaInfo.getSid());
				advertiseReq.setPosition(4);
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
									// int
									// height=mListView.getLayoutParams().height;
									// int[]
									// sizes=SizeUtil.getAdvertiseSizeNoMargin(AroundCategoryActivity.this);
									// int
									// mHeight=advertises.size()*(sizes[1]+(int)AroundCategoryActivity.this.getResources().getDimension(R.dimen.main_advertise_padding_top));
									// mListView.getLayoutParams().height=height+mHeight;
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

}
