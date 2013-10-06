package com.mates120.myword.ui;

import java.util.ArrayList;
import java.util.List;

import com.mates120.myword.DictionaryManager;
import com.mates120.myword.R;
import com.mates120.myword.Word;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * A fragment representing a list of Items.
 */
public class SearchFragment extends ListFragment{
	
	private DictionaryManager dictionaryManager;
	private EditText editText;
	private TextView wordTextView;
	private String wordTextViewValue = "";
	private View lineView;
	private int lineViewVisible = View.INVISIBLE;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		dictionaryManager = new DictionaryManager(this.getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search_list, container, false);
		editText = (EditText) view.findViewById(R.id.editTextSearch);
		wordTextView = (TextView) view.findViewById(R.id.resWordTextView);
		lineView = (View) view.findViewById(R.id.line);
		lineView.setVisibility(lineViewVisible);
		wordTextView.setText(wordTextViewValue);
		editText.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
		    	ResultArrayAdapter adapter;
		    	List<String> values = null;
		    	List<Word> words = null;
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_SEARCH)
		        {
		        	InputMethodManager inputMM = (InputMethodManager) 
		        			getActivity().getSystemService(
		        					Context.INPUT_METHOD_SERVICE);
	                inputMM.hideSoftInputFromWindow(
	                		editText.getApplicationWindowToken(), 
	                			InputMethodManager.HIDE_NOT_ALWAYS);
		            words = dictionaryManager.getWord(editText.getText().toString());
		            if(words.size() > 0) {
		            	editText.getText().clear();
		            	wordTextViewValue = editText.getText().toString();
		            	wordTextView.setText(wordTextViewValue);
		            	lineViewVisible = View.VISIBLE;
		            	lineView.setVisibility(lineViewVisible);
		            	values = new ArrayList<String>();
		            	for(Word word : words)
		            		values.add(word.getValue());
		            	adapter = new ResultArrayAdapter(getActivity(),
			        			android.R.id.list, values);
			        	setListAdapter(adapter);
			        }
		            else
			        	wordTextView.setText("No such word in the dictionary.");
		            handled = true;
		        }
		        return handled;
		    }
		});
		editText.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				InputMethodManager inputMM = (InputMethodManager) 
						getActivity().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputMM.hideSoftInputFromWindow(
							editText.getApplicationWindowToken(), 
                				InputMethodManager.HIDE_NOT_ALWAYS);
			}
			
		});
		return view;
	}	
}
