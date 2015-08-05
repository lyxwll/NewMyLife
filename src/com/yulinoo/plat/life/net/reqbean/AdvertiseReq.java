package com.yulinoo.plat.life.net.reqbean;

//取消关注小区
public class AdvertiseReq {
	private Integer position;
	private Long categorySid;
	private Long areaSid;
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public Long getCategorySid() {
		return categorySid;
	}
	public void setCategorySid(Long categorySid) {
		this.categorySid = categorySid;
	}
	public Long getAreaSid() {
		return areaSid;
	}
	public void setAreaSid(Long areaSid) {
		this.areaSid = areaSid;
	}
	

	

}
