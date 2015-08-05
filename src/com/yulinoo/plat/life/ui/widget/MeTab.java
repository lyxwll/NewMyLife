package com.yulinoo.plat.life.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yulinoo.plat.melife.R;

public class MeTab extends LinearLayout {
	private Context mContext;
	private OnTabSelectedListener onTabSelectedListener;
	private List<Tab> tabs;
	private List<TextView> namesView = new ArrayList<TextView>();

	public interface OnTabSelectedListener {
		public void onCheckedChanged(Tab tab);
	}

	public void setOnTabSelectedListener(OnTabSelectedListener onIabListener) {
		this.onTabSelectedListener = onIabListener;
	}

	public MeTab(Context context) {
		super(context);
		mContext = context;
	}

	public MeTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public List<Tab> getTabs() {
		return tabs;
	}	
	//我的menu中的主页和发布
	public void loadMeTab(List<Tab> tabs) {
		//this.getLayoutParams().height=SizeUtil.menu_height(mContext);
		namesView.clear();
		this.tabs = tabs;
		this.removeAllViews();

		int tsize=tabs.size();
		for(int kk=0;kk<tsize;kk++)
		{
			final Tab tab=tabs.get(kk);
			final View convertView = (View) LayoutInflater.from(mContext).inflate(R.layout.metab_item, null);
			final TextView itemName = (TextView) convertView.findViewById(R.id.item_name);
			itemName.setText(tab.getName());
			convertView.setTag(tab);
			namesView.add(itemName);

			this.addView(convertView);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			convertView.setLayoutParams(lp);
//			itemName.setLayoutParams(lp);
//			itemName.setPadding(40, 0, 40, 0);
//			itemName.setText(tab.getName());
//			itemName.setGravity(Gravity.CENTER_VERTICAL);
//			itemName.setTextSize(SizeUtil.title_menu_text_size(mContext));
//			namesView.add(itemName);
//			this.addView(itemName);
			if(kk==0)
			{
				itemName.setTextColor(mContext.getResources().getColor(R.color.white));
				itemName.setBackgroundResource(R.drawable.tab_selected);
			}else
			{
				itemName.setTextColor(mContext.getResources().getColor(R.color.black));
				itemName.setBackgroundResource(0);
			}

			//itemName.getLayoutParams().height=SizeUtil.menu_height(mContext)-20;
			itemName.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setOncheckedStatus(tab.getIndex());
					if (onTabSelectedListener != null) {
						onTabSelectedListener.onCheckedChanged(tab);
					}
				}
			});
		}
		if (onTabSelectedListener != null) {
			onTabSelectedListener.onCheckedChanged(tabs.get(0));//默认选择第一个
		}
	}
	
	public void setOncheckedStatus(int index){
		int nsize=namesView.size();
		for(int kk=0;kk<nsize;kk++)
		{
			TextView tvname=namesView.get(kk);
			if(index==kk)
			{
				tvname.setTextColor(mContext.getResources().getColor(R.color.white));
				tvname.setBackgroundResource(R.drawable.tab_selected);
			}else
			{
				tvname.setTextColor(mContext.getResources().getColor(R.color.black));
				tvname.setBackgroundResource(0);
			}
		}
	}
}
