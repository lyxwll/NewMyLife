package com.yulinoo.plat.life.ui.widget.preview;

import java.util.Observable;

public final class ImageZoomState extends Observable {

	private float mZoom = 1.0f;
	private float mPanX = 0.5f;
	private float mPanY = 0.5f;

	protected ImageZoomState(){
		
	}
	
	public float getmZoom() {
		return mZoom;
	}

	public void setmZoom(float mZoom) {

		if (this.mZoom != mZoom) {
			this.mZoom = mZoom < 1.0f ? 1.0f : mZoom;
			if (this.mZoom == 1.0f) {
				this.mPanX = 0.5f;
				this.mPanY = 0.5f;
			}
			
			setChanged();
		}

	}

	public float getmPanX() {
		return mPanX;
	}

	public void setmPanX(float mPanX) {

		if (mZoom == 1.0f) {
			return;
		}

		if (this.mPanX != mPanX) {
			this.mPanX = mPanX;
			this.setChanged();
		}
	}

	public float getmPanY() {
		return mPanY;
	}

	public void setmPanY(float mPanY) {

		if (mZoom == 1.0f) {
			return;
		}

		if (this.mPanY != mPanY) {
			this.mPanY = mPanY;
			this.setChanged();
		}
	}

	public float getZoomX(float aspectQuotient) {
		return Math.min(mZoom, mZoom * aspectQuotient);
	}

	public float getZoomY(float aspectQuotient) {
		return Math.min(mZoom, mZoom / aspectQuotient);
	}

}
