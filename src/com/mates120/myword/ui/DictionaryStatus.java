package com.mates120.myword.ui;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.mates120.myword.R;

public class DictionaryStatus
{
	private TextView dictStatus;
	private Context myContext;
	private String on;
	private String off;
	
	DictionaryStatus(Context context, TextView dictStatus)
	{
		myContext = context;
		this.dictStatus = dictStatus;
		on = myContext.getString(R.string.status_on);
		off = myContext.getString(R.string.status_off);
	}
	
	boolean isActive()
	{
		return dictStatus.getText().toString().equals(on);
	}
	
	void setStatus(boolean active)
	{
		if (active)
		{
			dictStatus.setText(on);
			dictStatus.setTextColor(Color.rgb(41, 163, 204));
		}
		else
		{
			dictStatus.setText(off);
			dictStatus.setTextColor(Color.rgb(130, 134, 136));
		}
	}
	
	void toggle()
	{
		setStatus(!isActive());
	}
}
