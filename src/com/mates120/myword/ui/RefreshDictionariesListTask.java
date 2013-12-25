package com.mates120.myword.ui;

import com.mates120.myword.AvailableDictionaries;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class RefreshDictionariesListTask
{
	private ProgressDialog progressDialog;
	private Context myContext;
	private Handler uiThreadHandler;
	
	public RefreshDictionariesListTask(Context context)
	{
		myContext = context;
		uiThreadHandler = new Handler();
		progressDialog = new ProgressDialog(myContext);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Checking dictionaries");
		progressDialog.show();
		Runnable refreshingTask = refreshDictionariesListTask();
		Thread refreshingThread = new Thread(refreshingTask);
		refreshingThread.start();
	}
	
	private Runnable refreshDictionariesListTask()
	{
		return new Runnable()
		{
			public void run()
			{
				AvailableDictionaries.getInstance(myContext).refreshList();
				uiThreadHandler.post(dismissProgressDialog());
			}
		};
	}
	
	private Runnable dismissProgressDialog()
	{
	    return new Runnable()
	    {
	    	public void run() {
	    		progressDialog.dismiss();
	    	}
	    };
	} 
}
