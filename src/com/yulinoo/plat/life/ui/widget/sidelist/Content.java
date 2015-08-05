package com.yulinoo.plat.life.ui.widget.sidelist;

import java.io.Serializable;

public class Content implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String letter;
	private String name;
	private String cityDomain;
	private String firstpinyin;
		
	
	public Content(String letter, String name,String cityDomain,String firstpinyin) {
		super();
		this.letter = letter;
		this.name = name;
		this.cityDomain=cityDomain;
		this.firstpinyin=firstpinyin;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCityDomain() {
		return cityDomain;
	}
	public void setCityDomain(String cityDomain) {
		this.cityDomain = cityDomain;
	}
	public String getFirstpinyin() {
		return firstpinyin;
	}
	public void setFirstpinyin(String firstpinyin) {
		this.firstpinyin = firstpinyin;
	}
	
}
