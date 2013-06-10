package com.mates120.myword;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
	
	public static final String TABLE_WORDS = "words";
	public static final String COL_WORDS_ID = "_id";
	public static final String COL_WORDS_SOURCE = "source";
	public static final String COL_WORDS_FIRST = "first";
	public static final String COL_WORDS_RATING = "rating";
	
	public static final String TABLE_VALUES = "values";
	public static final String COL_VALUES_ID = "_id";
	public static final String COL_VALUES_WORD = "word";
	public static final String COL_VALUES_VALUE = "value";
	public static final String COL_VALUES_TAG = "tag";
	
	public static final String TRIGGER = "FK_VALUES_WORD_WORDS_ID";
	
	public static final String VIEW_WORD_VALUES = "view_word_values";
	
	
	private static final String DATABASE_NAME = "myword.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE_WORDS = "create table"
			+ TABLE_WORDS + "(" + COL_WORDS_ID
			+ " integer primary key autoincrement, " + COL_WORDS_SOURCE
		    + " text not null, " + COL_WORDS_FIRST
		    + " text not null, " + COL_WORDS_RATING
		    + " integer not null);";
	
	private static final String DATABASE_CREATE_VALUES = "create table"
			+ TABLE_VALUES + " (" + COL_VALUES_ID
			+ " integer primary key autoincrement, " + COL_VALUES_WORD
			+ "INTEGER NOT NULL ,FOREIGN KEY (" + COL_VALUES_WORD + 
			") REFERENCES " + TABLE_WORDS + " (" + COL_WORDS_ID + "), "
			+ COL_VALUES_VALUE + " text not null, " + COL_VALUES_TAG
			+ " text not null);";
	
	private static final String TRIGGER_CREATE = "CREATE TRIGGER " + TRIGGER
			+ " BEFORE INSERT ON" + TABLE_WORDS
			+ " FOR EACH ROW BEGIN"
			+ " SELECT CASE WHEN ((SELECT " + COL_WORDS_ID + " FROM "+ TABLE_WORDS
			+ " WHERE " + COL_WORDS_ID + " = new." + COL_VALUES_WORD + " ) IS NULL)"
			+ " THEN RAISE (ABORT,'Foreign Key Violation') END;"+
			"  END;";
	
	/*private static final String CREATE_VIEW_WORDS_VALUES = "CREATE VIEW "
			+ VIEW_WORD_VALUES + " AS SELECT " + TABLE_WORDS + "." + COL_WORDS_ID
			+ " FROM " + TABLE_VALUES + " ";
	*/
	
	public DatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE_WORDS);
	    database.execSQL(DATABASE_CREATE_VALUES);
	    database.execSQL(TRIGGER_CREATE);
	  }
	  
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(DatabaseHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_VALUES);
	    db.execSQL("DROP TRIGGER IF EXIST " + TRIGGER);
	    onCreate(db);
	  }
}
