package com.yulinoo.plat.life.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;

@SuppressLint({ "DefaultLocale", "WorldReadableFiles", "SimpleDateFormat", "NewApi" })
public class MyScrollListView extends ListView {

	public MyScrollListView(Context context) {
		super(context);
	}
	
	public MyScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDividerHeight(0);
	}

	@Override
	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
		MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
