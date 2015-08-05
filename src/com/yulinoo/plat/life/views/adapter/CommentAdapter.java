package com.yulinoo.plat.life.views.adapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.Comment;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.CommentListReq;
import com.yulinoo.plat.life.net.resbean.CommentListResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.ProgressWebView;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.utils.BaseTools;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.FaceConversionUtil;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.MeUtil.ConcernResultListener;
import com.yulinoo.plat.life.utils.MeUtil.OnPraiseListener;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.life.views.activity.BaseActivity;
import com.yulinoo.plat.life.views.activity.GoodDetailMoreFuncton;
import com.yulinoo.plat.life.views.activity.MerchantMapActivity;
import com.yulinoo.plat.life.views.activity.PrivateMessageActivity;
import com.yulinoo.plat.life.views.activity.UsrShopActivity;
import com.yulinoo.plat.life.views.activity.UsrZoneActivity;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class CommentAdapter extends YuLinooLoadAdapter<Comment> implements OnClickListener,XListView.IXListViewListener{

	private LayoutInflater inflater;
	private Context mContext;
	private MyApplication myapp;
	private ForumNote forumNote;
	private int[] picture_size;
	//private SwipeRefreshLayout mSwipeLayout;
	private boolean isEnd=false;
	private boolean isLoading=false;
	private XListView mListView;

	//微信
	private IWXAPI wxapi;
	//微信APP_ID
	private static final String WX_APP_ID="wx4350b648c81fddfb";

	//更多功能的组件
	private GoodDetailMoreFuncton function;
	private View agree;
	private View share;
	private View location;
	private View phone;
	private View private_message;
	private View focus;
	private TextView focusTextView;//关注中的textView
	private View popContainer;
	private View right_img_view;
	private PopupWindow popupWindow;

	public CommentAdapter(Context context,ForumNote forumNote,XListView mListView) {
		inflater = LayoutInflater.from(context);
		mContext=context;
		picture_size=SizeUtil.weibo_picture_size(mContext);
		myapp=((MyApplication)mContext.getApplicationContext());
		this.forumNote=forumNote;
		agree_number = forumNote.getPraiseNumber();
		this.mListView=mListView;

		//微信实例化
		wxapi=WXAPIFactory.createWXAPI(mContext, WX_APP_ID);
		wxapi.registerApp(WX_APP_ID);

		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());

		mListView.setAdapter(this);
	}
	public static interface IViewType {
		int COMMEANT_HEADER = 0;//头部
		int COMMENT_ITEM = 1;//内容列表
		int COMMENT_FOOTER=2;//尾部无更多数据
	}

	@Override
	public int getItemViewType(int position) {
		if(position==0)
		{
			return IViewType.COMMEANT_HEADER;
		}else 
		{
			Comment fn=getItem(position);
			if(fn==null)
			{
				return IViewType.COMMENT_FOOTER;
			}
			return IViewType.COMMENT_ITEM;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	private TextView feel_ok;//显示他们觉得很赞的内容
	private TextView comment_count;
	private TextView agree_count;
	private int agree_number;
	private ImageView uparrow;

	private View function_list;//功能列表
	private View function_fl;//功能显示开关
	private ImageView function_img;//功能显示开关切换图片
	private TextView merchantDetail;//商家详情

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		final Comment item = getItem(position);
		if (convertView == null) {
			holder = new HolderView();
			if(position==0)
			{
				convertView=inflater.inflate(R.layout.item_goods_details_header, parent,false);
				feel_ok = (TextView)convertView.findViewById(R.id.feel_ok);
				feel_ok.setVisibility(View.GONE);
				TextView goods_content=(TextView)convertView.findViewById(R.id.goods_content);
				goods_content.setText(null);
				SpannableString spannableString = FaceConversionUtil.getInstance().getExpressionString(mContext, forumNote.getGoodsContent());
				if(spannableString!=null)
				{
					goods_content.append(spannableString);
				}
				String url=Constant.Requrl.getGoodsHtmlDesc()+"?sid="+forumNote.getGoodsSid();
				ProgressWebView goods_html_desc=(ProgressWebView)convertView.findViewById(R.id.goods_html_desc);
				goods_html_desc.loadUrl(url);
				//goods_html_desc.loadUrl("file:///android_asset/test.html");    

				TextView view_number=(TextView)convertView.findViewById(R.id.view_number);
				if(forumNote.getViewNumber()>9999)
				{
					view_number.setText(mContext.getString(R.string.view_number)+mContext.getString(R.string.much_more_view_number));
				}else
				{
					view_number.setText(mContext.getString(R.string.view_number)+forumNote.getViewNumber());
				}

				comment_count = (TextView)convertView.findViewById(R.id.comment_count);
				comment_count.setOnClickListener(this);
				comment_count.setText("评论 " +forumNote.getCommentNumber());	
				uparrow = (ImageView)convertView.findViewById(R.id.uparrow);
				agree_count = (TextView)convertView.findViewById(R.id.agree_count);
				agree_count.setOnClickListener(this);
				agree_count.setText(forumNote.getPraiseNumber()+ "人觉得赞");
				convertView.findViewById(R.id.merchant_info_rl).setOnClickListener(this);
				if(forumNote.getCreateDate()>100)
				{
					((TextView)convertView.findViewById(R.id.public_time)).setText(BaseTools.getChajuDate(forumNote.getCreateDate()));
				}
				((TextView)convertView.findViewById(R.id.name)).setText(forumNote.getAccName());
				convertView.findViewById(R.id.merchant_info_rl).setOnClickListener(this);;
				if(NullUtil.isStrNotNull(forumNote.getHeadPicture()))
				{
					ImageView merchantLogo=(ImageView)convertView.findViewById(R.id.merchantLogo);
					MeImageLoader.displayImage(forumNote.getHeadPicture(), merchantLogo, myapp.getHeadIconOption());
				}else
				{
					if(forumNote!=null)
					{
						ImageView merchantLogo=(ImageView)convertView.findViewById(R.id.merchantLogo);
						if(forumNote.getLatItude()!=null&&forumNote.getLatItude().doubleValue()>0)
						{//是商家
							merchantLogo.setImageResource(R.drawable.merchant_logo);
						}else
						{//是个人
							int sex=forumNote.getSex();
							if(Constant.SEX.SEX_WOMAN==sex)
							{
								merchantLogo.setImageResource(R.drawable.woman_selected);
							}else
							{
								merchantLogo.setImageResource(R.drawable.man_selected);
							}
						}
					}
				}
				//				function_fl=convertView.findViewById(R.id.function_fl);
				//				function_list=convertView.findViewById(R.id.function_list);
				//				function_img=(ImageView)convertView.findViewById(R.id.function_img);
				merchantDetail = (TextView) convertView.findViewById(R.id.merchant_detail_tv);
				initFunctions();
				initViewPager();
				initPagerIndicator();
				//mSwipeLayout.startRefresh();//开始加载评论内容
				mListView.autoRefresh();
			}else
			{
				if(item!=null)
				{
					convertView = inflater.inflate(R.layout.goods_comment_list_item, parent,false);
					holder.head = (ImageView) convertView.findViewById(R.id.head);
					holder.usr_sex_img = (ImageView) convertView.findViewById(R.id.usr_sex);
					holder.userName = (TextView) convertView.findViewById(R.id.userName);
					holder.publishTime = (TextView) convertView.findViewById(R.id.publishTime);
					holder.content = (TextView) convertView.findViewById(R.id.content);
					holder.userAddr = (TextView) convertView.findViewById(R.id.userAddr);
					holder.usr_info = convertView.findViewById(R.id.usr_info);
				}else
				{
					convertView=inflater.inflate(R.layout.included_cont_item_footer, parent,false);
				}
			}
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		if(position==0)
		{
			//			initViewPager();
			//			initPagerIndicator();
		}else
		{
			if(item!=null)
			{
				if(NullUtil.isStrNotNull(item.getHeadPicture()))
				{
					MeImageLoader.displayImage(item.getHeadPicture(), holder.head, myapp.getHeadIconOption());
				}else
				{
					int sex=item.getSex();
					if(Constant.SEX.SEX_MAN==sex)
					{
						holder.head.setImageResource(R.drawable.man_selected);
					}else if(Constant.SEX.SEX_WOMAN==sex)
					{
						holder.head.setImageResource(R.drawable.woman_selected);
					}
				}
				holder.userName.setText(item.getAccName());
				holder.publishTime.setText(BaseTools.getChajuDate(item.getCreatedDate()));
				if(item.getCommentType()==Constant.MSGTYPE.TYPE_COMMENT)
				{//是评论
					if(NullUtil.isStrNotNull(item.getCommentCont()))
					{
						holder.content.setText(null);			
						SpannableString spannableString = FaceConversionUtil.getInstance().getExpressionString(mContext, item.getCommentCont());
						holder.content.append(spannableString);
					}
				}else
				{//是赞
					holder.content.setText("赞了一下");
				}
				setSexImg(item,holder.usr_sex_img);
				holder.userAddr.setText(mContext.getString(R.string.come_from)+item.getAreaName());
				holder.usr_info.setTag(item);
				holder.usr_info.setOnClickListener(this);
			}else
			{

			}
		}
		return convertView;
	}

	//初始化图片浏览
	private void initViewPager()
	{

	}
	//初始化图片下面的圆点
	private void initPagerIndicator()
	{

	}

	//初始化功能
	private void initFunctions()
	{

		//function_list.setVisibility(View.GONE);
		right_img_view.setVisibility(View.GONE);
		Account me=AppContext.currentAccount();
		if(me.getSid()!=forumNote.getAccSid())
		{//不是自己
			//function_fl.setVisibility(View.VISIBLE);
			//function_fl.setOnClickListener(this);
			right_img_view.setVisibility(View.VISIBLE);
			right_img_view.setOnClickListener(this);
			////分享
			//View share=function_list.findViewById(R.id.share);
			//View private_message=function_list.findViewById(R.id.private_message);
			//View phone_number=function_list.findViewById(R.id.phone_number);
			//View iamhere=function_list.findViewById(R.id.iamhere);
			if(forumNote.getLongItude()!=null&&forumNote.getLongItude().doubleValue()>0)
			{//说明是商家
				private_message.setVisibility(View.GONE);
				phone.setVisibility(View.VISIBLE);
				location.setVisibility(View.VISIBLE);
				//点击了地图
				location.setOnClickListener(this);
				//点击了电话号码
				phone.setOnClickListener(this);
				//点击了分享
				share.setOnClickListener(this);
			}else
			{//个人
				phone.setVisibility(View.GONE);
				location.setVisibility(View.GONE);
				if(me.getSid()!=forumNote.getAccSid())
				{
					//点击了私信
					private_message.setVisibility(View.VISIBLE);
					private_message.setOnClickListener(this);
				}else
				{//是自己，则隐藏
					private_message.setVisibility(View.GONE);
				}
			}
			//点击了点赞
			agree.setOnClickListener(this);
			setConcern();
		}else
		{//是自己
			right_img_view.setVisibility(View.GONE);
		}
	}
	private void setConcern()//设置关注
	{
		int subType=Constant.SUBTYPE.SUBTYPE_USR;
		if(forumNote.getLatItude()!=null&&forumNote.getLatItude().doubleValue()>0)
		{
			subType=Constant.SUBTYPE.SUBTYPE_MERCHANT;
		}
		if(AppContext.hasFocusMerchant(forumNote.getAccSid(),subType))
		{//忆关注该商家
			focusTextView.setSelected(true);
			focusTextView.setText(R.string.have_concern);
		}else
		{
			focusTextView.setSelected(false);
			focusTextView.setText(R.string.add_concern);
		}
		focus.setOnClickListener(this);
		//focusTextView.setOnClickListener(this);
	}
	private void setSexImg(Comment comment,ImageView usr_sex_img)
	{
		int sex =comment.getSex();
		if(sex==Constant.SEX.SEX_MAN)
		{
			usr_sex_img.setImageResource(R.drawable.man_selected);
		}else if(sex==Constant.SEX.SEX_WOMAN)
		{
			usr_sex_img.setImageResource(R.drawable.woman_selected);
		}
	}

	private class HolderView {
		public ImageView head;//头像
		public TextView userName;//发贴者名称
		public TextView publishTime;//发布时间
		public ImageView usr_sex_img;//用户性别
		public TextView content;//内容
		public TextView userAddr;//来自小区
		public View usr_info;//含有用户信息的容器
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.usr_info:
		{
			Comment comment=(Comment)v.getTag();
			Account account=new Account();
			account.setSid(comment.getAlongAccSid());
			account.setAccName(comment.getAccName());
			account.setSex(comment.getSex());
			AreaInfo ai=new AreaInfo();
			ai.setSid(comment.getAlongAreaSid());
			ai.setAreaName(comment.getAreaName());
			account.setAreaInfo(ai);
			account.setHeadPicture(comment.getHeadPicture());
			mContext.startActivity(new Intent(mContext, UsrZoneActivity.class).putExtra(Constant.ActivityExtra.ACCOUNT, account));
			break;
		}
		case R.id.comment_count:
		{//点击了评论列表
			NOW_SELECTED=VIEW_TYPE_COMMENT;
			setUpArrow();
			//mSwipeLayout.startRefresh();
			mListView.autoRefresh();
			break;
		}
		case R.id.agree_count:
		{//点击了赞列表
			NOW_SELECTED=VIEW_TYPE_OK;
			setUpArrow();
			//mSwipeLayout.startRefresh();
			mListView.autoRefresh();
			break;
		}
		case R.id.good_more_function:
		{//点击头部的显示更多功能
			//			if(function_list.getVisibility()==View.VISIBLE)
			//			{//原来可见时
			//				function_list.setVisibility(View.GONE);
			//				function_img.setImageResource(R.drawable.more_function);
			//			}else
			//			{
			//				function_list.setVisibility(View.VISIBLE);
			//				function_img.setImageResource(R.drawable.close);
			//			}
			function.showGdMoreFunctionPopupWindow();
			break;
		}
		//点了点赞
		case R.id.gd_agree_ll://更多功能中的点赞
		{
			MeUtil.addCommentPraise(mContext, forumNote.getGoodsSid(), forumNote.getAccSid(),null, new OnPraiseListener() {
				@Override
				public void OnPraiseed(boolean isOk,String message) {
					if(isOk)
					{	function.disMissPopupWindow();
					//MeLifeMainActivity.instance().indexFragment.goodsNumberAdd(forumNote,Constant.MSGTYPE.TYPE_PRAISE);
					Intent intent = new Intent();  
					intent.setAction(Constant.BroadType.ADD_PRAISE_OK);
					intent.putExtra(Constant.ActivityExtra.GOODSSID, forumNote.getGoodsSid());
					mContext.sendBroadcast(intent);
					MeUtil.showToast(mContext,"赞一下成功");
					agree_number = agree_number + 1;
					agree_count.setText(agree_number + "人觉得赞");
					}else
					{	
						function.disMissPopupWindow();
						MeUtil.showToast(mContext,"赞失败:"+message);
					}
				}
			});
			break;
		}
		case R.id.gd_private_message_ll://更多功能中的私信
		{
			if(!AppContext.currentAccount().getIsUsrLogin())
			{
				MeUtil.showToast(mContext,mContext.getString(R.string.need_login_first));
				//MeLifeMainActivity.instance().onMenuSelected(MeLifeMainActivity.instance().my_zone.getIndex());
				return;
			}
			Account account=new Account();
			account.setSid(forumNote.getAccSid());
			account.setAccName(forumNote.getAccName());
			account.setSex(forumNote.getSex());
			AreaInfo ai=new AreaInfo();
			ai.setSid(forumNote.getAlongAreaSid());
			ai.setAreaName(forumNote.getAreaName());
			account.setAreaInfo(ai);
			account.setHeadPicture(forumNote.getHeadPicture());
			mContext.startActivity(new Intent(mContext, PrivateMessageActivity.class).putExtra(Constant.ActivityExtra.ACCOUNT, account));
			function.disMissPopupWindow();
			break;
		}
		case R.id.gd_location_ll://更多功能中的地图位置
		{	
			Merchant merchant=new Merchant();
			merchant.setSid(forumNote.getAccSid());
			merchant.setAlongAreaSid(forumNote.getAlongAreaSid());
			merchant.setAreaName(forumNote.getAccName());
			merchant.setLongItude(forumNote.getLongItude().doubleValue());
			merchant.setLatItude(forumNote.getLatItude().doubleValue());
			merchant.setMerchantName(forumNote.getAccName());
			mContext.startActivity(new Intent(mContext, MerchantMapActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant));
			function.disMissPopupWindow();
			break;
		}
		case R.id.gd_phone_ll://更多功能中的电话联系
		{
			if(NullUtil.isStrNotNull(forumNote.getTelphone()))
			{
				Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + forumNote.getTelphone())); 
				mContext.startActivity(phoneIntent);// 启动 
				function.disMissPopupWindow();
			}else
			{
				((BaseActivity)mContext).showToast(mContext.getString(R.string.have_no_merchant_telphone));
				function.disMissPopupWindow();
			}
			break;
		}
		case R.id.gd_focus_ll://更多功能中的关注
		{
			Merchant mer=new Merchant();
			mer.setSid(forumNote.getAccSid());
			mer.setMerchantName(forumNote.getAccName());
			mer.setAlongAreaSid(forumNote.getAlongAreaSid());
			mer.setAreaName(forumNote.getAreaName());
			if(forumNote.getLatItude()!=null&&forumNote.getLatItude().doubleValue()>0)
			{//说明是商家，因为有经纬度
				mer.setType(Constant.SUBTYPE.SUBTYPE_MERCHANT);
			}else
			{
				mer.setType(Constant.SUBTYPE.SUBTYPE_USR);
			}
			MeUtil.concernMerchant(mContext,mer,new ConcernResultListener() {
				@Override
				public void concernResult(boolean isConcern, boolean result) {
					if(result)
					{
						if(isConcern)
						{
							focusTextView.setSelected(true);
							focusTextView.setText(R.string.have_concern);
						}else
						{
							focusTextView.setSelected(false);
							focusTextView.setText(R.string.add_concern);
						}
					}
				}
			});
			break;
		}
		case R.id.merchant_info_rl://商家信息
		case R.id.merchant_detail_tv://商家详情
		{
			if(forumNote.getLongItude()!=null && forumNote.getLongItude().doubleValue()>0)
			{//是商家
				Merchant merchant=new Merchant();
				merchant.setSid(forumNote.getAccSid());
				merchant.setAlongAreaSid(forumNote.getAlongAreaSid());
				merchant.setAreaName(forumNote.getAccName());
				merchant.setBackPic(forumNote.getHeadPicture());
				merchant.setAreaName(forumNote.getAreaName());
				merchant.setAlongAreaSid(forumNote.getAlongAreaSid());
				merchant.setType(Constant.SUBTYPE.SUBTYPE_MERCHANT);
				mContext.startActivity(new Intent(mContext, UsrShopActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant));
			}else
			{//是个人
				Account account=new Account();
				account.setSid(forumNote.getAccSid());
				account.setAccName(forumNote.getAccName());
				account.setSex(forumNote.getSex());
				AreaInfo ai=new AreaInfo();
				ai.setSid(forumNote.getAlongAreaSid());
				ai.setAreaName(forumNote.getAreaName());
				account.setAreaInfo(ai);
				account.setHeadPicture(forumNote.getHeadPicture());
				mContext.startActivity(new Intent(mContext, UsrZoneActivity.class).putExtra(Constant.ActivityExtra.ACCOUNT, account));
			}

			break;

		}
		case R.id.gd_share_ll:
			//wechatShare(0);//分享到微信好友  
			wechatShare(1);//分享到微信朋友圈 
			function.disMissPopupWindow();
			break;

		default:
			break;
		}
	}
	public static final int VIEW_TYPE_OK=1;//查看类型，是看赞
	public static final int VIEW_TYPE_COMMENT=2;//查看类型，是看评论
	private int NOW_SELECTED=VIEW_TYPE_COMMENT;//默认选择的是评论
	//设置箭头位置
	public void setUpArrow()
	{
		int[] location = new int[2];
		if(NOW_SELECTED==VIEW_TYPE_COMMENT)
		{//是评论
			if(comment_count==null)
			{
				return;
			}
			comment_count.getLocationInWindow(location);
		}else if(NOW_SELECTED==VIEW_TYPE_OK)
		{
			if(agree_count==null)
			{
				return;
			}
			agree_count.getLocationInWindow(location);
		}
		RelativeLayout.LayoutParams  lp=(RelativeLayout.LayoutParams)uparrow.getLayoutParams();
		lp.setMargins(location[0]+10, 1, 0, 0);
		uparrow.setLayoutParams(lp);
	}
	private int pageNo=0;
	public synchronized void loadCommentsAndPraise(final boolean isUp) {
		final Comment header=getList().get(0);
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
		if(isLoading)
		{
			return;
		}

		CommentListReq commentListReq = new CommentListReq();
		commentListReq.setPageNo(pageNo);
		commentListReq.setGoodsSid(forumNote.getGoodsSid());

		RequestBean<CommentListResponse> requestBean = new RequestBean<CommentListResponse>();
		requestBean.setRequestBody(commentListReq);
		requestBean.setResponseBody(CommentListResponse.class);
		if(NOW_SELECTED==VIEW_TYPE_COMMENT)
		{//是评论
			requestBean.setURL(Constant.Requrl.getGoodsCommentList());
		}else if(NOW_SELECTED==VIEW_TYPE_OK)
		{//取点赞
			requestBean.setURL(Constant.Requrl.getGoodsPraiseList());
		}
		isLoading=true;
		MeMessageService.instance().requestServer(requestBean, new UICallback<CommentListResponse>() {
			@Override
			public void onSuccess(CommentListResponse respose) {
				try {
					if(isUp)
					{
						//mSwipeLayout.setRefreshing(false);
						clear();
						getList().add(header);
					}else
					{
						//mSwipeLayout.setLoading(false);
					}
					List<Comment> comments = respose.getList();
					if(comments==null)
					{
						comments=new ArrayList<Comment>();
					}
					if(comments!=null&&comments.size()>0)
					{
						for(Comment comm:comments)
						{
							if(NOW_SELECTED==VIEW_TYPE_COMMENT)
							{//是评论
								comm.setCommentType(Constant.MSGTYPE.TYPE_COMMENT);
							}else if(NOW_SELECTED==VIEW_TYPE_OK)
							{//是点赞
								comm.setCommentType(Constant.MSGTYPE.TYPE_PRAISE);
							}
						}
					}else
					{//无新的数据
						isEnd=true;
						comments.add(null);
					}
					load(comments);
				} catch (Exception e) {
				}
				onLoad();
				isLoading=false;
			}

			@Override
			public void onError(String message) {
				isEnd=true;
				if(isUp)
				{
					//mSwipeLayout.setRefreshing(false);
				}else
				{
					//mSwipeLayout.setLoading(false);
				}
				MeUtil.showToast(mContext,message);
				onLoad();
				isLoading=false;
			}

			@Override
			public void onOffline(String message) {
				isEnd=true;
				if(isUp)
				{
					//mSwipeLayout.setRefreshing(false);
				}else
				{
					//mSwipeLayout.setLoading(false);
				}
				MeUtil.showToast(mContext,message);
				onLoad();
				isLoading=false;
			}
		});
	}
	public void addCommentNumber()
	{
		forumNote.setCommentNumber(forumNote.getCommentNumber()+1);
		int fnumber=forumNote.getCommentNumber();
		if(fnumber>9999)
		{
			comment_count.setText("评论数:"+mContext.getString(R.string.much_more_view_number));
		}else
		{
			comment_count.setText("评论数:"+fnumber);
		}

	}

	@Override
	public void onRefresh() {
		loadCommentsAndPraise(true);
	}

	@Override
	public void onLoadMore() {
		loadCommentsAndPraise(false);
	}
	private Handler handler=new Handler();
	private void onLoad() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				mListView.stopRefresh();
				mListView.stopLoadMore();
				mListView.setRefreshTime(getTime());
			}
		});
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
	}

	/**
	 * 微信分享的方法
	 * @param flag
	 */
	private void wechatShare(int flag){  
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = Constant.Requrl.getGoodsHtmlDesc()+"?sid="+forumNote.getGoodsSid();
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = "[与邻生活]"+forumNote.getGoodsContent();
		msg.description = forumNote.getGoodsContent();

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
		wxapi.sendReq(req);
	} 

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}


	/**
	 * 用于将activity中头部右边的moreFunction中的view传进来
	 */
	public void setMoreFunction(GoodDetailMoreFuncton function,View popContainer,PopupWindow popupWindow,View agree,View share,View location,View phone,View private_message,View focus,TextView focusTextView,View rightImaView){
		this.popupWindow=popupWindow;
		this.function=function;
		this.popContainer=popContainer;
		this.agree=agree;
		this.share=share;
		this.location=location;
		this.phone=phone;
		this.private_message=private_message;
		this.focus=focus;
		this.focusTextView=focusTextView;
		this.right_img_view=rightImaView;
	}

}
