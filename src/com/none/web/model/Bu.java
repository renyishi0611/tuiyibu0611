package com.none.web.model;

import java.io.Serializable;

public class Bu implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String bu;
	private String buPhoto;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBu() {
		return bu;
	}

	public void setBu(String bu) {
		this.bu = bu;
	}

	public String getBuPhoto() {
		return buPhoto;
	}

	public void setBuPhoto(String buPhoto) {
		this.buPhoto = buPhoto;
	}

}
