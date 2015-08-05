package com.yulinoo.plat.life.ui.widget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.ChannelReq;
import com.yulinoo.plat.life.net.resbean.ForumNoteResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.views.activity.ZoneSelectActivity;
import com.yulinoo.plat.life.views.adapter.MerchantAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class IndexWidget extends LinearLayout implements OnClickListener,IXListViewListener {
	private Context mContext;	
	public IndexWidget(Context context) {
		super(context);
		mContext = context;
		init();
	}
	public IndexWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	private int queryType=Constant.AREA.QUERY_OWN_AREAINFO;
	private MerchantAdapter indexChannelAdapter;
	//private MeNormalListView mListView;
	//private SwipeRefreshLayout mSwipeLayout;
	private XListView mListView;
	public ImageView indirector_selector;//第一次引导帮助
	private boolean isEnd=false;
	private int pageNo=0;
	private void init()
	{
		View view = LayoutInflater.from(mContext).inflate(R.layout.index, this,false);
		indexChannelAdapter = new MerchantAdapter(mContext);
		//		mListView = (MeNormalListView) view.findViewById(R.id.list);
		//		mListView.setAdapter(indexChannelAdapter);
		//		mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		//		mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
		//			@Override
		//			public void onRefresh() {
		//				loadMerchant(true);
		//			}
		//		});
		//		mSwipeLayout.setOnLoadListener(new OnLoadListener() {
		//			@Override
		//			public void onLoad() {
		//				loadMerchant(false);
		//			}
		//		});
		//		MeUtil.setSwipeLayoutColor(mSwipeLayout);
		//		mSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
		//		mSwipeLayout.setLoadNoFull(false);

		mListView = (XListView) view.findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(indexChannelAdapter);

		indirector_selector=(ImageView)view.findViewById(R.id.indirector_selector);
		if(AppContext.currentAreaInfo()!=null)
		{
			indirector_selector.setVisibility(View.GONE);
		}else
		{
			indirector_selector.setVisibility(View.VISIBLE);
			indirector_selector.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//startActivityForResult(new Intent(getActivity(), ZoneSelectActivity.class), 100);
					mContext.startActivity(new Intent(mContext, ZoneSelectActivity.class));
				}
			});
		}
		this.addView(view);
	}

	public void loadMerchant(final boolean isUp) {
		if(AppContext.currentAreaInfo()==null)
		{
			//			if(isUp)
			//			{
			//				mSwipeLayout.setRefreshing(false);
			//			}else
			//			{
			//				mSwipeLayout.setLoading(false);
			//			}
			onLoad();
			return;
		}
		if(isUp)
		{
			isEnd=false;
			pageNo=0;
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
		if(MeMessageService.isReady())
		{
			if(!(MeMessageService.instance().hasInternet(mContext)))
			{//没有网络的情况,则直接读取缓存的数据
				//				if(isUp)
				//				{
				//					mSwipeLayout.setRefreshing(false);
				//				}else
				//				{
				//					mSwipeLayout.setLoading(false);
				//				}
				onLoad();
				if(indexChannelAdapter.getList().size()==0)
				{//读缓存
					List<ForumNote> listForumNotes=new Select().from(ForumNote.class).where("alongAreaSid=?",AppContext.currentAreaInfo().getSid()).orderBy("createDate desc").execute();
					indexChannelAdapter.load(listForumNotes);
				}
				return;
			}

			ChannelReq merchantReq = new ChannelReq();
			merchantReq.setAreaInfoSid(AppContext.currentAreaInfo().getSid());
			merchantReq.setQueryType(queryType);
			merchantReq.setPosition(Constant.AREA.CHANNEL_INDEX);
			merchantReq.setPageNo(pageNo);

			RequestBean<ForumNoteResponse> requestBean = new RequestBean<ForumNoteResponse>();
			requestBean.setRequestBody(merchantReq);
			requestBean.setResponseBody(ForumNoteResponse.class);
			requestBean.setURL(Constant.Requrl.getIndexMerchantList());
			MeMessageService.instance().requestServer(requestBean, new UICallback<ForumNoteResponse>() {
				@Override
				public void onSuccess(ForumNoteResponse respose) {
					try {
						onLoadDataDone(respose,isUp);
					} catch (Exception e) {
					}
				}

				@Override
				public void onError(String message) {
					isEnd=true;
					//					if(isUp)
					//					{
					//						mSwipeLayout.setRefreshing(false);
					//					}else
					//					{
					//						mSwipeLayout.setLoading(false);
					//					}
					onLoad();
					MeUtil.showToast(mContext, message);
				}

				@Override
				public void onOffline(String message) {
					isEnd=true;
					//					if(isUp)
					//					{
					//						mSwipeLayout.setRefreshing(false);
					//					}else
					//					{
					//						mSwipeLayout.setLoading(false);
					//					}
					onLoad();
					MeUtil.showToast(mContext, message);
				}
			});
		}
	}
	private synchronized void onLoadDataDone(ForumNoteResponse respose,boolean isUp)
	{
		try {
			if(isUp)
			{
				new Delete().from(ForumNote.class).where("alongAreaSid=?",AppContext.currentAreaInfo().getSid()).execute();
				indexChannelAdapter.clear();
				//mSwipeLayout.setRefreshing(false);
			}else
			{
				//mSwipeLayout.setLoading(false);
			}
			onLoad();
			List<ForumNote> listFns=respose.getList();
			if(listFns!=null)
			{
				if(listFns.size()==0)
				{
					isEnd=true;
				}
				if(listFns!=null)
				{
					for(ForumNote fn:listFns)
					{
						new Delete().from(ForumNote.class).where("goodsSid=? and alongAreaSid=? ",fn.getGoodsSid(),AppContext.currentAreaInfo().getSid()).execute();
						fn.setAlongAreaSid(AppContext.currentAreaInfo().getSid());
						fn.save();
					}
					indexChannelAdapter.load(listFns);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadIndex()
	{
		mListView.autoRefresh();
		if(indirector_selector!=null)
		{
			indirector_selector.setVisibility(View.GONE);
		}
	}

//	public void reLoadIndex()
//	{
//		//mSwipeLayout.startRefresh();
//		//loadMerchant(true);
//		mListView.autoRefresh();
//		if(indirector_selector!=null)
//		{
//			indirector_selector.setVisibility(View.GONE);
//		}
//	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{

		}
	}

	//主要用于商品的评论和点赞数量的增加,向GoodsDetailActivity提供的方法,其他地方不会使用
	public void goodsNumberAdd(Long goodsSid,int type)
	{
		if(goodsSid!=null)
		{
			if(indexChannelAdapter!=null)
			{
				List<ForumNote> list=indexChannelAdapter.getList();
				for(ForumNote fonote:list)
				{
					if(fonote.getGoodsSid().longValue()==goodsSid.longValue())
					{
						if(type==Constant.MSGTYPE.TYPE_COMMENT)
						{//是评论
							fonote.setCommentNumber(fonote.getCommentNumber().intValue()+1);
						}else if(type==Constant.MSGTYPE.TYPE_PRAISE)
						{//是赞
							fonote.setPraiseNumber(fonote.getPraiseNumber().intValue()+1);
						}
					}
				}
				indexChannelAdapter.notifyDataSetChanged();
			}
		}
	}
	@Override
	public void onRefresh() {
		loadMerchant(true);
	}
	@Override
	public void onLoadMore() {
		loadMerchant(false);
	}
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getTime());
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
	}
	
//	private boolean isInitShow=false;
//	public boolean hasInitShow()
//	{
//		return isInitShow;
//	}
//	public void setInitShow(boolean initShow)
//	{
//		isInitShow=initShow;
//	}

}
