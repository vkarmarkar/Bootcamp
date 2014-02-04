package com.codepath.apps.restclienttemplate;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends Activity {
	TextView tvCharLeft;
	EditText etTweet;

	private final TextWatcher mTextEditorWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			tvCharLeft.setText(String.valueOf(140 - s.length()) + " char left");
		}

		public void afterTextChanged(Editable s) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		tvCharLeft = (TextView) findViewById(R.id.tvCharLeft);
		etTweet = (EditText) findViewById(R.id.etTweet);
		etTweet.addTextChangedListener(mTextEditorWatcher);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	public void cancelCompose(View view) {
		setResult(RESULT_OK, getIntent());
		finish();
	}

	public void postTweet(View view) {
		TwitterRestClientApp.getRestClient().postTweet(
				etTweet.getText().toString(), new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jsonTweet) {
						setResult(RESULT_OK, getIntent());
						finish();
					}

					@Override
					public void onFailure(java.lang.Throwable e,
							org.json.JSONObject errorResponse) {
						Log.d(this.toString(), errorResponse.toString());
					}
				});
	}

}
