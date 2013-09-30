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
		pacMan = context.getPackageManager();
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
	
	public List<String> getDictionaries(){
		//TODO: Implement another function that search installed dictionaries.
		List<ApplicationInfo> packages = pacMan.getInstalledApplications(PackageManager.GET_META_DATA);
		List<String> dicts = new ArrayList<String>();
		for (ApplicationInfo packageInfo : packages)
			if(packageInfo.packageName.endsWith("dictionarytemplate"))
				dicts.add(packageInfo.packageName); 
		return dicts;
	}
	
	public void setSearchInDict(String dictName, boolean searchIn){
		//TODO: Implement another function, that enable or disable installed dicts apps.
	}
	
	public void dictSync(){
		List<ApplicationInfo> packages = pacMan.getInstalledApplications(PackageManager.GET_META_DATA);
		List<String> dicts = new ArrayList<String>();
		for (ApplicationInfo packageInfo : packages)
			if(packageInfo.packageName.endsWith("dictionarytemplate"))
				dicts.add(packageInfo.packageName); 
	}
}
