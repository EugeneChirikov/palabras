package com.mates120.myword.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable
{
	private boolean isChecked = false;
	
	public CheckableRelativeLayout(Context context, AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public CheckableRelativeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public CheckableRelativeLayout(Context context)
	{
		super(context);
	}

	@Override
	public boolean isChecked()
	{
		return isChecked;
	}

	@Override
	public void setChecked(boolean checked)
	{
		isChecked = checked;
		updateBackground();
	}

	@Override
	public void toggle()
	{
		isChecked = !isChecked;
		updateBackground();
	}
	
	public void updateBackground()
	{
		setActivated(isChecked);
/*		if (isChecked)
			setBackgroundColor(0x33B5E500);
		else
			setBackgroundColor(Color.TRANSPARENT);*/
	}

}
