package com.yulinoo.plat.life.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class BaseTools {

	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	private static int oneHour=60*60*1000;
	//返回与当前时间之间的差
	public static String getChajuDate(long sourceDate)
	{
		long nowTime=System.currentTimeMillis();
		long timeCha=nowTime-sourceDate;
		int tc=(int)(timeCha/oneHour);

		if(tc==0)
		{
			return "刚刚不久";
		}else if(tc<24)
		{
			return tc+"小时前";
		}else if(tc>240)
		{//超过了10天了
			if(tc>480)
			{//超过了20天
				return "较早前";
			}else
			{
				return "十天前";
			}
		}else
		{//10天之内
			return tc/24+"天前";
		}

		//			if(tc==0)
		//			{
		//				return "刚刚不久";
		//			}else if(tc<24)
		//			{
		//				return tc+"小时前";
		//			}else if(tc<72)
		//			{
		//				int days=tc/24;
		//				return "三天前";
		//			}else if(tc<240)
		//			{
		//				return "十天前";
		//			}else
		//			{
		//				return "较早前";
		//			}
	}


	public static void main(String[] args) {
		long tmp=1418277673016l;
		System.out.println(getChajuDate(tmp));
	}

}
