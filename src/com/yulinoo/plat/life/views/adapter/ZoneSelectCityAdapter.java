package com.yulinoo.plat.life.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.CityInfo;
import com.yulinoo.plat.melife.R;

public class ZoneSelectCityAdapter extends YuLinooLoadAdapter<CityInfo> {

	private LayoutInflater inflater;

	public ZoneSelectCityAdapter(Context context,OnCitySelectedListener onCitySelectedListener) {
		inflater = LayoutInflater.from(context);
		this.onCitySelectedListener=onCitySelectedListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		final CityInfo item = getItem(position);
		if (convertView == null) {
			holder = new HolderView();
			convertView = inflater.inflate(R.layout.zone_select_city, parent,false);
			holder.zone_select_city_name = (TextView) convertView.findViewById(R.id.zone_select_city_name);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.zone_select_city_name.setText(item.getCityName());
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(onCitySelectedListener!=null)
				{
					onCitySelectedListener.onCitySelected(item);
				}
			}
		});
		return convertView;
	}

	private class HolderView {
		public TextView zone_select_city_name;
	}
	
	private OnCitySelectedListener onCitySelectedListener;
	public OnCitySelectedListener getOnCitySelectedListener() {
		return onCitySelectedListener;
	}

	public void setOnCitySelectedListener(
			OnCitySelectedListener onCitySelectedListener) {
		this.onCitySelectedListener = onCitySelectedListener;
	}

	public interface OnCitySelectedListener
	{
		public void onCitySelected(CityInfo city);
	}

}
