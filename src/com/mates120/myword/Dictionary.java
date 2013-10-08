package com.mates120.myword;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class Dictionary
{
	private final String PACKAGE = "com.mates120.dictionary.";
	private long id;
	private String name;
	private String app;
	private boolean isActive;
	
	private final String[] projection = new String[]{"_id", "source", "value"};
	private final String selection =  "source = ?";
	
	public Dictionary(){}
	
	public Dictionary(String dictApp, ContentResolver cr){
		app = dictApp;
		Uri uri = getProviderUri("words/create");
		Cursor cursor = cr.query(uri, null, null, null, null);
		cursor.moveToFirst();
		name = cursor.getString(0);
		cursor.close();
		isActive = true;
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getApp(){
		return app;
	}
	
	public void setApp(String app){
		this.app = app;
	}
	
	public boolean isActive(){
		return isActive;
	}
	
	public void setActive(boolean searchIn){
		this.isActive = searchIn;
	}
	
	public Word getWord(String wordSource, ContentResolver cr)
	{
		Word foundWord = null;
		Uri uri = getProviderUri("words");
		String[] selectionArgs = new String[]{wordSource};
		Cursor cursor = cr.query(uri, projection, selection, selectionArgs, null);
		cursor.moveToFirst();
		foundWord = cursorToWord(cursor);
		cursor.close();
		return foundWord;
	}
	
	/*
	 * uri_opt can be "words" for search words in dictionaries or
	 * "words/create" for create dictionary via contentProvider
	 */
	private Uri getProviderUri(String uri_opt)
	{
		Uri uri = null;
		uri = Uri.parse("content://" + PACKAGE + app + ".WordsProvider/" + uri_opt);
		return uri;
	}
	
	private Word cursorToWord(Cursor cursor)
	{
		Word sWord = null;
		if(cursor != null && cursor.getCount() > 0)	
			sWord = new Word(cursor.getString(1),cursor.getString(2), name);
		return sWord;
	}
}
