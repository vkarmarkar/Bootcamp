package com.vinner.googleimagesearch;

import java.io.Serializable;

import android.net.Uri;

public class SearchFilter implements Serializable {
	private static final long serialVersionUID = -423733075958908787L;
	private ImageSize size;
	private ImageType type;
	private String color;
	private String site;

	public SearchFilter(ImageSize size, ImageType type, String color,
			String site) {
		super();
		this.size = size;
		this.type = type;
		this.color = color;
		this.site = site;
	}

	public SearchFilter() {
	}

	public ImageSize getSize() {
		return size;
	}

	public void setSize(ImageSize size) {
		this.size = size;
	}

	public ImageType getType() {
		return type;
	}

	public void setType(ImageType type) {
		this.type = type;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (this.getSite() != null && !this.getSite().isEmpty()) {
			builder.append("&as_sitesearch=" + Uri.encode(this.getSite()));
		}
		if (this.getColor() != null && !this.getColor().isEmpty()) {
			builder.append("&imgcolor=" + Uri.encode(this.getColor()));
		}
		if (this.getSize() != null) {
			builder.append("&imgsz=" + Uri.encode(this.getSize().getValue()));
		}
		if (this.getType() != null) {
			builder.append("&imgtype=" + Uri.encode(this.getType().getValue()));
		}
		return builder.toString();
	}
}
