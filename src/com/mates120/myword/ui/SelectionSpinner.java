package com.mates120.myword.ui;

import java.util.ArrayList;
import java.util.List;

import com.mates120.myword.R;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


public class SelectionSpinner
{
	private Spinner selectionSpinner;
	private int itemsSelected;
	private SpinnerAdapter spinnerContent;
	private CharSequence itemsSelectedText;
	private Context myContext;
	private LayoutInflater myInflater;
	private SelectionSpinnerListener myListener = null;
	private int SELECT_ALL_POS = 1;
	private List<Integer> selectedItemsList;

	public SelectionSpinner(FragmentActivity activity)
	{
		myContext = (Context)activity;
		myInflater = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		selectionSpinner = (Spinner)myInflater.inflate(R.layout.selection_spinner, null);
		spinnerContent = new SpinnerAdapter(myContext, myInflater, R.layout.spinner_item);
		spinnerContent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectedItemsList = new ArrayList<Integer>();
		spinnerContent.add("");
		spinnerContent.add(myContext.getString(R.string.select_all));
		dropSelected();
		
		class SelectionListener implements OnItemSelectedListener
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				if (myListener == null)
					return;
				if (position == SELECT_ALL_POS)
				{
					myListener.onAllItemsSelected();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				
			}			
		}

		selectionSpinner.setOnItemSelectedListener(new SelectionListener());
	}
	
	public void setListener(SelectionSpinnerListener sl)
	{
		myListener = sl;
	}

	public View getView()
	{
		return selectionSpinner;
	}

	public List<Integer> getSelectedItemsList()
	{
		return selectedItemsList;
	}
	
	private void refreshItemsSelectedText()
	{
		itemsSelectedText = itemsSelected + " " + myContext.getString(R.string.selected);
		spinnerContent.setTitleText(itemsSelectedText);
		selectionSpinner.setAdapter(spinnerContent);
	}
	
	public void increaseSelected(int position)
	{
		selectedItemsList.add(position);
		itemsSelected++;
		refreshItemsSelectedText();
	}

	public void decreaseSelected(int position)
	{
		selectedItemsList.remove(Integer.valueOf(position));
		itemsSelected--;
		refreshItemsSelectedText();

	}
	
	public void dropSelected()
	{
		itemsSelected = 0;
		refreshItemsSelectedText();
		selectedItemsList.clear();
	}
}
