package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.TextView.OnEditorActionListener;

public class ActivitySearch extends ListActivity {
	
	private DictionaryManager dictionaryManager;
	private EditText editText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		List<Value> valuesToShow = new ArrayList<Value>();
		dictionaryManager = new DictionaryManager(this);
		editText = (EditText) findViewById(R.id.editTextSearch);
		
		ArrayAdapter<Value> adapter = new ArrayAdapter<Value>(this,
		        android.R.layout.simple_list_item_1, valuesToShow);
		setListAdapter(adapter);
		editText.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
		    	@SuppressWarnings("unchecked")
		    	ArrayAdapter<Value> adapter = (ArrayAdapter<Value>) getListAdapter();
		    	adapter.clear();
		        Word word = null;
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		            word = dictionaryManager.getWord(editText.getText().toString());
		            if(word != null){
		            	System.out.println("Values number: " + word.getValues().size());
		            	for(int i = 0; i < word.getValues().size(); i++){
		            		System.out.println(word.getValue(i));
		            		adapter.add(word.getValue(i));
		            		adapter.notifyDataSetChanged();
		            	}
		            }
		            else
		            	Toast.makeText(getBaseContext(), R.string.no_such_word_in_db, Toast.LENGTH_LONG).show();
		            handled = true;
		        }
		        return handled;
		    }
		});
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	}

	@Override
	protected void onPause() {
	    super.onPause();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
