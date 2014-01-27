package com.vinner.googleimagesearch;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;

public class ImageAdapter extends ArrayAdapter<ImageResult> {

	public ImageAdapter(Context context, List<ImageResult> imageResults) {
		super(context, R.layout.image, imageResults);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SmartImageView imageView;
		ImageResult image = this.getItem(position);
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			imageView = (SmartImageView) inflater.inflate(R.layout.image,
					parent, false);
		} else {
			imageView = (SmartImageView) convertView;
			imageView.setImageResource(android.R.color.transparent);
		}
		imageView.setImageUrl(image.getThumUrl());
		return imageView;
	}

}
