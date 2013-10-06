package com.mates120.myword.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mates120.myword.R;
import com.mates120.myword.Word;

public class ResultArrayAdapter extends ArrayAdapter<Word>{
	private Context context;

	public ResultArrayAdapter(Context context, 
			int textViewResourceId, List<Word> words){
		super(context, textViewResourceId, words);
		this.context = context;
	}
	
	private class ViewHolder {
		public TextView value;
		public TextView dictionary;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = null;
		Word word = getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.search_result_item, null);
			holder = new ViewHolder();
			holder.value = (TextView) convertView.findViewById(R.id.valueTextView);
			holder.dictionary = (TextView) convertView.findViewById(R.id.dictTextView);
			
			convertView.setTag(holder);
		}else
			holder = (ViewHolder) convertView.getTag();
		
		holder.value.setText(word.getValue());
		holder.dictionary.setText(word.getDictName());
		return convertView;
	}
}
