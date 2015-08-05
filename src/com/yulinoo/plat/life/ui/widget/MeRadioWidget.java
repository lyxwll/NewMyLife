package com.yulinoo.plat.life.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yulinoo.plat.life.ui.widget.bean.MeRadio;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
//自定义radio选择视图
public class MeRadioWidget extends LinearLayout implements OnClickListener{
	private Context mContext;
	private List<MeRadio> lstMeRadios;
	private List<LinearLayout> listViews;
	private boolean enabled;
	private MeRadio nowMeRadio;
	public MeRadioWidget(Context context) {
		super(context);
		mContext = context;
	}
	public MeRadioWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	//参数二表示的是每一人子控件的宽度和高度
	@SuppressLint("NewApi")
	public void load(List<MeRadio> lst,int[] viewSize)
	{
		this.removeAllViews();
		int size=lst.size();
		this.listViews=new ArrayList<LinearLayout>(size);
		this.lstMeRadios=lst;
		for(int kk=0;kk<size;kk++)
		{
			MeRadio mr=lst.get(kk);
			ImageView iv=null;
			TextView tv=null;
			LinearLayout fl=new LinearLayout(mContext);
			LayoutParams flp=new LayoutParams(0,LayoutParams.MATCH_PARENT,1);
			fl.setLayoutParams(flp);
			fl.setGravity(Gravity.CENTER);
			if(mr.getDirect()==MeRadio.DIRECT_TOP)
			{
				fl.setOrientation(LinearLayout.VERTICAL);
			}else if(mr.getDirect()==MeRadio.DIRECT_LEFT)
			{
				fl.setOrientation(LinearLayout.HORIZONTAL);
			}
			
			if(mr.getSelectedPicture()>0)
			{//说明有图片
				iv=new ImageView(mContext);
				LayoutParams imglp=new LayoutParams(viewSize[0],viewSize[1]);
				iv.setLayoutParams(imglp);
				StateListDrawable drawable =MeUtil.createImageSelectStateListDrawable(getResources(),mr.getSelectedPicture(),mr.getUnSelectedPicture(),viewSize);
				iv.setImageDrawable(drawable);
				fl.addView(iv);
			}
			if(NullUtil.isStrNotNull(mr.getName()))
			{
				tv=new TextView(mContext);
				LayoutParams lp=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tv.setLayoutParams(lp);
				tv.setText(mr.getName());
				if(mr.getSelectedTextColor()>0)
				{
					ColorStateList clist=MeUtil.createColorSelectStateList(getResources().getColor(mr.getSelectedTextColor()), getResources().getColor(mr.getUnSelectedTextColor()));
					tv.setTextColor(clist);
				}
				tv.setTextSize(mr.getTextSize());
				fl.addView(tv);
			}
			fl.setTag(mr);
			setViewSelected(fl);
			fl.setOnClickListener(this);
			listViews.add(fl);
			this.addView(fl);			
		}
		if(onMeRadioSelectedListener!=null)
		{
			onMeRadioSelectedListener.OnMeRadioSelected(nowMeRadio);
		}
		//getLayoutParams().height=llheight;
		//setPadding(3, 5, 3, 5);
		setGravity(Gravity.CENTER);
	}
	
	private void setViewSelected(LinearLayout ll)
	{
		MeRadio mr=(MeRadio)ll.getTag();
		int childCount=ll.getChildCount();
		for(int kk=0;kk<childCount;kk++)
		{
			View view=ll.getChildAt(kk);
			if(mr.isSelected())
			{
				nowMeRadio=mr;
				view.setSelected(true);
			}else
			{
				view.setSelected(false);
			}
		}
		if(mr.getSelectedBgColor()>0)
		{
			ll.setBackgroundColor(0);
			if(mr.isSelected())
			{
				ll.setBackgroundColor(getResources().getColor(mr.getSelectedBgColor()));
			}else
			{
				ll.setBackgroundColor(getResources().getColor(mr.getUnSelectedBgColor()));
			}
		}
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled=enabled;
	}
	@Override
	public void onClick(View v) {
		if(!enabled)
		{
			return;
		}
		
		MeRadio meRadio=(MeRadio)v.getTag();
		if(nowMeRadio.getIndex()==meRadio.getIndex())
		{
			return;
		}
		int size=lstMeRadios.size();
		for(int kk=0;kk<size;kk++)
		{
			MeRadio mradio=lstMeRadios.get(kk);
			if(mradio.getIndex()==meRadio.getIndex())
			{
				mradio.setSelected(true);
			}else
			{
				mradio.setSelected(false);
			}
			LinearLayout ll=listViews.get(kk);
			//ll.setTag(mradio);
			setViewSelected(ll);
		}

		if(onMeRadioSelectedListener!=null)
		{
			onMeRadioSelectedListener.OnMeRadioSelected(meRadio);
		}
	}
	public MeRadio getNowRadio()
	{
		return nowMeRadio;
	}

	public interface OnMeRadioSelectedListener
	{
		public void OnMeRadioSelected(MeRadio meRadio);
	}
	private OnMeRadioSelectedListener onMeRadioSelectedListener;
	public OnMeRadioSelectedListener getOnMeRadioSelectedListener() {
		return onMeRadioSelectedListener;
	}
	public void setOnMeRadioSelectedListener(
			OnMeRadioSelectedListener onMeRadioSelectedListener) {
		this.onMeRadioSelectedListener = onMeRadioSelectedListener;
	}

}
