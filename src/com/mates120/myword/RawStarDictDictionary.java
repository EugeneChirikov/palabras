package com.mates120.myword;

import java.io.InputStream;

public class RawStarDictDictionary extends RawDictionary {
	public InputStream ifoFile;
	public InputStream idxFile;
	public InputStream dictFile;

	public RawStarDictDictionary(InputStream ifoFile, InputStream idxFile, InputStream dictFile)
	{
		this.ifoFile = ifoFile;
		this.idxFile = idxFile;
		this.dictFile = dictFile;
	}
}
