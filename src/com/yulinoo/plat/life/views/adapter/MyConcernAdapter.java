package com.yulinoo.plat.life.views.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.ConcernNumber;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.bean.MessageCenterInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.DelLinkedUsrReq;
import com.yulinoo.plat.life.net.resbean.NormalResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.ConfirmCloseDialog;
import com.yulinoo.plat.life.ui.widget.ConfirmCloseDialog.FinishCallback;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.FaceConversionUtil;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.views.activity.MeLifeMainActivity;
import com.yulinoo.plat.life.views.activity.MyLinkedUsrActivity;
import com.yulinoo.plat.life.views.activity.PrivateMessageActivity;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class MyConcernAdapter extends YuLinooLoadAdapter<MessageCenterInfo> implements OnClickListener{
	private LayoutInflater inflater;
	private Context mContext;
	private MyApplication myapp;
	private View myfriend_rl;
	private View favorite_merchant_rl;
	private View service_rl;
	private View guess_love_rl;
	private View myfriend_rl_redHot;
	private View favorite_merchant_rl_redHot;
	private View service_redHot;
	private ConfirmCloseDialog confirmCloseDialog;
	public MyConcernAdapter(Context context/*,List<UserGroup> ugs*/) {
		inflater = LayoutInflater.from(context);
		mContext=context;
		myapp=((MyApplication)mContext.getApplicationContext());
	}

	public static interface IViewType {
		int SHOW_HEADER = 0;//分组
		int SHOW_ITEM = 1;//内容项
	}
	@Override
	public int getItemViewType(int position) {
		if(position==0)
		{
			return IViewType.SHOW_HEADER;
		}else
		{
			return IViewType.SHOW_ITEM;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		final MessageCenterInfo item = getItem(position);

		if (convertView == null) {
			holder = new HolderView();
			if(position==0)
			{
				convertView = inflater.inflate(R.layout.item_myconcern_header, parent,false);
				myfriend_rl=convertView.findViewById(R.id.myfriend_rl);
				myfriend_rl.setOnClickListener(this);
				favorite_merchant_rl=convertView.findViewById(R.id.favorite_merchant_rl);
				favorite_merchant_rl.setOnClickListener(this);
				service_rl=convertView.findViewById(R.id.service_rl);
				service_rl.setOnClickListener(this);
				guess_love_rl=convertView.findViewById(R.id.guess_love_rl);
				guess_love_rl.setOnClickListener(this);
				myfriend_rl_redHot=convertView.findViewById(R.id.myfriend_rl_redHot);
				favorite_merchant_rl_redHot=convertView.findViewById(R.id.favorite_merchant_rl_redHot);
				service_redHot=convertView.findViewById(R.id.service_redHot);
			}else
			{
				convertView = inflater.inflate(R.layout.item_myconcern_cont, parent,false);
				holder.myconcern_icon = (ImageView) convertView.findViewById(R.id.myconcern_icon);
				holder.myconcern_title = (TextView) convertView.findViewById(R.id.myconcern_title);
				holder.areaName = (TextView) convertView.findViewById(R.id.areaName);
				//holder.responseTime = (TextView) convertView.findViewById(R.id.responseTime);
				holder.lastCont = (TextView) convertView.findViewById(R.id.lastCont);
				holder.usr_redHot = (ImageView) convertView.findViewById(R.id.usr_redHot);
			}
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		if(position>0)
		{
			if(NullUtil.isStrNotNull(item.getResponseHeaderPicture()))
			{
				MeImageLoader.displayImage(item.getResponseHeaderPicture(), holder.myconcern_icon, myapp.getHeadIconOption());
			}
			holder.myconcern_title.setText(item.getResponseNickName());
			if(NullUtil.isStrNotNull(item.getResponseContent()))
			{
				holder.lastCont.setText(null);
				SpannableString spannableString = FaceConversionUtil.getInstance().getExpressionString(mContext, item.getResponseContent());
				holder.lastCont.append(spannableString);
			}
//			if(holder.responseTime!=null&&item.getResponseTime()!=null)
//			{
//				holder.responseTime.setText(BaseTools.getChajuDate(item.getResponseTime()));
//			}
			holder.areaName.setText(mContext.getString(R.string.come_from)+item.getResponseUsrAreaName());
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showPrivateMessage(item);
				}
			});
			convertView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					delLinkedUsr(item);
					return false;
				}
			});
			if(item.getReadStatus()==Constant.MessageReadStatus.READ_STATUS_UNREADED)
			{
				holder.usr_redHot.setVisibility(View.VISIBLE);
			}else
			{
				holder.usr_redHot.setVisibility(View.GONE);
			}

		}else
		{
			setRedHot();
		}
		return convertView;
	}

	private class HolderView {
		public ImageView myconcern_icon;
		public TextView myconcern_title;
		public TextView areaName;
		//public TextView responseTime;
		public TextView lastCont;
		public ImageView usr_redHot;
	}
	
	private void showPrivateMessage(MessageCenterInfo item)
	{
		if(!AppContext.currentAccount().getIsUsrLogin())
		{
			MeUtil.showToast(mContext,mContext.getString(R.string.need_login_first));
			//MeLifeMainActivity.instance().onMenuSelected(MeLifeMainActivity.instance().my_zone.getIndex());
			return;
		}
		//设置状态为已读状态
		item.setReadStatus(Constant.MessageReadStatus.READ_STATUS_READED);
		item.save();
		this.notifyDataSetChanged();
		
		Intent intent = new Intent();  
        intent.setAction(Constant.BroadType.SUBSCRIBE_READED);  
		mContext.sendBroadcast(intent);

		Account account=new Account();
		account.setSid(item.getResponseUsrSid());
		account.setAccName(item.getResponseNickName());
		AreaInfo ai=new AreaInfo();
		ai.setSid(item.getResponseUsrAreaSid());
		ai.setAreaName(item.getResponseUsrAreaName());
		account.setAreaInfo(ai);
		mContext.startActivity(new Intent(mContext, PrivateMessageActivity.class).putExtra(Constant.ActivityExtra.ACCOUNT, account));
	}
	private void delLinkedUsr(final MessageCenterInfo item)
	{
		confirmCloseDialog = new ConfirmCloseDialog(mContext, "\n是否删除当前聊天?\n", "删除聊天", "删除", "取消", new FinishCallback() {
			@Override
			public void confirmThisOperation() {
				confirmCloseDialog.dismiss();
				confirmCloseDialog = null;
				final Account me=AppContext.currentAccount();
				if(me!=null)
				{
					//删除联系人
					DelLinkedUsrReq req=new DelLinkedUsrReq();
					req.setMevalue(me.getMevalue());
					req.setResponseUsrSid(item.getResponseUsrSid());
					RequestBean<NormalResponse> requestBean = new RequestBean<NormalResponse>();
					requestBean.setRequestBody(req);
					requestBean.setResponseBody(NormalResponse.class);
					requestBean.setURL(Constant.Requrl.getDelLinkedUsr());
					if(MeLifeMainActivity.isInstanciated())
					{
						MeMessageService.instance().requestServer(requestBean, new UICallback<NormalResponse>() {
							@Override
							public void onSuccess(NormalResponse respose) {
								try {
									if(respose.isSuccess())
									{
										List<MessageCenterInfo> list=getList();
										int size=list.size();
										for(int kk=0;kk<size;kk++)
										{
											MessageCenterInfo mci=list.get(kk);
											if(mci!=null)
											{
												if(mci.getResponseUsrSid().longValue()==item.getResponseUsrSid().longValue()
														&&mci.getGoodsPublishUsrSid().longValue()==item.getGoodsPublishUsrSid().longValue())
												{
													list.remove(kk);
													break;
												}
											}
										}
										
										new Delete().from(MessageCenterInfo.class).where("responseUsrSid=? and goodsPublishUsrSid=?",item.getResponseUsrSid(),me.getSid()).execute();
										
										MyConcernAdapter.this.notifyDataSetChanged();
									}else
									{
										MeUtil.showToast(mContext, respose.getMsg());
									}
								} catch (Exception e) {
								}
							}
							@Override
							public void onError(String message) {
								MeUtil.showToast(mContext, message);
							}

							@Override
							public void onOffline(String message) {
								MeUtil.showToast(mContext,message);
							}
						});
					}
				}
			}

			@Override
			public void cancle() {
				confirmCloseDialog.dismiss();
				confirmCloseDialog = null;
			}
		});
		confirmCloseDialog.show();
	}
	
	private void setRedHot()
	{
		Account account=AppContext.currentAccount();
		if(account!=null)
		{
			//当前用户关注的好友列表
			List<Merchant> listSubs=AppContext.currentFocusMerchant();
			//当前数据库存储的好友的更新状态
			List<ConcernNumber> lisCs=ConcernNumber.getUsrConcernNumber(account.getSid());
			if(lisCs!=null)
			{
				if(lisCs.size()>0)
				{
					for(Merchant merchant:listSubs)
					{
						long accSid=merchant.getSid();//关注的商家/用户的主键
						for(ConcernNumber cn:lisCs)
						{
							long cacc=cn.getConcernAccSid().longValue();
							int concernNumber=cn.getNumber();
							if(concernNumber==0)
							{
								new Delete().from(ConcernNumber.class).where("id=?",cn.getId());
							}else
							{
								if(accSid==cacc)
								{
									int subType=merchant.getType();
									if(Constant.SUBTYPE.SUBTYPE_MERCHANT==subType)
									{//收藏的商铺有更新
										if(favorite_merchant_rl_redHot!=null)
										{
											if(favorite_merchant_rl_redHot.getVisibility()==View.GONE){
												favorite_merchant_rl_redHot.setVisibility(View.VISIBLE);
											}
										}
									}else if(Constant.SUBTYPE.SUBTYPE_SERVICE==subType)
									{//服务号有更新
										if(service_redHot!=null)
										{
											if(service_redHot.getVisibility()==View.GONE){
												service_redHot.setVisibility(View.VISIBLE);
											}
										}
									}else if(Constant.SUBTYPE.SUBTYPE_USR==subType)
									{//邻友有更新
										if(myfriend_rl_redHot!=null)
										{
											if(myfriend_rl_redHot.getVisibility()==View.GONE){
												myfriend_rl_redHot.setVisibility(View.VISIBLE);
											}
										}
									}
								}
							}
						}
					}
				}else
				{
					if(favorite_merchant_rl_redHot!=null)
					{
						if(favorite_merchant_rl_redHot.getVisibility()==View.VISIBLE){
							favorite_merchant_rl_redHot.setVisibility(View.GONE);
						}
						if(service_redHot.getVisibility()==View.VISIBLE){
							service_redHot.setVisibility(View.GONE);
						}
						if(myfriend_rl_redHot.getVisibility()==View.VISIBLE){
							myfriend_rl_redHot.setVisibility(View.GONE);
						}
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.myfriend_rl:
		{
			if(AppContext.currentAccount()==null)
			{
				MeUtil.showToast(mContext, mContext.getString(R.string.needconcerarea));
				return;
			}
			Merchant merchant=new Merchant();
			merchant.setSid(AppContext.currentAccount().getSid());
			mContext.startActivity(new Intent(mContext, MyLinkedUsrActivity.class)
			.putExtra(Constant.ActivityExtra.MERCHANT, merchant)
			.putExtra(Constant.ActivityExtra.LINKED_LOAD_TYPE, Constant.ConcernLoadType.LOAD_MYFRIEND)
			.putExtra(Constant.ActivityExtra.LINKEDTITLE, mContext.getString(R.string.myfriend_rl)));
			break;
		}
		case R.id.favorite_merchant_rl:
		{
			if(AppContext.currentAccount()==null)
			{
				MeUtil.showToast(mContext, mContext.getString(R.string.needconcerarea));
				return;
			}
			Merchant merchant=new Merchant();
			merchant.setSid(AppContext.currentAccount().getSid());
			mContext.startActivity(new Intent(mContext, MyLinkedUsrActivity.class)
			.putExtra(Constant.ActivityExtra.MERCHANT, merchant)
			.putExtra(Constant.ActivityExtra.LINKED_LOAD_TYPE, Constant.ConcernLoadType.LOAD_FAVORITE)
			.putExtra(Constant.ActivityExtra.LINKEDTITLE, mContext.getString(R.string.favorite_merchant_rl)));
			break;
		}
		case R.id.service_rl:
		{
			if(AppContext.currentAccount()==null)
			{
				MeUtil.showToast(mContext, mContext.getString(R.string.needconcerarea));
				return;
			}
			Merchant merchant=new Merchant();
			merchant.setSid(AppContext.currentAccount().getSid());
			mContext.startActivity(new Intent(mContext, MyLinkedUsrActivity.class)
			.putExtra(Constant.ActivityExtra.MERCHANT, merchant)
			.putExtra(Constant.ActivityExtra.LINKED_LOAD_TYPE, Constant.ConcernLoadType.LOAD_SERVICE)
			.putExtra(Constant.ActivityExtra.LINKEDTITLE, mContext.getString(R.string.service_rl)));
			break;
		}
		case R.id.guess_love_rl:
		{
			if(AppContext.currentAccount()==null)
			{
				MeUtil.showToast(mContext, mContext.getString(R.string.needconcerarea));
				return;
			}
			Merchant merchant=new Merchant();
			merchant.setSid(AppContext.currentAccount().getSid());
			mContext.startActivity(new Intent(mContext, MyLinkedUsrActivity.class)
			.putExtra(Constant.ActivityExtra.MERCHANT, merchant)
			.putExtra(Constant.ActivityExtra.LINKED_LOAD_TYPE, Constant.ConcernLoadType.LOAD_GUESS_LOVE)
			.putExtra(Constant.ActivityExtra.LINKEDTITLE, mContext.getString(R.string.guess_love_rl)));
			break;
		}
		}
	}

}
