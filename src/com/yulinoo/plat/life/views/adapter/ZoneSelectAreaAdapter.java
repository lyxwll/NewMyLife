package com.yulinoo.plat.life.views.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.ConcernAreaReq;
import com.yulinoo.plat.life.net.reqbean.TmpsidReq;
import com.yulinoo.plat.life.net.resbean.TmpSidResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.MeUtil.OnCityForumLoadListener;
import com.yulinoo.plat.life.utils.MeUtil.OnLoadCityCategoryListener;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class ZoneSelectAreaAdapter extends YuLinooLoadAdapter<AreaInfo> {
	private LayoutInflater inflater;
	private Context mContext;
	private boolean openShop;//是否小区开店，小区开店的情况下，可以直接选择小区
	private List<AreaInfo> listFocusArea=null;

	public ZoneSelectAreaAdapter(Context context, boolean openShop) {
		this.openShop = openShop;
		inflater = LayoutInflater.from(context);
		mContext = context;
		listFocusArea=AppContext.currentFocusArea();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		final AreaInfo item = getItem(position);

		if (convertView == null) {
			holder = new HolderView();
			convertView = inflater.inflate(R.layout.zone_select_item, parent,false);
			holder.area_address = (TextView) convertView.findViewById(R.id.area_address);
			holder.area_name = (TextView) convertView.findViewById(R.id.area_name);
			holder.focusArea = (TextView) convertView.findViewById(R.id.focusArea);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		if(!openShop)
		{
			holder.focusArea.setSelected(false);
			holder.focusArea.setText(R.string.add_concern);
			final TextView concernArea=holder.focusArea;
			setConcern(concernArea, item);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(concernArea.getText().toString().equals(mContext.getString(R.string.add_concern)))
					{//需要关注的,已经关注了的,就设置不能点击
						concernArea(item);
					}
				}
			});
		}else
		{
			holder.focusArea.setVisibility(View.GONE);
		}
		holder.area_name.setText(item.getAreaName());
		holder.area_address.setText(mContext.getString(R.string.enter_merchant)+":"+item.getEnterNum()+" "+mContext.getString(R.string.concern_person)+":"+item.getAttNum());

		if (openShop) {//如果此时是小区开店
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(onAreaInfoSelectedListener!=null)
					{
						onAreaInfoSelectedListener.onAreaInfoSelected(item);
					}
				}
			});
		}


		return convertView;
	}

	//设置关注
	private void setConcern(final TextView concernArea,final AreaInfo nowAi)
	{
		if(listFocusArea!=null)
		{
			for(AreaInfo ai:listFocusArea)
			{
				if(ai.getSid()==nowAi.getSid())
				{//已关注
					concernArea.setSelected(true);
					concernArea.setText(R.string.have_concern);
					break;
				}
			}
		}
		if(!openShop)
		{//不是小区开店
			concernArea.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(concernArea.isSelected())
					{//忆关注的情况，则取消关注
						MeUtil.cancelConcernArea(mContext, nowAi, onAreaInfoConcernedListener);
					}else
					{
						concernArea(nowAi);
					}
				}
			});

		}else
		{
			concernArea.setVisibility(View.GONE);
		}
	}


	private class HolderView {
		public TextView area_name;
		public TextView area_address;
		public TextView focusArea;
	}
	//关注小区
	public void concernArea(final AreaInfo areaInfo) {
		Account account=AppContext.currentAccount();
		if(account!=null)
		{//说明当前有用户信息，或者为临时账号信息或者为上次登录用户记住密码的信息
			trueConcernArea(areaInfo);
		}else
		{//需要从服务器端取临时账号
			TmpsidReq tmpsidReq = new TmpsidReq();
			tmpsidReq.setCitySid(areaInfo.getAlongCitySid());
			tmpsidReq.setDistrictSid(areaInfo.getAlongDistrictSid());
			tmpsidReq.setPhoneType(AppContext.getPhoneType());
			RequestBean<TmpSidResponse> requestBean = new RequestBean<TmpSidResponse>();
			requestBean.setRequestBody(tmpsidReq);
			requestBean.setResponseBody(TmpSidResponse.class);
			requestBean.setURL(Constant.Requrl.getTempSid());
			MeMessageService.instance().requestServer(requestBean, new UICallback<TmpSidResponse>() {
				@Override
				public void onSuccess(TmpSidResponse respose) {
					try {
						Account accountTB=new Account();
						accountTB.setSid(respose.getAccSid());
						accountTB.setAccName("me_"+respose.getAccSid());
						accountTB.setIsTemp(true);//标识为临时账号
						accountTB.setLastAccTime(System.currentTimeMillis());//设置最后访问的时间点
						accountTB.setAccType(Constant.ACCTYPE.ACCTYPE_NORMAL_USR);//账号类型
						accountTB.setIsUsrLogin(false);
						accountTB.save();
						trueConcernArea(areaInfo);
					} catch (Exception e) {
					}
				}

				@Override
				public void onError(String message) {
					ProgressUtil.dissmissProgress();
					MeUtil.showToast(mContext, message);
				}

				@Override
				public void onOffline(String message) {
					ProgressUtil.dissmissProgress();
					MeUtil.showToast(mContext, message);
				}
			});
		}

	}
	private void trueConcernArea(final AreaInfo areaInfo) {
		ConcernAreaReq concernAreaReq = new ConcernAreaReq();
		concernAreaReq.setAreaSid(areaInfo.getSid());
		concernAreaReq.setCitySid(areaInfo.getAlongCitySid());
		concernAreaReq.setAccSid(AppContext.currentAccount().getSid());
		RequestBean<String> requestBean = new RequestBean<String>();
		requestBean.setRequestBody(concernAreaReq);
		requestBean.setResponseBody(String.class);
		requestBean.setURL(Constant.Requrl.getConcernArea());
		ProgressUtil.showProgress(mContext, "正在关注...");
		MeMessageService.instance().requestServer(requestBean, new UICallback<String>() {
			@Override
			public void onSuccess(String respose) {
				try {
					MeUtil.loadCityCategory(areaInfo.getAlongCitySid(),new OnLoadCityCategoryListener() {
						@Override
						public void onLoadCityCategoryDone() {
							MeUtil.loadCityForums(areaInfo.getAlongCitySid(),new OnCityForumLoadListener() {
								@Override
								public void cityForumLoadDone() {
									ProgressUtil.dissmissProgress();
									AppContext.focusArea(areaInfo, true);
									if(onAreaInfoConcernedListener!=null)
									{
										onAreaInfoConcernedListener.onAreaInfoConcerned(areaInfo, true);
									}
								}
							});
						}
					});
				} catch (Exception e) {
				}
			}

			@Override
			public void onError(String message) {
				ProgressUtil.dissmissProgress();
				MeUtil.showToast(mContext, message);
			}

			@Override
			public void onOffline(String message) {
				ProgressUtil.dissmissProgress();
				MeUtil.showToast(mContext, message);
			}
		});

	}

	private OnAreaInfoSelectedListener onAreaInfoSelectedListener;

	public OnAreaInfoSelectedListener getOnAreaInfoSelectedListener() {
		return onAreaInfoSelectedListener;
	}

	public void setOnAreaInfoSelectedListener(
			OnAreaInfoSelectedListener onAreaInfoSelectedListener) {
		this.onAreaInfoSelectedListener = onAreaInfoSelectedListener;
	}


	public interface OnAreaInfoSelectedListener
	{
		public void onAreaInfoSelected(AreaInfo areaInfo);
	}

	private OnAreaInfoConcernedListener onAreaInfoConcernedListener;
	public OnAreaInfoConcernedListener getOnAreaInfoConcernedListener() {
		return onAreaInfoConcernedListener;
	}

	public void setOnAreaInfoConcernedListener(
			OnAreaInfoConcernedListener onAreaInfoConcernedListener) {
		this.onAreaInfoConcernedListener = onAreaInfoConcernedListener;
	}

	public interface OnAreaInfoConcernedListener
	{
		//参数二表示是否是关注
		public void onAreaInfoConcerned(AreaInfo areaInfo,boolean isConcerned);
	}

}
