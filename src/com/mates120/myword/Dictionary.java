package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
	private String name;
	private List<Word> words;
	
	public Dictionary(String name){
		this.name = name;
	}
	
	public void addWord(String word_source, List<String> valuesInString){
		List <Value> values = new ArrayList<Value>();
		for (int i = 0; i < valuesInString.size(); i++)
			values.add(new Value(valuesInString.get(i), name));
		this.words.add(new Word(word_source, values));
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
}
