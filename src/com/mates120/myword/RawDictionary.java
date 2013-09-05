package com.mates120.myword;

public class RawDictionary {
	protected Dictionary parsedDict;

	RawDictionary()
	{
		parsedDict = null;
	}
	
	public Dictionary parseAll()
	{
		return parsedDict;
	}
}
