package com.codepath.apps.restclienttemplate;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterRestClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1/";
	public static final String REST_CONSUMER_KEY = "qesvZzqzT3eJ8bHIY1mXZw";
	public static final String REST_CONSUMER_SECRET = "rr7XqGQYPzzKFYgS8yISCUmobODrCtLmulYrXUVkQ";
	public static final String REST_CALLBACK_URL = "oauth://codepathtweets";

	public TwitterRestClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(long max_id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", "25");
		if (max_id > 0) {
			params.put("max_id", String.valueOf(max_id));
		}
		getClient().get(apiUrl, params, handler);
	}

	public void postTweet(String body, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		getClient().post(apiUrl, params, handler);
	}

	public void getMentions(AsyncHttpResponseHandler handler) {
		String url = getApiUrl("statuses/mentions_timeline.json");
		client.get(url, null, handler);
	}

	public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler) {
		String url = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		if (screenName != null) {
			params.put("screen_name", screenName);
		}
		client.get(url, params, handler);
	}

	public void getMyInfo(AsyncHttpResponseHandler handler) {
		String url = getApiUrl("account/verify_credentials.json");
		client.get(url, null, handler);
	}

	public void getTimeline(String screen_name, long max_id, Timeline timeline,
			JsonHttpResponseHandler handler) {
		switch (timeline) {
		case HOME:
			getHomeTimeline(max_id, handler);
			break;
		case MENTIONS:
			getMentions(handler);
			break;
		case USER:
			getUserTimeline(screen_name, handler);
			break;
		}
	}

	public void getUserInfo(String screeName, String userId,
			JsonHttpResponseHandler handler) {
		String url = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screeName);
		params.put("user_id", userId);
		client.get(url, params, handler);
	}

}