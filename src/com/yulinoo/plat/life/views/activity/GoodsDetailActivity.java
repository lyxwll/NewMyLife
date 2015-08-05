package com.yulinoo.plat.life.views.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Comment;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.SendMessageWidget;
import com.yulinoo.plat.life.ui.widget.SendMessageWidget.OnSendMessageClickListener;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.MeUtil.OnPraiseListener;
import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.life.views.adapter.CommentAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;
////商品详情，含有评论，点赞等信息
@SuppressLint({ "InlinedApi", "ResourceAsColor" })
public class GoodsDetailActivity extends BaseActivity {
	public static final String MERCHANT="merchant";//商家信息

	private ForumNote forumNote;
	private int commentPageNo;
	private int okPageNo;
	private CommentAdapter commentAdapter;
	private TextView feel_ok;//显示他们觉得很赞的内容
	private String[] imageUrls;
	private MyApplication myapp;
	int[] picture_size;
	private SendMessageWidget send_message;
	private Comment header=new Comment();//用于填充头部的空值对象
	private XListView mListView;
	
	//更多功能
	private GoodDetailMoreFuncton goodDetailMoreFuncton;
	private LayoutInflater inflater=null;
	private View popContainer;
	private View agree;
	private View share;
	private View location;
	private View phone;
	private View private_message;
	private View focus;
	private TextView focusTextView;
	private PopupWindow popupWindow;
	//private View right_img_view;
	
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.goods_details);
		myapp=(MyApplication)getApplication();
		Intent intent=this.getIntent();
		forumNote = (ForumNote) intent.getSerializableExtra(MERCHANT);
		picture_size=SizeUtil.weibo_picture_size(mContext);
		inflater=LayoutInflater.from(this);
	}

	@Override
	protected void initComponent() {
		send_message=(SendMessageWidget)findViewById(R.id.send_message);
		send_message.setOnSendMessageClickListener(new OnSendMessageClickListener() {
			@Override
			public void onSendMessageClick(String message) {
				addComment(Constant.MSGTYPE.TYPE_COMMENT, message);
			}
		});
		
		mListView = (XListView) findViewById(R.id.list_view);
		//初始化popupWindow
		goodDetailMoreFuncton=new GoodDetailMoreFuncton(mContext, inflater, right_img_view);
		getPopView();
		commentAdapter = new CommentAdapter(this,forumNote,mListView);
		commentAdapter.setMoreFunction(goodDetailMoreFuncton, popContainer,popupWindow, agree, share, location, phone, private_message,focus,focusTextView,right_img_view);
		List<Comment> listData=new ArrayList<Comment>();
		listData.add(header);//设置一个空的，让其加载头部内容
		commentAdapter.load(listData);
	}
	
	private TextView focusMerchant;
	private ImageView uparrow;
	//设置箭头位置
	private void setUpArrow()
	{
		if(commentAdapter!=null)
		{
			commentAdapter.setUpArrow();
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {
		title.setText("详情");
		
	}

	private void addComment(final int type,String message) {
		if(!AppContext.currentAccount().getIsUsrLogin())
		{
			showToast(getString(R.string.need_login_first));
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
		MeUtil.addCommentPraise(mContext, forumNote.getGoodsSid(), forumNote.getAccSid(), message, new OnPraiseListener() {
			@Override
			public void OnPraiseed(boolean isOk,String message) {
				if(isOk)
				{
					showToast("评论成功");
					if (commentAdapter!=null) {
						commentAdapter.loadCommentsAndPraise(true);
						commentAdapter.addCommentNumber();
					}
					//MeLifeMainActivity.instance().indexFragment.goodsNumberAdd(forumNote,type);
					Intent intent = new Intent();  
		            intent.setAction(Constant.BroadType.ADD_COMMENT_OK);
		            intent.putExtra(Constant.ActivityExtra.GOODSSID, forumNote.getGoodsSid());
					mContext.sendBroadcast(intent);
				}else
				{
					showToast("评论失败："+message);
				}
			}
		});
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus)
		{
			setUpArrow();
		}
	}
	
	/**
	 * 获得更多功能的相关 view
	 */
	private void getPopView(){
		popContainer=goodDetailMoreFuncton.getGdPopupContainer();
		popupWindow=goodDetailMoreFuncton.getGdMoreFunctionPopupWindow();
		agree=goodDetailMoreFuncton.getAgree();
		share=goodDetailMoreFuncton.getShare();
		location=goodDetailMoreFuncton.getLocation();
		phone=goodDetailMoreFuncton.getPhone();
		private_message=goodDetailMoreFuncton.getPrivate_massage();
		focus=goodDetailMoreFuncton.getFocus();
		focusTextView=goodDetailMoreFuncton.getFocusTextView();
	}
}
