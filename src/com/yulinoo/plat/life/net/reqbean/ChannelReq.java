package com.yulinoo.plat.life.net.reqbean;

import com.yulinoo.plat.life.utils.Constant;

public class ChannelReq {
	private Integer pageNo;
	private Long areaInfoSid;
	private Integer queryType;
	private Integer position=Constant.AREA.CHANNEL_INDEX;
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Long getAreaInfoSid() {
		return areaInfoSid;
	}
	public void setAreaInfoSid(Long areaInfoSid) {
		this.areaInfoSid = areaInfoSid;
	}
	public Integer getQueryType() {
		return queryType;
	}
	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
}
