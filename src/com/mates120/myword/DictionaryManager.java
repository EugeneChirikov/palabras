package com.mates120.myword;

import java.util.List;

import android.content.Context;

public class DictionaryManager {
	private DataSource dataSource;
	
	public DictionaryManager(Context context){
		dataSource = new DataSource(context);
	}
	
	public void addDictionary(Dictionary dictionary ){
		
	}
	
	public void addWord(String word_source, String value_source, String tag){
		dataSource.open();
		dataSource.createValue(value_source, tag);
		
	}
	
	public void addWord(String word_source, List<String> values, String tag){
			dataSource.open();
			dataSource.createValue(values.get(0), tag);
			
		}
	
	public Word getWord(String word_source){
		Word word = new Word();
		return word;
	}
}
