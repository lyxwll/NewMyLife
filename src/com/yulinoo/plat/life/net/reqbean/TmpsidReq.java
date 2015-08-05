package com.yulinoo.plat.life.net.reqbean;

public class TmpsidReq {
	private Long citySid;
	private Long districtSid;
	private String phoneType;

	public Long getCitySid() {
		return citySid;
	}

	public void setCitySid(Long citySid) {
		this.citySid = citySid;
	}

	public Long getDistrictSid() {
		return districtSid;
	}

	public void setDistrictSid(Long districtSid) {
		this.districtSid = districtSid;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

}
