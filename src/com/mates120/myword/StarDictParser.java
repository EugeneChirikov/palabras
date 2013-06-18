package com.mates120.myword;

import com.mates120.myword.Exceptions.DictionaryParserException;

public class StarDictParser {
	private final String DICT_DATA_PATH = "/storage/sdcard0/dictdata/";
	private Dictionary parsedDict;

	public StarDictParser(){
		this.parsedDict = new Dictionary();
	}

	public Dictionary parseAll() throws DictionaryParserException
	{		
		DictionaryFile ifoFile = new DictionaryFile(DICT_DATA_PATH, ".ifo");
		parseIFO(ifoFile);
		DictionaryFile idxFile = new DictionaryFile(DICT_DATA_PATH, ".idx");
		parseIDX(idxFile);
		String[] dictExtensions = {".dict", ".dict.dz"}; 
		DictionaryFile dictFile = new DictionaryFile(DICT_DATA_PATH, dictExtensions);
		parseDICT(dictFile);
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
