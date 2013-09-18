package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
	private String name;

	private List<SourceWord> words;
	private boolean searchIn;
	
	public Dictionary(String name){
		this.name = name;
		words = new ArrayList<SourceWord>();
		searchIn = true;
	}
	
	public Dictionary(String name, int searchIn){
		this.name = name;
		if (searchIn == 1)
			this.searchIn = true;
		else
			this.searchIn = false;
	}

	public void setName(String name){
		this.name = name;
	}

	public void addWord(String word_source, List<Value> valuesInString)
	{
		this.words.add(new Word(word_source, valuesInString));

	}
	
	public String getName(){
		return this.name;
	}
	
	public List<Word> getWords(){


		return this.words;
	}
	
	public Word getWord(int index){
		return words.get(index);
	}
	
	public boolean isSearchIn(){
		return this.searchIn;
	}
	
	public void setSearchIn(boolean searchIn){
		this.searchIn = searchIn;
	}
}
