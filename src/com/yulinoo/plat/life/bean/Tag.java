package com.yulinoo.plat.life.bean;

import java.io.Serializable;

//分类下的标签
public class Tag implements Serializable{
	private static final long serialVersionUID = 1L;
	private long sid;
	private String tagName;
	private long alongCategorySid;
	public boolean checked;//该标签是否被用户选中
	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public long getAlongCategorySid() {
		return alongCategorySid;
	}
	public void setAlongCategorySid(long alongCategorySid) {
		this.alongCategorySid = alongCategorySid;
	}

}
