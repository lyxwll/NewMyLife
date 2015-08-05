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

public class MyTab extends LinearLayout {

	private Context mContext;

	private View lastClickView;

	private TextView lastClickLineView;
	private TextView lastClickTextView;

	private OnIabListener onIabListener;

	private List<Tab> tabs;
	private int nowIndex=0;//当前索引号，默认情况下为第一个，即0

	private List<TextView> linesView = new ArrayList<TextView>();
	private List<TextView> namesView = new ArrayList<TextView>();

	private float width;

	public float getCellWidth(){
		return width;
	}

	//	public void setOncheckedStatus(int index){
	//		if(lastClickTextView != null) {
	//			lastClickTextView.setTextColor(getResources().getColor(R.color.gray));
	//			
	//		}
	//		
	//		if(lastClickLineView != null) {
	//			lastClickLineView.setBackgroundColor(getResources().getColor(R.color.transparent));
	//		}
	//		//TextView thisLinesView  = linesView.get(index);
	//		TextView thisNamesView  = namesView.get(index);
	//		
	//		if(thisNamesView != null) {
	//			thisNamesView.setTextColor(getResources().getColor(R.color.mytab_selected));
	//		} 
	//		
	////		if(thisLinesView != null) {
	////			thisLinesView.setBackgroundColor(getResources().getColor(R.color.red2));
	////		}
	//		
	//		//lastClickLineView = thisLinesView;
	//		nowIndex=index;
	//		int nsize=namesView.size();
	//		for(int kk=0;kk<nsize;kk++)
	//		{
	//			TextView tv=namesView.get(kk);
	//			if(nowIndex==kk)
	//			{
	//				tv.setBackgroundResource(R.drawable.tab_selected);
	//				
	//			}else
	//			{
	//				tv.setBackgroundResource(0);
	//			}
	//		}
	//		lastClickTextView = thisNamesView;
	//		
	//		
	//	}


	public interface OnIabListener {
		public void onCheckedChanged(Tab tab);
	}

	public void setOnCheckListener(OnIabListener onIabListener) {
		this.onIabListener = onIabListener;
	}

	public MyTab(Context context) {
		super(context);
		mContext = context;
	}

	public MyTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public List<Tab> getTabs() {
		return tabs; 
	}

	//我的menu中的主页和发布
	public void loadZoneTab(List<Tab> tabs) {
		namesView.clear();
		linesView.clear();
		this.tabs = tabs;
		this.removeAllViews();
		Tab selectedTab=null;
		int tsize=tabs.size();
		int count=tsize;
		if (tsize>4) {
			count=4;
		}
		for(int kk=0;kk<tsize;kk++)
		{
			final Tab tab=tabs.get(kk);
			View convertView = null;
			String name=tab.getName();
			int namelen=name.length();
			if(namelen<3)
			{
				convertView=(View) LayoutInflater.from(mContext).inflate(R.layout.mytab_zone_item_2, null);
			}else if(namelen<4)
			{
				convertView=(View) LayoutInflater.from(mContext).inflate(R.layout.mytab_zone_item_3, null);
			}else if(namelen<5)
			{
				convertView=(View) LayoutInflater.from(mContext).inflate(R.layout.mytab_zone_item_4, null);
			}else
			{
				convertView=(View) LayoutInflater.from(mContext).inflate(R.layout.mytab_zone_item_5, null);
			}

			final TextView itemName = (TextView) convertView.findViewById(R.id.item_name);
			final TextView itemLine = (TextView) convertView.findViewById(R.id.item_line);
			itemName.setText(name);
			convertView.setTag(tab);
			namesView.add(itemName);
			itemLine.setTag(tab);
			linesView.add(itemLine);
			this.addView(convertView);

			if(tab.isSelected())
			{
				selectedTab=tab;
				itemLine.setBackgroundResource(R.color.me_main_color);
			}else
			{
				itemLine.setBackgroundResource(R.color.menu_color);
			}
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setMyZoneOncheckedStatus(tab.getIndex());
					if (onIabListener != null) {
						onIabListener.onCheckedChanged(tab);
					}
				}
			});
		}
		if(selectedTab!=null)
		{
			if (onIabListener != null) {
				onIabListener.onCheckedChanged(selectedTab);
			}
		}
	}
	public void setMyZoneOncheckedStatus(int index){
		int nsize=namesView.size();
		for(int kk=0;kk<nsize;kk++)
		{
			TextView tvline=linesView.get(kk);
			Tab tab=(Tab)tvline.getTag();
			if(index==tab.getIndex())
			{
				tvline.setBackgroundResource(R.color.me_main_color);
			}else
			{
				tvline.setBackgroundResource(R.color.menu_color);
			}
		}
	}
}
