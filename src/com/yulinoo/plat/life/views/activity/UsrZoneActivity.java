package com.yulinoo.plat.life.views.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.bean.MessageBox;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.UsrDetailReq;
import com.yulinoo.plat.life.net.resbean.UsrDetailResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.service.MeMessageService.OnMessageBoxArrivedListener;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.views.adapter.UsrZoneAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;

//用户个人空间(即用户的首页页面，从其他用户侧所看到的)
public class UsrZoneActivity extends EditPhotoActivity implements
		OnMessageBoxArrivedListener {
	private Account account;// 用户信息
	// private MeNormalListView mListView;
	// private SwipeRefreshLayout mSwipeLayout;
	private XListView mListView;
	private UsrZoneAdapter myZoneAdapter;
	private boolean isMe = false;
	private boolean rh_isShowing = false;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.usr_zone);
		account = (Account) this.getIntent().getSerializableExtra(
				Constant.ActivityExtra.ACCOUNT);
		rh_isShowing = (Boolean) this.getIntent().getBooleanExtra(
				"rh_isShowing", false);

		account.setSex(Constant.SEX.SEX_SECRECT);// 先设置一个默认的
		Account me = AppContext.currentAccount();
		if (me.getSid().longValue() == account.getSid().longValue()) {
			isMe = true;
			initBroadcastReceiver();
		}
		if (MeMessageService.isReady()) {
			MeMessageService.instance().addOnMessageBoxArrivedListener(this);
			MeMessageService.instance().loadMessageBox();
		}
	}

	private MyBroadcastReceiver areaOpenShopBroadcastReceiver;
	private MyBroadcastReceiver goodDeletedBroadcastReceiver;

	private void initBroadcastReceiver() {
		IntentFilter intentOpenShopFilter = new IntentFilter();
		IntentFilter intentGoodDeleteFilter = new IntentFilter();
		intentOpenShopFilter.addAction(Constant.BroadType.AREA_OPEN_SHOP);
		intentGoodDeleteFilter.addAction(Constant.BroadType.GOODS_DELETED);
		areaOpenShopBroadcastReceiver = new MyBroadcastReceiver();
		goodDeletedBroadcastReceiver = new MyBroadcastReceiver();
		this.registerReceiver(areaOpenShopBroadcastReceiver,
				intentOpenShopFilter);
		this.registerReceiver(goodDeletedBroadcastReceiver,
				intentGoodDeleteFilter);
	}

	@SuppressLint({ "InlinedApi", "ResourceAsColor" })
	@Override
	protected void initComponent() {
		// mListView = (MeNormalListView) findViewById(R.id.list);
		// mSwipeLayout = (SwipeRefreshLayout)
		// findViewById(R.id.swipe_container);
		// MeUtil.setSwipeLayoutColor(mSwipeLayout);
		// mSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
		// mSwipeLayout.setLoadNoFull(false);
		mListView = (XListView) findViewById(R.id.list_view);
		// 加载用户详情
		loadUsrDetail();
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,
			View RightImaView, TextView rightText, TextView title,
			View titleLayout) {
		// title.setText(account.getAccName());
		title.setText("个人中心");
		// rightText.setVisibility(View.VISIBLE);
		// rightText.setText("关于");
		// rightText.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// startActivity(new Intent(UsrZoneActivity.this,
		// About500MeActivity.class));
		// }
		// });
	}

	// @Override
	// public void finish() {
	// super.finish();
	// Account me=AppContext.currentAccount();
	// if(me!=null&&me.getSid().longValue()==account.getSid().longValue())
	// {
	// overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	// }
	// }
	@Override
	protected void onDestroy() {
		MeMessageService.instance().removeOnMessageBoxArrivedListener(this);
		if (isMe) {
			try {
				this.unregisterReceiver(goodDeletedBroadcastReceiver);
				this.unregisterReceiver(areaOpenShopBroadcastReceiver);
			} catch (Exception e) {
			}
		}

		super.onDestroy();
	}

	// 加载用户详情
	private void loadUsrDetail() {
		UsrDetailReq merchantDetailReq = new UsrDetailReq();
		merchantDetailReq.setSid(account.getSid());
		RequestBean<UsrDetailResponse> requestBean = new RequestBean<UsrDetailResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(merchantDetailReq);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(UsrDetailResponse.class);
		requestBean.setURL(Constant.Requrl.getUsrDetail());
		requestServer(requestBean, new UICallback<UsrDetailResponse>() {
			@Override
			public void onSuccess(UsrDetailResponse respose) {
				if (respose.isSuccess()) {
					try {
						Account tmp = account;
						account = respose.getAccout();
						account.setAreaInfo(tmp.getAreaInfo());
						// account.setAreaName(tmp.getAreaName());
						// account.setAreaSid(tmp.getAreaSid());
						myZoneAdapter = new UsrZoneAdapter(
								UsrZoneActivity.this, account, mListView,
								rh_isShowing);
						List<ForumNote> indexList = new ArrayList<ForumNote>();
						indexList.add(null);// 只是为了告诉listview有两条数据，并不会实际使用
						myZoneAdapter.load(indexList);
					} catch (Exception e) {
					}
				} else {
					showToast(respose.getMsg());
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
	}

	@Override
	public void photoUploadDone(Bitmap newPhoto, String afterCompressUrl,
			String netUrlPath) {
		Account account = AppContext.currentAccount();
		if (account != null) {
			account.setHeadPicture(netUrlPath);
			account.save();
			if (MeLifeMainActivity.isInstanciated()) {
				// MeLifeMainActivity.instance().updateUsrHeader();
			}
		}
		if (myZoneAdapter != null) {
			myZoneAdapter.changeHeaderPicture(newPhoto);
		}
	}

	@Override
	public void onMessageBoxArrived(MessageBox box) {
		if (myZoneAdapter != null) {
			int commentNumber = box.getCommentNumber();
			int praiseNumber = box.getPraiseNumber();
			if (commentNumber > 0 || praiseNumber > 0) {// 显示红点
				myZoneAdapter.showRedHot(true);
			} else {
				myZoneAdapter.showRedHot(false);
			}
		}
	}

	private class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constant.BroadType.AREA_OPEN_SHOP)) {// 小区受到改变
				if (myZoneAdapter != null) {
					myZoneAdapter.setOpenShop();
				}
			} else if (action.equals(Constant.BroadType.GOODS_DELETED)) {
				if (mListView != null) {
					mListView.autoRefresh();
				}
			}
		}
	}

}
