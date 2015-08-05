package com.yulinoo.plat.life.views.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.LoginReq;
import com.yulinoo.plat.life.net.reqbean.RegisterReq;
import com.yulinoo.plat.life.net.reqbean.UsrWeiboListReq;
import com.yulinoo.plat.life.net.resbean.ForumNoteResponse;
import com.yulinoo.plat.life.net.resbean.RandNickNameResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.MeRadioWidget;
import com.yulinoo.plat.life.ui.widget.MyTab;
import com.yulinoo.plat.life.ui.widget.MyTab.OnIabListener;
import com.yulinoo.plat.life.ui.widget.Tab;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.utils.DensityUtil;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.MeUtil.ConcernResultListener;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.PictureUtil;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.life.views.activity.About500MeActivity;
import com.yulinoo.plat.life.views.activity.AreaOpenShopAcitity;
import com.yulinoo.plat.life.views.activity.MeLifeMainActivity;
import com.yulinoo.plat.life.views.activity.MessageCenterActivity;
import com.yulinoo.plat.life.views.activity.ModifyPasswordActivity;
import com.yulinoo.plat.life.views.activity.MyLinkedUsrActivity;
import com.yulinoo.plat.life.views.activity.PrivateMessageActivity;
import com.yulinoo.plat.life.views.activity.SuggestionActivity;
import com.yulinoo.plat.life.views.activity.UserProtocalActivity;
import com.yulinoo.plat.life.views.activity.UsrShopActivity;
import com.yulinoo.plat.life.views.activity.UsrZoneActivity;
import com.yulinoo.plat.melife.R;

import config.AppContext;
//用户的个人空间适配器，包含自己
public class UsrZoneAdapter extends YuLinooLoadAdapter<ForumNote> implements OnClickListener,IXListViewListener{
	private LayoutInflater inflater;
	private UsrZoneActivity activity;
	private MyApplication myapp;
	private Account account;

	private TextView view_number;
	private TextView fans_number;
	private ImageView background_img;
	private ImageView usr_header;
	private TextView nick_name_tv;
	private TextView checktxt_tv;//用户的签名
	private TextView shop_addr;//用户的签名
	private ImageView usr_sex_img;//用户性别
	private ImageView redHot;//红点
	private View message_center;//消息中心
	private TextView open_shop_tv;//小区开店
	private TextView focusAccount;//关注
	private TextView send_message;//私信
	private MyTab zoneTab;
	private Tab nowTab;

	private View my_zone_index = null;//用户主页
	private TextView edit_my_info;//编辑资料
	private MeRadioWidget sex_group;
	private EditText index_nickname;
	private EditText index_userchk;
	private EditText index_addr;

	private View my_zone_setting = null;//用户设置


	private List<ForumNote> indexList=new ArrayList<ForumNote>();
	private List<ForumNote> indexSetting=new ArrayList<ForumNote>();

	private boolean isme=false;//是否是自己，如果是自己的话，则可以对自己的信息进行修改
	//	private SwipeRefreshLayout mSwipeLayout;
	//	private MeNormalListView mListView;
	private XListView mListView;
	private boolean isEnd=false;
	private boolean rh_isShowing=false;
	
	public UsrZoneAdapter(UsrZoneActivity activity,Account acc,XListView mListView,boolean rh_isShowing) {
		inflater = LayoutInflater.from(activity);
		this.activity = activity;
		myapp=((MyApplication)activity.getApplicationContext());
		account=acc;
		Account me=AppContext.currentAccount();
		if(me.getSid().longValue()==acc.getSid().longValue())
		{//是我本人
			isme=true;
		}
		//this.onTabSelectedListener=onTabSelectedListener;
		this.mListView=mListView;
		this.rh_isShowing=rh_isShowing;
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(this);
	}

	public static interface IViewType {//返回的这个数字值与视图的容器的索引相对应
		int MYZONE_HEAD = 0;//头部
		int MYZONE_INDEX = 1;//首页
		int MYZONE_CONT = 2;//内容
		int MYZONE_FOOTER = 3;//无更多数据时的尾部
		int MYZONE_SETTING = 4;//设置
	}
	@Override
	public int getItemViewType(int position) {
		if(position==0)
		{//头部
			return IViewType.MYZONE_HEAD;
		}else 
		{
			if(nowTab!=null)
			{
				if(nowTab.getIndex()==1)
				{//首页
					return IViewType.MYZONE_INDEX;
				}else if(nowTab.getIndex()==4)
				{//设置
					return IViewType.MYZONE_SETTING;
				}else{//内容或者尾部
					ForumNote fn=getItem(position);
					if(fn!=null)
					{
						return IViewType.MYZONE_CONT;
					}else
					{
						return IViewType.MYZONE_FOOTER;
					}
				}
			}else
			{
				return IViewType.MYZONE_INDEX;
			}
		}
	}

	@Override
	public int getViewTypeCount() {
		if(isme)
		{//是我自己，则多一个设置出来
			return 5;
		}else
		{
			return 4;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ForumNote item=getItem(position);
		CommonHolderView holder = null;
		if (convertView == null) {
			holder = new CommonHolderView();
			if (position==0) {
				convertView = inflater.inflate(R.layout.included_usr_zone_header, parent,false);
				initHeader(convertView);
			} else {
				if(nowTab!=null)
				{
					if(nowTab.getIndex()==1)
					{//首页
						convertView=my_zone_index = inflater.inflate(R.layout.included_usr_zone_index, parent,false);
						showIndex();
					}else  if(nowTab.getIndex()==4)
					{//设置
						convertView=my_zone_setting=inflater.inflate(R.layout.included_usr_zone_setting, parent,false);
						showSetting();
					}else{//内容页
						if(item!=null)
						{
							convertView=inflater.inflate(R.layout.merchant_goods_item, parent,false);
							MeUtil.initWeiboContent(activity, holder, convertView, item);
						}else
						{
							convertView=inflater.inflate(R.layout.included_cont_item_footer, parent,false);
						}
					}
				}else
				{
					convertView=my_zone_index = inflater.inflate(R.layout.included_usr_zone_index, parent,false);
					showIndex();
				}
			}
			convertView.setTag(holder);
		} else {
			holder = (CommonHolderView) convertView.getTag();
		}
		if(position>0&&nowTab!=null)
		{
			if(nowTab.getIndex()==2&&position>0)
			{
				if(item!=null)
				{
					MeUtil.setWeiboContent(activity, holder, convertView, item,true);
				}else
				{
				}
			}
		}

		return convertView;
	}


	public void initHeader(View convertView)
	{
		view_number=(TextView)convertView.findViewById(R.id.view_number);
		fans_number=(TextView)convertView.findViewById(R.id.fans_number);
		background_img = (ImageView) convertView.findViewById(R.id.background_img);
		usr_header = (ImageView) convertView.findViewById(R.id.usr_header);
		checktxt_tv = (TextView)convertView.findViewById(R.id.checktxt);
		nick_name_tv = (TextView) convertView.findViewById(R.id.nick_name);
		shop_addr = (TextView) convertView.findViewById(R.id.shop_addr);
		usr_sex_img = (ImageView) convertView.findViewById(R.id.usr_sex);

		redHot = (ImageView)convertView.findViewById(R.id.redHot);
		if (rh_isShowing==true) {
			redHot.setVisibility(View.VISIBLE);
		}
		
		message_center = convertView.findViewById(R.id.message_center);
		open_shop_tv = (TextView) convertView.findViewById(R.id.open_shop_tv);
		focusAccount=(TextView)convertView.findViewById(R.id.focusAccount);
		send_message=(TextView)convertView.findViewById(R.id.send_message);
		if(isme)
		{//是我本人则显示消息中心
			message_center.setVisibility(View.VISIBLE);
			message_center.setOnClickListener(this);
			open_shop_tv.setVisibility(View.VISIBLE);
			setOpenShop();//设置是否是小区开店还是我的商铺
			focusAccount.setVisibility(View.GONE);
			send_message.setVisibility(View.GONE);
			usr_header.setOnClickListener(this);
		}else
		{
			message_center.setVisibility(View.GONE);
			open_shop_tv.setVisibility(View.GONE);
			focusAccount.setVisibility(View.VISIBLE);
			setConcern();//设置关注状态
			if(account.getSex()!=null)
			{
				if(account.getSex().intValue()==Constant.SEX.SEX_WOMAN)
				{
					send_message.setText(activity.getString(R.string.private_message_her));
				}else
				{
					send_message.setText(activity.getString(R.string.private_message_him));
				}
			}else{
				send_message.setText(activity.getString(R.string.private_message_him));
			}
			send_message.setVisibility(View.VISIBLE);
			send_message.setOnClickListener(this);
		}

		zoneTab = (MyTab) convertView.findViewById(R.id.mytab);
		zoneTab.setOnCheckListener(new OnIabListener() {
			@Override
			public void onCheckedChanged(Tab tb) {
				if(nowTab!=null)
				{
					if(nowTab.getIndex()==tb.getIndex())
					{
						return;
					}
				}
				nowTab = tb;
				if(nowTab.getIndex()==1||nowTab.getIndex()==4)
				{//首页或者设置
					mListView.setPullLoadEnable(false);
					clear();
					if(indexList.size()==0)
					{
						indexList.add(null);//只是为了告诉listview有两条数据，并不会实际使用
						indexList.add(null);
					}
					load(indexList);
				}else if(nowTab.getIndex()==2)
				{//是发布
					loadGoods(true);
				}/*else if(nowTab.getIndex()==4)
				{//是设置
					clear();
					if(indexList.size()==0)
					{
						indexList.add(null);//只是为了告诉listview有两条数据，并不会实际使用
						indexList.add(null);
					}
					load(indexSetting);
				}*/
			}
		});

		//设置背景宽高
		int[] bgsizse=SizeUtil.background_size(activity);
		background_img.getLayoutParams().width=bgsizse[0];
		background_img.getLayoutParams().height=bgsizse[1];
		//设置头像位置,上头像中间与背景边框一致
		int header_size=(int)activity.getResources().getDimension(R.dimen.bigger_header_size);//DensityUtil.dip2px(activity, activity.getResources().getDimension(R.dimen.bigger_header_size));//SizeUtil.usr_zone_header_size(activity);
		int leftMargin=DensityUtil.dip2px(activity, 10);
		android.widget.FrameLayout.LayoutParams headerlp=(android.widget.FrameLayout.LayoutParams)usr_header.getLayoutParams();
		headerlp.setMargins(leftMargin, bgsizse[1]-header_size/2, 0, 0);

		//int left_width=header_size+20;
		//((android.widget.FrameLayout.LayoutParams)convertView.findViewById(R.id.name_and_num_ll).getLayoutParams()).setMargins(left_width, 0, 0, 0);
		//LinearLayout addr_and_sign_ll=(LinearLayout)convertView.findViewById(R.id.addr_and_sign_ll);
		//addr_and_sign_ll.setPadding(left_width, 0, 0, 0);

		int tmpNumber=account.getViewNumber();
		if(tmpNumber>9999)
		{
			view_number.setText(activity.getString(R.string.view_number)+activity.getString(R.string.much_more_view_number));
		}else
		{
			view_number.setText(activity.getString(R.string.view_number)+account.getViewNumber());
		}
		tmpNumber=account.getFansNumber();
		if(tmpNumber>9999)
		{
			fans_number.setText(activity.getString(R.string.fans_number)+activity.getString(R.string.much_more_view_number));
		}else
		{
			fans_number.setText(activity.getString(R.string.fans_number)+tmpNumber);
		}
		fans_number.setOnClickListener(this);

		if(NullUtil.isStrNotNull(account.getBackground()))
		{
			MeImageLoader.displayImage(account.getBackground(), background_img, myapp.getWeiIconOption());
		}

		if(NullUtil.isStrNotNull(account.getHeadPicture()))
		{
			MeImageLoader.displayImage(account.getHeadPicture(), usr_header, myapp.getHeadIconOption());
		}else
		{
			Integer sex=account.getSex();
			if(sex!=null)
			{
				if(Constant.SEX.SEX_MAN==sex.intValue())
				{
					usr_header.setImageResource(R.drawable.man_selected);
				}else if(Constant.SEX.SEX_WOMAN==sex.intValue())
				{
					usr_header.setImageResource(R.drawable.woman_selected);
				}
			}
		}

		nick_name_tv.setText(account.getAccName());
		checktxt_tv.setText(account.getSignatureName());

		AreaInfo uarea=account.getAreaInfo();
		if(uarea!=null)
		{
			shop_addr.setText(uarea.getAreaName());
		}else
		{
			AreaInfo ai=AppContext.nearByArea();
			if(ai!=null)
			{
				shop_addr.setText(ai.getAreaName());
			}else
			{
				shop_addr.setText("暂未关注任何小区");
			}
		}

		setSexImg();//设置用户性别
		showMyZoneTab();
	}
	//设置关注
	private void setConcern()
	{
		if(AppContext.hasFocusMerchant(account.getSid(),Constant.SUBTYPE.SUBTYPE_USR))
		{//忆关注该商家
			focusAccount.setSelected(true);
			focusAccount.setText(R.string.have_concern);
		}else
		{
			focusAccount.setSelected(false);
			focusAccount.setText(R.string.add_concern);
		}
		focusAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Merchant merchant=new Merchant();
				merchant.setSid(account.getSid());
				merchant.setType(Constant.SUBTYPE.SUBTYPE_USR);
				merchant.setAlongAreaSid(account.getAreaInfo().getSid());
				merchant.setAreaName(account.getAreaInfo().getAreaName());
				MeUtil.concernMerchant(activity,merchant,new ConcernResultListener() {
					@Override
					public void concernResult(boolean isConcern, boolean result) {
						if(result)
						{
							//MeLifeMainActivity.instance().myConcernFragment.loadConerns();
							if(isConcern)
							{
								focusAccount.setSelected(true);
								focusAccount.setText(R.string.have_concern);
								int tmpNumber=account.getFansNumber()+1;
								if(tmpNumber>9999)
								{
									fans_number.setText(activity.getString(R.string.fans_number)+activity.getString(R.string.much_more_view_number));
								}else
								{
									fans_number.setText(activity.getString(R.string.fans_number)+tmpNumber);
								}
							}else
							{
								focusAccount.setSelected(false);
								focusAccount.setText(R.string.add_concern);
								int tmpNumber=account.getFansNumber()-1;
								if(tmpNumber>9999)
								{
									fans_number.setText(activity.getString(R.string.fans_number)+activity.getString(R.string.much_more_view_number));
								}else
								{
									fans_number.setText(activity.getString(R.string.fans_number)+tmpNumber);
								}
							}
						}
					}
				});
			}
		});
	}
	private void setSexImg()
	{
		if(account.getSex()!=null)
		{
			int sex =account.getSex();
			if(sex==Constant.SEX.SEX_MAN)
			{
				usr_sex_img.setImageResource(R.drawable.man_selected);
			}else if(sex==Constant.SEX.SEX_WOMAN)
			{
				usr_sex_img.setImageResource(R.drawable.woman_selected);
			}
		}else
		{
			usr_sex_img.setImageResource(R.drawable.man_selected);
		}
		
	}
	public void setOpenShop() {
		if (AppContext.currentAccount().getIsUsrLogin()) {
			if (AppContext.currentAccount().getAccType()==Constant.ACCTYPE.ACCTYPE_NORMAL_MERCHANT) {
				open_shop_tv.setText("管理店铺");
				open_shop_tv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Merchant merchant=new Merchant();
						merchant.setSid(AppContext.currentAccount().getSid());
						activity.startActivity(new Intent(activity,UsrShopActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant));
					}
				});
			} else if (AppContext.currentAccount().getAccType() == Constant.ACCTYPE.ACCTYPE_NORMAL_USR) {
				open_shop_tv.setText("小区开店");
				open_shop_tv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						activity.startActivity(new Intent(activity,AreaOpenShopAcitity.class));
					}
				});
			}
		}
	}
	private void showMyZoneTab()
	{
		List<Tab> tabs = new ArrayList<Tab>();
		Tab tabIndex = new Tab();//主页
		tabIndex.setIndex(1);
		tabIndex.setName("主页");
		tabIndex.setSelected(false);
		tabs.add(tabIndex);

		Tab tabPublish = new Tab();//发布
		tabPublish.setIndex(2);
		tabPublish.setName("发布");
		tabs.add(tabPublish);
		tabPublish.setSelected(true);

		if(isme)
		{
			Tab tabSetting = new Tab();//发布
			tabSetting.setIndex(4);
			tabSetting.setName("设置");
			tabs.add(tabSetting);
		}
		zoneTab.loadZoneTab(tabs);
	}

	private void showIndex()
	{
		edit_my_info=(TextView)my_zone_index.findViewById(R.id.edit_my_info);
		sex_group=(MeRadioWidget)my_zone_index.findViewById(R.id.sex_group);
		sex_group.setEnabled(false);
		MeUtil.setSexGroup(activity, sex_group,account.getSex());
		index_nickname=(EditText)my_zone_index.findViewById(R.id.index_nickname);
		index_nickname.setText(account.getAccName());
		index_userchk=(EditText)my_zone_index.findViewById(R.id.index_userchk);
		index_userchk.setText(account.getSignatureName());
		index_addr=(EditText)my_zone_index.findViewById(R.id.index_addr);
		index_addr.setText(account.getAddress());
		ImageView srand_nickname=(ImageView) my_zone_index.findViewById(R.id.srand_nickname);

		if(isme)
		{
			edit_my_info.setVisibility(View.VISIBLE);
			//			StateListDrawable drawable =MeUtil.createImageSelectStateListDrawable(activity.getResources(),R.drawable.edit_selected,R.drawable.edit_normal,SizeUtil.normal_text_picture_size(activity));
			//			edit_my_info.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
			ColorStateList colors=MeUtil.createColorSelectStateList(activity.getResources().getColor(R.color.font_red), activity.getResources().getColor(R.color.font_blue));
			edit_my_info.setTextColor(colors);
			edit_my_info.setText(R.string.modify_info);
			edit_my_info.setOnClickListener(this);
			srand_nickname.setVisibility(View.VISIBLE);
			srand_nickname.setOnClickListener(this);
		}else
		{
			edit_my_info.setVisibility(View.GONE);
			srand_nickname.setVisibility(View.GONE);
		}
	}

	private void showSetting()
	{
		my_zone_setting.findViewById(R.id.surgestion).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.startActivity(new Intent(activity, SuggestionActivity.class)
				.putExtra(Constant.ActivityExtra.SUGGESTION_TYPE, Constant.SuggestionType.SUGGESTION));
			}
		});
		my_zone_setting.findViewById(R.id.user_protocal).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						activity.startActivity(new Intent(activity,	UserProtocalActivity.class));
					}
				});
		my_zone_setting.findViewById(R.id.about_500me).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						activity.startActivity(new Intent(activity,	About500MeActivity.class));
					}
				});
		my_zone_setting.findViewById(R.id.modify_pwd_rl).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						activity.startActivity(new Intent(activity,	ModifyPasswordActivity.class));
					}
				});
		my_zone_setting.findViewById(R.id.usr_logout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						//AppContext.changeCurrentAccount(activity, null);
						Account currAccount=AppContext.currentAccount();
						if(currAccount!=null&&!currAccount.getIsTemp()&&currAccount.getIsUsrLogin())
						{
							currAccount.setIsUsrLogin(false);
							currAccount.save();
							AppContext.setCurrentAccountNull();
							AppContext.setCurrentAreaInfo(null);
						}

						Intent intent = new Intent();  
						intent.setAction(Constant.BroadType.SUBSCRIBE_READED);  
						activity.sendBroadcast(intent);

						if(MeLifeMainActivity.isInstanciated())
						{
							//MeLifeMainActivity.instance().updateUsrHeader();
						}
						activity.finish();

					}
				});		
	}


	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.srand_nickname:
		{//取随机昵称
			loadSrandNickName();
			break;
		}
		case R.id.edit_my_info:
		{//编辑资料
			editMyInfo();
			break;
		}case R.id.usr_header:
		{
			//MeLifeMainActivity.instance().myZoneFragment.editPhoto(background_img, 200, 200,Constant.Requrl.getModifyUsrAvarta());
			activity.editPhoto(background_img, 200, 200,Constant.Requrl.getModifyUsrAvarta());
			break;
		}
		case R.id.message_center:
		{
			activity.startActivity(new Intent(activity, MessageCenterActivity.class));
			break;
		}
		case R.id.send_message:
		{//发私信
			if(!AppContext.currentAccount().getIsUsrLogin())
			{
				MeUtil.showToast(activity,activity.getString(R.string.need_login_first));
				//MeLifeMainActivity.instance().onMenuSelected(MeLifeMainActivity.instance().my_zone.getIndex());
				return;
			}
			activity.startActivity(new Intent(activity, PrivateMessageActivity.class).putExtra(Constant.ActivityExtra.ACCOUNT, account));
			break;
		}
		case R.id.fans_number:
		{
			Merchant merchant=new Merchant();
			merchant.setSid(account.getSid());
			activity.startActivity(new Intent(activity, MyLinkedUsrActivity.class)
			.putExtra(Constant.ActivityExtra.MERCHANT, merchant)
			.putExtra(Constant.ActivityExtra.LINKED_LOAD_TYPE, Constant.ConcernLoadType.LOAD_FANS)
			.putExtra(Constant.ActivityExtra.LINKEDTITLE, "粉丝"));
			break;
		}
		}
	}
	//取随机昵称
	private void loadSrandNickName()
	{
		if(!edit_my_info.isSelected())
		{
			return;
		}
		LoginReq loginReq = new LoginReq();
		RequestBean<RandNickNameResponse> requestBean = new RequestBean<RandNickNameResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(loginReq);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(RandNickNameResponse.class);
		requestBean.setURL(Constant.Requrl.getRandName());
		ProgressUtil.showProgress(activity, "正在获取...");
		MeMessageService.instance().requestServer(requestBean, new UICallback<RandNickNameResponse>() {

			@Override
			public void onSuccess(RandNickNameResponse respose) {
				try {
					ProgressUtil.dissmissProgress();
					if(!respose.isSuccess()) {
						MeUtil.showToast(activity,respose.getMsg());
						return;
					}
					String nickname=respose.getNickName();
					index_nickname.setText(nickname);
				} catch (Exception e) {
				}
			}

			@Override
			public void onError(String message) {
				ProgressUtil.dissmissProgress();
				MeUtil.showToast(activity, message);
			}

			@Override
			public void onOffline(String message) {
				ProgressUtil.dissmissProgress();
				MeUtil.showToast(activity, message);
			}
		});
	}
	private void editMyInfo()
	{
		if(!edit_my_info.isSelected())
		{
			edit_my_info.setSelected(true);
			edit_my_info.setText(R.string.save_info);
			index_nickname.setEnabled(true);
			index_userchk.setEnabled(true);
			index_addr.setEnabled(true);
			sex_group.setEnabled(true);
		}else
		{//保存
			edit_my_info.setSelected(false);
			edit_my_info.setText(R.string.modify_info);
			index_nickname.setEnabled(false);
			index_userchk.setEnabled(false);
			index_addr.setEnabled(false);
			sex_group.setEnabled(false);

			final String strNickName=index_nickname.getText().toString();
			if(NullUtil.isStrNull(strNickName))
			{
				MeUtil.showToast(activity,"用户昵称不允许为空！");
				return;
			}
			if(strNickName.length()>20)
			{
				MeUtil.showToast(activity,"用户昵称过长，请重新输入");
				return;
			}
			final String strChk=index_userchk.getText().toString();
			if(NullUtil.isStrNotNull(strChk))
			{
				if(strChk.length()>50)
				{
					MeUtil.showToast(activity,"签名内容过长，请少于50字");
					return;
				}
			}
			final String strAddr=index_addr.getText().toString();
			if(NullUtil.isStrNotNull(strAddr))
			{
				if(strAddr.length()>100)
				{
					MeUtil.showToast(activity,"地址内容过长，请少于100字");
					return;
				}
			}

			//保存
			RegisterReq registerReq=new RegisterReq();
			registerReq.setMevalue(AppContext.currentAccount().getMevalue());
			registerReq.setSex(sex_group.getNowRadio().getIndex());
			registerReq.setSignatureName(strChk);
			registerReq.setAccName(strNickName);
			registerReq.setAddress(strAddr);
			RequestBean<RandNickNameResponse> requestBean = new RequestBean<RandNickNameResponse>();
			requestBean.setHttpMethod(HTTP_METHOD.POST);
			requestBean.setRequestBody(registerReq);
			requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
			requestBean.setResponseBody(RandNickNameResponse.class);
			requestBean.setURL(Constant.Requrl.getModifyAccount());
			ProgressUtil.showProgress(activity, "正在保存...");
			MeMessageService.instance().requestServer(requestBean, new UICallback<RandNickNameResponse>() {

				@Override
				public void onSuccess(RandNickNameResponse respose) {
					try {
						ProgressUtil.dissmissProgress();
						if(!respose.isSuccess()) {
							MeUtil.showToast(activity,respose.getMsg());
							return;
						}
						MeUtil.showToast(activity,"修改成功");
						nick_name_tv.setText(strNickName);
						checktxt_tv.setText(strChk);
						Account account=AppContext.currentAccount();
						account.setAccName(strNickName);
						if(NullUtil.isStrNotNull(strChk))
						{
							account.setSignatureName(strChk);
						}
						account.setSex(sex_group.getNowRadio().getIndex());
						account.save();
						setSexImg();
					} catch (Exception e) {
					}
				}

				@Override
				public void onError(String message) {
					ProgressUtil.dissmissProgress();
					MeUtil.showToast(activity, message);
				}

				@Override
				public void onOffline(String message) {
					ProgressUtil.dissmissProgress();
					MeUtil.showToast(activity, message);
				}
			});
		}
	}




	//修改头像
	public void changeHeaderPicture(Bitmap newPhoto)
	{
		usr_header.setImageBitmap(PictureUtil.getRoundedCornerBitmap(newPhoto));
	}
	//是否显示红点
	public void showRedHot(boolean isShow)
	{
		if(redHot!=null)
		{
			if(isShow)
			{
				redHot.setVisibility(View.VISIBLE);
			}else
			{
				redHot.setVisibility(View.GONE);
			}
		}
	}
	
	int pageNo=0;
	//加载商品，参数表示为是示向上拉取数据
	private synchronized void loadGoods(final boolean isUp) {
		if(isUp)
		{
			isEnd=false;
			pageNo=0;
		}else{
			if(isEnd)
			{
				//mSwipeLayout.setLoading(false);
				onLoad();
				return;
			}
			pageNo++;
		}
//		if(isLoading)
//		{
//			return;
//		}

		UsrWeiboListReq usrWeiboReq = new UsrWeiboListReq();
		usrWeiboReq.setAccSid(account.getSid());
		usrWeiboReq.setPageNo(pageNo);
		RequestBean<ForumNoteResponse> requestBean = new RequestBean<ForumNoteResponse>();
		requestBean.setRequestBody(usrWeiboReq);
		requestBean.setResponseBody(ForumNoteResponse.class);
		requestBean.setURL(Constant.Requrl.getWeiBoList());
		//isLoading=true;
		MeMessageService.instance().requestServer(requestBean, new UICallback<ForumNoteResponse>() {
			@Override
			public void onSuccess(ForumNoteResponse respose) {
				try {
					loadDataDone(respose,isUp);
				} catch (Exception e) {
				}
				//isLoading=false;
			}

			@Override
			public void onError(String message) {
				//myListView.onRefreshComplete();
				//				if(isUp)
				//				{
				//					//onTabSelectedListener.closeSwipeLayoutHeader();
				//					mSwipeLayout.setRefreshing(false);
				//				}else
				//				{
				//					//onTabSelectedListener.closeSwipeLayoutFooter();
				//					mSwipeLayout.setLoading(false);
				//				}
				onLoad();
				MeUtil.showToast(activity, message);
				//isLoading=false;
			}

			@Override
			public void onOffline(String message) {
				//myListView.onRefreshComplete();
				//				if(isUp)
				//				{
				//					//onTabSelectedListener.closeSwipeLayoutHeader();
				//					mSwipeLayout.setRefreshing(false);
				//				}else
				//				{
				//					//onTabSelectedListener.closeSwipeLayoutFooter();
				//					mSwipeLayout.setLoading(false);
				//				}
				onLoad();
				MeUtil.showToast(activity, message);
				//isLoading=false;
			}
		});
	}
	//必然是发布的数据获取结果
	private synchronized void loadDataDone(ForumNoteResponse respose,final boolean isUp)
	{
		if(isUp)
		{
			clear();
			//mSwipeLayout.setRefreshing(false);
		}else
		{
			//mSwipeLayout.setLoading(false);
		}
		onLoad();
		List<ForumNote> listGoods=respose.getList();
		if(listGoods!=null&&listGoods.size()>0)
		{
			if(isUp)
			{//说明是第一次加载，则在前面插入一条空记录，用于显示头部
				listGoods.add(0, null);
			}
			load(listGoods);
		}else
		{//没有新的数据
			isEnd=true;
			if(isUp)
			{//说明是第一次加载
				listGoods.add(null);//头部
			}
			listGoods.add(null);
			load(listGoods);
		}
	}

	@Override
	public void onRefresh() {
		if(nowTab!=null)
		{
			if(nowTab.getIndex()==2)
			{//只有发布的才有下拉的意义
				loadGoods(true);
			}else
			{
				//mSwipeLayout.setRefreshing(false);
				onLoad();
			}
		}else
		{
			//mSwipeLayout.setRefreshing(false);
			onLoad();
		}
	}

	@Override
	public void onLoadMore() {
		if(nowTab!=null)
		{
			if(nowTab.getIndex()==2)
			{//只有发布的才有下拉的意义
				loadGoods(false);
			}else
			{
				//mSwipeLayout.setLoading(false);
				onLoad();
			}
		}else
		{
			//mSwipeLayout.setLoading(false);
			onLoad();
		}
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
