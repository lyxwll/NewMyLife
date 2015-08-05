package com.yulinoo.plat.life.views.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.ResetPasswordReq;
import com.yulinoo.plat.life.net.reqbean.ValCodeReq;
import com.yulinoo.plat.life.net.resbean.NormalResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.melife.R;

public class ForgotPasswordActivity extends BaseActivity {
	private EditText username_zh;
	private EditText valCode;
	private EditText password;
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.forgot_pwd);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	protected void initComponent() {
		username_zh=(EditText)findViewById(R.id.username_zh);
		valCode=(EditText)findViewById(R.id.valCode);
		password=(EditText)findViewById(R.id.password);
		findViewById(R.id.resetPassword_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String userName_zh_str = username_zh.getText().toString();
				if(!MeUtil.isMobilePhone(userName_zh_str))
				{
					showToast("请输入正确的手机号格式");
					return;
				}
				final String new_password=password.getText().toString();
				if(NullUtil.isStrNotNull(new_password))
				{
					if(new_password.length()>20)
					{
						showToast("新密码过长,请确保长度少于20!");
						return;
					}
					ResetPasswordReq loginReq = new ResetPasswordReq();
					loginReq.setAccount(userName_zh_str);
					loginReq.setValCode(valCode.getText().toString());
					loginReq.setPassword(new_password);
					RequestBean<NormalResponse> requestBean = new RequestBean<NormalResponse>();
					requestBean.setHttpMethod(HTTP_METHOD.POST);
					requestBean.setRequestBody(loginReq);
					requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
					requestBean.setResponseBody(NormalResponse.class);
					requestBean.setURL(Constant.Requrl.getResetPassword());
					ProgressUtil.showProgress(ForgotPasswordActivity.this, "正在处理...");
					requestServer(requestBean, new UICallback<NormalResponse>() {
						@Override
						public void onSuccess(NormalResponse respose) {
							try {
								ProgressUtil.dissmissProgress();
								if(respose.isSuccess())
								{
									showToast("重置密码成功");
									finish();
								}else
								{
									showToast(respose.getMsg());
								}
							} catch (Exception e) {
							}
						}

						@Override
						public void onError(String message) {
							ProgressUtil.dissmissProgress();
							showToast(message);
						}

						@Override
						public void onOffline(String message) {
							ProgressUtil.dissmissProgress();
							showToast(message);
						}
					});
				}else
				{
					showToast("请输入新密码");
				}
			}
		});
		findViewById(R.id.getValCode).setOnClickListener(new OnClickListener() {
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
						ProgressUtil.showProgress(ForgotPasswordActivity.this, "正在发送...");
						requestServer(requestBean, new UICallback<NormalResponse>() {
							@Override
							public void onSuccess(NormalResponse respose) {
								try {
									ProgressUtil.dissmissProgress();
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
							public void onOffline(String  message) {
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
		rightText.setVisibility(View.INVISIBLE);
		title.setText("忘记密码");
	}
	
}
