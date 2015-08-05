package com.yulinoo.plat.life.net.resbean;

public class LoginResponse extends NormalResponse{
	
	private String mevalue;//登录成功之后的随机码，用以表示身份的认同
	private Boolean loginOther;//是否在其他地方登录过
	private Integer viewNumber;//商家浏览数
	private Integer fansNumber;//粉丝数
	private long accSid;//账号
	public String getMevalue() {
		return mevalue;
	}
	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}
	public Boolean getLoginOther() {
		return loginOther;
	}
	public void setLoginOther(Boolean loginOther) {
		this.loginOther = loginOther;
	}
	public Integer getViewNumber() {
		return viewNumber;
	}
	public void setViewNumber(Integer viewNumber) {
		this.viewNumber = viewNumber;
	}
	public Integer getFansNumber() {
		return fansNumber;
	}
	public void setFansNumber(Integer fansNumber) {
		this.fansNumber = fansNumber;
	}
	public long getAccSid() {
		return accSid;
	}
	public void setAccSid(long accSid) {
		this.accSid = accSid;
	}
	
	
	
//	private int accType;
//	

//	private String accName;//账号名称
//	private long alongCitySid;//所属城市
//	private long merchantSid;//商家主键
//	private boolean success;
//	private String msg;
//	private String headPicture;//头像
//	private String background;//背景图
//	private String signatureName;//签名
//	
//	private int attenNumber;//关注数
//	private int publishNumber;//发布数
//	private int sex;//性别
//	
//	private String mevalue;//登录成功之后的随机码，用以表示身份的认同
//	private Boolean loginOther;//是否在其他地方登录过
//	private Integer viewNumber;//商家浏览数
//	private Integer fansNumber;//粉丝数
//	
//	public boolean isSuccess() {
//		return success;
//	}
//
//	public void setSuccess(boolean success) {
//		this.success = success;
//	}
//
//	public String getMsg() {
//		return msg;
//	}
//
//	public void setMsg(String msg) {
//		this.msg = msg;
//	}
//
//	public long getAccSid() {
//		return accSid;
//	}
//
//	public void setAccSid(long accSid) {
//		this.accSid = accSid;
//	}
//
//	public int getAccType() {
//		return accType;
//	}
//
//	public void setAccType(int accType) {
//		this.accType = accType;
//	}
//
//	public String getMevalue() {
//		return mevalue;
//	}
//
//	public void setMevalue(String mevalue) {
//		this.mevalue = mevalue;
//	}
//
//	public long getMerchantSid() {
//		return merchantSid;
//	}
//
//	public void setMerchantSid(long merchantSid) {
//		this.merchantSid = merchantSid;
//	}
//
//	public String getHeadPicture() {
//		return headPicture;
//	}
//
//	public void setHeadPicture(String headPicture) {
//		this.headPicture = headPicture;
//	}
//
//	public String getBackground() {
//		return background;
//	}
//
//	public void setBackground(String background) {
//		this.background = background;
//	}
//
//	public String getSignatureName() {
//		return signatureName;
//	}
//
//	public void setSignatureName(String signatureName) {
//		this.signatureName = signatureName;
//	}
//
//	public String getAccName() {
//		return accName;
//	}
//
//	public void setAccName(String accName) {
//		this.accName = accName;
//	}
//
//	public long getAlongCitySid() {
//		return alongCitySid;
//	}
//
//	public void setAlongCitySid(long alongCitySid) {
//		this.alongCitySid = alongCitySid;
//	}
//
//	public int getAttenNumber() {
//		return attenNumber;
//	}
//
//	public void setAttenNumber(int attenNumber) {
//		this.attenNumber = attenNumber;
//	}
//
//	public int getPublishNumber() {
//		return publishNumber;
//	}
//
//	public void setPublishNumber(int publishNumber) {
//		this.publishNumber = publishNumber;
//	}
//
//	public int getSex() {
//		return sex;
//	}
//
//	public void setSex(int sex) {
//		this.sex = sex;
//	}
//
//	public Boolean getLoginOther() {
//		return loginOther;
//	}
//
//	public void setLoginOther(Boolean loginOther) {
//		this.loginOther = loginOther;
//	}
//
//	public void setViewNumber(Integer viewNumber) {
//		this.viewNumber = viewNumber;
//	}
//
//	public void setFansNumber(Integer fansNumber) {
//		this.fansNumber = fansNumber;
//	}

}
