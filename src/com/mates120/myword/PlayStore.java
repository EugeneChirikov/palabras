package com.mates120.myword;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

public class PlayStore 
{
	private static final String LOG_TAG = "";
	private static final String publisherSearch = "market://search?q=pub:";
	private static final String packageSearch = "market://details?id=";
	private static final String generalSearch = "market://search?q=";
	
	private static final String dictsPackage = "com.mates120.dictionary.";
	private static final String publisher = "Eugene Chirikov %26 Victor Kalutskiy";

	private PackageManager pm;
	private Context myContext;
	
	public PlayStore(Context context)
	{
		myContext = context;
		this.pm = myContext.getPackageManager();		
	}
	
	public void findDictionaries()
	{
		Uri dictsUri = Uri.parse(publisherSearch + publisher);
//		Uri dictsUri = Uri.parse("msdofsdj");
		Intent storeIntent = new Intent(Intent.ACTION_VIEW).setData(dictsUri);
		if (storeIntent.resolveActivity(pm) != null)
			myContext.startActivity(storeIntent);
		else
			Log.d(LOG_TAG, "You don't have PlayStore installed.");
	}
}
