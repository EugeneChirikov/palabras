package com.mates120.myword;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class Dictionary
{
	private long id;
	private String name;
	private String app;
	private boolean isActive;
	
	private final String[] projection = new String[]{"_id", "source", "value"};
	private final String selection =  "source = ?";
	
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
		Uri uri = getProviderUri();
		String[] selectionArgs = new String[]{wordSource};
		Cursor cursor = cr.query(uri, projection, selection, selectionArgs, null);
		cursor.moveToFirst();
		foundWord = cursorToWord(cursor);
		cursor.close();
		return foundWord;
	}
	
	private Uri getProviderUri()
	{
		Uri uri = Uri.parse("content://" + app + ".WordsProvider/words");
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
