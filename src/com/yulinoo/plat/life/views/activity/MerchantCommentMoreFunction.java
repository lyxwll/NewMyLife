package com.yulinoo.plat.life.views.activity;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.MerchantCommentReq;
import com.yulinoo.plat.life.net.resbean.ForumNoteSingleResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.MeFaceViewWiget;
import com.yulinoo.plat.life.ui.widget.MerchantCommentDialog;
import com.yulinoo.plat.life.ui.widget.MeFaceViewWiget.OnFaceClickListener;
import com.yulinoo.plat.life.utils.BaseTools;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.MeUtil.OnPraiseListener;
import com.yulinoo.plat.melife.R;

import config.AppContext;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MerchantCommentMoreFunction implements OnClickListener{
	
	private MerchantCommentListActivity activity;
	private LayoutInflater inflater;
	private View headPosition;
	private ForumNote forumNote;
	private MyApplication app;
	
	private View popContainer;
	private PopupWindow popupWindow;
	private TextView addComment;
	
	private MerchantCommentDialog commentDialog;
	private ImageView ivIcon;
	private MeFaceViewWiget meFace;
	private TextView publish;
	private EditText des;
	private int max_text_number=500;
	private TextView sy_text_number;
	
	private ImageView publishUsrHead;
	private TextView publishUsrName;
	private TextView publishContent;
	private TextView publishTime;
	private TextView publishArea;

	public MerchantCommentMoreFunction(MerchantCommentListActivity activity,LayoutInflater inflater,View headPosition, ForumNote forumNote) {
		this.activity=activity;
		this.inflater=inflater;
		this.headPosition=headPosition;
		this.forumNote=forumNote;
		app=(MyApplication) activity.getApplicationContext();
		popContainer=inflater.inflate(R.layout.merchant_comment_more_function_layout, null);
		addComment=(TextView) popContainer.findViewById(R.id.add_comment_tv);
		addComment.setOnClickListener(this);
	}
	
	public void showMerchantCommentMoreFunction(){
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
		case R.id.add_comment_tv:
			disMissPopupWindow();
			commentDialog=new MerchantCommentDialog(activity,forumNote);
			commentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			commentDialog.show();
			initCommentDialog();
			//请求网络,显示商家信息
			getMerchantInfor();
//			WindowManager windowManager = activity.getWindowManager();
//			Display display = windowManager.getDefaultDisplay();
//			WindowManager.LayoutParams lp = commentDialog.getWindow().getAttributes();
//			lp.width = (int)(AppContext.getScreenWidth(activity)); //设置宽度
//			commentDialog.getWindow().setAttributes(lp);
			break;
		}
	}
	
	private void initCommentDialog(){
		ivIcon=commentDialog.getIvIcon();
		meFace=commentDialog.getMeFace();
		publish=commentDialog.getPublish();
		des=commentDialog.getDes();
		max_text_number=Integer.parseInt(activity.getString(R.string.max_text_number));
		sy_text_number=commentDialog.getSyTextNumber();
		sy_text_number.setText("( "+max_text_number+"/"+max_text_number+" )");
		
		publishUsrHead=commentDialog.getPublishUsrHead();
		publishUsrName=commentDialog.getPublishUsrName();
		publishContent=commentDialog.getPublishContent();
		publishTime=commentDialog.getPublishTime();
		publishArea=commentDialog.getPublishArea();
		setListener();
	}
	
	private void setListener(){
		ivIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (meFace.getVisibility()==View.VISIBLE) {
					meFace.setVisibility(View.GONE);
				} else {
					meFace.setVisibility(View.VISIBLE);
					MeUtil.closeSoftPad(activity);
				}
			}
		});
		
		des.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String txt=s.toString();
				int newSize=txt.length();
				if(newSize>max_text_number)
				{
					des.setText(des.getText().toString().substring(0,max_text_number));
					MeUtil.showToast(activity,"输入字数过多,将会被截断");
					newSize=500;
				}
				sy_text_number.setText("( "+(max_text_number-newSize)+"/"+max_text_number+" )");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		
		meFace.setOnFaceClickListener(new OnFaceClickListener() {
			@Override
			public void onFaceClick(SpannableString spannableString) {
				des.append(spannableString);
			}
		});
		
		publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String message=des.getText().toString();
				if (message==null) {
					MeUtil.showToast(activity, "不能发布空消息!");
				}else {
					addComment(Constant.MSGTYPE.TYPE_COMMENT,message);
				}
			}
		});
	}

	private void addComment(final int type,String message) {
		if(!AppContext.currentAccount().getIsUsrLogin())
		{
			MeUtil.showToast(activity,activity.getString(R.string.need_login_first));
			activity.startActivity(new Intent(activity, LoginActivity.class));
			return;
		}
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
		MeUtil.addCommentPraise(activity, forumNote.getGoodsSid(), forumNote.getAccSid(), message, new OnPraiseListener() {
			@Override
			public void OnPraiseed(boolean isOk,String message) {
				if(isOk)
				{	
					MeUtil.showToast(activity,"评论成功");
					//设置activity界面刷新
					MerchantCommentListActivity.instance().setNeedRefresh(true);
					//发广播告诉评论增加了
					Intent intent = new Intent();  
		            intent.setAction(Constant.BroadType.ADD_COMMENT_OK);
		            intent.putExtra(Constant.ActivityExtra.GOODSSID, forumNote.getGoodsSid());
		            activity.sendBroadcast(intent);
		            commentDialog.dismiss();
				}else
				{
					MeUtil.showToast(activity,"评论失败："+message);
				}
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
						MeImageLoader.displayImage(headPicUrl, publishUsrHead, app.getHeadIconOption());
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
					MeUtil.showToast(activity, "请求网络出错");
				}
			}

			@Override
			public void onError(String message) {
				MeUtil.showToast(activity, message);
			}

			@Override
			public void onOffline(String message) {
				MeUtil.showToast(activity, message);
			}
		});
	}
}
