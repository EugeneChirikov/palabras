package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;


public class DictionaryManager
{
	AvailableDictionaries availableDictionaries;
	ContentResolver contentResolver;
	
	public DictionaryManager(Context context)
	{
		availableDictionaries = new AvailableDictionaries(context);
		contentResolver = context.getContentResolver();
	}
	
	public List<Word> getWord(String wordSource)
	{
		List<Word> foundWords = new ArrayList<Word>();
		Word foundWord = null;
		List<Dictionary> dictList = getDictionaries();
		for (Dictionary d : dictList)
		{
			foundWord = d.getWord(wordSource, contentResolver);
			if (foundWord != null)
				foundWords.add(foundWord);
		}		
		return foundWords;
	}
	
	public List<Dictionary> getDictionaries()
	{		
		return availableDictionaries.getList();
	}

}
