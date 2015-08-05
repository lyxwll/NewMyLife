package com.yulinoo.plat.life.ui.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.renderscript.Element;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.core.e;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.Category;
import com.yulinoo.plat.life.bean.ForumInfo;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.ProductListAddReq;
import com.yulinoo.plat.life.net.reqbean.UsrWeiboListReq;
import com.yulinoo.plat.life.net.resbean.ForumNoteResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.MyListView.OnRefreshListener;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.views.activity.LoginActivity;
import com.yulinoo.plat.life.views.activity.MeLifeMainActivity;
import com.yulinoo.plat.life.views.activity.PublishGoodsActivity;
import com.yulinoo.plat.life.views.adapter.ForumGridAdapter;
import com.yulinoo.plat.life.views.adapter.MerchantAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;

@SuppressLint({ "InlinedApi", "ResourceAsColor" })
public class NeighbourTalkRightWidget extends RelativeLayout implements OnClickListener,IXListViewListener{
	private Context mContext;

	public ForumInfo nowForum;
	private int postPageNo=0;
	private View right_btn_rl;
	private ImageView show_quan;//显示圈
	private TextView public_talk;
	private int USR_PUBLISH_INFO=1;//用户进行的是发布帖子
	private int USR_CHOICE_FORUM=2;//用户点击的是栏目
	private int nowAction=USR_CHOICE_FORUM;//当前用户的操作行为
	private WindowManager wManager = null;
	private WindowManager.LayoutParams wmParams;
	private View view;

	private ViewPager categoryViewPager;
	private List<View> mListViews=new ArrayList<View>();//用来存储pager的视图的
	private LinearLayout page_indicator;//用于显示分类页码的
	private List<ImageView> listPages=new ArrayList<ImageView>();//用来装圆点的

	private View forum_category_rl;
	private ImageView open_forum;
	private View choice_forum_position;

	private ImageView publish_note;

//	private MeNormalListView mListView;
//	public SwipeRefreshLayout mSwipeLayout;
	private XListView mListView;
	private MerchantAdapter indexChannelAdapter;

	private boolean isSelectedEdit=false;

	private View shadow_background;

	private boolean isEnd=false;
	
	private Activity activity;
	
	private boolean needReloadPost=true;
	
	public void setNeedRelaodPost(boolean needReloadPost){
		this.needReloadPost=needReloadPost;
	}	
	
	public NeighbourTalkRightWidget(Context context) {
		super(context);
		mContext = context;
		init();
	}
	public NeighbourTalkRightWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init()
	{
		final View convertView = (View) LayoutInflater.from(mContext).inflate(R.layout.neighbour_talk_right, null);
		indexChannelAdapter = new MerchantAdapter(mContext);
		mListView = (XListView) convertView.findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(indexChannelAdapter);

		choice_forum_position=convertView.findViewById(R.id.choice_forum_position);
		forum_category_rl=convertView.findViewById(R.id.forum_category_rl);
		open_forum=(ImageView)convertView.findViewById(R.id.open_forum);
		open_forum.setOnClickListener(this);

		publish_note=(ImageView)convertView.findViewById(R.id.publish_note);
		publish_note.setOnClickListener(this);

		page_indicator = (LinearLayout) convertView.findViewById(R.id.page_indicator);
		categoryViewPager = (ViewPager) convertView.findViewById(R.id.forumViewPager);

		shadow_background=convertView.findViewById(R.id.shadow_background);
		this.addView(convertView);
	}
	
	public void show(boolean isShow)
	{
		if(isShow)
		{
			this.setVisibility(View.VISIBLE);
			load();
		}else
		{
			this.setVisibility(GONE);
		}
	}
	
	public void setActivity(Activity activity)
	{
		this.activity=activity;
	}

	public void load()
	{
		if(AppContext.currentAreaInfo()==null)
		{
			if(MeLifeMainActivity.isInstanciated())
			{
				MeUtil.showToast(mContext,mContext.getString(R.string.needconcerarea));
			}
			return;
		}
		Category ca=AppContext.getNeighbourTalk();
		if(ca==null)
		{
			MeUtil.showToast(mContext,"未设置社区贴吧的栏目分类，请联系管理员");
			return;
		}
		if(indexChannelAdapter!=null)
		{
			if (needReloadPost) {
				needReloadPost=false;
				mListView.autoRefresh();
			}
			//loadPost(true);//mSwipeLayout.startRefresh();
		}
	}
	

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.publish_note:
		{
			Category neighbour_talk_category=AppContext.getNeighbourTalk();
			if(neighbour_talk_category==null)
			{
				MeUtil.showToast(mContext, "无分类,请联系管理员");
				return;
			}
			List<ForumInfo> lstForums=AppContext.getForumByCategoryId(neighbour_talk_category.getSid());
			if(lstForums.size()==0)
			{
				MeUtil.showToast(mContext, "无分类下的栏目,请联系管理员");
				return;
			}
			Account account=AppContext.currentAccount();
			if(account==null)
			{
				return;
			}
			if(!account.getIsUsrLogin())
			{
				MeUtil.showToast(activity, activity.getString(R.string.need_login_first));
				activity.startActivity(new Intent(activity, LoginActivity.class));
				return;
			}
			ForumInfo nowForum=lstForums.get(0);//发到第一个栏目,目前考虑将所有栏目合并
			ProductListAddReq plareq=new ProductListAddReq();
			AreaInfo ai=AppContext.nearByArea();
			plareq.setAlongAreaSid(ai.getSid());
			plareq.setCategorySid(nowForum.getAlongCategorySid());
			Account acc=AppContext.currentAccount();
			plareq.setMevalue(acc.getMevalue());
			plareq.setProductSid(nowForum.getProductSid());
			plareq.setForumSid(nowForum.getSid());
			mContext.startActivity(new Intent(mContext, PublishGoodsActivity.class)
			.putExtra(PublishGoodsActivity.PUBLIC_TAB, plareq)
			.putExtra(Constant.ActivityExtra.PUBLISH_GOODS_TYPE, Constant.PUBLISH_GOODS_TYPE.PERSONAL_NEIGHBOUR_PUBLISH));
			break;
		}
		case R.id.open_forum:
		{
			setForums();
			break;
		}
		}
	}
	
	

	private void setForums()
	{
		if(forum_category_rl.getVisibility()==View.VISIBLE)
		{
			choice_forum_position.setVisibility(View.GONE);
			forum_category_rl.setVisibility(View.GONE);
			isSelectedEdit=false;//只要关闭，则取消选中发贴状态
			Animation outani=AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_out);
			forum_category_rl.setAnimation(outani);
			outani.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					shadow_background.setVisibility(View.GONE);
				}
			});
		}else
		{
			shadow_background.setVisibility(View.VISIBLE);
			showForumTab();
			forum_category_rl.setVisibility(View.VISIBLE);
			Animation inani=AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in);
			forum_category_rl.setAnimation(inani);
			
		}
	}
	public void closeForums()
	{
		shadow_background.setVisibility(View.GONE);
		forum_category_rl.setVisibility(View.GONE);
		isSelectedEdit=false;//只要关闭，则取消选中发贴状态
	}


	private int num_per_pager=6;//每页显示的帖子分类数量
	private void showForumTab()
	{
		categoryViewPager.removeAllViews();
		page_indicator.removeAllViews();
		mListViews.clear();
		listPages.clear();

		Category neighbour_talk_category=AppContext.getNeighbourTalk();
		if(neighbour_talk_category==null)
		{
			MeUtil.showToast(mContext, "无分类栏目,请联系管理员");
			return;
		}

		List<ForumInfo> lstForums=AppContext.getForumByCategoryId(neighbour_talk_category.getSid());
		int page_number=0;
		int size=lstForums.size();
		int mod=size%num_per_pager;
		if(mod==0)
		{
			page_number=size/num_per_pager;
		}else
		{
			page_number=size/num_per_pager+1;
		}
		for(int kk=0;kk<page_number;kk++)
		{
			View view=LayoutInflater.from(mContext).inflate(R.layout.neighbour_right_forum_gridview, null);
			mListViews.add(view);
			GridView gridView= (GridView) view.findViewById(R.id.main_gridview);
			gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			ForumGridAdapter zoneMainFunctionAdapter = new ForumGridAdapter(mContext);
			gridView.setAdapter(zoneMainFunctionAdapter);
			int tmpSize=(kk+1)*num_per_pager;
			if(tmpSize>size)
			{
				tmpSize=size;
			}
			List<ForumInfo> gridData=new ArrayList<ForumInfo>();
			for(int mk=kk*num_per_pager;mk<tmpSize;mk++)
			{
				gridData.add(lstForums.get(mk));
			}
			zoneMainFunctionAdapter.load(gridData);
			setOnGridViewItemListener(gridView, zoneMainFunctionAdapter);

			ImageView iv=new ImageView(mContext);
			android.widget.LinearLayout.LayoutParams vp=new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			iv.setLayoutParams(vp);
			vp.setMargins(7, 0, 7, 0);
			if(kk==0)
			{
				iv.setBackgroundResource(R.drawable.page_indicator_focused);
			}else
			{
				iv.setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			listPages.add(iv);
			page_indicator.addView(iv);
		}
		if(page_number==1)
		{//只有一页的情况下,不显示分页的点点
			page_indicator.setVisibility(View.GONE);
		}
		categoryViewPager.setAdapter(new PagerAdapter() {
			@Override  
			public void destroyItem(ViewGroup container, int position, Object object)   {     
				container.removeView(mListViews.get(position));//删除页卡  
			}

			@Override  
			public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡         
				container.addView(mListViews.get(position), 0);//添加页卡  
				return mListViews.get(position);  
			}  

			@Override  
			public int getCount() {           
				return  mListViews.size();//返回页卡的数量  
			}  

			@Override  
			public boolean isViewFromObject(View arg0, Object arg1) {             
				return arg0==arg1;//官方提示这样写  
			}
		});
		categoryViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				int size=listPages.size();
				for(int kk=0;kk<size;kk++)
				{
					ImageView iv=listPages.get(kk);
					if(position==kk)
					{
						iv.setBackgroundResource(R.drawable.page_indicator_focused);
					}else
					{
						iv.setBackgroundResource(R.drawable.page_indicator_unfocused);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});


	}
	//某个分类被选中了，则进入对应的栏目列表中
	private void setOnGridViewItemListener(GridView gridView, final ForumGridAdapter zoneMainFunctionAdapter) {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				nowForum=zoneMainFunctionAdapter.getItem(arg2);
				if(isSelectedEdit)
				{//是发帖
					ProductListAddReq plareq=new ProductListAddReq();
					AreaInfo ai=AppContext.nearByArea();
					plareq.setAlongAreaSid(ai.getSid());
					//plareq.setCategorySid(nowForum.getAlongCategorySid());
					Account acc=AppContext.currentAccount();
					plareq.setMevalue(acc.getMevalue());
					plareq.setProductSid(nowForum.getProductSid());
					plareq.setForumSid(nowForum.getSid());
					mContext.startActivity(new Intent(mContext, PublishGoodsActivity.class)
					.putExtra(PublishGoodsActivity.PUBLIC_TAB, plareq)
					.putExtra(Constant.ActivityExtra.PUBLISH_GOODS_TYPE, Constant.PUBLISH_GOODS_TYPE.PERSONAL_NEIGHBOUR_PUBLISH));
				}else
				{//是显示
					mListView.autoRefresh();
				}
				//setForums();
				closeForums();
			}
		});
	}

	//加载论坛帖子
	private void loadPost(final boolean isUp)
	{
		if(AppContext.getNeighbourTalk()!=null)
		{
			List<ForumInfo> lstForumInfos=AppContext.getForumByCategoryId(AppContext.getNeighbourTalk().getSid());
			if(lstForumInfos!=null&&lstForumInfos.size()>0)
			{
				load(isUp);
			}
		}else
		{
			onLoad();
		}
	}

	private void load(final boolean isUp)
	{
		long forumSid=-1;
		if(nowForum!=null)
		{
			forumSid=nowForum.getSid();
		}
		if(isUp)
		{
			postPageNo=0;
			isEnd=false;
		}else
		{
			if(isEnd)
			{
				//mSwipeLayout.setLoading(false);
				onLoad();
				return;
			}
			postPageNo++;
		}

		UsrWeiboListReq usrWeiboReq = new UsrWeiboListReq();
		usrWeiboReq.setAlongCategorySid(AppContext.getNeighbourTalk().getSid());
		usrWeiboReq.setAlongAreaSid(AppContext.currentAreaInfo().getSid());
		if(forumSid>0)
		{
			usrWeiboReq.setAlongForumSid(forumSid);
		}
		usrWeiboReq.setPageNo(postPageNo);
		RequestBean<ForumNoteResponse> requestBean = new RequestBean<ForumNoteResponse>();
		requestBean.setRequestBody(usrWeiboReq);
		requestBean.setResponseBody(ForumNoteResponse.class);
		requestBean.setURL(Constant.Requrl.getWeiBoList());
		MeMessageService.instance().requestServer(requestBean, new UICallback<ForumNoteResponse>() {

			@Override
			public void onSuccess(ForumNoteResponse respose) {
				try {
					loadDataDone(respose,isUp);
				} catch (Exception e) {
				}
			}

			@Override
			public void onError(String message) {
				isEnd=true;
				onLoad();
				MeUtil.showToast(mContext, message);
			}

			@Override
			public void onOffline(String message) {
				isEnd=true;
				onLoad();
				MeUtil.showToast(mContext, message);
			}
		});
	}

	private synchronized void loadDataDone(ForumNoteResponse respose,boolean isUp)
	{
		if(isUp)
		{
			onLoad();
			indexChannelAdapter.clear();
		}else
		{
			onLoad();
		}
		if(respose!=null)
		{
			List<ForumNote> concerns = respose.getList();
			if(concerns!=null)
			{
				if(concerns.size()==0)
				{
					isEnd=true;
				}
				indexChannelAdapter.load(concerns, false);
			}
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
		loadPost(true);
	}
	@Override
	public void onLoadMore() {
		loadPost(false);
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
