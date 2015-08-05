package com.yulinoo.plat.life.views.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.MeUtil.OnLoginListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.melife.R;

import config.AppContext;

//用户个人空间(即用户的首页页面，从其他用户侧所看到的)
public class LoginActivity extends BaseActivity implements OnClickListener {
	private TextView register;// 我要注册
	private TextView login;// 登录

	private EditText userName;
	private EditText password;
	private ImageView login_bg;

	@Override
	protected void initView(Bundle savedInstanceState) {

		setContentView(R.layout.login);

		register = (TextView) findViewById(R.id.register);
		register.setOnClickListener(this);
		findViewById(R.id.forgot_pwd).setOnClickListener(this);
		login = (TextView) findViewById(R.id.login);
		login.setOnClickListener(this);
		userName = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login_bg = (ImageView) findViewById(R.id.login_bg);
		int size[] = SizeUtil.weibo_picture_size(mContext);
		login_bg.getLayoutParams().width = size[0];
		login_bg.getLayoutParams().height = size[1];

		List<Account> listAccount = new Select().from(Account.class)
				.orderBy("lastAccTime desc").execute();
		if (listAccount != null && listAccount.size() > 0) {// 说明已经有了账号,取第一个账号，如果是注册用户，则进行登录操作
			final Account acc = listAccount.get(0);
			if (!acc.getIsTemp()) {// 不是临时账号，进行登录操作
				userName.setText(acc.getAccount());
				password.setText(acc.getAccPwd());
			}
		}
	}

	@SuppressLint({ "InlinedApi", "ResourceAsColor" })
	@Override
	protected void initComponent() {

	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,
			View RightImaView, TextView rightText, TextView title,
			View titleLayout) {
		title.setText("用户登录");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login: {
			login();
			break;
		}
		case R.id.register: {
			if (AppContext.currentFocusArea() != null
					&& AppContext.currentFocusArea().size() > 0) {
				mContext.startActivity(new Intent(mContext,
						RegisterActivity.class));
			} else {
				MeUtil.showToast(mContext,
						mContext.getString(R.string.needconcerarea));
			}
			finish();
			break;
		}
		case R.id.forgot_pwd: {
			mContext.startActivity(new Intent(mContext,
					ForgotPasswordActivity.class));
			finish();
			break;
		}
		}
	}

	private void login() {
		final String userName_str = userName.getText().toString();
		final String password_str = password.getText().toString();
		ProgressUtil.showProgress(mContext, "正在登录...");
		MeUtil.login(userName_str, password_str, new OnLoginListener() {
			@Override
			public void onLogin(boolean isSuccess, String message) {
				ProgressUtil.dissmissProgress();
				if (isSuccess) {
					View view = MeLifeMainActivity.instance().getWindow()
							.peekDecorView();
					if (view != null) {
						InputMethodManager inputmanger = (InputMethodManager) MeLifeMainActivity
								.instance().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						inputmanger.hideSoftInputFromWindow(
								view.getWindowToken(), 0);
					}
					Account account = AppContext.currentAccount();
					if (account != null && account.getIsUsrLogin()) {// 用户已登录
						if (MeLifeMainActivity.isInstanciated()) {
							// MeLifeMainActivity.instance().updateUsrHeader();
							// 小区改变了
							// 发送广播的方法
							// Intent intent = new Intent();
							// intent.setAction(Constant.BroadType.AREA_CHANGED);
							// mContext.sendBroadcast(intent);
							// 使用方法来让mainActivity刷新,设置需要刷新index为true
							MeLifeMainActivity.instance().setNeedRefreshIndex(
									true);

							// 用户关注的内容改变了
							Intent intentSubscribe = new Intent();
							intentSubscribe
									.setAction(Constant.BroadType.SUBSCRIBE_READED);
							mContext.sendBroadcast(intentSubscribe);
						}
						if (AppContext.currentFocusArea() != null
								&& AppContext.currentFocusArea().size() > 0) {
							AreaInfo ai = AppContext.currentFocusArea().get(0);
							account.setAreaInfo(ai);
							startActivity(new Intent(LoginActivity.this,
									UsrZoneActivity.class).putExtra(
									Constant.ActivityExtra.ACCOUNT, account));
							finish();
						}
					}
					// LoginWidget.this.setVisibility(View.GONE);
					// myZoneWidget.show();
				} else {
					MeMessageService.instance().showToast(message);
				}
			}
		});
	}

}
