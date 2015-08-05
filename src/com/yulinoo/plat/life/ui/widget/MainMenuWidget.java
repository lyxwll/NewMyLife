package com.yulinoo.plat.life.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.melife.R;

//主菜单btn封装
public class MainMenuWidget extends RelativeLayout {
	private Context mContext;
	public MainMenuWidget(Context context) {
		super(context);
		mContext = context;
	}
	private View menu_bottom;
	private TextView menu_name;
	private ImageView new_message;
	private int index;
	public MainMenuWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		final View convertView = (View) LayoutInflater.from(context).inflate(R.layout.main_menu_widget, null);
		menu_bottom=convertView.findViewById(R.id.menu_bottom);
		menu_name=(TextView)convertView.findViewById(R.id.menu_name_);
		//menu_name.setTextSize(SizeUtil.title_menu_text_size(context));
		new_message=(ImageView)convertView.findViewById(R.id.new_message);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onMainMenuSelectedListener!=null)
				{
					onMainMenuSelectedListener.onMenuSelected(index);
				}
			}
		});
//		LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(0, SizeUtil.menu_height(context), 1);
//		this.setLayoutParams(llp);
		this.addView(convertView);
	}
	
	//设置本菜单选中状态
	public void setMenuState(boolean isSelected)
	{
		if(isSelected)
		{
			//menu_name.setSelected(true);
			menu_name.setTextColor(getResources().getColor(R.color.me_main_color));
			menu_bottom.setBackgroundColor(getResources().getColor(R.color.me_main_color));
		}else
		{
			//menu_name.setSelected(false);
			menu_name.setTextColor(getResources().getColor(R.color.black));
			menu_bottom.setBackgroundColor(getResources().getColor(R.color.menu_color));
		}
	}
	public void setMenuName(String name)
	{
		menu_name.setText(name);
	}
	
	public void showNewMessage()
	{
		new_message.setVisibility(View.VISIBLE);
	}
	public void hideNewMessage()
	{
		new_message.setVisibility(View.GONE);
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}



	public interface OnMainMenuSelectedListener
	{
		public void onMenuSelected(int index);
	}
	
	private OnMainMenuSelectedListener onMainMenuSelectedListener;
	public OnMainMenuSelectedListener getOnMainMenuSelectedListener() {
		return onMainMenuSelectedListener;
	}

	public void setOnMainMenuSelectedListener(
			OnMainMenuSelectedListener onMainMenuSelectedListener) {
		this.onMainMenuSelectedListener = onMainMenuSelectedListener;
	}
	
	
}
