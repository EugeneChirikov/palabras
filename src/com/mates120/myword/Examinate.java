package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Examinate extends Activity {
	
	//Only for testing.
	private DictionaryManager dictionaryManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examinate);
		dictionaryManager = new DictionaryManager(getApplicationContext());
		test1DictionaryAdd();
		test2DictionaryAdd();
		test1DictionaryDelete();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.examinate, menu);
		return true;
	}
	
	/*
	 * Test function to add test1Dict dictionary.
	 */
	private void test1DictionaryAdd(){
		Dictionary testDictionary = new Dictionary("test1Dict");
		List <String> testWord1Values = new ArrayList<String>();
		testWord1Values.add("d1w1value1");
		testWord1Values.add("d1w1value2");
		testWord1Values.add("d1w1value3");
		testDictionary.addWord("d1source1unique", testWord1Values);
		List <String> testWord2Values = new ArrayList<String>();
		testWord2Values.add("d1w2value1");
		testWord2Values.add("d1w2value2");
		testWord2Values.add("d1w2value3");
		testDictionary.addWord("source2everywhere", testWord2Values);
		List <String> testWord3Values = new ArrayList<String>();
		testWord3Values.add("d1w3value1");
		testWord3Values.add("d1w3value2");
		testWord3Values.add("d1w3value3");
		testDictionary.addWord("d1source3unique", testWord3Values);
		dictionaryManager.addDictionary(testDictionary);
	}
	
	private void test2DictionaryAdd(){
		Dictionary testDictionary = new Dictionary("test2Dict");
		List <String> testWord1Values = new ArrayList<String>();
		testWord1Values.add("d2w1value1");
		testWord1Values.add("d2w1value2");
		testWord1Values.add("d2w1value3");
		testDictionary.addWord("d2source1unique", testWord1Values);
		List <String> testWord2Values = new ArrayList<String>();
		testWord2Values.add("d2w2value1");
		testWord2Values.add("d2w2value2");
		testWord2Values.add("d2w2value3");
		testDictionary.addWord("source2everywhere", testWord2Values);
		List <String> testWord3Values = new ArrayList<String>();
		testWord3Values.add("d2w3value1");
		testWord3Values.add("d2w3value2");
		testWord3Values.add("d2w3value3");
		testDictionary.addWord("d2source3unique", testWord3Values);
		dictionaryManager.addDictionary(testDictionary);
	}
	
	private void test1DictionaryDelete(){
		dictionaryManager.deleteDictionary("test1Dict");
	}

}
