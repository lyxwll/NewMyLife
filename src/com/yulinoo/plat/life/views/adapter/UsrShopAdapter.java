package com.yulinoo.plat.life.views.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.yulinoo.plat.life.bean.LocationPoint;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.bean.ProductInfo;
import com.yulinoo.plat.life.bean.Tag;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.AreaInfoDetailReq;
import com.yulinoo.plat.life.net.reqbean.GoodsListReq;
import com.yulinoo.plat.life.net.reqbean.ShopModifyReq;
import com.yulinoo.plat.life.net.resbean.AreaInfoDetailResponse;
import com.yulinoo.plat.life.net.resbean.ForumNoteResponse;
import com.yulinoo.plat.life.net.resbean.NormalResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.MyTab;
import com.yulinoo.plat.life.ui.widget.MyTab.OnIabListener;
import com.yulinoo.plat.life.ui.widget.Tab;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.DensityUtil;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.MeUtil.ConcernResultListener;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.PictureUtil;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.life.views.activity.BaseActivity;
import com.yulinoo.plat.life.views.activity.CategorySelectActivity;
import com.yulinoo.plat.life.views.activity.MerchantChoiceMapActivity;
import com.yulinoo.plat.life.views.activity.MerchantCommentListActivity;
import com.yulinoo.plat.life.views.activity.MerchantInfroActivity;
import com.yulinoo.plat.life.views.activity.MerchantMapActivity;
import com.yulinoo.plat.life.views.activity.MerchantPraiseListActivity;
import com.yulinoo.plat.life.views.activity.MyLinkedUsrActivity;
import com.yulinoo.plat.life.views.activity.UsrShopActivity;
import com.yulinoo.plat.life.views.activity.UsrShopLeaveMessageActivity;
import com.yulinoo.plat.life.views.activity.ZoneSelectActivity;
import com.yulinoo.plat.melife.R;

import config.AppContext;
//用户的个人空间适配器，包含自己
public class UsrShopAdapter extends YuLinooLoadAdapter<ForumNote> implements OnClickListener,IXListViewListener{

	private LayoutInflater inflater;
	private UsrShopActivity activity;
	private MyApplication myapp;
	private Merchant merchant;
	private ForumNote forumNote;
	private Tab nowTab;

	private boolean isme=false;//是否是自己，如果是自己的话，则可以对自己的信息进行修改

	private ImageView background_img;
	private TextView shop_name;
	private EditText shopchkcont;//店铺签名
	private ImageView usr_header;
	private TextView fans_number;

	private TextView focusMerchant;//关注
	private View leave_message;//留言
	private View share;//分享

	private View phone_number;//电话
	private TextView phone_numberTextView;
	private View position;//位置
	private TextView shop_addr;//店铺地址
	private TextView positionTextView;//地址详情
	private View shopDetail;//详情
	private TextView shopDetailTextView;
	private View nameAndNumber;

	private View myshop_index;//商家首页，即商家详情
	//	private EditText index_merchant_name_et;
	//	private TextView index_shop_area;
	//	private TextView index_shop_position;
	//	private TextView index_shop_category;
	//	private EditText index_shop_addr_et;
	//	private EditText index_shop_telphone_et;
	//	private EditText index_shop_sign_name_et;
	//	private EditText index_shop_desc_et;
	//	private TextView index_edit_my_shop_info;

	//点赞 评论 浏览
	private TextView view_number;//浏览
	private View praseView;
	private TextView prase_number;//点赞数量
	private View commentView;
	private TextView comment_number;//评论数量

	//	private View index_shop_area_rl;
	//	private View index_shop_position_rl;
	//	private View index_shop_category_rl;

	//	private AreaInfo areaInfo;//商家选择了小区
	//	private LocationPoint locationPoint;//商家选择了地图位置
	//	private List<Tag> productInfos;//商家选择了经营范围

	private List<ForumNote> indexList=new ArrayList<ForumNote>();

	private MyTab zoneTab;

	private boolean isEnd=false;
	private boolean isLoading=false;

	private XListView mListView;
	public UsrShopAdapter(UsrShopActivity acti,Merchant acc,XListView mListView,ForumNote forumNote) {
		inflater = LayoutInflater.from(acti);
		this.activity = acti;
		myapp=((MyApplication)activity.getApplicationContext());
		merchant=acc;
		this.forumNote=forumNote;

		Account me=AppContext.currentAccount();
		if(me.getSid()==acc.getSid())
		{//是我本人
			isme=true;
		}
		this.mListView=mListView;
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(this);
	}
	public static interface IViewType {
		int SHOP_HEAD = 0;//头部
		int SHOP_INDEX = 1;//首页
		int SHOP_GOODS = 2;//产品内容
		int SHOP_FOOTER = 3;//尾部无更多数据
		int SHOP_LEAVE_MESSAGE = 4;//留言内容
	}
	@Override
	public int getItemViewType(int position) {
		if(position==0)
		{//头部
			return IViewType.SHOP_HEAD;
		}else 
		{
			if(nowTab!=null)
			{
				if(nowTab.getIndex()==0)
				{//首页
					return IViewType.SHOP_INDEX;
				}else
				{
					ForumNote fn=getItem(position);
					if(fn!=null)
					{
						if(nowTab.isPermissionPersional())
						{//允许个人发布
							return IViewType.SHOP_LEAVE_MESSAGE;
						}else
						{
							return IViewType.SHOP_GOODS;
						}
					}else
					{//为空就表示没有更多数据
						return IViewType.SHOP_FOOTER;
					}
				}
			}else
			{
				return IViewType.SHOP_FOOTER;
			}
		}
	}

	@Override
	public int getViewTypeCount() {
		return 5;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ForumNote item=getItem(position);
		CommonHolderView holder = null;
		if (convertView == null) {
			holder = new CommonHolderView();
			if (position==0) {
				convertView = inflater.inflate(R.layout.included_usr_shop_header, parent,false);
				initHeader(convertView);
			} else {
				if(nowTab.getIndex()==0)
				{//首页
					convertView=myshop_index = inflater.inflate(R.layout.included_usr_shop_index, parent,false);
					//showIndex();
				}else 
				{
					if(item!=null)
					{
						if(nowTab.isPermissionPersional())
						{//允许个人发布
							convertView = inflater.inflate(R.layout.item_merchant_list, parent,false);
						}else
						{
							convertView = inflater.inflate(R.layout.merchant_goods_item, parent,false);
						}
						MeUtil.initWeiboContent(activity, holder, convertView, item);
					}else
					{
						convertView=inflater.inflate(R.layout.included_cont_item_footer, parent,false);
					}
				}
			}
			convertView.setTag(holder);
		} else {
			holder = (CommonHolderView) convertView.getTag();
		}

		if(nowTab!=null&&nowTab.getIndex()>0&&position>0)
		{
			if(item!=null)
			{
				MeUtil.setWeiboContent(activity, holder, convertView, item,true);
			}else
			{
			}
		}
		return convertView;
	}

	public void initHeader(View convertView)
	{
		background_img = (ImageView) convertView.findViewById(R.id.background_img_1);
		usr_header = (ImageView) convertView.findViewById(R.id.usr_header_1);
		shop_name = (TextView) convertView.findViewById(R.id.shop_name_1);
		shopchkcont = (EditText) convertView.findViewById(R.id.shop_sign);//商家签名
		nameAndNumber=convertView.findViewById(R.id.name_and_num_ll);

		fans_number = (TextView)convertView.findViewById(R.id.fans_number_1);
		fans_number.setOnClickListener(this);
		//三个功能
		focusMerchant = (TextView) convertView.findViewById(R.id.focusMerchant);
		leave_message=convertView.findViewById(R.id.leave_message_rl);
		share=convertView.findViewById(R.id.share_rl);
		//下面的三个部分
		phone_number =  convertView.findViewById(R.id.telphone_rl);
		phone_numberTextView=(TextView) convertView.findViewById(R.id.shouji_tv);
		phone_number.setOnClickListener(this);
		position =  convertView.findViewById(R.id.address_rl);
		shop_addr = (TextView) convertView.findViewById(R.id.address_from_where_tv);
		positionTextView=(TextView) convertView.findViewById(R.id.address_address_tv);
		position.setOnClickListener(this);
		shopDetail=convertView.findViewById(R.id.detial_rl);
		shopDetailTextView=(TextView) convertView.findViewById(R.id.introduce_tv);
		shopDetail.setOnClickListener(this);

		//点赞 评论 浏览三个
		view_number = (TextView)convertView.findViewById(R.id.usr_shop_browse_number_tv);
		praseView=convertView.findViewById(R.id.usr_shop_ok_number_rl);
		prase_number=(TextView) convertView.findViewById(R.id.ok_number_tv);
		praseView.setOnClickListener(this);
		commentView=convertView.findViewById(R.id.usr_shop_comment_number_rl);
		comment_number=(TextView) convertView.findViewById(R.id.usr_shop_comment_number_tv);
		commentView.setOnClickListener(this);
		if(isme)
		{	
			shopchkcont.setEnabled(true);
			focusMerchant.setVisibility(View.GONE);
			leave_message.setVisibility(View.GONE);
			share.setVisibility(View.GONE);
			usr_header.setOnClickListener(this);
		}else
		{
			shopchkcont.setEnabled(false);
			setConcern();
			leave_message.setOnClickListener(this);
			share.setOnClickListener(this);
		}

		String headPic=merchant.getLogoPic();
		if(NullUtil.isStrNotNull(headPic))
		{
			MeImageLoader.displayImage(headPic, usr_header, myapp.getHeadIconOption());
		}
		String backPic=merchant.getBackPic();
		if(NullUtil.isStrNotNull(backPic))
		{
			MeImageLoader.displayImage(backPic, background_img, myapp.getWeiIconOption());
		}
		shop_name.setText(merchant.getMerchantName());
		if (merchant.getSignatureName()!=null) {
			shopchkcont.setText(merchant.getSignatureName());
		}else {
			if (isme) {
				shopchkcont.setText("编辑商家签名");
				shopchkcont.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						//跳转到商家infro界面进行修改
						activity.startActivity(new Intent(activity, MerchantInfroActivity.class));
					}
				});
			}else {
				shopchkcont.setText("商家暂无签名");
			}
		}
		int tmpNumber=merchant.getFansNumber();
		//		if(tmpNumber>9999)
		//		{
		//			view_number.setText(activity.getString(R.string.view_number)+activity.getString(R.string.much_more_view_number));
		//		}else
		//		{
		//			view_number.setText(activity.getString(R.string.view_number)+tmpNumber);
		//		}
		//		tmpNumber=merchant.getFansNumber();
		if(tmpNumber>9999)
		{
			fans_number.setText(activity.getString(R.string.fans_number)+activity.getString(R.string.much_more_view_number));
		}else
		{
			fans_number.setText(activity.getString(R.string.fans_number)+tmpNumber);
		}
		fans_number.setOnClickListener(this);

		//下面的三个部分
		shop_addr.setText(merchant.getAreaName());
		positionTextView.setText(merchant.getMerchantAddr());
		if (merchant.getMerchantTelphone()==null||merchant.getMerchantTelphone().equals("")) {
			phone_numberTextView.setText("商家暂无电话");
		}else {
			phone_numberTextView.setText(merchant.getMerchantTelphone());
		}
		if (merchant.getMerchantDesc()==null||merchant.getMerchantDesc().equals("")) {
			shopDetailTextView.setText("商家暂无介绍");
		}else {
			shopDetailTextView.setText(merchant.getMerchantDesc());
		}
		//点赞 评论 浏览三个
		int fanNumber=merchant.getViewNumber();
		if (fanNumber>9999) {
			view_number.setText(activity.getString(R.string.much_more_view_number));
		}else {
			view_number.setText(""+fanNumber);
		}
		prase_number.setText(""+merchant.getOkNumber());
		comment_number.setText(""+merchant.getTotalCommentNum());

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
				UsrShopActivity usactivity=(UsrShopActivity)activity;
				if(nowTab.getIndex()==0)
				{//首页
					mListView.setPullLoadEnable(false);
					clear();
					usactivity.showAddProduct(false,nowTab);
					if(indexList.size()==0)
					{
						indexList.add(null);//只是为了告诉listView有两条数据，并不会实际使用
						indexList.add(null);
					}
					load(indexList);
				}else
				{
					if(isme)
					{
						usactivity.showAddProduct(true,nowTab);
					}else
					{
						if(tb.isPermissionPersional())
						{
							usactivity.showAddProduct(true,nowTab);
						}else
						{
							usactivity.showAddProduct(false,nowTab);
						}
					}
					loadGoods(true);
				}
			}
		});
		//设置背景宽高
		int[] bgsizse=SizeUtil.getAdvertiseSizeNoMargin(activity);
		background_img.getLayoutParams().width=bgsizse[0];
		background_img.getLayoutParams().height=bgsizse[1];
		//设置头像位置,上头像中间与背景边框一致
		int header_size=(int)activity.getResources().getDimension(R.dimen.bigger_header_size);//DensityUtil.dip2px(activity, activity.getResources().getDimension(R.dimen.bigger_header_size));//SizeUtil.usr_zone_header_size(activity);
		int leftMargin=DensityUtil.dip2px(activity, 10);
		int leftMarginNn=DensityUtil.dip2px(activity, 100);
		android.widget.FrameLayout.LayoutParams headerlp=(android.widget.FrameLayout.LayoutParams)usr_header.getLayoutParams();
		android.widget.FrameLayout.LayoutParams nameAndNumberlp=(android.widget.FrameLayout.LayoutParams)nameAndNumber.getLayoutParams();
		nameAndNumberlp.setMargins(leftMarginNn, bgsizse[1]-header_size/2-10, 0, 0);
		headerlp.setMargins(leftMargin, bgsizse[1]-header_size/2, 0, 0);
		showTab();
	}

	//设置关注
	private void setConcern()
	{
		if(AppContext.hasFocusMerchant(merchant.getSid(),Constant.SUBTYPE.SUBTYPE_MERCHANT))
		{//已关注该商家
			focusMerchant.setSelected(true);
			focusMerchant.setText(R.string.have_concern);
		}else
		{
			focusMerchant.setSelected(false);
			focusMerchant.setText(R.string.add_concern);
		}
		focusMerchant.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MeUtil.concernMerchant(activity,merchant,new ConcernResultListener() {
					@Override
					public void concernResult(boolean isConcern, boolean result) {
						if(result)
						{
							//MeLifeMainActivity.instance().myConcernFragment.loadConerns();
							if(isConcern)
							{
								focusMerchant.setSelected(true);
								focusMerchant.setText(R.string.have_concern);
								int tmpNumber=merchant.getFansNumber()+1;
								if(tmpNumber>9999)
								{
									fans_number.setText(activity.getString(R.string.fans_number)+activity.getString(R.string.much_more_view_number));
								}else
								{
									fans_number.setText(activity.getString(R.string.fans_number)+tmpNumber);
								}
							}else
							{
								focusMerchant.setSelected(false);
								focusMerchant.setText(R.string.add_concern);
								int tmpNumber=merchant.getFansNumber()-1;
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

	private Long leaveMessageProductSid=null;

	private void showTab()
	{
		List<Tab> tabs = new ArrayList<Tab>();
		//		Tab tabIndex = new Tab();//主页
		//		tabIndex.setIndex(0);
		//		tabIndex.setName("主页");
		//		tabs.add(tabIndex);
		//		{
		//			Tab tabIndex1 = new Tab();//主页
		//			tabIndex1.setIndex(10);
		//			tabIndex1.setName("主页1");
		//			tabs.add(tabIndex1);
		//			Tab tabIndex2 = new Tab();//主页
		//			tabIndex2.setIndex(20);
		//			tabIndex2.setName("主页2");
		//			tabs.add(tabIndex2);
		//			Tab tabIndex3 = new Tab();//主页
		//			tabIndex3.setIndex(30);
		//			tabIndex3.setName("主页3");
		//			tabs.add(tabIndex3);
		//			Tab tabIndex4 = new Tab();//主页
		//			tabIndex4.setIndex(40);
		//			tabIndex4.setName("主页4");
		//			tabs.add(tabIndex4);
		//		}
		List<ProductInfo> hotFoods = merchant.getProducts();
		if(hotFoods!=null && hotFoods.size()>0)
		{
			int size=hotFoods.size();//主要是要加主页这个固定的页面
			for (int i = 0; i < size; i++) {
				ProductInfo pi=hotFoods.get(i);
				Tab tab = new Tab();
				tab.setProductSid(pi.getSid());
				tab.setName(pi.getProductName());
				tab.setIndex(i+1);
				tab.setSelected(false);
				if (pi.getProductName().equals("留言")) {
					leaveMessageProductSid=pi.getSid();
					continue;
				}
				if(ProductInfo.PERSONAL_CAN_PUBLISH==pi.getPermissionPersional())
				{
					tab.setPermissionPersional(true);
				}else
				{
					tab.setPermissionPersional(false);
				}
				tabs.add(tab);
			}
			tabs.get(0).setSelected(true);//将第二个设为选中状态
		}
		zoneTab.loadZoneTab(tabs);
	}

	//	private void showIndex()
	//	{
	//		index_merchant_name_et=(EditText)myshop_index.findViewById(R.id.index_merchant_name_et) ;
	//		index_shop_area=(TextView)myshop_index.findViewById(R.id.index_shop_area) ;
	//		index_shop_position=(TextView)myshop_index.findViewById(R.id.index_shop_position) ;
	//		index_shop_category=(TextView)myshop_index.findViewById(R.id.index_shop_category) ;
	//		index_shop_addr_et=(EditText)myshop_index.findViewById(R.id.index_shop_addr_et) ;
	//		index_shop_telphone_et=(EditText)myshop_index.findViewById(R.id.index_shop_telphone_et) ;
	//		index_shop_sign_name_et=(EditText)myshop_index.findViewById(R.id.index_shop_sign_name_et) ;
	//		index_shop_desc_et=(EditText)myshop_index.findViewById(R.id.index_shop_desc_et) ;
	//		index_edit_my_shop_info=(TextView)myshop_index.findViewById(R.id.index_edit_my_shop_info);
	//
	//		index_shop_area_rl=myshop_index.findViewById(R.id.index_shop_area_rl);
	//		index_shop_position_rl=myshop_index.findViewById(R.id.index_shop_position_rl);
	//		index_shop_category_rl=myshop_index.findViewById(R.id.index_shop_category_rl);
	//
	//		index_shop_area_rl.setEnabled(false);
	//		index_shop_position_rl.setEnabled(false);
	//		index_shop_category_rl.setEnabled(false);
	//
	//		if(isme)
	//		{
	//			index_edit_my_shop_info.setVisibility(View.VISIBLE);
	//			//			StateListDrawable drawable =MeUtil.createImageSelectStateListDrawable(activity.getResources(),R.drawable.edit_selected,R.drawable.edit_normal,SizeUtil.normal_text_picture_size(activity));
	//			//			index_edit_my_shop_info.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
	//			ColorStateList colors=MeUtil.createColorSelectStateList(activity.getResources().getColor(R.color.font_red), activity.getResources().getColor(R.color.font_blue));
	//			index_edit_my_shop_info.setTextColor(colors);
	//			index_edit_my_shop_info.setText(R.string.modify_info);
	//			index_edit_my_shop_info.setOnClickListener(new OnClickListener() {
	//				@Override
	//				public void onClick(View v) {
	//					//editMyShopInfo();
	//				}
	//			});
	//			index_shop_area_rl.setOnClickListener(new OnClickListener() {
	//				@Override
	//				public void onClick(View v) {
	//					activity.startActivityForResult(new Intent(activity, ZoneSelectActivity.class).putExtra(ZoneSelectActivity.isOpenShop, true), ZoneSelectActivity.AREA_SELECTED);
	//				}
	//			});
	//			index_shop_position_rl.setOnClickListener(new OnClickListener() {
	//				@Override
	//				public void onClick(View v) {
	//					long areaSid=merchant.getAlongAreaSid();
	//					AreaInfoDetailReq areaDetailReq=new AreaInfoDetailReq();
	//					areaDetailReq.setAreaSid(areaSid);
	//					RequestBean<AreaInfoDetailResponse> requestBean = new RequestBean<AreaInfoDetailResponse>();
	//					requestBean.setRequestBody(areaDetailReq);
	//					requestBean.setResponseBody(AreaInfoDetailResponse.class);
	//					requestBean.setURL(Constant.Requrl.getAreaDetail());
	//					ProgressUtil.showProgress(activity, "处理中...");
	//					activity.requestServer(requestBean, new UICallback<AreaInfoDetailResponse>() {
	//						@Override
	//						public void onSuccess(AreaInfoDetailResponse respose) {
	//							try {
	//								ProgressUtil.dissmissProgress();
	//								if(respose.isSuccess())
	//								{
	//									AreaInfo ai=respose.getArea();
	//									activity.startActivityForResult(new Intent(activity, MerchantChoiceMapActivity.class)
	//									.putExtra(MerchantChoiceMapActivity.area, ai)
	//									.putExtra(MerchantChoiceMapActivity.MERCHANT, merchant)
	//									, MerchantChoiceMapActivity.AMAP_SELECTED);
	//								}else
	//								{
	//									activity.showToast(respose.getMsg());
	//								}
	//							} catch (Exception e) {
	//							}
	//						}
	//
	//						@Override
	//						public void onError(String message) {
	//							ProgressUtil.dissmissProgress();
	//							activity.showToast(message);
	//						}
	//
	//						@Override
	//						public void onOffline(String message) {
	//							ProgressUtil.dissmissProgress();
	//							activity.showToast(message);
	//						}
	//					});
	//				}
	//			});
	//
	//			index_shop_category_rl.setOnClickListener(new OnClickListener() {
	//				@Override
	//				public void onClick(View v) {
	//					activity.startActivityForResult(new Intent(activity, CategorySelectActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant), CategorySelectActivity.selectTagOk);
	//				}
	//			});
	//		}else
	//		{
	//			index_edit_my_shop_info.setVisibility(View.GONE);
	//			myshop_index.findViewById(R.id.index_shop_area_right_iv).setVisibility(View.GONE);
	//			myshop_index.findViewById(R.id.index_shop_position_iv).setVisibility(View.GONE);
	//			myshop_index.findViewById(R.id.index_shop_category_iv).setVisibility(View.GONE);
	//			myshop_index.findViewById(R.id.index_shop_position_ll).setVisibility(View.GONE);
	//		}
	//
	//		index_merchant_name_et.setText(merchant.getMerchantName());
	//		index_shop_area.setText(merchant.getAreaName());
	//		if(merchant.getLatItude()>0&&merchant.getLongItude()>0)
	//		{
	//			index_shop_position.setText(activity.getString(R.string.have_choice_position));
	//		}else
	//		{
	//			index_shop_position.setText(activity.getString(R.string.not_choice_position));
	//		}
	//
	//		index_shop_category.setText(merchant.getMerchantTagNameArray());
	//		index_shop_addr_et.setText(merchant.getMerchantAddr());
	//		index_shop_telphone_et.setText(merchant.getMerchantTelphone());
	//		index_shop_sign_name_et.setText(merchant.getSignatureName());
	//		index_shop_desc_et.setText(merchant.getMerchantDesc());
	//	}

	//	private void editMyShopInfo()
	//	{
	//		if(!index_edit_my_shop_info.isSelected())
	//		{
	//			index_edit_my_shop_info.setSelected(true);
	//			index_edit_my_shop_info.setText(R.string.save_info);
	//			areaInfo=null;
	//			locationPoint=null;
	//			productInfos=null;
	//			index_merchant_name_et.setEnabled(true);
	//			index_shop_addr_et.setEnabled(true);
	//			index_shop_telphone_et.setEnabled(true);
	//			index_shop_sign_name_et.setEnabled(true);
	//			index_shop_desc_et.setEnabled(true);
	//
	//			index_shop_area_rl.setEnabled(true);
	//			index_shop_position_rl.setEnabled(true);
	//			index_shop_category_rl.setEnabled(true);
	//		}else if(index_edit_my_shop_info.isSelected())
	//		{
	//			index_edit_my_shop_info.setSelected(false);
	//			index_edit_my_shop_info.setText(R.string.modify_info);
	//			index_merchant_name_et.setEnabled(false);
	//			index_shop_addr_et.setEnabled(false);
	//			index_shop_telphone_et.setEnabled(false);
	//			index_shop_sign_name_et.setEnabled(false);
	//			index_shop_desc_et.setEnabled(false);
	//
	//			index_shop_area_rl.setEnabled(false);
	//			index_shop_position_rl.setEnabled(false);
	//			index_shop_category_rl.setEnabled(false);
	//
	//			String sn=index_merchant_name_et.getText().toString();
	//			if(NullUtil.isStrNull(sn))
	//			{
	//				activity.showToast(activity.getString(R.string.shop_name)+"不允许为空");
	//				return;
	//			}
	//			if(sn.length()>100)
	//			{
	//				activity.showToast(activity.getString(R.string.shop_name)+"不能多于100个字");
	//				return;
	//			}
	//
	//			String tel=index_shop_telphone_et.getText().toString();
	//			if(NullUtil.isStrNull(tel))
	//			{
	//				activity.showToast(activity.getString(R.string.shop_telphone)+"不允许为空");
	//				return;
	//			}
	//
	//			if(tel.length()>20)
	//			{
	//				activity.showToast(activity.getString(R.string.shop_telphone)+"不能多于20个字");
	//				return;
	//			}
	//
	//			//保存商户信息
	//			final ShopModifyReq shopModifyReq = new ShopModifyReq();
	//			shopModifyReq.setMerchantDesc(index_shop_desc_et.getText().toString());
	//			shopModifyReq.setMerchantAddr(index_shop_addr_et.getText().toString());
	//			shopModifyReq.setMerchantName(sn);
	//			shopModifyReq.setMerchantTelphone(tel);
	//			shopModifyReq.setMevalue(AppContext.currentAccount().getMevalue());
	//			shopModifyReq.setSignatureName(index_shop_sign_name_et.getText().toString());
	//			if(areaInfo!=null)
	//			{
	//				shopModifyReq.setAlongAreaSid(areaInfo.getSid());
	//			}
	//			if(locationPoint!=null)
	//			{
	//				shopModifyReq.setLatItude(locationPoint.getLatitude());
	//				shopModifyReq.setLongItude(locationPoint.getLongitude());
	//			}
	//			if (productInfos != null) {
	//				StringBuffer scope = new StringBuffer();
	//				for (Tag productInfo : productInfos) {
	//					if (scope.length() == 0) {
	//						scope.append(productInfo.getAlongCategorySid() + "-" + productInfo.getSid());
	//					} else {
	//						scope.append(",").append(productInfo.getAlongCategorySid() + "-" + productInfo.getSid());
	//					}
	//				}
	//				shopModifyReq.setScope(scope.toString());
	//			}
	//
	//			RequestBean<NormalResponse> requestBean = new RequestBean<NormalResponse>();
	//			requestBean.setRequestBody(shopModifyReq);
	//			requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
	//			requestBean.setResponseBody(NormalResponse.class);
	//			requestBean.setURL(Constant.Requrl.getModifyShop());
	//			ProgressUtil.showProgress(activity, "正在保存...");
	//			activity.requestServer(requestBean, new UICallback<NormalResponse>() {
	//				@Override
	//				public void onSuccess(NormalResponse respose) {
	//					try {
	//						ProgressUtil.dissmissProgress();
	//						if(respose.isSuccess())
	//						{
	//							activity.showToast("修改信息成功");
	//							merchant.setMerchantName(index_merchant_name_et.getText().toString());
	//							if(areaInfo!=null)
	//							{
	//								merchant.setAreaName(areaInfo.getAreaName());
	//								merchant.setAlongAreaSid(areaInfo.getSid());
	//							}
	//							if(locationPoint!=null)
	//							{
	//								merchant.setLatItude(locationPoint.getLatitude());
	//								merchant.setLongItude(locationPoint.getLongitude());
	//							}
	//							if(productInfos!=null)
	//							{
	//								StringBuffer bufTagSids=new StringBuffer();
	//								StringBuffer bufTagName=new StringBuffer();
	//								for (Tag p : productInfos) {
	//									if (bufTagSids.length() == 0) {
	//										bufTagSids.append(p.getSid());
	//										bufTagName.append(p.getTagName());
	//									} else {
	//										bufTagSids.append(",").append(p.getSid());
	//										bufTagName.append(",").append(p.getTagName());
	//									}
	//								}
	//								merchant.setMerchantTagSidArray(bufTagSids.toString());
	//								merchant.setMerchantTagNameArray(bufTagName.toString());
	//							}
	//							merchant.setMerchantTelphone(index_shop_telphone_et.getText().toString());
	//							merchant.setSignatureName(index_shop_sign_name_et.getText().toString());
	//							merchant.setMerchantDesc(index_shop_desc_et.getText().toString());
	//
	//							shop_name.setText(merchant.getMerchantName());
	//							shop_addr.setText(merchant.getAreaName());
	//							shopchkcont.setText(merchant.getSignatureName());
	//						}else
	//						{
	//							activity.showToast(respose.getMsg());
	//						}
	//					} catch (Exception e) {
	//					}
	//				}
	//
	//				@Override
	//				public void onError(String message) {
	//					ProgressUtil.dissmissProgress();
	//					activity.showToast(message);
	//				}
	//
	//				@Override
	//				public void onOffline(String message) {
	//					ProgressUtil.dissmissProgress();
	//					activity.showToast(message);
	//				}
	//			});
	//		}
	//	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.usr_header://用户头像
		{
			//MeLifeMainActivity.instance().myZoneFragment.editPhoto(background_img, 200, 200,Constant.Requrl.getModifyUsrAvarta());
			activity.editPhoto(background_img, 200, 200,Constant.Requrl.getModifyMerchantAvarta());
			break;
		}
		case R.id.share_rl://分享
		{
			MeUtil.showToast(activity, "该功能即将开放");
			break;
		}
		case R.id.leave_message_rl://留言
		{
			if (leaveMessageProductSid!=null) {
				activity.startActivity(new Intent(activity, UsrShopLeaveMessageActivity.class).putExtra("leaveMessageProductSid", leaveMessageProductSid).putExtra(Constant.ActivityExtra.MERCHANT, merchant));
			}else {
				MeUtil.showToast(activity, "该商家没有开放留言功能");
			}
			break;
		}
		case R.id.address_rl://地址
		{
			if(merchant.getLongItude()>0)
			{
				activity.startActivity(new Intent(activity, MerchantMapActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant));
			}
			break;
		}
		case R.id.telphone_rl:{//商家电话
			if(NullUtil.isStrNotNull(merchant.getMerchantTelphone()))
			{
				Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + merchant.getMerchantTelphone())); 
				activity.startActivity(phoneIntent);// 启动 
			}else
			{
				((BaseActivity)activity).showToast(activity.getString(R.string.have_no_merchant_telphone));
			}
			break;
		}
		case R.id.detial_rl://商家的介绍
		{
			activity.startActivity(new Intent(activity, MerchantInfroActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant));
			break;
		}
		case R.id.fans_number_1:
		{
			activity.startActivity(new Intent(activity, MyLinkedUsrActivity.class)
			.putExtra(Constant.ActivityExtra.MERCHANT, merchant)
			.putExtra(Constant.ActivityExtra.LINKED_LOAD_TYPE, Constant.ConcernLoadType.LOAD_FANS)
			.putExtra(Constant.ActivityExtra.LINKEDTITLE, "粉丝"));
			break;
		}
		case R.id.usr_shop_ok_number_rl:{//点赞
			activity.startActivity(new Intent(activity, MerchantPraiseListActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, forumNote));
			break;
		}
		case R.id.usr_shop_comment_number_rl:{//评论
			activity.startActivity(new Intent(activity, MerchantCommentListActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, forumNote));
			break;
		}
		}
	}

	//修改头像
	public void changeHeaderPicture(Bitmap newPhoto)
	{
		usr_header.setImageBitmap(PictureUtil.getRoundedCornerBitmap(newPhoto));
	}
	//	//是否显示红点
	//	public void showRedHot(boolean isShow)
	//	{
	//		if(isShow)
	//		{
	//			redHot.setVisibility(View.VISIBLE);
	//		}else
	//		{
	//			redHot.setVisibility(View.GONE);
	//		}
	//	}
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
				onLoad();
				return;
			}
			pageNo++;
		}
		if(isLoading)
		{
			return;
		}
		GoodsListReq merchantReq = new GoodsListReq();
		merchantReq.setProductSid(nowTab.getProductSid());
		merchantReq.setMerchantSid(merchant.getSid());
		merchantReq.setPageNo(pageNo);

		RequestBean<ForumNoteResponse> requestBean = new RequestBean<ForumNoteResponse>();
		requestBean.setRequestBody(merchantReq);
		requestBean.setResponseBody(ForumNoteResponse.class);
		requestBean.setURL(Constant.Requrl.getMerchantGoodsList());
		isLoading=true;
		MeMessageService.instance().requestServer(requestBean, new UICallback<ForumNoteResponse>() {
			@Override
			public void onSuccess(ForumNoteResponse respose) {
				try {
					loadDataDone(respose,isUp);
				} catch (Exception e) {
					e.printStackTrace();
				}
				isLoading=false;
			}

			@Override
			public void onError(String message) {
				if(isUp)
				{
				}else
				{
				}
				onLoad();
				activity.showToast(message);
				isLoading=false;
			}

			@Override
			public void onOffline(String message) {
				//myListView.onRefreshComplete();
				if(isUp)
				{
					//onTabSelectedListener.closeSwipeLayoutHeader();
					//mSwipeLayout.setRefreshing(false);
				}else
				{
					//onTabSelectedListener.closeSwipeLayoutFooter();
					//mSwipeLayout.setLoading(false);
				}
				onLoad();
				activity.showToast(message);
				isLoading=false;
			}
		});
	}

	private synchronized void loadDataDone(ForumNoteResponse respose,final boolean isUp)
	{
		if(isUp)
		{
			//clear();
			//mSwipeLayout.setRefreshing(false);
		}else
		{
			//mSwipeLayout.setLoading(false);
		}
		onLoad();
		List<ForumNote> listGoods=respose.getList();
		List<ForumNote> forumNotes2=new ArrayList<ForumNote>();
		double latItude = merchant.getLatItude();
		double longItude=merchant.getLongItude();
		java.math.BigDecimal f_latItude = new java.math.BigDecimal(latItude);
		java.math.BigDecimal f_longItude = new java.math.BigDecimal(longItude);
		for(ForumNote forumNote:listGoods){
			forumNote.setLatItude(f_latItude);
			forumNote.setLongItude(f_longItude);
			forumNotes2.add(forumNote);
		}
		if(forumNotes2!=null&&forumNotes2.size()>0)
		{
			if(isUp)
			{//说明是第一次加载，则在前面插入一条空记录，用于显示头部
				clear();
				forumNotes2.add(0, null);
			}
			load(forumNotes2);
		}else
		{//没有新的数据
			isEnd=true;
			if(isUp)
			{
				clear();
				forumNotes2.add(null);//头部
			}
			forumNotes2.add(null);//头部
			load(forumNotes2);
		}
	}

	//	public void categorySelected(List<Tag> listTags)
	//	{
	//		productInfos = listTags;
	//		StringBuffer stringBuffer = new StringBuffer();
	//		if (productInfos != null) {
	//			for (Tag p : productInfos) {
	//				if (stringBuffer.length() == 0) {
	//					stringBuffer.append(p.getTagName());
	//				} else {
	//					stringBuffer.append(",").append(p.getTagName());
	//				}
	//			}
	//		}
	//		index_shop_category.setText(stringBuffer.toString());
	//	}
	//	public void areaSelected(AreaInfo ai)
	//	{
	//		areaInfo=ai;
	//		index_shop_area.setText(areaInfo.getAreaName());
	//	}
	//	public void loactionSelected(LocationPoint lp)
	//	{
	//		index_shop_position.setText(activity.getString(R.string.have_choice_position));
	//		locationPoint=lp;
	//	}

	@Override
	public void onRefresh() {
		if(nowTab!=null)
		{
			if(nowTab.getIndex()>0)
			{//只有产品下拉才有意义
				loadGoods(true);
			}else
			{
				onLoad();
			}
		}else
		{
			onLoad();
		}
	}

	@Override
	public void onLoadMore() {
		if(nowTab!=null)
		{
			if(nowTab.getIndex()>0)
			{//只有产品才有意义
				loadGoods(false);
			}else
			{
				onLoad();
			}
		}else
		{
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
