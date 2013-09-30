package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	
	private String[] allWordsColumns = {DatabaseHelper.COL_DICTS_ID, 
			DatabaseHelper.COL_DICTS_NAME, DatabaseHelper.COL_DICTS_APP};
	
	public DataSource(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public long insertDictionary(String dictName, String dictApp){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_DICTS_NAME, dictName);
		values.put(DatabaseHelper.COL_DICTS_APP, dictApp);
		return database.insert(DatabaseHelper.TABLE_DICTS, null, values);
	}
	
	public void deleteDictById(long dictId){
		System.out.println("Word deleted with id: " + dictId);
		database.delete(DatabaseHelper.TABLE_DICTS, 
				DatabaseHelper.COL_DICTS_ID + " = " + dictId, null);
	}
	
	public List<Dictionary> getDicts(){
		List<Dictionary> dicts = new ArrayList<Dictionary>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_DICTS, allWordsColumns, 
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Dictionary dict = cursorToDict(cursor);
			dicts.add(dict);
			cursor.moveToNext();
		}
		cursor.close();
		return dicts;
	}
	
	private Dictionary cursorToDict(Cursor cursor){
		Dictionary dict = null;
		if(cursor != null && cursor.getCount() > 0){
			dict = new Dictionary();
			dict.setId(cursor.getLong(0));
			dict.setName(cursor.getString(1));
			dict.setApp(cursor.getString(2));
			if(cursor.getInt(3) == 0)
				dict.setSearched(false);
			else
				dict.setSearched(true);
		}
		return dict;
	}

}
