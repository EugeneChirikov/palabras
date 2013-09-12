package com.mates120.myword;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import com.mates120.myword.Exceptions.DictionaryParserException;

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