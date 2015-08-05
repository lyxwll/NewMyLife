package com.yulinoo.plat.life.views.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yulinoo.plat.life.ui.widget.NeighbourTalkLeftWidget;
import com.yulinoo.plat.life.ui.widget.NeighbourTalkRightWidget;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.SharedPreferencesUtil;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class NeighbourTalkActivity extends Activity implements OnClickListener {
	private NeighbourTalkLeftWidget leftWidget;
	private NeighbourTalkRightWidget rightWidget;
	private View neighbour_talk_tip_fl;
	private View close_neighbour_talk_tip;
	private TextView leftTalk;
	private TextView rightTalk;
	private View back_btn;
	private View right_hint;
	private int openType=Constant.NEIGHBOURE_TALK_OPEN_TYPE.OPEN_CHAT;//默认打开的是聊天室
	private NeighbourTalkMoreFunction neighbourTalkMoreFunction;
	//第一次设置为true，表示第一次点击社区贴吧的时候，应该加载
	private LayoutInflater inflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.neighbour_talk);
		inflater=LayoutInflater.from(this);
		openType=getIntent().getIntExtra(Constant.ActivityExtra.NEIGHBOUR_TALK_OPEN_TYPE, Constant.NEIGHBOURE_TALK_OPEN_TYPE.OPEN_CHAT);
		leftWidget=(NeighbourTalkLeftWidget)findViewById(R.id.left_widget);
		rightWidget=(NeighbourTalkRightWidget)findViewById(R.id.right_widget);
		rightWidget.setActivity(this);
		leftTalk=(TextView)findViewById(R.id.leftTalk);
		leftTalk.setSelected(true);
		leftTalk.setOnClickListener(this);
		rightTalk=(TextView)findViewById(R.id.rightTalk);
		rightTalk.setSelected(false);
		rightTalk.setOnClickListener(this);
		if(openType==Constant.NEIGHBOURE_TALK_OPEN_TYPE.OPEN_CHAT)
		{
			leftTalk.setSelected(true);
			rightTalk.setSelected(false);
			leftWidget.show(true);
			rightWidget.show(false);
		}else
		{
			leftTalk.setSelected(false);
			rightTalk.setSelected(true);
			leftWidget.show(false);
			rightWidget.show(true);
		}

		back_btn=findViewById(R.id.back_btn);
		back_btn.setOnClickListener(this);
		right_hint=findViewById(R.id.right_hint_fl);
		right_hint.setOnClickListener(this);
		neighbourTalkMoreFunction=new NeighbourTalkMoreFunction(this,inflater, right_hint);
		
		initTip();
		if(AppContext.currentAreaInfo()==null)
		{
			MeUtil.showToast(this,getString(R.string.needconcerarea));
			return;
		}

		leftWidget.startTimer();
		initBroadCastReceiver();
	}
	private MyBroadcastReceiver goodsCommentBroadcastReceiver;
	private MyBroadcastReceiver goodsPraiseBroadcastReceiver;
	private MyBroadcastReceiver goodsAddBroadcastReceiver;
	//初始化广播接收者
	private void initBroadCastReceiver()
	{
		IntentFilter intentGoodsCommentFilter = new IntentFilter();
		intentGoodsCommentFilter.addAction(Constant.BroadType.ADD_COMMENT_OK);
		goodsCommentBroadcastReceiver=new MyBroadcastReceiver();
		this.registerReceiver(goodsCommentBroadcastReceiver, intentGoodsCommentFilter);
		IntentFilter intentGoodsPraiseFilter = new IntentFilter();
		intentGoodsPraiseFilter.addAction(Constant.BroadType.ADD_PRAISE_OK);
		goodsPraiseBroadcastReceiver=new MyBroadcastReceiver();
		this.registerReceiver(goodsPraiseBroadcastReceiver, intentGoodsPraiseFilter);
		IntentFilter intentGoodsAddFilter = new IntentFilter();
		intentGoodsAddFilter.addAction(Constant.BroadType.GOODS_ADDED);
		goodsAddBroadcastReceiver=new MyBroadcastReceiver();
		this.registerReceiver(goodsAddBroadcastReceiver, intentGoodsAddFilter);

	}

	//初始化提示
	private void initTip()
	{
		neighbour_talk_tip_fl=findViewById(R.id.neighbour_talk_tip_fl);
		//		close_neighbour_talk_tip=findViewById(R.id.close_neighbour_talk_tip);
		//		String my_neighbour_tip=SharedPreferencesUtil.getString(this, "neighbour_talk_tip", "no");
		//		if("no".equals(my_neighbour_tip))
		//		{//说明没有显示过提示
		//			neighbour_talk_tip_fl.setVisibility(View.VISIBLE);
		//			close_neighbour_talk_tip.setOnClickListener(new OnClickListener() {
		//				@Override
		//				public void onClick(View v) {
		//					SharedPreferencesUtil.save(NeighbourTalkActivity.this, "neighbour_talk_tip", "yes");
		//					neighbour_talk_tip_fl.setVisibility(View.GONE);
		//				}
		//			});
		//		}else
		//		{
		//			neighbour_talk_tip_fl.setVisibility(View.GONE);
		//		}
		neighbour_talk_tip_fl.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy() {
		rightWidget.closeForums();
		try {
			this.unregisterReceiver(goodsCommentBroadcastReceiver);
			this.unregisterReceiver(goodsPraiseBroadcastReceiver);
			this.unregisterReceiver(goodsAddBroadcastReceiver);
		} catch (Exception e) {}
		leftWidget.stopTimer();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.leftTalk:
		{
			leftTalk.setSelected(true);
			rightTalk.setSelected(false);
			leftWidget.show(true);
			rightWidget.show(false);
			//			leftWidget.setVisibility(View.VISIBLE);
			//			rightWidget.setVisibility(View.GONE);
			//			rightWidget.closeForums();
			break;
		}
		case R.id.rightTalk:
		{
			leftTalk.setSelected(false);
			rightTalk.setSelected(true);
			leftWidget.show(false);
			rightWidget.show(true);
			break;
		}
		case R.id.back_btn:
		{
			finish();
			break;
		}
		case R.id.right_hint_fl:
		{
			neighbourTalkMoreFunction.showNeighTalkMoreFunction();
			break;
		}
		}
	}

	private class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(Constant.BroadType.ADD_COMMENT_OK))
			{//评论添加了
				long goodsSid=intent.getLongExtra(Constant.ActivityExtra.GOODSSID, -1L);
				if(goodsSid>0)
				{
					rightWidget.goodsNumberAdd(goodsSid, Constant.MSGTYPE.TYPE_COMMENT);
				}
			}else if(action.equals(Constant.BroadType.ADD_PRAISE_OK))
			{//点赞
				long goodsSid=intent.getLongExtra(Constant.ActivityExtra.GOODSSID, -1L);
				if(goodsSid>0)
				{
					rightWidget.goodsNumberAdd(goodsSid, Constant.MSGTYPE.TYPE_PRAISE);
				}
			}else if(action.equals(Constant.BroadType.GOODS_ADDED))
			{//发布了新的帖子之后,重新加载一下帖子
				rightWidget.setNeedRelaodPost(true);
				rightWidget.load();
			}
		}
	}
}
