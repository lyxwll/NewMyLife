package com.yulinoo.plat.life.ui.widget;

import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.ui.widget.MeFaceViewWiget.OnFaceClickListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.MeUtil.OnPraiseListener;
import com.yulinoo.plat.life.views.activity.LoginActivity;
import com.yulinoo.plat.melife.R;

import config.AppContext;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MerchantCommentDialog extends Dialog {
	
	private Context mContext;
	private ForumNote forumNote;
	
	private TextView sy_text_number;
	private ImageView ivIcon = null;
	private MeFaceViewWiget meface;
	private TextView publish;
	private EditText des;
	
	private ImageView publishUsrHead;
	private TextView publishUsrName;
	private TextView publishContent;
	private TextView publishTime;
	private TextView publishArea;

	public MerchantCommentDialog(Context context,ForumNote forumNote) {
		super(context);
		this.mContext=context;
		this.forumNote=forumNote;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_add_comment_dialog);
		des = (EditText) findViewById(R.id.add_comment_et);
		sy_text_number=(TextView)findViewById(R.id.sy_text_number_tv);
		ivIcon = (ImageView) findViewById(R.id.face_icon);
		meface = (MeFaceViewWiget) findViewById(R.id.meface_widget);
		publish=(TextView)findViewById(R.id.publish_tv);
		
		publishUsrHead=(ImageView) findViewById(R.id.goodsPublishUsrHeaderPicture);
		publishUsrName=(TextView) findViewById(R.id.goodsPublishUsrName);
		publishContent=(TextView) findViewById(R.id.goodsPublishContent);
		publishTime=(TextView) findViewById(R.id.commented_usr_time);
		publishArea=(TextView) findViewById(R.id.commented_usr_area);
		
		Window win = getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		win.setAttributes(lp);
	}
	
	public ImageView getIvIcon(){
		return ivIcon;
	}
	
	public MeFaceViewWiget getMeFace(){
		return meface;
	}
	
	public TextView getPublish(){
		return publish;
	}
	
	public EditText getDes(){
		return des;
	}
	
	public TextView getSyTextNumber(){
		return sy_text_number;
	}

	public ImageView getPublishUsrHead() {
		return publishUsrHead;
	}

	public void setPublishUsrHead(ImageView publishUsrHead) {
		this.publishUsrHead = publishUsrHead;
	}

	public TextView getPublishUsrName() {
		return publishUsrName;
	}

	public void setPublishUsrName(TextView publishUsrName) {
		this.publishUsrName = publishUsrName;
	}

	public TextView getPublishContent() {
		return publishContent;
	}

	public void setPublishContent(TextView publishContent) {
		this.publishContent = publishContent;
	}

	public TextView getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(TextView publishTime) {
		this.publishTime = publishTime;
	}

	public TextView getPublishArea() {
		return publishArea;
	}

	public void setPublishArea(TextView publishArea) {
		this.publishArea = publishArea;
	}
	
}
