package com.yulinoo.plat.life.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("WorldReadableFiles")
public class SharedPreferencesUtil {
	public static final String FILE_NAME = "me500life";

	public static final String TMP_SID = "TMP_SID";

	public static final String TMP_SID_FOR_REGISTER = "TMP_SID_FOR_REGISTER";

	public static final String ACCESS_SID = "accSid";

	public static final String MEVALUE = "mevalue";

	public static final String NEED_OPEN_SHOP = "need_open_shop";

	public static final String LOGIN_USERNAME = "LOGIN_USERNAME";

	public static void save(Context context, String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				FILE_NAME, Context.MODE_WORLD_WRITEABLE);
		sharedPreferences.edit().putString(key, value).commit();
	}

	public static String getString(Context context, String key,
			String defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				FILE_NAME, Context.MODE_WORLD_WRITEABLE);
		return sharedPreferences.getString(key, defaultValue);
	}
}
