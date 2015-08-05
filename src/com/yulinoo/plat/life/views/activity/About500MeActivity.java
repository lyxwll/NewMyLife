package com.yulinoo.plat.life.views.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.melife.R;

public class About500MeActivity extends BaseActivity {

	private WebView webView;


	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.single_web_view);
		webView = (WebView)findViewById(R.id.web_view);
		webView.loadUrl(Constant.Requrl.getAboutME()); 
	}

	@Override
	protected void initComponent() {
		
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {
		rightImg.setVisibility(View.INVISIBLE);
		rightText.setVisibility(View.INVISIBLE);
		title.setText(getString(R.string.about_500me_life));
	}
}
