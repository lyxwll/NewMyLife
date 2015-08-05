package com.yulinoo.plat.life.views.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.ConcernNumber;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.FaceConversionUtil;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.views.activity.UsrShopActivity;
import com.yulinoo.plat.life.views.activity.UsrZoneActivity;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class MyLinkedUsrAdapter extends YuLinooLoadAdapter<Merchant>{
	private LayoutInflater inflater;
	private Context mContext;
	private MyApplication myapp;
	private List<ConcernNumber> listCnumber;
	public MyLinkedUsrAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mContext=context;
		myapp=((MyApplication)mContext.getApplicationContext());
		Account account=AppContext.currentAccount();
		if(account!=null)
		{
			listCnumber=ConcernNumber.getUsrConcernNumber(account.getSid());
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		final Merchant item = getItem(position);
		if (convertView == null) {
			holder = new HolderView();
			convertView = inflater.inflate(R.layout.item_myconcern_cont, parent,false);
			holder.myconcern_icon = (ImageView) convertView.findViewById(R.id.myconcern_icon);
			holder.myconcern_title = (TextView) convertView.findViewById(R.id.myconcern_title);
			holder.usr_redHot = convertView.findViewById(R.id.usr_redHot);
			holder.areaName = (TextView) convertView.findViewById(R.id.areaName);
			holder.lastCont = (TextView) convertView.findViewById(R.id.lastCont);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		if(NullUtil.isStrNotNull(item.getLogoPic()))
		{
			MeImageLoader.displayImage(item.getLogoPic(), holder.myconcern_icon, myapp.getHeadIconOption());
		}else
		{
			if(item.getLatItude()>0)
			{//是商家
				holder.myconcern_icon.setImageResource(R.drawable.merchant_logo);
			}else
			{//是个人
				if(Constant.SEX.SEX_WOMAN==item.getSex())
				{
					holder.myconcern_icon.setImageResource(R.drawable.woman_selected);
				}else
				{
					holder.myconcern_icon.setImageResource(R.drawable.man_selected);
				}
			}
			
		}

		holder.myconcern_title.setText(item.getMerchantName());
		
		holder.lastCont.setText(null);
		if(NullUtil.isStrNotNull(item.getLastCont()))
		{
			SpannableString spannableString = FaceConversionUtil.getInstance().getExpressionString(mContext, item.getLastCont());
			holder.lastCont.append(spannableString);
		}
		holder.areaName.setText(mContext.getString(R.string.come_from)+item.getAreaName());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(item.getLongItude()>0&&item.getLatItude()>0)
				{
					showMerchant(item,(HolderView)v.getTag());
				}else
				{
					showUsrZone(item,(HolderView)v.getTag());
				}
			}
		});
		if(listCnumber!=null&&listCnumber.size()>0)
		{
			boolean showRedHot=false;
			for(ConcernNumber cn:listCnumber)
			{
				long caccsid=cn.getConcernAccSid();
				if(caccsid==item.getSid())
				{
					showRedHot=true;
					break;
				}
			}
			if(showRedHot)
			{
				holder.usr_redHot.setVisibility(View.VISIBLE);
			}else
			{
				holder.usr_redHot.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	private class HolderView {
		public ImageView myconcern_icon;
		public TextView myconcern_title;
		public View usr_redHot;
		public TextView areaName;
		public TextView lastCont;
	}

	
	private void showMerchant(Merchant merchant,HolderView holder)
	{
		Account account=AppContext.currentAccount();
		if(account!=null)
		{
			ConcernNumber.setConcernNumberReaded(account.getSid(), merchant.getSid());
			if(holder!=null&&holder.usr_redHot!=null)
			{
				if(holder.usr_redHot.getVisibility()==View.VISIBLE)
				{
					Intent intent = new Intent();  
	                intent.setAction(Constant.BroadType.SUBSCRIBE_READED);  
					mContext.sendBroadcast(intent);
					holder.usr_redHot.setVisibility(View.GONE);	
				}
			}
		}
		
		Intent intent = new Intent(mContext, UsrShopActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant);
		mContext.startActivity(intent);
	}
	private void showUsrZone(Merchant item,HolderView holder)
	{
		Account acc=AppContext.currentAccount();
		if(acc!=null)
		{
			ConcernNumber.setConcernNumberReaded(acc.getSid(), item.getSid());
			if(holder!=null&&holder.usr_redHot!=null)
			{
				if(holder.usr_redHot.getVisibility()==View.VISIBLE)
				{
					Intent intent = new Intent();  
	                intent.setAction(Constant.BroadType.SUBSCRIBE_READED);  
					mContext.sendBroadcast(intent);
					holder.usr_redHot.setVisibility(View.GONE);	
				}
			}
		}
		Account account=new Account();
		account.setSid(item.getSid());
		account.setAccName(item.getMerchantName());
		AreaInfo ai=new AreaInfo();
		ai.setSid(item.getAlongAreaSid());
		ai.setAreaName(item.getAreaName());
		account.setAreaInfo(ai);
		mContext.startActivity(new Intent(mContext, UsrZoneActivity.class).putExtra(Constant.ActivityExtra.ACCOUNT, account));
	}

}
