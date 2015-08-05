package com.yulinoo.plat.life.net.reqbean;

public class ConcernMerchantReq {
	private Long accSid;
	private Long merchantSid;
	private int subType;
	private Long areaSid;
	private String areaName;
	public Long getAccSid() {
		return accSid;
	}
	public void setAccSid(Long accSid) {
		this.accSid = accSid;
	}
	public Long getMerchantSid() {
		return merchantSid;
	}
	public void setMerchantSid(Long merchantSid) {
		this.merchantSid = merchantSid;
	}
	public int getSubType() {
		return subType;
	}
	public void setSubType(int subType) {
		this.subType = subType;
	}
	public Long getAreaSid() {
		return areaSid;
	}
	public void setAreaSid(Long areaSid) {
		this.areaSid = areaSid;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}


}
