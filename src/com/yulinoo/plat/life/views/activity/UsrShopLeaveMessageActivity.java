package com.yulinoo.plat.life.views.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.GoodsListReq;
import com.yulinoo.plat.life.net.reqbean.ProductListAddReq;
import com.yulinoo.plat.life.net.resbean.ForumNoteResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.views.adapter.UsrShopLeaveMessageAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class UsrShopLeaveMessageActivity extends BaseActivity implements IXListViewListener,OnClickListener{

	private XListView mListView;
	private TextView leaveMessageTv;
	private Long leaveMessageProductSid=null;
	private UsrShopLeaveMessageAdapter adapter;
	private Merchant merchant;

	private boolean isEnd=false;
	private boolean isLoading=false;
	
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.usr_shop_leave_message_layout);
		mListView=(XListView) findViewById(R.id.leave_message_list_view);
		leaveMessageTv=(TextView) findViewById(R.id.leave_message_tv);
		leaveMessageTv.setOnClickListener(this);
		leaveMessageProductSid=getIntent().getLongExtra("leaveMessageProductSid", 0);
		merchant=(Merchant) getIntent().getSerializableExtra(Constant.ActivityExtra.MERCHANT);
	}

	@Override
	protected void initComponent() {
		adapter=new UsrShopLeaveMessageAdapter(this);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(adapter);
		mListView.autoRefresh();
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,
			View RightImaView, TextView rightText, TextView title,
			View titleLayout) {
		title.setText("商家留言");
	}
	int pageNo=0;
	//加载商品，参数表示为是示向上拉取数据
	private synchronized void loadGoods(final boolean isUp) {
		if(isUp)
		{
			isEnd=false;
			pageNo=0;
		}else{
			if(isEnd)
			{
				onLoad();
				return;
			}
			pageNo++;
		}
		if(isLoading)
		{
			return;
		}
		GoodsListReq merchantReq = new GoodsListReq();
		merchantReq.setProductSid(leaveMessageProductSid);
		merchantReq.setMerchantSid(merchant.getSid());
		merchantReq.setPageNo(pageNo);

		RequestBean<ForumNoteResponse> requestBean = new RequestBean<ForumNoteResponse>();
		requestBean.setRequestBody(merchantReq);
		requestBean.setResponseBody(ForumNoteResponse.class);
		requestBean.setURL(Constant.Requrl.getMerchantGoodsList());
		isLoading=true;
		MeMessageService.instance().requestServer(requestBean, new UICallback<ForumNoteResponse>() {
			@Override
			public void onSuccess(ForumNoteResponse respose) {
				try {
					loadDataDone(respose,isUp);
				} catch (Exception e) {
					e.printStackTrace();
				}
				isLoading=false;
			}

			@Override
			public void onError(String message) {
				onLoad();
				//activity.showToast(message);
				isLoading=false;
			}

			@Override
			public void onOffline(String message) {
				onLoad();
				//activity.showToast(message);
				isLoading=false;
			}
		});
	}

	private synchronized void loadDataDone(ForumNoteResponse respose,final boolean isUp)
	{		
		onLoad();
		List<ForumNote> listGoods=respose.getList();
		if(listGoods!=null&&listGoods.size()>0)
		{
			if(isUp)
			{//说明是第一次加载，则在前面插入一条空记录，用于显示头部
				adapter.clear();
				//listGoods.add(0, null);
			}
			adapter.load(listGoods);
		}else
		{//没有新的数据
			isEnd=true;
			if(isUp)
			{
				adapter.clear();
				//listGoods.add(null);//头部
			}
			//listGoods.add(null);//头部
			adapter.load(listGoods);
		}
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getTime());
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
	}

	@Override
	public void onRefresh() {
		loadGoods(true);
	}

	@Override
	public void onLoadMore() {
		loadGoods(false);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.leave_message_tv:
			ProductListAddReq plareq=new ProductListAddReq();
			AreaInfo ai=AppContext.nearByArea();
			plareq.setAlongAreaSid(ai.getSid());
			plareq.setAlongMerchantSid(merchant.getSid());
			Account acc=AppContext.currentAccount();
			plareq.setMevalue(acc.getMevalue());
			plareq.setProductSid(leaveMessageProductSid);
			startActivity(new Intent(mContext, PublishGoodsActivity.class)
			.putExtra(PublishGoodsActivity.PUBLIC_TAB, plareq)
			.putExtra(Constant.ActivityExtra.PUBLISH_GOODS_TYPE, Constant.PUBLISH_GOODS_TYPE.MERCHANT_ZONE_PUBLISH));
			break;
		}
		
	}

}
