package com.yulinoo.plat.life.ui.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulinoo.plat.melife.R;

@SuppressWarnings("unused")
public class InternetProgressDialog extends ProgressDialog {

	private String progressText;

	private ImageView mCancelBtn;

	private View.OnClickListener mCancelOnClickListener;

	private Context context;

	private boolean showProgress;
	TextView textView;

	public InternetProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	public void setCancelBtnListener(View.OnClickListener listener) {
		mCancelOnClickListener = listener;
	}

	public void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}

	public InternetProgressDialog(Context context, String text) {
		super(context);
		this.progressText = text;
		this.context = context;
	}

	public InternetProgressDialog(Context context, String text,
			boolean canBack, View.OnClickListener listener) {
		super(context);
		this.progressText = text;
		this.context = context;
		this.mCancelOnClickListener = listener;
	}

	public InternetProgressDialog(Context context, String text, boolean canBack) {
		super(context);
		this.progressText = text;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_progress);

		if (!"".equals(progressText)) {
			TextView textView = (TextView) findViewById(R.id.progress_msg);
			textView.setText(progressText);
		}

		// if(showProgress) {
		textView = (TextView) findViewById(R.id.progress);
		// }
	}

	public void setProgress(String progress) {
		textView.setVisibility(View.VISIBLE);
		textView.setText(progress);
	}

	public void setDismissListener(OnDismissListener listener) {
		setOnDismissListener(listener);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return false;
	}
}
