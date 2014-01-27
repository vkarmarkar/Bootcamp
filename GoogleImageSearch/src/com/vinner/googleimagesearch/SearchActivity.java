package com.vinner.googleimagesearch;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;

public class SearchActivity extends Activity {
	private static final int REQUEST_CODE = 0;
	EditText etSearchBox;
	GridView gvImages;
	ImageAdapter imageAdapter;
	SearchFilter filter = new SearchFilter();
	private List<ImageResult> imageResults = new ArrayList<ImageResult>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		etSearchBox = (EditText) findViewById(R.id.etSearchBox);
		setupSearchBox();
		imageAdapter = new ImageAdapter(this, imageResults);
		gvImages = (GridView) findViewById(R.id.gvImages);
		setupGridView();
	}

	private void setupGridView() {
		gvImages.setAdapter(imageAdapter);
		gvImages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long arg3) {
				Intent i = new Intent(getApplicationContext(), ImageActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivityForResult(i, REQUEST_CODE);
			}
		});
		gvImages.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your
				// AdapterView
				search(etSearchBox, totalItemsCount);
			}
		});
	}

	private void setupSearchBox() {
		etSearchBox.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
					search(v, 0);
					return true;
				}
				return false;
			}
		});
	}

	protected void search(View view, final int offset) {
		String query = etSearchBox.getText().toString();
		if (!query.isEmpty()) {
			AsyncHttpClient client = new AsyncHttpClient();
			String request = "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&start="
					+ offset
					+ "&v=1.0&q="
					+ Uri.encode(query)
					+ filter.toString();
			client.get(request, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					try {
						JSONArray jsonArray = null;
						jsonArray = response.getJSONObject("responseData")
								.getJSONArray("results");
						if (offset == 0) {
							imageResults.clear();
						}
						imageResults.addAll(ImageResult
								.fromJSONArray(jsonArray));
						imageAdapter.addAll(imageResults);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, java.lang.Throwable e,
						org.json.JSONObject errorResponse) {
					Log.d(this.toString(),
							statusCode + " " + errorResponse.toString());
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	public void loadSettings(MenuItem mi) {
		Intent i = new Intent(SearchActivity.this, SettingsActivity.class);
		i.putExtra("filter", filter);
		startActivityForResult(i, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent i) {
		// REQUEST_CODE is defined above
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			// Extract name value from result extras
			filter = (SearchFilter) i.getSerializableExtra("filter");
			search(etSearchBox, 0);
		}
	}

}
