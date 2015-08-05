package com.yulinoo.plat.life.views.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Category;
import com.yulinoo.plat.melife.R;

public class CategoryAdapter extends YuLinooLoadAdapter<Category>{
	private LayoutInflater inflater;

	private Context mContext;
	public CategoryAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mContext=context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		final Category item = getItem(position);

		if (convertView == null) {
			holder = new HolderView();
			convertView = inflater.inflate(R.layout.category_item, parent,false);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);

		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.name.setText(item.getCategoryName());
		if(item.isSelected())
		{
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.assist_txt_color));
		}else
		{
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<Category> lstCategory=getList();
				for(Category ca:lstCategory)
				{
					if(item.getSid()==ca.getSid())
					{
						ca.setSelected(true);
					}else
					{
						ca.setSelected(false);
					}
				}
				if(onCategorySelectedListener!=null)
				{
					onCategorySelectedListener.onCategorySelected(item);
				}
				notifyDataSetChanged();
			}
		});

		return convertView;
	}	

	private class HolderView {
		public TextView name;
	}
	
	public interface OnCategorySelectedListener
	{
		public void onCategorySelected(Category category);
	}
	private OnCategorySelectedListener onCategorySelectedListener;
	public OnCategorySelectedListener getOnCategorySelectedListener() {
		return onCategorySelectedListener;
	}

	public void setOnCategorySelectedListener(
			OnCategorySelectedListener onCategorySelectedListener) {
		this.onCategorySelectedListener = onCategorySelectedListener;
	}
	
}
