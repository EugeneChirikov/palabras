package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
	private String name;
	private List<Word> words;
	
	public Dictionary(String name){
		this.name = name;
		words = new ArrayList<Word>();
	}	

	public Dictionary(){		
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
}
