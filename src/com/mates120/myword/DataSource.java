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
	
	private String[] allWordsColumns = {DatabaseHelper.COL_WORDS_ID, 
			DatabaseHelper.COL_WORDS_SOURCE, 
			DatabaseHelper.COL_WORDS_SEARCH_COUNTER, 
			DatabaseHelper.COL_WORDS_RATING};
	
	private String[] allValuesColumns = {DatabaseHelper.COL_VALUES_ID, 
			DatabaseHelper.COL_VALUES_VALUE,
			DatabaseHelper.COL_VALUES_DICT_ID};
	
	private String[] allLinksColumns = {DatabaseHelper.COL_LINKS_WORD,
			DatabaseHelper.COL_LINKS_VALUE};
	
	private String[] allDictionariesColumns = {DatabaseHelper.COL_DICTIONARIES_ID,
			DatabaseHelper.COL_DICTIONARIES_NAME};
	
	public DataSource(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public long insertWord(String word_source){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_WORDS_SOURCE, word_source);
		values.put(DatabaseHelper.COL_WORDS_SEARCH_COUNTER, 0);
		values.put(DatabaseHelper.COL_WORDS_RATING, 0);
		return database.insert(DatabaseHelper.TABLE_WORDS, null, values);
	}
	
	public void deleteWord(Word word){
		long id = word.getId();
		System.out.println("Word deleted with id: " + id);
		database.delete(DatabaseHelper.TABLE_WORDS, 
				DatabaseHelper.COL_WORDS_ID + " = " + id, null);
	}
	
	public Word getWordBySource(String source){
		Cursor cursor = database.query(DatabaseHelper.TABLE_WORDS, allWordsColumns, 
				DatabaseHelper.COL_WORDS_SOURCE + " = " + source, null, null, null, null);
		cursor.moveToFirst();
		Word foundWord = cursorToWord(cursor);
		cursor.close();
		return foundWord;
	}
	
	public List<Word> getAllWords(){
		List<Word> words = new ArrayList<Word>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_WORDS, allWordsColumns, 
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Word word = cursorToWord(cursor);
			words.add(word);
			cursor.moveToNext();
		}
		cursor.close();
		return words;
	}
	
	private Word cursorToWord(Cursor cursor){
		Word word = null;
		if(cursor != null && cursor.getCount() > 0){
			word = new Word();
			word.setId(cursor.getLong(0));
			word.setSource(cursor.getString(1));
			word.setSearchCount(cursor.getInt(2));
			word.setRating(cursor.getInt(3));
		}
		return word;
	}
	
	public long insertValue(String value, long dictionaryId){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_VALUES_VALUE, value);
		values.put(DatabaseHelper.COL_VALUES_DICT_ID, dictionaryId);
		return database.insert(DatabaseHelper.TABLE_VALUES, null, values);
	}
	
	public void deleteValue(Value value){
		long id = value.getId();
		System.out.println("Value deleted with id: " + id);
		database.delete(DatabaseHelper.TABLE_VALUES, 
				DatabaseHelper.COL_VALUES_ID + " = " + id, null);
	}
	
	public Value getValue(Value value){
		Cursor cursor = database.query(DatabaseHelper.TABLE_VALUES, allValuesColumns, 
				DatabaseHelper.COL_VALUES_VALUE + " = " + value.getValue() 
				+ " and " + DatabaseHelper.COL_VALUES_DICT_ID 
				+ " = " + getDictionaryId(value.getDictionary()), 
				null, null, null, null);
		cursor.moveToFirst();
		Value findValue = cursorToValue(cursor);
		cursor.close();
		return findValue;
	}
	
	public List<Value> getAllValues(){
		List<Value> values = new ArrayList<Value>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_VALUES, allValuesColumns, 
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Value value = cursorToValue(cursor);
			values.add(value);
			cursor.moveToNext();
		}
		cursor.close();
		return values;
	}
	
	private Value cursorToValue(Cursor cursor){
		Value value = null;
		if(cursor != null && cursor.getCount() > 0){
			value = new Value();
			value.setId(cursor.getLong(0));
			value.setValue(cursor.getString(1));
			value.setDictionary(getDictionaryName(cursor.getLong(2)));
		}
		return value;
	}
	
	public void createLink(long wordId, long valueId){
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
	
	public long insertDictionary(String name){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_DICTIONARIES_NAME, name);
		return database.insert(DatabaseHelper.TABLE_DICTIONARIES, null, values);
	}
	
	public long deleteDictionary(String name){
		Cursor cursor = database.query(DatabaseHelper.TABLE_DICTIONARIES,
				allDictionariesColumns, DatabaseHelper.COL_DICTIONARIES_NAME 
				+ " = " + name,	null, null, null, null);
		cursor.moveToFirst();
		long id = cursor.getLong(0);
		cursor.close();
		System.out.println("Dictionary deleted with id: " + id);
		database.delete(DatabaseHelper.TABLE_DICTIONARIES, 
				DatabaseHelper.COL_VALUES_ID + " = " + id, null);
		return id;
	}
	
	public String getDictionaryName(long dictId){
		Cursor cursor = database.query(DatabaseHelper.TABLE_DICTIONARIES,
				allDictionariesColumns, DatabaseHelper.COL_DICTIONARIES_ID
				+ " = " + dictId,	null, null, null, null);
		cursor.moveToFirst();
		String name = cursor.getString(1);
		cursor.close();
		return name;
	}
	
	public long getDictionaryId(String dictName){
		Cursor cursor = database.query(DatabaseHelper.TABLE_DICTIONARIES,
				allDictionariesColumns, DatabaseHelper.COL_DICTIONARIES_NAME
				+ " = " + dictName,	null, null, null, null);
		cursor.moveToFirst();
		long id = cursor.getLong(0);
		cursor.close();
		return id;
	}
	
	private List<Value> getWordValues(long wordId){
		List<Value> values = new ArrayList<Value>();
		Cursor cursor = database.rawQuery("select " + allValuesColumns[0] 
				+", " + allValuesColumns[1] + ", " + allValuesColumns[2] 
				+ " from "	+ DatabaseHelper.TABLE_LINKS 
				+ " join " + DatabaseHelper.TABLE_VALUES
				+ " where " + DatabaseHelper.COL_LINKS_WORD + " = " + wordId, null);
		cursor.moveToFirst();
		/*The issue could be here.
		 */
		System.out.println(cursor.getColumnCount());
		while (!cursor.isAfterLast()){
			Value value = cursorToValue(cursor);
			values.add(value);
			cursor.moveToNext();
		}
		cursor.close();
		return values;
	}
}
