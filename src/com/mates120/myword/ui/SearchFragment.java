package com.mates120.myword.ui;

import java.util.List;

import com.mates120.myword.AvailableDictionaries;
import com.mates120.myword.R;
import com.mates120.myword.Word;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * A fragment representing a list of Items.
 *
 *           
 *       
 */

/*<EditText
android:id="@+id/editTextSearch"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:hint="@string/enter_word_for_search"
android:imeOptions="actionSearch"
android:inputType="text" >
<requestFocus />
</EditText>

<ListView
android:id="@android:id/list"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:dividerHeight="3dp"
android:showDividers="middle" >
</ListView>        

<?xml version="1.0" encoding="utf-8"?>
<TableLayout xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:tools="http://schemas.android.com/tools"
android:id="@+id/Settings"
android:layout_width="fill_parent"
android:layout_height="match_parent"
android:shrinkColumns="1"
android:stretchColumns="*"
tools:context=".SearchFragment" >*/

public class SearchFragment extends Fragment
{	
	private AvailableDictionaries availableDictionaries;
	private EditText editText;
	private WebView resultWebView;
	private String mime = "text/html";
	private String encoding = "utf-8";
	private ListView hintsList;
	private LinearLayout searchLayout;
	private boolean hintsShown = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		availableDictionaries = AvailableDictionaries.getInstance(this.getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{		
		View view = inflater.inflate(R.layout.fragment_search_list, container, false);
		searchLayout = (LinearLayout)view.findViewById(R.id.Search);		
		editText = (EditText) view.findViewById(R.id.editTextSearch);
		
		resultWebView = new WebView(getActivity());
		hintsList = new ListView(getActivity());
		
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
	
	private void showHintsList()
	{
		if (hintsShown == true)
			return;
		searchLayout.removeView(resultWebView);
		searchLayout.addView(hintsList);
		hintsShown = true;
	}
	
	private void showResultsView()
	{
		if (hintsShown == false)
			return;
		searchLayout.removeView(hintsList);
		searchLayout.addView(resultWebView);
		hintsShown = false;
	}
	
	private void findAndShowWordDefenition()
	{
        List<Word> words = null;
        words = availableDictionaries.getWord(editText.getText().toString());
        if(words.isEmpty())
        {
        	Word emptyWord = new Word("","No such word in any of dictionaries","");
        	words.add(emptyWord);
        }
        else
        {
        	editText.getText().clear();
        }
//        resultWebView.setVisibility(View.VISIBLE);
        resultWebView.loadDataWithBaseURL(null, convertValuesToHtml(words), mime, encoding, null);
        showResultsView();
	}
	
	private String convertValuesToHtml(List<Word> words)
	{
		HtmlPageComposer pc = new HtmlPageComposer();		
		return pc.makePage(words);
	}
	
	class EditorActionListener implements OnEditorActionListener{

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{	    	
	        boolean handled = false;
	        if (actionId != EditorInfo.IME_ACTION_SEARCH)
	        	return handled;
	        
        	InputMethodManager inputMM = (InputMethodManager) 
        			getActivity().getSystemService(
        					Context.INPUT_METHOD_SERVICE);
            inputMM.hideSoftInputFromWindow(
            		editText.getApplicationWindowToken(), 
            			InputMethodManager.HIDE_NOT_ALWAYS);            
            findAndShowWordDefenition();
            handled = true;
	        return handled;
	    }		
	}
	
	class EditTextWatcher implements TextWatcher{
		
		@Override
		public void afterTextChanged(Editable s) {
			List<String> hints;
			ArrayAdapter<String> hintsAdapter;
			hints = availableDictionaries.getHints(editText.getText().toString());
			hintsAdapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, hints);
			hintsList.setAdapter(hintsAdapter);
			hintsList.setOnItemClickListener(new HintSelectListener());
			showHintsList();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {}		
	}
	
	class HintSelectListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{			
			findAndShowWordDefenition();
		}
	}
}
