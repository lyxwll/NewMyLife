package com.yulinoo.plat.life.ui.widget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.ConcernNumber;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.bean.MessageBox;
import com.yulinoo.plat.life.bean.MessageCenterInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.MessageCenterInfoReq;
import com.yulinoo.plat.life.net.resbean.MessageCenterInfoResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.service.MeMessageService.OnMessageBoxArrivedListener;
import com.yulinoo.plat.life.ui.widget.MyListView.OnRefreshListener;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.MeUtil.OnLoadFocusMerchantListener;
import com.yulinoo.plat.life.views.adapter.MyConcernAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class ConcernWidget extends LinearLayout implements OnMessageBoxArrivedListener,IXListViewListener {
	private Context mContext;	
	public ConcernWidget(Context context) {
		super(context);
		mContext = context;
		init();
	}
	public ConcernWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
//	private MeNormalListView mListView;
//	private SwipeRefreshLayout mSwipeLayout;
	private XListView mListView;
	
	public MyConcernAdapter myConcernAdapter;
	private Account me;
	//private List<UserGroup> lstGroups=null;
	private void init()
	{
		final View view = (View) LayoutInflater.from(mContext).inflate(R.layout.myconcern, this,false);
		me=AppContext.currentAccount();
//		mListView = (MeNormalListView) view.findViewById(R.id.list);
//		mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
//		mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
//			@Override
//			public void onRefresh() {
//				//loadContactUsr();
//				mSwipeLayout.setRefreshing(false);
//				if(AppContext.currentAccount()==null)
//				{
//					return;
//				}
//				if(AppContext.currentAreaInfo()==null)
//				{
//					return;
//				}
//				refreshConcern();
//			}
//		});
//		MeUtil.setSwipeLayoutColor(mSwipeLayout);
//		mSwipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
		
		MeMessageService.instance().addOnMessageBoxArrivedListener(this);

		//加载成功之后,再返回
		myConcernAdapter = new MyConcernAdapter(mContext/*,lstGroups*/);
		mListView = (XListView) view.findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(myConcernAdapter);
		this.addView(view);
	}
	
	private View menu_concern_redHot;
	public void setConcernMenuView(View  menu_concern_redHot)
	{
		this.menu_concern_redHot=menu_concern_redHot;
		refreshConcern();
	}

	public void showView()
	{
		this.setVisibility(View.VISIBLE);
	}
	public void hiddenView()
	{
		this.setVisibility(View.GONE);
	}

	//是否显示红点的判断
	private boolean isMenuShowRedHot()
	{
		Account account=AppContext.currentAccount();
		if(account!=null)
		{//显示组
			//当前用户关注的好友列表
			List<Merchant> listSubs=AppContext.currentFocusMerchant();
			//当成数据库存储的好友的更新状态
			List<ConcernNumber> lisCs=ConcernNumber.getUsrConcernNumber(account.getSid());
			if(lisCs!=null)
			{
				for(Merchant merchant:listSubs)
				{
					long accSid=merchant.getSid();//关注的商家/用户的主键
					for(ConcernNumber cn:lisCs)
					{
						long cacc=cn.getConcernAccSid().longValue();
						int concernNumber=cn.getNumber();
						if(concernNumber>0&&accSid==cacc)
						{//只要有一个没有读的话,就需要菜单显示红点
							return true;
						}
					}
				}
			}
		}
		List<MessageCenterInfo> lstMcis=MessageCenterInfo.getMessageCenterInfo();
		if(lstMcis!=null&&lstMcis.size()>0)
		{//检测最近联系人
			for(MessageCenterInfo mci:lstMcis)
			{
				if(mci.getReadStatus()==Constant.MessageReadStatus.READ_STATUS_UNREADED)
				{
					return true;
				}
			}
		}
		return false;
	}

//	//订阅的账号有数量更新
//	public void setSubscribeNumber(final Map<Long,Integer> map)
//	{
//		final Account account=AppContext.currentAccount();
//		if(account!=null)
//		{
//			//更新关注的好友信息
//			MeUtil.loadSubscribe(mContext, new OnLoadFocusMerchantListener() {
//				@Override
//				public void onLoadFocusMerchantDone(boolean isSuccess, String message) {
//					Iterator<Entry<Long, Integer>> it=map.entrySet().iterator();
//					while(it.hasNext())
//					{
//						Entry<Long, Integer> entry=it.next();
//						ConcernNumber.setConcernNumberReaded(account.getSid(), entry.getKey());
//						ConcernNumber cn=new ConcernNumber();
//						cn.setAlongSid(account.getSid());
//						cn.setConcernAccSid(entry.getKey());
//						cn.setNumber(entry.getValue());
//						cn.save();
//					}
//					refreshConcern();
//				}
//			});
//		}
//	}

	public void setRedHot()
	{
		if(myConcernAdapter!=null)
		{//重新更新一次
			//myConcernAdapter.setRedHot();
			//mSwipeLayout.startRefresh();
			mListView.autoRefresh();
		}
	}

	//加载最新的联系人,因为服务器端有更新了
	public void loadNewContactUsr()
	{
		if(AppContext.currentAccount()==null)
		{
			return;
		}
		if(AppContext.currentAreaInfo()==null)
		{
			return;
		}
		if(myConcernAdapter!=null)
		{
			long contactTime=0L;
			List<MessageCenterInfo> nowContact=myConcernAdapter.getList();
			for(MessageCenterInfo mci:nowContact)
			{
				if(mci!=null)
				{
					Long responseTime=mci.getResponseTime();
					if(responseTime!=null)
					{
						long rt=responseTime.longValue();
						if(rt>contactTime)
						{
							contactTime=rt;
						}
					}
				}
			}

			MessageCenterInfoReq req=new MessageCenterInfoReq();
			req.setMevalue(AppContext.currentAccount().getMevalue());
			req.setType(Constant.MSGTYPE.TYPE_CONTACT);		
			req.setContactTime(contactTime);
			RequestBean<MessageCenterInfoResponse> requestBean = new RequestBean<MessageCenterInfoResponse>();
			requestBean.setRequestBody(req);
			requestBean.setResponseBody(MessageCenterInfoResponse.class);
			requestBean.setURL(Constant.Requrl.getMyWrapMessage());
			MeMessageService.instance().requestServer(requestBean, new UICallback<MessageCenterInfoResponse>() {
				@Override
				public void onSuccess(MessageCenterInfoResponse respose) {
					try {
						List<MessageCenterInfo> list=respose.getList();
						if(list!=null)
						{
							loadDataDone(list);
						}
					} catch (Exception e) {
					}
				}

				@Override
				public void onError(String message) {
					MeUtil.showToast(mContext,message);
				}

				@Override
				public void onOffline(String message) {
					MeUtil.showToast(mContext,message);
				}
			});
		}
	}

	private synchronized void loadDataDone(List<MessageCenterInfo> listNewContacts)//传回来的联系人列表项
	{
		//当前正在显示的联系人列表项
		List<MessageCenterInfo> listNowContact=myConcernAdapter.getList();
		for(MessageCenterInfo mci:listNewContacts)
		{
			boolean isIncluded=false;//当前正在显示的最近联系是否已经包含了传入的mci对应的联系人
			for(MessageCenterInfo sr:listNowContact)
			{
				if(sr!=null)
				{
					if(mci.getResponseUsrSid().longValue()==sr.getResponseUsrSid().longValue())
					{
						isIncluded=true;
						//原来显示的联系人中已有该联系人,只是聊天的消息有所变化而已
						sr.setResponseContent(mci.getResponseContent());
						sr.setResponseTime(mci.getResponseTime());
						sr.setReadStatus(Constant.MessageReadStatus.READ_STATUS_UNREADED);
						if(me!=null)
						{
							sr.setGoodsPublishUsrSid(me.getSid());
						}
						sr.save();
						break;
					}
				}

			}

			if(!isIncluded)
			{//原来显示的联系人无该联系人,则加入即可
				mci.setReadStatus(Constant.MessageReadStatus.READ_STATUS_UNREADED);
				if(me!=null)
				{
					mci.setGoodsPublishUsrSid(me.getSid());
					mci.save();
				}
			}else
			{//当前正在显示的最近联系人中包含了mci对应的联系人,此时则更新最新的消息
//				new Update(MessageCenterInfo.class).set("readStatus=? and responseContent=? and responseTime=?", 
//						Constant.MessageReadStatus.READ_STATUS_READED,mci.getResponseContent(),mci.getResponseTime())
//						.where("goodsPublishUsrSid=? and responseUsrSid=?",me.getSid(),mci.getResponseUsrSid())
//						.execute();
				MessageCenterInfo.updateMci(mci);
			}
		}
		refreshConcern();
	}
	//重新刷新关注列表,包含组和最近联系人都要刷新
	public void refreshConcern()
	{
		if(myConcernAdapter!=null)
		{
			myConcernAdapter.clear();
			//取出自己的最近联系人列表
			List<MessageCenterInfo> dbmcis=MessageCenterInfo.getMessageCenterInfo();
			dbmcis.add(0, null);//占位头部
			myConcernAdapter.load(dbmcis);
		}
//		if(this.menu_concern_redHot!=null)
//		{
//			if(isMenuShowRedHot())
//			{
//				menu_concern_redHot.setVisibility(View.VISIBLE);
//			}else
//			{
//				menu_concern_redHot.setVisibility(View.GONE);
//			}
//		}
	}

	public void updateConcern(MessageCenterInfo msgci)
	{
		List<MessageCenterInfo> listmcis=myConcernAdapter.getList();
		if(listmcis!=null)
		{
			boolean linkedmanHasIncluded=false;//最近联系人中是否已经包含了该聊天对象
			for(MessageCenterInfo mci:listmcis)
			{
				if(mci!=null)
				{
					if(mci.getResponseUsrSid().longValue()==msgci.getResponseUsrSid().longValue())
					{
						linkedmanHasIncluded=true;
						break;
					}
				}
			}
			if(!linkedmanHasIncluded)
			{//当前联系人中未包含
				msgci.save();
			}else
			{//当前联系人已经包含了对应的聊天对象
				if(me!=null)
				{
					MessageCenterInfo.updateMci(msgci);
				}
			}
			refreshConcern();
		}
	}

	@Override
	public void onMessageBoxArrived(MessageBox box) {
		int letterNumber=box.getLetterNumber();
		if(letterNumber>0)
		{//说明有人给他发了私信
			loadNewContactUsr();
		}
	}
	@Override
	public void onRefresh() {
		onLoad();
		if(AppContext.currentAccount()==null)
		{
			return;
		}
		if(AppContext.currentAreaInfo()==null)
		{
			return;
		}
		refreshConcern();
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
