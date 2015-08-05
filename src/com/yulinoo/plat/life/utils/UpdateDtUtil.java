package com.yulinoo.plat.life.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

//关于一些更新时间的工具类
public class UpdateDtUtil {
	private static Map<String, String> mapCache=new HashMap<String, String>();
	//获取聊天室的最后一条记录的时间
	public static Long getAreaChatRoomDt(Context mContext,long areaSid)
	{
		String getAreaChatRoomDt=mapCache.get("getAreaChatRoomDt_"+areaSid);
		if(!NullUtil.isStrNotNull(getAreaChatRoomDt))
		{//开始没有取过,则取出来
			getAreaChatRoomDt=SharedPreferencesUtil.getString(mContext, "getAreaChatRoomDt_"+areaSid, "no");
			mapCache.put("getAreaChatRoomDt_"+areaSid, getAreaChatRoomDt);
		}
		if("no".equals(getAreaChatRoomDt))
		{//之前没有存储过
			return null;
		}else
		{
			return Long.parseLong(getAreaChatRoomDt);
		}
	}
	//设置聊天室的最后一条记录的时间
	public static void setAreaChatRoomDt(Context mContext,long areaSid,long time)
	{
		mapCache.put("getAreaChatRoomDt_"+areaSid, ""+time);
		SharedPreferencesUtil.save(mContext, "getAreaChatRoomDt_"+areaSid, ""+time);
	}
	
}
