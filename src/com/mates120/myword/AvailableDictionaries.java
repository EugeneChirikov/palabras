package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class AvailableDictionaries
{   // Implements Singleton pattern (no multithreading yet)
	private List<Dictionary> knownDictionaries;
	private KnownDictionariesDB dictsDB;
	private PackageManager pacMan;
	ContentResolver contentResolver;
	
	private static final String DICTIONARY_PACKAGE = "com.mates120.dictionary.";
	private static AvailableDictionaries uniqueInstance;
	
	private AvailableDictionaries(Context context)
	{
		knownDictionaries = new ArrayList<Dictionary>();
		dictsDB = new KnownDictionariesDB(context);
		pacMan = context.getPackageManager();
		contentResolver = context.getContentResolver();
	}
	
	public static synchronized AvailableDictionaries getInstance(Context context)
	{
		if (uniqueInstance == null)
			uniqueInstance = new AvailableDictionaries(context);
		return uniqueInstance;
	}
	
	public synchronized List<Dictionary> getList()
	{
		return knownDictionaries;
	}
	
	public synchronized void refreshList()
	{
		obtainKnownDictionariesList();
		List<String> allDicts = obtainInstalledDictionariesList();
		insertNewlyInstalledDictionaries(allDicts);
		cleanupAlreadyDeletedDictionaries(allDicts);
	}
	
	private void obtainKnownDictionariesList()
	{
		dictsDB.open();
		knownDictionaries = dictsDB.getDicts();
		dictsDB.close();
	}
	
	private List<String> obtainInstalledDictionariesList()
	{
		List<ApplicationInfo> packages = pacMan.getInstalledApplications(PackageManager.GET_META_DATA);
		List<String> dictsInSystem = new ArrayList<String>();
		for (ApplicationInfo packageInfo : packages)
			if(packageInfo.packageName.startsWith(DICTIONARY_PACKAGE))
				dictsInSystem.add(packageInfo.packageName.substring(24));
		return dictsInSystem;
	}
	
	private boolean insertNewlyInstalledDictionaries(List<String> installedDicts)
	{
		boolean wereChanges = false;
		for (String newDict : installedDicts)
		{
			if (isKnownDictionary(newDict))
				continue;
			Dictionary currentDictionary = new Dictionary(newDict, contentResolver);
			dictsDB.open();
			dictsDB.insertDictionary(currentDictionary);
			dictsDB.close();
			knownDictionaries.add(currentDictionary);
			wereChanges = true;
		}
		return wereChanges;
	}
	
	private boolean isKnownDictionary(String dict)
	{		
		for (Dictionary knownDict : knownDictionaries)
			if (dict.equals(knownDict.getApp()))
				return true;
		return false;
	}
			
	private boolean cleanupAlreadyDeletedDictionaries(List<String> installedDicts)
	{
		boolean wereChanges = false;
		for (Dictionary knownDict : knownDictionaries)
		{
			if (isKnownInTheSystem(knownDict.getApp(), installedDicts))
				continue;
			dictsDB.open();
			dictsDB.deleteDictByAppName(knownDict.getApp());
			dictsDB.close();
			knownDictionaries.remove(knownDict);
			wereChanges = true;
		}
		return wereChanges;
	}
	
	private boolean isKnownInTheSystem(String knownDict, List<String> allDicts)
	{		
		return allDicts.contains(knownDict);
	}
	
	public void setDictionaryActive(String dictName, boolean isActive)
	{
		dictsDB.open();
		dictsDB.setActiveDict(dictName, isActive);
		dictsDB.close();
		for(int i = 0; i < knownDictionaries.size(); i++)
			if (knownDictionaries.get(i).getName().equals(dictName))
			{
				knownDictionaries.get(i).setActive(isActive);
				break;
			}
	}
	
	public synchronized List<Word> getWord(String wordSource)
	{
		List<Word> foundWords = new ArrayList<Word>();
		Word foundWord = null;
		for (Dictionary d : knownDictionaries)
		{
			if(!d.isActive())
				continue;
			foundWord = d.getWord(wordSource, contentResolver);
			if (foundWord != null)
				foundWords.add(foundWord);
		}
		return foundWords;
	}
	
	public synchronized List<String> getHints(String startWith)
	{
		List<String> availHints = new ArrayList<String>();
		List<String> dictHints;
		for (Dictionary d : knownDictionaries)
		{
			if(!d.isActive())
				continue;
			dictHints = d.getHints(startWith, contentResolver);
			if (!dictHints.isEmpty())
				for (String hint : dictHints) {
					if (availHints.size() == 20)
						break;
					availHints.add(hint);
				}
			if (availHints.size() == 20)
				break;
		}
		return availHints;
	}
}
