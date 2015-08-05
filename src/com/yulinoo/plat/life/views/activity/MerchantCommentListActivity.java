package com.yulinoo.plat.life.views.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.bean.MessageCenterInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.MerchantMessageCenterInfoReq;
import com.yulinoo.plat.life.net.resbean.MessageCenterInfoResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.BackWidget.OnBackBtnClickListener;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.views.adapter.MerchantCommentItemAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;
//商家的评论列表
public class MerchantCommentListActivity extends BaseActivity implements OnClickListener,IXListViewListener{
	
	private static MerchantCommentListActivity instance;
	
	private int pageNo=0;
	private BackWidget back_btn;

	private MerchantCommentItemAdapter merchantCommentItemAdapter;
	private int nowSelected=Constant.COMMDIRECTION.DIRECTION_ALL;//默认把所有评论加上
	private XListView mListView;
	private boolean isEnd=false;
	
	private ForumNote forumNote;
	private Account meAcc;
	private boolean isMe=false;
	private View more_function;
	private MerchantCommentMoreFunction commentMoreFunction;
	private LayoutInflater inflater;
	
	private boolean needRefresh=false;
	
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.merchant_comment_list);
		instance=this;
		forumNote=(ForumNote) getIntent().getSerializableExtra(Constant.ActivityExtra.MERCHANT);
		meAcc=AppContext.currentAccount();
		inflater=LayoutInflater.from(this);
		more_function=findViewById(R.id.add_comment_fl);
		more_function.setOnClickListener(this);
		commentMoreFunction=new MerchantCommentMoreFunction(this, inflater, more_function,forumNote);
		if (forumNote.getAccSid()==meAcc.getSid()) {
			isMe=true;
		}
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
		merchantCommentItemAdapter=new MerchantCommentItemAdapter(this);

		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(merchantCommentItemAdapter);
		mListView.autoRefresh();
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		case R.id.add_comment_fl:{
			commentMoreFunction.showMerchantCommentMoreFunction();
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
				onLoad();
				return;
			}
			pageNo++;
		}

		MerchantMessageCenterInfoReq req=new MerchantMessageCenterInfoReq();
		req.setDirection(nowSelected);
		req.setAccSid(forumNote.getAccSid());
		if (isMe) {
			req.setMevalue(AppContext.currentAccount().getMevalue());
		}
		req.setCommentType(Constant.MSGTYPE.TYPE_COMMENT);
		req.setPageNo(pageNo);
		RequestBean<MessageCenterInfoResponse> requestBean = new RequestBean<MessageCenterInfoResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(req);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(MessageCenterInfoResponse.class);
		requestBean.setURL(Constant.Requrl.getMerchantReview());
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
				onLoad();
				showToast(message);
			}

			@Override
			public void onOffline(String message) {
				isEnd=true;
				onLoad();
				showToast(message);
			}
		});
	}

	private synchronized void loadDataDone(MessageCenterInfoResponse respose,boolean isUp)
	{
		if(isUp==true)
		{
			merchantCommentItemAdapter.clear();
		}else
		{
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
			merchantCommentItemAdapter.load(list);
		}

	}

	public static MerchantCommentListActivity instance(){
		if (instance!=null) {
			return instance;
		}else {
			throw new RuntimeException("MerchantCommentListActivity did not instantied!");
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
	
	public void setNeedRefresh(boolean needRefresh){
		this.needRefresh=needRefresh;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			if (needRefresh) {
				mListView.autoRefresh();
			}
		}
	}
}
