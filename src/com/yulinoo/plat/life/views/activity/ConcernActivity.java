package com.yulinoo.plat.life.views.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.MessageBox;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.service.MeMessageService.OnMessageBoxArrivedListener;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.ConcernWidget;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.melife.R;

public class ConcernActivity extends BaseActivity {

	private ConcernWidget concernWidget;

	private BroadcastReceiver subscribeReadedBroadcastReceiver;

	private static ConcernActivity instance=null;

	public static final  ConcernActivity instance(){
		if (instance!=null) {
			return instance;
		}else {
			throw new RuntimeException("ConcernActivity not instantiated yet");
		}
	}

//	private boolean concernAttUpdate=false;
//
//	public  void setConcernAttUpdate(boolean concernAttUpdate){
//		this.concernAttUpdate=concernAttUpdate;
//	}
//
//	private MessageBox box;
//
//	public void setMessageBox(MessageBox box){
//		this.box=box;
//	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.concren_activity_layout);
		instance=this;
		concernWidget=(ConcernWidget) findViewById(R.id.concern_widget);
		initBroadcastReceiver();
		
	}

	private void initBroadcastReceiver(){
		subscribeReadedBroadcastReceiver=new MyBroadcastReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction(Constant.BroadType.SUBSCRIBE_READED);
		this.registerReceiver(subscribeReadedBroadcastReceiver, filter);
	}

	@Override
	protected void initComponent() {
		concernWidget.refreshConcern();
//		if (concernAttUpdate) {// 说明有关注的商家/好友有状态变更
//			concernWidget.setSubscribeNumber(box.getAttUpdate());
//		}
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,
			View RightImaView, TextView rightText, TextView title,
			View titleLayout) {
		title.setText("我的关注");
	}

	private class MyBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String action=arg1.getAction();
			if (action.equals(Constant.BroadType.SUBSCRIBE_READED)) {
				if (concernWidget!=null) {
					concernWidget.refreshConcern();
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		try {
			this.unregisterReceiver(subscribeReadedBroadcastReceiver);
		} catch (Exception e) {
		}
		super.onDestroy();
	}

}
