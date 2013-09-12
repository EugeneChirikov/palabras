package com.mates120.myword;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.ContentResolver;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.widget.ProgressBar;
import android.widget.TextView;

public class InstallDictActivity extends Activity
{
    private ProgressBar installationProgress;
    private Handler uiThreadHandler = new Handler();
    private TextView installationStatusView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		1. Obtaining dictionary files
//		2. Extracting data
//		3. Installing

		setContentView(R.layout.activity_install_dict);
		installationStatusView = (TextView)findViewById(R.id.installStatus);
        installationProgress = (ProgressBar)findViewById(R.id.installProgressBar);
        
        Runnable installDictionaryTask = installDictionaryTask();
        Thread installationThread = new Thread(installDictionaryTask);
        installationThread.start();	
		
	}

	private Runnable installDictionaryTask()
	{
		return new Runnable()
        {
        	public void run()
        	{
        		upgradeStatusWith("Obtaining dictionary data...");
        		String dictType = "StarDict"; // assume this we can get with Intent
        		RawDictionary rawDictionary = obtainDictionaryFiles(dictType);
        		upgradeProgressBarWith(33);
        		upgradeStatusWith("Extracting dictionary data...");
        		Dictionary dictionary = rawDictionary.parseAll();
        		upgradeProgressBarWith(66);
        		upgradeStatusWith("Indexing dictionary data...");
//        		upgradeProgressBarWith(100);
//        		upgradeStatusWith("Installation complete");
        	}
        };
	}
	
	private void upgradeStatusWith(String statusText)
	{
		Runnable upgradeStatusTask = upgradeStatusTask(statusText);
		uiThreadHandler.post(upgradeStatusTask);
	}
	
	private Runnable upgradeStatusTask(final String statusText)
	{
		return new Runnable()
        {
            public void run() {
            	installationStatusView.setText(statusText);
            }
        };
	}
	
	private void upgradeProgressBarWith(int progressStatusValue)
	{
		Runnable updateProgressBarTask = updateProgressBarTask(progressStatusValue);
		uiThreadHandler.post(updateProgressBarTask);
	}

	private Runnable updateProgressBarTask(final int progressStatusValue)
	{
		return new Runnable()
        {
            public void run() {
            	installationProgress.setProgress(progressStatusValue);
            }
        };
	}

	private RawDictionary obtainDictionaryFiles(String dictType)
	{
		RawDictionary rawDict = null;
		if (dictType == "StarDict")
		{
			try
			{
				InputStream ifoFile = obtainFileFromProvider("ifo");
//				readFileStream2(ifoFile);
				InputStream idxFile = obtainFileFromProvider("idx");
//				readFileStream2(idxFile);
				InputStream dictFile = obtainFileFromProvider("dict");
//				readFileStream2(dictFile);
				rawDict = new RawStarDictDictionary(ifoFile, idxFile, dictFile);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return rawDict;
	}

	private InputStream obtainFileFromProvider(String dictName)
	{
		Uri CONTENT_URI = Uri.parse("content://com.mates120.testdictionary.provider/" + dictName);
		ContentResolver contentResolver = getContentResolver();
		ParcelFileDescriptor parcelDescriptor = null;
		try {
			parcelDescriptor = contentResolver.openFileDescriptor(CONTENT_URI, "");
			if (parcelDescriptor == null)
				throw new Exception("Failed to obtain dictionary data");
		}
		catch (Exception e) {
//			Notify user that installation is failed and close the activity
//			Use "Ok" button and then finish() activity
			e.printStackTrace();
			finish();
		}
		FileDescriptor fileDescriptor = parcelDescriptor.getFileDescriptor();	
		InputStream fileStream = new FileInputStream(fileDescriptor);
		return fileStream;
	}

}
