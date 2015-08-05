package com.yulinoo.plat.life.views.activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.net.callback.ICallBack;
import com.yulinoo.plat.life.net.callback.IContentReportCallback;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.callback.UIUploadCallback;
import com.yulinoo.plat.life.net.reqbean.ConcernMerchantReq;
import com.yulinoo.plat.life.net.resbean.ConcernMerchantResponse;
import com.yulinoo.plat.life.net.service.ContentUploadService;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.net.service.RequestService;
import com.yulinoo.plat.life.net.service.UploadBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.BackWidget.OnBackBtnClickListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public abstract class BaseActivity extends FragmentActivity {
	protected Context mContext;
	//protected TextView left_img;
	//protected TextView left_text;
	protected TextView right_img;
	protected TextView right_text;
	protected TextView title;
	protected View right_img_view;
	//protected View left_head;
	protected View titleLayout;
	protected BackWidget back_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initView(savedInstanceState);
		//left_img = (TextView) findViewById(R.id.left_img);
		//left_text = (TextView) findViewById(R.id.left_text);
		right_img_view=findViewById(R.id.good_more_function);
		right_img = (TextView) findViewById(R.id.right_img);
		right_text = (TextView) findViewById(R.id.right_text);
		title = (TextView) findViewById(R.id.title);
		//left_head = findViewById(R.id.left_head);
		titleLayout = findViewById(R.id.titleLayout);
		back_btn=(BackWidget)findViewById(R.id.back_btn);
		if(back_btn!=null)
		{
			back_btn.setBackBtnClickListener(new OnBackBtnClickListener() {
				@Override
				public void onBackBtnClick() {
					finish();
				}
			});
		}
		
		
//		if(left_head!=null)
//		{
//			left_head.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					finish();
//				}
//			});
//		}
		initComponent();
		initHead(back_btn, right_img,right_img_view, right_text, title, titleLayout);
		View main_title_rl=findViewById(R.id.main_title_rl);
		if(main_title_rl!=null)
		{
			//main_title_rl.getLayoutParams().height=SizeUtil.header_height(this);
//			if(title!=null)
//			{
//				//title.setTextSize(SizeUtil.title_menu_text_size(this));
//			}
			
		}
		
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}

	protected abstract void initView(Bundle savedInstanceState);

	protected abstract void initComponent();

	protected abstract void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout);
    
	
	protected Handler handler = new Handler();

	public void showToast(String message) {

		try {
			Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} catch (Error e) {
		}
	}

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
						showToast(message);
						callback.onError(message);
					}
				});
			}
		}, this);
	}

	public void uploadToServer(UploadBean uploadBean, final UIUploadCallback uiUploadCallback) {

		if (uploadBean.getURL() == null) {
			showToast("URL地址不能为空");
			return;
		}

		ContentUploadService contentUploadService = new ContentUploadService(new IContentReportCallback() {
			@Override
			public void onSuccess(final String urlPath) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						uiUploadCallback.onSuccess(urlPath);
					}
				});
			}

			@Override
			public void onProgress(final int progress) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						uiUploadCallback.onProgress(progress + "%");
					}
				});
			}

			@Override
			public void onError(final String msg) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						uiUploadCallback.onError(msg);
					}
				});
			}
		});

		contentUploadService.upload(uploadBean.getFilePath(), uploadBean.getURL());
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
	

	protected String getViewString(View view) {
		if(view instanceof EditText) {
			return ((EditText)view).getText().toString().trim();
		} else if(view instanceof TextView) {
			return ((TextView)view).getText().toString().trim();
		}
		return null;
	}
	
	//显示提示
	public void showTip(final TextView tv)
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					@SuppressLint("NewApi")
					@Override
					public void run() {
						   createHeightAnimator(tv, tv.getHeight(), 0).start();
					}
				});
			}
		}).start();
	}
	@SuppressLint("NewApi")
	private static ValueAnimator createHeightAnimator(final View view, int start, int end) {
		ValueAnimator animator = ValueAnimator.ofInt(start, end);
		animator.setDuration(2000);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				try {
				int value = (Integer) valueAnimator.getAnimatedValue();
				ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
				layoutParams.height = value;
				view.setLayoutParams(layoutParams);
				} catch (Exception e) {
				}
			}

		});
		// animator.setDuration(DURATION);
		return animator;
	}
	
//	//关注/取消关注
//	public void concernMerchant(final Merchant merchant,final ConcernResultListener cl) {
//		Account account=AppContext.currentAccount();
//		if(account.getSid()==merchant.getSid())
//		{//不能关注自己
//			return;
//		}
//		ConcernMerchantReq concernMerchantReq = new ConcernMerchantReq();
//		concernMerchantReq.setAccSid(account.getSid());
//		concernMerchantReq.setMerchantSid(merchant.getSid());
//		concernMerchantReq.setSubType(merchant.getSubType());
//		concernMerchantReq.setAreaSid(merchant.getAlongAreaSid());
//		concernMerchantReq.setAreaName(merchant.getAreaName());
//		RequestBean<ConcernMerchantResponse> requestBean = new RequestBean<ConcernMerchantResponse>();
//		requestBean.setRequestBody(concernMerchantReq);
//		requestBean.setResponseBody(ConcernMerchantResponse.class);
//		if(AppContext.hasFocusMerchant(merchant.getSid()))
//		{//已关注
//			requestBean.setURL(Constant.Requrl.getCancelConcernMerchant());
//		}else
//		{
//			requestBean.setURL(Constant.Requrl.getConcernMerchant());
//		}
//		requestServer(requestBean, new UICallback<ConcernMerchantResponse>() {
//
//			@Override
//			public void onSuccess(ConcernMerchantResponse respose) {
//				try {
//					if (respose.isSuccess()) {
//						if(AppContext.hasFocusMerchant(merchant.getSid()))
//						{
//							AppContext.focusMerchant(merchant, false);
//							//showToast(mContext.getString(R.string.cancel_concern_success));
//							if(cl!=null)
//							{
//								cl.concernResult(false, true);
//							}
//						}else
//						{//关注结果
//							Merchant mer=respose.getMerchant();
//							mer.setAlongAreaSid(merchant.getAlongAreaSid());
//							mer.setAreaName(merchant.getAreaName());
//							mer.setSubType(merchant.getSubType());
//							mer.setSid(merchant.getSid());
//							AppContext.focusMerchant(mer,true);
//							//showToast(mContext.getString(R.string.concern_success));
//							if(cl!=null)
//							{
//								cl.concernResult(true, true);
//							}
//						}
//					} else {
//						if(AppContext.hasFocusMerchant(merchant.getSid()))
//						{
//							showToast(mContext.getString(R.string.cancel_concern_failure)+":"+respose.getMsg());
//							if(cl!=null)
//							{
//								cl.concernResult(false, false);
//							}
//						}else
//						{
//							showToast(mContext.getString(R.string.concern_failure)+":"+respose.getMsg());
//							if(cl!=null)
//							{
//								cl.concernResult(true, false);
//							}
//						}
//					}
//				} catch (Exception e) {
//					showToast(mContext.getString(R.string.concern_failure)+":"+e.getMessage());
//				}
//				//ProgressUtil.dissmissProgress();
//			}
//
//			@Override
//			public void onError(String message) {
//				showToast(message);
//				ProgressUtil.dissmissProgress();
//			}
//
//			@Override
//			public void onOffline(String message) {
//				ProgressUtil.dissmissProgress();
//				showToast(message);
//			}
//		});
//	}
//	
//	public interface ConcernResultListener{
//		public void concernResult(boolean isConcern,boolean result);
//	}

}
