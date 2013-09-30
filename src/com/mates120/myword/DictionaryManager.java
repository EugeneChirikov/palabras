package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;

public class DictionaryManager {
	
	private PackageManager pacMan;
	private DataSource dataSource;
	
	private Context context;
	private final String[] projection = new String[]{"_id", "source", "value"};
	private final String selection =  "source = ?";
	
	public DictionaryManager(Context context){
		this.context = context;
		this.pacMan = context.getPackageManager();
		this.dataSource = new DataSource(context);
	}
	
	public Word getWord(String wordSource){
		Word foundWord = null;
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.parse(
				"content://com.mates120.dictionarytemplate.WordsProvider/words");
		String[] selectionArgs = new String[]{wordSource};
		Cursor cursor = cr.query(uri, projection, selection, selectionArgs, null);
		cursor.moveToFirst();
		foundWord = cursorToWord(cursor);
		cursor.close();
		return foundWord;
	}
	
	private Word cursorToWord(Cursor cursor){
		Word sWord = null;
		if(cursor != null && cursor.getCount() > 0){
			sWord = new Word(cursor.getString(1),cursor.getString(2));
		}
		return sWord;
	}
	
	public List<Dictionary> getDictionaries(){
		List<Dictionary> dicts;
		dataSource.open();
		dicts = dataSource.getDicts();
		dataSource.close();
		return dicts;
	}
	
	public void setSearchInDict(String dictName, boolean searchIn){
		dataSource.open();
		dataSource.setSearchInDict(dictName, searchIn);
		dataSource.close();
	}
	
	public void dictSync(){
		List<ApplicationInfo> packages = pacMan.getInstalledApplications(PackageManager.GET_META_DATA);
		List<String> dictsInSystem = new ArrayList<String>();
		List<Dictionary> dictsInDB;
		boolean inDB;
		for (ApplicationInfo packageInfo : packages)
			if(packageInfo.packageName.startsWith("com.mates120."))
				dictsInSystem.add(packageInfo.packageName.substring(13));

		dataSource.open();
		dictsInDB = dataSource.getDicts();
		if(!dictsInDB.isEmpty())
			for (String dictSys : dictsInSystem){
				inDB = false;
				for(Dictionary dictDB : dictsInDB)
					if (dictDB.getName().equals(dictSys)){
						System.out.println(dictDB.getName());
						inDB = true;
						break;
					}
				if(!inDB)
					dataSource.insertDictionary(dictSys, "com.mates120." + dictSys);
			}
		else
			for (String dictSys : dictsInSystem){
				System.out.println(dictSys);
				dataSource.insertDictionary(dictSys, "com.mates120." + dictSys);
			}
		dataSource.close();
	}
}
