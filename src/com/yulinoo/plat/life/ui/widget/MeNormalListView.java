package com.yulinoo.plat.life.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ListView;

public class MeNormalListView extends ListView {
	public MeNormalListView(Context context) {
		super(context);
		init(context);
	}

	public MeNormalListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {
		this.setCacheColorHint(Color.TRANSPARENT);
		this.setDividerHeight(0);
	}
}
