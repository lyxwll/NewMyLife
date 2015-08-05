package com.yulinoo.plat.life.ui.widget.preview;

import android.view.MotionEvent;
import android.view.View;

public final class SimpleImageZoomListener implements View.OnTouchListener {

	private ImageZoomState mState;
	private final float SENSIBILITY = 0.8f;
	private float sX;
	private float sY;

	private float sX01;
	private float sY01;

	private float sDistance;
	
	protected SimpleImageZoomListener(){
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
         
		try {
		int action = event.getAction();
		int pointNum = event.getPointerCount();
		if (pointNum == 1) {//
			float mX = event.getX();// 
			float mY = event.getY();//
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				sX01 = mX;
				sY01 = mY;
				sX = mX;
				sY = mY;
				return false;// 
			case MotionEvent.ACTION_MOVE:
				float dX = (mX - sX) / v.getWidth();
				float dY = (mY - sY) / v.getHeight();
				mState.setmPanX(mState.getmPanX() - dX * SENSIBILITY);
				mState.setmPanY(mState.getmPanY() - dY * SENSIBILITY);
				mState.notifyObservers();
				sX = mX;
				sY = mY;
				break;
			case MotionEvent.ACTION_UP:
				if (event.getX() == sX01 && event.getY() == sY01) {
					return false;// return false 
				}
				break;
			}
		}

		if (pointNum == 2) {// 
			float mX0 = event.getX(event.getPointerId(0));
			float mY0 = event.getY(event.getPointerId(0));
			float mX1 = event.getX(event.getPointerId(1));
			float mY1 = event.getY(event.getPointerId(1));
			float distance = this.getDistance(mX0, mY0, mX1, mY1);
			switch (action) {
			case MotionEvent.ACTION_POINTER_2_DOWN:
			case MotionEvent.ACTION_POINTER_1_DOWN:
				sDistance = distance;
				break;
			case MotionEvent.ACTION_POINTER_1_UP:
				sX = mX1;
				sY = mY1;
				break;
			case MotionEvent.ACTION_POINTER_2_UP:
				sX = mX0;
				sY = mY0;
				break;
			case MotionEvent.ACTION_MOVE:
				mState.setmZoom(mState.getmZoom() * distance / sDistance);
				mState.notifyObservers();
				sDistance = distance;
				break;

			}

		}
		return true;
		} catch (Exception e) {
			// TODO: handle exception
		}// 
		
		return false;
	}

	private float getDistance(float mX0, float mY0, float mX1, float mY1) {
		double dX2 = Math.pow(mX0 - mX1, 2);// 
		double dY2 = Math.pow(mY0 - mY1, 2);//
		return (float) Math.pow(dX2 + dY2, 0.5);
	}

	public void setZoomState(ImageZoomState state) {
		mState = state;
	}
}
