package com.yulinoo.plat.life.bean;

import java.io.Serializable;

//商品图片信息
public class GoodsPic extends BaseBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private long sid;
	private String savePatha;//保存路径
	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
	}
	public String getSavePatha() {
		return savePatha;
	}
	public void setSavePatha(String savePatha) {
		this.savePatha = savePatha;
	}
	
	
}
