package com.codepath.apps.restclienttemplate;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
	private static final int REQUEST_CODE = 0;
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		fetchTweets(0);
	}

	private void fetchTweets(long max_id) {
		TwitterRestClientApp.getRestClient().getHomeTimeline(max_id,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						ListView listView = (ListView) findViewById(R.id.lvTweets);
						tweets = Tweet.fromJson(jsonTweets);
						TweetAdapter adapter = new TweetAdapter(
								getBaseContext(), tweets);
						listView.setAdapter(adapter);
						listView.setOnScrollListener(new EndlessScrollListener() {
							@Override
							public void onLoadMore(int page, int totalItemsCount) {
								fetchTweets(Tweet.findLowestId(tweets));
							}
						});
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	public void composeTweet(MenuItem mi) {
		Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
		startActivityForResult(i, REQUEST_CODE);
	}

	public void refreshTimeline(MenuItem mi) {
		fetchTweets(0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent i) {
		// REQUEST_CODE is defined above
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			fetchTweets(0);
		}
	}
}
