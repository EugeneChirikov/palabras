package com.mates120.myword.ui;

import com.mates120.myword.Dictionary;
import com.mates120.myword.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DictionaryArrayAdapter extends ArrayAdapter<Dictionary>{
	
	private Context context;

	public DictionaryArrayAdapter(Context context, int textViewResourceId)
	{
		super(context, textViewResourceId);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{		
		Dictionary dict = getItem(position);
		
		if(convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater)context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.dict_item, null);
		}
		TextView dictName = (TextView)convertView.findViewById(R.id.dict_name);
		TextView dictStatusText = (TextView)convertView.findViewById(R.id.dict_status);
		DictionaryStatus dictStatus = new DictionaryStatus(context, dictStatusText);
		dictName.setText(dict.getName());
		dictStatus.setStatus(dict.isActive());
//		dictStatus = null;
		return convertView;
	}
}
