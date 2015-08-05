package com.yulinoo.plat.life.views.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.Comment;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.bean.MessageCenterInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.BeanDetailReq;
import com.yulinoo.plat.life.net.reqbean.CommentListReq;
import com.yulinoo.plat.life.net.resbean.CommentListResponse;
import com.yulinoo.plat.life.net.resbean.GoodsDetailResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.MyListView.OnRefreshListener;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.utils.BaseTools;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.FaceConversionUtil;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.views.activity.GoodsDetailActivity;
import com.yulinoo.plat.life.views.activity.MeLifeMainActivity;
import com.yulinoo.plat.life.views.adapter.MerchantAdapter.IViewType;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class MyCommentItemAdapter extends YuLinooLoadAdapter<MessageCenterInfo> implements OnClickListener,XListView.IXListViewListener{
	private LayoutInflater inflater;
	private MyApplication myapp;
	private Context mContext;
	//private SimpleDateFormat sdf;
	private Account me;
	private boolean isEnd=false;
	private boolean isLoading=false;
	
	
	public MyCommentItemAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mContext=context;
		myapp=(MyApplication)context.getApplicationContext();
		//sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINESE);
		me=AppContext.currentAccount();
	}
	
	public static interface IViewType {
		int FOOTER = 0;//底部的无更多内容显示
		int CONTENT = 1;//内容
	}
	@Override
	public int getItemViewType(int position) {
		MessageCenterInfo item = getItem(position);
		if(item==null)
		{
			return IViewType.FOOTER;
		}else
		{//广告
			return IViewType.CONTENT;
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
			if (item!=null) {
				convertView = inflater.inflate(R.layout.my_comment_list_item, parent,false);
				holder.responseHeaderPicture = (ImageView) convertView.findViewById(R.id.responseHeaderPicture);
				holder.responseNickName = (TextView) convertView.findViewById(R.id.responseNickName);
				holder.comment_direction_desc = (TextView) convertView.findViewById(R.id.comment_direction_desc);
				holder.response_usr_time = (TextView) convertView.findViewById(R.id.response_usr_time);
				holder.response_usr_area = (TextView) convertView.findViewById(R.id.response_usr_area);
				holder.responseContent = (TextView) convertView.findViewById(R.id.responseContent);
				
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
		//绑定数据
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
			if(me.getSid().longValue()==item.getResponseUsrSid().longValue())
			{//当前用户与响应者的ID相同，说明是自己
				holder.comment_direction_desc.setTextColor(mContext.getResources().getColor(R.color.font_blue));
				holder.comment_direction_desc.setText(R.string.me_send_comment);
			}else
			{
				holder.comment_direction_desc.setTextColor(mContext.getResources().getColor(R.color.font_orange));
				holder.comment_direction_desc.setText(R.string.i_receive_comment);
			}
			if(holder.response_usr_time!=null&&item.getResponseTime()!=null)
			{
				holder.response_usr_time.setText(BaseTools.getChajuDate(item.getResponseTime()));
			}
			holder.response_usr_area.setText(mContext.getString(R.string.come_from)+item.getResponseUsrAreaName());
			if(NullUtil.isStrNotNull(item.getResponseContent()))
			{
				holder.responseContent.setText(null);
				SpannableString responseContent = FaceConversionUtil.getInstance().getExpressionString(mContext, item.getResponseContent());
				holder.responseContent.append(responseContent);
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
			if (NullUtil.isStrNotNull(item.getGoodsPublishContent())) {
				SpannableString goodsPublishContent = FaceConversionUtil.getInstance().getExpressionString(mContext, item.getGoodsPublishContent());
				holder.goodsPublishContent.append(goodsPublishContent);
			}
			holder.goodsPublishUsrName.setText(item.getGoodsPublishUsrName());
		}
		return convertView;
	}

	private class HolderView {
		public ImageView responseHeaderPicture;//评论人头像
		public TextView responseNickName;//评论人昵称
		public TextView comment_direction_desc;//评论方向说明
		public TextView response_usr_time;//评论时间
		public TextView response_usr_area;//评论所在小区
		public TextView responseContent;//评论人的内容

		public ImageView goodsPublishUsrHeaderPicture;//被评论者的头像
		public TextView goodsPublishUsrName;//被评论者的昵称
		public TextView commented_usr_time;//被评论者的评论时间
		public TextView commented_usr_area;//被评论者的评论所在小区
		public TextView goodsPublishContent;//被评论者的内容,正常情况下应该是微博的文字内容
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
					MeUtil.showToast(mContext, message);
				}

				@Override
				public void onOffline(String message) {
					MeUtil.showToast(mContext, message);
				}
			});
			break;
		}

		}
	}

	@Override
	public void onRefresh() {
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
	
	private int pageNo=0;
	public synchronized void loadCommentsAndPraise(final boolean isUp) {
		final MessageCenterInfo messageLists=getList().get(0);
		if(isUp)
		{
			pageNo=0;
			isEnd=false;
		}else
		{
			if(isEnd)
			{
				//mSwipeLayout.setLoading(false);
				//onLoad();
				return;
			}
			pageNo++;
		}
		if(isLoading)
		{
			return;
		}
	}

}
