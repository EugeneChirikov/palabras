package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
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
	private DictsFactory dictsFactory;
	private ContentResolver contentResolver;

	public KnownDictionariesDB(Context context)
	{
		dbHelper = new DatabaseHelper(context);
		contentResolver = context.getContentResolver();
		dictsFactory = new DictsFactory(contentResolver);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public void addDictionary(DictionarySpec spec)
	{
		String[] attr = spec.requestAttributes(contentResolver);
		String name = attr[0];
		String type = attr[1];
		insertDictionary(name, spec.getAppName(), type);
	}
	
	
	public long insertDictionary(String name, String app, String type)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.DictColumns.NAME.toString(), name);
		values.put(DatabaseHelper.DictColumns.APP.toString(), app);
		values.put(DatabaseHelper.DictColumns.ISACTIVE.toString(), true);
		values.put(DatabaseHelper.DictColumns.TYPE.toString(), type);
		return database.insert(DatabaseHelper.TABLE_DICTS, null, values);
	}
	
	private int changeDictionaryData(Dictionary dict, String name, String type)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.DictColumns.NAME.toString(), name);
		values.put(DatabaseHelper.DictColumns.TYPE.toString(), type);
		String[] whereArgs = new String[]{Long.toString(dict.getId())};
		String whereClause = DatabaseHelper.DictColumns.ID.toString() + " = ?";
		return database.update(DatabaseHelper.TABLE_DICTS, values, whereClause, whereArgs);
	}

	public void deleteDict(DictionarySpec spec)
	{
		deleteDictByAppName(spec.getAppName());
	}
	
	public void deleteDictByAppName(String dictApp)
	{
		Log.d("MY_WORD","Dictionary deleted with app name: " + dictApp);
		database.delete(DatabaseHelper.TABLE_DICTS,
			DatabaseHelper.DictColumns.APP.toString() + " = ?", new String[]{dictApp});
	}
	
	public void deleteDictById(long dictId)
	{
		Log.d("MY_WORD","Dictionary deleted with id: " + dictId);
		database.delete(DatabaseHelper.TABLE_DICTS,
			DatabaseHelper.DictColumns.ID + " = " + dictId, null);
	}
	
	public List<DictionarySpec> getDictSpecs()
	{
		List<DictionarySpec> dicts = new ArrayList<DictionarySpec>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_DICTS, DatabaseHelper.DictColumns.names(),
		null, null, null, null, null);
		if(cursor == null || !cursor.moveToFirst())
			return dicts;
		do
		{
			DictionarySpec dict = cursorToDictSpec(cursor);
			dicts.add(dict);
		}
		while(cursor.moveToNext());
		cursor.close();
		return dicts;
	}
	
	private DictionarySpec cursorToDictSpec(Cursor cursor)
	{
		DictionarySpec dictSpec = null;
		if(cursor != null && cursor.getCount() > 0)
		{
			dictSpec = new DictionarySpec(cursor.getString(DatabaseHelper.DictColumns.APP.position()));
		}
		return dictSpec;
	}
	
	public void updateData()
	{
		List<Dictionary> dicts = getDicts();
		for (Dictionary dict: dicts)
		{
			String[] attr = dict.getSpec().requestAttributes(contentResolver);
			String name = attr[0];
			String type = attr[1];
			String dictType = dictsFactory.determineType(dict);
			if (name.equals(dict.getName()) && type.equals(dictType))
				continue;
			changeDictionaryData(dict, name, type);
		}
	}
	
	public List<Dictionary> getDicts()
	{
		List<Dictionary> dicts = new ArrayList<Dictionary>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_DICTS, DatabaseHelper.DictColumns.names(),
		null, null, null, null, null);
		if(cursor == null || !cursor.moveToFirst())
			return dicts;
		do
		{
			Dictionary dict = cursorToDict(cursor);
			dicts.add(dict);
		}
		while(cursor.moveToNext());
		cursor.close();
		return dicts;
	}
	
	private Dictionary cursorToDict(Cursor cursor)
	{
		Dictionary dict = null;
		DictionarySpec dictSpec = null;
		if(cursor != null && cursor.getCount() > 0)
		{
			dictSpec = new DictionarySpec(cursor.getString(DatabaseHelper.DictColumns.APP.position()));
			long id = cursor.getLong(DatabaseHelper.DictColumns.ID.position());
			String name = cursor.getString(DatabaseHelper.DictColumns.NAME.position());
			boolean isActive = cursor.getInt(DatabaseHelper.DictColumns.ISACTIVE.position()) > 0 ? true : false;
			String type = cursor.getString(DatabaseHelper.DictColumns.TYPE.position());
			dict = dictsFactory.createDictionary(dictSpec, id, name, isActive, type);
		}
		return dict;
	}
	
	public long setActiveDict(DictionarySpec spec, boolean isActive)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.DictColumns.ISACTIVE.toString(), isActive ? 1 : 0);
		return database.update(DatabaseHelper.TABLE_DICTS, values,
			DatabaseHelper.DictColumns.APP.toString() + "=?", new String[]{spec.getAppName()});
	}
}
