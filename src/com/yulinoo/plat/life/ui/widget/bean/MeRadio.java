package com.yulinoo.plat.life.ui.widget.bean;


public class MeRadio {
	private int index;
	private String strIndex;
	private String name;
	private int selectedPicture=-1;//选中的图片
	private int unSelectedPicture=-1;
	private int selectedTextColor=-1;
	private int unSelectedTextColor=-1;
	private int selectedBgColor=-1;
	private int unSelectedBgColor=-1;
	private int textSize=12;
	private int direct;
	private boolean isSelected;
	
	public static final int DIRECT_LEFT=1;
	public static final int DIRECT_TOP=2;
	public static final int DIRECT_RIGHT=3;
	public static final int DIRECT_BOTTOM=4;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getStrIndex() {
		return strIndex;
	}
	public void setStrIndex(String strIndex) {
		this.strIndex = strIndex;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSelectedPicture() {
		return selectedPicture;
	}
	public void setSelectedPicture(int selectedPicture) {
		this.selectedPicture = selectedPicture;
	}
	public int getUnSelectedPicture() {
		return unSelectedPicture;
	}
	public void setUnSelectedPicture(int unSelectedPicture) {
		this.unSelectedPicture = unSelectedPicture;
	}
	public int getDirect() {
		return direct;
	}
	public void setDirect(int direct) {
		this.direct = direct;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public int getSelectedTextColor() {
		return selectedTextColor;
	}
	public void setSelectedTextColor(int selectedTextColor) {
		this.selectedTextColor = selectedTextColor;
	}
	public int getUnSelectedTextColor() {
		return unSelectedTextColor;
	}
	public void setUnSelectedTextColor(int unSelectedTextColor) {
		this.unSelectedTextColor = unSelectedTextColor;
	}
	public int getSelectedBgColor() {
		return selectedBgColor;
	}
	public void setSelectedBgColor(int selectedBgColor) {
		this.selectedBgColor = selectedBgColor;
	}
	public int getUnSelectedBgColor() {
		return unSelectedBgColor;
	}
	public void setUnSelectedBgColor(int unSelectedBgColor) {
		this.unSelectedBgColor = unSelectedBgColor;
	}
	public int getTextSize() {
		return textSize;
	}
	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}
}
