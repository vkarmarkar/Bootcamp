package com.codepath.apps.fragment;

import android.os.Bundle;

import com.codepath.apps.restclienttemplate.Timeline;

public class HomeTimelineFragment extends TweetListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fetchTweets(Timeline.HOME);
	}

}
