package com.mates120.myword;

import java.util.List;

import android.content.Context;

public class DictionaryManager {
	private DataSource dataSource;
	
	public DictionaryManager(Context context){
		dataSource = new DataSource(context);
	}
	
	public void addDictionary(Dictionary dictionary ){
		for (int i = 0; i < dictionary.getWords().size(); i ++){
			addWord(dictionary.getWord(i).getSource(), 
					dictionary.getWord(i).getValues(), 
					dictionary.getName());
		}
	}
	
	public void deleteDictionary(String name){
		long dictId;
		dictId = dataSource.deleteDictionary(name);
	}
	
	/*Func to add single word with single value. Now not usable.
	 * 
	 * private void addWord(String word_source, String value_source, String dictName){
		long dictId;
		long valueId;
		long wordId;
		dataSource.open();
		dictId = dataSource.insertDictionary(dictName);
		valueId = dataSource.insertValue(value_source, dictId);
		wordId = dataSource.insertWord(word_source);
		dataSource.createLink(wordId, valueId);
		dataSource.close();
		
	}*/
	
	private void addWord(String word_source, List<Value> values, String dictName){
		long dictId;
		long valueId;
		long valueIds[] = new long[values.size()];
		long wordId;
		dataSource.open();
		dictId = dataSource.insertDictionary(dictName);
		for (int i = 0; i < values.size(); i++){
			valueId = dataSource.insertValue(values.get(i).getValue(), dictId);
			valueIds[i] = valueId;
		}
		wordId = dataSource.insertWord(word_source);
		for (int i = 0; i < valueIds.length; i ++){
			dataSource.createLink(wordId, valueIds[i]);
		}
		dataSource.close();
	}
	
	public Word getWord(String word_source){
		Word word = new Word();
		return word;
	}
}
