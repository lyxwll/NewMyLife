package com.yulinoo.plat.life.ui.widget;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yulinoo.plat.life.ui.widget.MeFaceViewWiget.OnFaceClickListener;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.melife.R;

public class SendMessageWidget extends LinearLayout implements OnClickListener{
	private Context mContext;
	private EditText comment_et;
	private TextView publish_comment;
	private MeFaceViewWiget meface;
	private ImageView ivIcon = null;
	public SendMessageWidget(Context context) {
		super(context);
		mContext = context;
		initView();
	}
	public SendMessageWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}
	
	private void initView()
	{
		final View convertView = (View) LayoutInflater.from(mContext).inflate(R.layout.included_send_message, null);
		comment_et = (EditText) convertView.findViewById(R.id.comment_et);
		ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
		ivIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (meface.getVisibility()==View.VISIBLE) {
					meface.setVisibility(View.GONE);
				} else {
					meface.setVisibility(View.VISIBLE);
				}
			}
		});
		publish_comment = (TextView) convertView.findViewById(R.id.publish_comment);
		publish_comment.setOnClickListener(this);
		meface = (MeFaceViewWiget) convertView.findViewById(R.id.meface);
		meface.setOnFaceClickListener(new OnFaceClickListener() {
			@Override
			public void onFaceClick(SpannableString spannableString) {
				comment_et.append(spannableString);
			}
		});
		
		this.addView(convertView);
	}
	@Override
	public void onClick(View v) {
		if(onSendMessageClickListener!=null)
		{
			String message=comment_et.getText().toString();
			if(NullUtil.isStrNotNull(message))
			{
				onSendMessageClickListener.onSendMessageClick(message);
				comment_et.setText(null);
				InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);  
				//imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				imm.hideSoftInputFromWindow(comment_et.getWindowToken(), 0);
				meface.setVisibility(View.GONE);
			}else
			{
				MeUtil.showToast(mContext,"不能发布空信息！");
			}
		}
	}
	
	private OnSendMessageClickListener onSendMessageClickListener;
	
	public OnSendMessageClickListener getOnSendMessageClickListener() {
		return onSendMessageClickListener;
	}
	public void setOnSendMessageClickListener(
			OnSendMessageClickListener onSendMessageClickListener) {
		this.onSendMessageClickListener = onSendMessageClickListener;
	}

	public interface OnSendMessageClickListener
	{
		public void onSendMessageClick(String message);
	}
	
	
}
