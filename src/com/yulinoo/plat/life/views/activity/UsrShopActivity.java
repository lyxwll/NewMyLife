package com.yulinoo.plat.life.views.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.bean.LocationPoint;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.bean.ProductInfo;
import com.yulinoo.plat.life.bean.TagListBean;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.MerchantDetailReq;
import com.yulinoo.plat.life.net.reqbean.ProductListAddReq;
import com.yulinoo.plat.life.net.reqbean.ProductReq;
import com.yulinoo.plat.life.net.resbean.MerchantDetailResponse;
import com.yulinoo.plat.life.net.resbean.ProductResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.MeTab;
import com.yulinoo.plat.life.ui.widget.Tab;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.views.adapter.UsrShopAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;
//用户的商铺，包含自己
public class UsrShopActivity extends EditPhotoActivity implements OnClickListener{

	private Merchant merchant;//商家信息
	private ForumNote forumNote;
	private MyApplication myapp;
	private TextView product_add;//发布
	private MeTab meTab;
	private Tab nowTab;
	private int praiseNumber=0;
	private int commentNumber=0;
	//商家的商品列表页面
	//private MerchantGoodsAdaper merchantGoodsAdapter;
	//商家的留言页面，留言即有其他用户在该商家的某个产品下发贴
	//private MerchantLeaveMessageAdaper merchantLeaveMessageAdaper;
	//当前Adapter
	//private YuLinooLoadAdapter<ForumNote> nowAdapter;

	private View photoPosition;//用于让拍照依靠的位置

	//	private MeNormalListView mListView;
	//	private SwipeRefreshLayout mSwipeLayout;
	private XListView mListView;
	private UsrShopAdapter usrShopAdapter;

	private MyBroadcastReceiver goodsDeletedBroadcastReceiver;
	private MyBroadcastReceiver goodsAddedBroadcastReceiver;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.usr_shop);
		myapp=(MyApplication)getApplicationContext();
		merchant = (Merchant) this.getIntent().getSerializableExtra(Constant.ActivityExtra.MERCHANT);
		forumNote=(ForumNote) this.getIntent().getSerializableExtra("usr_shop_forumNote");
		
		try {
			IntentFilter goodsDeletedIntentFilter = new IntentFilter();
			goodsDeletedIntentFilter.addAction(Constant.BroadType.GOODS_DELETED);
			goodsDeletedBroadcastReceiver=new MyBroadcastReceiver();
			this.registerReceiver(goodsDeletedBroadcastReceiver, goodsDeletedIntentFilter);
			IntentFilter goodsAddedIntentFilter = new IntentFilter();
			goodsAddedIntentFilter.addAction(Constant.BroadType.GOODS_ADDED);
			goodsAddedBroadcastReceiver=new MyBroadcastReceiver();
			this.registerReceiver(goodsAddedBroadcastReceiver, goodsAddedIntentFilter);
		} catch (Exception e) {}
	}

	@SuppressLint({ "InlinedApi", "ResourceAsColor" })
	@Override
	protected void initComponent() {
		//		mListView = (MeNormalListView) findViewById(R.id.list);
		//		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		//		MeUtil.setSwipeLayoutColor(mSwipeLayout);
		//		mSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
		//		mSwipeLayout.setLoadNoFull(false);
		mListView = (XListView) findViewById(R.id.list_view);

		loadMerchantDetail();

		product_add =  (TextView)findViewById(R.id.product_add);
		product_add.setOnClickListener(this);

	}
	private TextView title_tv;
	@Override
	protected void initHead(BackWidget back_btn,
			TextView rightImg,View RightImaView , TextView rightText, TextView title,
			View titleLayout) {
		title_tv=title;
		//title.setText("商铺");
	}

	//加载商家详情
	private void loadMerchantDetail() {
		MerchantDetailReq merchantDetailReq=new MerchantDetailReq();
		merchantDetailReq.setMerchantSid(merchant.getSid());
		RequestBean<MerchantDetailResponse> requestBean = new RequestBean<MerchantDetailResponse>();
		requestBean.setRequestBody(merchantDetailReq);
		requestBean.setResponseBody(MerchantDetailResponse.class);
		requestBean.setURL(Constant.Requrl.getMerchantDetail());
		requestServer(requestBean, new UICallback<MerchantDetailResponse>() {
			@Override
			public void onSuccess(MerchantDetailResponse respose) {
				try {
					if(respose.isSuccess())
					{
						merchant=respose.getMerchant();
						title_tv.setText(merchant.getMerchantName());
						loadProducts();			
					}else
					{
						showToast(respose.getMsg());
					}
				} catch (Exception e) {
					showToast(e.getMessage());
				}

			}

			@Override
			public void onError(String message) {
				showToast(message);
			}

			@Override
			public void onOffline(String message) {
				showToast(message);
			}
		});
	}

	private void loadProducts()
	{
		ProductReq merchantProductReq = new ProductReq();
		//merchantProductReq.setMevalue(AppContext.currentAccount(this).getMevalue());
		merchantProductReq.setMerchantSid(merchant.getSid());
		RequestBean<ProductResponse> requestBean = new RequestBean<ProductResponse>();
		requestBean.setRequestBody(merchantProductReq);
		requestBean.setResponseBody(ProductResponse.class);
		requestBean.setURL(Constant.Requrl.getMerchantProductsList());
		MeMessageService.instance().requestServer(requestBean, new UICallback<ProductResponse>() {
			@Override
			public void onSuccess(ProductResponse respose) {
				if (respose == null ||respose.getList()==null)
					return;
				try {
					List<ProductInfo> hotFoods = respose.getList();
					merchant.setProducts(hotFoods);//设置商家产品
					usrShopAdapter=new UsrShopAdapter(UsrShopActivity.this, merchant,mListView,forumNote);

					List<ForumNote> list=new ArrayList<ForumNote>();
					list.add(null);//占用头部
					usrShopAdapter.load(list);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String message) {
				showToast(message);
			}

			@Override
			public void onOffline(String message) {
				showToast(message);
			}
		});
	}

	public void showAddProduct(boolean isShow,Tab tab)
	{
		nowTab=tab;
		if(isShow)
		{
			product_add.setVisibility(View.VISIBLE);
		}else
		{
			product_add.setVisibility(View.GONE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == CategorySelectActivity.selectTagOk) {//选择了分类结果
			TagListBean productListBean = (TagListBean) data.getSerializableExtra(CategorySelectActivity.tagListBean);
			if(usrShopAdapter!=null)
			{
				MerchantInfroActivity.instance().categorySelected(productListBean.getTags());
			}
		} else if (resultCode == ZoneSelectActivity.AREA_SELECTED) {//选择了小区
			if(usrShopAdapter!=null)
			{
				MerchantInfroActivity.instance().areaSelected((AreaInfo) data.getSerializableExtra("AreaInfo"));
			}
		}else if(resultCode==MerchantChoiceMapActivity.AMAP_SELECTED)
		{//选择了店铺地图位置
			if(usrShopAdapter!=null)
			{
				MerchantInfroActivity.instance().loactionSelected((LocationPoint)data.getSerializableExtra(MerchantChoiceMapActivity.LOCAP));
			}
		}
	}

	@Override
	protected void onDestroy() {
		try {
			this.unregisterReceiver(goodsDeletedBroadcastReceiver);
			this.unregisterReceiver(goodsAddedBroadcastReceiver);
		} catch (Exception e) {}

		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.product_add:
		{
			ProductListAddReq plareq=new ProductListAddReq();
			AreaInfo ai=AppContext.nearByArea();
			plareq.setAlongAreaSid(ai.getSid());
			plareq.setAlongMerchantSid(merchant.getSid());
			Account acc=AppContext.currentAccount();
			plareq.setMevalue(acc.getMevalue());
			plareq.setProductSid(nowTab.getProductSid());
			startActivity(new Intent(mContext, PublishGoodsActivity.class)
			.putExtra(PublishGoodsActivity.PUBLIC_TAB, plareq)
			.putExtra(Constant.ActivityExtra.PUBLISH_GOODS_TYPE, Constant.PUBLISH_GOODS_TYPE.MERCHANT_ZONE_PUBLISH));
			break;
		}
		case R.id.usr_header:
		{
			editPhoto(photoPosition, 100, 100,Constant.Requrl.getModifyMerchantAvarta());
			break;
		}
		}
	}

	@Override
	public void photoUploadDone(Bitmap newPhoto, String afterCompressUrl,
			String netUrlPath) {
		if(usrShopAdapter!=null)
		{
			usrShopAdapter.changeHeaderPicture(newPhoto);
		}
	}

	private class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(Constant.BroadType.GOODS_DELETED)
					||action.equals(Constant.BroadType.GOODS_ADDED)) {
				//				if(mSwipeLayout!=null)
				//				{
				//					mSwipeLayout.startRefresh();
				//				}
				if(mListView!=null)
				{
					mListView.autoRefresh();
				}
			}
		}
	}
}
