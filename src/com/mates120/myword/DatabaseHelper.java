package com.mates120.myword;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String TABLE_DICTS = "known_dicts_table";	
	public static final String COL_DICTS_ID = "_id";
	public static final String COL_DICTS_NAME = "name";
	public static final String COL_DICTS_APP = "app";
	public static final String COL_DICTS_ISACTIVE = "isactive";

	private static final String DATABASE_CREATE_DICTS = "create table "
		+ TABLE_DICTS + "("
		+ COL_DICTS_ID + " integer primary key autoincrement, "
		+ COL_DICTS_NAME + " text not null unique, "
		+ COL_DICTS_APP	+ " text not null, "
		+ COL_DICTS_ISACTIVE + " integer not null);";  //default value should be 1

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