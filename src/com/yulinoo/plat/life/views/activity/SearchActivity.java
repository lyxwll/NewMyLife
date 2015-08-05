package com.yulinoo.plat.life.views.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.HotSearch;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.GuessYourLoveReq;
import com.yulinoo.plat.life.net.reqbean.HotSearchReq;
import com.yulinoo.plat.life.net.reqbean.SearchReq;
import com.yulinoo.plat.life.net.resbean.HotSearchResponse;
import com.yulinoo.plat.life.net.resbean.MerchantResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.MeNormalListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.life.views.adapter.HotSearchAdapter;
import com.yulinoo.plat.life.views.adapter.MyLinkedUsrAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;
//搜索
public class SearchActivity extends BaseActivity implements OnClickListener,IXListViewListener{
	private int pageNo=0;
	private Merchant merchant;
	private MyLinkedUsrAdapter myConcernAdapter;
	//	private MeNormalListView mListView;
	//	private SwipeRefreshLayout mSwipeLayout;
	private XListView mListView;

	private MeNormalListView hot_merchant_list;
	private MyLinkedUsrAdapter hotMerchantAdapter;
	private List<Merchant> listHotMerchants;
	private List<HotSearch> listHotSearchs;

	private TextView hot_search_tv;
	private TextView hot_merchant_tv;
	private LinearLayout hot_search_ll;

	private EditText search_cont_et;
	private View go_search;

	private boolean isme;
	private boolean isEnd=false;
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.search);
	}

	@SuppressLint({ "InlinedApi", "ResourceAsColor" })
	@Override
	protected void initComponent() {
		myConcernAdapter = new MyLinkedUsrAdapter(this);
		//		mListView = (MeNormalListView) findViewById(R.id.list);
		//		mListView.setAdapter(myConcernAdapter);
		//		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		//		mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
		//			@Override
		//			public void onRefresh() {
		//				loadFuzzySearch(true);
		//			}
		//		});
		//		mSwipeLayout.setOnLoadListener(new OnLoadListener() {
		//			@Override
		//			public void onLoad() {
		//				loadFuzzySearch(false);
		//			}
		//		});
		//		MeUtil.setSwipeLayoutColor(mSwipeLayout);
		//		mSwipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(false);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(myConcernAdapter);

		search_cont_et=(EditText)findViewById(R.id.search_cont_et);
		go_search=findViewById(R.id.go_search);
		go_search.setOnClickListener(this);


		hot_search_ll=(LinearLayout)findViewById(R.id.hot_search_ll);
		hot_search_tv=(TextView) findViewById(R.id.hot_search_tv);
		hot_merchant_tv=(TextView) findViewById(R.id.hot_merchant_tv);
		hot_search_tv.setOnClickListener(this);
		hot_merchant_tv.setOnClickListener(this);

		hot_merchant_list=(MeNormalListView) findViewById(R.id.hot_merchant_list);
		hotMerchantAdapter=new MyLinkedUsrAdapter(this);
		hot_merchant_list.setAdapter(hotMerchantAdapter);

		loadHotSearch();
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {


	}

	//加载热门搜索
	private void loadHotSearch()
	{
		hot_search_tv.setTextColor(getResources().getColor(R.color.main_txt_color));
		hot_merchant_tv.setTextColor(getResources().getColor(R.color.assist_txt_color));
		hot_search_ll.setVisibility(View.VISIBLE);
		hot_merchant_list.setVisibility(View.GONE);
		//mSwipeLayout.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
		if(listHotSearchs==null)
		{
			if(AppContext.currentAreaInfo()==null)
			{
				return;
			}
			HotSearchReq guessYourLoveReq = new HotSearchReq();
			guessYourLoveReq.setCitySid(AppContext.currentAreaInfo().getAlongCitySid());
			RequestBean<HotSearchResponse> requestBean = new RequestBean<HotSearchResponse>();
			requestBean.setRequestBody(guessYourLoveReq);
			requestBean.setResponseBody(HotSearchResponse.class);
			requestBean.setURL(Constant.Requrl.getHotSearch());
			requestServer(requestBean, new UICallback<HotSearchResponse>() {

				@Override
				public void onSuccess(HotSearchResponse respose) {
					try {
						List<HotSearch> lstMerchants = respose.getList();
						if(lstMerchants!=null)
						{
							listHotSearchs=lstMerchants;
							//hot_search_ll
							for(HotSearch hs:lstMerchants)
							{
								String itemValue=hs.getItemValue();
								String itemArray=hs.getItemArray();
								if(NullUtil.isStrNotNull(itemValue)&&NullUtil.isStrNotNull(itemArray))
								{
									View view = getLayoutInflater().inflate(R.layout.item_hot_serach, null);
									TextView hot_search_tv=(TextView)view.findViewById(R.id.hot_search_tv);
									hot_search_tv.setText(itemValue);
									GridView hot_search_gridview=(GridView)view.findViewById(R.id.hot_search_gridview);
									hot_search_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
									List<String> listData=new ArrayList<String>();
									String[] items=itemArray.split(",");
									for(String str:items)
									{
										listData.add(str);
									}
									HotSearchAdapter hsAdapter=new HotSearchAdapter(mContext);
									hot_search_gridview.setAdapter(hsAdapter);
									setOnGridViewItemListener(hot_search_gridview,hsAdapter);
									hsAdapter.load(listData);
									hot_search_ll.addView(view);
								}
							}
						}
					} catch (Exception e) {
					}
				}

				@Override
				public void onError(String message) {
					//mSwipeLayout.setRefreshing(false);
					onLoad();
					showToast(message);
				}

				@Override
				public void onOffline(String message) {
					//mSwipeLayout.setRefreshing(false);
					onLoad();
					showToast(message);
				}
			});
		}
	}

	//某个分类被选中了，则进入对应的栏目列表中
	private void setOnGridViewItemListener(GridView gridView, final HotSearchAdapter zoneMainFunctionAdapter) {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String key=zoneMainFunctionAdapter.getItem(arg2);
				search_cont_et.setText("");
				search_cont_et.setText(key);
				hot_search_ll.setVisibility(View.GONE);
				hot_merchant_list.setVisibility(View.GONE);
				//				mSwipeLayout.setVisibility(View.VISIBLE);
				//				mSwipeLayout.startRefresh();				
				mListView.setVisibility(View.VISIBLE);
				mListView.autoRefresh();				
			}
		});
	}

	//加载热门商家
	private void loadHotMerchant()
	{
		hot_search_tv.setTextColor(getResources().getColor(R.color.assist_txt_color));
		hot_merchant_tv.setTextColor(getResources().getColor(R.color.main_txt_color));
		hot_search_ll.setVisibility(View.GONE);
		//mSwipeLayout.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
		hot_merchant_list.setVisibility(View.VISIBLE);
		if(listHotMerchants==null)
		{
			if(AppContext.currentAreaInfo()==null)
			{
				return;
			}

			GuessYourLoveReq guessYourLoveReq = new GuessYourLoveReq();//复用
			guessYourLoveReq.setAreaSid(AppContext.currentAreaInfo().getSid());
			RequestBean<MerchantResponse> requestBean = new RequestBean<MerchantResponse>();
			requestBean.setRequestBody(guessYourLoveReq);
			requestBean.setResponseBody(MerchantResponse.class);
			requestBean.setURL(Constant.Requrl.getHotMerchant());
			ProgressUtil.showProgress(mContext, "加载中...");
			requestServer(requestBean, new UICallback<MerchantResponse>() {
				@Override
				public void onSuccess(MerchantResponse respose) {
					ProgressUtil.dissmissProgress();
					try {
						List<Merchant> lstMerchants = respose.getList();
						if(lstMerchants!=null)
						{
							listHotMerchants=lstMerchants;
							hotMerchantAdapter.load(listHotMerchants);
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
		}
	}

	public void loadFuzzySearch(final boolean isUp) {
		hot_search_ll.setVisibility(View.GONE);
		//mSwipeLayout.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.VISIBLE);
		hot_merchant_list.setVisibility(View.GONE);
		if(isUp)
		{
			isEnd=false;
			pageNo=0;
		}else
		{
			if(isEnd)
			{
				//mSwipeLayout.setLoading(false);
				onLoad();
				return;
			}
			pageNo++;
		}
		AreaInfo ai=AppContext.currentAreaInfo();
		String search_cont=search_cont_et.getText().toString();
		SearchReq searchReq = new SearchReq();
		searchReq.setLatItude(ai.getLatItude());
		searchReq.setLongItude(ai.getLongItude());
		searchReq.setKey(search_cont);
		RequestBean<MerchantResponse> requestBean = new RequestBean<MerchantResponse>();
		requestBean.setRequestBody(searchReq);
		requestBean.setResponseBody(MerchantResponse.class);
		requestBean.setURL(Constant.Requrl.getFuzzySearch());
		requestServer(requestBean, new UICallback<MerchantResponse>() {
			@Override
			public void onSuccess(MerchantResponse respose) {
				try {
					loadDataDone(respose,isUp);
				} catch (Exception e) {
				}
			}

			@Override
			public void onError(String message) {
				isEnd=true;
				//				if(isUp)
				//				{
				//					mSwipeLayout.setRefreshing(false);
				//				}else
				//				{
				//					mSwipeLayout.setLoading(false);
				//				}
				onLoad();
				showToast(message);
			}

			@Override
			public void onOffline(String message) {
				isEnd=true;
				//				if(isUp)
				//				{
				//					mSwipeLayout.setRefreshing(false);
				//				}else
				//				{
				//					mSwipeLayout.setLoading(false);
				//				}
				onLoad();
				showToast(message);
			}
		});

	}

	private void loadDataDone(MerchantResponse respose,boolean isUp)
	{
		if(isUp)
		{
			//mSwipeLayout.setRefreshing(false);
			myConcernAdapter.clear();
		}else
		{
			//mSwipeLayout.setLoading(false);
		}
		onLoad();
		List<Merchant> listFns=respose.getList();
		if(listFns!=null)
		{
			if(listFns.size()==0)
			{
				isEnd=true;
				return;
			}
			myConcernAdapter.load(listFns);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.hot_search_tv:
		{
			loadHotSearch();
			break;
		}
		case R.id.hot_merchant_tv:
		{
			loadHotMerchant();
			break;
		}
		case R.id.go_search:
		{
			AreaInfo ai=AppContext.currentAreaInfo();
			if(ai==null)
			{
				return;
			}

			String search_cont=search_cont_et.getText().toString();
			if(NullUtil.isStrNotNull(search_cont))
			{
				//mSwipeLayout.startRefresh();
				mListView.autoRefresh();
				MeUtil.closeSoftPad(this);
			}else
			{
				showToast("请输入要搜索的内容");
			}
			break;
		}
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}

	@Override
	public void onRefresh() {
		loadFuzzySearch(true);		
	}

	@Override
	public void onLoadMore() {
		loadFuzzySearch(false);		
	}
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getTime());
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
	}
}
