package com.mates120.myword;

import com.mates120.myword.Exceptions.DictionaryParserException;

public class StarDictParser extends DictionaryParser {

	public Dictionary parseAll()
	{
		
		return null;
	}
	
	private void parseIFO(DictionaryFile ifoFile) throws DictionaryParserException{
		System.out.println(ifoFile.getValidPathToFile());
	}
	
	private void parseIDX(DictionaryFile idxFile) throws DictionaryParserException{
		System.out.println(idxFile.getValidPathToFile());
	}
	
	private void parseDICT(DictionaryFile dictFile) throws DictionaryParserException{
		System.out.println(dictFile.getValidPathToFile());
	}
}
