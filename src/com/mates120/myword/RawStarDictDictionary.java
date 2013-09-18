package com.mates120.myword;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import com.mates120.myword.Exceptions.EndOfFileException;
import com.mates120.myword.Exceptions.DictionaryParserException;

public class RawStarDictDictionary extends  RawDictionary
{
	private enum IFO_PARAMETERS
	{
		BOOKNAME("bookname"),       // required
		WORDCOUNT("wordcount"),     // required
		SYNWORDCOUNT("synwordcount"),  // required if ".syn" file exists.
		IDXFILESIZE("idxfilesize"),   // required
		IDXOFFSETBITS("idxoffsetbits"), // New in 3.0.0
		AUTHOR("author"),
		EMAIL("email"),
		WEBSITE("website"),
		DESCRIPTION("description"),    // You can use <br> for new line.
		DATE("date"),
		SAMETYPESEQUENCE("sametypesequence"), // very important.
		VERSION(""),
		UNKNOWN("");

		String paramName;
		IFO_PARAMETERS(String paramName)
		{
			this.paramName= paramName;
		}

		@Override
		public String toString()
		{
			return super.toString().toLowerCase(Locale.US);
		}
	}

	private InputStream ifoFile;
	private InputStream idxFile;
	private InputStream dictFile;
	private StreamsReader inputStream;

	private int wordcount = -1;
	private int synwordcount = -1;
	private int idxoffsetbits = -1;
	private int idxfilesize = -1;
	private String author = "";
	private String email = "";
	private String website = "";
	private String description = "";
	private String date = "";
	private String sametypesequence = "";
	private String version = "";
	private final byte WORD_ENDING = 0;
	
	private List <StarDictWord> wordsList = new ArrayList<StarDictWord>();

	public RawStarDictDictionary(InputStream ifoFile, InputStream idxFile, InputStream dictFile)
	{
		this.ifoFile = ifoFile;
		this.idxFile = idxFile;
		this.dictFile = dictFile;
	}

	private StarDictWord addWord(String wordString)
	{
		StarDictWord word = new StarDictWord(wordString);
		wordsList.add(word);
		return word;
	}
	
	public Dictionary parseAll()
	{
		parsedDict = new Dictionary();
		try {
			parseIFO();
//			checkParsedIFO();
			TimeUnit.SECONDS.sleep(10);
			parseIDX();
			checkParsedIDX();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("It's finally");
			checkParsedIDX();
		}
		return null;
	}

	private void checkParsedIFO()
	{
		System.out.println("bookname = " + parsedDict.getName());
		System.out.println("wordcount = " + Integer.toString(wordcount));
		System.out.println("synwordcount = " + Integer.toString(synwordcount));
		System.out.println("idxoffsetbits = " + Integer.toString(idxoffsetbits));
		System.out.println("idxfilesize = " + Integer.toString(idxfilesize));
		System.out.println("author = " + author);
		System.out.println("email = " + email);
		System.out.println("website = " + website);
		System.out.println("description = " + description);
		System.out.println("date = " + date);
		System.out.println("sametypesequence = " + sametypesequence);
	}
	
	private void parseIFO() throws IOException, DictionaryParserException
	{
		initStreamsReader(ifoFile);
		String ifoData = readEverythingString();

		Pattern regexpPattern = Pattern.compile("([a-z]+)=([0-9.]+|(?<=bookname=|description=|sametypesequence=).+)");
		Matcher regexpMatcher = regexpPattern.matcher(ifoData);
		while (regexpMatcher.find())
		{
			String parameterName = regexpMatcher.group(1);
			String parameterValue = regexpMatcher.group(2);
//			System.out.println("pname = " + parameterName);
//			System.out.println("pvalue = " + parameterValue);
			IFO_PARAMETERS parameter = identifyIFOParam(parameterName);
			switch (parameter)
			{
				case BOOKNAME:
					parsedDict.setName(parameterValue);
					break;
				case WORDCOUNT:
					wordcount = Integer.parseInt(parameterValue);
					break;
				case VERSION:
					version = parameterValue;
					if (!version.equals("2.4.2") && !version.equals("3.0.0"))
						throw new DictionaryParserException("Incompatible with this StarDict version: " + parameterValue);
					break;
				case SYNWORDCOUNT:
					synwordcount = Integer.parseInt(parameterValue);
					break;
				case IDXOFFSETBITS:
					idxoffsetbits = Integer.parseInt(parameterValue);
					break;
				case IDXFILESIZE:
					idxfilesize = Integer.parseInt(parameterValue);
					break;
				case AUTHOR:
					author = parameterValue;
					break;
				case EMAIL:
					email = parameterValue;
					break;
				case WEBSITE:
					website = parameterValue;
					break;
				case DESCRIPTION:
					description = parameterValue;
					break;
				case DATE:
					date = parameterValue;
					break;
				case SAMETYPESEQUENCE:
					sametypesequence = parameterValue;
				default:
					break;
			}
		}
		ifoFile.close();
	}

	private void initStreamsReader(InputStream myStream)
	{
		inputStream = new StreamsReader(myStream);
	}
	
	private void initStreamsReaderBuffered(InputStream myStream)
	{
		initStreamsReader(myStream);
		inputStream.initBufferedReading();
	}
	
	private String readEverythingString() throws IOException
	{
		return inputStream.readEverythingString();
	}
	
	private IFO_PARAMETERS identifyIFOParam(String paramName)
	{
		for (IFO_PARAMETERS p : IFO_PARAMETERS.values())
		{			
			if (paramName.equals( p.toString()))
			{
				return p;
			}
		}
		return IFO_PARAMETERS.UNKNOWN;
	}

	private void parseIDX() throws IOException, DictionaryParserException
	{
		setWordsOffsetSize();
		initStreamsReaderBuffered(idxFile);
		try
		{
			byte newByte;
			WordBuffer wordString = new WordBuffer();
			byte [] dataOffset = new byte [idxoffsetbits/8]; // 8 or 4 bytes
			byte [] dataSize = new byte [4]; //4 bytes
			StarDictWord newWord = null;
			while (true)
			{
				newByte = readByte();
				if (newByte == WORD_ENDING)
				{
					newWord = addWord(wordString.getWord());
					readBytesIntoBuffer(dataOffset);
					newWord.addDataOffset(dataOffset);
					readBytesIntoBuffer(dataSize);
					newWord.addDataSize(dataSize);
					wordString.clear();
				}
				else
				{
					wordString.addByte(newByte);
				}
			}
		}
		catch (EndOfFileException e)
		{
			System.out.println("End of file reached");
		}
		finally
		{
			System.out.println("Closing stream");
			idxFile.close();
		}
	}
	
	private byte readByte() throws EndOfFileException, IOException
	{
		return inputStream.getByte();
	}

	private void readBytesIntoBuffer(byte [] buffer) throws EndOfFileException, IOException
	{
		for (int i = 0; i < buffer.length; ++i)
		{
			byte newByte = readByte();
			buffer[i] = newByte;
		}
	}
	
	private void setWordsOffsetSize()
	{
		if (idxoffsetbits > 0)
			return;
		if (version.equals("3.0.0"))
			idxoffsetbits = 64;
		else
			idxoffsetbits = 32;
	}
	
	private void checkParsedIDX()
	{
		for (int i = 0; i < wordsList.size(); ++i)
		{
			String word = wordsList.get(i).getWordString();
			System.out.println(word);
		}
	}
	
	private void parseDICT() throws DictionaryParserException, IOException{
		dictFile.close();
	}
}
