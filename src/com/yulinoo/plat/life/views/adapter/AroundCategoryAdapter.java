package com.yulinoo.plat.life.views.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Advertise;
import com.yulinoo.plat.life.bean.Category;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.AdvertiseReq;
import com.yulinoo.plat.life.net.reqbean.BeanDetailReq;
import com.yulinoo.plat.life.net.resbean.AdvertiseListResponse;
import com.yulinoo.plat.life.net.resbean.GoodsDetailResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.utils.DensityUtil;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.life.views.activity.ForumMerchantActivity;
import com.yulinoo.plat.life.views.activity.GoodsDetailActivity;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class AroundCategoryAdapter extends YuLinooLoadAdapter<Advertise> {

	private Context mContext;
	private LayoutInflater inflater;
	private MyApplication app;
	private XListView mListView;

	private ImageView mainAdvertise;
	
	private ImageHandler handler;
	private MyApplication myapp;
	public AroundCategoryAdapter(Context mContext,XListView mListView) {
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
		this.app = (MyApplication) mContext.getApplicationContext();
		this.mListView=mListView;
		mListView.setPullRefreshEnable(false);
		mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mListView.setPullLoadEnable(false);
		
		handler=new ImageHandler(new WeakReference<AroundCategoryAdapter>(this));
		myapp=((MyApplication)mContext.getApplicationContext());
	}

	public static interface IViewType {
		int HEADER = 0;// 广告
		int FOOTER = 1;// 头部
	}

	@Override
	public int getItemViewType(int position) {
		Advertise item = getItem(position);
		if (item != null) {
			return IViewType.HEADER;
		} else {// 广告
			return IViewType.FOOTER;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		Holder holder;
		final Advertise advertise=getItem(position);
		if (convertview == null) {
			holder = new Holder();
			if (position == 0) {// 头部
				convertview = inflater.inflate(R.layout.main_category_gridview,parent,false);
				holder.advertiseFl=convertview.findViewById(R.id.advertise_fl);
				holder.advertiseViewPager=(ViewPager) convertview.findViewById(R.id.around_ViewPager);
				holder.pageIndicator=(LinearLayout) convertview.findViewById(R.id.around_page_indicator);
//				holder.gridView = (GridView) convertview.findViewById(R.id.main_category_gridview);
//				holder.gridView.getLayoutParams().width=AppContext.getScreenWidth(mContext);
				holder.category_container=(LinearLayout)convertview.findViewById(R.id.category_container);
				//设置gridView
				//initGridView(holder.gridView);
				initCategoryView(holder.category_container);
				//设置头部的广告
				//刚开始进来的时候,设置为看不见
				//holder.advertiseFl.setVisibility(View.GONE);
				int[] advertiseSize = SizeUtil.getAdvertiseSizeNoMargin(mContext);
				holder.advertiseFl.getLayoutParams().width = advertiseSize[0];
				holder.advertiseFl.getLayoutParams().height = advertiseSize[1];
				getHeadAdvertises(holder.advertiseFl,holder.advertiseViewPager, holder.pageIndicator);
			} else {
				convertview=inflater.inflate(R.layout.around_bottom_advertise_item, parent,false);
				mainAdvertise=(ImageView) convertview.findViewById(R.id.main_advertise_iv);
				int[] sizes=SizeUtil.getAdvertiseSizeNoMargin(mContext);
				mainAdvertise.getLayoutParams().width=sizes[0];
				mainAdvertise.getLayoutParams().height=sizes[1];
				String advertiseUrl=advertise.getUrl();
				if (advertiseUrl!=null) {
					MeImageLoader.displayImage(advertiseUrl, mainAdvertise, app.getWeiIconOption());
				}
				mainAdvertise.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						BeanDetailReq req=new BeanDetailReq();
						req.setSid(advertise.getAlongGoodsSid());
						RequestBean<GoodsDetailResponse> requestBean = new RequestBean<GoodsDetailResponse>();
						requestBean.setRequestBody(req);
						requestBean.setResponseBody(GoodsDetailResponse.class);
						requestBean.setURL(Constant.Requrl.getGoodsDetail());
						MeMessageService.instance().requestServer(requestBean, new UICallback<GoodsDetailResponse>() {
							@Override
							public void onSuccess(GoodsDetailResponse respose) {
								try {
									if(respose.isSuccess())
									{
										ForumNote forumNote=respose.getForumNote();
										if(forumNote!=null)
										{
											mContext.startActivity(new Intent(mContext, GoodsDetailActivity.class)
											.putExtra(GoodsDetailActivity.MERCHANT, forumNote));
										}
									}else
									{
										MeUtil.showToast(mContext,respose.getMsg());
									}
								} catch (Exception e) {
								}
							}

							@Override
							public void onError(String message) {
								MeUtil.showToast(mContext,message);
							}

							@Override
							public void onOffline(String message) {
								MeUtil.showToast(mContext,message);
							}
						});
					}
				});
			}
			convertview.setTag(holder);
		} else {
			holder = (Holder) convertview.getTag();
		}
		return convertview;
	}

	/**
	 * 设置头部的广告
	 */
	private List<ImageView> listLauncherPages=new ArrayList<ImageView>();
	private List<ImageView> listDotPages=new ArrayList<ImageView>();
	private List<Advertise> listResponses;


	private void initHeadAdvertise(ViewPager advertiseViewPager,LinearLayout pageIndicator){
		int advSize=listResponses.size();
		int[] advSizes=SizeUtil.getAdvertiseSizeNoMargin(mContext);
		//获取头部广告信息
		for (int i = 0; i < advSize; i++) {
			final Advertise advertise=listResponses.get(i);
			String url=advertise.getUrl();
			//广告图片
			ImageView imageView=new ImageView(mContext);
			android.widget.FrameLayout.LayoutParams vpLp=new android.widget.FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			vpLp.width=advSizes[0];
			vpLp.height=advSizes[1];
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					BeanDetailReq req=new BeanDetailReq();
					req.setSid(advertise.getAlongGoodsSid());
					RequestBean<GoodsDetailResponse> requestBean = new RequestBean<GoodsDetailResponse>();
					requestBean.setRequestBody(req);
					requestBean.setResponseBody(GoodsDetailResponse.class);
					requestBean.setURL(Constant.Requrl.getGoodsDetail());
					MeMessageService.instance().requestServer(requestBean, new UICallback<GoodsDetailResponse>() {
						@Override
						public void onSuccess(GoodsDetailResponse respose) {
							try {
								if(respose.isSuccess())
								{
									ForumNote forumNote=respose.getForumNote();
									if(forumNote!=null)
									{
										mContext.startActivity(new Intent(mContext, GoodsDetailActivity.class)
										.putExtra(GoodsDetailActivity.MERCHANT, forumNote));
									}
								}else
								{
									MeUtil.showToast(mContext,respose.getMsg());
								}
							} catch (Exception e) {
							}
						}

						@Override
						public void onError(String message) {
							MeUtil.showToast(mContext,message);
						}

						@Override
						public void onOffline(String message) {
							MeUtil.showToast(mContext,message);
						}
					});
				}
			});
			//vpLp.leftMargin=0;
			//vpLp.topMargin=0;
			//vpLp.rightMargin=0;
			//vpLp.bottomMargin=0;
			imageView.setLayoutParams(vpLp);
			MeImageLoader.displayImage(url, imageView, app.getWeiIconOption());
			listLauncherPages.add(imageView);

			if(advSize>1)
			{
				//指示器图片
				ImageView imageView2=new ImageView(mContext);
				android.widget.LinearLayout.LayoutParams vp=new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
				vp.setMargins(3, 0, 3, 0);
				imageView2.setLayoutParams(vp);
				if (i==0) {
					imageView2.setBackgroundResource(R.drawable.page_indicator_focused);
				}else {
					imageView2.setBackgroundResource(R.drawable.page_indicator_unfocused);
				}
				listDotPages.add(imageView2);
				pageIndicator.addView(imageView2);
				handler.setViewPager(advertiseViewPager);
				//开始轮播效果  
		        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);  
			}
			
		}

		advertiseViewPager.setAdapter(new PagerAdapter() {
			@Override  
			public void destroyItem(ViewGroup container, int position, Object object)   {     
				//container.removeView(listLauncherPages.get(position));//删除页卡  
			}

			@Override  
			public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡         
//				container.addView(listLauncherPages.get(position), 0);//添加页卡  
//				return listLauncherPages.get(position);
				//对ViewPager页号求模取出View列表中要显示的项  
	             position %= listLauncherPages.size();  
	             if (position<0){  
	                 position = listLauncherPages.size()+position;  
	             }  
	             ImageView view = listLauncherPages.get(position);
	             //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。  
	             ViewParent vp =view.getParent();  
	             if (vp!=null){  
	                 ViewGroup parent = (ViewGroup)vp;  
	                 parent.removeView(view);  
	             }
	             container.addView(view);    
	             //add listeners here if necessary  
	             return view;   
			}  

			@Override  
			public int getCount() {           
				//return  listLauncherPages.size();//返回页卡的数量  
				 //设置成最大，使用户看不到边界  
	            return Integer.MAX_VALUE;  
			}  

			@Override  
			public boolean isViewFromObject(View arg0, Object arg1) {             
				return arg0==arg1;//官方提示这样写  
			}
		});
		advertiseViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				int size=listDotPages.size();
				if(size>0)
				{
					position=Math.abs(position)%size;
					for(int kk=0;kk<size;kk++)
					{
						ImageView iv=listDotPages.get(kk);
						if(position==kk)
						{
							iv.setBackgroundResource(R.drawable.page_indicator_focused);
						}else
						{
							iv.setBackgroundResource(R.drawable.page_indicator_unfocused);
						}
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
	
    private static class ImageHandler extends Handler{  
        /** 
         * 请求更新显示的View。 
         */  
        protected static final int MSG_UPDATE_IMAGE  = 1;  
           
        //轮播间隔时间  
        protected static final long MSG_DELAY = 2000;  
           
        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等  
        private WeakReference<AroundCategoryAdapter> weakReference;  
        private int currentItem = 0;  
           
        protected ImageHandler(WeakReference<AroundCategoryAdapter> wk){  
            weakReference = wk;  
        }  
        private ViewPager advertiseViewPager;
        public void setViewPager(ViewPager vp)
        {
        	advertiseViewPager=vp;
        }
           
        @Override  
        public void handleMessage(Message msg) {  
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。  
            if (this.hasMessages(MSG_UPDATE_IMAGE)){  
            	this.removeMessages(MSG_UPDATE_IMAGE);  
            }  
            switch (msg.what) {  
            case MSG_UPDATE_IMAGE:  
                currentItem++;  
                advertiseViewPager.setCurrentItem(currentItem);  
                //准备下次播放  
                this.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);  
                break;  
            default:  
                break;  
            }   
        }  
    }  

	/**
	 * 获取头部广告图片
	 */
	private void getHeadAdvertises(final View advertiseFl,final ViewPager advertiseViewPager,final LinearLayout pageIndicator){
		AdvertiseReq advertiseReq=new AdvertiseReq();
		advertiseReq.setAreaSid(AppContext.currentAreaInfo().getSid());
		advertiseReq.setPosition(2);
		RequestBean<AdvertiseListResponse> requestBean=new RequestBean<AdvertiseListResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(advertiseReq);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(AdvertiseListResponse.class);
		requestBean.setURL(Constant.Requrl.getAdvertList());
		MeMessageService.instance().requestServer(requestBean, new UICallback<AdvertiseListResponse>() {

			@Override
			public void onSuccess(AdvertiseListResponse respose) {
				if (respose.getList()!=null && respose.getList().size()>0) {
					listResponses=respose.getList();
					advertiseFl.setVisibility(View.VISIBLE);
					initHeadAdvertise(advertiseViewPager,pageIndicator);
				}
			}

			@Override
			public void onError(String message) {

			}

			@Override
			public void onOffline(String message) {

			}
		});
	}


	/**
	 * 设置gridView
	 * @param gridView
	 */
	private void initCategoryView(LinearLayout category_container) {
		List<Category> categories = AppContext.currentCategorys(AppContext.currentAreaInfo().getAlongCitySid());
		List<Category> okCategory = new ArrayList<Category>();
		for (Category category : categories) {
			if (category.getHomeDisplay() == Constant.CATEGORY.NOT_SHOW_AS_NEIGHBOUR_TALK) {
				okCategory.add(category);
			}
		}
		
		int okSize=okCategory.size();
		if(okSize>0)
		{
			if(okSize<=3)
			{
				addThreeCategoryToContainer(category_container,okCategory.subList(0, okSize));
			}else if(okSize<=6)
			{
				addThreeCategoryToContainer(category_container,okCategory.subList(0, 3));
				addThreeCategoryToContainer(category_container,okCategory.subList(3, okSize));
			}else
			{
				addThreeCategoryToContainer(category_container,okCategory.subList(0, 3));
				addThreeCategoryToContainer(category_container,okCategory.subList(3, 6));
				int count=0;
				int mod=(okSize-6)%2;
				if(mod==0)
				{
					count=(okSize-6)/2;
				}else
				{
					count=(okSize-6)/2+1;
				}
				for(int kk=0;kk<count;kk++)
				{
					int ksize=6+2*kk+2;
					if(ksize>okSize)
					{
						ksize=okSize;
					}
					addTwoCategoryToContainer(category_container,okCategory.subList(6+2*kk, ksize));
				}
			}
		}
		
//		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		ZoneMainFunctionAdapter adapter = new ZoneMainFunctionAdapter(mContext);
//		adapter.load(okCategory);
//		gridView.setAdapter(adapter);
//		setOnGridViewItemListener(gridView, adapter);
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void onCategoryClicked(final View view,final Category category)
	{
		final Drawable db=view.getBackground();
		view.setBackgroundColor(mContext.getResources().getColor(R.color.background_color));
		kkhandler.postDelayed(new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				try {
					//arg1.setBackground(db);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						view.setBackground(db);
					} else {
						view.setBackgroundDrawable(db);
					}
					mContext.startActivity(new Intent(mContext,
							ForumMerchantActivity.class).putExtra(Constant.ActivityExtra.CATEGORY,category));
				} catch (Exception e) {
				}
			}
		}, 100);
	}

	private void addThreeCategoryToContainer(LinearLayout category_container,List<Category> listCategorys)
	{
		View convertview = inflater.inflate(R.layout.item_category_three,null);
		View bigger_category=convertview.findViewById(R.id.bigger_category);
		View smaller1_category=convertview.findViewById(R.id.smaller1_category);
		View smaller2_category=convertview.findViewById(R.id.smaller2_category);
		
		int size=listCategorys.size();
		if(size==1)
		{
			final Category item_bigger=listCategorys.get(0);
			setBiggerCategory(item_bigger,bigger_category);
			bigger_category.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onCategoryClicked(v,item_bigger);
				}
			});
		}else if(size==2)
		{
			final Category item_bigger=listCategorys.get(0);
			setBiggerCategory(item_bigger,bigger_category);
			bigger_category.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onCategoryClicked(v,item_bigger);
				}
			});
			final Category item_smaller1=listCategorys.get(1);
			setSmaller1Category(item_smaller1, smaller1_category);
			smaller1_category.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onCategoryClicked(v,item_smaller1);
				}
			});
		}else if(size==3)
		{
			final Category item_bigger=listCategorys.get(0);
			setBiggerCategory(item_bigger,bigger_category);
			bigger_category.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onCategoryClicked(v,item_bigger);
				}
			});
			final Category item_smaller1=listCategorys.get(1);
			setSmaller1Category(item_smaller1, smaller1_category);
			smaller1_category.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onCategoryClicked(v,item_smaller1);
				}
			});
			final Category item_smaller2=listCategorys.get(2);
			setSmaller2Category(item_smaller2, smaller2_category);
			smaller2_category.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onCategoryClicked(v,item_smaller2);
				}
			});
		}
		
		category_container.addView(convertview);
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setBiggerCategory(Category item,View bigger_category)
	{
		ImageView bigger_category_icon=(ImageView)bigger_category.findViewById(R.id.bigger_category_icon);
		TextView bigger_category_name=(TextView)bigger_category.findViewById(R.id.bigger_category_name);
		TextView bigger_category_desc=(TextView)bigger_category.findViewById(R.id.bigger_category_desc);
		MeImageLoader.displayImage(item.getCategoryPic(), bigger_category_icon, myapp.getHeadIconOption());
		bigger_category_name.setText(item.getCategoryName());
		bigger_category_desc.setText(item.getCategoryDesc());
		GradientDrawable gd=new GradientDrawable();
		if (NullUtil.isStrNotNull(item.getColor())) {
			gd.setColor(Color.parseColor("#"+item.getColor()));
			gd.setCornerRadius(DensityUtil.dip2px(mContext, 5));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				bigger_category.setBackground(gd);
			} else {
				bigger_category.setBackgroundDrawable(gd);
			}
		}
	}
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setSmaller1Category(Category item,View smaller1_category)
	{
		ImageView smaller_category_icon1=(ImageView)smaller1_category.findViewById(R.id.smaller_category_icon1);
		TextView smaller_category_name1=(TextView)smaller1_category.findViewById(R.id.smaller_category_name1);
		TextView smaller_category_desc1=(TextView)smaller1_category.findViewById(R.id.smaller_category_desc1);
		MeImageLoader.displayImage(item.getCategoryPic(), smaller_category_icon1, myapp.getHeadIconOption());
		smaller_category_name1.setText(item.getCategoryName());
		smaller_category_desc1.setText(item.getCategoryDesc());
		GradientDrawable gd=new GradientDrawable();
		if (NullUtil.isStrNotNull(item.getColor())) {
			gd.setColor(Color.parseColor("#"+item.getColor()));
			gd.setCornerRadius(DensityUtil.dip2px(mContext, 5));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				smaller1_category.setBackground(gd);
			} else {
				smaller1_category.setBackgroundDrawable(gd);
			}
		}
	}
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setSmaller2Category(Category item,View smaller2_category)
	{
		ImageView smaller_category_icon2=(ImageView)smaller2_category.findViewById(R.id.smaller_category_icon2);
		TextView smaller_category_name2=(TextView)smaller2_category.findViewById(R.id.smaller_category_name2);
		TextView smaller_category_desc2=(TextView)smaller2_category.findViewById(R.id.smaller_category_desc2);
		MeImageLoader.displayImage(item.getCategoryPic(), smaller_category_icon2, myapp.getHeadIconOption());
		smaller_category_name2.setText(item.getCategoryName());
		smaller_category_desc2.setText(item.getCategoryDesc());
		GradientDrawable gd=new GradientDrawable();
		if (NullUtil.isStrNotNull(item.getColor())) {
			gd.setColor(Color.parseColor("#"+item.getColor()));
			gd.setCornerRadius(DensityUtil.dip2px(mContext, 5));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				smaller2_category.setBackground(gd);
			} else {
				smaller2_category.setBackgroundDrawable(gd);
			}
		}
	}
	
	private void addTwoCategoryToContainer(LinearLayout category_container,List<Category> listCategorys)
	{
		View convertview = inflater.inflate(R.layout.item_category_two,null);
		View smaller1_category=convertview.findViewById(R.id.smaller1_category);
		View smaller2_category=convertview.findViewById(R.id.smaller2_category);
		
		int size=listCategorys.size();
		if(size==1)
		{
			final Category item_bigger=listCategorys.get(0);
			setTwo1Category(item_bigger,smaller1_category);
			smaller1_category.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onCategoryClicked(v,item_bigger);
				}
			});
		}else if(size==2)
		{
			final Category item_bigger=listCategorys.get(0);
			setTwo1Category(item_bigger,smaller1_category);
			smaller1_category.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onCategoryClicked(v,item_bigger);
				}
			});
			final Category item_smaller1=listCategorys.get(1);
			setTwo2Category(item_smaller1, smaller2_category);
			smaller2_category.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onCategoryClicked(v,item_smaller1);
				}
			});
		}
		category_container.addView(convertview);
	}
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setTwo1Category(Category item,View smaller1_category)
	{
		ImageView smaller_category_icon1=(ImageView)smaller1_category.findViewById(R.id.smaller_category_icon1);
		TextView smaller_category_name1=(TextView)smaller1_category.findViewById(R.id.smaller_category_name1);
		TextView smaller_category_desc1=(TextView)smaller1_category.findViewById(R.id.smaller_category_desc1);
		MeImageLoader.displayImage(item.getCategoryPic(), smaller_category_icon1, myapp.getHeadIconOption());
		smaller_category_name1.setText(item.getCategoryName());
		smaller_category_desc1.setText(item.getCategoryDesc());
		GradientDrawable gd=new GradientDrawable();
		if (NullUtil.isStrNotNull(item.getColor())) {
			gd.setColor(Color.parseColor("#"+item.getColor()));
			gd.setCornerRadius(DensityUtil.dip2px(mContext, 5));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				smaller1_category.setBackground(gd);
			} else {
				smaller1_category.setBackgroundDrawable(gd);
			}
		}
	}
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setTwo2Category(Category item,View smaller2_category)
	{
		ImageView smaller_category_icon2=(ImageView)smaller2_category.findViewById(R.id.smaller_category_icon2);
		TextView smaller_category_name2=(TextView)smaller2_category.findViewById(R.id.smaller_category_name2);
		TextView smaller_category_desc2=(TextView)smaller2_category.findViewById(R.id.smaller_category_desc2);
		MeImageLoader.displayImage(item.getCategoryPic(), smaller_category_icon2, myapp.getHeadIconOption());
		smaller_category_name2.setText(item.getCategoryName());
		smaller_category_desc2.setText(item.getCategoryDesc());
		GradientDrawable gd=new GradientDrawable();
		if (NullUtil.isStrNotNull(item.getColor())) {
			gd.setColor(Color.parseColor("#"+item.getColor()));
			gd.setCornerRadius(DensityUtil.dip2px(mContext, 5));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				smaller2_category.setBackground(gd);
			} else {
				smaller2_category.setBackgroundDrawable(gd);
			}
		}
	}
	
	
	
	private Handler kkhandler=new Handler();
//	// 某个分类被选中了，则进入对应的栏目列表中
//	private void setOnGridViewItemListener(GridView gridView, final ZoneMainFunctionAdapter zoneMainFunctionAdapter) {
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//			@Override@SuppressLint("NewApi")
//			public void onItemClick(AdapterView<?> arg0, final View arg1, final int arg2, long arg3) {
//				final Drawable db=arg1.getBackground();
//				arg1.setBackgroundColor(mContext.getResources().getColor(R.color.background_color));
//				kkhandler.postDelayed(new Runnable() {
//					@SuppressWarnings("deprecation")
//					@Override
//					public void run() {
//						try {
//							//arg1.setBackground(db);
//							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//								arg1.setBackground(db);
//							} else {
//								arg1.setBackgroundDrawable(db);
//							}
//							mContext.startActivity(new Intent(mContext,
//									ForumMerchantActivity.class).putExtra(Constant.ActivityExtra.CATEGORY,
//											zoneMainFunctionAdapter.getItem(arg2)));
//						} catch (Exception e) {
//						}
//					}
//				}, 100);
//			}
//		});
//	}

	private class Holder {
		//public GridView gridView;
		public LinearLayout category_container;
		public ViewPager advertiseViewPager;
		public LinearLayout pageIndicator;
		public View advertiseFl;
	}
}
