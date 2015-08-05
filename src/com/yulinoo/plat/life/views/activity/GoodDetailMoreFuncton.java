package com.yulinoo.plat.life.views.activity;

import com.yulinoo.plat.melife.R;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

public class GoodDetailMoreFuncton implements OnClickListener {

	private Context mContext;
	private LayoutInflater inflater;
	private View headPosition;

	public GoodDetailMoreFuncton(Context Context, LayoutInflater inflater,View position) {
		this.mContext = Context;
		this.inflater = inflater;
		this.headPosition = position;
		// 初始化
		gdPopupContainer = inflater.inflate(R.layout.good_detail_header_more_function_pop, null);
		agree = gdPopupContainer.findViewById(R.id.gd_agree_ll);
		share = gdPopupContainer.findViewById(R.id.gd_share_ll);
		location = gdPopupContainer.findViewById(R.id.gd_location_ll);
		phone = gdPopupContainer.findViewById(R.id.gd_phone_ll);
		private_massage = gdPopupContainer.findViewById(R.id.gd_private_message_ll);
		focus = gdPopupContainer.findViewById(R.id.gd_focus_ll);
		focusTextView=(TextView) gdPopupContainer.findViewById(R.id.gd_focus_tv);
	}

	int[] xy = new int[2];
	// view
	private PopupWindow gdMoreFunctionPopupWindow;
	private View gdPopupContainer;
	// 点赞
	private View agree;
	// 分享
	private View share;
	// 位置
	private View location;
	// 电话联系
	private View phone;
	// 私信
	private View private_massage;
	// 关注
	private View focus;
	//关注中的TextView
	private TextView focusTextView;

	/**
	 * 获取popupWindow实例
	 */
	public void showGdMoreFunctionPopupWindow() {
		// 创建一个popupWindow实例
		gdMoreFunctionPopupWindow = new PopupWindow(gdPopupContainer,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, true);
		// 设置点击窗口外边窗口消失
		gdMoreFunctionPopupWindow.setOutsideTouchable(false);
		// 设置此参数获得焦点，否则无法点击
		gdMoreFunctionPopupWindow.setFocusable(true);
		gdPopupContainer.setFocusable(true);// comment by danielinbiti,设置view能够接听事件，标注1
		gdPopupContainer.setFocusableInTouchMode(true); // comment by danielinbiti,设置view能够接听事件
		// 标注2
		gdPopupContainer.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK) {
					if (gdMoreFunctionPopupWindow != null) {
						gdMoreFunctionPopupWindow.dismiss();
					}
				}
				return false;
			}
		});
		// 点击其他地方消失
		gdPopupContainer.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (gdMoreFunctionPopupWindow != null && gdMoreFunctionPopupWindow.isShowing()) {
					gdMoreFunctionPopupWindow.dismiss();
					gdMoreFunctionPopupWindow = null;
				}
				return false;
			}
		});
		gdMoreFunctionPopupWindow.showAsDropDown(headPosition, 0, 0);
	}

	/**
	 * disMissPopupWindow
	 */
	public void disMissPopupWindow() {
		if (gdMoreFunctionPopupWindow != null && gdMoreFunctionPopupWindow.isShowing()) {
			gdMoreFunctionPopupWindow.dismiss();
		}
	}

	@Override
	public void onClick(View arg0) {

	}

	public PopupWindow getGdMoreFunctionPopupWindow() {
		return gdMoreFunctionPopupWindow;
	}

	public void setGdMoreFunctionPopupWindow(PopupWindow gdMoreFunctionPopupWindow) {
		this.gdMoreFunctionPopupWindow = gdMoreFunctionPopupWindow;
	}

	public View getGdPopupContainer() {
		return gdPopupContainer;
	}

	public void setGdPopupContainer(View gdPopupContainer) {
		this.gdPopupContainer = gdPopupContainer;
	}

	public View getAgree() {
		return agree;
	}

	public void setAgree(View agree) {
		this.agree = agree;
	}

	public View getShare() {
		return share;
	}

	public void setShare(View share) {
		this.share = share;
	}

	public View getLocation() {
		return location;
	}

	public void setLocation(View location) {
		this.location = location;
	}

	public View getPhone() {
		return phone;
	}

	public void setPhone(View phone) {
		this.phone = phone;
	}

	public View getPrivate_massage() {
		return private_massage;
	}

	public void setPrivate_massage(View private_massage) {
		this.private_massage = private_massage;
	}

	public View getFocus() {
		return focus;
	}

	public void setFocus(View focus) {
		this.focus = focus;
	}

	public TextView getFocusTextView() {
		return focusTextView;
	}

	public void setFocusTextView(TextView focusTextView) {
		this.focusTextView = focusTextView;
	}
}
