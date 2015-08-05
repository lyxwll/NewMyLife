package com.yulinoo.plat.life.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.views.activity.NeighbourTalkActivity;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class MenuWidget extends LinearLayout implements OnClickListener {
	private Context mContext;	
	public MenuWidget(Context context) {
		super(context);
		mContext = context;
	}
	public MenuWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public static final int MENU_INDEX=1;
	public static final int MENU_NEIGHBOUR=2;
	public static final int MENU_CONCERN=3;
	public static final int MENU_NEIGHBOURTALK=4;
	
	private int last_menu=-1;

	private View menu_index;
	private View menu_neighbour;
	private View menu_concern;
	private View menu_neighbourtalk;
	private TextView menu_index_txt;
	private TextView menu_neighbour_txt;
	private TextView menu_concern_txt;
	private TextView menu_neighbourtalk_txt;
	private View menu_concern_redHot;
	private View menu_neighbourtalk_redHot;
	private void init()
	{
		View view = LayoutInflater.from(mContext).inflate(R.layout.included_footer, this,false);
		menu_index=view.findViewById(R.id.menu_index);
		menu_index_txt=(TextView)view.findViewById(R.id.menu_index_txt);
		
		menu_neighbour=view.findViewById(R.id.menu_neighbour);
		menu_neighbour_txt=(TextView)view.findViewById(R.id.menu_neighbour_txt);
		
		menu_concern=view.findViewById(R.id.menu_concern);
		menu_concern_txt=(TextView)view.findViewById(R.id.menu_concern_txt);
		
		menu_neighbourtalk=view.findViewById(R.id.menu_neighbourtalk);
		menu_neighbourtalk_txt=(TextView)view.findViewById(R.id.menu_neighbourtalk_txt);
		
		menu_concern_redHot=view.findViewById(R.id.menu_concern_redHot);
		menu_neighbourtalk_redHot=view.findViewById(R.id.menu_neighbourtalk_redHot);
		
		menu_index.setOnClickListener(this);
		menu_neighbour.setOnClickListener(this);
		menu_concern.setOnClickListener(this);
		menu_neighbourtalk.setOnClickListener(this);
		this.addView(view);
	}

	private View index;
	private NeighbourCircleWidget neighbourCircle;
	private ConcernWidget concern;
	public void initMenu(View index,NeighbourCircleWidget neighbourCircle,ConcernWidget concern)
	{
		this.index=index;
		this.neighbourCircle=neighbourCircle;
		this.concern=concern;
		concern.setConcernMenuView(menu_concern_redHot);
		//初始化时,将首页设置为选中状态
		setMenuSelected(MENU_INDEX);
	}
	
	private FinderWidget finder;
	public void setFinderWidget(FinderWidget finder)
	{
		this.finder=finder;
	}
	
	public void setMenuSelected(int selectedMenu)
	{
		if(last_menu==selectedMenu)
		{
			return;
		}
		if(finder!=null)
		{
			finder.hideCategory();
		}
		menu_index_txt.setTextColor(mContext.getResources().getColor(R.color.black));
		menu_neighbour_txt.setTextColor(mContext.getResources().getColor(R.color.black));
		menu_concern_txt.setTextColor(mContext.getResources().getColor(R.color.black));
		menu_neighbourtalk_txt.setTextColor(mContext.getResources().getColor(R.color.black));

		index.setVisibility(View.GONE);
		neighbourCircle.setVisibility(View.GONE);
		concern.hiddenView();
		
		if(MENU_INDEX==selectedMenu)
		{
			index.setVisibility(View.VISIBLE);
			menu_index_txt.setTextColor(mContext.getResources().getColor(R.color.me_main_color));
		}else if(MENU_NEIGHBOUR==selectedMenu)
		{
			menu_neighbour_txt.setTextColor(mContext.getResources().getColor(R.color.me_main_color));
			neighbourCircle.setVisibility(View.VISIBLE);
			neighbourCircle.showNeiburCircle();
		}else if(MENU_CONCERN==selectedMenu)
		{
			concern.showView();
			menu_concern_txt.setTextColor(mContext.getResources().getColor(R.color.me_main_color));
		}else if(MENU_NEIGHBOURTALK==selectedMenu)
		{
			menu_neighbourtalk_txt.setTextColor(mContext.getResources().getColor(R.color.me_main_color));
		}
	}
	
	public void setMenuRedHot(int selectedMenu,boolean isShow)
	{
		if(MENU_CONCERN==selectedMenu)
		{
			if(isShow)
			{
				menu_concern_redHot.setVisibility(View.VISIBLE);
			}else
			{
				menu_concern_redHot.setVisibility(View.GONE);
			}
		}else if(MENU_NEIGHBOURTALK==selectedMenu)
		{
			if(isShow)
			{
				menu_neighbourtalk_redHot.setVisibility(View.VISIBLE);
			}else
			{
				menu_neighbourtalk_redHot.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.menu_index:
		{
			setMenuSelected(MENU_INDEX);
			break;
		}
		case R.id.menu_neighbour:
		{
			setMenuSelected(MENU_NEIGHBOUR);
			break;
		}
		case R.id.menu_concern:
		{
			setMenuSelected(MENU_CONCERN);
			break;
		}
		case R.id.menu_neighbourtalk:
		{
			if(AppContext.currentAreaInfo()==null)
			{
				MeUtil.showToast(mContext,mContext.getString(R.string.needconcerarea));
				return;
			}
			mContext.startActivity(new Intent(mContext, NeighbourTalkActivity.class));
			break;
		}
		}
	}



}
