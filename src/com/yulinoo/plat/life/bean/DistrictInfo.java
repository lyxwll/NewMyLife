package com.yulinoo.plat.life.bean;

import java.io.Serializable;

public class DistrictInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long sid;

	private Integer status;

	private String createdAt;

	private String updatedAt;

	private Integer version;

	private Long alongCitySid;

	private String districtCode;

	private String districtName;

	private String firsttalph;

	private String firstpinyin;

	private String pinyin;

	private Integer orderBy;

	private String nameDesc;

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getAlongCitySid() {
		return alongCitySid;
	}

	public void setAlongCitySid(Long alongCitySid) {
		this.alongCitySid = alongCitySid;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getFirsttalph() {
		return firsttalph;
	}

	public void setFirsttalph(String firsttalph) {
		this.firsttalph = firsttalph;
	}

	public String getFirstpinyin() {
		return firstpinyin;
	}

	public void setFirstpinyin(String firstpinyin) {
		this.firstpinyin = firstpinyin;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public String getNameDesc() {
		return nameDesc;
	}

	public void setNameDesc(String nameDesc) {
		this.nameDesc = nameDesc;
	}
}