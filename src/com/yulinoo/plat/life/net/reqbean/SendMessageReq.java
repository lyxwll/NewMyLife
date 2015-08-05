
package com.yulinoo.plat.life.net.reqbean;

public class SendMessageReq {
    private String mevalue;
    private Long accSid;
    private Long goodsSid;
    private Integer type;
    private Long alongAreaSid;
    private String desc;
    
	public String getMevalue() {
		return mevalue;
	}
	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}
	public Long getAccSid() {
		return accSid;
	}
	public void setAccSid(Long accSid) {
		this.accSid = accSid;
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
	public Long getAlongAreaSid() {
		return alongAreaSid;
	}
	public void setAlongAreaSid(Long alongAreaSid) {
		this.alongAreaSid = alongAreaSid;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
    
}
