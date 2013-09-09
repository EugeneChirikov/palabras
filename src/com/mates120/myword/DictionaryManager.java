package com.mates120.myword;

import java.util.List;

import android.content.Context;
import android.util.Log;

public class DictionaryManager {
	
	private DataSource dataSource;
	
	public DictionaryManager(Context context){
		dataSource = new DataSource(context);
	}
	
	public void addDictionary(Dictionary dictionary ){
		dataSource.open();
		if(!dataSource.dictionaryInDB(dictionary.getName()))
			for (int i = 0; i < dictionary.getWords().size(); i ++){
				addWord(dictionary.getWord(i).getSource(), 
						dictionary.getWord(i).getValues(), 
						dictionary.getName());
			}
		dataSource.close();
	}
	
	public void deleteDictionary(String name){
		long valuesIds[];
		dataSource.open();
		valuesIds = dataSource.getDictionaryValuesIds(name);
		deleteLinksOfValues(valuesIds);
		dataSource.deleteDictionaryValues(name);
		dataSource.deleteDictionaryByName(name);
		dataSource.close();
	}
	
	private void addWord(String wordSource, List<String> values, String dictName){
		long wordId;
		long valueIds[];
		if(!dataSource.dictionaryInDB(dictName))
			dataSource.insertDictionary(dictName);
		valueIds = insertValues(values, dictName);
		Word existWord = dataSource.getWord(wordSource);
		if(existWord != null){
			wordId = existWord.getId();
		} else {
			wordId = dataSource.insertWord(wordSource);
		}
		createWordLinks(wordId, valueIds);
	}
	
	private long[] insertValues(List<String> values, String dictionaryName){
		long valueIds[] = new long[values.size()];
		for (int i = 0; i < values.size(); i++)
			valueIds[i] = dataSource.insertValue(values.get(i), dictionaryName);
		return valueIds;
	}
	
	private void createWordLinks(long wordId, long[] valueIds){
		for (int i = 0; i < valueIds.length; i ++)
			dataSource.createLink(wordId, valueIds[i]);
	}
	
	private void deleteLinksOfValues(long valuesIds[]){
		long wordId;
		for(int i = 0; i < valuesIds.length; i++){
			wordId = dataSource.getWordIdByValueId(valuesIds[i]);
			if(dataSource.wordHasOneValue(wordId))
				dataSource.deleteWordById(wordId);
			dataSource.deleteLinkByValue(valuesIds[i]);
		}
	}
	
	public Word getWord(String wordSource){
		dataSource.open();
		Word word = null;
		word = dataSource.getWord(wordSource);
		if(word != null)
			word.setValues(dataSource.getWordValues(word.getId()));
		dataSource.close();
		return word;
	}
	
	public List<Dictionary> getDictionaries(){
		dataSource.open();
		List<Dictionary> dicts = dataSource.getAllDictionaries();
		dataSource.close();
		return dicts;
	}
	
	public void setSearchInDict(String dictName, boolean searchIn){
		dataSource.open();
		Log.i("SETSEARCH IN DICT", String.valueOf(dataSource.setSearchInDict(dictName, searchIn)));
		dataSource.close();
	}
}
