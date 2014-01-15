package com.mates120.myword.ui;

import java.util.ArrayList;
import java.util.List;

import com.mates120.myword.AvailableDictionaries;
import com.mates120.myword.Dictionary;
import com.mates120.myword.R;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class SettingsFragment extends ListFragment
{	
	private AvailableDictionaries availableDictionaries;
	private List<Dictionary> dicts;
	private DictionaryArrayAdapter mAdapter;
	private Handler uiThreadHandler;
	private MultiChoiceModeListener dictsListListener;
	private List<Integer> dictsToDelete;
	private int itemsSelected;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		uiThreadHandler = new Handler();
		mAdapter = new DictionaryArrayAdapter(getActivity(), android.R.id.list);
		availableDictionaries = AvailableDictionaries.getInstance(this.getActivity());
		availableDictionaries.subscribeSettingsFragment(this);
		dictsListListener = createDictsListListener();
		dictsToDelete = new ArrayList<Integer>();
		itemsSelected = 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_settings, container, false);
		return view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		new RefreshDictionariesListTask(getActivity());
	};

	public void onDictionariesRefresh()
	{
		dicts = availableDictionaries.getList();
		uiThreadHandler.post(refreshDictsList());
	}
	
	private Runnable refreshDictsList()
	{
		return new Runnable()
		{
			@Override
			public void run()
			{				
				mAdapter.clear();
				mAdapter.addAll(dicts);
				setListAdapter(mAdapter);
//				installContextMenuForDictsList();
			}
		};
	}
	
	private MultiChoiceModeListener createDictsListListener()
	{
		return new MultiChoiceModeListener()
		{

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item)
			{
		        switch (item.getItemId())
		        {

	            	case R.id.dictlist_delete:
	            		itemsSelected = 0;
	            		dictsToDelete.clear();
	            		mode.finish(); // Action picked, so close the CAB
	            		return true;
	            	default:
	            		return false;
		        }
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		        MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.dictlist_actions, menu);
//		        getListView().setEnabled(false);
		        return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
//				getListView().setEnabled(true);				
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
				if (checked)
				{
					dictsToDelete.add(position);
					itemsSelected++;
				} 
				else
				{
					dictsToDelete.remove(position);
					itemsSelected--;
				}
				mode.setTitle(itemsSelected + " selected");				
			}};
	}
	
	private void installContextMenuForDictsList()
	{
		ListView listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(dictsListListener);
	}
	
	@Override
    public void onListItemClick(ListView l, View view, int position, long id) {
		CheckedTextView textView = (CheckedTextView) view;
		if (textView.isChecked())
		{			
			availableDictionaries.setDictionaryActive(textView.getText().toString(), false);
			Log.i("SET DICTIONARY", "SET FALSE");
			textView.setChecked(false);
		}
		else
		{
			availableDictionaries.setDictionaryActive(textView.getText().toString(), true);
			Log.i("SET DICTIONARY", "SET TRUE");
			textView.setChecked(true);
		}
	}
}