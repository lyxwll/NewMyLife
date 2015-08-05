package com.yulinoo.plat.life.views.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.yulinoo.plat.life.bean.Category;
import com.yulinoo.plat.life.bean.ForumInfo;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.bean.ProductInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.CategoryForumReq;
import com.yulinoo.plat.life.net.reqbean.ForumMerchantReq;
import com.yulinoo.plat.life.net.resbean.ForumNoteResponse;
import com.yulinoo.plat.life.net.resbean.ForumResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.MyTab;
import com.yulinoo.plat.life.ui.widget.MyTab.OnIabListener;
import com.yulinoo.plat.life.ui.widget.Tab;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.views.adapter.MerchantAdapter;
import com.yulinoo.plat.melife.R;

import config.AppContext;

/**
 * 分类下的栏目商家列表
 * 
 * @author Administrator
 * 
 */
@SuppressLint({ "NewApi", "ResourceAsColor" })
public class ForumMerchantActivity extends BaseActivity implements
		IXListViewListener {
	private Category nowCategory;
	private MerchantAdapter merchantAdapter;
	private HorizontalScrollView scrollView;
	ViewPager initZoneMainViewPager;
	List<View> zoneMainListViews = new ArrayList<View>();
	private TextView forumdesc;
	private int pageNo = 0;
	private XListView mListView;
	private MyTab zoneTab;
	private Tab nowTab;
	private boolean isEnd = false;
	private BroadcastReceiver goodsCommentBroadcastReceiver;
	private BroadcastReceiver goodPraiseBroadcastReceiver;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.forum_merchant_list);
		nowCategory = (Category) this.getIntent().getSerializableExtra(
				Constant.ActivityExtra.CATEGORY);
		initBroadcastReceiver();
	}

	@Override
	protected void initComponent() {

		zoneTab = (MyTab) findViewById(R.id.mytab);
		zoneTab.setOnCheckListener(new OnIabListener() {
			@Override
			public void onCheckedChanged(Tab tb) {
				if (nowTab != null) {
					if (nowTab.getIndex() == tb.getIndex()) {
						return;
					}
				}
				nowTab = tb;
				mListView.autoRefresh();
			}
		});
		merchantAdapter = new MerchantAdapter(mContext);
		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(merchantAdapter);
		loadForum();
	}

	private void loadForum() {// 加载栏目
		List<ForumInfo> lstForums = AppContext.getForumByCategoryId(nowCategory
				.getSid());
		if (lstForums != null && lstForums.size() > 0) {
			showForumTab();
		} else {
			CategoryForumReq cfr = new CategoryForumReq();
			cfr.setCategorySid(nowCategory.getSid());
			RequestBean<ForumResponse> requestBean = new RequestBean<ForumResponse>();
			requestBean.setHttpMethod(HTTP_METHOD.POST);
			requestBean.setRequestBody(cfr);
			requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
			requestBean.setResponseBody(ForumResponse.class);
			requestBean.setURL(Constant.Requrl.getForumList());
			requestServer(requestBean, new UICallback<ForumResponse>() {

				@Override
				public void onSuccess(ForumResponse respose) {
					try {
						if (respose == null || respose.getList() == null)
							return;
						List<ForumInfo> hotFoods = respose.getList();
						new Delete()
								.from(ForumInfo.class)
								.where("alongCategorySid=?",
										nowCategory.getSid()).execute();
						for (ForumInfo fi : hotFoods) {
							fi.setAlongCategorySid(nowCategory.getSid());
							if (fi.getForumType() == ForumInfo.FORUM_PRODUCT) {// 产品型栏目，则保存栏目对应的产品主键
								fi.setProductSid(fi.getProduct().getSid());
							} else {
								fi.setProductSid(-1);
							}
							AppContext.saveForumInfo(fi);
						}
						showForumTab();
					} catch (Exception e) {
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
	}

	private void showForumTab() {
		try {
			final List<ForumInfo> hotFoods = AppContext
					.getForumByCategoryId(nowCategory.getSid());
			if (hotFoods.size() > 0) {
				int size = hotFoods.size();// 主要是要加主页这个固定的页面
				List<Tab> listProducts = new ArrayList<Tab>(size);
				for (int i = 0; i < size; i++) {
					ForumInfo pi = hotFoods.get(i);
					Tab tab = new Tab();
					tab.setProductSid(pi.getSid());
					tab.setName(pi.getForumName());
					tab.setForumSid(pi.getSid());
					tab.setCategorySid(pi.getAlongCategorySid());
					tab.setIndex(i);
					tab.setSelected(false);
					if (pi.getProduct() != null) {
						ProductInfo product = pi.getProduct();
						if (ProductInfo.PERSONAL_CAN_PUBLISH == product
								.getPermissionPersional()) {
							tab.setPermissionPersional(true);
						} else {
							tab.setPermissionPersional(false);
						}
					} else {
						tab.setPermissionPersional(false);
					}
					listProducts.add(tab);
				}
				listProducts.get(0).setSelected(true);
				zoneTab.loadZoneTab(listProducts);
			}

		} catch (Exception e) {
			e.printStackTrace();
			showToast("暂无数据");
		}
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,
			View RightImaView, TextView rightText, TextView title,
			View titleLayout) {
		title.setText(nowCategory.getCategoryName());
		rightImg.setVisibility(View.GONE);
		rightText.setVisibility(View.VISIBLE);
		rightText.setText(R.string.suggestion_recommend_merchant);
		rightText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mContext.startActivity(new Intent(mContext,
						SuggestionActivity.class).putExtra(
						Constant.ActivityExtra.SUGGESTION_TYPE,
						Constant.SuggestionType.RECOMMEND_MERCHANT));
			}
		});
	}

	private void loadForumMerchant(final boolean isUp) {// 加载栏目下的商家
		if (isUp) {
			isEnd = false;
			pageNo = 0;
		} else {
			if (isEnd) {
				onLoad();
				return;
			}
			pageNo++;
		}
		ForumMerchantReq merchantReq = new ForumMerchantReq();
		merchantReq.setPageNo(pageNo);
		merchantReq.setAlongForumSid(nowTab.getForumSid());
		merchantReq.setCategorySid(nowTab.getCategorySid());
		merchantReq.setQueryType(Constant.AREA.QUERY_OWN_AREAINFO);
		merchantReq.setAreaInfoSid(AppContext.currentAreaInfo().getSid());
		RequestBean<ForumNoteResponse> requestBean = new RequestBean<ForumNoteResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(merchantReq);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(ForumNoteResponse.class);
		requestBean.setURL(Constant.Requrl.getForumMerchantList());
		requestServer(requestBean, new UICallback<ForumNoteResponse>() {

			@Override
			public void onSuccess(ForumNoteResponse respose) {
				try {
					loadDataDone(respose, isUp);
				} catch (Exception e) {
				}
			}

			@Override
			public void onError(String message) {
				isEnd = true;
				onLoad();
				showToast(message);
			}

			@Override
			public void onOffline(String message) {
				isEnd = true;
				onLoad();
				showToast(message);
			}
		});
	}

	private synchronized void loadDataDone(ForumNoteResponse respose,
			boolean isUp) {
		if (isUp) {
			merchantAdapter.clear();
		} else {
		}
		onLoad();
		List<ForumNote> listFns = respose.getList();
		if (listFns != null) {
			if (listFns.size() == 0) {
				isEnd = true;
			}
			merchantAdapter.load(listFns);
		}
	}

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	@Override
	public void onRefresh() {
		loadForumMerchant(true);
	}

	@Override
	public void onLoadMore() {
		loadForumMerchant(false);
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getTime());
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	private void initBroadcastReceiver() {
		goodsCommentBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.BroadType.ADD_COMMENT_OK);
		this.registerReceiver(goodsCommentBroadcastReceiver, filter);

		goodPraiseBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(Constant.BroadType.ADD_PRAISE_OK);
		this.registerReceiver(goodPraiseBroadcastReceiver, filter2);
	}

	private class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constant.BroadType.ADD_COMMENT_OK)) {
				long goodsSid = intent.getLongExtra(
						Constant.ActivityExtra.GOODSSID, -1L);
				if (goodsSid > 0) {
					goodsNumberAdd(goodsSid, Constant.MSGTYPE.TYPE_COMMENT);
				}
			} else if (action.equals(Constant.BroadType.ADD_PRAISE_OK)) {
				long goodsSid = intent.getLongExtra(
						Constant.ActivityExtra.GOODSSID, -1L);
				if (goodsSid > 0) {
					goodsNumberAdd(goodsSid, Constant.MSGTYPE.TYPE_PRAISE);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		try {
			this.unregisterReceiver(goodsCommentBroadcastReceiver);
			this.unregisterReceiver(goodPraiseBroadcastReceiver);
		} catch (Exception e) {
		}
		super.onDestroy();
	}

	// 主要用于商品的评论和点赞数量的增加,向本类提供的方法,其他地方不会使用
	public void goodsNumberAdd(Long goodsSid, int type) {
		if (goodsSid != null) {
			if (merchantAdapter != null) {
				List<ForumNote> list = merchantAdapter.getList();
				for (ForumNote fonote : list) {
					if (fonote.getGoodsSid().longValue() == goodsSid
							.longValue()) {
						if (type == Constant.MSGTYPE.TYPE_COMMENT) {// 是评论
							fonote.setCommentNumber(fonote.getCommentNumber()
									.intValue() + 1);
						} else if (type == Constant.MSGTYPE.TYPE_PRAISE) {// 是赞
							fonote.setPraiseNumber(fonote.getPraiseNumber()
									.intValue() + 1);
						}
					}
				}
				merchantAdapter.notifyDataSetChanged();
			}
		}
	}
}
