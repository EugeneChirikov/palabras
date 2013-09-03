package com.mates120.myword.ui;

import java.util.List;

import com.mates120.myword.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class DictionaryArrayAdapter extends ArrayAdapter<String>{
	
	Context context;
	
	public DictionaryArrayAdapter(Context context, 
			int textViewResourceId, List<String> dictNames){
		super(context, textViewResourceId, dictNames);
		this.context = context;
	}
	
	private class ViewHolder {
		TextView dictName;
		CheckBox searchIn;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = null;
		String dictName = getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.dict_item, null);
			holder = new ViewHolder();
			holder.dictName = (TextView) convertView.findViewById(R.id.dictName);
			holder.searchIn = (CheckBox) convertView.findViewById(R.id.searchIn);
			convertView.setTag(holder);
		}else
			holder = (ViewHolder) convertView.getTag();
		
		holder.dictName.setText(dictName);
		holder.searchIn.setChecked(true);
		
		return convertView;
	}
	//TODO: http://stackoverflow.com/questions/16291459/populate-list-of-custom-view-using-listfragment

}
