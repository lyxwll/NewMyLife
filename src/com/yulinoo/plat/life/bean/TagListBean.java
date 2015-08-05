package com.yulinoo.plat.life.bean;

import java.io.Serializable;
import java.util.List;

public class TagListBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Tag> tags;
	public List<Tag> getTags() {
		return tags;
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}


}
