
package com.yulinoo.plat.life.net.reqbean;

public class GetMessageBoxReq {
    private String mevalue;
    private Long areaSid;
    private Long beginTime;
    //private Long endTime;
    
	public String getMevalue() {
		return mevalue;
	}
	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}
	public Long getAreaSid() {
		return areaSid;
	}
	public void setAreaSid(Long areaSid) {
		this.areaSid = areaSid;
	}
	public Long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}
//	public Long getEndTime() {
//		return endTime;
//	}
//	public void setEndTime(Long endTime) {
//		this.endTime = endTime;
//	}
    
}
