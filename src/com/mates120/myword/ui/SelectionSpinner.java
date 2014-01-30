package com.mates120.myword.ui;

import com.mates120.myword.R;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SelectionSpinner
{
	private Spinner selectionSpinner;
	private int itemsSelected;
	private ArrayAdapter<CharSequence> spinnerContent;
	private CharSequence itemsSelectedText;
	private Context myContext;

	public SelectionSpinner(FragmentActivity activity)
	{
		myContext = (Context)activity;
		LayoutInflater inflater = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		selectionSpinner = (Spinner)inflater.inflate(R.layout.selection_spinner, null);
		spinnerContent = new ArrayAdapter<CharSequence>(myContext, android.R.layout.simple_spinner_item);
		spinnerContent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerContent.add(itemsSelectedText);
		spinnerContent.add(myContext.getString(R.string.select_all));
		dropSelected();
	}

	public View getView()
	{
		return selectionSpinner;
	}

	private void refreshItemsSelectedText()
	{
		itemsSelectedText = itemsSelected + myContext.getString(R.string.selected);
		spinnerContent.clear();
		spinnerContent.add(itemsSelectedText);
		spinnerContent.add(myContext.getString(R.string.select_all));
		selectionSpinner.setAdapter(spinnerContent);
	}
	
	public void increaseSelected()
	{
		itemsSelected++;
		refreshItemsSelectedText();
	}

	public void decreaseSelected()
	{
		itemsSelected--;
		refreshItemsSelectedText();
	}
	
	public void dropSelected()
	{
		itemsSelected = 0;
		refreshItemsSelectedText();
	}
}
