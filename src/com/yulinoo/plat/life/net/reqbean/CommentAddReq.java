package com.yulinoo.plat.life.net.reqbean;

public class CommentAddReq {
	private String  mevalue;
	private Long merchantSid;
	private Long goodsSid;
	private Integer type;
	private String desc;
	private Long alongAreaSid;
	public String getMevalue() {
		return mevalue;
	}
	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}
	public Long getMerchantSid() {
		return merchantSid;
	}
	public void setMerchantSid(Long merchantSid) {
		this.merchantSid = merchantSid;
	}
	public Long getGoodsSid() {
		return goodsSid;
	}
	public void setGoodsSid(Long goodsSid) {
		this.goodsSid = goodsSid;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Long getAlongAreaSid() {
		return alongAreaSid;
	}
	public void setAlongAreaSid(Long alongAreaSid) {
		this.alongAreaSid = alongAreaSid;
	}
	
}
