package com.yulinoo.plat.life.net.reqbean;

public class GetFansReq {
	private Long accSid;
	private Integer subType;
	private Integer pageNo;
	public Long getAccSid() {
		return accSid;
	}
	public void setAccSid(Long accSid) {
		this.accSid = accSid;
	}
	public Integer getSubType() {
		return subType;
	}
	public void setSubType(Integer subType) {
		this.subType = subType;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

}
