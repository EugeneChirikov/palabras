package com.mates120.myword;

import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;

public class StardictDictionary extends Dictionary
{
	public StardictDictionary(DictionarySpec spec, long id, String name, boolean active, ContentResolver cr)
	{
		super(spec, id, name, active, cr);
	}

	public Word getWord(String wordSource)
	{
		Word foundWord = null;
	
		return foundWord;
	}

	public List<String> getHints(String startCharacters)
	{
		hints.clear();
	
		return hints;
	}

	private Word cursorToWord(Cursor cursor)
	{
		Word sWord = null;
		if(cursor != null && cursor.getCount() > 0)	
			sWord = new Word(cursor.getString(1),cursor.getString(2), name);
		return sWord;
	}
}
