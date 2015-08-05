package com.yulinoo.plat.life.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Tag;
import com.yulinoo.plat.melife.R;

public class CategoryTagAdapter extends YuLinooLoadAdapter<Tag> {
	private LayoutInflater inflater;
	private OnCategoryTagSelectedListener onCheckedListener;
	private static final int max_selected=2;//最多只能选择两个标签
	private Context context;

	public void setOnCategoryTagSelected(OnCategoryTagSelectedListener onCheckedListener) {
		this.onCheckedListener = onCheckedListener;
	}

	public interface OnCategoryTagSelectedListener {
		public boolean onTagSelected(Tag productInfos,boolean checked);
	}

	public CategoryTagAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		this.context=context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		final Tag item = getItem(position);

		if (convertView == null) {
			holder = new HolderView();
			convertView = inflater.inflate(R.layout.category_tag_item, parent,false);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			convertView.setTag(holder);

		} else {
			holder = (HolderView) convertView.getTag();
		}
		holder.name.setText(item.getTagName());
		if (item.checked) {
			holder.checkbox.setChecked(true);
		} else {
			holder.checkbox.setChecked(false);
		}

		holder.checkbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onCheckedListener!=null)
				{
					CheckBox box=((CheckBox) v);
					boolean isChecked=box.isChecked();
					if(onCheckedListener.onTagSelected(item,isChecked))
					{
						if(isChecked)
						{//选择成功
							item.checked=true;
						}else
						{//取消选择成功
							item.checked=false;
						}
					}else
					{
						if(isChecked)
						{//选择失败
							item.checked=false;
							box.setChecked(false);
						}else
						{//取消选择失败
							item.checked=true;
							box.setChecked(true);
						}
					}
				}
				
//				CheckBox box=((CheckBox) v);
//				boolean isChecked=box.isChecked();
//				if(isChecked)
//				{
//					item.checked=true;
//				}else
//				{
//					item.checked=false;
//				}
//				if (onCheckedListener != null) {
//					List<Tag> productInfos = new ArrayList<Tag>();
//					for (Tag productInfo : getList()) {
//						if (productInfo.checked) {
//							productInfos.add(productInfo);
//						}
//					}
//					if(productInfos.size()>max_selected)
//					{
//						Toast toast = Toast.makeText(context, "最多只能选择"+max_selected+"项", Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
//						box.setChecked(false);
//						return;
//					}
//					onCheckedListener.onTagSelected(productInfos);
//				}
			}
		});

		return convertView;
	}

	private class HolderView {
		public TextView name;
		public CheckBox checkbox;
	}
}
