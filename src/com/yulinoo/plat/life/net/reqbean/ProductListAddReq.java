package com.yulinoo.plat.life.net.reqbean;

import java.io.Serializable;

public class ProductListAddReq implements Serializable{
	private static final long serialVersionUID = 1L;
	//private long accSid;//用户的账号ID
	//private String accName;//账号名称
	private String mevalue;//随机码
	private Long alongMerchantSid;//商家ID
	private Long productSid;//产品ID
	//private long citySid;//城市ID
	private Long categorySid;//产品所属分类ID
	private Long forumSid;//产品对应的栏目ID
	private String desc;//发布的内容
	private Long alongAreaSid;//小区ID
	//private String areaName;//小区名称
	private String indoorPath;//消息的附加图片路径
	public String getMevalue() {
		return mevalue;
	}
	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}
	public Long getAlongMerchantSid() {
		return alongMerchantSid;
	}
	public void setAlongMerchantSid(Long alongMerchantSid) {
		this.alongMerchantSid = alongMerchantSid;
	}
	public Long getProductSid() {
		return productSid;
	}
	public void setProductSid(Long productSid) {
		this.productSid = productSid;
	}
	public Long getCategorySid() {
		return categorySid;
	}
	public void setCategorySid(Long categorySid) {
		this.categorySid = categorySid;
	}
	public Long getForumSid() {
		return forumSid;
	}
	public void setForumSid(Long forumSid) {
		this.forumSid = forumSid;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Long getAlongAreaSid() {
		return alongAreaSid;
	}
	public void setAlongAreaSid(Long alongAreaSid) {
		this.alongAreaSid = alongAreaSid;
	}
	public String getIndoorPath() {
		return indoorPath;
	}
	public void setIndoorPath(String indoorPath) {
		this.indoorPath = indoorPath;
	}
	
}
