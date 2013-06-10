package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

public class Word {
	private long id;
	private String source;
	private int rating;
	private boolean firstCase;
	private List<String> values = new ArrayList<String>();
	
	/*public Word(String source, String transcription, String translation){
		this.source = source;
		this.transcription = transcription;
		this.translation = translation;
		this.firstCase = false;
		this.rating = 0;
	}*/
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getSource(){
		return source;
	}
	
	public void setSource(String source){
		this.source = source;
	}
	
	public void setFirst(){
		this.firstCase = true;
	}
	
	public boolean isFirst(){
		return this.firstCase;
	}
	
	public int getRating(){
		return rating;
	}
	
	public void setRating(int rating){
		this.rating = rating;
	}
	
	public List<String> getValues(){
		return values;
	}
	
	public void setValues(List <String> values){
		this.values.clear();
		this.values.addAll(values);
	}
	
	@Override
	public String toString(){
		return source;
	}
}
