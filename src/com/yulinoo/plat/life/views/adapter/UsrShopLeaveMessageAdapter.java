package com.yulinoo.plat.life.views.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.melife.R;

public class UsrShopLeaveMessageAdapter extends YuLinooLoadAdapter<ForumNote>{
	private Context mContext;
	private LayoutInflater inflater;

	public UsrShopLeaveMessageAdapter(Context mContext) {
		this.mContext=mContext;
		this.inflater=LayoutInflater.from(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommonHolderView holder;
		final ForumNote item = getItem(position);
		if (convertView == null) {
			holder = new CommonHolderView();
			convertView = inflater.inflate(R.layout.item_merchant_list, parent,false);	
			MeUtil.initWeiboContent(mContext, holder, convertView, item);
			convertView.setTag(holder);
		} else {
			holder = (CommonHolderView) convertView.getTag();
		}
		MeUtil.setWeiboContent(mContext, holder, convertView, item,true);
		return convertView;
	}
	


}
