package com.yulinoo.plat.life.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.yulinoo.plat.life.utils.FaceConversionUtil;
import com.yulinoo.plat.melife.R;

//对表情视图的封装
public class MeFaceViewWiget extends RelativeLayout {
	private Context mContext;
	private ViewPager viewPager;//存储表情符
	//表情
	private int[] eImages;
	private String[] eImageNames;
	private ArrayList<GridView> grids;
	private GridView gView1;
	private List<Map<String, Object>> listItems;
	private SimpleAdapter simpleAdapter;
	public MeFaceViewWiget(Context context) {
		super(context);
		mContext = context;
	}
	public MeFaceViewWiget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		viewPager= (ViewPager)LayoutInflater.from(mContext).inflate(R.layout.me_face_wiget, null);
		showData();
		this.addView(viewPager);
	}

	//展示表情
	private void showData() {
		eImages = FaceConversionUtil.getInstance().expressionImgs;
		eImageNames = FaceConversionUtil.getInstance().expressionImgNames;
		grids = new ArrayList<GridView>();
		gView1 = (GridView) LayoutInflater.from(mContext).inflate(R.layout.face_grid, null);
		gView1.setSelector(new ColorDrawable(Color.TRANSPARENT));
		listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < eImages.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", eImages[i]);
			listItems.add(listItem);
		}
		simpleAdapter = new SimpleAdapter(mContext, listItems,
				R.layout.single_expression, new String[] { "image" },
				new int[] { R.id.image });
		gView1.setAdapter(simpleAdapter);
		grids.add(gView1);
		setOnGridViewItemListener(gView1, simpleAdapter);

		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return grids.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(grids.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(grids.get(position));
				return grids.get(position);
			}
		};

		viewPager.setAdapter(mPagerAdapter);

		//viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}


	//某个分类被选中了，则进入对应的栏目列表中
	private void setOnGridViewItemListener(GridView gridView, final SimpleAdapter simpleAdapter) {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				if(onFaceClickListener!=null)
				{
					SpannableString spannableString = FaceConversionUtil.getInstance()
							.addFace(mContext, eImages[arg2], eImageNames[arg2]);
					onFaceClickListener.onFaceClick(spannableString);
				}
			}
		});
	}

	private OnFaceClickListener onFaceClickListener;

	public interface OnFaceClickListener
	{
		public void onFaceClick(SpannableString spannableString);
	}

	public OnFaceClickListener getOnFaceClickListener() {
		return onFaceClickListener;
	}
	public void setOnFaceClickListener(OnFaceClickListener onFaceClickListener) {
		this.onFaceClickListener = onFaceClickListener;
	}


}
