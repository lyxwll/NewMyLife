package com.yulinoo.plat.life.net.reqbean;

public class RegisterReq {
	private String account;
	private String password;
	private String accName;//呢称
	private Long citySid;
	private Long districtSid;
	private Long tempSid;
	private String phoneType;
	private Integer sex;//性别
	private String mevalue;//随机码，在修改用户信息时有用
	private String signatureName;//签名
	private String address;//地址
	private String valCode;//验证码
	private String inviteCode;//邀请码
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
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
	public Long getTempSid() {
		return tempSid;
	}
	public void setTempSid(Long tempSid) {
		this.tempSid = tempSid;
	}
	public String getPhoneType() {
		return phoneType;
	}
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getMevalue() {
		return mevalue;
	}
	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}
	public String getSignatureName() {
		return signatureName;
	}
	public void setSignatureName(String signatureName) {
		this.signatureName = signatureName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getValCode() {
		return valCode;
	}
	public void setValCode(String valCode) {
		this.valCode = valCode;
	}
	public String getInviteCode() {
		return inviteCode;
	}
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	

}
