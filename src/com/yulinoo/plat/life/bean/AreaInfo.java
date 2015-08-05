package com.yulinoo.plat.life.bean;

import java.io.Serializable;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

//小区信息
@Table(name = "AreaInfo")
public class AreaInfo extends BaseBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@Column(name = "sid")
	private long sid;
	@Column(name = "alongCitySid")
	private long alongCitySid;//所属城市
	@Column(name = "cityDomain")
	private String cityDomain;//城市域名
	@Column(name = "alongDistrictSid")
	private long alongDistrictSid;//所属区县
	@Column(name = "areaName")
	private String areaName;//小区名称
	@Column(name = "latItude")
	private double latItude;//经度
	@Column(name = "longItude")
	private double longItude;//纬度
	@Column(name = "icon")
	private String icon;//小区图片
	@Column(name = "enterNum")
	private int enterNum;//商家入驻数
	@Column(name = "attNum")
	private int attNum;//关注数
	@Column(name = "areaType")
	private int areaType;//小区类型，分为当前选中小区还是关注小区，正常情况下所有小区都是关注的小区，只是某一个小区会成为当前选中的小区
	@Column(name = "accSid")
	private Long accSid;//小区所属用户
	@Column(name = "concernAt")
	private Long concernAt;//关注的时间
	@Column(name = "updatedAt")
	private Long updatedAt;//小区的更新时间
	
	
	
	public static final int AREATYPE_NOWSHOW=1;//当前的选中小区
	public static final int AREATYPE_FOCUS=2;//当前用户的关注小区，选中小区可能为关注小区中的一条
	public static final int AREATYPE_FOCUS_AND_NOWSHOW=3;//该小区既是用户所关注的小区，又是当前进行显示的小区
	//public boolean concerned;//是否已经关注
	private boolean isMapOpen=false;//是指地图是否被打开，打开则显示详情，反之则只显示一个图标

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

	public double getLatItude() {
		return latItude;
	}

	public void setLatItude(double latItude) {
		this.latItude = latItude;
	}

	public double getLongItude() {
		return longItude;
	}

	public void setLongItude(double longItude) {
		this.longItude = longItude;
	}

	public long getAlongCitySid() {
		return alongCitySid;
	}

	public void setAlongCitySid(long alongCitySid) {
		this.alongCitySid = alongCitySid;
	}

	public long getAlongDistrictSid() {
		return alongDistrictSid;
	}

	public void setAlongDistrictSid(long alongDistrictSid) {
		this.alongDistrictSid = alongDistrictSid;
	}

	public int getAreaType() {
		return areaType;
	}

	public void setAreaType(int areaType) {
		this.areaType = areaType;
	}

	public boolean isMapOpen() {
		return isMapOpen;
	}

	public void setMapOpen(boolean isMapOpen) {
		this.isMapOpen = isMapOpen;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getEnterNum() {
		return enterNum;
	}

	public void setEnterNum(int enterNum) {
		this.enterNum = enterNum;
	}

	public int getAttNum() {
		return attNum;
	}

	public void setAttNum(int attNum) {
		this.attNum = attNum;
	}

	public String getCityDomain() {
		return cityDomain;
	}

	public void setCityDomain(String cityDomain) {
		this.cityDomain = cityDomain;
	}

	public Long getAccSid() {
		return accSid;
	}

	public void setAccSid(Long accSid) {
		this.accSid = accSid;
	}

	public Long getConcernAt() {
		return concernAt;
	}

	public void setConcernAt(Long concernAt) {
		this.concernAt = concernAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	

}
