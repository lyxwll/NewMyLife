package com.yulinoo.plat.life.ui.widget;

import com.yulinoo.plat.life.utils.DensityUtil;
import com.yulinoo.plat.melife.R;

import config.AppContext;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class MulinListView extends ListView{

	//ScrollView的子View， 也是ScrollView的唯一一个子View
	private View contentView; 
	private Context mContext;
	//移动因子, 是一个百分比, 比如手指移动了100px, 那么View就只移动50px
	//目的是达到一个延迟的效果
	private static final float MOVE_FACTOR = 1f;

	public MulinListView(Context context) {
		super(context);
		init(context);
	}

	public MulinListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	private void init(Context context)
	{	this.mContext=context;
	this.setCacheColorHint(Color.TRANSPARENT);
	this.setDividerHeight(0);
	screenHeight=AppContext.getScreenHeight(context);
	titleHeight=(int) context.getResources().getDimension(R.dimen.header_height);
	minHeight=DensityUtil.dip2px(mContext, 130);
	}

	private int titleHeight=0;
	private int minHeight=0;
	private int screenHeight=0;
	private int statusHeight=0;
	public void setStatusHeight(int sh)
	{
		this.statusHeight=sh;
	}

	private int perHeight=20;
	int fh=0;
	private boolean isRefresh=false;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
			{
				fh=1;
				int deltaY=(Integer)msg.obj;
				if(deltaY<0)
				{
					fh=-1;
				}
				final MarginLayoutParams lp = (MarginLayoutParams) MulinListView.this.getLayoutParams();
				int absDeltaY=Math.abs(deltaY);
				int count=0;
				int kk=absDeltaY%dh;
				if(kk==0)
				{
					count=absDeltaY/dh;
				}else
				{
					count=absDeltaY/dh+1;
				}
				isRefresh=true;
				int mm=1;
				for(;mm<=count;mm++)
				{
					final int marg=dh*fh;
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							int margTop=lp.topMargin+marg;
							int syHeight=(screenHeight-statusHeight-titleHeight-margTop);
							if(syHeight<=minHeight)
							{//到达最小高度
								return;
							}else
							{
								if(margTop <= (screenHeight-statusHeight-titleHeight-lp.height))
								{//到达最大高度
									return;
								}else
								{
									lp.topMargin=margTop;
									MulinListView.this.setLayoutParams(lp);
								}
							}
						}
					}, mm);
				}
				Message nmsg=new Message();
				nmsg.what=2;
				handler.sendMessageDelayed(nmsg, mm);
				break;
			}
			case 2:
			{
				//				int deltaY=(Integer)msg.obj;
				//				MarginLayoutParams lp = (MarginLayoutParams) MulinListView.this.getLayoutParams();
				//				lp.topMargin=lp.topMargin+deltaY;
				//				MulinListView.this.setLayoutParams(lp);
				isRefresh=false;
				break;
			}
			default:
				break;
			}

		}
	};
	private int dh=1;
	private float startY;
	int lastDeltaY=0;//用来防止抖动的
	/**
	 * 在触摸事件中, 处理上拉和下拉的逻辑
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			//记录按下时的Y值
			startY = ev.getY();
			break;
		case MotionEvent.ACTION_UP:	
			break;
		case MotionEvent.ACTION_MOVE:
			if (!isRefresh) {
				float nowY = ev.getY();
				int deltaY = (int)((nowY - startY)*MOVE_FACTOR);
				Message nm=new Message();
				nm.what=1;
//				if(deltaY<0)
//				{
//					if (isBottom())
//					{
//						break;
//					}
//				}
				nm.obj=deltaY;
				handler.sendMessage(nm);
				//startY=nowY;

				//			float nowY = ev.getY();
				//			Log.i("move", "============="+nowY+","+startY);
				//			int deltaY = (int) (nowY - startY);
				//			if(Math.abs(deltaY)>lastDeltaY)
				//			{
				//				Message nm=new Message();
				//				nm.what=1;
				//				int tmp=(int)((Math.abs(deltaY)-lastDeltaY)*MOVE_FACTOR);
				//				if(deltaY<0)
				//				{
				//					if (isBottom())
				//					{
				//						break;
				//					}
				//					tmp*=-1;
				//				}
				//				nm.obj=tmp;
				//				handler.sendMessage(nm);
				//				lastDeltaY=Math.abs(deltaY);
				//			}

			}
			break;
		default:
			break;
		}
		//return true;
		return super.dispatchTouchEvent(ev);
	}


	/**
	 * 判断是否滚动到底部
	 */
	private boolean isBottom() {
		MarginLayoutParams lp = (MarginLayoutParams)this.getLayoutParams();
		int margTop=lp.topMargin;
		return  margTop <= (screenHeight-statusHeight-titleHeight-getHeight());
	}

}
