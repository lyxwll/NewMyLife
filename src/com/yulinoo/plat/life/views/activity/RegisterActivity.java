package com.yulinoo.plat.life.views.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.RegisterReq;
import com.yulinoo.plat.life.net.reqbean.ValCodeReq;
import com.yulinoo.plat.life.net.resbean.NormalResponse;
import com.yulinoo.plat.life.net.resbean.RegisterResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.MeRadioWidget;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.MeUtil.OnLoginListener;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class RegisterActivity extends BaseActivity {

	private EditText username_zh;
	private TextView usr_protocal;
	private EditText valCode;
	private EditText inviteCode;
	private TextView getValCode;
	private MeRadioWidget sex_group;
	private EditText password;
	//private EditText confirmPassword;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.register);	
		//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
		//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	protected void initComponent() {
		username_zh = (EditText) findViewById(R.id.username_zh);
		//		userName_nc = (EditText) findViewById(R.id.username_nc);
		password = (EditText) findViewById(R.id.password);
		//		confirmPassword = (EditText) findViewById(R.id.repear_password);
		sex_group = (MeRadioWidget) findViewById(R.id.sex_group);
		sex_group.setEnabled(true);
		MeUtil.setSexGroup(this, sex_group, Constant.SEX.SEX_MAN);

		findViewById(R.id.register_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				register();
			}
		});
		valCode=(EditText)findViewById(R.id.valCode);//验证码
		inviteCode=(EditText)findViewById(R.id.inviteCode);//邀请码
		usr_protocal=(TextView)findViewById(R.id.usr_protocal);
		usr_protocal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mContext.startActivity(new Intent(mContext,	UserProtocalActivity.class));
			}
		});

		getValCode=(TextView)findViewById(R.id.getValCode);
		getValCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone=username_zh.getText().toString();
				if(NullUtil.isStrNotNull(phone))
				{
					if(MeUtil.isMobilePhone(phone))
					{
						ValCodeReq loginReq = new ValCodeReq();
						loginReq.setPhone(phone);
						RequestBean<NormalResponse> requestBean = new RequestBean<NormalResponse>();
						requestBean.setHttpMethod(HTTP_METHOD.POST);
						requestBean.setRequestBody(loginReq);
						requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
						requestBean.setResponseBody(NormalResponse.class);
						requestBean.setURL(Constant.Requrl.getSendValShortMessage());
						ProgressUtil.showProgress(RegisterActivity.this, "正在发送...");
						requestServer(requestBean, new UICallback<NormalResponse>() {
							@Override
							public void onSuccess(NormalResponse respose) {
								ProgressUtil.dissmissProgress();
								try {
									if(respose.isSuccess())
									{
										showToast("短信发送成功,请注意查收");
									}else
									{
										showToast(respose.getMsg());
									}
								} catch (Exception e) {
								}
								
							}

							@Override
							public void onError(String message) {
								showToast(message);
								ProgressUtil.dissmissProgress();
							}

							@Override
							public void onOffline(String message) {
								ProgressUtil.dissmissProgress();
								showToast(message);
							}
						});
					}else
					{
						showToast("请输入正确的手机号");
					}
				}else
				{
					showToast("请输入手机号");
				}
			}

		});

	}
	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {
		rightImg.setVisibility(View.INVISIBLE);
		title.setText("用户注册");
		//rightImg.setBackgroundResource(R.drawable.submit_btn);
		//		rightText.setText("注册");
		//		rightText.setVisibility(View.INVISIBLE);
		//		rightText.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//				register();
		//			}
		//		});

		//		rightImg.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//				register();
		//
		//			}
		//		});


	}

	private void register() {
		if(AppContext.currentAreaInfo()==null)
		{
			showToast("请先关注你的小区");
			return;
		}
		final String userName_zh_str = username_zh.getText().toString();
		if(!MeUtil.isMobilePhone(userName_zh_str))
		{
			showToast("请输入正确的手机号格式");
			return;
		}

		final String password_str=password.getText().toString();
		//final String confirm_pwd=confirmPassword.getText().toString();

		if(!NullUtil.isStrNotNull(password_str))
		{
			showToast("请输入密码");
			return;
		}
		if(password_str.length()>20)
		{
			showToast("输入的密码过长");
			return;
		}
		//		if(!password_str.equals(confirm_pwd))
		//		{
		//			showToast("请确保密码与重复密码一致");
		//			return;
		//		}

		RegisterReq registerReq = new RegisterReq();
		registerReq.setAccount(userName_zh_str);
		registerReq.setPassword(password_str);
		registerReq.setCitySid(AppContext.currentAreaInfo().getAlongCitySid());
		registerReq.setDistrictSid(AppContext.currentAreaInfo().getAlongDistrictSid());
		registerReq.setPhoneType(AppContext.getPhoneType());
		registerReq.setSex(sex_group.getNowRadio().getIndex());
		registerReq.setValCode(valCode.getText().toString());
		registerReq.setInviteCode(inviteCode.getText().toString());
		List<Account> accountTBs = new Select().from(Account.class).where("isTemp=?",Constant.DB_BOOLEAN.BOOLEAN_TRUE).execute();
		registerReq.setTempSid(accountTBs.get(0).getSid());

		RequestBean<RegisterResponse> requestBean = new RequestBean<RegisterResponse>();
		requestBean.setRequestBody(registerReq);
		requestBean.setResponseBody(RegisterResponse.class);
		requestBean.setURL(Constant.Requrl.getUsrRegister());
		ProgressUtil.showProgress(mContext, "正在注册...");
		requestServer(requestBean, new UICallback<RegisterResponse>() {

			@Override
			public void onSuccess(RegisterResponse respose) {
				ProgressUtil.dissmissProgress();
				try {
					if (respose.isSuccess()) {
						ProgressUtil.showProgress(mContext, "注册成功,正在登录...");
						MeUtil.login(userName_zh_str,password_str, new OnLoginListener() {
							@Override
							public void onLogin(boolean isSuccess, String message) {
								ProgressUtil.dissmissProgress();
								if(isSuccess)
								{
									if(MeLifeMainActivity.isInstanciated())
									{
										//MeLifeMainActivity.instance().updateUsrHeader();
									}
									Account account=AppContext.currentAccount();
									if(AppContext.currentFocusArea()!=null&&AppContext.currentFocusArea().size()>0)
									{
										AreaInfo ai=AppContext.currentFocusArea().get(0);
										account.setAreaInfo(ai);
										startActivity(new Intent(RegisterActivity.this, UsrZoneActivity.class).putExtra(Constant.ActivityExtra.ACCOUNT, account));
									}
									finish();
								}else
								{
									showToast(message);
								}
							}
						});
					} else {
						showToast(respose.getMsg());
					}
				} catch (Exception e) {
					showToast(e.getMessage());
				}
				
			}

			@Override
			public void onError(String message) {
				ProgressUtil.dissmissProgress();
			}

			@Override
			public void onOffline(String message) {
				ProgressUtil.dissmissProgress();
				showToast(message);
			}
		});

	}

	//	private int randPwd()
	//	{
	//		Random rand;
	//		rand= new Random();
	//		int min=100000;
	//		int max=999999;
	//		int tmp = Math.abs(rand.nextInt());
	//		return tmp % (max - min + 1) + min;
	//	}



}
