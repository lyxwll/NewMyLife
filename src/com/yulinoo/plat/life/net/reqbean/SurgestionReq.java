package com.yulinoo.plat.life.net.reqbean;

public class SurgestionReq {
	private Long citySid;
	private Long areaSid;
	private String suggestions;
	private String mevalue;
	private Long goodsSid;
	private Integer suggestionType;//建议的类型
	private String linkType;//联系方式

	public Long getCitySid() {
		return citySid;
	}

	public void setCitySid(Long citySid) {
		this.citySid = citySid;
	}

	public String getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}

	public String getMevalue() {
		return mevalue;
	}

	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}

	public Long getGoodsSid() {
		return goodsSid;
	}

	public void setGoodsSid(Long goodsSid) {
		this.goodsSid = goodsSid;
	}

	public Long getAreaSid() {
		return areaSid;
	}

	public void setAreaSid(Long areaSid) {
		this.areaSid = areaSid;
	}

	public Integer getSuggestionType() {
		return suggestionType;
	}

	public void setSuggestionType(Integer suggestionType) {
		this.suggestionType = suggestionType;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

}
