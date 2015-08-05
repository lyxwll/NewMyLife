package com.yulinoo.plat.life.bean;

import java.io.Serializable;
import java.util.List;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Merchant")//商家表
public class Merchant extends BaseBean implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final int ACCTYPE_MERCHANT=1;
	public static final int ACCTYPE_NORMAL_USR=2;
	@Column(name = "sid")
	private long sid;
	@Column(name = "type")
	private int type;//账号类型，是商家还是普通用户
	@Column(name = "merchantName")
	private String merchantName;
	@Column(name = "merchantAddr")
	private String merchantAddr;
	@Column(name = "merchantDesc")
	private String merchantDesc;
	@Column(name = "logoPic")
	private String logoPic;
	@Column(name = "backPic")
	private String backPic;
	@Column(name = "longItude")
	private double longItude;
	@Column(name = "latItude")
	private double latItude;
	@Column(name = "districtName")
	private String districtName;
	@Column(name = "mobilePhone")
	private String mobilePhone;
	@Column(name = "merchantTelphone")
	private String merchantTelphone;
	@Column(name = "alongAreaSid")
	private long alongAreaSid;
	@Column(name = "alongDistrictSid")
	private long alongDistrictSid;
	@Column(name = "alongCitySid")
	private long alongCitySid;
	@Column(name = "scope")
	private String scope;
	@Column(name = "signatureName")
	private String signatureName;//商家的店铺签名
	@Column(name = "merchantTagNameArray")
	private String merchantTagNameArray;//商家的标签名称
	@Column(name = "merchantTagSidArray")
	private String merchantTagSidArray;//商家的标签ID
	@Column(name = "areaName")
	private String areaName;//小区名称
	@Column(name = "categorySid")
	private long categorySid;////商家所属分类
	@Column(name = "lastCont")
	private String lastCont;//最后一条微博内容
	@Column(name = "viewNumber")
	private int viewNumber;//商家浏览次数
	@Column(name = "totalCommentNum")
	private int totalCommentNum;//评论次数
	public int getTotalCommentNum() {
		return totalCommentNum;
	}
	public void setTotalCommentNum(int totalCommentNum) {
		this.totalCommentNum = totalCommentNum;
	}
	public int getOkNumber() {
		return okNumber;
	}
	public void setOkNumber(int okNumber) {
		this.okNumber = okNumber;
	}
	@Column(name = "okNumber")
	private int okNumber;//点赞次数
	@Column(name = "fansNumber")
	private int fansNumber;//粉丝次数
	@Column(name = "publishNumber")
	private int publishNumber;//商家发布次数
	@Column(name = "accSid")
	private Long accSid;//所属用户
	@Column(name = "updatedAt")
	private Long updatedAt;//更新时间
	@Column(name = "vipLevel")
	private Long vipLevel;//商家级别

	private List<Tag> tagList;//商家的标签列表
	private Goods goods;//商家的商品，即最后一条微博
	private String categoryName;//所在分类名称
	@Column(name = "sex")
	private int sex;//性别，如果用户进行了关注的是个人，则含有性别信息

	private Integer showType;//显示类型,主要用于关注里面,显示类型有分组,内容项,以及一个最近联系人


	private List<ProductInfo> products;

	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public List<Tag> getTagList() {
		return tagList;
	}
	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}
	public String getMerchantDesc() {
		return merchantDesc;
	}
	public void setMerchantDesc(String merchantDesc) {
		this.merchantDesc = merchantDesc;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getMerchantTelphone() {
		return merchantTelphone;
	}
	public void setMerchantTelphone(String merchantTelphone) {
		this.merchantTelphone = merchantTelphone;
	}
	public String getLogoPic() {
		return logoPic;
	}
	public void setLogoPic(String logoPic) {
		this.logoPic = logoPic;
	}
	public long getAlongAreaSid() {
		return alongAreaSid;
	}
	public void setAlongAreaSid(long alongAreaSid) {
		this.alongAreaSid = alongAreaSid;
	}
	public long getAlongDistrictSid() {
		return alongDistrictSid;
	}
	public void setAlongDistrictSid(long alongDistrictSid) {
		this.alongDistrictSid = alongDistrictSid;
	}
	public long getAlongCitySid() {
		return alongCitySid;
	}
	public void setAlongCitySid(long alongCitySid) {
		this.alongCitySid = alongCitySid;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMerchantAddr() {
		return merchantAddr;
	}
	public void setMerchantAddr(String merchantAddr) {
		this.merchantAddr = merchantAddr;
	}
	public double getLongItude() {
		return longItude;
	}
	public void setLongItude(double longItude) {
		this.longItude = longItude;
	}
	public double getLatItude() {
		return latItude;
	}
	public void setLatItude(double latItude) {
		this.latItude = latItude;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
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
	public int getPublishNumber() {
		return publishNumber;
	}
	public void setPublishNumber(int publishNumber) {
		this.publishNumber = publishNumber;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public long getCategorySid() {
		return categorySid;
	}
	public void setCategorySid(long categorySid) {
		this.categorySid = categorySid;
	}
	public String getBackPic() {
		return backPic;
	}
	public void setBackPic(String backPic) {
		this.backPic = backPic;
	}
	public String getSignatureName() {
		return signatureName;
	}
	public void setSignatureName(String signatureName) {
		this.signatureName = signatureName;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMerchantTagNameArray() {
		return merchantTagNameArray;
	}
	public void setMerchantTagNameArray(String merchantTagNameArray) {
		this.merchantTagNameArray = merchantTagNameArray;
	}
	public String getMerchantTagSidArray() {
		return merchantTagSidArray;
	}
	public void setMerchantTagSidArray(String merchantTagSidArray) {
		this.merchantTagSidArray = merchantTagSidArray;
	}
	public String getLastCont() {
		return lastCont;
	}
	public void setLastCont(String lastCont) {
		this.lastCont = lastCont;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public List<ProductInfo> getProducts() {
		return products;
	}
	public void setProducts(List<ProductInfo> products) {
		this.products = products;
	}
	public Long getAccSid() {
		return accSid;
	}
	public void setAccSid(Long accSid) {
		this.accSid = accSid;
	}
	public Long getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Integer getShowType() {
		return showType;
	}
	public void setShowType(Integer showType) {
		this.showType = showType;
	}
	public Long getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(Long vipLevel) {
		this.vipLevel = vipLevel;
	}
}
