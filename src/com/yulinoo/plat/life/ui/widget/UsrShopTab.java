package com.yulinoo.plat.life.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yulinoo.plat.melife.R;

public class UsrShopTab extends FrameLayout implements OnClickListener{

	private Context mContext;

	private View lastClickView;

	private TextView lastClickLineView;
	private TextView lastClickTextView;

	private OnIabListener onIabListener;

	private List<Tab> tabs;
	private int nowIndex=0;//当前索引号，默认情况下为第一个，即0

	private LinearLayout tabLinearLayout;
	private View moreTab;
	private FrameLayout frameLayout;

	private List<TextView> linesView = new ArrayList<TextView>();
	private List<TextView> namesView = new ArrayList<TextView>();

	private float width;

	public float getCellWidth(){
		return width;
	}

	public interface OnIabListener {
		public void onCheckedChanged(Tab tab);
	}

	public void setOnCheckListener(OnIabListener onIabListener) {
		this.onIabListener = onIabListener;
	}

	public UsrShopTab(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public UsrShopTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init()
	{
		View convertView=(View) LayoutInflater.from(mContext).inflate(R.layout.usr_shop_tab_widget, null);
		tabLinearLayout=(LinearLayout)convertView.findViewById(R.id.tabLinearLayout);
		moreTab=convertView.findViewById(R.id.more_tab_rl);
		moreTab.setVisibility(View.GONE);
		this.addView(convertView);
	}

	public List<Tab> getTabs() {
		return tabs; 
	}
	//show 
	private void load(List<Tab> tabs)
	{
		namesView.clear();
		linesView.clear();
		tabLinearLayout.removeAllViews();
		Tab selectedTab=null;
		int tsize=tabs.size();
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
			tabLinearLayout.addView(convertView);

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

	//我的menu中的主页和发布
	public void loadZoneTab(List<Tab> tabs) {
		this.tabs = tabs;
		int tsize=tabs.size();
		int count=tsize;
		if (tsize>4) {
			count=4;
			moreTab.setVisibility(View.VISIBLE);
			moreTab.setOnClickListener(this);
		}
		List<Tab> ldtbs=new ArrayList<Tab>();
		for(int kk=0;kk<count;kk++)
		{
			ldtbs.add(tabs.get(kk));
		}
		load(ldtbs);
	}
	public void setMyZoneOncheckedStatus(int index){
		int nsize=namesView.size();
		for(int kk=0;kk<nsize;kk++)
		{
			TextView tvline=linesView.get(kk);
			Tab tab=(Tab)tvline.getTag();
			if(index==tab.getIndex())
			{
				tab.setSelected(true);
				tvline.setBackgroundResource(R.color.me_main_color);
			}else
			{
				tab.setSelected(false);
				tvline.setBackgroundResource(R.color.menu_color);
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch(view.getId())
		{
		case R.id.more_tab_rl:
		{
			boolean startAdd=false;
			List<Tab> nl=new ArrayList<Tab>();
			for(Tab tab:tabs)
			{
				if(tab.isSelected())
				{
					startAdd=true;
				}
				if(startAdd)
				{
					nl.add(tab);
					if(nl.size()==4)
					{
						break;
					}
				}
			}
			nl.get(0).setSelected(false);
			nl.get(1).setSelected(true);
			load(nl);
			break;
		}
		default:
			break;
		}
	}
}
