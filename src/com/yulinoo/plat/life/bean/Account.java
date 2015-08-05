package com.yulinoo.plat.life.bean;

import java.io.Serializable;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.yulinoo.plat.life.utils.Constant;

//账号表
@Table(name = "Account")
public class Account extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "sid")
	private Long sid;// 账号主键
	@Column(name = "isTemp")
	private Boolean isTemp;// 是否是临时账号
	@Column(name = "tempSid")
	private Long tempSid;// 用户注册时的临时账号
	@Column(name = "lastAccTime")
	private Long lastAccTime;// 账号上次访问应用时间
	@Column(name = "account")
	private String account;// 登录账号，一般为手机号
	@Column(name = "accPwd")
	private String accPwd;// 登录账号密码
	@Column(name = "accName")
	private String accName;// 登录账号名称，即一般意义上的昵称
	@Column(name = "lastAccArea")
	private Long lastAccArea;// 上次最后访问的小区主键
	@Column(name = "accType")
	private Integer accType = Constant.ACCTYPE.ACCTYPE_NORMAL_USR;// 账号类型
	@Column(name = "headPicture")
	private String headPicture;// 头像
	@Column(name = "background")
	private String background;// 背景图
	@Column(name = "signatureName")
	private String signatureName;// 签名
	@Column(name = "mevalue")
	private String mevalue = null;// 随机码，指登录之后获得的，类似于凭证
	@Column(name = "isUsrLogin")
	private Boolean isUsrLogin;// 当前用户是否登录
	@Column(name = "sex")
	private Integer sex;
	@Column(name = "viewNumber")
	private int viewNumber;// 商家浏览数
	@Column(name = "fansNumber")
	private int fansNumber;// 粉丝数
	@Column(name = "address")
	private String address;// 地址

	private AreaInfo areaInfo;// 用户所在小区，是一个临时的概念，即当前用户所挂载的小区

	// @Column(name="rememberPwd")
	// private boolean rememberPwd;//是否记住密码
	// @Column(name="autoLogin")
	// private boolean autoLogin;//是否自动登录
	// @Column(name="merchantSid")
	// private long merchantSid;//商家SID,该账号有可能是商家，一个商家有可能有多个账号

	// @Column(name="attenNumber")
	// private int attenNumber;//关注数
	// @Column(name="publishNumber")
	// private int publishNumber;//发布数

	// @Column(name = "mobilePhone")
	// private String mobilePhone;
	// @Column(name = "merchantTelphone")
	// private String merchantTelphone;

	//
	// private long areaSid;
	// private String areaName;

	public Long getLastAccTime() {
		return lastAccTime;
	}

	public Boolean getIsTemp() {
		return isTemp;
	}

	public void setIsTemp(Boolean isTemp) {
		this.isTemp = isTemp;
	}

	public void setLastAccTime(Long lastAccTime) {
		this.lastAccTime = lastAccTime;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccPwd() {
		return accPwd;
	}

	public void setAccPwd(String accPwd) {
		this.accPwd = accPwd;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public Long getLastAccArea() {
		return lastAccArea;
	}

	public void setLastAccArea(Long lastAccArea) {
		this.lastAccArea = lastAccArea;
	}

	public Integer getAccType() {
		return accType;
	}

	public void setAccType(Integer accType) {
		this.accType = accType;
	}

	public String getHeadPicture() {
		return headPicture;
	}

	public void setHeadPicture(String headPicture) {
		this.headPicture = headPicture;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getSignatureName() {
		return signatureName;
	}

	public void setSignatureName(String signatureName) {
		this.signatureName = signatureName;
	}

	public String getMevalue() {
		return mevalue;
	}

	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}

	public Boolean getIsUsrLogin() {
		return isUsrLogin;
	}

	public void setIsUsrLogin(Boolean isUsrLogin) {
		this.isUsrLogin = isUsrLogin;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public AreaInfo getAreaInfo() {
		return areaInfo;
	}

	public void setAreaInfo(AreaInfo areaInfo) {
		this.areaInfo = areaInfo;
	}

	public int getViewNumber() {
		return viewNumber;
	}

	public void setViewNumber(int viewNumber) {
		this.viewNumber = viewNumber;
	}

	public int getFansNumber() {
		return fansNumber;
	}

	public void setFansNumber(int fansNumber) {
		this.fansNumber = fansNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getTempSid() {
		return tempSid;
	}

	public void setTempSid(Long tempSid) {
		this.tempSid = tempSid;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

}
