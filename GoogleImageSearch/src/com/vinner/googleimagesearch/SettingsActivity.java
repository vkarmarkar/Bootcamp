package com.vinner.googleimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {
	Spinner sizeSpinner;
	Spinner typeSpinner;
	EditText etColor;
	EditText etSite;
	Button btSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		sizeSpinner = (Spinner) findViewById(R.id.spImageSize);
		setupSpinner(sizeSpinner, ImageSize.getAllSizes());
		typeSpinner = (Spinner) findViewById(R.id.spImageType);
		setupSpinner(typeSpinner, ImageType.getAllTypes());
		etColor = (EditText) findViewById(R.id.etColor);
		etSite = (EditText) findViewById(R.id.etSite);
		setOnLoadValues();
		btSave = (Button) findViewById(R.id.btSave);
		setupOnSaveListener();
	}

	@SuppressWarnings("unchecked")
	private void setOnLoadValues() {
		Intent intent = getIntent();
		SearchFilter filter = (SearchFilter) intent
				.getSerializableExtra("filter");
		if (filter.getSite() != null && !filter.getSite().isEmpty()) {
			etSite.setText(filter.getSite());
		}
		if (filter.getColor() != null && !filter.getColor().isEmpty()) {
			etColor.setText(filter.getColor());
		}
		if (filter.getSize() != null) {
			ArrayAdapter<String> adapter = (ArrayAdapter<String>) sizeSpinner.getAdapter();
			sizeSpinner.setSelection(adapter.getPosition(filter.getSize().getLabel()));
		}
		if (filter.getType() != null) {
			ArrayAdapter<String> adapter = (ArrayAdapter<String>) typeSpinner.getAdapter();
			typeSpinner.setSelection(adapter.getPosition(filter.getType().getLabel()));
		}
	}

	private void setupOnSaveListener() {
		btSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				SearchFilter filter = (SearchFilter) intent
						.getSerializableExtra("filter");
				filter.setColor(etColor.getText().toString());
				filter.setSite(etSite.getText().toString());
				filter.setSize(ImageSize.getSize(sizeSpinner.getSelectedItem()
						.toString()));
				filter.setType(ImageType.getType(typeSpinner.getSelectedItem()
						.toString()));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private void setupSpinner(Spinner spinner, String[] options) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, options);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
