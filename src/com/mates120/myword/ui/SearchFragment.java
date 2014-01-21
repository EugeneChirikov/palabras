package com.mates120.myword.ui;

import java.util.ArrayList;
import java.util.List;

import com.mates120.myword.AvailableDictionaries;
import com.mates120.myword.R;
import com.mates120.myword.Word;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class SearchFragment extends Fragment{
	
	private AvailableDictionaries availableDictionaries;
	
	private EditText editText;
	private String editContent;
	private static final String editContentKey = "edit_content";
	
	private Button clearButton;
	private WebView resultWebView;
	private String mime = "text/html";
	private String encoding = "utf-8";
	private ListView hintsList;
	private LinearLayout searchLayout;
	private LinearLayout webHorizLayout;
	private HtmlPageComposer htmlPageComposer;
	
	private String text;
	private List<String> hints;
	private static final String hintsContentKey = "hints_content";
	private ArrayAdapter<String> hintsAdapter;
	
	private String webViewContent;
	private static final String webViewContentKey = "web_view_content";
	
	private ActiveViewState activeViewState;
	private static final String activeStateKey = "ACTIVE_VIEW";
	
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		availableDictionaries = AvailableDictionaries.getInstance(this.getActivity());
		htmlPageComposer = new HtmlPageComposer(this.getActivity());
		hintsAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_search_list, container, false);
		searchLayout = (LinearLayout)view.findViewById(R.id.Search);
		webHorizLayout = (LinearLayout)view.findViewById(R.id.webViewLinearLayout);
		editText = (EditText) view.findViewById(R.id.editTextSearch);
		clearButton = (Button) view.findViewById(R.id.clearTextButton);
		clearButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				editText.getText().clear();
			}
		});
		resultWebView = new WebView(getActivity());		
		resultWebView.setLayoutParams(new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		hintsList = new ListView(getActivity());		
		hintsList.setOnItemClickListener(new HintSelectListener());
		editText.setOnEditorActionListener(new EditorActionListener());
		editText.setOnFocusChangeListener(new SearchLineFocusChangeListener());
		editText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showHintsList();
			}
		});
		editText.addTextChangedListener(new EditTextWatcher());
		activeViewState = ActiveViewState.NONE;
		restoreWebViewState(savedInstanceState);
		restoreHintsState(savedInstanceState);
		restoreActiveViewState(savedInstanceState);
		fillLayout();
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		outState.putString(activeStateKey, activeViewState.name());
		outState.putString(webViewContentKey, webViewContent);
		outState.putString(editContentKey, editText.getText().toString());
		outState.putStringArrayList(hintsContentKey, (ArrayList<String>) hints);
	};
	
	class SearchLineFocusChangeListener implements OnFocusChangeListener{
		@Override
		public void onFocusChange(View arg0, boolean hasFocus)
		{
			hideKeyboard();
//			if(hasFocus)
//				showHintsList();
		}
	}

	private void restoreWebViewState(Bundle savedInstanceState)
	{
		if (savedInstanceState == null)
			return;
		webViewContent = savedInstanceState.getString(webViewContentKey);		
		if (webViewContent == null)
			return;		
		loadWebViewContent();
	}

	private void restoreHintsState(Bundle savedInstanceState)
	{
		if (savedInstanceState == null)
			return;
		editContent = savedInstanceState.getString(editContentKey);
		editText.setText(editContent);
		hints = savedInstanceState.getStringArrayList(hintsContentKey);
		if (hints == null)
			return;
		updateHintsAdapterFromHintsList();
	}
	
	private void restoreActiveViewState(Bundle savedInstanceState){
		if (savedInstanceState == null)
			return;
		activeViewState = ActiveViewState.valueOf(
				savedInstanceState.getString(activeStateKey));
	}
	
	private void showHintsList()
	{
		clearSearchLayout();
		clearViewParent(hintsList);
		searchLayout.addView(hintsList);
		activeViewState = ActiveViewState.HINTS;
	}
	
	private void showResultsView()
	{
		clearViewParent(resultWebView);
		if(isInLandscape()){
			webHorizLayout.addView(resultWebView);
		}else{
			clearSearchLayout();
			searchLayout.addView(resultWebView);
		}
		activeViewState = ActiveViewState.WEB;
		hideKeyboard();
	}
	
	private void findAndShowWordDefenition(CharSequence word)
	{
        List<Word> words = null;
        words = availableDictionaries.getWord(word.toString());
        webViewContent = (words.isEmpty())?
        			generateWordNotFoundHtml():convertValuesToHtml(words);
        loadWebViewContent();
        showResultsView();
	}
	
	private void loadWebViewContent()
	{
		resultWebView.loadDataWithBaseURL("file:///android_asset/", webViewContent, mime, encoding, null);
	}

	private String generateWordNotFoundHtml()
	{
		return htmlPageComposer.makeNotFoundPage();
	}
	
	private String convertValuesToHtml(List<Word> words)
	{
		return htmlPageComposer.makePage(words);
	}
	
	class EditorActionListener implements OnEditorActionListener{

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{	    	
	        boolean handled = false;
	        if (actionId != EditorInfo.IME_ACTION_SEARCH)
	        	return handled;	        
        	hideKeyboard();
            findAndShowWordDefenition(editText.getText());
            handled = true;
	        return handled;
	    }		
	}
	
	private void updateHintsAdapterFromHintsList()
	{
		hintsAdapter.clear();
		hintsAdapter.addAll(hints);
		hintsList.setAdapter(hintsAdapter);
	}
	
	private void hideKeyboard()
	{
		InputMethodManager inputMM = (InputMethodManager) 
    			getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMM.hideSoftInputFromWindow(
        		editText.getApplicationWindowToken(), 
        			InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	class EditTextWatcher implements TextWatcher
	{
		@Override
		public void afterTextChanged(Editable s)
		{
			text = editText.getText().toString();
			if (text.length() != 0)
			{
				if (text.equals(editContent)) //hints were updated from bundle
				{
					return;
				}
				GetHintsAsyncTask hintsAsTask = new GetHintsAsyncTask();
				hintsAsTask.execute();
				showHintsList();
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count){		
		}		
	}
	
	class GetHintsAsyncTask extends AsyncTask<Void, 
	Integer, Void>{

		@Override
		protected void onPostExecute(Void result) {
			updateHintsAdapterFromHintsList();
			cancel(true);
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... params) {
			hints = availableDictionaries.getHints(text);
			return null;
		}		
	}

	class HintSelectListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			hideKeyboard();
			TextView textItem = (TextView) arg1;
			findAndShowWordDefenition(textItem.getText());			
		}
	}
	
	private void fillLayout(){
		if (!isInLandscape()){
			if(activeViewState == ActiveViewState.HINTS)
				showHintsList();
			if(activeViewState == ActiveViewState.WEB)
				showResultsView();
			return;
		}
		clearViewParent(hintsList);
		clearViewParent(resultWebView);
		if(hintsList != null)
			searchLayout.addView(hintsList);
		if(resultWebView != null)
			webHorizLayout.addView(resultWebView);
	}
	
	private boolean isInLandscape(){
		DisplayMetrics outMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		int width = outMetrics.widthPixels;
		int height = outMetrics.heightPixels;
		return (width > height);
	}
	
	private void clearViewParent(View view){
		if(view.getParent() != null)
			((LinearLayout)view.getParent()).removeView(view);
	}
	
	private void clearSearchLayout(){
		if (searchLayout.getChildCount() > 1){
			searchLayout.removeViewAt(1);
		}
	}
	
	public void showLastHints(){
		showHintsList();
	}

	public boolean onBackShouldClose() {
		boolean shouldClose = true;
		if(!isInLandscape() && activeViewState == ActiveViewState.WEB){
			showHintsList();
			shouldClose = false;
		}
		return shouldClose;
	}
}
