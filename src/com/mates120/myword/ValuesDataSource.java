package com.mates120.myword;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ValuesDataSource {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	
	private String[] allValuesColumns = {DatabaseHelper.COL_VALUES_ID, 
			DatabaseHelper.COL_VALUES_VALUE,
			DatabaseHelper.COL_VALUES_TAG};
	
	public ValuesDataSource(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
}
