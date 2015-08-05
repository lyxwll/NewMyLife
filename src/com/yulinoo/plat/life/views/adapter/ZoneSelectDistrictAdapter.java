package com.yulinoo.plat.life.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.DistrictInfo;
import com.yulinoo.plat.melife.R;

public class ZoneSelectDistrictAdapter extends YuLinooLoadAdapter<DistrictInfo> {

	private LayoutInflater inflater;

	public ZoneSelectDistrictAdapter(Context context,OnDistrictSelectedListener onDistrictSelectedListener) {
		inflater = LayoutInflater.from(context);
		this.onDistrictSelectedListener=onDistrictSelectedListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		final DistrictInfo item = getItem(position);

		if (convertView == null) {
			holder = new HolderView();
			convertView = inflater.inflate(R.layout.zone_select_district, parent,false);
			holder.zone_select_distinct_name = (TextView) convertView.findViewById(R.id.zone_select_distinct_name);
			
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.zone_select_distinct_name.setText(item.getDistrictName());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onDistrictSelectedListener!=null)
				{
					onDistrictSelectedListener.onDistrictSelected(item);
				}
			}
		});
		return convertView;
	}

	private class HolderView {
		public TextView zone_select_distinct_name;
	}
	private OnDistrictSelectedListener onDistrictSelectedListener;
	
	public OnDistrictSelectedListener getOnDistrictSelectedListener() {
		return onDistrictSelectedListener;
	}

	public void setOnDistrictSelectedListener(
			OnDistrictSelectedListener onDistrictSelectedListener) {
		this.onDistrictSelectedListener = onDistrictSelectedListener;
	}

	public interface OnDistrictSelectedListener
	{
		public void onDistrictSelected(DistrictInfo district);
	}
}
