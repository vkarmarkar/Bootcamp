package com.vinner.googleimagesearch;

import java.util.ArrayList;

public enum ImageType {

	FACE("Face", "face"), PHOTO("Photo", "photo"), CLIP_ART("Clip Art", "clipart"), LINE_ART("Line Art", "lineart");

	private String label;
	private String value;

	public String getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	private ImageType(String label, String value) {
		this.label = label;
		this.value = value;
	}

	public static ImageType getType(String label) {
		for (ImageType type : ImageType.values()) {
			if (type.label.equals(label)) {
				return type;
			}
		}
		return null;
	}

	public static String[] getAllTypes() {
		ArrayList<String> types = new ArrayList<String>();
		for (ImageType type : ImageType.values()) {
			types.add(type.label);
		}
		return types.toArray(new String[types.size()]);
	}
}
