package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;

abstract public class Dictionary
{
	protected String name;
	private boolean isActive;
	protected List<String> hints;
	protected DictionarySpec spec;
	protected ContentResolver cr;
	private long id;
	
	public Dictionary(DictionarySpec spec, long id, String name, boolean active, ContentResolver cr)
	{
		this.spec = spec;
		this.cr = cr;
		hints = new ArrayList<String>();
		this.id = id;
		this.isActive = active;
		this.name = name;
	}
	
	abstract public Word getWord(String wordSource);
	abstract public List<String> getHints(String startCharacters);

	public long getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public DictionarySpec getSpec()
	{
		return spec;
	}
	
	public String getApp()
	{
		return spec.getAppName();
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public void setActive(boolean active)
	{
		this.isActive = active;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}