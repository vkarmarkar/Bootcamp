package com.codepath.apps.fragment;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.restclienttemplate.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.Timeline;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.TwitterRestClientApp;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TweetListFragment extends Fragment {
	ListView listView;
	TweetAdapter adapter;
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	long max_id;
	public String screen_name;

	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inf.inflate(R.layout.fragment_tweets_list, parent, false);
		listView = (ListView) v.findViewById(R.id.lvTweets);
		adapter = new TweetAdapter(v.getContext(), tweets);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				max_id = Tweet.findLowestId(tweets);
				fetchTweets(Timeline.HOME);
			}
		});
	}

	public TweetAdapter getAdapter() {
		return adapter;
	}

	public void refreshTimeline() {
		getAdapter().clear();
		max_id = 0;
		fetchTweets(Timeline.HOME);
	}

	public void fetchTweets(Timeline timeline) {
		TwitterRestClientApp.getRestClient().getTimeline(screen_name, max_id, timeline,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						getAdapter().addAll(Tweet.fromJson(jsonTweets));
					}
				});

	}

}
