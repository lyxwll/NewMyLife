package com.yulinoo.plat.life.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.MessageBox;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.service.MeMessageService.OnMessageBoxArrivedListener;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.melife.R;
//消息中心
public class MessageCenterActivity extends BaseActivity implements OnClickListener,OnMessageBoxArrivedListener{
	private RelativeLayout comment_rl;
	private RelativeLayout ok_rl;
	private ImageView comment_redHot;
	private ImageView agreed_redHot;
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.message_center);
	}

	@Override
	protected void initComponent() {
		comment_rl=(RelativeLayout)findViewById(R.id.comment_rl);
		ok_rl=(RelativeLayout)findViewById(R.id.ok_rl);
		comment_redHot=(ImageView)findViewById(R.id.comment_redHot);
		agreed_redHot=(ImageView)findViewById(R.id.agreed_redHot);

		comment_rl.setOnClickListener(this);
		ok_rl.setOnClickListener(this);
		if(MeMessageService.isReady())
		{
			MeMessageService.instance().addOnMessageBoxArrivedListener(this);
			MeMessageService.instance().loadMessageBox();
		}
	}

	@Override
	protected void onDestroy() {
		MeMessageService.instance().removeOnMessageBoxArrivedListener(this);
		super.onDestroy();
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {
		//rightImg.setBackgroundResource(R.drawable.more_btn);
		rightImg.setVisibility(View.GONE);
//		rightText.setVisibility(View.VISIBLE);
//		rightText.setText(getString(R.string.zhen_readed));
//		Account account=AppContext.currentAccount();
//		if(Constant.SEX.SEX_WOMAN==account.getSex())
//		{
//			rightText.setText(getString(R.string.bg_readed));
//		}
//		rightText.setOnClickListener(this);
		title.setText("消息中心");

	}
	


	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		case R.id.comment_rl:
		{
			showCommentRedHot(false);
			startActivity(new Intent(this, MyCommentListActivity.class));
			break;
		}
		case R.id.ok_rl:
		{
			showAgreedRedHot(false);
			startActivity(new Intent(this, MyPraiseListActivity.class));
			break;
		}
		case R.id.right_text:
		{
			
			break;
		}
		}
	}

	@Override
	public void onMessageBoxArrived(MessageBox box) {
		int commentNumber=box.getCommentNumber();
		if(commentNumber>0)
		{
			showCommentRedHot(true);
		}else
		{
			showCommentRedHot(false);
		}
		int praiseNumber=box.getPraiseNumber();
		if(praiseNumber>0)
		{
			showAgreedRedHot(true);
		}else
		{
			showAgreedRedHot(false);
		}
	}

	public void showCommentRedHot(boolean isShow)
	{
		if(isShow)
		{
			comment_redHot.setVisibility(View.VISIBLE);
		}else
		{
			comment_redHot.setVisibility(View.GONE);
		}
	}
	public void showAgreedRedHot(boolean isShow)
	{
		if(isShow)
		{
			agreed_redHot.setVisibility(View.VISIBLE);
		}else
		{
			agreed_redHot.setVisibility(View.GONE);
		}
	}
	


}
