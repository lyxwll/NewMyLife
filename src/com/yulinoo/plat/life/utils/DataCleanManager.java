package com.yulinoo.plat.life.utils;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.yulinoo.plat.life.ui.widget.InternetProgressDialog;

import config.ConfigCache;

public class DataCleanManager {
	/** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
	}

	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}

	/**
	 * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
	 * context
	 */

	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
	}

	/** * 按名字清除本应用数据库 * * @param context * @param dbName */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	public static void cleanAllCacheData(final Activity activity,boolean isClearShareRef) {
		if(isClearShareRef) {
			DataCleanManager.cleanSharedPreference(activity);
		}
		// 清楚数据库
		///DataCleanManager.cleanDatabases(activity);
		// 删除图片
		if(isClearShareRef) {
			return;
		}
		final InternetProgressDialog internetProgressDialog = new InternetProgressDialog(activity, "正在清除...");
		internetProgressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String downloadCacheUrl = ConfigCache.getCachePath();
					File file = new File(downloadCacheUrl);
					DeleteFile(file);
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (internetProgressDialog != null) {
								internetProgressDialog.dismiss();
							}
							Toast.makeText(activity, "清除完成", Toast.LENGTH_SHORT).show();
						}
					});
				} catch (Exception e) {
				}
			}
		}).start();

	}

	private static void DeleteFile(File file) {
		if (file.exists() == false) {
			return;
		} else {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					file.delete();
					return;
				}
				for (File f : childFile) {
					DeleteFile(f);
				}
				file.delete();
			}
		}
	}

}
