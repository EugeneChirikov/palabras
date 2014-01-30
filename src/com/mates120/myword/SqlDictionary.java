package com.mates120.myword;

import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class SqlDictionary extends Dictionary
{	
	private final String[] wordsProjection = {"_id", "source", "value"};
	private final String[] hintsProjection = {"source"};
	private final String wordsSelection =  "source = ?";
	private final String hintsSelection =  "source like ?";
	
	public SqlDictionary(DictionarySpec spec, long id, String name, boolean active, ContentResolver cr)
	{
		super(spec, id, name, active, cr);
	}
	
	public Word getWord(String wordSource)
	{
		Word foundWord = null;
		Uri uri = spec.getProviderUri("words");
		String[] selectionArgs = new String[]{wordSource};
		Cursor cursor = cr.query(uri, wordsProjection, wordsSelection, selectionArgs, null);
		cursor.moveToFirst();
		foundWord = cursorToWord(cursor);
		cursor.close();
		return foundWord;
	}
	
	public List<String> getHints(String startCharacters)
	{
		hints.clear();
		String hint;
		Uri uri = spec.getProviderUri("words/hints");
		String[] selectionArgs = {startCharacters + "%"};
		Cursor cursor = cr.query(uri, hintsProjection, hintsSelection, selectionArgs, "source ASC");
		cursor.moveToFirst();
		while(cursor != null && cursor.getCount() > 0)
		{
			hint = cursor.getString(0);
			hints.add(hint);
			if(!cursor.moveToNext())
				break;
		}
		cursor.close();
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
