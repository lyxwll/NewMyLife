package com.yulinoo.plat.life.views.activity;

import com.yulinoo.plat.life.ui.widget.NeighbourTalkHintDialog;
import com.yulinoo.plat.melife.R;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;

public class NeighbourTalkMoreFunction implements OnClickListener{

	private Context mContext;
	private LayoutInflater inflater;
	private View headPosition;

	private View popContainer;
	private PopupWindow popupWindow;
	private TextView about_talk;
	private TextView about_post;
	private NeighbourTalkHintDialog hintDialog;

	public NeighbourTalkMoreFunction(Context mContext,LayoutInflater inflater,View headPosition) {
		this.mContext=mContext;
		this.inflater=inflater;
		this.headPosition=headPosition;
	}

	public void showNeighTalkMoreFunction(){
		popContainer=inflater.inflate(R.layout.neighbour_talk_more_function, null,false);
		about_talk=(TextView) popContainer.findViewById(R.id.about_talk_tv);
		about_talk.setOnClickListener(this);
		about_post=(TextView) popContainer.findViewById(R.id.about_post_tv);
		about_post.setOnClickListener(this);
		hintDialog=new NeighbourTalkHintDialog(mContext/*,R.style.neighbour_dialog*/);
		hintDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		popupWindow=new PopupWindow(popContainer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
		popupWindow.setOutsideTouchable(false);
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
		case R.id.about_talk_tv:
			disMissPopupWindow();
			hintDialog.show();
			hintDialog.setHintContent(mContext.getResources().getString(R.string.about_talk));
			break;
		case R.id.about_post_tv:
			disMissPopupWindow();
			hintDialog.show();
			hintDialog.setHintContent(mContext.getResources().getString(R.string.about_post));
			break;
		}
	}
}
