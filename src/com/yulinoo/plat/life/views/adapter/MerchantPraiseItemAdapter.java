package com.yulinoo.plat.life.views.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.bean.MessageCenterInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.BeanDetailReq;
import com.yulinoo.plat.life.net.resbean.GoodsDetailResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.utils.BaseTools;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.FaceConversionUtil;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.views.activity.GoodsDetailActivity;
import com.yulinoo.plat.life.views.activity.MeLifeMainActivity;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class MerchantPraiseItemAdapter extends YuLinooLoadAdapter<MessageCenterInfo> implements OnClickListener{
	private LayoutInflater inflater;
	private MyApplication myapp;
	private Context mContext;
	private Account me;
	public MerchantPraiseItemAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mContext=context;
		myapp=(MyApplication)context.getApplicationContext();
		me=AppContext.currentAccount();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		final MessageCenterInfo item = getItem(position);
		if (convertView == null) {
			holder = new HolderView();
			if (item!=null) {
				convertView = inflater.inflate(R.layout.merchant_praise_list_item, parent,false);
				holder.responseHeaderPicture = (ImageView) convertView.findViewById(R.id.responseHeaderPicture);
				holder.responseNickName = (TextView) convertView.findViewById(R.id.responseNickName);
				holder.comment_direction_desc = (TextView) convertView.findViewById(R.id.comment_direction_desc);
				holder.response_usr_time = (TextView) convertView.findViewById(R.id.response_usr_time);
				holder.response_usr_area = (TextView) convertView.findViewById(R.id.response_usr_area);

				holder.goodsPublishUsrHeaderPicture = (ImageView) convertView.findViewById(R.id.goodsPublishUsrHeaderPicture);
				holder.goodsPublishUsrName = (TextView) convertView.findViewById(R.id.goodsPublishUsrName);
				holder.commented_usr_time = (TextView) convertView.findViewById(R.id.commented_usr_time);
				holder.commented_usr_area = (TextView) convertView.findViewById(R.id.commented_usr_area);
				holder.goodsPublishContent = (TextView) convertView.findViewById(R.id.goodsPublishContent);
			}else {
				convertView=inflater.inflate(R.layout.included_cont_item_footer, null);
			}
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		if (item!=null) {
			View content_zt=convertView.findViewById(R.id.content_zt);
			if(content_zt!=null)
			{
				content_zt.setTag(item);
				content_zt.setOnClickListener(this);
			}
			View content_above=convertView.findViewById(R.id.content_above);
			if(content_above!=null)
			{
				content_above.setTag(item);
				content_above.setOnClickListener(this);
			}
			
			if(NullUtil.isStrNotNull(item.getResponseHeaderPicture()))
			{
				MeImageLoader.displayImage(item.getResponseHeaderPicture(), holder.responseHeaderPicture, myapp.getHeadIconOption());
			}
			holder.responseNickName.setText(item.getResponseNickName());
			if(holder.response_usr_time!=null&&item.getResponseTime()!=null)
			{
				holder.response_usr_time.setText(BaseTools.getChajuDate(item.getResponseTime()));
			}
			holder.response_usr_area.setText(mContext.getString(R.string.come_from)+item.getResponseUsrAreaName());
			
			if(me.getSid().longValue()==item.getResponseUsrSid().longValue())
			{//当前用户与响应者的ID相同，说明是自己
				holder.comment_direction_desc.setTextColor(mContext.getResources().getColor(R.color.font_blue));
				holder.comment_direction_desc.setText(R.string.you_send_praise);
			}else
			{
				holder.comment_direction_desc.setTextColor(mContext.getResources().getColor(R.color.font_orange));
				holder.comment_direction_desc.setText("发出了赞");
			}
			
			if(NullUtil.isStrNotNull(item.getGoodsPublishUsrHeaderPicture()))
			{
				MeImageLoader.displayImage(item.getGoodsPublishUsrHeaderPicture(), holder.goodsPublishUsrHeaderPicture, myapp.getHeadIconOption());
			}
			if(holder.response_usr_time!=null&&item.getGoodsPublishTime()!=null)
			{
				holder.commented_usr_time.setText(BaseTools.getChajuDate(item.getGoodsPublishTime()));
			}
			holder.commented_usr_area.setText(mContext.getString(R.string.come_from)+item.getGoodsPublishAreaName());
			holder.goodsPublishContent.setText(null);
			if(NullUtil.isStrNotNull(item.getGoodsPublishContent()))
			{
				SpannableString spannableString = FaceConversionUtil.getInstance().getExpressionString(mContext, item.getGoodsPublishContent());
				holder.goodsPublishContent.append(spannableString);
			}
			
			holder.goodsPublishUsrName.setText(item.getGoodsPublishUsrName());
		}
		
		return convertView;
	}

	private class HolderView {
		public ImageView responseHeaderPicture;//点赞人头像
		public TextView responseNickName;//点赞人昵称
		public TextView comment_direction_desc;//点赞人方向说明
		public TextView response_usr_time;//点赞人时间
		public TextView response_usr_area;//点赞人所在小区
		
		public ImageView goodsPublishUsrHeaderPicture;//被点赞者的头像
		public TextView goodsPublishUsrName;//被点赞者的昵称
		public TextView commented_usr_time;//被点赞者的评论时间
		public TextView commented_usr_area;//被点赞者的评论所在小区
		public TextView goodsPublishContent;//被点赞者的内容,正常情况下应该是微博的文字内容
	}
	@Override
	public void onClick(View v) {
		final MessageCenterInfo item=(MessageCenterInfo)v.getTag();
		switch(v.getId())
		{
		case R.id.content_above:
		case R.id.content_zt:
		{
			BeanDetailReq req=new BeanDetailReq();
			req.setSid(item.getAlongGoodsSid());
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
