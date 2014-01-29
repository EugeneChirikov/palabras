package com.mates120.myword;

import android.content.ContentResolver;

public class DictsFactory
{
	private ContentResolver contentResolver;

	public DictsFactory(ContentResolver cr)
	{
		contentResolver = cr;
	}
	
	public Dictionary createDictionary(DictionarySpec spec, long id, String name, boolean active, String type)
	{
		Dictionary newDict = null;
		if (type.equals("sql"))
			newDict = new SqlDictionary(spec, id, name, active, contentResolver);
		if (type.equals("stardict"))
			newDict = new StardictDictionary(spec, id, name, active, contentResolver);
		return newDict;
	}
	
	public String determineType(Dictionary dictObj)
	{
		if (dictObj instanceof SqlDictionary)
			return "sql";
		if (dictObj instanceof StardictDictionary)
			return "stardict";
		return null;
	}
}
