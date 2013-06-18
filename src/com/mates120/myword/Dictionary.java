package com.mates120.myword;

import java.util.List;

public class Dictionary {
	private String name;
	private List<Word> words;
	
	public Dictionary(String name){
		this.name = name;
	}
	
	public Dictionary(){		
	}

	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void addWord(String word_source, List<String> values){
		this.words.add(new Word(word_source, values, name));
	}
	
	public List<Word> getWords(){
		return this.words;
	}
}
