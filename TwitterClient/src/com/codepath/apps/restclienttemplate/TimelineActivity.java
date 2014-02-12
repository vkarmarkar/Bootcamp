package com.codepath.apps.restclienttemplate;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.fragment.HomeTimelineFragment;
import com.codepath.apps.fragment.MentionsFragment;
import com.codepath.apps.fragment.TweetListFragment;

public class TimelineActivity extends FragmentActivity implements TabListener {
	private static final int REQUEST_CODE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupTabs();
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tabHome = actionBar.newTab().setText("Home").setTag(Timeline.HOME)
				.setIcon(R.drawable.ic_home).setTabListener(this);
		Tab tabMentions = actionBar.newTab().setText("Mentions")
				.setTag(Timeline.MENTIONS).setIcon(R.drawable.ic_at)
				.setTabListener(this);

		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
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

	public void profile(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("screen_name", "_vinner_");
		startActivityForResult(i, REQUEST_CODE);
	}

	public void refreshTimeline(MenuItem mi) {
		Fragment fragment = getSupportFragmentManager().findFragmentById(
				R.id.frameContainer);
		if (fragment instanceof TweetListFragment) {
			((TweetListFragment) fragment).refreshTimeline();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent i) {
		// REQUEST_CODE is defined above
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			refreshTimeline(null);
		}
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = manager
				.beginTransaction();
		if (Timeline.HOME.equals(tab.getTag())) {
			transaction
					.replace(R.id.frameContainer, new HomeTimelineFragment());
		} else {
			transaction.replace(R.id.frameContainer, new MentionsFragment());
		}
		transaction.commit();
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {

	}
}
