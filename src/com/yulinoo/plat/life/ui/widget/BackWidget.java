package com.yulinoo.plat.life.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.yulinoo.plat.melife.R;

public class BackWidget extends RelativeLayout {
	private Context mContext;
	public BackWidget(Context context) {
		super(context);
		mContext = context;
	}
	public BackWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		final View convertView = (View) LayoutInflater.from(context).inflate(R.layout.included_back_btn, null);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(backBtnClickListener!=null)
				{
					
					backBtnClickListener.onBackBtnClick();
				}
			}
		});
		this.addView(convertView);
	}
	private OnBackBtnClickListener backBtnClickListener;
	
	public interface OnBackBtnClickListener
	{
		public void onBackBtnClick();
	}

	public OnBackBtnClickListener getBackBtnClickListener() {
		return backBtnClickListener;
	}

	public void setBackBtnClickListener(OnBackBtnClickListener backBtnClickListener) {
		this.backBtnClickListener = backBtnClickListener;
	}
	
}
