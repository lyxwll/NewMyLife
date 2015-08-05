package com.yulinoo.plat.life.net.reqbean;

public class MessageCenterInfoReq {
	private String mevalue;
	private Integer type;
	private Integer pageNo;
	private Integer direction;
	private Long responseUsrSid;
	private Long beginTime;
	private Long endTime;
	private Long contactTime;//联系人最后更新时间
	
	public String getMevalue() {
		return mevalue;
	}
	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	public Long getResponseUsrSid() {
		return responseUsrSid;
	}
	public void setResponseUsrSid(Long responseUsrSid) {
		this.responseUsrSid = responseUsrSid;
	}
	public Long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public Long getContactTime() {
		return contactTime;
	}
	public void setContactTime(Long contactTime) {
		this.contactTime = contactTime;
	}

}
