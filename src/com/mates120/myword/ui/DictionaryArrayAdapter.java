package com.mates120.myword.ui;

import java.util.List;

import com.mates120.myword.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

public class DictionaryArrayAdapter extends ArrayAdapter<String>{
	
	private Context context;

	public DictionaryArrayAdapter(Context context, 
			int textViewResourceId, List<String> dicts){
		super(context, textViewResourceId, dicts);
		this.context = context;
	}
	
	@Override
	public CheckedTextView getView(int position, View convertView, ViewGroup parent){
		CheckedTextView dictionary = null;
		String dict = getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.dict_item, null);
		}
		dictionary = (CheckedTextView) convertView;
		
		dictionary.setText(dict);
		dictionary.setChecked(true);
		return (CheckedTextView)convertView;
	}
}
