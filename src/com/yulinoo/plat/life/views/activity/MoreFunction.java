package com.yulinoo.plat.life.views.activity;


import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.Category;
import com.yulinoo.plat.life.bean.ForumInfo;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.bean.ProductInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.ProductListAddReq;
import com.yulinoo.plat.life.net.reqbean.ProductReq;
import com.yulinoo.plat.life.net.resbean.ProductResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.melife.R;

import config.AppContext;


public class MoreFunction implements OnClickListener
{
	private Context context;
	private LayoutInflater inflater;
	private View position;

	public MoreFunction(Context cxt,LayoutInflater infl,View po)
	{
		context=cxt;
		inflater=infl;
		position=po;
	}

	int[] xy = new int[2];
	private PopupWindow lkAreaPopupWindow;
	/***
	 * 获取PopupWindow实例
	 */
	public void showMoreFunctionPopupWindow() {
		View popupWindow_view = inflater.inflate(R.layout.header_more_function_pop, null,false);
		popupWindow_view.findViewById(R.id.publish_note).setOnClickListener(this);
		popupWindow_view.findViewById(R.id.about_us).setOnClickListener(this);
		initOpenShop(popupWindow_view);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		lkAreaPopupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, true);
		// 设置动画效果
		//lkAreaPopupWindow.setAnimationStyle(R.style.AnimationFade);
		//设置点击窗口外边窗口消失    
		lkAreaPopupWindow.setOutsideTouchable(false);     
		// 设置此参数获得焦点，否则无法点击    
		lkAreaPopupWindow.setFocusable(true);
		popupWindow_view.setFocusable(true);//comment by danielinbiti,设置view能够接听事件，标注1   
		popupWindow_view.setFocusableInTouchMode(true); //comment by danielinbiti,设置view能够接听事件 标注2   
		popupWindow_view.setOnKeyListener(new OnKeyListener(){   
			@Override   
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {   
				if (arg1 == KeyEvent.KEYCODE_BACK){
					if(lkAreaPopupWindow != null) {
						lkAreaPopupWindow.dismiss();
					}
				}
				return false;    
			}
		});  

		//点击其他地方消失		
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (lkAreaPopupWindow != null && lkAreaPopupWindow.isShowing()) {
					lkAreaPopupWindow.dismiss();
					lkAreaPopupWindow = null;
				}
				return false;
			}
		});	
		lkAreaPopupWindow.showAsDropDown(position, 0,0);
	}

	private void initOpenShop(View popupWindow_view)
	{
		TextView tv=(TextView)popupWindow_view.findViewById(R.id.publish_goods_tv);
		ImageView iv=(ImageView)popupWindow_view.findViewById(R.id.publish_goods_tv_iv);
		View publish_goods=popupWindow_view.findViewById(R.id.publish_goods);
		final Account account=AppContext.currentAccount();
		if(account!=null)
		{
			if (account.getIsUsrLogin()) {
				if (account.getAccType()==Constant.ACCTYPE.ACCTYPE_NORMAL_MERCHANT) {//如果是商家，显示发布商品
					tv.setText("管理店铺");
					iv.setImageResource(R.drawable.publish_goods_black);
					publish_goods.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Merchant merchant=new Merchant();
							merchant.setSid(AppContext.currentAccount().getSid());
							context.startActivity(new Intent(context,UsrShopActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant));
							if(lkAreaPopupWindow != null) {   
								lkAreaPopupWindow.dismiss();
							}
						}
					});
				} else if (account.getAccType() == Constant.ACCTYPE.ACCTYPE_NORMAL_USR) {
					tv.setText("小区开店");
					iv.setImageResource(R.drawable.open_shop);
					publish_goods.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							context.startActivity(new Intent(context,AreaOpenShopAcitity.class));
							if(lkAreaPopupWindow != null) {   
								lkAreaPopupWindow.dismiss();
							}
						}
					});
				}
			}else
			{
				tv.setText("小区开店");
				iv.setImageResource(R.drawable.open_shop);
				publish_goods.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MeUtil.showToast(context, context.getString(R.string.need_login_first));
						context.startActivity(new Intent(context, LoginActivity.class));
						if(lkAreaPopupWindow != null) {   
							lkAreaPopupWindow.dismiss();
						}
					}
				});
			}
		}else
		{
			tv.setText("小区开店");
			iv.setImageResource(R.drawable.open_shop);
			publish_goods.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MeUtil.showToast(context, context.getString(R.string.need_login_first));
					context.startActivity(new Intent(context, LoginActivity.class));
					if(lkAreaPopupWindow != null) {   
						lkAreaPopupWindow.dismiss();
					}
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.publish_note:
		{
			AreaInfo currentArea=AppContext.currentAreaInfo();
			if(currentArea==null)
			{
				MeUtil.showToast(context, context.getResources().getString(R.string.needconcerarea));
				return;
			}

			Category neighbour_talk_category=AppContext.getNeighbourTalk();
			if(neighbour_talk_category==null)
			{
				MeUtil.showToast(context, "无分类,请联系管理员");
				return;
			}
			List<ForumInfo> lstForums=AppContext.getForumByCategoryId(neighbour_talk_category.getSid());
			if(lstForums.size()==0)
			{
				MeUtil.showToast(context, "无分类下的栏目,请联系管理员");
				return;
			}
			AreaInfo nowArea=AppContext.currentAreaInfo();
			if(nowArea==null)
			{
				MeUtil.showToast(context, context.getString(R.string.needconcerarea));
				return;
			}
			Account account=AppContext.currentAccount();
			if(account==null)
			{
				return;
			}
			if(!account.getIsUsrLogin())
			{
				MeUtil.showToast(context, context.getString(R.string.need_login_first));
				context.startActivity(new Intent(context, LoginActivity.class));
				if(lkAreaPopupWindow != null) {   
					lkAreaPopupWindow.dismiss();
				}
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
			context.startActivity(new Intent(context, PublishGoodsActivity.class)
			.putExtra(PublishGoodsActivity.PUBLIC_TAB, plareq)
			.putExtra(Constant.ActivityExtra.PUBLISH_GOODS_TYPE, Constant.PUBLISH_GOODS_TYPE.PERSONAL_INDEX_PUBLISH));
			lkAreaPopupWindow.dismiss();
			break;
		}
		case R.id.about_us:
		{
			context.startActivity(new Intent(context, About500MeActivity.class));
			lkAreaPopupWindow.dismiss();
			break;
		}
		}
	}	

}