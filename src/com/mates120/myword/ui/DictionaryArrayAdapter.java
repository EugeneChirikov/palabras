package com.mates120.myword.ui;

import java.util.List;

import com.mates120.myword.Dictionary;
import com.mates120.myword.DictionaryManager;
import com.mates120.myword.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class DictionaryArrayAdapter extends ArrayAdapter<Dictionary>{
	
	private Context context;
	private CheckedTextView dictionary;
	private DictionaryManager dictManager;
	
	public DictionaryArrayAdapter(Context context, 
			int textViewResourceId, List<Dictionary> dicts){
		super(context, textViewResourceId, dicts);
		this.context = context;
	}
	
	private class ViewHolder {
		public CheckedTextView dictionary;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = null;
		Dictionary dict = getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.dict_item, null);
			holder = new ViewHolder();
			holder.dictionary = (CheckedTextView) convertView.findViewById(R.id.dictionary);
			
			convertView.setTag(holder);
		}else
			holder = (ViewHolder) convertView.getTag();
		
		holder.dictionary.setText(dict.getName());
		holder.dictionary.setChecked(dict.isSearchIn());
		addListenerOnSearchIn(convertView);
		return convertView;
	}
	//http://stackoverflow.com/questions/16291459/populate-list-of-custom-view-using-listfragment
	
	public void addListenerOnSearchIn(View currentView){
		dictionary = (CheckedTextView) currentView.findViewById(R.id.dictionary);
		
		dictionary.setOnClickListener(new OnClickListener(){
			private DictionaryManager dictManager;
			
			@Override
			public void onClick(View v) {
				if (((CheckedTextView) v).isChecked()){
					dictManager = new DictionaryManager(getContext());
					dictManager.setSearchInDict(
							((CheckedTextView) v).getText().toString(), true);
				}
			}
			
		});
	}
}
