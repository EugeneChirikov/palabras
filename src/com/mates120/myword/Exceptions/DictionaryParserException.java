package com.mates120.myword.Exceptions;


public class DictionaryParserException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1338845442367684432L;
	public DictionaryParserException(String message)
	{
		super(message);
	}
	public DictionaryParserException() {
		// TODO Auto-generated constructor stub
	}
	public String getMessage()
	{
		return super.getMessage();
	}
}