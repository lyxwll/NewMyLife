package com.yulinoo.plat.life.net.resbean;

public class ShopOpenResponse {
	private boolean success;
	private String msg;
	private long merchantSid;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public long getMerchantSid() {
		return merchantSid;
	}
	public void setMerchantSid(long merchantSid) {
		this.merchantSid = merchantSid;
	}
	

}
