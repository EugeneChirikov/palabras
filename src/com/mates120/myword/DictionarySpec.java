package com.mates120.myword;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class DictionarySpec implements Comparable<DictionarySpec>
{
	private static String PACKAGE;
	String appName;
	
	public static void setPackage(String apackage)
	{
		PACKAGE = apackage;
	}
	
	public DictionarySpec(String appName)
	{
		this.appName = appName;
	}

	public String getAppName()
	{
		return appName;
	}

	public String getPackage()
	{
		return PACKAGE;
	}
	
	@Override
	public int compareTo(DictionarySpec another)
	{
		return appName.compareTo(another.getAppName());
	}
	
	public Uri getProviderUri(String uri_opt)
	{
		return Uri.parse("content://" + PACKAGE + appName + ".WordsProvider/" + uri_opt);
	}
	
	public String[] requestAttributes(ContentResolver cr)
	{
		Uri uri = getProviderUri("words/create");
		Cursor cursor = cr.query(uri, null, null, null, null);
		cursor.moveToFirst();
		String name = cursor.getString(0);
		String type = cursor.getString(1);
		cursor.close();
		return new String[]{name, type};
	}
	
	@Override
	public String toString()
	{
		return PACKAGE + appName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object o)
	{
		boolean same = false;

        if (o != null && o instanceof DictionarySpec)
        {
        	DictionarySpec spec = (DictionarySpec)o;
            same = PACKAGE.equals(spec.getPackage()) && appName.equals(spec.getAppName());
        }
        return same;
	}
}
