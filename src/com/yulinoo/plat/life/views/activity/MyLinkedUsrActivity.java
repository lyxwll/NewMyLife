package com.yulinoo.plat.life.views.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.GetFansReq;
import com.yulinoo.plat.life.net.reqbean.GuessYourLoveReq;
import com.yulinoo.plat.life.net.reqbean.UsrGroupReq;
import com.yulinoo.plat.life.net.resbean.MerchantResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.MeNormalListView;
import com.yulinoo.plat.life.ui.widget.MyListView.OnRefreshListener;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.views.adapter.MyLinkedUsrAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;
public class MyLinkedUsrActivity extends BaseActivity implements IXListViewListener{
	private int pageNo=0;
	private Merchant merchant;
	private String showTitle;
	private int loadType;//加载的数据类型
	private MyLinkedUsrAdapter myConcernAdapter;
	//	private MeNormalListView mListView;
	//	private SwipeRefreshLayout mSwipeLayout;
	private XListView mListView;

	private boolean isme;
	private boolean isEnd=false;
	
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.my_fans);
		merchant = (Merchant) this.getIntent().getSerializableExtra(Constant.ActivityExtra.MERCHANT);
		showTitle = (String) this.getIntent().getSerializableExtra(Constant.ActivityExtra.LINKEDTITLE);
		loadType = (Integer) this.getIntent().getSerializableExtra(Constant.ActivityExtra.LINKED_LOAD_TYPE);
	}

	@SuppressLint({ "InlinedApi", "ResourceAsColor" })
	@Override
	protected void initComponent() {
		myConcernAdapter = new MyLinkedUsrAdapter(this);
		//		mListView = (MeNormalListView) findViewById(R.id.list);
		//		mListView.setAdapter(myConcernAdapter);
		//		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		//		mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
		//			@Override
		//			public void onRefresh() {
		//				if(Constant.ConcernLoadType.LOAD_MYFRIEND==loadType)
		//				{//加载我的邻友
		//					loadMyFriend();
		//				}else if(Constant.ConcernLoadType.LOAD_FAVORITE==loadType)
		//				{//加载店铺收藏
		//					loadMyFavorite();
		//				}else if(Constant.ConcernLoadType.LOAD_SERVICE==loadType)
		//				{//加载服务号
		//					loadService();
		//				}else if(Constant.ConcernLoadType.LOAD_USER_GROUP==loadType)
		//				{//加载用户组的数据
		//					loadUserGroup(true);
		//				}else if(Constant.ConcernLoadType.LOAD_GUESS_LOVE==loadType)
		//				{//加载猜你喜欢
		//					loadGuessLove(true);
		//				}else if(Constant.ConcernLoadType.LOAD_FANS==loadType)
		//				{
		//					loadMyFans(true);
		//				}
		//			}
		//		});
		//		mSwipeLayout.setOnLoadListener(new OnLoadListener() {
		//			@Override
		//			public void onLoad() {
		//				if(Constant.ConcernLoadType.LOAD_MYFRIEND==loadType
		//						||Constant.ConcernLoadType.LOAD_GUESS_LOVE==loadType
		//						||Constant.ConcernLoadType.LOAD_USER_GROUP==loadType
		//						||Constant.ConcernLoadType.LOAD_FAVORITE==loadType)
		//				{//加载我的邻友,猜你喜欢,店铺收藏,都是直接读取缓存
		//					mSwipeLayout.setLoading(false);
		//				}else if(Constant.ConcernLoadType.LOAD_FANS==loadType)
		//				{
		//					loadMyFans(false);
		//				}
		//			}
		//		});
		//		MeUtil.setSwipeLayoutColor(mSwipeLayout);
		//		mSwipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
		//		mSwipeLayout.startRefresh();
		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(myConcernAdapter);
		mListView.autoRefresh();
	}



	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {
		title.setText(showTitle);
	}
	//加载我的邻友
	private void loadMyFriend()
	{
		List<Merchant> listFriends=new ArrayList<Merchant>();
		List<Merchant> listConcerns=AppContext.currentFocusMerchant();
		for(Merchant merchant:listConcerns)
		{
			if(merchant.getType()==Constant.SUBTYPE.SUBTYPE_USR)
			{
				listFriends.add(merchant);
			}
		}
		loadDataDone(listFriends,true);
	}
	//加载店铺收藏
	private void loadMyFavorite()
	{
		List<Merchant> listFriends=new ArrayList<Merchant>();
		List<Merchant> listConcerns=AppContext.currentFocusMerchant();
		for(Merchant merchant:listConcerns)
		{
			if(merchant.getType()==Constant.SUBTYPE.SUBTYPE_MERCHANT)
			{
				listFriends.add(merchant);
			}
		}
		loadDataDone(listFriends,true);
	}
	
	//加载服务号
	private void loadService()
	{
		List<Merchant> listFriends=new ArrayList<Merchant>();
		List<Merchant> listConcerns=AppContext.currentFocusMerchant();
		for(Merchant merchant:listConcerns)
		{
			if(merchant.getType()==Constant.SUBTYPE.SUBTYPE_SERVICE)
			{
				listFriends.add(merchant);
			}
		}
		loadDataDone(listFriends,true);
	}

	//加载猜你喜欢
	public void loadUserGroup(final boolean isUp) {
		if(AppContext.currentAreaInfo()==null)
		{
			return;
		}

		UsrGroupReq guessYourLoveReq = new UsrGroupReq();
		guessYourLoveReq.setGroupSid(merchant.getSid());//复用的该字段存储的是用户组的SID
		guessYourLoveReq.setAccSid(AppContext.currentAccount().getSid());
		RequestBean<MerchantResponse> requestBean = new RequestBean<MerchantResponse>();
		requestBean.setRequestBody(guessYourLoveReq);
		requestBean.setResponseBody(MerchantResponse.class);
		requestBean.setURL(Constant.Requrl.getConcernUsrGroupUsrs());
		requestServer(requestBean, new UICallback<MerchantResponse>() {

			@Override
			public void onSuccess(MerchantResponse respose) {
				synchronized (mListView) {
					try {
						List<Merchant> concerns = respose.getList();
						loadDataDone(concerns,isUp);
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onError(String message) {
				//mSwipeLayout.setRefreshing(false);
				onLoad();
				showToast(message);
			}

			@Override
			public void onOffline(String message) {
				//mSwipeLayout.setRefreshing(false);
				onLoad();
				showToast(message);
			}
		});

	}

	//加载猜你喜欢
	public void loadGuessLove(final boolean isUp) {
		if(AppContext.currentAreaInfo()==null)
		{
			return;
		}

		GuessYourLoveReq guessYourLoveReq = new GuessYourLoveReq();
		guessYourLoveReq.setAreaSid(AppContext.currentAreaInfo().getSid());
		guessYourLoveReq.setAccSid(AppContext.currentAccount().getSid());
		RequestBean<MerchantResponse> requestBean = new RequestBean<MerchantResponse>();
		requestBean.setRequestBody(guessYourLoveReq);
		requestBean.setResponseBody(MerchantResponse.class);
		requestBean.setURL(Constant.Requrl.getGuessYourLove());
		requestServer(requestBean, new UICallback<MerchantResponse>() {

			@Override
			public void onSuccess(MerchantResponse respose) {
				synchronized (mListView) {
					try {
						List<Merchant> concerns = respose.getList();
						loadDataDone(concerns,isUp);
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onError(String message) {
				//mSwipeLayout.setRefreshing(false);
				onLoad();
				showToast(message);
			}

			@Override
			public void onOffline(String message) {
				//mSwipeLayout.setRefreshing(false);
				onLoad();
				showToast(message);
			}
		});

	}

	//加载我的粉丝
	public void loadMyFans(final boolean isUp) {
		if(isUp)
		{
			pageNo=0;
			isEnd=false;
		}else
		{
			if(isEnd)
			{
				//mSwipeLayout.setLoading(false);
				onLoad();
				return;
			}
			pageNo++;
		}

		GetFansReq fansReq = new GetFansReq();
		fansReq.setPageNo(pageNo);
		fansReq.setAccSid(merchant.getSid());
		if(merchant.getLongItude()>0)
		{//是商家
			fansReq.setSubType(Constant.SUBTYPE.SUBTYPE_MERCHANT);
		}else
		{//是用户
			fansReq.setSubType(Constant.SUBTYPE.SUBTYPE_USR);
		}
		RequestBean<MerchantResponse> requestBean = new RequestBean<MerchantResponse>();
		requestBean.setRequestBody(fansReq);
		requestBean.setResponseBody(MerchantResponse.class);
		requestBean.setURL(Constant.Requrl.getFans());
		requestServer(requestBean, new UICallback<MerchantResponse>() {
			@Override
			public void onSuccess(MerchantResponse respose) {
				try {
					loadDataDone(respose.getList(),isUp);
				} catch (Exception e) {
				}
			}

			@Override
			public void onError(String message) {
				isEnd=true;
//				if(isUp)
//				{
//					mSwipeLayout.setRefreshing(false);
//				}else
//				{
//					mSwipeLayout.setLoading(false);
//				}
				onLoad();
				showToast(message);
			}

			@Override
			public void onOffline(String message) {
				isEnd=true;
//				if(isUp)
//				{
//					mSwipeLayout.setRefreshing(false);
//				}else
//				{
//					mSwipeLayout.setLoading(false);
//				}
				onLoad();
				showToast(message);
			}
		});

	}

	private synchronized void loadDataDone(List<Merchant> listFns,boolean isUp)
	{
		try {
			if(isUp)
			{
				//mSwipeLayout.setRefreshing(false);
				myConcernAdapter.clear();
			}else
			{
				//mSwipeLayout.setLoading(false);
			}
			onLoad();
			if(listFns.size()==0)
			{
				isEnd=true;
				return;
			}
			if(listFns!=null)
			{
				for(Merchant merchant:listFns)
				{
					merchant.setType(Constant.SUBTYPE.SUBTYPE_USR);
				}
				myConcernAdapter.load(listFns);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		if(Constant.ConcernLoadType.LOAD_MYFRIEND==loadType)
		{//加载我的邻友
			loadMyFriend();
		}else if(Constant.ConcernLoadType.LOAD_FAVORITE==loadType)
		{//加载店铺收藏
			loadMyFavorite();
		}else if(Constant.ConcernLoadType.LOAD_SERVICE==loadType)
		{//加载服务号
			loadService();
		}else if(Constant.ConcernLoadType.LOAD_USER_GROUP==loadType)
		{//加载用户组的数据
			loadUserGroup(true);
		}else if(Constant.ConcernLoadType.LOAD_GUESS_LOVE==loadType)
		{//加载猜你喜欢
			loadGuessLove(true);
		}else if(Constant.ConcernLoadType.LOAD_FANS==loadType)
		{
			loadMyFans(true);
		}		
	}

	@Override
	public void onLoadMore() {
		if(Constant.ConcernLoadType.LOAD_MYFRIEND==loadType
				||Constant.ConcernLoadType.LOAD_GUESS_LOVE==loadType
				||Constant.ConcernLoadType.LOAD_USER_GROUP==loadType
				||Constant.ConcernLoadType.LOAD_FAVORITE==loadType
				||Constant.ConcernLoadType.LOAD_SERVICE==loadType)
		{//加载我的邻友,猜你喜欢,店铺收藏,都是直接读取缓存
			//mSwipeLayout.setLoading(false);
			onLoad();
		}else if(Constant.ConcernLoadType.LOAD_FANS==loadType)
		{
			loadMyFans(false);
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
}
