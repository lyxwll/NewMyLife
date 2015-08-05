package com.yulinoo.plat.life;

import java.util.HashMap;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.activeandroid.ActiveAndroid;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.yulinoo.plat.life.utils.DataCleanManager;
import com.yulinoo.plat.life.utils.SharedPreferencesUtil;
import com.yulinoo.plat.melife.R;

@SuppressLint("WorldReadableFiles")
public class MyApplication extends Application {

	public static final String NEED_LOAD_CONCERN = "need_load_concern";

	public static final String NEED_LOAD_NEIGHBOUR = "need_load_neighbour";

	public static final String NEED_LOAD_ZONE = "need_load_zone";

	public static final String LOCATION_POINT = "location_point";

	private HashMap<String, Object> map = new HashMap<String, Object>();

	public void put(String key, Object object) {
		map.put(key, object);
	}

	public Object get(String key) {
		return map.get(key);
	}

	private DisplayImageOptions headIconOption;// 显示图片配置信息
	private DisplayImageOptions weiboIconOption;// 显示图片配置信息

	@Override
	public void onCreate() {
		super.onCreate();

		int DB_VERSION = 5;
		@SuppressWarnings("deprecation")
		final SharedPreferences sharedPreferences = this.getSharedPreferences(
				"userInfo", Context.MODE_WORLD_READABLE);
		int database_version_check = sharedPreferences.getInt(
				"database_version_check", DB_VERSION);

		if (database_version_check <= DB_VERSION) {
			SharedPreferencesUtil.save(getApplicationContext(), "randCode",
					"no");
			// DataCleanManager.cleanDatabases(this);
			{
				DataCleanManager.cleanDatabaseByName(getApplicationContext(),
						"AreaInfo");
				DataCleanManager.cleanDatabaseByName(getApplicationContext(),
						"CityCategory");
				DataCleanManager.cleanDatabaseByName(getApplicationContext(),
						"ChatMessage");
				DataCleanManager.cleanDatabaseByName(getApplicationContext(),
						"ConcernNumber");
				DataCleanManager.cleanDatabaseByName(getApplicationContext(),
						"ForumInfo");
				DataCleanManager.cleanDatabaseByName(getApplicationContext(),
						"Merchant");
				DataCleanManager.cleanDatabaseByName(getApplicationContext(),
						"MessageCenterInfo");
				DataCleanManager.cleanDatabaseByName(getApplicationContext(),
						"Thumbnail");
			}
			SharedPreferencesUtil.save(this, "isFirst", "yes");
			Editor editor = sharedPreferences.edit();
			editor.putInt("database_version_check", DB_VERSION + 1);
			editor.commit();
		}
		ActiveAndroid.initialize(this);

		initImageLoader(getApplicationContext());

		headIconOption = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(100)).build();
		weiboIconOption = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();

		String randCode = SharedPreferencesUtil.getString(
				getApplicationContext(), "randCode", "no");
		if ("no".equals(randCode)) {// 随机码还没有生成
			UUID uuid = UUID.randomUUID();
			String temp = uuid.toString().replaceAll("-", "");
			SharedPreferencesUtil.save(getApplicationContext(), "randCode",
					temp);
		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
	}

	public static void initImageLoader(Context context) {
		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(context)
		// .threadPriority(Thread.NORM_PRIORITY - 2)
		// .denyCacheImageMultipleSizesInMemory()
		// .diskCacheFileNameGenerator(new Md5FileNameGenerator())
		// .diskCacheSize(50 * 1024 * 1024) // 50 Mb
		// .tasksProcessingOrder(QueueProcessingType.LIFO)
		// .build();
		// File cacheDir =
		// StorageUtils.getOwnCacheDirectory(context.getApplicationContext(),
		// "500me/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(480, 800)
				// max width, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(5 * 1024 * 1024))
				// You can pass your own memory cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(5 * 1024 * 1024)
				.diskCacheSize(50 * 1024 * 1024)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCacheFileCount(300)
				// 缓存的文件数量
				// .diskCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
																				// (5
																				// s),
																				// readTimeout
																				// (30
																				// s)超时时间
				// .writeDebugLogs() // Remove for release app
				.build();// 开始构建
		ImageLoader.getInstance().init(config);

	}

	/**
	 * 得到显示头像图片配置信息实例
	 * 
	 * @return
	 */
	public DisplayImageOptions getHeadIconOption() {
		return headIconOption;
	}

	public DisplayImageOptions getWeiIconOption() {
		return weiboIconOption;
	}
}
