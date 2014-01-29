package com.mates120.myword;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static enum DictColumns
	{
		ID(0, "_id"),
		NAME(1, "name"),		
		APP(2, "app"),
		ISACTIVE(3, "isactive"),
		TYPE(4, "type");
		
		private int position;
		private String name;
		
		DictColumns(int position, String name)
		{
			this.position = position;
			this.name = name;
		}

		@Override
		public String toString()
		{
			return name;
		}
		
		public int position()
		{
			return position;
		}
		
		public static String[] names()
		{
		    DictColumns[] columns = values();
		    String[] names = new String[columns.length];
		    for (int i = 0; i < columns.length; i++)
		        names[i] = columns[i].toString();
		    return names;
		}
		
	}
		
	public static final String TABLE_DICTS = "known_dicts_table";	

	private static final String DATABASE_CREATE_DICTS = "create table "
		+ TABLE_DICTS + "("
		+ DictColumns.ID + " integer primary key autoincrement, "
		+ DictColumns.NAME + " text not null, "
		+ DictColumns.APP	+ " text not null unique, "
		+ DictColumns.ISACTIVE + " integer not null, "
		+ DictColumns.TYPE + " text not null);";

	public static final String TABLE_LEARNING = "learning_table";
	public static final String COL_LEARNING_ID = "_id";
	public static final String COL_WORD_ID = "word_id";
	public static final String COL_DICT_ID = "dict_id";
	public static final String COL_SEARCH_FREQUENCY = "search_freq";
	
	private static final String DATABASE_CREATE_LEARNING = "create table "
			+ TABLE_LEARNING + "("
			+ COL_LEARNING_ID + " integer primary key autoincrement, "
			+ COL_WORD_ID + " integer not null, "
			+ COL_DICT_ID + " integer not null, "
			+ COL_SEARCH_FREQUENCY + " integer not null);";  // default value (right after creation) should be 1 here
	
	private static final String DATABASE_NAME = "myword.db";
	private static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE_DICTS);
		database.execSQL(DATABASE_CREATE_LEARNING);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(DatabaseHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
			+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DICTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEARNING);
		onCreate(db);
	}

}