package com.yulinoo.plat.life.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Category;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.melife.R;

public class HotSearchAdapter extends YuLinooLoadAdapter<String> {
	private Context mContext;
	private LayoutInflater inflater;
	public HotSearchAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mContext=context;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		final String item = getItem(position);

		if (convertView == null) {
			holder = new HolderView();
			convertView = inflater.inflate(R.layout.item_hot_serach_word, parent,false);
			holder.function_name = (TextView) convertView.findViewById(R.id.hot_search_tv);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.function_name.setText(item);
		return convertView;
	}
	
	private class HolderView {
		public TextView function_name;
	}

}
