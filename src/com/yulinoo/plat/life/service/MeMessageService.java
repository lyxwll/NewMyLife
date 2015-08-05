package com.yulinoo.plat.life.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.util.LongSparseArray;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.ChatMessage;
import com.yulinoo.plat.life.bean.MessageBox;
import com.yulinoo.plat.life.net.callback.ICallBack;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.AdvertiseReq;
import com.yulinoo.plat.life.net.reqbean.GetMessageBoxReq;
import com.yulinoo.plat.life.net.resbean.AdvertiseResponse;
import com.yulinoo.plat.life.net.resbean.MessageBoxResponse;
import com.yulinoo.plat.life.net.resbean.VersionResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.net.service.RequestService;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.SharedPreferencesUtil;
import com.yulinoo.plat.life.utils.UpdateDtUtil;
import com.yulinoo.plat.melife.R;

import config.AppContext;


public final class MeMessageService extends Service  {

	private static MeMessageService instance;
	private Timer timer = new Timer();
	private TimerTask messageBoxTimerTask;
	//private TimerTask chatRoomTimerTask;
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this; 
	}

	public static boolean isReady() {
		return instance!=null;
	}

	public static MeMessageService instance()  {
		if (isReady()) return instance;
		throw new RuntimeException("MeMessageService not instantiated yet");
	}

	@Override
	public synchronized void onDestroy() {
		instance = null;
		timer.cancel();
		timer=null;
		super.onDestroy();
	}
	//开始获取消息
	public void startGetMessage()
	{
		if(timer!=null)
		{
			try {
				if(messageBoxTimerTask==null)
				{
					messageBoxTimerTask=new TimerTask() {
						@Override
						public void run() {
							if(instance!=null)
							{
								handler.post(new Runnable() {
									@Override
									public void run() {
										getMessageBox();
									}
								});
							}
						}
					};
				}
				timer.schedule(messageBoxTimerTask,5000,10000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {//取新版本
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								String imageurl="http://www.yulinoo.com/app/android/ver.json";
								// 从网络上获取图片
								URL url = new URL(imageurl);
								HttpURLConnection conn = (HttpURLConnection) url.openConnection();
								conn.setConnectTimeout(5000);
								conn.setRequestMethod("GET");
								conn.setDoInput(true);
								if (conn.getResponseCode() == 200) {
									InputStream is = conn.getInputStream();//得到网络返回的输入流
									String result = RequestService.readData(is, "utf-8");
									Gson gson = new Gson();
									try {
										VersionResponse respose = gson.fromJson(result, VersionResponse.class);
										String pkName = MeMessageService.this.getPackageName();
										int versionCode = MeMessageService.this.getPackageManager().getPackageInfo(pkName, 0).versionCode;
										int newVersion=respose.getVerCode();
										if (newVersion > versionCode) {//需要进行更新提示
											Intent intent = new Intent();  
											intent.setAction(Constant.BroadType.NEW_VERSION);
											intent.putExtra(Constant.ActivityExtra.NEW_VERSION, respose);
											MeMessageService.this.sendBroadcast(intent);
										}
									} catch (Exception e) {
										//e.printStackTrace();
									}
									conn.disconnect();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();

				} catch (Exception e) {
				}
				if(AppContext.currentAreaInfo()!=null)
				{
					//取关注的小区列表信息
					MeUtil.loadFocusArea(null, null);
					
					try {//5秒后取广告
						Thread.sleep(5000);
						AdvertiseReq req=new AdvertiseReq();
						req.setAreaSid(AppContext.currentAreaInfo().getSid());
						req.setPosition(3);
						RequestBean<AdvertiseResponse> requestBean = new RequestBean<AdvertiseResponse>();
						requestBean.setRequestBody(req);
						requestBean.setResponseBody(AdvertiseResponse.class);
						requestBean.setURL(Constant.Requrl.getAdv());
						requestServer(requestBean, new UICallback<AdvertiseResponse>() {
							@Override
							public void onSuccess(AdvertiseResponse respose) {
								try {
									if(respose.isSuccess())
									{
										String filePath=StorageUtils.getCacheDirectory(getApplicationContext()).getAbsolutePath();//
										if(!filePath.endsWith(File.separator))
										{
											filePath=filePath+File.separator+"thumb"+File.separator;
										}
										File folders=new File(filePath);
										folders.mkdirs();
										filePath+="load_adv_url.png";

										if(respose.getMerchantAdvert()!=null)
										{
											String adv_path=respose.getMerchantAdvert().getUrl();
											String load_adv=SharedPreferencesUtil.getString(getApplicationContext(), "load_adv_url", "");
											if(!load_adv.equals(adv_path))
											{//说明需要下载广告图片
												downLoadAdvertisePicture(adv_path,filePath);
											}else
											{//判断图片是否已经下载了
												File file=new File(filePath);
												if(!file.exists())
												{//下载广告图片
													downLoadAdvertisePicture(adv_path,filePath);
												}
											}
										}
									}
								} catch (Exception e) {
								}
							}

							@Override
							public void onError(String message) {
								showToast(message);
							}

							@Override
							public void onOffline(String message) {
								showToast(message);
							}
						});

					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}


			}
		}).start();
	}
	//下载广告图片
	private void downLoadAdvertisePicture(final String urlPath,final String file)
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String imageurl=Constant.URL.HTTP+AppContext.currentAreaInfo().getCityDomain()+Constant.DOT+Constant.URL.IMG+Constant.URL.BASE_DOMAIN+urlPath;
					// 从网络上获取图片
					URL url = new URL(imageurl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					if (conn.getResponseCode() == 200) {
						InputStream is = conn.getInputStream();
						FileOutputStream fos = new FileOutputStream(file);
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = is.read(buffer)) != -1) {
							fos.write(buffer, 0, len);
						}
						is.close();
						fos.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
	//由外部加载一下消息盒子
	public void loadMessageBox()
	{
		getMessageBox();
	}
	private boolean isLoading=false;
	private synchronized void getMessageBox()
	{
		AreaInfo areaInfo=AppContext.currentAreaInfo();
		if(areaInfo==null)
		{
			return;
		}
		if(isLoading)
		{
			return;
		}
		GetMessageBoxReq getMessageBoxReq=new GetMessageBoxReq();
		getMessageBoxReq.setAreaSid(areaInfo.getSid());
		getMessageBoxReq.setBeginTime(UpdateDtUtil.getAreaChatRoomDt(getApplicationContext(), areaInfo.getSid()));
		getMessageBoxReq.setMevalue(AppContext.currentAccount().getMevalue());
		RequestBean<MessageBoxResponse> requestBean = new RequestBean<MessageBoxResponse>();
		requestBean.setRequestBody(getMessageBoxReq);
		requestBean.setResponseBody(MessageBoxResponse.class);
		requestBean.setURL(Constant.Requrl.getMessageBox());
		isLoading=true;
		requestServer(requestBean, new UICallback<MessageBoxResponse>() {
			@Override
			public void onSuccess(final MessageBoxResponse respose) {
				try {
					if(respose.isSuccess())
					{
						for(OnMessageBoxArrivedListener listener:listMessageBoxArrivedListener)
						{
							listener.onMessageBoxArrived(respose.getMessageBox());
						}
					}
				} catch (Exception e) {
				}
				isLoading=false;
			}

			@Override
			public void onError(String message) {
				//showToast(message);
				isLoading=false;
			}

			@Override
			public void onOffline(String message) {
				//showToast(message);
				isLoading=false;
			}
		});
	}

	public interface OnMessageBoxArrivedListener
	{
		public void onMessageBoxArrived(MessageBox box);
	}
	private List<OnMessageBoxArrivedListener> listMessageBoxArrivedListener=new ArrayList<OnMessageBoxArrivedListener>();
	public synchronized void addOnMessageBoxArrivedListener(OnMessageBoxArrivedListener listener)
	{
		if(!listMessageBoxArrivedListener.contains(listener))
		{
			listMessageBoxArrivedListener.add(listener);
		}
	}
	public synchronized void removeOnMessageBoxArrivedListener(OnMessageBoxArrivedListener listener)
	{
		if(listMessageBoxArrivedListener.contains(listener))
		{
			listMessageBoxArrivedListener.remove(listener);
		}
	}



	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private LongSparseArray<List<ChatMessage>> areaMessage=new LongSparseArray<List<ChatMessage>>();
	public List<ChatMessage> getAreaMessage(Long areaSid)
	{
		return areaMessage.get(areaSid);
	}


	protected Handler handler = new Handler();
	public <T> void requestServer(RequestBean<T> requestBean, final UICallback<T> callback) {

		if (!hasInternet(this)) {
			try {
				callback.onOffline(getString(R.string.net_off_line));
			} catch (Exception e) {
			}
			return;
		}

		RequestService requestService = new RequestService();
		requestService.request(requestBean, new ICallBack() {
			@Override
			public void onSuccess(final Object respose) {
				handler.post(new Runnable() {
					@SuppressWarnings("unchecked")
					@Override
					public void run() {
						callback.onSuccess((T) respose);
					}
				});
			}

			@Override
			public void onError(final String message) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError(message);
					}
				});
			}
		}, this);
	}
	// 是否有网
	public boolean hasInternet(Context context) {
		try {
			ConnectivityManager manger = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manger.getActiveNetworkInfo();
			if (info != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	public void showToast(String message) {
		try {
			Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		} catch (Error e) {
		}
	}
}

