package com.none.web.model;

public enum StatusEnum {
	Published("Published", 0), Draft("Draft", 1);

	private String name;
	private int index;

	private StatusEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (StatusEnum status : StatusEnum.values()) {
			if (status.getIndex() == index) {
				return status.name;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
