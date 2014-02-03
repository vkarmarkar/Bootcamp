package com.codepath.apps.restclienttemplate.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Tweets")
public class Tweet extends BaseModel {
	private User user;
	private long id;

	public User getUser() {
		return user;
	}

	public long getId() {
		return id;
	}

	// Define database columns and associated fields
	@Column(name = "userId")
	String userId;
	@Column(name = "userHandle")
	String userHandle;
	@Column(name = "timestamp")
	String timestamp;
	@Column(name = "body")
	String body;

	// Make sure to always define this constructor with no arguments
	public Tweet() {
		super();
	}

	public Tweet(JSONObject object) {
		super();

		try {
			this.userId = object.getString("user_id");
			this.userHandle = object.getString("user_username");
			this.timestamp = object.getString("timestamp");
			this.body = object.getString("body");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		try {
			tweet.jsonObject = jsonObject;
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
			tweet.id = jsonObject.getLong("id");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return tweet;
	}

	public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			Tweet tweet = fromJson(tweetJson);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}

		return tweets;
	}

	public String getBody() {
		return getString("text");
	}

	public String getTimestamp() {
		return getString("created_at");
	}

	public static long findLowestId(ArrayList<Tweet> tweets) {
		long id = 999999999;
		for (Tweet tweet : tweets) {
			if (tweet.getId() < id) {
				id = tweet.getId();
			}
		}
		return id;
	}
}
