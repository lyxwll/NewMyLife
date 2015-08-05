package com.yulinoo.plat.life.views.activity;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.MerchantCommentReq;
import com.yulinoo.plat.life.net.reqbean.SendMessageReq;
import com.yulinoo.plat.life.net.resbean.ForumNoteSingleResponse;
import com.yulinoo.plat.life.net.resbean.NormalResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.MerchantPraiseDialog;
import com.yulinoo.plat.life.utils.BaseTools;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.utils.MeUtil.OnPraiseListener;
import com.yulinoo.plat.melife.R;

import config.AppContext;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MerchantPraiseMoreFunction implements OnClickListener{

	private Context mContext;
	private LayoutInflater inflater;
	private View headPosition;
	private ForumNote forumNote;
	private MyApplication app;

	private View popContainer;
	private TextView addPraise;
	private PopupWindow popupWindow;

	private MerchantPraiseDialog praiseDialog;
	private ImageView publishHead;
	private TextView publishUsrName;
	private TextView publishTime;
	private TextView publishArea;
	private TextView publishContent;
	private TextView addPraiseButton;

	public MerchantPraiseMoreFunction(Context mContext,LayoutInflater inflater,View headPosition,ForumNote forumNote) {
		this.mContext=mContext;
		this.inflater=inflater;
		this.headPosition=headPosition;
		this.forumNote=forumNote;
		this.app=(MyApplication) mContext.getApplicationContext();
	}

	public void showMerchantPraiseMoreFunction(){
		popContainer=inflater.inflate(R.layout.merchant_praise_more_function_layout, null);
		addPraise=(TextView) popContainer.findViewById(R.id.add_praise_tv);
		addPraise.setOnClickListener(this);

		popupWindow=new PopupWindow(popContainer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
		// 设置此参数获得焦点，否则无法点击
		popupWindow.setFocusable(true);
		popContainer.setFocusable(true);// comment by danielinbiti,设置view能够接听事件，标注1
		popContainer.setFocusableInTouchMode(true); // comment by danielinbiti,设置view能够接听事件
		// 标注2
		popContainer.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK) {
					if (popupWindow != null) {
						popupWindow.dismiss();
					}
				}
				return false;
			}
		});
		// 点击其他地方消失
		popContainer.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
		popupWindow.showAsDropDown(headPosition, 0, 0);
	}

	/**
	 * disMissPopupWindow
	 */
	public void disMissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.add_praise_tv:
			disMissPopupWindow();
			praiseDialog=new MerchantPraiseDialog(mContext);
			praiseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			praiseDialog.show();
			initPraiseDialog();
			getMerchantInfor();
			break;
		}

	}

	//评论和赞
	private void addCommentPraise(Context context,long goodsSid,long merchantSid,final String content,final OnPraiseListener onPraiseListener) {
		if(!AppContext.currentAccount().getIsUsrLogin())
		{
			MeUtil.showToast(context,context.getString(R.string.need_login_first));
			context.startActivity(new Intent(context, LoginActivity.class));
			return;
		}
		SendMessageReq req=new SendMessageReq();
		req.setAccSid(merchantSid);
		req.setMevalue(AppContext.currentAccount().getMevalue());
		if(NullUtil.isStrNotNull(content))
		{//说明是评论
			req.setType(Constant.MSGTYPE.TYPE_COMMENT);
			req.setDesc(content);
		}else
		{
			req.setType(Constant.MSGTYPE.TYPE_PRAISE);
		}

		req.setAlongAreaSid(AppContext.nearByArea().getSid());
		req.setGoodsSid(goodsSid);
		RequestBean<NormalResponse> requestBean = new RequestBean<NormalResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(req);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(NormalResponse.class);
		requestBean.setURL(Constant.Requrl.getSendWrapMessage());
		ProgressUtil.showProgress(context, "处理中...");
		MeMessageService.instance().requestServer(requestBean, new UICallback<NormalResponse>() {

			@Override
			public void onSuccess(NormalResponse respose) {
				try {
					ProgressUtil.dissmissProgress();
					if(onPraiseListener!=null)
					{
						if(respose.isSuccess())
						{
							onPraiseListener.OnPraiseed(true,null);
						}else
						{
							onPraiseListener.OnPraiseed(false,respose.getMsg());
						}
					}
				} catch (Exception e) {
				}
			}

			@Override
			public void onError(String message) {
				ProgressUtil.dissmissProgress();
				if(onPraiseListener!=null)
				{
					onPraiseListener.OnPraiseed(false,message);
				}
			}

			@Override
			public void onOffline(String message) {
				ProgressUtil.dissmissProgress();
				if(onPraiseListener!=null)
				{
					onPraiseListener.OnPraiseed(false,message);
				}
			}
		});
	}

	public interface OnPraiseListener
	{
		public void OnPraiseed(boolean isOk,String message);
	}

	private void initPraiseDialog(){
		publishArea=praiseDialog.getPublishArea();
		publishContent=praiseDialog.getPublishContent();
		publishHead=praiseDialog.getPublishHead();
		publishTime=praiseDialog.getPublishTime();
		publishUsrName=praiseDialog.getPublishUsrName();
		addPraiseButton=praiseDialog.getAddPraise();
		addPraiseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				//设置点赞
				addCommentPraise(mContext, forumNote.getGoodsSid(), forumNote.getAccSid(), null, new OnPraiseListener() {

					@Override
					public void OnPraiseed(boolean isOk, String message) {
						if(isOk)
						{
							MeUtil.showToast(mContext, mContext.getString(R.string.praise_success));
							//刷新点赞界面
							MerchantPraiseListActivity.instance().setNeedRefresh(true);
							//发一个广播
							Intent intent = new Intent();  
							intent.setAction(Constant.BroadType.ADD_PRAISE_OK);
							intent.putExtra(Constant.ActivityExtra.GOODSSID, forumNote.getGoodsSid());
							mContext.sendBroadcast(intent);
							praiseDialog.dismiss();
						}else
						{
							MeUtil.showToast(mContext, message);
							praiseDialog.dismiss();
						}
					}
				});

			}
		});
	}
	
	private void getMerchantInfor(){
		MerchantCommentReq commentReq=new MerchantCommentReq();
		commentReq.setAccSid(forumNote.getAccSid());
		RequestBean<ForumNoteSingleResponse> requestBean=new RequestBean<ForumNoteSingleResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(commentReq);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(ForumNoteSingleResponse.class);
		requestBean.setURL(Constant.Requrl.getEarlyGood());
		MeMessageService.instance().requestServer(requestBean, new UICallback<ForumNoteSingleResponse>() {

			@Override
			public void onSuccess(ForumNoteSingleResponse respose) {
				ForumNote forumNote=respose.getForumNote();
				if (forumNote!=null) {
					String headPicUrl=forumNote.getHeadPicture();
					if (headPicUrl!=null) {
						MeImageLoader.displayImage(headPicUrl, publishHead, app.getHeadIconOption());
					}
					String merchantName=forumNote.getAccName();
					if (merchantName!=null) {
						publishUsrName.setText(merchantName);
					}
					String goodContent=forumNote.getGoodsContent();
					if (goodContent!=null) {
						publishContent.setText(goodContent);
					}
					Long time=forumNote.getCreateDate();
					if (time!=null && time>0) {
						publishTime.setText(BaseTools.getChajuDate(time));
					}
					String areaName=forumNote.getAreaName();
					if (areaName!=null) {
						publishArea.setText(areaName);
					}
				}else {
					MeUtil.showToast(mContext, "请求网络出错");
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
	}
}
