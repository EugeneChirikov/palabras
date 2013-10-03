package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class AvailableDictionaries
{
	private List<Dictionary> knownDictionaries;
	private PackageManager pacMan;
	private Context context;
	
	public AvailableDictionaries(Context context)
	{
		this.context = context;
		knownDictionaries = new ArrayList<Dictionary>();
		pacMan = context.getPackageManager();
	}
	
	public List<Dictionary> getList()
	{
		return knownDictionaries;
	}
	
	public void refreshList()
	{
		obtainKnownDictionariesList();
		List<String> allDicts = obtainAllDictionariesList();
		List<String> newDicts = findNewlyInstalledDictionaries(allDicts);
		if (newDicts.size() > 0)
		{
			// notify user, do in a separate thread
			makeDictionariesKnown(newDicts);
		}
		deletedDicts = findAlreadyDeletedDictionaries(allDicts);
		if (deletedDicts != null)
		{
			// notify user, do in a main thread
			cleanUpDictionaries(deletedDicts);
		}
	}
	
	private void obtainKnownDictionariesList()
	{
		KnownDictionariesDB db = new KnownDictionariesDB(context);
		knownDictionaries = db.getDicts();
	}
	
	private List<String> obtainAllDictionariesList()
	{
		List<ApplicationInfo> packages = pacMan.getInstalledApplications(PackageManager.GET_META_DATA);
		List<String> dictsInSystem = new ArrayList<String>();
		for (ApplicationInfo packageInfo : packages)
			if(packageInfo.packageName.startsWith("com.mates120."))
				dictsInSystem.add(packageInfo.packageName.substring(13));
		return dictsInSystem;
	}
	
	private List<String> findNewlyInstalledDictionaries(List<String> allDicts)
	{
		List<String> newlyInstalledDicts = new ArrayList<String>();
		for (String newDict : allDicts)
		{
			if (isKnownDictionary(newDict))
				continue;
			newlyInstalledDicts.add(newDict);
		}
		return newlyInstalledDicts;
	}
	
	private boolean isKnownDictionary(String dict)
	{
		for (Dictionary knownDict : knownDictionaries)
		{
			if (dict.equals(knownDict.getApp()))
				return true;
		}
		return false;
	}
			
	private List<String> findAlreadyDeletedDictionaries(List<String> allDicts)
	{
		

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
