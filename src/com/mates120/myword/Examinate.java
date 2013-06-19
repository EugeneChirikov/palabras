package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Examinate extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examinate);
		DictionaryManager dictionaryManager = new DictionaryManager(getApplicationContext());
		Dictionary testDictionary = new Dictionary("testDict");
		List <String> testWord1Values = new ArrayList<String>();
		testWord1Values.add("w1value1");
		testWord1Values.add("w1value2");
		testWord1Values.add("w1value3");
		testDictionary.addWord("source1", testWord1Values);
		List <String> testWord2Values = new ArrayList<String>();
		testWord2Values.add("w2value1");
		testWord2Values.add("w2value2");
		testWord2Values.add("w2value3");
		testDictionary.addWord("source2", testWord2Values);
		List <String> testWord3Values = new ArrayList<String>();
		testWord3Values.add("w3value1");
		testWord3Values.add("w3value2");
		testWord3Values.add("w3value3");
		testDictionary.addWord("source3", testWord3Values);
		dictionaryManager.addDictionary(testDictionary);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.examinate, menu);
		return true;
	}

}
