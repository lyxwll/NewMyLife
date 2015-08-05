package com.yulinoo.plat.life.bean;

import java.io.Serializable;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "CityCategory")
public class Category extends BaseBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@Column(name = "sid")
	private long sid;
	@Column(name = "categoryName")
	private String categoryName;
	@Column(name = "orderBy")
	private int orderBy;
	@Column(name = "categoryPic")
	private String categoryPic;
	@Column(name = "extracType")
	private int extracType;
	@Column(name = "merchantCanChoice")
	private int merchantCanChoice;//商家是否要选
	@Column(name = "homeDisplay")
	private int homeDisplay;//是否是邻语
	@Column(name = "citySid")
	private Long citySid;//所属城市
	@Column(name = "updatedAt")
	private Long updatedAt;//更新时间
	@Column(name = "status")
	private int status;//状态，0表示逻辑删除，1表示有效
	@Column(name="categoryDesc")
	private String categoryDesc;//分类描述
	@Column(name="color")
	private String color;//背景颜色
	
	private boolean isSelected;
	
	public String getCategoryPic() {
		return categoryPic;
	}
	public void setCategoryPic(String categoryPic) {
		this.categoryPic = categoryPic;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
	}
	public int getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}
	public int getExtracType() {
		return extracType;
	}
	public void setExtracType(int extracType) {
		this.extracType = extracType;
	}
	public int getMerchantCanChoice() {
		return merchantCanChoice;
	}
	public void setMerchantCanChoice(int merchantCanChoice) {
		this.merchantCanChoice = merchantCanChoice;
	}
	public int getHomeDisplay() {
		return homeDisplay;
	}
	public void setHomeDisplay(int homeDisplay) {
		this.homeDisplay = homeDisplay;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public Long getCitySid() {
		return citySid;
	}
	public void setCitySid(Long citySid) {
		this.citySid = citySid;
	}
	
	public Long getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

}
