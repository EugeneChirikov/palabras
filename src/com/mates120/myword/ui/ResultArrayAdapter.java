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
import com.mates120.myword.Value;

public class ResultArrayAdapter extends ArrayAdapter<Value>{
	private Context context;

	public ResultArrayAdapter(Context context, 
			int textViewResourceId, List<Value> values){
		super(context, textViewResourceId, values);
		this.context = context;
	}
	
	private class ViewHolder {
		public TextView value;
		public TextView dictionary;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = null;
		Value value = getItem(position);
		
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
		
		holder.value.setText(value.getValue());
		holder.dictionary.setText(value.getDictionary());
		return convertView;
	}
}
