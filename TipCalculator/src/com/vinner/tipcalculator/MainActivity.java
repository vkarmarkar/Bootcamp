package com.vinner.tipcalculator;

import java.text.DecimalFormat;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	EditText etBillAmt;
	Button btTip10;
	Button btTip15;
	Button btTip20;
	EditText etCustomTip;
	TextView tvTipAmt;
	double selectedTip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		etBillAmt = (EditText) findViewById(R.id.tvBillAmt);
		btTip10 = (Button) findViewById(R.id.btTip10);
		btTip15 = (Button) findViewById(R.id.btTip15);
		btTip20 = (Button) findViewById(R.id.btTip20);
		etCustomTip = (EditText) findViewById(R.id.etCustomTip);
		tvTipAmt = (TextView) findViewById(R.id.tvTipAmt);
		setupTipButtonListeners();
		setupBillAmtListener();
		setupCustomTipListener();
	}

	private void setupCustomTipListener() {
		etCustomTip.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					btTip10.setTextColor(Color.BLACK);
					btTip15.setTextColor(Color.BLACK);
					btTip20.setTextColor(Color.BLACK);
					etCustomTip.setTextColor(Color.BLUE);
				}
			}
		});
		etCustomTip.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable tip) {
				try {
					if (tip != null && tip.length() > 0) {
						selectedTip = Double.parseDouble(tip.toString()) / 100;
					}
				} catch (NumberFormatException nfe) {
					selectedTip = 0;
				}
				calculateTip(selectedTip);
			}
		});
	}

	private void setupBillAmtListener() {
		etBillAmt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable tip) {
				calculateTip(selectedTip);
			}
		});
	}

	private void setupTipButtonListeners() {
		btTip10.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				selectedTip = 0.10;
				calculateTip(selectedTip);
				btTip10.setTextColor(Color.BLUE);
				btTip15.setTextColor(Color.BLACK);
				btTip20.setTextColor(Color.BLACK);
				etCustomTip.setTextColor(Color.BLACK);
			}
		});
		btTip15.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				selectedTip = 0.15;
				calculateTip(selectedTip);
				btTip15.setTextColor(Color.BLUE);
				btTip10.setTextColor(Color.BLACK);
				btTip20.setTextColor(Color.BLACK);
				etCustomTip.setTextColor(Color.BLACK);
			}
		});
		btTip20.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				selectedTip = 0.20;
				calculateTip(selectedTip);
				btTip20.setTextColor(Color.BLUE);
				btTip10.setTextColor(Color.BLACK);
				btTip15.setTextColor(Color.BLACK);
				etCustomTip.setTextColor(Color.BLACK);
			}
		});
	}

	protected void calculateTip(double d) {
		StringBuilder tipText = new StringBuilder();
		if (etBillAmt != null && etBillAmt.getText().toString().length() > 0
				&& d > 0) {
			try {
				tipText.append("Tip is: $");
				Double tipAmt = d
						* Double.parseDouble(etBillAmt.getText().toString());
				tipText.append(new DecimalFormat("#.##").format(tipAmt));
			} catch (NumberFormatException nfe) {
				tipText = new StringBuilder("Invalid bill amount.");
			}
		}
		tvTipAmt.setText(tipText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
