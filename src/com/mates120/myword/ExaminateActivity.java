package com.mates120.myword;

import com.mates120.myword.Exceptions.DictionaryParserException;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ExaminateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examinate);
		StarDictParser parser = new StarDictParser();
		
		try {
			System.out.println("Parsing...");
			parser.parseAll();
			System.out.println("...Done");
		} catch (DictionaryParserException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.examinate, menu);
		return true;
	}

}
