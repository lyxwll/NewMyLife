package com.yulinoo.plat.life.views.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.MessageCenterInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.MessageCenterInfoReq;
import com.yulinoo.plat.life.net.resbean.MessageCenterInfoResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.BackWidget.OnBackBtnClickListener;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.views.adapter.MyPraiseItemAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;
//我的点赞列表
public class MyPraiseListActivity extends BaseActivity implements OnClickListener,IXListViewListener{
	private int pageNo=0;
	//private MyListView myListView;
	private BackWidget back_btn;
	private TextView praise_title;
	private LinearLayout my_praise_pop;
	private TextView all_praise;//全部评论
	private TextView praise_me;//我的评论
	private TextView my_praise;//评论我的
	
//	private MeNormalListView mListView;
//	private SwipeRefreshLayout mSwipeLayout;
	private XListView mListView;
	
	private MyPraiseItemAdapter myPraiseItemAdapter;
	private int nowSelected=Constant.PRAISEDIRECTION.DIRECTION_ALL;//默认把所有评论加上
	
	private boolean isEnd=false;
	
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.my_praise_list);
	}

	@SuppressLint({ "InlinedApi", "ResourceAsColor" })
	@Override
	protected void initComponent() {
		back_btn=(BackWidget)findViewById(R.id.back_btn);
		back_btn.setBackBtnClickListener(new OnBackBtnClickListener() {
			@Override
			public void onBackBtnClick() {
				finish();
			}
		});
		praise_title=(TextView)findViewById(R.id.praise_title);
		praise_title.setOnClickListener(this);
		all_praise=(TextView)findViewById(R.id.all_praise);
		all_praise.setOnClickListener(this);
		praise_me=(TextView)findViewById(R.id.praise_me);
		praise_me.setOnClickListener(this);
		my_praise=(TextView)findViewById(R.id.my_praise);
		my_praise.setOnClickListener(this);
		my_praise_pop=(LinearLayout)findViewById(R.id.my_praise_pop);
		my_praise_pop.setOnClickListener(this);
		myPraiseItemAdapter=new MyPraiseItemAdapter(this);
//		mListView = (MeNormalListView) findViewById(R.id.list);
//		mListView.setAdapter(myPraiseItemAdapter);
//		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//		mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
//			@Override
//			public void onRefresh() {
//				loadPraise(true);
//			}
//		});
//		mSwipeLayout.setOnLoadListener(new OnLoadListener() {
//			@Override
//			public void onLoad() {
//				loadPraise(false);
//			}
//		});
//		MeUtil.setSwipeLayoutColor(mSwipeLayout);
//		mSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
//		mSwipeLayout.setLoadNoFull(false);
//		mSwipeLayout.startRefresh();
		
		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(myPraiseItemAdapter);
		mListView.autoRefresh();
	}

	

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {
		//rightImg.setBackgroundResource(R.drawable.more_btn);
//		rightImg.setVisibility(View.GONE);
//		rightText.setVisibility(View.VISIBLE);
//		rightText.setText("朕已阅");
//		title.setText("消息中心");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		case R.id.praise_title:
		{
			if(my_praise_pop.getVisibility()==View.GONE)
			{
				my_praise_pop.setVisibility(View.VISIBLE);
			}else
			{
				my_praise_pop.setVisibility(View.GONE);
			}
			break;
		}
		case R.id.all_praise:
		{
			nowSelected=Constant.PRAISEDIRECTION.DIRECTION_ALL;
			praise_title.setText(getString(R.string.all_praise));
			my_praise_pop.setVisibility(View.GONE);
			loadPraise(true);
			break;
		}
		case R.id.praise_me:
		{
			nowSelected=Constant.PRAISEDIRECTION.DIRECTION_PRAISE_ME;
			praise_title.setText(getString(R.string.praise_me));
			my_praise_pop.setVisibility(View.GONE);
			loadPraise(true);
			break;
		}
		case R.id.my_praise:
		{
			nowSelected=Constant.PRAISEDIRECTION.DIRECTION_MY_PRAISE;
			praise_title.setText(getString(R.string.my_praise));
			my_praise_pop.setVisibility(View.GONE);
			loadPraise(true);
			break;
		}
		}
	}

	private void loadPraise(final boolean isUp)
	{
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
		
		MessageCenterInfoReq req=new MessageCenterInfoReq();
		req.setDirection(nowSelected);
		req.setMevalue(AppContext.currentAccount().getMevalue());
		req.setType(Constant.MSGTYPE.TYPE_PRAISE);
		req.setPageNo(pageNo);
		RequestBean<MessageCenterInfoResponse> requestBean = new RequestBean<MessageCenterInfoResponse>();
		requestBean.setRequestBody(req);
		requestBean.setResponseBody(MessageCenterInfoResponse.class);
		requestBean.setURL(Constant.Requrl.getMyWrapMessage());
		//isLoading=true;
		requestServer(requestBean, new UICallback<MessageCenterInfoResponse>() {
			@Override
			public void onSuccess(MessageCenterInfoResponse respose) {
				try {
					loadDataDone(respose,isUp);
					//isLoading=false;
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
	
	private synchronized void loadDataDone(MessageCenterInfoResponse respose,boolean isUp)
	{
		if(isUp)
		{
			//mSwipeLayout.setRefreshing(false);
			myPraiseItemAdapter.clear();
		}else
		{
			//mSwipeLayout.setLoading(false);
		}
		onLoad();
		List<MessageCenterInfo> list=respose.getList();
		if(list!=null)
		{
			if(list.size()==0)
			{
				list.add(null);
				isEnd=true;
				mListView.setPullLoadEnable(false);
			}
			myPraiseItemAdapter.load(list);
		}
	}
	
	@Override
	public void onRefresh() {
		loadPraise(true);
		mListView.setPullLoadEnable(true);
	}

	@Override
	public void onLoadMore() {
		loadPraise(false);
		mListView.setPullLoadEnable(true);
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
