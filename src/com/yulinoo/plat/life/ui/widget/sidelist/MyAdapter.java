package com.yulinoo.plat.life.ui.widget.sidelist;

import java.util.List;

import com.yulinoo.plat.life.bean.CityInfo;
import com.yulinoo.plat.life.views.adapter.ZoneSelectCityAdapter.OnCitySelectedListener;
import com.yulinoo.plat.melife.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter implements SectionIndexer{

	private List<Content> list = null;
	private Context mContext;
	private SectionIndexer mIndexer;
	private OnCitySelectedListener onCitySelectedListener;
	
	public MyAdapter(Context mContext, List<Content> list,OnCitySelectedListener onCitySelectedListener) {
		this.mContext = mContext;
		this.list = list;
		this.onCitySelectedListener=onCitySelectedListener;
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list != null ? list.get(position) : null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final Content mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.side_list_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.titleLayout=view.findViewById(R.id.title_ll);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		if (position == 0) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getLetter());
		} else {
			String lastCatalog = list.get(position - 1).getLetter();
			if (mContent.getLetter().equals(lastCatalog)) {
				viewHolder.tvLetter.setVisibility(View.GONE);
			} else {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(mContent.getLetter());
			}
		}
		viewHolder.tvTitle.setText(this.list.get(position).getName());
		viewHolder.titleLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (onCitySelectedListener!=null) {
					onCitySelectedListener.onCitySelected(mContent);
				}
			}
		});
		return view;
	}
	


	final static class ViewHolder {
		TextView tvTitle;
		TextView tvLetter;
		View titleLayout;
	}


	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSectionForPosition(int position) {
		
		return 0;
	}

	public int getPositionForSection(int section) {
		Content mContent;
		String l;
		if (section == '!') {
			return 0;
		} else {
			for (int i = 0; i < getCount(); i++) {
				mContent = (Content) list.get(i);
				l = mContent.getLetter();
				char firstChar = l.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i + 1;
				}

			}
		}
		mContent = null;
		l = null;
		return -1;
	}
	
	public OnCitySelectedListener getOnCitySelectedListener() {
		return onCitySelectedListener;
	}

	public void setOnCitySelectedListener(
			OnCitySelectedListener onCitySelectedListener) {
		this.onCitySelectedListener = onCitySelectedListener;
	}

	public interface OnCitySelectedListener
	{
		public void onCitySelected(Content content);
	}
}