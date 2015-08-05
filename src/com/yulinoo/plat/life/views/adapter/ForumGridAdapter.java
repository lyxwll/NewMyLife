package com.yulinoo.plat.life.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.ForumInfo;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.melife.R;

public class ForumGridAdapter extends YuLinooLoadAdapter<ForumInfo> {
	private Context mContext;
	private LayoutInflater inflater;
	private MyApplication myapp;
	public ForumGridAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mContext=context;
		myapp=((MyApplication)context.getApplicationContext());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		final ForumInfo item = getItem(position);

		if (convertView == null) {
			holder = new HolderView();
			convertView = inflater.inflate(R.layout.main_category_gridview_item, parent,false);
			holder.function_name = (TextView) convertView.findViewById(R.id.function_name);
			holder.function_icon = (ImageView) convertView.findViewById(R.id.function_icon);
//			int forum_picture_size=SizeUtil.forum_picture_size(mContext);
//			holder.function_icon.getLayoutParams().width=forum_picture_size;
//			holder.function_icon.getLayoutParams().height=forum_picture_size;
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
	    MeImageLoader.displayImage(item.getPhotoSrc(), holder.function_icon, myapp.getHeadIconOption());
		holder.function_name.setText(item.getForumName());
		return convertView;
	}
	
	private class HolderView {
		public TextView function_name;
		public ImageView function_icon;
	}

}
