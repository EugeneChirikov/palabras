package com.mates120.myword;

public class StarDictParser {
	private final String DICT_DATA_PATH = "/sdcard/data/dictdata/";
	private Dictionary parsedDict;

	public StarDictParser(){
		this.parsedDict = new Dictionary();
	}

	public Dictionary parseAll()
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
	
	private void parseIFO(DictionaryFile ifoFile){
		
	}
	
	private void parseIDX(DictionaryFile idxFile){
		
	}
	
	private void parseDICT(DictionaryFile dictFile){
		
	}
}
