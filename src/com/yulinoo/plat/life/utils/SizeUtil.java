package com.yulinoo.plat.life.utils;

import android.content.Context;
import config.AppContext;

public class SizeUtil {

	// 菜单栏高度
	public static int menu_height(Context context) {
		int screenHeight = AppContext.getScreenHeight(context);
		if (screenHeight <= 600) {// 小于480的屏幕
			return 36;
		} else if (screenHeight <= 1000) {
			return 55;
		} else if (screenHeight <= 1300) {
			return 80;
		} else {
			return 100;
		}
	}

	public static int[] getAdvertiseSize(Context context) {
		int screenWidth = AppContext.getScreenWidth(context) - 2
				* DensityUtil.dip2px(context, 10);
		return new int[] { screenWidth, screenWidth * 25 / 64 };
	}

	public static int[] getAdvertiseSizeNoMargin(Context context) {
		int screenWidth = AppContext.getScreenWidth(context);
		return new int[] { screenWidth, screenWidth * 25 / 64 };
	}

	public static int getLinkedAreaHeight(Context context) {
		return DensityUtil.dip2px(context, 60);
	}

	// 标题栏标题与菜单项字体大小
	public static int title_menu_text_size(Context context) {
		int screenHeight = AppContext.getScreenHeight(context);
		if (screenHeight <= 600) {// 小于480的屏幕
			return 15;
		} else if (screenHeight <= 960) {
			return 16;
		} else {
			return 18;
		}
	}

	// 背景图像
	public static int[] background_size(Context context) {
		int screenWidth = AppContext.getScreenWidth(context);
		int size[] = new int[3];
		size[0] = screenWidth;
		size[1] = screenWidth * 3 / 5;
		// size[2]=screenWidth*20/108;
		return size;
	}

	private static int size[];

	// 商品内容中图片的size
	public static int[] weibo_picture_size(Context context) {
		if (size == null) {
			int screenWidth = AppContext.getScreenWidth(context) - 2
					* DensityUtil.dip2px(context, 10);
			size = new int[2];
			size[0] = screenWidth;
			size[1] = (size[0]) * 3 / 5;
		}

		return size;
	}

	// 我的近邻中地图圈的绽放比例
	public static float my_neighbour_map_size(Context context) {
		int screenHeight = AppContext.getScreenWidth(context);
		if (screenHeight <= 480) {// 小于480的屏幕
			return 15.9f;
		} else if (screenHeight <= 540) {
			return 16.2f;
		} else if (screenHeight <= 640) {
			return 16.3f;
		} else if (screenHeight <= 720) {
			return 16.4f;
		} else if (screenHeight <= 1300) {
			return 16.5f;
		} else {
			return 16.6f;
		}
	}

	// 我的关注下面的菜单选项宽高
	public static int[] usr_zone_sex_size(Context context) {
		int screenHeight = AppContext.getScreenHeight(context);
		if (screenHeight <= 600) {// 小于480的屏幕
			return new int[] { 50, 50 };
		} else if (screenHeight <= 960) {
			return new int[] { 60, 60 };
		} else {
			return new int[] { 100, 100 };
		}
	}

}
