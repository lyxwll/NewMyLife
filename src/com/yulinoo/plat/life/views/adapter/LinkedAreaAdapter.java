package com.yulinoo.plat.life.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.melife.R;

public class LinkedAreaAdapter extends YuLinooLoadAdapter<AreaInfo> {
	
	private LayoutInflater inflater;


	public LinkedAreaAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		final AreaInfo item = getItem(position);

		if (convertView == null) {
			holder = new HolderView();
			convertView = inflater.inflate(R.layout.linked_area_pop_item, parent,false);
			holder.area_name = (TextView) convertView.findViewById(R.id.area_name);
			holder.function_icon = (ImageView) convertView.findViewById(R.id.function_icon);
			convertView.setTag(holder);

		} else {
			holder = (HolderView) convertView.getTag();
		}
		//loadProfile(item, item.getLogoPic(), holder.function_icon, true);
		holder.area_name.setText(item.getAreaName());
		return convertView;
	}
	
	private class HolderView {
		public TextView area_name;
		public ImageView function_icon;
	}

}
