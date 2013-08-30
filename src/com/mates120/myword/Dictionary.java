package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
	private String name;
	private List<SourceWord> words;
	
	public Dictionary(String name){
		this.name = name;
		words = new ArrayList<SourceWord>();
		boolean searchIn = true;
	}
	
	public void addWord(String word_source, List<String> valuesInString){
		this.words.add(new SourceWord(word_source, valuesInString));
	}
	
	public String getName(){
		return this.name;
	}
	
	public List<SourceWord> getWords(){
		return this.words;
	}
	
	public SourceWord getWord(int index){
		return words.get(index);
	}
}
