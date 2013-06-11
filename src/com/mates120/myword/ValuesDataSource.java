package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ValuesDataSource extends DataSource{
	
	private String[] allValuesColumns = {DatabaseHelper.COL_VALUES_ID, 
			DatabaseHelper.COL_VALUES_VALUE,
			DatabaseHelper.COL_VALUES_TAG};
	
	public ValuesDataSource(Context context){
		super(context);
	}
	
	public Value createValue(String value, String tag){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_VALUES_VALUE, value);
		values.put(DatabaseHelper.COL_VALUES_TAG, tag);
		long insertId = getDatabase().insert(DatabaseHelper.TABLE_VALUES, null, values);
		Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_VALUES,
				allValuesColumns, DatabaseHelper.COL_VALUES_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		Value newValue = cursorToValue(cursor);
		cursor.close();
		return newValue;
	}
	
	public void deleteValue(Value value){
		long id = value.getId();
		System.out.println("Value deleted with id: " + id);
		getDatabase().delete(DatabaseHelper.TABLE_VALUES, 
				DatabaseHelper.COL_VALUES_ID + " = " + id, null);
	}
	
	public List<Value> getAllLinks(){
		List<Value> values = new ArrayList<Value>();
		Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_VALUES, allValuesColumns, 
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
		Value value = new Value();
		value.setId(cursor.getLong(0));
		value.setValue(cursor.getString(1));
		value.setTag(cursor.getString(2));
		return value;
	}
}
