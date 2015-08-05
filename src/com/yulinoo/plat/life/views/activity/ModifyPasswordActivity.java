package com.yulinoo.plat.life.views.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.ModifyPasswordReq;
import com.yulinoo.plat.life.net.resbean.NormalResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class ModifyPasswordActivity extends BaseActivity {

	private EditText password;
	private EditText re_password;
	private TextView modify_btn;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.modify_pwd);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	protected void initComponent() {
		password=(EditText)findViewById(R.id.password);
		re_password=(EditText)findViewById(R.id.re_password);
		modify_btn=(TextView)findViewById(R.id.modify_btn);
		modify_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String strPassword=password.getText().toString();
				String strRePassword=re_password.getText().toString();
				if(NullUtil.isStrNotNull(strPassword))
				{
					if(strPassword.length()>20)
					{
						showToast("新密码过长,请确保长度少于20!");
						return;
					}
					if(NullUtil.isStrNotNull(strRePassword))
					{
						if(strPassword.equals(strRePassword))
						{
							final Account account=AppContext.currentAccount();
							ModifyPasswordReq loginReq = new ModifyPasswordReq();
							loginReq.setMevalue(account.getMevalue());
							loginReq.setNewPassword(strPassword);
							RequestBean<NormalResponse> requestBean = new RequestBean<NormalResponse>();
							requestBean.setHttpMethod(HTTP_METHOD.POST);
							requestBean.setRequestBody(loginReq);
							requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
							requestBean.setResponseBody(NormalResponse.class);
							requestBean.setURL(Constant.Requrl.getModifyPassword());
							ProgressUtil.showProgress(ModifyPasswordActivity.this, "正在处理...");
							requestServer(requestBean, new UICallback<NormalResponse>() {
								@Override
								public void onSuccess(NormalResponse respose) {
									try {
										ProgressUtil.dissmissProgress();
										if(respose.isSuccess())
										{
											account.setAccPwd(strPassword);
											account.save();
											showToast("密码修改成功");
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
							showToast("请确保两次输入的密码一致");
						}
					}else
					{
						showToast("请输入确认密码");
					}
				}else
				{
					showToast("请输入新密码");
				}
			}
		});
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {
		rightImg.setVisibility(View.INVISIBLE);
		rightText.setVisibility(View.INVISIBLE);
		title.setText("修改密码");
	}
}
