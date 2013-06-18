package com.mates120.myword;

import java.io.File;
import java.io.FilenameFilter;

import com.mates120.myword.Exceptions.*;


public class DictionaryFile
{
	private String[] extensions;
	private String searchPath = null;

	public DictionaryFile(String searchPath, String[] extensions)
	{
		this.extensions = extensions;
		this.searchPath = searchPath;
	}

	public DictionaryFile(String searchPath, String extension)
	{
		this.extensions = new String[1];
		this.extensions[0] = extension;
		this.searchPath = searchPath;
	}

	private File makeDirectoryObject() throws NotADirectoryException
	{
		File directory = new File(this.searchPath);
		if (!directory.isDirectory())
			throw new NotADirectoryException();
		return directory;
	}

	public String getValidPathToFile() throws DictionaryParserException
	{
		File directory = this.makeDirectoryObject();
		DictionaryFilenamesFilter filter = new DictionaryFilenamesFilter(this.extensions);
		String[] filesOfDictionaries = directory.list(filter);
		debugPrintFileNames(filesOfDictionaries);
		this.requireOnlyOneFile(filesOfDictionaries.length);		
		return filesOfDictionaries[0];
	}
	
	private void requireOnlyOneFile(int listLenght) throws ToManyAlikeFilesException,
	                                                       ExpectedFilesNotFoundExeption
	{
		if (listLenght > 1)
			throw new ToManyAlikeFilesException();
		if (listLenght == 0)
			throw new ExpectedFilesNotFoundExeption();
	}

	private void debugPrintFileNames(String[] fileNames)
	{
		System.out.println("debugPrintFileNames:");
		for (int i = 0; i < fileNames.length; ++i)
		{
			System.out.println(fileNames[i]);
		}
	}
	
	public class DictionaryFilenamesFilter implements FilenameFilter
	{
		private String[] extensions;
		public DictionaryFilenamesFilter(String[] extensions)
		{
			this.extensions = extensions;
		}

		@Override
		public boolean accept(File dir, String filename) {
			for (int i = 0; i < extensions.length; ++i)
				if (filename.endsWith(extensions[i]))
					return true;
			return false;
		}
		
	}
}
