package com.mates120.myword.ui;

import java.util.List;

import com.mates120.myword.Dictionary;
import com.mates120.myword.DictionaryManager;
import com.mates120.myword.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

public class DictionaryArrayAdapter extends ArrayAdapter<Dictionary>{
	
	private Context context;
	private DictionaryManager dictionaryManager;

	public DictionaryArrayAdapter(Context context, 
			int textViewResourceId, List<Dictionary> dicts, DictionaryManager dm){
		super(context, textViewResourceId, dicts);
		this.context = context;
		this.dictionaryManager = dm;
	}
	
	public CheckedTextView getView(int position, View convertView, ViewGroup parent){
		CheckedTextView dictionary = null;
		Dictionary dict = getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.dict_item, null);
			dictionary = (CheckedTextView) convertView;
		}else
			dictionary = (CheckedTextView) convertView;
		
		dictionary.setText(dict.getName());
		Log.i("SEARCH IN", dict.isSearchIn() + " " + dictionary.isChecked());
		dictionary.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				CheckedTextView textView = (CheckedTextView) view;
				if (textView.isChecked()){
					dictionaryManager.setSearchInDict(
							textView.getText().toString(), false);
					Log.i("SET DICTIONARY", "SET FALSE");
				}else{
					dictionaryManager.setSearchInDict(
							textView.getText().toString(), true);
					List<Dictionary> dicts = dictionaryManager.getDictionaries();
					for (int i = 0; i < dicts.size(); i++)
						Log.i("DICTIONARY AND IS SEARCH", dicts.get(i).getName() + 
								" " + dicts.get(i).isSearchIn());
					Log.i("SET DICTIONARY", "SET TRUE");
				}
				textView.setChecked(!textView.isChecked());
			}
		});
		
		dictionary.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View view, MotionEvent arg1) {
				CheckedTextView textView = (CheckedTextView) view;
				return false;
			}
			
		});
		return (CheckedTextView)convertView;
	}
	//http://stackoverflow.com/questions/16291459/populate-list-of-custom-view-using-listfragment
	
}
