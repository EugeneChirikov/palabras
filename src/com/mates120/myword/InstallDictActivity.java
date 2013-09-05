package com.mates120.myword;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;

public class InstallDictActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		1. Obtaining dictionary files
//		2. Extracting data
//		3. Installing

//		setContentView(R.layout.activity_examinate);
		
		String dictType = "StarDict"; // assume this we can get with Intent
		RawDictionary rawDictionary = obtainDictionaryFiles(dictType);
		Dictionary dictionary = rawDictionary.parseAll();
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
				throw new Exception("Failed to obtain dicitionary data");
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

	private void readFileStream2(InputStream fileStream) throws IOException
	{
		System.out.println("Printing...");
		int available = 0;
		try {
			available = fileStream.available();
			System.out.println("Available " + available);
		} catch (IOException e) {
			System.out.println("Available failed");
		}
        byte[] buffer = new byte[available];
        System.out.println("Buff length " + buffer.length);
        fileStream.read(buffer);
        System.out.println(convertToString(buffer));
	}

	private String convertToString(byte[] buffer) throws UnsupportedEncodingException
	{
		return new String(buffer, "UTF-8");
	}
//	private Uri makeUpUri()
}
