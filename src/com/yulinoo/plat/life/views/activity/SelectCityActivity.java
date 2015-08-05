package com.yulinoo.plat.life.views.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.yulinoo.plat.life.bean.CityInfo;
import com.yulinoo.plat.life.bean.LocationPoint;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.resbean.CityInfoResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.sidelist.ChineseCharToEn;
import com.yulinoo.plat.life.ui.widget.sidelist.Content;
import com.yulinoo.plat.life.ui.widget.sidelist.MyAdapter;
import com.yulinoo.plat.life.ui.widget.sidelist.MyAdapter.OnCitySelectedListener;
import com.yulinoo.plat.life.ui.widget.sidelist.PinyinComparator;
import com.yulinoo.plat.life.ui.widget.sidelist.SideBar;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.melife.R;

public class SelectCityActivity extends BaseActivity implements OnCitySelectedListener {

	private ListView mListView;
	private SideBar sideBar;
	private WindowManager windowManager;
	private TextView mDialogText;
	private View head;
	private MyAdapter myAdapter;

	@Override
	protected void initView(Bundle savedInstanceState) {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		setContentView(R.layout.select_city_layout);
		mListView=(ListView) findViewById(R.id.listview);
		sideBar=(SideBar) findViewById(R.id.side_bar);
		mDialogText=(TextView) LayoutInflater.from(this).inflate(R.layout.list_position, null);
		head=LayoutInflater.from(this).inflate(R.layout.side_list_head, null);
	}

	@Override
	protected void initComponent() {
		mListView.addHeaderView(head);
		mDialogText.setVisibility(View.GONE);
		windowManager=(WindowManager) getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		windowManager.addView(mDialogText, lp);
		sideBar.setTextView(mDialogText);
		//绑定数据
		loadData();
	}

	private void loadData(){
		ProgressUtil.showProgress(mContext, "正在获取已开通城市信息");
		RequestBean<CityInfoResponse> requestBean = new RequestBean<CityInfoResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(CityInfoResponse.class);
		requestBean.setURL(Constant.Requrl.getCityList());
		requestServer(requestBean, new UICallback<CityInfoResponse>() {

			@Override
			public void onSuccess(CityInfoResponse respose) {
				ProgressUtil.dissmissProgress();
				if (respose.getList()!=null) {
					List<CityInfo> cityInfos=respose.getList();
					List<Content> contents=new ArrayList<Content>();
					for (CityInfo cityInfo : cityInfos) {
						String areaName=cityInfo.getCityName();
						String cityDomin=cityInfo.getCityDomain();
						String firstpinyin=cityInfo.getFirstpinyin();
						char firstLetter=cityInfo.getFirstpinyin().charAt(0);
						Content content=new Content(""+firstLetter, areaName,cityDomin,firstpinyin);
						contents.add(content);
					}
					Collections.sort(contents,new PinyinComparator());
					myAdapter=new MyAdapter(SelectCityActivity.this, contents,SelectCityActivity.this);
					mListView.setAdapter(myAdapter);
					sideBar.setListView(mListView);
				}
			}

			@Override
			public void onError(String message) {
				ProgressUtil.dissmissProgress();
				MeUtil.showToast(mContext, "请求网络出错");
			}

			@Override
			public void onOffline(String message) {
				ProgressUtil.dissmissProgress();
				MeUtil.showToast(mContext, "请求网络出错");
			}
		});
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,
			View RightImaView, TextView rightText, TextView title,
			View titleLayout) {
		title.setText("已开通城市");
	}


	@Override
	public void onCitySelected(Content content) {
		if (content.getCityDomain()!=null && content.getName()!=null) {
			Intent intent=new Intent();
			intent.putExtra("select_city", content);
			setResult(NewZoneSelectActivity.SELECT_CITY, intent);
			finish();
		}
	}

}
