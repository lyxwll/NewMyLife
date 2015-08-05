package com.yulinoo.plat.life.views.activity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.MeUtil.OnCityForumLoadListener;
import com.yulinoo.plat.life.utils.MeUtil.OnLoadCityCategoryListener;
import com.yulinoo.plat.life.utils.MeUtil.OnLoginListener;
import com.yulinoo.plat.life.utils.SharedPreferencesUtil;
import com.yulinoo.plat.melife.R;

import config.AppContext;

/**
 * 
 * @author: wll
 */
public class MeLifeLauncherActivity extends BaseActivity {

	private WelcomeHandler mHandler;
	private ServiceWaitThread mThread;
	private View first_fl;
	private View not_first_fl;
	private ImageView loading_advtise;
	private long startTime = 0;
	private long endTime = 0;
	int loadNumber = 0;
	int maxLoadNumber = 2;
	public static final int LOAD_DONE = 100;// 加载数据完成/
	private LayoutInflater inflater;

	private ProgressBar progressBar;

	@Override
	protected void initView(Bundle savedInstanceState) {

		setContentView(R.layout.launcher);
		mHandler = new WelcomeHandler(this);
		inflater = LayoutInflater.from(this);
		startTime = endTime = System.currentTimeMillis();

	}

	@Override
	protected void initComponent() {
		first_fl = findViewById(R.id.first_fl);
		not_first_fl = findViewById(R.id.not_first_fl);
		loading_advtise = (ImageView) findViewById(R.id.loading_advtise);
		progressBar = (ProgressBar) findViewById(R.id.pb);

		dealScreen();
		initLauncherPage();
		startService();
		initAdvtise();
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,
			View RightImaView, TextView rightText, TextView title,
			View titleLayout) {
	}

	/**
	 * 初始化广告
	 */
	@SuppressWarnings("deprecation")
	private void initAdvtise() {
		String filePath = StorageUtils.getCacheDirectory(
				getApplicationContext()).getAbsolutePath();//
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator + "thumb" + File.separator;
		}
		filePath += "load_adv_url.png";
		File file = new File(filePath);
		if (file.exists()) {// 说明有广告页,则显示广告
			Drawable drawable = Drawable.createFromPath(filePath);
			loading_advtise.setBackgroundDrawable(drawable);
		}
	}

	private void startService() {
		startService(new Intent(Intent.ACTION_MAIN).setClass(this,
				MeMessageService.class));
		mThread = new ServiceWaitThread();
		mThread.start();
	}

	private class ServiceWaitThread extends Thread {
		public void run() {
			while (!MeMessageService.isReady()) {
				try {
					sleep(30);
				} catch (InterruptedException e) {
					throw new RuntimeException(
							"waiting thread sleep() has been interrupted");
				}
			}
			mThread = null;
			mHandler.sendEmptyMessage(1);// 服务启动完成
		}
	}

	static class WelcomeHandler extends Handler {
		WeakReference<MeLifeLauncherActivity> mActivity;

		WelcomeHandler(MeLifeLauncherActivity activity) {
			mActivity = new WeakReference<MeLifeLauncherActivity>(activity);
		}

		private long time_wait = 3000;// 启动时等待的时长

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: {// 进入主页面
						// if(!mActivity.get().isFirstLogin)
						// {//不是第一次登录
						// mActivity.get().startMainActivity();
						// }
				break;
			}
			case 1: {// 服务启动完毕
				mActivity.get().initAccount();// 初始化账号
				break;
			}
			case LOAD_DONE: {
				mActivity.get().loadNumber++;
				mActivity.get().endTime = System.currentTimeMillis();
				if (mActivity.get().loadNumber > mActivity.get().maxLoadNumber) {// 可以启动进入应用了
					long delay_time = mActivity.get().endTime
							- mActivity.get().startTime;
					if (delay_time > time_wait) {
						delay_time = 0;
					} else {
						delay_time = time_wait - delay_time;
					}
					mActivity.get().startMainActivity(delay_time);
				}
				break;
			}
			}
		}
	};

	/**
	 * 进入应用
	 * 
	 * @param delay
	 */
	private void startMainActivity(long delay) {
		
		final Class<? extends Activity> classToStart = MeLifeMainActivity.class;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent().setClass(
						MeLifeLauncherActivity.this, classToStart).setData(
						getIntent().getData()));
				finish();
			}
		}, delay);
	}

	private void initAccount() {
		// 先把所有的账号设置为未登录
		// new Update(Account.class).set("isUsrLogin=? and mevalue=null",
		// Constant.DB_BOOLEAN.BOOLEAN_FALSE).execute();
		List<Account> listAccount = new Select().from(Account.class)
				.orderBy("lastAccTime desc").execute();
		for (Account acc : listAccount) {
			acc.setIsUsrLogin(false);
			acc.setMevalue(null);
			acc.save();
		}
		if (listAccount != null && listAccount.size() > 0) {// 说明已经有了账号,取第一个账号，如果是注册用户，则进行登录操作
			final Account acc = listAccount.get(0);
			if (!acc.getIsTemp()) {// 不是临时账号，进行登录操作
				MeUtil.login(acc.getAccount(), acc.getAccPwd(),
						new OnLoginListener() {
							@Override
							public void onLogin(boolean isSuccess,
									String message) {
								if (isSuccess) {// 登录成功

								} else {// 登录失败

								}
								initCityCategory();// 初始化城市分类
								initCityForum();// 初始化城市栏目
								mHandler.sendEmptyMessage(LOAD_DONE);
							}
						});
			} else {
				mHandler.sendEmptyMessage(LOAD_DONE);
				initCityCategory();// 初始化城市分类
				initCityForum();// 初始化城市栏目
			}
		} else {
			mHandler.sendEmptyMessage(LOAD_DONE);
			initCityCategory();// 初始化城市分类
			initCityForum();// 初始化城市栏目
		}
	}

	// 加载城市分类
	private void initCityCategory() {
		AreaInfo ai = AppContext.currentAreaInfo();
		if (ai != null) {
			MeUtil.loadCityCategory(ai.getAlongCitySid(),
					new OnLoadCityCategoryListener() {
						@Override
						public void onLoadCityCategoryDone() {
							mHandler.sendEmptyMessage(LOAD_DONE);
						}
					});
		} else {
			mHandler.sendEmptyMessage(LOAD_DONE);
		}
	}

	// 加载城市栏目
	private void initCityForum() {
		AreaInfo ai = AppContext.currentAreaInfo();
		if (ai != null) {
			MeUtil.loadCityForums(ai.getAlongCitySid(),
					new OnCityForumLoadListener() {
						@Override
						public void cityForumLoadDone() {
							mHandler.sendEmptyMessage(LOAD_DONE);
						}
					});
		} else {
			mHandler.sendEmptyMessage(LOAD_DONE);
		}
	}

	/**
	 * 处理屏幕宽度和高度的问题
	 */
	private void dealScreen() {
		DisplayMetrics dm = new DisplayMetrics();
		// 取得窗口属性
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 窗口高度
		AppContext.setScreenWidth(this, dm.widthPixels);
		AppContext.setScreenHeight(this, dm.heightPixels);
	}

	private List<View> listLauncherPages = new ArrayList<View>();
	private List<ImageView> listDotPages = new ArrayList<ImageView>();

	/**
	 * 初始化启动页面
	 */
	private void initLauncherPage() {

		String isFirst = SharedPreferencesUtil.getString(mContext, "isFirst",
				"yes");
		if ("yes".equals(isFirst)) {// 是第一次启动
			maxLoadNumber = 3;
			first_fl.setVisibility(View.VISIBLE);
			not_first_fl.setVisibility(View.GONE);
			first_fl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SharedPreferencesUtil.save(mContext, "isFirst", "no");
					mHandler.sendEmptyMessage(LOAD_DONE);
					progressBar.setVisibility(View.VISIBLE);
				}
			});
		} else {
			maxLoadNumber = 2;
			first_fl.setVisibility(View.GONE);
			not_first_fl.setVisibility(View.VISIBLE);
		}
	}

}
