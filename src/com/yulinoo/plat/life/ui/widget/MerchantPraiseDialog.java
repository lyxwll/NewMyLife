package com.yulinoo.plat.life.ui.widget;

import com.yulinoo.plat.melife.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MerchantPraiseDialog extends Dialog {

	private ImageView publishHead;
	private TextView publishUsrName;
	private TextView publishTime;
	private TextView publishArea;
	private TextView publishContent;
	private TextView addPraise;

	public MerchantPraiseDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_add_praise_dialog);
		publishHead = (ImageView) findViewById(R.id.goodsPublishUsrHeaderPicture);
		publishUsrName = (TextView) findViewById(R.id.goodsPublishUsrName);
		publishTime = (TextView) findViewById(R.id.commented_usr_time);
		publishArea = (TextView) findViewById(R.id.commented_usr_area);
		publishContent = (TextView) findViewById(R.id.goodsPublishContent);
		addPraise = (TextView) findViewById(R.id.add_praise_tv);
		
		Window win = getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		win.setAttributes(lp);
	}

	public ImageView getPublishHead() {
		return publishHead;
	}

	public void setPublishHead(ImageView publishHead) {
		this.publishHead = publishHead;
	}

	public TextView getPublishUsrName() {
		return publishUsrName;
	}

	public void setPublishUsrName(TextView publishUsrName) {
		this.publishUsrName = publishUsrName;
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

	public TextView getPublishContent() {
		return publishContent;
	}

	public void setPublishContent(TextView publishContent) {
		this.publishContent = publishContent;
	}

	public TextView getAddPraise() {
		return addPraise;
	}

	public void setAddPraise(TextView addPraise) {
		this.addPraise = addPraise;
	}

}
