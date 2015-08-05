package com.yulinoo.plat.life.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.yulinoo.plat.melife.R;

/**
 * 操作确认对话框
 * 
 */
public class ConfirmCloseDialog extends Dialog {

	private TextView tip_messag;
	private String messageId;
	private Spanned messageSpaned;
	private View download_progress;
	private TextView progress;
	private TextView ok_button;
	private TextView cancel_button;
	private View bottom_confirm;

	private FinishCallback callback;

	private String cancelBtn;
	private String title;
	private String ok;
	private String no;

	public void setProgress(String progressContent) {
		progress.setText(progressContent);
	}

	public ConfirmCloseDialog(Context context, String tipMessage, String cancelBtn, FinishCallback callback) {
		super(context, R.style.dialog);
		this.messageId = tipMessage;
		this.callback = callback;
		this.cancelBtn = cancelBtn;
	}

	public ConfirmCloseDialog(Context context, String tipMessage, String title, String ok, String no, FinishCallback callback) {
		super(context, R.style.dialog);
		this.messageId = tipMessage;
		this.callback = callback;
		this.title = title;
		this.ok = ok;
		this.no = no;

	}

	public ConfirmCloseDialog(Context context, Spanned tipMessage, String title, String ok, String no, FinishCallback callback) {
		super(context, R.style.dialog);
		this.messageSpaned = tipMessage;
		this.callback = callback;
		this.title = title;
		this.ok = ok;
		this.no = no;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_confirm);
		initView();

	}

	private void initView() {
		this.setCancelable(false);
		download_progress = findViewById(R.id.download_progress);
		progress = (TextView) findViewById(R.id.progress);
		tip_messag = (TextView) findViewById(R.id.tip_message);
		if (messageId != null) {
			tip_messag.setText(messageId);
		}
		ok_button = (TextView) findViewById(R.id.ok_button);
		cancel_button = (TextView) findViewById(R.id.cancel_button);
		cancel_button.setText(cancelBtn);
		bottom_confirm = findViewById(R.id.bottom_confirm);
		TextView title_text = (TextView) findViewById(R.id.title_text);

		if (ok != null) {
			ok_button.setText(ok);
		}

		if (no != null) {
			cancel_button.setText(no);
		}

		if (title != null) {
			title_text.setText(title);
			findViewById(R.id.update_remark).setVisibility(View.GONE);
		}

		if (messageSpaned != null) {
			tip_messag.setText(messageSpaned);
		}

		ok_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callback != null) {
					callback.confirmThisOperation();
					download_progress.setVisibility(View.VISIBLE);
					bottom_confirm.setVisibility(View.GONE);
				}
			}
		});

		cancel_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (callback != null) {
					callback.cancle();
					dismiss();
				}
			}
		});

	}
	private int showNumber=1;
	public void downLoadPlus(long count,long length)
	{
		if(count/(showNumber*256000)>0)
		{
			showNumber++;
			double oklen=Math.floor(count*100/length);
			progress.setText(oklen+"%");
		}
	}

	public interface FinishCallback {

		public void confirmThisOperation();

		public void cancle();
	}

}
