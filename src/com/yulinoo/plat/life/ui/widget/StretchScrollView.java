package com.yulinoo.plat.life.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

//有拉升效果的滚动条
public class StretchScrollView extends ScrollView {
    private static final int MSG_REST_POSITION = 0x01;

    /** The max scroll height. */
    private static final int MAX_SCROLL_HEIGHT = 800;
    /** Damping, the smaller the greater the resistance */
    private static final float SCROLL_RATIO = 0.4f;

    private View mChildRootView;

    private float mTouchY;
    private boolean mTouchStop = false;

    private int mScrollY = 0;
    private int mScrollDy = 0;
    private boolean isDownBottom=false;//是否已经到达底部
    private boolean isTop=false;//是否已经到达顶部

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (MSG_REST_POSITION == msg.what) {
                if (mScrollY != 0 && mTouchStop) {
                    mScrollY -= mScrollDy;

                    if ((mScrollDy < 0 && mScrollY > 0) || (mScrollDy > 0 && mScrollY < 0)) {
                        mScrollY = 0;
                    }

                    mChildRootView.scrollTo(0, mScrollY);
                    // continue scroll after 20ms
                    sendEmptyMessageDelayed(MSG_REST_POSITION, 20);
                }
            }
        }
    };

    public StretchScrollView(Context context) {
        super(context);

        init();
    }

    public StretchScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public StretchScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }
    @SuppressLint("NewApi")
    private void init() {
        // set scroll mode
        setOverScrollMode(OVER_SCROLL_NEVER);
    }
    /*** 
     * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate 
     * 方法，也应该调用父类的方法，使该方法得以执行. 
     */  
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            // when finished inflating from layout xml, get the first child view
            mChildRootView = getChildAt(0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchY = ev.getY();
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (null != mChildRootView) {
            doTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }
    private float xDistance, yDistance, xLast, yLast;
    private void doTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
        case MotionEvent.ACTION_DOWN:
        	xDistance = yDistance = 0f;
            xLast = ev.getX();
            yLast = ev.getY();
        	break;
            case MotionEvent.ACTION_UP:
                mScrollY = mChildRootView.getScrollY();
                if (mScrollY != 0) {
                    mTouchStop = true;
                    mScrollDy = (int) (mScrollY / 10.0f);
                    mHandler.sendEmptyMessage(MSG_REST_POSITION);
                }
                break;

            case MotionEvent.ACTION_MOVE:
            	final float curX = ev.getX();
                final float curY = ev.getY();
                
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                
                if(xDistance > yDistance){//横向滚动不算数
                	return ;
                }  
                float nowY = ev.getY();
                int deltaY = (int) (mTouchY - nowY);
                mTouchY = nowY;
                if (isNeedMove()) {// 当滚动到最上或者最下时就不会再滚动，这时移动布局  
                    int offset = mChildRootView.getScrollY();
                    if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {
                        mChildRootView.scrollBy(0, (int) (deltaY * SCROLL_RATIO));
                        mTouchStop = false;
                    }
                }
                break;

            default:
                break;
        }
    }

    private boolean isNeedMove() {
        int viewHeight = mChildRootView.getMeasuredHeight();
        int scrollHeight = getHeight();
        int offset = viewHeight - scrollHeight;
        int scrollY = getScrollY();
        
        if(scrollY==0)
        {
        	if(!isTop)
        	{
        		isTop=true;
        		if(onOverScrollListener!=null)
        		{
        			onOverScrollListener.onScrollHeader();
        		}
        	}
        }else
        {
        	isTop=false;
        }
        
        if(scrollY==offset)
        {
        	if(!isDownBottom)
        	{
        		isDownBottom=true;
        		if(onOverScrollListener!=null)
        		{
        			onOverScrollListener.onScrollFooter();
        		}
        	}
        }else
        {
        	isDownBottom=false;
        }
     // 0是顶部，后面那个是底部 
        return scrollY == 0 || scrollY == offset;
    }
    

    
    private OnOverScrollListener onOverScrollListener;
	public OnOverScrollListener getOnOverScrollListener() {
		return onOverScrollListener;
	}

	public void setOnOverScrollListener(OnOverScrollListener onOverScrollListener) {
		this.onOverScrollListener = onOverScrollListener;
	}

	//拉到了最下面或者上面的回调接口
    public interface OnOverScrollListener{
    	public void onScrollFooter();
    	public void onScrollHeader();
    }
    
}