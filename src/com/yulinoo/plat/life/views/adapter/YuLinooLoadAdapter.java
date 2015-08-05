package com.yulinoo.plat.life.views.adapter;

import java.util.ArrayList;
import java.util.List;

import android.widget.BaseAdapter;

public abstract class YuLinooLoadAdapter<T> extends BaseAdapter {

	private List<T> list = new ArrayList<T>();

	public void clear() {
		list.clear();
	}
	public List<T> getList()
	{
		return list;
	}
//	public YuLinooLoadAdapter()
//	{
////		roundOptions = new DisplayImageOptions.Builder()
////		.showImageOnLoading(R.drawable.ic_stub)
////		.showImageForEmptyUri(R.drawable.ic_empty)
////		.showImageOnFail(R.drawable.ic_error)
////		.cacheInMemory(true)
////		.cacheOnDisk(true)
////		.considerExifParams(true)
////		.displayer(new RoundedBitmapDisplayer(100))
////		.build();
////		rectOptions = new DisplayImageOptions.Builder()
////		.showImageOnLoading(R.drawable.ic_stub)
////		.showImageForEmptyUri(R.drawable.ic_empty)
////		.showImageOnFail(R.drawable.ic_error)
////		.cacheInMemory(true)
////		.cacheOnDisk(true)
////		.considerExifParams(true)
////		.displayer(new RoundedBitmapDisplayer(0))
////		.build();
//	}
//	public ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
//	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
//
//		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
//
//		@Override
//		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//			if (loadedImage != null) {
//				ImageView imageView = (ImageView) view;
//				boolean firstDisplay = !displayedImages.contains(imageUri);
//				if (firstDisplay) {
//					FadeInBitmapDisplayer.animate(imageView, 500);
//					displayedImages.add(imageUri);
//				}
//			}
//		}
//	}
	
	public void load(List<T> listData, boolean showBackground) {
		if (listData != null && listData.size() > 0) {
			for (T t : listData) {
				if (!list.contains(t)) {
					list.add(t);
				}
			}
		}
		this.notifyDataSetChanged();
	}

	public void load(List<T> listData) {
		list.addAll(listData);
		this.notifyDataSetChanged();
	}
	public void load(T data) {
		list.add(data);
		this.notifyDataSetChanged();
	}



	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public T getItem(int position) {
		return list != null ? list.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		View v = getView(position, convertView,parent);
////		if(getCount() % 2 == 1 && showBackground) {
////			
////			if (position % 2 == 1) {
////				v.setBackgroundResource(R.color.white);
////			} else {
////				v.setBackgroundResource(R.color.listview_item_color);
////			}
////			
////		} else if(showBackground){
////			if (position % 2 == 1) {
////				v.setBackgroundResource(R.color.listview_item_color);
////			} else {
////				v.setBackgroundResource(R.color.white);
////			}
////		}
////		if (showBackground) {
////			if (position % 2 != 0) {
////				v.setBackgroundResource(R.color.background);
////			} else {
////				v.setBackgroundResource(R.color.white);
////			}
////		}
//		return v;
//	}
//
//	protected abstract View getView(int position, View convertView, ViewGroup parent);

}
