package com.yulinoo.plat.life.views.activity;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.DensityUtil;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.life.views.adapter.ZoneSelectAreaAdapter.OnAreaInfoConcernedListener;
import com.yulinoo.plat.melife.R;

import config.AppContext;


public class LinkedArea 
{
	private Context context;
	private LayoutInflater inflater;
	private View position;
	
	public LinkedArea(Context cxt,LayoutInflater infl,View po)
	{
		context=cxt;
		inflater=infl;
		position=po;
	}
	
	//所关联的小区
	public void linkedAreas()
	{
		List<AreaInfo> linkedAreas=AppContext.currentFocusArea();
		if(linkedAreas!=null&&linkedAreas.size()>0)
		{
			getLkAreaPopupWindow();
		}else
		{//还没有关联的小区，则进入小区选择界面
			context.startActivity(new Intent(context, NewZoneSelectActivity.class));
		}
	}
	int[] xy = new int[2];
	private PopupWindow lkAreaPopupWindow;
	/***
	 * 获取PopupWindow实例
	 */
	private void getLkAreaPopupWindow() {
		initLkAreaPopupWindow();
		lkAreaPopupWindow.showAsDropDown(position, 0,0);
	}
	private int areasize=5;//每屏显示五个小区
	//初始化关联小区弹出窗口
	private void initLkAreaPopupWindow()
	{
		View popupWindow_view = inflater.inflate(R.layout.linked_area_pop, null,false);
		LinearLayout area_container = (LinearLayout) popupWindow_view.findViewById(R.id.area_container);
		List<AreaInfo> linkedAreas=AppContext.currentFocusArea();
		int lksize=linkedAreas.size();
		if(lksize<=areasize)
		{//用户关联的小区小于5个小区
			for(final AreaInfo pi:linkedAreas)
			{
				addAreaItem(area_container,pi);
			}
			//areaContainer
		}else
		{//用户关注的小区超过了5个，则考虑使用viewpager进行实现
			ViewPager vp=new ViewPager(context);
			LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT, areasize*SizeUtil.getLinkedAreaHeight(context));
			vp.setLayoutParams(lp);
			area_container.addView(vp);
			final List<View> mListViews=new ArrayList<View>();
			int pagenum=lksize/areasize+1;//viewpager里面的页面数
			if(lksize%areasize==0)
			{
				pagenum=lksize/areasize;
			}
			for(int kk=0;kk<pagenum;kk++)
			{
				LinearLayout ll=new LinearLayout(context);
				ll.setOrientation(LinearLayout.VERTICAL);
				LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				
				//lp=new LayoutParams(LayoutParams.MATCH_PARENT, 200);
				
				ll.setLayoutParams(llp);
				mListViews.add(ll);
				int mk=kk*areasize;
				int lsize=(kk+1)*areasize;
				if(lsize>lksize)
				{
					lsize=lksize;
				}
				for(;mk<lsize;mk++)
				{
					addAreaItem(ll,linkedAreas.get(mk));
				}
			}
			vp.setAdapter(new PagerAdapter() {
				@Override  
				public void destroyItem(ViewGroup container, int position, Object object)   {     
					container.removeView(mListViews.get(position));//删除页卡  
				}

				@Override  
				public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡         
					container.addView(mListViews.get(position), 0);//添加页卡  
					return mListViews.get(position);  
				}  

				@Override  
				public int getCount() {           
					return  mListViews.size();//返回页卡的数量  
				}  

				@Override  
				public boolean isViewFromObject(View arg0, Object arg1) {             
					return arg0==arg1;//官方提示这样写  
				}
			});
		}

		View convertView = inflater.inflate(R.layout.linked_area_pop_more, null);
		((TextView) convertView.findViewById(R.id.more_area)).setText("更多小区");
		area_container.addView(convertView);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lkAreaPopupWindow.dismiss();
				context.startActivity(new Intent(context, NewZoneSelectActivity.class));
			}
		});

		// 创建PopupWindow实例,200,150分别是宽度和高度
		lkAreaPopupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, true);
		// 设置动画效果
		//lkAreaPopupWindow.setAnimationStyle(R.style.AnimationFade);
		//设置点击窗口外边窗口消失    
		lkAreaPopupWindow.setOutsideTouchable(false);     
		// 设置此参数获得焦点，否则无法点击    
		lkAreaPopupWindow.setFocusable(true);
		popupWindow_view.setFocusable(true);//comment by danielinbiti,设置view能够接听事件，标注1   
		popupWindow_view.setFocusableInTouchMode(true); //comment by danielinbiti,设置view能够接听事件 标注2   
		popupWindow_view.setOnKeyListener(new OnKeyListener(){   
			@Override   
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {   
				if (arg1 == KeyEvent.KEYCODE_BACK){   
					if(lkAreaPopupWindow != null) {   
						lkAreaPopupWindow.dismiss();
					}
				}
				return false;    
			}
		});  


		//点击其他地方消失		
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (lkAreaPopupWindow != null && lkAreaPopupWindow.isShowing()) {
					lkAreaPopupWindow.dismiss();
					lkAreaPopupWindow = null;
				}
				return false;
			}
		});	
	}
	private void addAreaItem(ViewGroup parentView,final AreaInfo ai)
	{
		View convertView = inflater.inflate(R.layout.linked_area_pop_item, null);
		((TextView) convertView.findViewById(R.id.area_name)).setText(ai.getAreaName());
		((TextView) convertView.findViewById(R.id.area_desc)).setText(context.getString(R.string.enter_merchant)+":"+ai.getEnterNum()+","+context.getString(R.string.concern_person)+":"+ai.getAttNum());
		parentView.addView(convertView);
		View cancel_Concern_area=convertView.findViewById(R.id.cancel_Concern_area);
		cancel_Concern_area.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<AreaInfo> listConcernArea=AppContext.currentFocusArea();
				if(listConcernArea!=null&&listConcernArea.size()==1)
				{
					MeUtil.showToast(context,"请确保至少有一个关注的小区");;
					return;
				}
				MeUtil.cancelConcernArea(context, ai, new OnAreaInfoConcernedListener() {
					@Override
					public void onAreaInfoConcerned(AreaInfo areaInfo, boolean isConcerned) {
						lkAreaPopupWindow.dismiss();
					}
				});
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lkAreaPopupWindow.dismiss();
				AppContext.setCurrentAreaInfo(ai);
				Intent intent = new Intent();  
	            intent.setAction(Constant.BroadType.AREA_CHANGED);  
				context.sendBroadcast(intent);
			}
		});
	}	

}