package com.yulinoo.plat.life.net.reqbean;

public class MerchantMessageCenterInfoReq {
	private String mevalue;
	private Long accSid;
	private Integer commentType;
	private Integer pageNo;
	private Integer direction;
	private Long responseUsrSid;
	private Long beginTime;
	private Long endTime;
	private Long contactTime;// 联系人最后更新时间

	public Long getAccSid() {
		return accSid;
	}

	public void setAccSid(Long accSid) {
		this.accSid = accSid;
	}

	public String getMevalue() {
		return mevalue;
	}

	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}

	public Integer getCommentType() {
		return commentType;
	}

	public void setCommentType(Integer commentType) {
		this.commentType = commentType;
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
