package com.yulinoo.plat.life.views.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Category;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.bean.Tag;
import com.yulinoo.plat.life.bean.TagListBean;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.CategoryTagReq;
import com.yulinoo.plat.life.net.resbean.TagResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.MeNormalListView;
import com.yulinoo.plat.life.ui.widget.MyListView.OnRefreshListener;
import com.yulinoo.plat.life.ui.widget.listview.XListView;
import com.yulinoo.plat.life.ui.widget.listview.XListView.IXListViewListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.views.adapter.CategoryAdapter;
import com.yulinoo.plat.life.views.adapter.CategoryAdapter.OnCategorySelectedListener;
import com.yulinoo.plat.life.views.adapter.CategoryTagAdapter;
import com.yulinoo.plat.life.views.adapter.CategoryTagAdapter.OnCategoryTagSelectedListener;
import com.yulinoo.plat.melife.R;

import config.AppContext;

@SuppressLint({ "InlinedApi", "ResourceAsColor" })
public class CategorySelectActivity extends BaseActivity implements OnCategorySelectedListener,OnCategoryTagSelectedListener,IXListViewListener{
	public static final String tagListBean="tagListBean";
	public static final int selectTagOk=100;	

	private CategoryAdapter categoryAdapter;
	private TextView product;
	private CategoryTagAdapter categoryTagAdapter;
	private ListView categoryListView;
//	private MeNormalListView mListView;
//	private SwipeRefreshLayout mSwipeLayout;
	private XListView mListView;
	private List<Tag> mProductInfos=new ArrayList<Tag>();

	private Merchant merchant;

	private long selectedCategory;//已选择了标签的分类
	private Category nowCategory;//当前分类

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.shop_category);
		merchant = (Merchant) this.getIntent().getSerializableExtra(Constant.ActivityExtra.MERCHANT);
	}

	@Override
	protected void initComponent() {
		categoryAdapter = new CategoryAdapter(this);
		categoryAdapter.setOnCategorySelectedListener(this);
		categoryListView = (ListView) findViewById(R.id.myListView_category);
		categoryListView.setAdapter(categoryAdapter);
		categoryTagAdapter = new CategoryTagAdapter(mContext);
		categoryTagAdapter.setOnCategoryTagSelected(this);
		//		categoryTagListView = (ListView) findViewById(R.id.myListView_category_product);
		//		categoryTagListView.setAdapter(categoryTagAdapter);
//		mListView = (MeNormalListView) findViewById(R.id.list);
//		mListView.setAdapter(categoryTagAdapter);
//		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//		mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
//			@Override
//			public void onRefresh() {
//				loadTag();
//			}
//		});
//		MeUtil.setSwipeLayoutColor(mSwipeLayout);
//		mSwipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
		
		mListView = (XListView)findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		mListView.setAdapter(categoryTagAdapter);
		
		
		loadCategory();
		product = (TextView) findViewById(R.id.product);
		if(merchant!=null)
		{
			String tagNames=merchant.getMerchantTagNameArray();
			String tmpSids=merchant.getMerchantTagSidArray();
			if(tmpSids.length()>0)
			{
				String[] tas=tagNames.split(",");
				String[] tc=tmpSids.split(",");
				int len=tas.length;
				for(int kk=0;kk<len;kk++)
				{
					String name=tas[kk];
					String sid=tc[kk];
					Tag tag=new Tag();
					tag.setSid(Long.parseLong(sid));
					tag.setTagName(name);
					tag.setAlongCategorySid(nowCategory.getSid());
					mProductInfos.add(tag);
				}
				product.setText(tagNames);
			}
		}
	}

	//加载分类
	private void loadCategory() {
		List<Category> cityCategories = AppContext.currentCategorys(AppContext.currentAreaInfo().getAlongCitySid());
		if(cityCategories!=null&&cityCategories.size()>0)
		{
			List<Category> lstCategory=new ArrayList<Category>();
			for(Category ca:cityCategories)
			{
				if(ca.getMerchantCanChoice()==Constant.CATEGORY.MERCHANT_CAN_CHOICE)
				{
					lstCategory.add(ca);
					ca.setSelected(false);
					if(merchant!=null)
					{
						if(ca.getSid()==merchant.getCategorySid())
						{
							nowCategory=ca;
							selectedCategory=nowCategory.getSid();
							ca.setSelected(true);
						}
					}
				}
			}
			
			if(merchant==null)
			{
				nowCategory=lstCategory.get(0);
				selectedCategory=nowCategory.getSid();
				nowCategory.setSelected(true);
			}
			categoryAdapter.load(lstCategory);
			loadTag();
		}
	}

	private void loadTag() {//加载分类下的标签
		CategoryTagReq cfr=new CategoryTagReq();
		cfr.setCategorySid(nowCategory.getSid());
		RequestBean<TagResponse> requestBean = new RequestBean<TagResponse>();
		requestBean.setRequestBody(cfr);
		requestBean.setResponseBody(TagResponse.class);
		requestBean.setURL(Constant.Requrl.getCategoryTagList());
		//mSwipeLayout.setRefreshing(true);
		requestServer(requestBean, new UICallback<TagResponse>() {
			@Override
			public void onSuccess(TagResponse respose) {
				try {
					onLoad();
					loadDataDone(respose);
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

	private synchronized void loadDataDone(TagResponse respose)
	{
		//mSwipeLayout.setRefreshing(false);
		if (respose == null || respose.getList() == null)
			return;

		List<Tag> hotFoods = respose.getList();
		for(Tag tag:hotFoods)
		{
			tag.setAlongCategorySid(nowCategory.getSid());
			for(Tag tg:mProductInfos)
			{
				if(tag.getSid()==tg.getSid())
				{
					tag.checked=true;
				}
			}
		}
		categoryTagAdapter.clear();
		categoryTagAdapter.load(hotFoods);
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {

		title.setText("请选择店铺类别");
		rightImg.setVisibility(View.GONE);
		rightText.setText("完成");
		rightText.setVisibility(View.VISIBLE);
		rightText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mProductInfos==null||mProductInfos.size()==0)
				{
					showToast("请至少选择一种经营类型");
					return;
				}
				TagListBean productListBean = new TagListBean();
				productListBean.setTags(mProductInfos);
				Intent intent = new Intent();
				intent.putExtra(tagListBean, productListBean);

				setResult(selectTagOk, intent);
				finish();
			}
		});
	}

	@Override
	public void onCategorySelected(Category category) {
		nowCategory=category;
		selectedCategory=nowCategory.getSid();
		loadTag();
	}

	@Override
	public boolean onTagSelected(Tag tag,boolean checked) {
		if(checked)
		{//是选择某个标签
			if(-1==selectedCategory)
			{//说明之前没有任何选中的分类
				selectedCategory=tag.getAlongCategorySid();
			}
			long categorySid=tag.getAlongCategorySid();
			if(categorySid==selectedCategory)
			{//说明选择的是同一个分类
				String selectedTags=product.getText().toString();
				if(NullUtil.isStrNotNull(selectedTags))
				{//已有选中的标签
					String[] stags=selectedTags.split(",");
					if(stags.length>1)
					{
						showToast("最多允许选择两种类别");
						return false;
					}else
					{
						mProductInfos.add(tag);
						showSelectedTag();
						selectedCategory=tag.getAlongCategorySid();
						return true;
					}
				}else
				{//还未有选中的标签
					mProductInfos.add(tag);
					showSelectedTag();
					selectedCategory=tag.getAlongCategorySid();
					return true;
				}
			}else
			{//说明选择的是不同的分类下的标签，此种情况下则不允许
				showToast("只允许选择同一种分类下的类别");
				return false;
			}
		}else
		{//取消某个标签
			for(Tag tg:mProductInfos)
			{
				if(tg.getSid()==tag.getSid())
				{
					mProductInfos.remove(tg);
					break;
				}
			}
			showSelectedTag();
			if(mProductInfos.size()==0)
			{//没有选中的标签
				selectedCategory=-1;
			}
			return true;
		}
	}
	public void showSelectedTag()
	{
		int size=mProductInfos.size();
		if(size>0)
		{
			if(size==1)
			{
				product.setText(mProductInfos.get(0).getTagName());
			}else if(size==2)
			{
				product.setText(mProductInfos.get(0).getTagName()+","+mProductInfos.get(1).getTagName());
			}
		}else
		{
			product.setText("");
		}
	}
	
	@Override
	public void onRefresh() {
		loadTag();
	}
	@Override
	public void onLoadMore() {
	}
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getTime());
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
	}

	//	void setOnCheckListener() {
	//		categoryTagAdapter.setOnCheckedListener(new OnCheckedListener() {
	//			@Override
	//			public void onCheck(List<Tag> productInfos) {
	//				StringBuffer sb = new StringBuffer();
	//				mProductInfos = productInfos;
	//				for (Tag productInfo : productInfos) {
	//					if (sb.length() == 0) {
	//						sb.append(productInfo.getTagName());
	//					} else {
	//						sb.append(",").append(productInfo.getTagName());
	//					}
	//				}
	//				product.setText(sb.toString());
	//			}
	//		});
	//	}
}
