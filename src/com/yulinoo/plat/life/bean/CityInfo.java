package com.yulinoo.plat.life.bean;

import java.io.Serializable;

public class CityInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private long sid;
	private String cityName;
	private int cityCode;
	private String cityDomain;
	private String customerPhoto;//客服电话
	private String firstpinyin;
	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public int getCityCode() {
		return cityCode;
	}
	public void setCityCode(int cityCode) {
		this.cityCode = cityCode;
	}
	public String getCityDomain() {
		return cityDomain;
	}
	public void setCityDomain(String cityDomain) {
		this.cityDomain = cityDomain;
	}
	public String getCustomerPhoto() {
		return customerPhoto;
	}
	public void setCustomerPhoto(String customerPhoto) {
		this.customerPhoto = customerPhoto;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getFirstpinyin() {
		return firstpinyin;
	}
	public void setFirstpinyin(String firstpinyin) {
		this.firstpinyin = firstpinyin;
	}

}
