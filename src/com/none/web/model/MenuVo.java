package com.none.web.model;

public class MenuVo {
	private String name;//菜单名称
	private String id;//菜单ID
	private String imgicon;//菜单图标
	private String entity;//哪些entity能看到菜单  add by Bing 20160427
	private String pic_1x;
	private String pic_2x;
	private String pic_3x;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImgicon() {
		return imgicon;
	}
	public void setImgicon(String imgicon) {
		this.imgicon = imgicon;
	}
	public String getPic_1x() {
		return pic_1x;
	}
	public void setPic_1x(String pic_1x) {
		this.pic_1x = pic_1x;
	}
	public String getPic_2x() {
		return pic_2x;
	}
	public void setPic_2x(String pic_2x) {
		this.pic_2x = pic_2x;
	}
	public String getPic_3x() {
		return pic_3x;
	}
	public void setPic_3x(String pic_3x) {
		this.pic_3x = pic_3x;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
}
