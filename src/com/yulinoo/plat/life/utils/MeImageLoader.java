package com.yulinoo.plat.life.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yulinoo.plat.life.bean.AreaInfo;

import config.AppContext;

public class MeImageLoader {
	public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options)
	{
		LayoutParams lp=imageView.getLayoutParams();
		if(lp!=null)
		{
			int width=lp.width;
			int height=lp.height;
			if(width>0&&height>0)
			{
				uri=getPictureSize(uri,width,height);
			}
		}
		AreaInfo ai=AppContext.currentAreaInfo();
		if(ai!=null)
		{
			String newUrl=Constant.URL.HTTP+AppContext.currentAreaInfo().getCityDomain()+DOT+Constant.URL.IMG+Constant.URL.BASE_DOMAIN+uri;
			ImageLoader.getInstance().displayImage(newUrl, imageView, options, animateFirstListener);
		}
	}
	public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener)
	{
		LayoutParams lp=imageView.getLayoutParams();
		if(lp!=null)
		{
			int width=lp.width;
			int height=lp.height;
			if(width>0&&height>0)
			{
				try {
					uri=getPictureSize(uri,width,height);
				} catch (Exception e) {
				}
			}
		}
		String newUrl=Constant.URL.HTTP+AppContext.currentAreaInfo().getCityDomain()+DOT+Constant.URL.IMG+Constant.URL.BASE_DOMAIN+uri;
		ImageLoader.getInstance().displayImage(newUrl, imageView, options, listener);
	}
	public static final String DOT=".";
	private static final String X="x";
	private static String getPictureSize(String sourcePic,int widht,int height)
	{
		return sourcePic+DOT+widht+X+height+sourcePic.substring(sourcePic.lastIndexOf(DOT));
	}
	
	private static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
