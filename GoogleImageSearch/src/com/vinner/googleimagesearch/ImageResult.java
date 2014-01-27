package com.vinner.googleimagesearch;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {
	private static final long serialVersionUID = 5641853701906881953L;
	private String fullUrl;
	private String thumUrl;
	public ImageResult(JSONObject jsonObject) {
		try {
			this.fullUrl = jsonObject.getString("url");
			this.thumUrl = jsonObject.getString("tbUrl");
		} catch (JSONException e) {
			this.fullUrl = null;
			this.thumUrl = null;
		}
	}
	public String getFullUrl() {
		return fullUrl;
	}
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	public String getThumUrl() {
		return thumUrl;
	}
	public void setThumUrl(String thumUrl) {
		this.thumUrl = thumUrl;
	}
	public static ArrayList<ImageResult> fromJSONArray(
			JSONArray jsonArray) {
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int i = 0; i < jsonArray.length(); i ++) {
			try {
				results.add(new ImageResult(jsonArray.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
}
