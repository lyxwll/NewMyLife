package com.yulinoo.plat.life.views.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class MyAreaActivity extends BaseActivity{

	private WebView webView;
	private AreaInfo currentAreaInfo;
	private Long areaId;
	private String areaName;
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.my_area_layout);
		webView=(WebView) findViewById(R.id.my_area_web_view);

		currentAreaInfo=AppContext.currentAreaInfo();
		areaId=currentAreaInfo.getSid();
		areaName=currentAreaInfo.getAreaName();
		String reqUrl=Constant.Requrl.getAreaDetailById()+"?sid="+areaId;
		System.out.println("请求网址是: "+ reqUrl);
		webView.loadUrl(reqUrl);
	}

	@Override
	protected void initComponent() {

	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,
			View RightImaView, TextView rightText, TextView title,
			View titleLayout) {
		rightImg.setVisibility(View.INVISIBLE);
		rightText.setVisibility(View.INVISIBLE);
		title.setText(areaName);
	}

}
