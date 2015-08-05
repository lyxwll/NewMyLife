package com.yulinoo.plat.life.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yulinoo.plat.life.bean.Category;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.views.activity.ForumMerchantActivity;
import com.yulinoo.plat.life.views.activity.MyNeighbourActivity;
import com.yulinoo.plat.life.views.activity.SearchActivity;
import com.yulinoo.plat.life.views.adapter.ZoneMainFunctionAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class FinderWidget extends FrameLayout implements OnClickListener{
	private Context mContext;
	private LayoutInflater mInflater;
	private View go_search;
	public FinderWidget(Context context) {
		super(context);
		mContext = context;
		init();
	}
	public FinderWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private ViewPager categoryViewPager;
	private View select_category_rl;
	private LinearLayout page_indicator;//用于显示分类页码的
	private List<ImageView> listPages=new ArrayList<ImageView>();

	private void init()
	{
		mInflater=LayoutInflater.from(mContext);
		final View view = (View)mInflater.inflate(R.layout.finder, this,false);
		go_search=view.findViewById(R.id.go_search);
		go_search.setOnClickListener(this);
		select_category_rl=view.findViewById(R.id.select_category_rl);
		select_category_rl.setVisibility(View.GONE);
		page_indicator=(LinearLayout)view.findViewById(R.id.page_indicator);
		categoryViewPager = (ViewPager) view.findViewById(R.id.categoryViewPager);
		this.addView(view);
		this.setVisibility(View.GONE);
	}
	private ImageView finder_img;
	public void setFinderImage(ImageView finder_img)
	{
		this.finder_img=finder_img;
	}

	public void setCategory()
	{
		if(select_category_rl.getVisibility()==View.GONE)
		{
			finder_img.setImageResource(R.drawable.finder_close);
			this.setVisibility(View.VISIBLE);
			select_category_rl.setVisibility(View.VISIBLE);
			Animation inani=AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in);
			select_category_rl.setAnimation(inani);
//			inani.setAnimationListener(new AnimationListener() {
//				@Override
//				public void onAnimationStart(Animation animation) {
//				}
//
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//
//				}
//
//				@Override
//				public void onAnimationEnd(Animation animation) {
//					//category_img.setImageResource(R.drawable.close);
//					
//				}
//			});
			loadCityCategory();
		}else
		{
			//			handler.postDelayed(new Runnable() {
			//				@Override
			//				public void run() {
			//					select_category_rl.setVisibility(View.GONE);
			//					category_img.setImageResource(R.drawable.more_function);
			//				}
			//			}, 200);
			finder_img.setImageResource(R.drawable.finder_open);
			select_category_rl.setVisibility(View.GONE);
			Animation outani=AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_out);
			select_category_rl.setAnimation(outani);
			outani.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					FinderWidget.this.setVisibility(View.GONE);
				}
			});
		}
	}
	public void loadCityCategory() {
		if(AppContext.currentAreaInfo()==null)
		{
			return;
		}

		initCategory(AppContext.currentCategorys(AppContext.currentAreaInfo().getAlongCitySid()));
	}
	private int categorySize=6;
	private void initCategory(List<Category> list)
	{
		if(listPages.size()>0)
		{
			listPages.clear();
		}
		List<Category> okCategory=new ArrayList<Category>();
		for(Category category:list)
		{
			if(category.getHomeDisplay()==Constant.CATEGORY.NOT_SHOW_AS_NEIGHBOUR_TALK)
			{
				okCategory.add(category);
			}
		}


		int lsize=okCategory.size();
		int page=1;
		if(lsize%categorySize==0)
		{
			page=lsize/categorySize;
		}else
		{
			page=lsize/categorySize+1;
		}
		if(page==1)
		{
			page_indicator.setVisibility(View.GONE);
		}else
		{
			page_indicator.setVisibility(View.VISIBLE);
		}
		page_indicator.removeAllViews();
		final List<View> mListViews=new ArrayList<View>();
		for(int kk=0;kk<page;kk++)
		{
			ImageView iv=new ImageView(mContext);
			android.widget.LinearLayout.LayoutParams vp=new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			iv.setLayoutParams(vp);
			vp.setMargins(7, 0, 7, 0);
			if(kk==0)
			{
				iv.setBackgroundResource(R.drawable.page_indicator_focused);
			}else
			{
				iv.setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			listPages.add(iv);
			page_indicator.addView(iv);


			View v = mInflater.inflate(R.layout.main_category_gridview, null);
			mListViews.add(v);
			GridView gridView = (GridView) v.findViewById(R.id.main_gridview);
			gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			ZoneMainFunctionAdapter zoneMainFunctionAdapter = new ZoneMainFunctionAdapter(mContext);
			gridView.setAdapter(zoneMainFunctionAdapter);
			List<Category> indexCategory=new ArrayList<Category>();
			int mk=kk*categorySize;
			int msize=(kk+1)*categorySize;
			if(msize>lsize)
			{
				msize=lsize;
			}
			for(;mk<msize;mk++)
			{
				indexCategory.add(okCategory.get(mk));
			}

			zoneMainFunctionAdapter.load(indexCategory, false);
			setOnGridViewItemListener(gridView, zoneMainFunctionAdapter);
		}
		categoryViewPager.setAdapter(new PagerAdapter() {
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
		categoryViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				int size=listPages.size();
				for(int kk=0;kk<size;kk++)
				{
					ImageView iv=listPages.get(kk);
					if(position==kk)
					{
						iv.setBackgroundResource(R.drawable.page_indicator_focused);
					}else
					{
						iv.setBackgroundResource(R.drawable.page_indicator_unfocused);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	//某个分类被选中了，则进入对应的栏目列表中
	private void setOnGridViewItemListener(GridView gridView, final ZoneMainFunctionAdapter zoneMainFunctionAdapter) {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				mContext.startActivity(new Intent(mContext, ForumMerchantActivity.class).putExtra(Constant.ActivityExtra.CATEGORY, zoneMainFunctionAdapter.getItem(arg2)));
				//hideCategory();
			}
		});
	}
	public void hideCategory()
	{
		if(select_category_rl!=null)
		{
			select_category_rl.setVisibility(View.GONE);
			this.setVisibility(View.GONE);
			finder_img.setImageResource(R.drawable.finder_open);
		}
	}
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.go_search:
		{
			mContext.startActivity(new Intent(mContext, SearchActivity.class));
			break;
		}
		}
	}




}
