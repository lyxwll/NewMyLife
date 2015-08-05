package com.yulinoo.plat.life.views.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class ImagePreviewActivity extends Activity {
	private String[] imageUrls = null;
	private TextView preview_number;
	private List<View> listViews;
	DisplayImageOptions options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fr_image_pager);
		imageUrls = ((String) this.getIntent().getStringExtra(Constant.IntentKey.IMAGE_FILE_URLS)).split(",");
		listViews=new ArrayList<View>(imageUrls.length);
		LayoutInflater inflater = LayoutInflater.from(this);
		for(int kk=0;kk<imageUrls.length;kk++)
		{
			View imageLayout = inflater.inflate(R.layout.pager_image_item, null);
			imageLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			listViews.add(imageLayout);
		}
		preview_number = (TextView)findViewById(R.id.preview_number);
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImageAdapter());
		pager.setCurrentItem(0);
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
		preview_number.setText(1 + "/" +imageUrls.length);
		download(0);
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_stub)
		.showImageOnFail(R.drawable.ic_error)
		.resetViewBeforeLoading(true)
		.cacheOnDisk(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();
	}
	
	private class ImageAdapter extends PagerAdapter {

		private LayoutInflater inflater;

		ImageAdapter() {
			inflater = LayoutInflater.from(ImagePreviewActivity.this);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = listViews.get(position);
			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
	
	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			download(position);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	private void download(int position)
	{
		preview_number.setText(position+1 + "/" +imageUrls.length);
		View imageLayout=listViews.get(position);
		ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
		final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

		ImageLoader.getInstance().displayImage(Constant.URL.HTTP+AppContext.currentAreaInfo().getCityDomain()+MeImageLoader.DOT+Constant.URL.IMG+Constant.URL.BASE_DOMAIN+imageUrls[position], imageView, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				spinner.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
					case IO_ERROR:
						message = "Input/Output error";
						break;
					case DECODING_ERROR:
						message = "Image can't be decoded";
						break;
					case NETWORK_DENIED:
						message = "Downloads are denied";
						break;
					case OUT_OF_MEMORY:
						message = "Out Of Memory error";
						break;
					case UNKNOWN:
						message = "Unknown error";
						break;
				}
				Toast.makeText(ImagePreviewActivity.this, message, Toast.LENGTH_SHORT).show();

				spinner.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				spinner.setVisibility(View.GONE);
			}
		});
	}
	
	
//		private ViewPager mPager;//页卡内容
//		private List<View> listViews; // Tab页面列表
//		private BitmapPreview[] bitmapPreviews;
//		private String[] urls;
//		private TextView preview_number;
//
//		@Override
//		public void onCreate(Bundle savedInstanceState) {
//			super.onCreate(savedInstanceState);
//			setContentView(R.layout.image_preview);
//			preview_number = (TextView)findViewById(R.id.preview_number);
//			InitViewPager();
//			
//			findViewById(R.id.left_head).setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					finish() ;
//				}
//			});
//			
//		}
//		
//		@Override
//		public void finish() {
//			super.finish();
//			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
//		}
//
//
//		
//
//		/**
//		 * 初始化ViewPager
//		 */
//		private void InitViewPager() {
//			
//			// 载入图片资源ID
//			String[] imgUrlArray = ((String) this.getIntent().getStringExtra(Constant.IntentKey.IMAGE_FILE_URLS)).split(",");
//			
//			mPager = (ViewPager) findViewById(R.id.vPager);
//			listViews = new ArrayList<View>();
//			LayoutInflater mInflater = getLayoutInflater();
//			bitmapPreviews = new BitmapPreview[imgUrlArray.length];
//			urls = new String[imgUrlArray.length];
//			
//			for(int i= 0;i<imgUrlArray.length;i++) {
//				View v = mInflater.inflate(R.layout.imge_preview_item,null);
//				BitmapPreview bitmapPreview = (BitmapPreview)v.findViewById(R.id.pictureview);
//				listViews.add(v);
//				bitmapPreviews[i] = bitmapPreview;
//				urls[i] = imgUrlArray[i];
//			}
//			
//			
//			
//			mPager.setAdapter(new MyPagerAdapter(listViews));
//			mPager.setCurrentItem(0);
//			mPager.setOnPageChangeListener(new MyOnPageChangeListener());
//			
//			download(urls[0],bitmapPreviews[0]);
//			preview_number.setText(1 + "/" +urls.length);
//		}
//
//
//		/**
//		 * ViewPager适配器
//		 */
//		public class MyPagerAdapter extends PagerAdapter {
//			public List<View> mListViews;
//
//			public MyPagerAdapter(List<View> mListViews) {
//				this.mListViews = mListViews;
//			}
//
//			@Override
//			public void destroyItem(View arg0, int arg1, Object arg2) {
//				((ViewPager) arg0).removeView(mListViews.get(arg1));
//			}
//
//			@Override
//			public void finishUpdate(View arg0) {
//			}
//
//			@Override
//			public int getCount() {
//				return mListViews.size();
//			}
//
//			@Override
//			public Object instantiateItem(View arg0, int arg1) {
//				((ViewPager) arg0).addView(mListViews.get(arg1), 0);
//				return mListViews.get(arg1);
//			}
//
//			@Override
//			public boolean isViewFromObject(View arg0, Object arg1) {
//				return arg0 == (arg1);
//			}
//
//			@Override
//			public void restoreState(Parcelable arg0, ClassLoader arg1) {
//			}
//
//			@Override
//			public Parcelable saveState() {
//				return null;
//			}
//
//			@Override
//			public void startUpdate(View arg0) {
//			}
//		}
//
//		/**
//		 * 头标点击监听
//		 */
//		public class MyOnClickListener implements View.OnClickListener {
//			private int index = 0;
//
//			public MyOnClickListener(int i) {
//				index = i;
//			}
//
//			@Override
//			public void onClick(View v) {
//				mPager.setCurrentItem(index);
//			}
//		};
//
//		/**
//		 * 页卡切换监听
//		 */
//		public class MyOnPageChangeListener implements OnPageChangeListener {
//
//
//			@Override
//			public void onPageSelected(int arg0) {
//				preview_number.setText(arg0+1 + "/" +urls.length);
//				download(urls[arg0],bitmapPreviews[arg0]);
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//			}
//		}
//		
//		
//		private void download(final String url, final BitmapPreview bp) {
//			if (url != null) {
//				ProgressUtil.showProgress(this, "正在加载");
//				DownloadPictureService downloadPictureService = new DownloadPictureService(new IPictureDownloadCallback() {
//					@Override
//					public void onSuccess(final String fileName, final Integer id) {
//
//						ImagePreviewActivity.this.runOnUiThread(new Runnable() {
//
//							@Override
//							public void run() {
//								ProgressUtil.dissmissProgress();
//								Log.i("TAG","................................................");
//								bp.loadBitmap(fileName);
//							}
//						});
//					}
//
//					@Override
//					public void onError(Integer userid) {
//
//						ImagePreviewActivity.this.runOnUiThread(new Runnable() {
//
//							@Override
//							public void run() {
//								ProgressUtil.dissmissProgress();
//								Toast.makeText(ImagePreviewActivity.this, "加载图片异常！", Toast.LENGTH_SHORT).show();
//								finish();
//							}
//						});
//					}
//				});
//				downloadPictureService.downloadPicByDetailPage(url);
//			}
//		}

}
