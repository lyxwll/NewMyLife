package com.yulinoo.plat.life.views.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Category;
import com.yulinoo.plat.life.utils.DensityUtil;
import com.yulinoo.plat.life.utils.ImageThumbnail;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.melife.R;

public class ZoneMainFunctionAdapter extends YuLinooLoadAdapter<Category> {
	private Context mContext;
	private LayoutInflater inflater;
	private MyApplication myapp;
	public ZoneMainFunctionAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mContext=context;
		myapp=((MyApplication)context.getApplicationContext());
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		final Category item = getItem(position);
		if (convertView == null) {
			holder = new HolderView();
			convertView = inflater.inflate(R.layout.main_category_gridview_item, parent,false);
			holder.function_name = (TextView) convertView.findViewById(R.id.function_name);
			holder.function_icon = (ImageView) convertView.findViewById(R.id.function_icon);
			holder.funtion_detail=(TextView) convertView.findViewById(R.id.function_detail);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		MeImageLoader.displayImage(item.getCategoryPic(), holder.function_icon, myapp.getHeadIconOption());
		holder.function_name.setText(item.getCategoryName());
		holder.funtion_detail.setText(item.getCategoryDesc());
		GradientDrawable gd=new GradientDrawable();
		if (NullUtil.isStrNotNull(item.getColor())) {
			gd.setColor(Color.parseColor("#"+item.getColor()));
			gd.setCornerRadius(DensityUtil.dip2px(mContext, 5));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				convertView.setBackground(gd);
			} else {
				convertView.setBackgroundDrawable(gd);
			}
		}
		return convertView;
	}
	public StateListDrawable createSelectedListDrawable(String col) {
		StateListDrawable drawable = new StateListDrawable();
		GradientDrawable gd=new GradientDrawable();
		gd.setColor(Color.parseColor("#"+col));
		gd.setCornerRadius(10);
		drawable.addState(new int[]{-android.R.attr.state_selected,-android.R.attr.state_pressed,-android.R.attr.state_focused},gd);
		GradientDrawable gd2=new GradientDrawable();
		gd2.setColor(Color.parseColor("#e9e9e9"));
		gd2.setCornerRadius(10);
		drawable.addState(new int[]{android.R.attr.state_selected,android.R.attr.state_pressed,android.R.attr.state_focused},gd2);//前面加个负号表示未选中状态
		return drawable;
	}
	
//	public  ColorStateList createColorSelectStateList(int selected, int normal) {
//		int[] colors = new int[] { selected, normal };
//		int[][] states = new int[2][];
//		states[0] = new int[] { android.R.attr.state_selected};
//		states[1] = new int[] { -android.R.attr.state_selected};
//		ColorStateList colorList = new ColorStateList(states, colors);
//		Drawable concern_selected_background_true= mContext.getResources().getDrawable(R.drawable.concern_selected_background_true);
//		
//		GradientDrawable gd=new GradientDrawable();
//		StateListDrawable sd=new StateListDrawable();
//		sd.addState(new int[]{android.R.attr.state_checked},gd);
//		
//		return colorList;
//	}

	private class HolderView {
		public TextView function_name;
		public ImageView function_icon;
		public TextView funtion_detail;
	}
}
