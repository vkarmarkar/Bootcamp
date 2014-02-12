package com.codepath.apps.restclienttemplate;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.fragment.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		Intent i = getIntent();
		if (i.getStringExtra("screen_name") == null) {
			TwitterRestClientApp.getRestClient().getMyInfo(
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject json) {
							loadUserInfo(json);
						}
					});
		} else {
			Fragment fragment = getSupportFragmentManager().findFragmentById(
					R.id.fragmentUserTimeline);
			((UserTimelineFragment) fragment).screen_name = i
					.getStringExtra("screen_name");
			((UserTimelineFragment) fragment).fetchTweets(Timeline.USER);
			TwitterRestClientApp.getRestClient().getUserInfo(
					i.getStringExtra("screen_name"),
					i.getStringExtra("user_id"), new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject json) {
							loadUserInfo(json);
						}
					});
		}
	}

	protected void loadUserInfo(JSONObject json) {
		User u = User.fromJson(json);
		getActionBar().setTitle("@" + u.getScreenName());
		ImageView imageView = (ImageView) findViewById(R.id.profileImage);
		TextView tvName = (TextView) findViewById(R.id.name);
		TextView tvFollowing = (TextView) findViewById(R.id.following);
		TextView tvFollowers = (TextView) findViewById(R.id.followers);
		TextView tvTagline = (TextView) findViewById(R.id.tagLine);
		tvName.setText(u.getName());
		tvFollowers.setText(u.getFollowersCount() + " Followers");
		tvFollowing.setText(u.getFriendsCount() + " Following");
		tvTagline.setText(u.getTagline());
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(),
				imageView);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
