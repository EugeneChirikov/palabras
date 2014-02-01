package com.mates120.myword.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class SpinnerAdapter extends ArrayAdapter<CharSequence>
{
	private int resourceId;
	CharSequence titleText;
	LayoutInflater myInflater;
	Context myContext;

	public SpinnerAdapter(Context context, LayoutInflater inflater, int resource)
	{
		super(context, resource);
		resourceId = resource;
		myInflater = inflater;
		titleText = "";
		myContext = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
			convertView = myInflater.inflate(resourceId, parent, false);
        TextView tv = (TextView) convertView;
        tv.setText(titleText);
		return convertView;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		View v = null;
        if (position == 0)
        {
            TextView tv = new TextView(myContext);
            tv.setHeight(0);
            v = tv;
        }
        else
            v = super.getDropDownView(position, null, parent);
        return v;
	};
	
	public void setTitleText(CharSequence text)
	{
		titleText = text;
	}
}