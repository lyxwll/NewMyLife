package com.yulinoo.plat.life.views.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.views.adapter.MyCommentItemAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;
//我的评论列表
public class MyCommentListActivity extends BaseActivity implements OnClickListener,IXListViewListener{
	private int pageNo=0;
	//private MyListView myListView;
	private BackWidget back_btn;
	private TextView comment_title;
	private LinearLayout my_comment_pop;
	private TextView all_comment;//全部评论
	private TextView my_comment;//我的评论
	private TextView comment_me;//评论我的
	
	private MyCommentItemAdapter myCommentItemAdapter;
	private int nowSelected=Constant.COMMDIRECTION.DIRECTION_ALL;//默认把所有评论加上
//	private MeNormalListView mListView;
//	private SwipeRefreshLayout mSwipeLayout;
	private XListView mListView;
	private boolean isEnd=false;
	//private boolean isLoading=false;
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.my_comment_list);
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
		comment_title=(TextView)findViewById(R.id.comment_title);
		comment_title.setOnClickListener(this);
		all_comment=(TextView)findViewById(R.id.all_comment);
		all_comment.setOnClickListener(this);
		my_comment=(TextView)findViewById(R.id.my_comment);
		my_comment.setOnClickListener(this);
		comment_me=(TextView)findViewById(R.id.comment_me);
		comment_me.setOnClickListener(this);
		my_comment_pop=(LinearLayout)findViewById(R.id.my_comment_pop);
		my_comment_pop.setOnClickListener(this);
		
		myCommentItemAdapter=new MyCommentItemAdapter(this);
//		mListView = (MeNormalListView) findViewById(R.id.list);
//		mListView.setAdapter(myCommentItemAdapter);
//		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//		mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
//			@Override
//			public void onRefresh() {
//				loadComment(true);
//			}
//		});
//		mSwipeLayout.setOnLoadListener(new OnLoadListener() {
//			@Override
//			public void onLoad() {
//				loadComment(false);
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
		mListView.setAdapter(myCommentItemAdapter);
		mListView.autoRefresh();
		
		
//		myListView=(MyListView)findViewById(R.id.myListView);
//		myListView.setAdapter(myCommentItemAdapter);
//		myListView.setonRefreshListener(new OnRefreshListener() {
//			@Override
//			public void onRefresh() {
//				pageNo=0;
//				loadComment(true);
//			}
//			
//			@Override
//			public void onBottom() {
//				pageNo++;
//				loadComment(false);
//			}
//		});
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
		case R.id.comment_title:
		{
			if(my_comment_pop.getVisibility()==View.GONE)
			{
				my_comment_pop.setVisibility(View.VISIBLE);
			}else
			{
				my_comment_pop.setVisibility(View.GONE);
			}
			break;
		}
		case R.id.all_comment:
		{
			nowSelected=Constant.COMMDIRECTION.DIRECTION_ALL;
			comment_title.setText(getString(R.string.all_comment));
			my_comment_pop.setVisibility(View.GONE);
			loadComment(true);
			break;
		}
		case R.id.my_comment:
		{
			nowSelected=Constant.COMMDIRECTION.DIRECTION_MY_COMMENT;
			comment_title.setText(getString(R.string.my_comment));
			my_comment_pop.setVisibility(View.GONE);
			loadComment(true);
			break;
		}
		case R.id.comment_me:
		{
			nowSelected=Constant.COMMDIRECTION.DIRECTION_COMMENT_MINE;
			comment_title.setText(getString(R.string.comment_me));
			my_comment_pop.setVisibility(View.GONE);
			loadComment(true);
			break;
		}
		}
	}

	private void loadComment(final boolean isUp)
	{
		if(isUp==true)
		{
			pageNo=0;
			isEnd=false;
		}else
		{
			if(isEnd==true)
			{
				//mSwipeLayout.setLoading(false);
				onLoad();
				return;
			}
			pageNo++;
		}/*if (isLoading==true) {
			return;
		}*/
		
		MessageCenterInfoReq req=new MessageCenterInfoReq();
		req.setDirection(nowSelected);
		req.setMevalue(AppContext.currentAccount().getMevalue());
		req.setType(Constant.MSGTYPE.TYPE_COMMENT);
		req.setPageNo(pageNo);
		RequestBean<MessageCenterInfoResponse> requestBean = new RequestBean<MessageCenterInfoResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(req);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(MessageCenterInfoResponse.class);
		requestBean.setURL(Constant.Requrl.getMyWrapMessage());
		//isLoading=true;
		requestServer(requestBean, new UICallback<MessageCenterInfoResponse>() {
			@Override
			public void onSuccess(MessageCenterInfoResponse respose) {
				try {
					loadDataDone(respose,isUp);
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
		if(isUp==true)
		{
			//mSwipeLayout.setRefreshing(false);
			myCommentItemAdapter.clear();
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
				mListView.setPullLoadEnable(false);
				list.add(null);
				isEnd=true;
			}
			myCommentItemAdapter.load(list);
		}
		
	}
	

	@Override
	public void onRefresh() {
		loadComment(true);
		mListView.setPullLoadEnable(true);
	}

	@Override
	public void onLoadMore() {
		loadComment(false);
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
