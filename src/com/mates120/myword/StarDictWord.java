package com.mates120.myword;

public class StarDictWord
{
//	public static boolean extendedOffset;
	private Word word;
	private byte [] dataOffset;
	private byte [] dataSize;
	
	public StarDictWord(String wordString)
	{
		word = new Word(wordString);
	}
	
	public void addDataOffset(byte [] dataOffset)
	{
		this.dataOffset = dataOffset;
	}
	
	public void addDataSize(byte [] dataSize)
	{
		this.dataSize = dataSize;
	}
	
	public String getWordString()
	{
		return word.getSource();
	}
}