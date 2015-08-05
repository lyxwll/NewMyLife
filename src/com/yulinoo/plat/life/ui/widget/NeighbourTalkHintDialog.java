package com.yulinoo.plat.life.ui.widget;

import com.yulinoo.plat.melife.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class NeighbourTalkHintDialog extends Dialog{

	public NeighbourTalkHintDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	private TextView hintTextView;
	
	public NeighbourTalkHintDialog(Context context) {
		super(context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.neighbour_talk_hint_dialog);
		hintTextView=(TextView) findViewById(R.id.hint_content_tv);
	}
	
	public void setHintContent(String hintContent){
		if (hintTextView!=null) {
			hintTextView.setText(hintContent);
		}
	}
}
