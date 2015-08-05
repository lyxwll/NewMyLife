package com.yulinoo.plat.life.net.resbean;

import java.io.Serializable;


public class VersionResponse extends NormalResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String appname;
	private String apkname;
	private String verName;
	private String verSize;
	private Integer verCode;
	private String desc;//更新说明
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getApkname() {
		return apkname;
	}
	public void setApkname(String apkname) {
		this.apkname = apkname;
	}
	public String getVerName() {
		return verName;
	}
	public void setVerName(String verName) {
		this.verName = verName;
	}
	public Integer getVerCode() {
		return verCode;
	}
	public void setVerCode(Integer verCode) {
		this.verCode = verCode;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getVerSize() {
		return verSize;
	}
	public void setVerSize(String verSize) {
		this.verSize = verSize;
	}
	
	

	
}
