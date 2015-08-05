package com.yulinoo.plat.life.ui.widget;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yulinoo.plat.life.ui.widget.bean.MeMenu;
import com.yulinoo.plat.melife.R;

//主菜单btn封装
public class MeMenuWidget extends LinearLayout {
	private Context mContext;
	private List<MeMenu> lstMenus;
	public MeMenuWidget(Context context) {
		super(context);
		mContext = context;
	}
	
	public MeMenuWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;		
	}
	
	public void loadMenu(List<MeMenu> lstMenus)
	{
		this.lstMenus=lstMenus;
		int menusize=lstMenus.size();
		for(int kk=0;kk<menusize;kk++)
		{
			MeMenu menu=lstMenus.get(kk);
			final View convertView = (View) LayoutInflater.from(mContext).inflate(R.layout.main_menu_widget, null);
			LinearLayout.LayoutParams llp=new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
			convertView.setLayoutParams(llp);
			this.addView(convertView);
			
			
			
			
		}
		
	}
	
}
