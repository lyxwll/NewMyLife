package com.yulinoo.plat.life.ui.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.ChatMessage;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.GetChatMessageReq;
import com.yulinoo.plat.life.net.reqbean.SendChatMessageReq;
import com.yulinoo.plat.life.net.resbean.NormalResponse;
import com.yulinoo.plat.life.net.resbean.ReceiveChatMessageResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.SendMessageWidget.OnSendMessageClickListener;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.UpdateDtUtil;
import com.yulinoo.plat.life.views.activity.LoginActivity;
import com.yulinoo.plat.life.views.activity.MeLifeMainActivity;
import com.yulinoo.plat.life.views.adapter.ChatMsgViewAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;

@SuppressLint({ "ResourceAsColor", "InlinedApi" })
public class NeighbourTalkLeftWidget extends RelativeLayout implements IXListViewListener {
	private Context mContext;
	private ChatMsgViewAdapter mMessageAdapter;//聊天消息适配器
	private Long beginTime=null;
	private Long endTime=null;

//	private MeNormalListView mListView;
//	private SwipeRefreshLayout mSwipeLayout;

	private SendMessageWidget sendMessage;
	
	private Timer timer = new Timer();
	private TimerTask timerTask=null;
	private XListView mListView;

	public NeighbourTalkLeftWidget(Context context) {
		super(context);
		mContext = context;
	}
	public NeighbourTalkLeftWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		final View convertView = (View) LayoutInflater.from(context).inflate(R.layout.neighbour_talk_left, null);
		mMessageAdapter = new ChatMsgViewAdapter(mContext);
		sendMessage=(SendMessageWidget)convertView.findViewById(R.id.send_message);
		sendMessage.setOnSendMessageClickListener(new OnSendMessageClickListener() {
			@Override
			public void onSendMessageClick(String message) {
				sendMessage(message);
			}
		});

//		mListView = (MeNormalListView) convertView.findViewById(R.id.list);
//		mListView.setAdapter(mMessageAdapter);
//		mSwipeLayout = (SwipeRefreshLayout) convertView.findViewById(R.id.swipe_container);
//		mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
//			@Override
//			public void onRefresh() {
//				if(MeLifeMainActivity.isInstanciated())
//				{
//					if(AppContext.currentAreaInfo()==null)
//					{
//						MeUtil.showToast(mContext,mContext.getString(R.string.needconcerarea));
//						return;
//					}
//				}
//				loadMessage(true);
//			}
//		});
//		MeUtil.setSwipeLayoutColor(mSwipeLayout);
//		mSwipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
//		mSwipeLayout.setLoadNoFull(false);
		mListView = (XListView) convertView.findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		//mListView.setPullLoadEnable(true);
		//mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(mMessageAdapter);
		this.addView(convertView);
	}
	
	public void show(boolean isShow)
	{
		if(isShow)
		{
			this.setVisibility(View.VISIBLE);
		}else
		{
			this.setVisibility(GONE);
		}
	}

	public void startTimer()
	{
		if(timerTask==null)
		{
			try {
				timerTask=new TimerTask() {
					@Override
					public void run() {
						loadMessage(false);
					}
				};
				timer.schedule(timerTask, 0,2000);
			} catch (Exception e) {
				System.out.println("NeighbourTalkLeftWidget-startTimer"+e.getMessage());
			}
		}
	}
	public void stopTimer()
	{
		if(timerTask!=null)
		{
			try {
				timerTask.cancel();
				timerTask=null;
			} catch (Exception e) {
				System.out.println("NeighbourTalkLeftWidget-stopTimer"+e.getMessage());
				timerTask=null;
			}
		}
	}
	private boolean isLoading=false;
	//加载聊天记录
	public synchronized void loadMessage(final boolean isUp)
	{
		if(isLoading)
		{
			if(isUp)
			{
				onLoad();
			}
			return;
		}
		endTime=beginTime=null;
		if(isUp)
		{
			List<ChatMessage> lstyl=mMessageAdapter.getList();
			if(lstyl!=null&&lstyl.size()>0)
			{
				endTime=lstyl.get(0).getSendTime();
			}
		}else
		{
			List<ChatMessage> lstyl=mMessageAdapter.getList();
			if(lstyl!=null&&lstyl.size()>0)
			{
				beginTime=lstyl.get(lstyl.size()-1).getSendTime();
			}
		}
		GetChatMessageReq entity=new GetChatMessageReq();

		final AreaInfo ai=AppContext.currentAreaInfo();
		if(ai==null)
		{
			if(isUp)
			{
				onLoad();
			}
			return;
		}
		entity.setAreaSid(ai.getSid());
		entity.setBeginTime(beginTime);
		entity.setEndTime(endTime);
		RequestBean<ReceiveChatMessageResponse> requestBean = new RequestBean<ReceiveChatMessageResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(entity);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(ReceiveChatMessageResponse.class);
		requestBean.setURL(Constant.Requrl.getChatRoomMessage());
		isLoading=true;
		MeMessageService.instance().requestServer(requestBean, new UICallback<ReceiveChatMessageResponse>() {
			@Override
			public void onSuccess(ReceiveChatMessageResponse respose) {
				try {
					loadDataDone(respose,isUp);
				} catch (Exception e) {
				}
				isLoading=false;
			}

			@Override
			public void onError(String message) {
				if(isUp)
				{
					onLoad();
				}
				MeUtil.showToast(mContext, message);
				isLoading=false;
			}

			@Override
			public void onOffline(String message) {
				if(isUp)
				{
					onLoad();
				}
				MeUtil.showToast(mContext, message);
				isLoading=false;
			}
		});
	}
	
	private synchronized void loadDataDone(ReceiveChatMessageResponse respose,boolean isUP)
	{
		onLoad();
		if(respose.isSuccess())
		{
			long csid=AppContext.currentAccount().getSid();
			List<ChatMessage> listnew=respose.getList();
			if(listnew!=null&&listnew.size()>0)
			{
				Long bt=listnew.get(0).getSendTime();
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
					if(csid==cm.getAccSid())
					{
						cm.setMyMessage(true);
					}else
					{
						cm.setMyMessage(false);
					}
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
				AreaInfo areaInfo=AppContext.currentAreaInfo();
				Long upDt=UpdateDtUtil.getAreaChatRoomDt(getContext(), areaInfo.getSid());
				if(upDt!=null)
				{
					if(upDt.longValue()<bt.longValue())
					{
						UpdateDtUtil.setAreaChatRoomDt(getContext(), areaInfo.getSid(), bt.longValue());
					}
				}else
				{
					UpdateDtUtil.setAreaChatRoomDt(getContext(), areaInfo.getSid(), bt.longValue());
				}
			}
		}
	}

	//发送消息
	private void sendMessage(String message) {
//		if(!account.getIsUsrLogin())
//		{
//			MeUtil.showToast(activity, activity.getString(R.string.need_login_first));
//			activity.startActivity(new Intent(activity, LoginActivity.class));
//			return;
//		}
		if(AppContext.currentAccount()==null)
		{
			MeUtil.showToast(mContext,mContext.getString(R.string.need_login_first));
			//MeLifeMainActivity.instance().onMenuSelected(MeLifeMainActivity.instance().my_zone.getIndex());
			mContext.startActivity(new Intent(mContext, LoginActivity.class));
			return;
		}
		if(!AppContext.currentAccount().getIsUsrLogin())
		{
			MeUtil.showToast(mContext,mContext.getString(R.string.need_login_first));
			//MeLifeMainActivity.instance().onMenuSelected(MeLifeMainActivity.instance().my_zone.getIndex());
			mContext.startActivity(new Intent(mContext, LoginActivity.class));
			return;
		}
		if (message.length() > 0) {
			SendChatMessageReq entity = new SendChatMessageReq();
			final AreaInfo ai=AppContext.nearByArea();
			entity.setToAreaSid(AppContext.currentAreaInfo().getSid());
			entity.setFromAreaName(ai.getAreaName());
			entity.setFromAreaSid(ai.getSid());
			entity.setContent(message);
			entity.setMevalue(AppContext.currentAccount().getMevalue());

			RequestBean<NormalResponse> requestBean = new RequestBean<NormalResponse>();
			requestBean.setRequestBody(entity);
			requestBean.setResponseBody(NormalResponse.class);
			requestBean.setURL(Constant.Requrl.getSendChatRoomMessage());
			MeMessageService.instance().requestServer(requestBean, new UICallback<NormalResponse>() {

				@Override
				public void onSuccess(NormalResponse respose) {
					try {
						loadMessage(false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onError(String message) {
					//mListView.onRefreshComplete();
				}

				@Override
				public void onOffline(String message) {
					//mListView.onRefreshComplete();
				}
			});


			//			mDataArrays.add(entity);
			//			mAdapter.notifyDataSetChanged();
			//			mEditTextContent.setText("");
			//			mListView.setSelection(mListView.getCount() - 1);
		}else
		{
			MeUtil.showToast(mContext,mContext.getString(R.string.can_not_send_empty_message));
		}
	}

	//清除聊天室记录
	public synchronized void clearChatRoomMessage()
	{
		if(mMessageAdapter!=null)
		{
			mMessageAdapter.clear();
		}
	}
	@Override
	public void onRefresh() {
		if(MeLifeMainActivity.isInstanciated())
		{
			if(AppContext.currentAreaInfo()==null)
			{
				MeUtil.showToast(mContext,mContext.getString(R.string.needconcerarea));
				return;
			}
		}
		loadMessage(true);
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
