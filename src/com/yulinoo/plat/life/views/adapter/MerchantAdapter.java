package com.yulinoo.plat.life.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amap.api.location.core.e;
import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.BeanDetailReq;
import com.yulinoo.plat.life.net.resbean.GoodsDetailResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.life.views.activity.GoodsDetailActivity;
import com.yulinoo.plat.melife.R;

//商家信息显示列表
public class MerchantAdapter extends YuLinooLoadAdapter<ForumNote> implements OnClickListener{
	private LayoutInflater inflater;
	private Context mContext;
	private MyApplication myapp;
	public MerchantAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		this.mContext = context;
		myapp=(MyApplication)context.getApplicationContext();
	}
	public static interface IViewType {
		int ADVERTISE = 0;//广告
		int CONTENT = 1;//内容
	}
	@Override
	public int getItemViewType(int position) {
		ForumNote item = getItem(position);
		if(NullUtil.isStrNotNull(item.getGoodsContent())||NullUtil.isStrNotNull(item.getGoodsPhotoArray()))
		{
			return IViewType.CONTENT;
		}else
		{//广告
			return IViewType.ADVERTISE;
		}
		
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommonHolderView holder;
		final ForumNote item = getItem(position);

		if (convertView == null) {
			holder = new CommonHolderView();
			
			if(NullUtil.isStrNotNull(item.getGoodsContent())||NullUtil.isStrNotNull(item.getGoodsPhotoArray()))
			{
				convertView = inflater.inflate(R.layout.item_merchant_list, parent,false);	
				MeUtil.initWeiboContent(mContext, holder, convertView, item);
			}else
			{
				convertView = inflater.inflate(R.layout.item_advertise, parent,false);
				holder.adv=(ImageView)convertView.findViewById(R.id.adv);
				holder.close_adv=(ImageView)convertView.findViewById(R.id.close_adv);
				holder.adv_fl=convertView.findViewById(R.id.adv_fl);
			}
			convertView.setOnLongClickListener(null);	
			convertView.setTag(holder);
		} else {
			holder = (CommonHolderView) convertView.getTag();
		}
		if(NullUtil.isStrNotNull(item.getGoodsContent())||NullUtil.isStrNotNull(item.getGoodsPhotoArray()))
		{//有内容
			if (item.getLongItude()!=null && item.getLongItude().floatValue()>0) {
				MeUtil.setWeiboContent(mContext, holder, convertView, item,false);
			}else {
				MeUtil.setWeiboContent(mContext, holder, convertView, item,true);
			}
		}else
		{
			if(item.isClosed())
			{
				holder.adv_fl.setVisibility(View.GONE);
			}else
			{
				holder.adv_fl.setVisibility(View.VISIBLE);
//				holder.close_adv.setTag(item);
//				holder.close_adv.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						item.setClosed(true);
//						holder.adv_fl.setVisibility(View.GONE);
//					}
//				});
				if(NullUtil.isStrNotNull(item.getHeadPicture()))
				{
					int[] scrennWidth=SizeUtil.getAdvertiseSize(mContext);
					holder.adv.setTag(item);
					holder.adv.setOnClickListener(this);
					holder.adv.getLayoutParams().width=scrennWidth[0];
					holder.adv.getLayoutParams().height=scrennWidth[1];
					MeImageLoader.displayImage(item.getHeadPicture(), holder.adv, myapp.getWeiIconOption());
				}
			}
		}

		return convertView;
	}


	@Override
	public void onClick(final View v) {
		final ForumNote item=(ForumNote)v.getTag();
		switch(v.getId())
		{
		case R.id.adv:
		{
			BeanDetailReq req=new BeanDetailReq();
			req.setSid(item.getGoodsSid());
			RequestBean<GoodsDetailResponse> requestBean = new RequestBean<GoodsDetailResponse>();
			requestBean.setRequestBody(req);
			requestBean.setResponseBody(GoodsDetailResponse.class);
			requestBean.setURL(Constant.Requrl.getGoodsDetail());
			MeMessageService.instance().requestServer(requestBean, new UICallback<GoodsDetailResponse>() {
				@Override
				public void onSuccess(GoodsDetailResponse respose) {
					try {
						if(respose.isSuccess())
						{
							ForumNote forumNote=respose.getForumNote();
							if(forumNote!=null)
							{
								mContext.startActivity(new Intent(mContext, GoodsDetailActivity.class)
								.putExtra(GoodsDetailActivity.MERCHANT, forumNote));
							}
						}else
						{
							MeUtil.showToast(mContext,respose.getMsg());
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
			break;
		}
		}
	}
}