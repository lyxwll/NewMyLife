package com.yulinoo.plat.life.bean;

import java.io.Serializable;


public class Advertise implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long alongMerchantSid;
	private Long alongGoodsSid;
	private String url;//图片的url
	private Long sid;//商品的主键
	public Long getAlongMerchantSid() {
		return alongMerchantSid;
	}
	public void setAlongMerchantSid(Long alongMerchantSid) {
		this.alongMerchantSid = alongMerchantSid;
	}
	
	public Long getAlongGoodsSid() {
		return alongGoodsSid;
	}
	public void setAlongGoodsSid(Long alongGoodsSid) {
		this.alongGoodsSid = alongGoodsSid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
	}
}
