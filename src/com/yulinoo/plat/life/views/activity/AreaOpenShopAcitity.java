package com.yulinoo.plat.life.views.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.CityInfo;
import com.yulinoo.plat.life.bean.DistrictInfo;
import com.yulinoo.plat.life.bean.LocationPoint;
import com.yulinoo.plat.life.bean.Tag;
import com.yulinoo.plat.life.bean.TagListBean;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.ShopModifyReq;
import com.yulinoo.plat.life.net.resbean.ShopOpenResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.SThumbnail;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.melife.R;

import config.AppContext;
//小区开店
public class AreaOpenShopAcitity extends ImageActivtity implements OnClickListener {
	SThumbnail sThumbnail;

	private TextView shop_category;

	private EditText shop_name;

	private EditText description;

	//private EditText tel;

	private EditText mobile;

	private List<Tag> productInfos;
	private TextView address;
	private TextView amap;
	private AreaInfo areaInfo;
	private CityInfo mSelectCity;
	private DistrictInfo mDistrictInfo;
	private LocationPoint locationPoint;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.area_open_shop);
	}

	@Override
	protected void initComponent() {
		address = (TextView) findViewById(R.id.address);
		amap = (TextView) findViewById(R.id.amap);
		description = (EditText) findViewById(R.id.description);
		shop_name = (EditText) findViewById(R.id.shop_name);
		//tel = (EditText) findViewById(R.id.tel);
		mobile = (EditText) findViewById(R.id.mobile);
		//tel = (EditText) findViewById(R.id.tel);
		sThumbnail = (SThumbnail) findViewById(R.id.sThumbnail);
		shop_category = (TextView) findViewById(R.id.shop_category);
		shop_category.setOnClickListener(this);

		TextView submit = (TextView) findViewById(R.id.submit);
		submit.setOnClickListener(this);
		address.setOnClickListener(this);
		amap.setOnClickListener(this);
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {
		rightText.setVisibility(View.INVISIBLE);
		rightImg.setVisibility(View.INVISIBLE);
		title.setText("小区开店");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shop_category:
			startActivityForResult(new Intent(mContext, CategorySelectActivity.class), CategorySelectActivity.selectTagOk);
			break;
		case R.id.submit:
			submit();
			break;
		case R.id.address:
			//选择开店所在的小区
			startActivityForResult(new Intent(mContext, NewZoneSelectActivity.class).putExtra(ZoneSelectActivity.isOpenShop, true), ZoneSelectActivity.AREA_SELECTED);
			break;
		case R.id.amap:
		{//小区地图
			if(areaInfo==null)
			{//
				showToast("请先选择您的小区");
				return;
			}
			startActivityForResult(new Intent(mContext, MerchantChoiceMapActivity.class).putExtra(MerchantChoiceMapActivity.area, areaInfo), MerchantChoiceMapActivity.AMAP_SELECTED);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == CategorySelectActivity.selectTagOk) {//选择了分类结果
			TagListBean productListBean = (TagListBean) data.getSerializableExtra(CategorySelectActivity.tagListBean);
			productInfos = productListBean.getTags();

			StringBuffer stringBuffer = new StringBuffer();
			if (productInfos != null) {
				for (Tag p : productInfos) {
					if (stringBuffer.length() == 0) {
						stringBuffer.append(p.getTagName());
					} else {
						stringBuffer.append(",").append(p.getTagName());
					}
				}
			}
			shop_category.setText(stringBuffer.toString());
		} else if (resultCode == NewZoneSelectActivity.AREA_SELECTED) {//选择了小区
			try {
				areaInfo = (AreaInfo) data.getSerializableExtra("AreaInfo");
				//mSelectCity = (CityInfo) data.getSerializableExtra("mSelectCity");
				//mDistrictInfo = (DistrictInfo) data.getSerializableExtra("mDistrictInfo");
				address.setText(/*mSelectCity.getCityName() + "," + mDistrictInfo.getDistrictName() + "," + */areaInfo.getAreaName());
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}

		}else if(resultCode==MerchantChoiceMapActivity.AMAP_SELECTED)
		{//选择了店铺地图位置
			try {
				amap.setText("已选择地图位置");
				locationPoint=(LocationPoint)data.getSerializableExtra(MerchantChoiceMapActivity.LOCAP);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}



	private void submit() {
//		小区ID	areaSid
//		区域ID	area
//		城市ID	cityId

		if(areaInfo == null || mDistrictInfo == null || mSelectCity == null) {
			showToast("请选择小区");
			return;
		}
		if(locationPoint==null)
		{
			showToast("请选择店铺地图位置");
			return;
		}
		

		
		ShopModifyReq shopAddReq = new ShopModifyReq();
		shopAddReq.setMevalue(AppContext.currentAccount().getMevalue());
		shopAddReq.setMerchantName(getViewString(shop_name));
		shopAddReq.setMerchantDesc(getViewString(description));
		shopAddReq.setMerchantTelphone(getViewString(mobile));
		shopAddReq.setAlongAreaSid(areaInfo.getSid());
		
		if (NullUtil.isStrNull(getViewString(shop_name))) {
			showToast("店铺名称不能为空");
			return;
		}
		
		if(locationPoint!=null)
		{
			shopAddReq.setLatItude(locationPoint.getLatitude());
			shopAddReq.setLongItude(locationPoint.getLongitude());
		}
		
		if (productInfos != null&&productInfos.size()>0) {
			StringBuffer scope = new StringBuffer();
			for (Tag productInfo : productInfos) {
				if (scope.length() == 0) {
					scope.append(productInfo.getAlongCategorySid() + "-" + productInfo.getSid());
				} else {
					scope.append(",").append(productInfo.getAlongCategorySid() + "-" + productInfo.getSid());
				}
			}
			shopAddReq.setScope(scope.toString());
		}else
		{
			showToast("请选择经营类别");
			return;
		}
		
		RequestBean<ShopOpenResponse> requestBean = new RequestBean<ShopOpenResponse>();
		requestBean.setRequestBody(shopAddReq);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(ShopOpenResponse.class);
		requestBean.setURL(Constant.Requrl.getOpenShop());
		ProgressUtil.showProgress(mContext, "正在提交数据...");
		requestServer(requestBean, new UICallback<ShopOpenResponse>() {

			@Override
			public void onSuccess(ShopOpenResponse respose) {
				try {
					ProgressUtil.dissmissProgress();
					if (respose.isSuccess()) {
						showToast("小区开店成功");
						Account account=AppContext.currentAccount();
						account.setAccType(Constant.ACCTYPE.ACCTYPE_NORMAL_MERCHANT);
						account.save();
						
						Intent intent = new Intent();
			            intent.setAction(Constant.BroadType.AREA_OPEN_SHOP);  
						mContext.sendBroadcast(intent);
						
//						if(MeLifeMainActivity.instance().myZoneFragment.myZoneWidget!=null)
//						{
//							MeLifeMainActivity.instance().myZoneFragment.myZoneWidget.setOpenShop();
//						}
						finish();
					} else {
						showToast("小区开店失败:" + respose.getMsg());
					}
				} catch (Exception e) {
					showToast("小区开店失败:" + e.getMessage());
				}
				
				
			}
			

			@Override
			public void onError(String message) {
				ProgressUtil.dissmissProgress();
				showToast(message); 
			}

			@Override
			public void onOffline(String message) {
				ProgressUtil.dissmissProgress();
				showToast(message); 
			}
		});
	}
}
