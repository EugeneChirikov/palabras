package com.mates120.myword;

import java.io.File;
import java.io.FilenameFilter;

public class DictionaryFile 
{
	private String extension = null;
	private String dirPath = null;
	DictionaryFile(String dirPath, String extension)
	{
		this.extension = extension;
		this.dirPath = dirPath;
	}
	
	File fileDescriptor()
	{
		File directory = new File(this.dirPath);
		if (!directory.isDirectory())
		{
			return null;
		}
		String[] filesOfDictionaries = directory.list()
	}

	public class DictionaryFilenamesFilter implements FilenameFilter
	{
		private String[] extensions = {".ifo", ".idx", ".dict.dz"};
		
		public DictionaryFilenamesFilter(String[] extensions)
		{
			this.extensions = extensions;
		}

		@Override
		public boolean accept(File dir, String filename) {
			// TODO Auto-generated method stub		
			return filename.endsWith();
		}
		
	}
}
