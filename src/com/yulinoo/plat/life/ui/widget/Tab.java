package com.yulinoo.plat.life.ui.widget;

import java.io.Serializable;

//tab的封闭
public class Tab implements Serializable{
	private static final long serialVersionUID = 1L;
	private int index;//tab的索引顺序号
	private String name;//tab的显示名称
	private boolean isSelected;//是否被选中
	private boolean permissionPersional;//是否允许个人发布
	
	private long productSid;//产品ID
	private long categorySid;//分类ID
	private long forumSid;//栏目ID
	private long merchantSid;//商家ID
	private long alongAreaSid;//小区ID
	private String areaName;//小区名称
	private long channelSid;//频道ID
	private long alongMerchantSid;
	private String photoUrl;//发贴者的头像
	private String accName;//账号名称
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getProductSid() {
		return productSid;
	}
	public void setProductSid(long productSid) {
		this.productSid = productSid;
	}
	public long getCategorySid() {
		return categorySid;
	}
	public void setCategorySid(long categorySid) {
		this.categorySid = categorySid;
	}
	public long getForumSid() {
		return forumSid;
	}
	public void setForumSid(long forumSid) {
		this.forumSid = forumSid;
	}
	public long getChannelSid() {
		return channelSid;
	}
	public void setChannelSid(long channelSid) {
		this.channelSid = channelSid;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public boolean isPermissionPersional() {
		return permissionPersional;
	}
	public void setPermissionPersional(boolean permissionPersional) {
		this.permissionPersional = permissionPersional;
	}
	public long getMerchantSid() {
		return merchantSid;
	}
	public void setMerchantSid(long merchantSid) {
		this.merchantSid = merchantSid;
	}
	
	public long getAlongAreaSid() {
		return alongAreaSid;
	}
	public void setAlongAreaSid(long alongAreaSid) {
		this.alongAreaSid = alongAreaSid;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public long getAlongMerchantSid() {
		return alongMerchantSid;
	}
	public void setAlongMerchantSid(long alongMerchantSid) {
		this.alongMerchantSid = alongMerchantSid;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
}
