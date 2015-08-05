package com.yulinoo.plat.life.views.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.SurgestionReq;
import com.yulinoo.plat.life.net.resbean.NormalResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class SuggestionActivity extends BaseActivity {

	private EditText linkType;
	private EditText des;
	private Long goodsSid;
	private Integer suggestionType;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.surgestion);
		goodsSid = (Long) this.getIntent().getSerializableExtra(
				Constant.ActivityExtra.GOODSSID);
		suggestionType = (Integer) this.getIntent().getSerializableExtra(
				Constant.ActivityExtra.SUGGESTION_TYPE);
	}

	@Override
	protected void initComponent() {
		des = (EditText) findViewById(R.id.des);
		linkType = (EditText) findViewById(R.id.linkType);
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,
			View RightImaView, TextView rightText, TextView title,
			View titleLayout) {
		rightText.setVisibility(View.VISIBLE);
		rightText.setText("提交");
		// rightImg.setBackgroundResource(R.drawable.submit_btn);
		rightImg.setVisibility(View.GONE);
		title.setText(getString(R.string.suggestion_reback));

		rightImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				surgestion();
			}
		});

		rightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				surgestion();
			}
		});
		if (suggestionType != null) {
			if (Constant.SuggestionType.ERROR == suggestionType) {// 投诉纠错
				title.setText(R.string.suggestion_error);
				des.setHint(R.string.suggestion_error_desc);
			} else if (Constant.SuggestionType.SUGGESTION == suggestionType) {// 意见建议
				title.setText(R.string.suggestion_reback);
				des.setHint(R.string.suggestion_reback_desc);
			} else if (Constant.SuggestionType.RECOMMEND_MERCHANT == suggestionType) {// 推荐商家
				title.setText(R.string.suggestion_recommend_merchant);
				des.setHint(R.string.suggestion_recommend_merchant_desc);
			}
		}
	}

	public void surgestion() {
		SurgestionReq surgestionReq = new SurgestionReq();
		if (AppContext.currentAreaInfo() == null) {
			showToast("请先关注小区");
			return;
		}
		if (NullUtil.isStrNull(getViewString(des))) {
			showToast("提交的内容不能为空");
			return;
		}
		if (getViewString(des).length() > 200) {
			showToast("提交的内容不能超过200个字");
			return;
		}

		surgestionReq
				.setCitySid(AppContext.currentAreaInfo().getAlongCitySid());
		surgestionReq.setAreaSid(AppContext.currentAreaInfo().getSid());
		surgestionReq.setSuggestions(des.getText().toString());
		surgestionReq.setMevalue(AppContext.currentAccount().getMevalue());
		surgestionReq.setGoodsSid(goodsSid);
		surgestionReq.setSuggestionType(suggestionType);
		surgestionReq.setLinkType(linkType.getText().toString());
		RequestBean<NormalResponse> requestBean = new RequestBean<NormalResponse>();
		requestBean.setRequestBody(surgestionReq);
		requestBean.setResponseBody(NormalResponse.class);
		requestBean.setURL(Constant.Requrl.getUsrSuggestion());
		ProgressUtil.showProgress(mContext, "正在提交...");
		requestServer(requestBean, new UICallback<NormalResponse>() {

			@Override
			public void onSuccess(NormalResponse respose) {
				ProgressUtil.dissmissProgress();
				if (respose.isSuccess()) {
					showToast("提交成功");
					finish();
				} else {
					showToast("提交失败:" + respose.getMsg());
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

	}

}
