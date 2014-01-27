package com.vinner.googleimagesearch;

import java.util.ArrayList;

public enum ImageSize {

	SMALL ("Small", "icon"), MEDIUM ("Medium", "medium"), LARGE( "Large", "xxlarge"), EXTRA_LARGE ("Extra Large", "huge");

	private String label;
	private String value;

	private ImageSize(String label, String value) {
		this.label = label;
		this.value = value;
	}

	public static ImageSize getSize(String label) {
		for (ImageSize size : ImageSize.values()) {
			if (size.label.equals(label)) {
				return size;
			}
		}
		return null;
	}
	
	public static String[] getAllSizes() {
		ArrayList<String> sizes = new ArrayList<String>();
		for (ImageSize size : ImageSize.values()) {
			sizes.add(size.label);
		}
		return sizes.toArray(new String[sizes.size()]);
	}

	public String getValue() {
		return value;
	}
	
	public String getLabel() {
		return label;
	}

}
