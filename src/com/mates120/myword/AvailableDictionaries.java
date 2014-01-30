package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import com.mates120.myword.ui.SettingsFragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public class AvailableDictionaries
{
	private List<Dictionary> knownDictionaries;
	private KnownDictionariesDB dictsDB;
	private PackageManager pacMan;
	private SettingsFragment settingsFragment;
	private Context myContext;

	
	private static final String DICTIONARY_PACKAGE = "com.mates120.dictionary.";
	private volatile static AvailableDictionaries uniqueInstance;
	
	private AvailableDictionaries(Context context)
	{		
		knownDictionaries = new ArrayList<Dictionary>();
		DictionarySpec.setPackage(DICTIONARY_PACKAGE);
		dictsDB = new KnownDictionariesDB(context);
		pacMan = context.getPackageManager();
		myContext = context;
	}
	
	public synchronized void subscribeSettingsFragment(SettingsFragment sf)
	{
		settingsFragment = sf;
		notifyAll();
	}
	
	public static AvailableDictionaries getInstance(Context context)
	{
		if (uniqueInstance == null)
		{
			synchronized (AvailableDictionaries.class)
			{
				if (uniqueInstance == null)
					uniqueInstance = new AvailableDictionaries(context);
			}			
		}
		return uniqueInstance;
	}
	
	public synchronized List<Dictionary> getList()
	{
		return knownDictionaries;
	}
	
	public synchronized void refreshList()
	{
		List<DictionarySpec> knownDicts = obtainKnownDictionariesList();
		List<DictionarySpec> installedDicts = obtainInstalledDictionariesList();		
		updateDictionariesDB(knownDicts, installedDicts);
		knownDictionaries = obtainLatestDictionariesList();

		while (settingsFragment == null)
		{
			try {
				wait();
			} catch (InterruptedException e){}
		}
		settingsFragment.onDictionariesRefresh();
	}

	private List<Dictionary> obtainLatestDictionariesList()
	{
		dictsDB.open();
		dictsDB.updateData(); // in case name or type changed
		List<Dictionary> dicts = dictsDB.getDicts();
		dictsDB.close();
		return dicts;
	}
	
	private List<DictionarySpec> obtainKnownDictionariesList()
	{
		dictsDB.open();
		List<DictionarySpec> dicts = dictsDB.getDictSpecs();
		dictsDB.close();
		return dicts;
	}

	private List<DictionarySpec> obtainInstalledDictionariesList()
	{
		List<ApplicationInfo> packages = pacMan.getInstalledApplications(PackageManager.GET_META_DATA);
		List<DictionarySpec> dictsInSystem = new ArrayList<DictionarySpec>();
		String appName;
		for (ApplicationInfo packageInfo : packages)
		{
			if(packageInfo.packageName.startsWith(DICTIONARY_PACKAGE))
			{
				appName = packageInfo.packageName.substring(DICTIONARY_PACKAGE.length());
				dictsInSystem.add(new DictionarySpec(appName));
			}
		}
		return dictsInSystem;
	}

	private void updateDictionariesDB(List<DictionarySpec> knownDicts, List<DictionarySpec> foundDicts)
	{
		dictsDB.open();
		for (DictionarySpec knownDict : knownDicts)
		{
			if (foundDicts.contains(knownDict)) //такой словарь известен
			{
				System.out.println(knownDict);
				if (!foundDicts.remove(knownDict))
					throw new RuntimeException("False logic");
				continue;
			}
			// такой словарь уже удален, убрать из базы
			System.out.println("We remove stale db row");
			dictsDB.deleteDict(knownDict);
		}
		for (DictionarySpec newDictSpec: foundDicts)
		{
			System.out.println("We add new");
			dictsDB.addDictionary(newDictSpec);
		}
		dictsDB.close();
	}

	public void setDictionaryActive(Dictionary dict, boolean isActive)
	{
		dictsDB.open();
		dictsDB.setActiveDict(dict.getSpec(), isActive);
		dictsDB.close();
		dict.setActive(isActive);
	}
	
	public void deleteDictionary(Dictionary dict)
	{
		Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", dict.getSpec().toString(), null));
		myContext.startActivity(intent);
		dictsDB.open();
		System.out.println("DELETE");
		dictsDB.deleteDict(dict.getSpec());
		dictsDB.close();
	}
	
	public List<Word> getWord(String wordSource)
	{
		List<Word> foundWords = new ArrayList<Word>();
		Word foundWord = null;
		for (Dictionary d : knownDictionaries)
		{
			if(!d.isActive())
				continue;
			foundWord = d.getWord(wordSource);
			if (foundWord != null)
				foundWords.add(foundWord);
		}
		return foundWords;
	}
	
	public List<String> getHints(String startWith)
	{
		List<String> totalHints = new ArrayList<String>();
		List<String> dictHints;
		for (Dictionary d : knownDictionaries)
		{
			if(!d.isActive())
				continue;
			dictHints = d.getHints(startWith);
			totalHints.addAll(dictHints); // take first 20 in alphabetic order and exclude duplicates later
			                              // should try to use TreeSet for this
		}
		return totalHints;
	}
}
