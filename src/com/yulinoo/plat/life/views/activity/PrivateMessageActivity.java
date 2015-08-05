package com.yulinoo.plat.life.views.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.ChatMessage;
import com.yulinoo.plat.life.bean.MessageCenterInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.MessageCenterInfoReq;
import com.yulinoo.plat.life.net.reqbean.SendMessageReq;
import com.yulinoo.plat.life.net.resbean.NormalResponse;
import com.yulinoo.plat.life.net.resbean.ReceiveChatMessageResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.SendMessageWidget;
import com.yulinoo.plat.life.ui.widget.SendMessageWidget.OnSendMessageClickListener;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.views.adapter.ChatMsgViewAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;
//私信
public class PrivateMessageActivity extends BaseActivity implements IXListViewListener {
	//private ImageView ivIcon;
	//private MeFaceViewWiget meface;
	//private EditText comment_et;
	//private TextView publish_message;
	private Account account;
	private Account me;
	private MyApplication myapp;
	//private MyListView myListView;
	private  ChatMsgViewAdapter mMessageAdapter;//私信记录
	//private int pageNo=0;
	private Long beginTime=null;
	private Long endTime=null;
//	private MeNormalListView mListView;
//	private SwipeRefreshLayout mSwipeLayout;
	private XListView mListView;
	private SendMessageWidget send_message;
	
	private Timer timer = new Timer();
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.private_message);
		account = (Account) this.getIntent().getSerializableExtra(Constant.ActivityExtra.ACCOUNT);
		me=AppContext.currentAccount();
		myapp=(MyApplication)getApplication();
		//MeMessageService.instance().addOnMessageBoxArrivedListener(this);
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@SuppressLint({ "InlinedApi", "ResourceAsColor" })
	@Override
	protected void initComponent() {
		
		mMessageAdapter = new ChatMsgViewAdapter(this);

//		mListView = (MeNormalListView) findViewById(R.id.list);
//		mListView.setAdapter(mMessageAdapter);
//		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//		mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
//			@Override
//			public void onRefresh() {
//				loadLetterList(true);
//			}
//		});
//		MeUtil.setSwipeLayoutColor(mSwipeLayout);
//		mSwipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
		
		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		//mListView.setPullLoadEnable(true);
		//mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(mMessageAdapter);
		mListView.autoRefresh();

		send_message=(SendMessageWidget)findViewById(R.id.send_message);
		send_message.setOnSendMessageClickListener(new OnSendMessageClickListener() {
			@Override
			public void onSendMessageClick(String message) {
				sendMessage(message);
				loadLetterList(false);
				if(MeLifeMainActivity.isInstanciated())
				{
					MessageCenterInfo mci=new MessageCenterInfo();
					mci.setGoodsPublishUsrSid(me.getSid());
					mci.setResponseContent(message);
					mci.setReadStatus(Constant.MessageReadStatus.READ_STATUS_READED);
					mci.setResponseHeaderPicture(account.getHeadPicture());
					mci.setResponseNickName(account.getAccName());
					mci.setResponseTime(System.currentTimeMillis());
					mci.setResponseUsrSid(account.getSid());
					AreaInfo ai=account.getAreaInfo();
					if(ai!=null)
					{
						mci.setResponseUsrAreaName(ai.getAreaName());
						mci.setResponseUsrAreaSid(ai.getSid());
						//MeLifeMainActivity.instance().updateConcern(mci);
					}
					
//					MyConcernAdapter myConcernAdapter=MeLifeMainActivity.instance().myConcernFragment.myConcernAdapter;
//					if(myConcernAdapter!=null)
//					{
//						List<MessageCenterInfo> listmcis=myConcernAdapter.getList();
//						if(listmcis!=null&&listmcis.size()>0)
//						{
//							boolean linkedmanHasIncluded=false;//最近联系人中是否已经包含了该聊天对象
//							for(MessageCenterInfo mci:listmcis)
//							{
//								if(mci!=null)
//								{
//									if(mci.getResponseUsrSid().longValue()==account.getSid().longValue())
//									{
//										linkedmanHasIncluded=true;
//										break;
//									}
//								}
//							}
//							if(!linkedmanHasIncluded)
//							{//当前联系人中未包含
//								MessageCenterInfo mci=new MessageCenterInfo();
//								mci.setGoodsPublishUsrSid(me.getSid());
//								mci.setResponseContent(message);
//								mci.setReadStatus(Constant.MessageReadStatus.READ_STATUS_READED);
//								mci.setResponseHeaderPicture(account.getHeadPicture());
//								mci.setResponseNickName(account.getAccName());
//								mci.setResponseTime(System.currentTimeMillis());
//								mci.setResponseUsrSid(account.getSid());
//								AreaInfo ai=account.getAreaInfo();
//								if(ai!=null)
//								{
//									mci.setResponseUsrAreaName(ai.getAreaName());
//									mci.setResponseUsrAreaSid(ai.getSid());
//									mci.save();
//									myConcernAdapter.load(mci);
//								}
//							}
//						}
//					}
				}
			}
		});
		//loadLetterList(false);
		//initSize();
		if(timer!=null)
		{
			try {
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						loadLetterList(false);
					}
				},0,2000);
			} catch (Exception e) {
			}
		}
	}
	
	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {
		//rightImg.setBackgroundResource(R.drawable.more_btn);
		//		rightImg.setVisibility(View.GONE);
		//		rightText.setText("商铺详情");
		//
				title.setText(account.getAccName());
		//		rightImg.setVisibility(View.VISIBLE);
		//		if(NullUtil.isStrNotNull(account.getHeadPicture()))
		//		{
		//			//MeImageLoader.displayImage(account.getHeadPicture(), imageView, options);
		//		}
		//
		//		rightText.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//               //startActivity(new Intent(mContext,ShopDetailActivity.class));
		//			}
		//		});
	}

	
	@Override
	protected void onDestroy() {
		//MeMessageService.instance().removeOnMessageBoxArrivedListener(this);
		timer.cancel();
		timer=null;
		super.onDestroy();
	}	
	
	private void sendMessage(String message)
	{
		SendMessageReq req=new SendMessageReq();
		req.setAccSid(account.getSid());
		req.setMevalue(AppContext.currentAccount().getMevalue());
		req.setType(Constant.MSGTYPE.TYPE_CONTACT);
		req.setDesc(message);
		req.setAlongAreaSid(AppContext.nearByArea().getSid());

		RequestBean<NormalResponse> requestBean = new RequestBean<NormalResponse>();
		requestBean.setRequestBody(req);
		requestBean.setResponseBody(NormalResponse.class);
		requestBean.setURL(Constant.Requrl.getSendWrapMessage());
		requestServer(requestBean, new UICallback<NormalResponse>() {
			@Override
			public void onSuccess(final NormalResponse respose) {
				if(respose.isSuccess())
				{
					//comment_et.setText("");
				}else
				{
					showToast(respose.getMsg());
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
	private boolean isLoading=false;//是否正在加载数据
	//加载私信消息记录
	private synchronized void loadLetterList(final boolean isUp)
	{
		if(isLoading)
		{
			if(isUp)
			{
				//mSwipeLayout.setRefreshing(false);
				onLoad();
			}
			return;
		}
		endTime=beginTime=null;
		if(isUp)
		{
			//myListView.initFreshing();
			List<ChatMessage> lstyl=mMessageAdapter.getList();
			if(lstyl!=null&&lstyl.size()>0)
			{
				endTime=lstyl.get(0).getSendTime();
			}
		}else
		{
			//myListView.initLoadgingData();
			List<ChatMessage> lstyl=mMessageAdapter.getList();
			if(lstyl!=null&&lstyl.size()>0)
			{
				beginTime=lstyl.get(lstyl.size()-1).getSendTime();
			}
		}
		isLoading=true;
		MessageCenterInfoReq req=new MessageCenterInfoReq();
		req.setMevalue(AppContext.currentAccount().getMevalue());
		req.setResponseUsrSid(account.getSid());
		req.setBeginTime(beginTime);
		req.setEndTime(endTime);
		RequestBean<ReceiveChatMessageResponse> requestBean = new RequestBean<ReceiveChatMessageResponse>();
		requestBean.setRequestBody(req);
		requestBean.setResponseBody(ReceiveChatMessageResponse.class);
		requestBean.setURL(Constant.Requrl.getMyPrivateMessage());
		requestServer(requestBean, new UICallback<ReceiveChatMessageResponse>() {
			@Override
			public void onSuccess(ReceiveChatMessageResponse respose) {
				try {
					loadDataDone(respose);
				} catch (Exception e) {
				}
				
				isLoading=false;
			}

			@Override
			public void onError(String message) {
				//mSwipeLayout.setRefreshing(false);
				onLoad();
				showToast(message);
				isLoading=false;
			}

			@Override
			public void onOffline(String message) {
				//mSwipeLayout.setRefreshing(false);
				onLoad();
				showToast(message);
				isLoading=false;
			}
		});
	}
	
	private synchronized void loadDataDone(ReceiveChatMessageResponse respose)
	{
		//mSwipeLayout.setRefreshing(false);
		onLoad();
		if(respose.isSuccess())
		{
			long csid=AppContext.currentAccount().getSid();
			List<ChatMessage> listnew=respose.getList();
			if(listnew!=null&&listnew.size()>0)
			{
				List<ChatMessage> lstAscOrder=new ArrayList<ChatMessage>();
				int lnsize=listnew.size();
				for(int kk=lnsize-1;kk>=0;kk--)
				{
					ChatMessage cm=listnew.get(kk);
					if(csid==cm.getAccSid())
					{
						cm.setMyMessage(true);
					}else
					{
						cm.setMyMessage(false);
					}
					lstAscOrder.add(cm);
				}
				List<ChatMessage> lstCorrect=new ArrayList<ChatMessage>();
				List<ChatMessage> lstyl=mMessageAdapter.getList();
				for(ChatMessage cm:lstyl)
				{
					lstCorrect.add(cm);
				}
				mMessageAdapter.clear();
				if(beginTime!=null)
				{//往下拉取最新的数据，则是在原来的数据之下加上最新的
					
					lstCorrect.addAll(lstAscOrder);
					mMessageAdapter.load(lstCorrect);
					mListView.setSelection(lstCorrect.size());
				}else
				{
					if(endTime!=null)
					{//往上拉，则是在原来的数据之上中上获取回来的
						lstAscOrder.addAll(lstCorrect);
						mMessageAdapter.load(lstAscOrder);
						mListView.setSelection(0);
					}else
					{
						mMessageAdapter.load(lstAscOrder);
						mListView.setSelection(lstAscOrder.size());
					}
				}
			}
		}
	}

	@Override
	public void onRefresh() {
		loadLetterList(true);		
	}

	@Override
	public void onLoadMore() {
		
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
