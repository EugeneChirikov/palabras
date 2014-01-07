package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class KnownDictionariesDB
{
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	private String[] allDictsColumns = {DatabaseHelper.COL_DICTS_ID,
		DatabaseHelper.COL_DICTS_NAME, DatabaseHelper.COL_DICTS_APP,
		DatabaseHelper.COL_DICTS_ISACTIVE};

	public KnownDictionariesDB(Context context)
	{
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public long insertDictionary(Dictionary dictionary)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_DICTS_NAME, dictionary.getName());
		values.put(DatabaseHelper.COL_DICTS_APP, dictionary.getApp());
		values.put(DatabaseHelper.COL_DICTS_ISACTIVE, 1);
		return database.insert(DatabaseHelper.TABLE_DICTS, null, values);
	}
	
	public void deleteDictByAppName(String dictApp)
	{
		Log.d("MY_WORD","Dictionary deleted with app name: " + dictApp);
		database.delete(DatabaseHelper.TABLE_DICTS,
			DatabaseHelper.COL_DICTS_APP + " = ?", new String[]{dictApp});
	}
	
	public void deleteDictById(long dictId)
	{
		Log.d("MY_WORD","Dictionary deleted with id: " + dictId);
		database.delete(DatabaseHelper.TABLE_DICTS,
			DatabaseHelper.COL_DICTS_ID + " = " + dictId, null);
	}
	
	public List<Dictionary> getDicts()
	{
		List<Dictionary> dicts = new ArrayList<Dictionary>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_DICTS, allDictsColumns,
		null, null, null, null, null);
		if(cursor == null)
			return dicts;
		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			Dictionary dict = cursorToDict(cursor);
			dicts.add(dict);
			cursor.moveToNext();
		}
		cursor.close();
		return dicts;
	}
	
	private Dictionary cursorToDict(Cursor cursor)
	{
		Dictionary dict = null;
		if(cursor != null && cursor.getCount() > 0)
		{
			dict = new Dictionary();
			dict.setId(cursor.getLong(0));
			dict.setName(cursor.getString(1));
			dict.setApp(cursor.getString(2));
			dict.setActive(cursor.getInt(3) > 0 ? true : false);
		}
		return dict;
	}
	
	public long setActiveDict(String name, boolean isActive)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_DICTS_ISACTIVE, isActive ? 1 : 0);
		return database.update(DatabaseHelper.TABLE_DICTS, values,
			DatabaseHelper.COL_DICTS_NAME + "=?", new String[]{name});
	}
}
