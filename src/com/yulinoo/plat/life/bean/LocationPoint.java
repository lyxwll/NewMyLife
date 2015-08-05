package com.yulinoo.plat.life.bean;

import java.io.Serializable;

/**
 * 定位标注点
 * @author jefry
 *
 */
public class LocationPoint implements Serializable{
	private static final long serialVersionUID = 1L;
	private String address;
	private double latitude;//定位点经度
	private double longitude;//定位点纬度
	private String name;//定位点名称
	private String date;
	private boolean isMe; //是我的位置
	
	private int number;
	
	private String obj1;
	
	private long  areaId;
	
	private long  cityId;
	
	private long  distinctId;
	
	public static final int LOCAL_AREA=1;
	public static final int LOCAL_MERCHANT=2;
	public int localType;//位置类型，目前分为小区和商家
	
	
	
	
	

	
	
	public long getAreaId() {
		return areaId;
	}
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public long getDistinctId() {
		return distinctId;
	}
	public void setDistinctId(long distinctId) {
		this.distinctId = distinctId;
	}
	public String getObj1() {
		return obj1;
	}
	public void setObj1(String obj1) {
		this.obj1 = obj1;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public boolean isMe() {
		return isMe;
	}
	public void setMe(boolean isMe) {
		this.isMe = isMe;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	

}
