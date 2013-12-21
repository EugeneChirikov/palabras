package com.mates120.myword.ui;

import java.util.List;

import com.mates120.myword.AvailableDictionaries;
import com.mates120.myword.R;
import com.mates120.myword.Word;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * A fragment representing a list of Items.
 */
public class SearchFragment extends ListFragment{
	
	private AvailableDictionaries availableDictionaries;
	private EditText editText;
	private TextView wordTextView;
	private String wordTextViewValue = "";
	private View lineView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		availableDictionaries = AvailableDictionaries.getInstance(this.getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search_list, container, false);
		editText = (EditText) view.findViewById(R.id.editTextSearch);
		wordTextView = (TextView) view.findViewById(R.id.resWordTextView);
		lineView = (View) view.findViewById(R.id.line);
		lineView.setVisibility(View.INVISIBLE);
		wordTextView.setText(wordTextViewValue);
		editText.setOnEditorActionListener(new EditorActionListener());
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
		editText.addTextChangedListener(new EditTextWatcher());
		return view;
	}
	
	class EditorActionListener implements OnEditorActionListener{

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			ResultArrayAdapter adapter;
	    	List<Word> words = null;
	        boolean handled = false;
	        if (actionId != EditorInfo.IME_ACTION_SEARCH)
	        	return handled;
	        
        	InputMethodManager inputMM = (InputMethodManager) 
        			getActivity().getSystemService(
        					Context.INPUT_METHOD_SERVICE);
            inputMM.hideSoftInputFromWindow(
            		editText.getApplicationWindowToken(), 
            			InputMethodManager.HIDE_NOT_ALWAYS);
            words = availableDictionaries.getWord(editText.getText().toString());
            if(words.isEmpty())
            {
            	wordTextView.setText("No such word in any of dictionaries");
            	lineView.setVisibility(View.INVISIBLE);
            	setListAdapter(null);
            }
            else
            {
            	editText.getText().clear();
            	wordTextViewValue = words.get(0).getSource();
            	wordTextView.setText(wordTextViewValue);
            	lineView.setVisibility(View.VISIBLE);	            	
            	adapter = new ResultArrayAdapter(getActivity(),
	        			android.R.id.list, words);
	        	setListAdapter(adapter);
	        }		          
            handled = true;
	        return handled;
	    }		
	}
	
	class EditTextWatcher implements TextWatcher{
		
		@Override
		public void afterTextChanged(Editable s) {
			List<String> hints;
			ArrayAdapter<String> hintsAdapter;
			wordTextView.setHeight(0);
			hints = availableDictionaries.getHints(editText.getText().toString());
			hintsAdapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, hints);
        	setListAdapter(hintsAdapter);
        	getListView().setOnItemClickListener(new HintSelectListener());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
/*			List<String> hints;
			ArrayAdapter<String> hintsAdapter;
			wordTextView.setHeight(0);
			hints = availableDictionaries.getHints(editText.getText().toString());
			hintsAdapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, hints);
        	setListAdapter(hintsAdapter);
        	getListView().setOnItemClickListener(new HintSelectListener());*/
		}
		
	}
	
	class HintSelectListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			TextView textItem = (TextView) arg1;
			editText.setText(textItem.getText());
		}
		
	}
}
