package com.mates120.myword;

import java.util.List;

import android.content.Context;

public class DictionaryManager {
	private DataSource dataSource;
	
	public DictionaryManager(Context context){
		dataSource = new DataSource(context);
	}
	
	public void addDictionary(Dictionary dictionary ){
		dataSource.open();
		for (int i = 0; i < dictionary.getWords().size(); i ++){
			addWord(dictionary.getWord(i).getSource(), 
					dictionary.getWord(i).getValues(), 
					dictionary.getName());
		}
		dataSource.close();
	}
	
	public void deleteDictionary(String name){
		long dictId;
		dictId = dataSource.deleteDictionary(name);
	}
	
	/*Func to add single word with single value. Now not usable.
	 * 
	 * private void addWord(String wordSource, String value_source, String dictName){
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
	
	private void addWord(String wordSource, List<Value> values, String dictName){
		long dictId;
		long wordId;
		long valueIds[];
		dictId = dataSource.insertDictionary(dictName);
		valueIds = insertValues(values, dictId);
		Word existWord = dataSource.getWordBySource(wordSource);
		if(!existWord.equals(null)){
			wordId = existWord.getId();
		} else {
			wordId = dataSource.insertWord(wordSource);
		}
		dataSource.insertWord(wordSource);
		createWordLinks(wordId, valueIds);
	}
	
	private long[] insertValues(List<Value> values, long dictId){
		long valueId;
		long valueIds[] = new long[values.size()];
		for (int i = 0; i < values.size(); i++){
			valueId = dataSource.insertValue(values.get(i).getValue(), dictId);
			valueIds[i] = valueId;
		}
		return valueIds;
	}
	
	private void createWordLinks(long wordId, long[] valueIds){
		for (int i = 0; i < valueIds.length; i ++)
			dataSource.createLink(wordId, valueIds[i]);
	}
	
	public Word getWord(String wordSource){
		Word word = new Word();
		return word;
	}
}
