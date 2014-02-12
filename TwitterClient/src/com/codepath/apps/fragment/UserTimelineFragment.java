package com.codepath.apps.fragment;

import android.os.Bundle;

import com.codepath.apps.restclienttemplate.Timeline;

public class UserTimelineFragment extends TweetListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fetchTweets(Timeline.USER);
	}
}
