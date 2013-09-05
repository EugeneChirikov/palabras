package com.mates120.myword;

import java.io.FileNotFoundException;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

	public InputStream ifoFile;
	public InputStream idxFile;
	public InputStream dictFile;
	int wordcount = -1;
	int synwordcount = -1;
	int idxoffsetbits = -1;
	int idxfilesize = -1;
	String author = "";
	String email = "";
	String website = "";
	String description = "";
	String date = "";
	String sametypesequence = "";
	String version = "";
	private final byte WORD_ENDING = 0;
	
	private List <StarDictWord> wordsList = new ArrayList<StarDictWord>();
	
	public class StarDictWord
	{
//		public static boolean extendedOffset;
		private Word word;
		private byte [] dataOffset;
		private byte [] dataSize;
		
		public StarDictWord(String wordString)
		{
			word = new Word(wordString);
		}
		
		public void addDataOffset(byte [] dataOffset)
		{
			this.dataOffset = dataOffset;
		}
		
		public void addDataSize(byte [] dataSize)
		{
			this.dataSize = dataSize;
		}
		
		public String getWordString()
		{
			return word.getSource();
		}
	}
	
	public class WordBuffer
	{
		private final int MAX_WORD_LENGTH = 255;
		private byte [] wordString;
		int length;
		
		public WordBuffer()
		{
			wordString = new byte [MAX_WORD_LENGTH];
			length = 0;
		}
		
		public void addByte(byte newByte) throws DictionaryParserException
		{
			if (length >= MAX_WORD_LENGTH)
				throw new DictionaryParserException("Word is too long. Dictionary is invalid.");
			wordString[length] = newByte;
			length++;
		}
		
		public String getWord() throws CharacterCodingException
		{
			ByteBuffer wrappedBuffer = ByteBuffer.wrap(wordString, 0, length);
			return byteBufferToString(wrappedBuffer);
		}
		
		private String byteBufferToString(ByteBuffer byteBuffer) throws CharacterCodingException
		{
			Charset charset = Charset.forName("UTF-8");
			CharsetDecoder decoder = charset.newDecoder();
			return decoder.decode(byteBuffer).toString();
		}
	}
	
	private StarDictWord addWord(String wordString)
	{
		StarDictWord word = new StarDictWord(wordString);
		wordsList.add(word);
		return word;
	}
	
	public RawStarDictDictionary(InputStream ifoFile, InputStream idxFile, InputStream dictFile)
	{
		this.ifoFile = ifoFile;
		this.idxFile = idxFile;
		this.dictFile = dictFile;
	}
	
	public Dictionary parseAll()
	{
		parsedDict = new Dictionary();
		try {
			parseIFO();
//			checkParsedIFO();
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
		String ifoData = readEverythingStringFrom(ifoFile);
//		int regexpFlags = 0;
//		System.out.println(ifoData);
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
		try
		{
			byte newByte;
			WordBuffer wordString = new WordBuffer();
			byte [] dataOffset = new byte [idxoffsetbits/8]; // 8 or 4 bytes
			byte [] dataSize = new byte [4]; //4 bytes
			while (true)
			{
				newByte = getByteFrom(idxFile);
				if (newByte == WORD_ENDING)
				{
					StarDictWord newWord = addWord(wordString.getWord());
					wordString = new WordBuffer();
					readBytesIntoBuffer(dataOffset);
					newWord.addDataOffset(dataOffset);
					readBytesIntoBuffer(dataSize);
					newWord.addDataSize(dataSize);
				}
				else
				{
					wordString.addByte(newByte);
				}
			}
		} catch (EndOfFileException e) {}
	}

	private void readBytesIntoBuffer(byte [] buffer) throws EndOfFileException, IOException
	{
		for (int i = 0; i < buffer.length; ++i)
		{
			byte newByte = getByteFrom(idxFile);
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
	
	private void parseDICT(DictionaryFile dictFile) throws DictionaryParserException{
		System.out.println(dictFile.getValidPathToFile());
	}

	private String readEverythingStringFrom(InputStream fileStream) throws IOException
	{	
        byte[] buffer = readEverythingByteFrom(fileStream);
        String newst = convertToString(buffer);
        return newst;
	}

	private byte getByteFrom(InputStream fileStream) throws IOException, EndOfFileException
	{
		int readByte = fileStream.read();
		if (readByte < 0)
			throw new EndOfFileException();
		return (byte)readByte;
	}
	
	private byte[] readEverythingByteFrom(InputStream fileStream) throws IOException
	{
		int available = 0;
		available = fileStream.available();
        byte[] buffer = new byte[available];
        fileStream.read(buffer);
        return buffer;
	}

	private String convertToString(byte[] buffer) throws UnsupportedEncodingException
	{
		return new String(buffer, "UTF-8");
	}
}
