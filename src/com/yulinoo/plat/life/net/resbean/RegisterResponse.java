package com.yulinoo.plat.life.net.resbean;

public class RegisterResponse {
	private boolean success;
	private String msg;
	private String mevalue;
	private int accType;
	private long accSid;

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

	public String getMevalue() {
		return mevalue;
	}

	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}

	public int getAccType() {
		return accType;
	}

	public void setAccType(int accType) {
		this.accType = accType;
	}

	public long getAccSid() {
		return accSid;
	}

	public void setAccSid(long accSid) {
		this.accSid = accSid;
	}

}
