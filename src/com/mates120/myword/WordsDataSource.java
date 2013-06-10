package com.mates120.myword;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WordsDataSource {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allWordsColumns = {DatabaseHelper.COL_WORDS_ID, 
			DatabaseHelper.COL_WORDS_SOURCE, DatabaseHelper.COL_WORDS_FIRST, 
			DatabaseHelper.COL_WORDS_RATING};
	private String[] allValuesColumns = {DatabaseHelper.COL_VALUES_ID, 
			DatabaseHelper.COL_VALUES_WORD, DatabaseHelper.COL_VALUES_VALUE,
			DatabaseHelper.COL_VALUES_TAG};
	
	public WordsDataSource(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	
	public Word createWord(String word){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_WORDS_SOURCE, word);
		values.put(DatabaseHelper.COL_WORDS_FIRST, true);
		values.put(DatabaseHelper.COL_WORDS_RATING, 0);
		long insertId = database.insert(DatabaseHelper.TABLE_WORDS, null, values);
		Cursor cursor = database.query(DatabaseHelper.TABLE_WORDS,
				allWordsColumns, DatabaseHelper.COL_WORDS_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		Word newWord = cursorToWord(cursor);
		cursor.close();
		return newWord;
	}
}
