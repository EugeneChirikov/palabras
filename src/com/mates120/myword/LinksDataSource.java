package com.mates120.myword;

import android.content.ContentValues;
import android.content.Context;

public class LinksDataSource extends DataSource{
	
	public LinksDataSource(Context context){
		super(context);
	}
	
	public void createLink(int wordId, int valueId){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_LINKS_WORD, wordId);
		values.put(DatabaseHelper.COL_LINKS_VALUE, valueId);
		getDatabase().insert(DatabaseHelper.TABLE_LINKS, null, values);
	}
	
	public void deleteLink(int wordId, int valueId){
		System.out.println("Link deleted with word id: " + wordId 
				+ " and value id: " + valueId);
		getDatabase().delete(DatabaseHelper.TABLE_LINKS, 
				DatabaseHelper.COL_LINKS_WORD + " = " + wordId 
				+ DatabaseHelper.COL_LINKS_VALUE + " = " + valueId, null);
	}
}
