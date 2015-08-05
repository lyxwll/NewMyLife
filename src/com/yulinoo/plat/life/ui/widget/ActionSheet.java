package com.yulinoo.plat.life.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yulinoo.plat.melife.R;

public class ActionSheet {

	private ReturnResult result;
	private Context mContext;
	private PopupWindow mPop;

	public ActionSheet(Context mContext, ReturnResult result) {
		this.result = result;
		this.mContext = mContext;
	}

	public void dismiss() {
		try {
			if (mPop != null) {
				mPop.dismiss();
				mPop = null;
			}
		} catch (Exception e) {
		}
	}

	@SuppressLint("InlinedApi")
	public void putData(List<ActionSheetParameter> actionSheetParameters,View showView) {

		if (mPop != null && mPop.isShowing()) {
			mPop.dismiss();
			mPop = null;
			return;
		}

		if (actionSheetParameters == null || actionSheetParameters.size() == 0) {
			Toast.makeText(mContext, "未获取到类型", Toast.LENGTH_SHORT).show();
			return;
		}

		View convertView = LayoutInflater.from(mContext).inflate(R.layout./* wiget_option_alert_item */wiget_action_sheet_list, null);
		ListView listView = (ListView) convertView.findViewById(R.id.option_list);
		final ActionSheetAdapter optionAdapter = new ActionSheetAdapter(mContext, actionSheetParameters);
		listView.setAdapter(optionAdapter);

		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setDividerHeight(0);
		
		convertView.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (mPop != null) {
						mPop.dismiss();
						mPop = null;
					}
				} catch (Exception e) {
				}
			}
		});
		// cancel
		// optionAdapter.loadData(keyValueList);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (result != null) {
					ActionSheetParameter keyValue = optionAdapter.getItem(arg2);
					try {
						mPop.dismiss();
					} catch (Exception e) {
					}
					result.onResult(keyValue.getKey(), keyValue.getValue());
				}
			}
		});
		mPop = new PopupWindow(convertView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false);
		mPop.setFocusable(true);
		mPop.setAnimationStyle(R.style.AnimBottom); 
		mPop.showAtLocation(showView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}
	@SuppressLint("InlinedApi")
	public void putData(List<ActionSheetParameter> actionSheetParameters,View showView,int location) {
		
		if (mPop != null && mPop.isShowing()) {
			mPop.dismiss();
			mPop = null;
			return;
		}
		
		if (actionSheetParameters == null || actionSheetParameters.size() == 0) {
			Toast.makeText(mContext, "未获取到类型", Toast.LENGTH_SHORT).show();
			return;
		}
		
		View convertView = LayoutInflater.from(mContext).inflate(R.layout./* wiget_option_alert_item */wiget_action_sheet_list, null);
		ListView listView = (ListView) convertView.findViewById(R.id.option_list);
		final ActionSheetAdapter optionAdapter = new ActionSheetAdapter(mContext, actionSheetParameters);
		listView.setAdapter(optionAdapter);
		
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setDividerHeight(0);
		
		convertView.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					if (mPop != null) {
						mPop.dismiss();
						mPop = null;
					}
				} catch (Exception e) {
				}
			}
		});
		// cancel
		// optionAdapter.loadData(keyValueList);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (result != null) {
					ActionSheetParameter keyValue = optionAdapter.getItem(arg2);
					try {
						mPop.dismiss();
					} catch (Exception e) {
					}
					result.onResult(keyValue.getKey(), keyValue.getValue());
				}
			}
		});
		mPop = new PopupWindow(convertView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false);
		mPop.setFocusable(true);
		mPop.setAnimationStyle(R.style.AnimBottom); 
		mPop.showAtLocation(showView, location, 0, 0); // 设置layout在PopupWindow中显示的位置
		
	}

	public interface ReturnResult {
		public void onResult(String key, String value);
	}

	class ActionSheetAdapter extends BaseAdapter {

		private List<ActionSheetParameter> actionSheetParameters = new ArrayList<ActionSheetParameter>();
		private LayoutInflater inflater;

		public ActionSheetAdapter(Context context, List<ActionSheetParameter> actionSheetParameters) {
			inflater = LayoutInflater.from(context);
			this.actionSheetParameters = actionSheetParameters;
		}

		@Override
		public int getCount() {
			return actionSheetParameters.size();
		}

		@Override
		public ActionSheetParameter getItem(int position) {
			return actionSheetParameters.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holder;
			ActionSheetParameter item = getItem(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.wiget_action_sheet_item, null);
				holder = new HolderView();
				holder.option_item = (TextView) convertView.findViewById(R.id.option_item);
				convertView.setTag(holder);
			} else {
				holder = (HolderView) convertView.getTag();
			}
			holder.option_item.setText("  " + item.getValue());
			holder.option_item.setBackgroundResource(item.getDrawableId());
			return convertView;
		}

		class HolderView {
			public TextView option_item;
		}

	}

	public class ActionSheetParameter {
		private String key;
		private String value;
		private int drawableId;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public int getDrawableId() {
			return drawableId;
		}

		public void setDrawableId(int drawableId) {
			this.drawableId = drawableId;
		}

	}
}

