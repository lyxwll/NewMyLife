package com.yulinoo.plat.life.views.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.widget.TextView;

import com.amap.api.mapcore.ac;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.IndexWidget;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class IndexActivity extends BaseActivity{

	private IndexWidget indexWidget;
	private BroadcastReceiver goodsCommentBroadcastReceiver;
	private BroadcastReceiver goodPraiseBroadcastReceiver;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.index_activity_layout);
		indexWidget=(IndexWidget) findViewById(R.id.index_widget);
		initBroadcastReceiver();
	}

	private void initBroadcastReceiver(){
		goodsCommentBroadcastReceiver=new MyBroadcastReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction(Constant.BroadType.ADD_COMMENT_OK);
		this.registerReceiver(goodsCommentBroadcastReceiver, filter);

		goodPraiseBroadcastReceiver=new MyBroadcastReceiver();
		IntentFilter filter2=new IntentFilter();
		filter2.addAction(Constant.BroadType.ADD_PRAISE_OK);
		this.registerReceiver(goodPraiseBroadcastReceiver, filter2);
	}

	@Override
	protected void initComponent() {
		indexWidget.loadIndex();
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,
			View RightImaView, TextView rightText, TextView title,
			View titleLayout) {
		if (AppContext.currentAreaInfo()!=null) {
			title.setText(AppContext.currentAreaInfo().getAreaName());
		}
	}

	private class MyBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String action=arg1.getAction();
			if (action.equals(Constant.BroadType.ADD_COMMENT_OK)) {
				System.out.println("评论增加了!!!!!");
				long goodsSid = arg1.getLongExtra(Constant.ActivityExtra.GOODSSID, -1L);
				if (goodsSid > 0 && indexWidget != null) {
					indexWidget.goodsNumberAdd(goodsSid,Constant.MSGTYPE.TYPE_COMMENT);
				}
			}else if (action.equals(Constant.BroadType.ADD_PRAISE_OK)) {
				long goodsSid = arg1.getLongExtra(Constant.ActivityExtra.GOODSSID, -1L);
				if (goodsSid > 0 && indexWidget != null) {
					indexWidget.goodsNumberAdd(goodsSid, Constant.MSGTYPE.TYPE_PRAISE);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		try {
			this.unregisterReceiver(goodsCommentBroadcastReceiver);
			this.unregisterReceiver(goodPraiseBroadcastReceiver);
		} catch (Exception e) {
		}
		super.onDestroy();
	}

}
