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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.bean.MessageCenterInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.MerchantMessageCenterInfoReq;
import com.yulinoo.plat.life.net.reqbean.MessageCenterInfoReq;
import com.yulinoo.plat.life.net.resbean.MessageCenterInfoResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.BackWidget.OnBackBtnClickListener;
import com.yulinoo.plat.life.ui.widget.MerchantPraiseDialog;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.views.adapter.MerchantPraiseItemAdapter;
import com.yulinoo.plat.life.views.adapter.MyPraiseItemAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;
//商家的点赞列表
public class MerchantPraiseListActivity extends BaseActivity implements OnClickListener,IXListViewListener{
	
	private int pageNo=0;
	private BackWidget back_btn;
	private TextView praise_title;
	private View more_function;
	private XListView mListView;
	private MerchantPraiseMoreFunction praiseMoreFunction;
	private LayoutInflater inflater;

	private MerchantPraiseItemAdapter merchantPraiseItemAdapter;
	private int nowSelected=Constant.PRAISEDIRECTION.DIRECTION_ALL;//默认把所有评论加上

	private boolean isEnd=false;
	private ForumNote forumNote;
	private Account meAcc;
	private boolean isMe=false;
	
	private boolean needRefresh=false;
	private static MerchantPraiseListActivity instance;
	
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.merchant_praise_list);
		instance=this;
		forumNote=(ForumNote) getIntent().getSerializableExtra(Constant.ActivityExtra.MERCHANT);
		meAcc=AppContext.currentAccount();
		inflater=LayoutInflater.from(this);
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
		praise_title=(TextView)findViewById(R.id.praise_title);
		praise_title.setOnClickListener(this);
		more_function=findViewById(R.id.add_praise_fl);
		more_function.setOnClickListener(this);
		praiseMoreFunction=new MerchantPraiseMoreFunction(this, inflater, more_function, forumNote);
		merchantPraiseItemAdapter=new MerchantPraiseItemAdapter(this);

		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(merchantPraiseItemAdapter);
		mListView.autoRefresh();
	}

	public static MerchantPraiseListActivity instance(){
		if (instance!=null) {
			return instance;
		}else {
			throw new RuntimeException("MerchantPraiseListActivity did not instantied!");
		}
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.add_praise_fl:{
			praiseMoreFunction.showMerchantPraiseMoreFunction();
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
				onLoad();
				return;
			}
			pageNo++;
		}

		MerchantMessageCenterInfoReq req=new MerchantMessageCenterInfoReq();
		req.setDirection(nowSelected);
		//req.setMevalue(AppContext.currentAccount().getMevalue());
		req.setCommentType(Constant.MSGTYPE.TYPE_PRAISE);
		req.setAccSid(forumNote.getAccSid());
		req.setPageNo(pageNo);
		RequestBean<MessageCenterInfoResponse> requestBean = new RequestBean<MessageCenterInfoResponse>();
		requestBean.setRequestBody(req);
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
		if(isUp)
		{
			merchantPraiseItemAdapter.clear();
		}else
		{
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
			merchantPraiseItemAdapter.load(list);
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
	
	public void setNeedRefresh(boolean mNeedRefresh){
		this.needRefresh=mNeedRefresh;
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
