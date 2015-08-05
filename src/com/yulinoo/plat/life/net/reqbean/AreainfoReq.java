package com.yulinoo.plat.life.net.reqbean;

public class AreainfoReq {
	private Long alongCitySid;
	private Long alongDistrictSid;
	private Integer pageNo;
	private String pinyin;
	private String word;

	public Long getAlongCitySid() {
		return alongCitySid;
	}

	public void setAlongCitySid(Long alongCitySid) {
		this.alongCitySid = alongCitySid;
	}

	public Long getAlongDistrictSid() {
		return alongDistrictSid;
	}

	public void setAlongDistrictSid(Long alongDistrictSid) {
		this.alongDistrictSid = alongDistrictSid;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

}
