package com.yulinoo.plat.life.views.adapter;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.ChatMessage;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.FaceConversionUtil;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.views.activity.UsrZoneActivity;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class ChatMsgViewAdapter extends YuLinooLoadAdapter<ChatMessage>
		implements OnClickListener {

	private LayoutInflater inflater;
	private Context mContext;
	private MyApplication myapp;
	private Account me;
	private AreaInfo nowArea;

	private PopupWindow popupWindow;

	public ChatMsgViewAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		this.mContext = context;
		myapp = ((MyApplication) mContext.getApplicationContext());
		me = AppContext.currentAccount();
		nowArea = AppContext.currentAreaInfo();
	}

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	@Override
	public int getItemViewType(int position) {
		final ChatMessage item = getItem(position);
		if (item.isMyMessage()) {
			return IMsgViewType.IMVT_TO_MSG;
		} else {
			return IMsgViewType.IMVT_COM_MSG;
		}

	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ChatMessage item = getItem(position);
		boolean isMyMessage = item.isMyMessage();
		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (isMyMessage) {
				convertView = inflater.inflate(
						R.layout.chatting_item_msg_text_right, parent, false);
			} else {
				convertView = inflater.inflate(
						R.layout.chatting_item_msg_text_left, parent, false);
			}
			viewHolder = new ViewHolder();
			// viewHolder.tvSendTime = (TextView) convertView
			// .findViewById(R.id.tv_sendtime);
			viewHolder.iv_userhead = (ImageView) convertView
					.findViewById(R.id.iv_userhead);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tv_username);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);
			viewHolder.tvUserAddr = (TextView) convertView
					.findViewById(R.id.tv_userarea);
			// viewHolder.tvTime = (TextView)
			// convertView.findViewById(R.id.tv_time);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// viewHolder.tvSendTime.setText(entity.getDate());

		viewHolder.tvContent.setText(null);
		SpannableString spannableString = FaceConversionUtil.getInstance()
				.getExpressionString(mContext, item.getContent());
		viewHolder.tvContent.append(spannableString);
		viewHolder.tvContent
				.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		viewHolder.tvContent.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				showCopyPopupWindow((TextView) arg0);
				return true;
			}
		});
		// viewHolder.tvTime.setText("");
		if (viewHolder.tvUserName != null) {
			viewHolder.tvUserName.setText(item.getAccName());
		}
		if (viewHolder.tvUserAddr != null) {
			viewHolder.tvUserAddr.setText(item.getFromAreaName());
			if (nowArea != null) {
				if (nowArea.getSid() != item.getFromAreaSid()) {
					viewHolder.tvUserAddr.setTextColor(mContext.getResources()
							.getColor(R.color.font_red));
				} else {
					viewHolder.tvUserAddr.setTextColor(mContext.getResources()
							.getColor(R.color.addr_color));
				}
			}
		}

		if (NullUtil.isStrNotNull(item.getUserHead())) {
			MeImageLoader.displayImage(item.getUserHead(),
					viewHolder.iv_userhead, myapp.getHeadIconOption());
		} else {
			int sex = item.getSex();
			if (Constant.SEX.SEX_MAN == sex) {
				viewHolder.iv_userhead
						.setImageResource(R.drawable.man_selected);
			} else if (Constant.SEX.SEX_WOMAN == sex) {
				viewHolder.iv_userhead
						.setImageResource(R.drawable.woman_selected);
			}
		}
		viewHolder.iv_userhead.setTag(item);
		viewHolder.iv_userhead.setOnClickListener(this);
		return convertView;
	}

	private class ViewHolder {
		// public TextView tvSendTime;
		public ImageView iv_userhead;
		public TextView tvUserName;
		public TextView tvContent;
		public TextView tvUserAddr;
		// public TextView tvTime;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_userhead: {
			ChatMessage item = (ChatMessage) v.getTag();
			if (me.getSid().longValue() == item.getAccSid()) {// 是自己,则点击无意义
				return;
			}
			Account account = new Account();
			account.setSid(item.getAccSid());
			account.setAccName(item.getAccName());
			AreaInfo ai = new AreaInfo();
			ai.setSid(item.getFromAreaSid());
			ai.setAreaName(item.getFromAreaName());
			account.setAreaInfo(ai);
			account.setSex(item.getSex());
			mContext.startActivity(new Intent(mContext, UsrZoneActivity.class)
					.putExtra(Constant.ActivityExtra.ACCOUNT, account));
			break;
		}
		}
	}

	@SuppressLint("NewApi")
	private void showCopyPopupWindow(final TextView v) {
		View view = inflater.inflate(R.layout.copy_popupwindow, null);
		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		popupWindow.showAsDropDown(v, 40, 5);

		view.findViewById(R.id.copy_popupwindw_tv).setOnClickListener(
				new OnClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onClick(View arg0) {
						popupWindow.dismiss();
						ClipboardManager cbm = (ClipboardManager) mContext
								.getSystemService(Context.CLIPBOARD_SERVICE);
						cbm.setPrimaryClip(ClipData.newPlainText(
								"newPlainTextLabel", v.getText().toString()
										.trim()));
					}
				});

	}

}
