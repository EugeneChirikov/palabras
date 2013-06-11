package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LinksDataSource {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	
	private String[] allLinksColumns = {DatabaseHelper.COL_LINKS_WORD,
			DatabaseHelper.COL_LINKS_VALUE};
	
	public LinksDataSource(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void createLink(int wordId, int valueId){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_LINKS_WORD, wordId);
		values.put(DatabaseHelper.COL_LINKS_VALUE, valueId);
		database.insert(DatabaseHelper.TABLE_LINKS, null, values);
	}
	
	public void deleteLink(int wordId, int valueId){
		System.out.println("Link deleted with word id: " + wordId 
				+ " and value id: " + valueId);
		database.delete(DatabaseHelper.TABLE_LINKS, 
				DatabaseHelper.COL_LINKS_WORD + " = " + wordId 
				+ DatabaseHelper.COL_LINKS_VALUE + " = " + valueId, null);
	}
}
