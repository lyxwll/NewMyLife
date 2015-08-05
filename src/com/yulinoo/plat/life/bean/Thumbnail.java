package com.yulinoo.plat.life.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Thumbnail")
public class Thumbnail extends Model {

	@Column(name = "compressPath")
	private String compressPath;

	@Column(name = "rawPath")
	private String rawPath;

	@Column(name = "type")
	private int type;

	public String getCompressPath() {
		return compressPath;
	}

	public void setCompressPath(String compressPath) {
		this.compressPath = compressPath;
	}

	public String getRawPath() {
		return rawPath;
	}

	public void setRawPath(String rawPath) {
		this.rawPath = rawPath;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
