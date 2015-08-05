package com.yulinoo.plat.life.bean;

import java.math.BigDecimal;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "ForumNote")
public class ForumNote extends BaseBean implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	@Column(name = "accSid")
	private Long accSid;
	@Column(name = "accName")
	private String accName;
	@Column(name = "address")
	private String address;
	@Column(name = "sex")
	private int sex;
	@Column(name = "longItude")
	private BigDecimal longItude;
	@Column(name = "latItude")
    private BigDecimal latItude;
	@Column(name = "telphone")
    private String telphone;
	@Column(name = "headPicture")
    private String headPicture;
	@Column(name = "merchantTagSidArray")
    private String merchantTagSidArray;
	@Column(name = "merchantTagNameArray")
	private String merchantTagNameArray;
	@Column(name = "alongAreaSid")
	private Long alongAreaSid;
	@Column(name = "areaName")
	private String areaName;
	@Column(name = "categoryName")
    private String categoryName;
	@Column(name = "categorySid")
	private Long categorySid;
	@Column(name = "goodsSid")
	private Long goodsSid;
	@Column(name = "goodsContent")
	private String goodsContent;
	@Column(name = "goodsPhotoArray")
	private String goodsPhotoArray;
	@Column(name = "viewNumber")
	private Integer viewNumber;
	@Column(name = "praiseNumber")
	private Integer praiseNumber;
	@Column(name = "commentNumber")
	private Integer commentNumber;
	@Column(name = "createDate")
	private Long createDate;
	@Column(name = "alongForumSid")
	private Long alongForumSid;
	@Column(name = "alongForumName")
	private String alongForumName;
	@Column(name = "merchantType")
	private Integer merchantType;
	@Column(name = "vipLevel")
	private Integer vipLevel;
	
	private boolean isClosed;//是否关闭,主要针对广告的
	
	public Long getAccSid() {
		return accSid;
	}
	public void setAccSid(Long accSid) {
		this.accSid = accSid;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public BigDecimal getLongItude() {
		return longItude;
	}
	public void setLongItude(BigDecimal longItude) {
		this.longItude = longItude;
	}
	public BigDecimal getLatItude() {
		return latItude;
	}
	public void setLatItude(BigDecimal latItude) {
		this.latItude = latItude;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getHeadPicture() {
		return headPicture;
	}
	public void setHeadPicture(String headPicture) {
		this.headPicture = headPicture;
	}
	public String getMerchantTagSidArray() {
		return merchantTagSidArray;
	}
	public void setMerchantTagSidArray(String merchantTagSidArray) {
		this.merchantTagSidArray = merchantTagSidArray;
	}
	public String getMerchantTagNameArray() {
		return merchantTagNameArray;
	}
	public void setMerchantTagNameArray(String merchantTagNameArray) {
		this.merchantTagNameArray = merchantTagNameArray;
	}
	public Long getAlongAreaSid() {
		return alongAreaSid;
	}
	public void setAlongAreaSid(Long alongAreaSid) {
		this.alongAreaSid = alongAreaSid;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Long getCategorySid() {
		return categorySid;
	}
	public void setCategorySid(Long categorySid) {
		this.categorySid = categorySid;
	}
	public Long getGoodsSid() {
		return goodsSid;
	}
	public void setGoodsSid(Long goodsSid) {
		this.goodsSid = goodsSid;
	}
	public String getGoodsContent() {
		return goodsContent;
	}
	public void setGoodsContent(String goodsContent) {
		this.goodsContent = goodsContent;
	}
	public String getGoodsPhotoArray() {
		return goodsPhotoArray;
	}
	public void setGoodsPhotoArray(String goodsPhotoArray) {
		this.goodsPhotoArray = goodsPhotoArray;
	}
	public Integer getViewNumber() {
		return viewNumber;
	}
	public void setViewNumber(Integer viewNumber) {
		this.viewNumber = viewNumber;
	}
	public Integer getPraiseNumber() {
		return praiseNumber;
	}
	public void setPraiseNumber(Integer praiseNumber) {
		this.praiseNumber = praiseNumber;
	}
	public Integer getCommentNumber() {
		return commentNumber;
	}
	public void setCommentNumber(Integer commentNumber) {
		this.commentNumber = commentNumber;
	}
	public Long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	public Long getAlongForumSid() {
		return alongForumSid;
	}
	public void setAlongForumSid(Long alongForumSid) {
		this.alongForumSid = alongForumSid;
	}
	public String getAlongForumName() {
		return alongForumName;
	}
	public void setAlongForumName(String alongForumName) {
		this.alongForumName = alongForumName;
	}
//	public boolean isHaveAdvertise() {
//		return haveAdvertise;
//	}
//	public void setHaveAdvertise(boolean haveAdvertise) {
//		this.haveAdvertise = haveAdvertise;
//	}
	public boolean isClosed() {
		return isClosed;
	}
	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}
	public Integer getMerchantType() {
		return merchantType;
	}
	public void setMerchantType(Integer merchantType) {
		this.merchantType = merchantType;
	}
	public Integer getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}
}
