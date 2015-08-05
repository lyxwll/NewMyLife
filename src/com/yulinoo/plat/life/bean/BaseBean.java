package com.yulinoo.plat.life.bean;

import com.activeandroid.Model;

public class BaseBean extends Model{
private boolean needDownload = true;
	private boolean needAnimation = true;
	public boolean isNeedAnimation() {
		return needAnimation;
	}

	public void setNeedAnimation(boolean needAnimation) {
		this.needAnimation = needAnimation;
	}

	public boolean isNeedDownload() {
		return needDownload;
	}

	public void setNeedDownload(boolean needDownload) {
		this.needDownload = needDownload;
	}
}
