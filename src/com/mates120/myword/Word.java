package com.mates120.myword;

import java.util.ArrayList;
import java.util.List;

public class Word {
	private long id;
	private String source;
	private int rating;
	private int searchCount;
	private List<Value> values;
	
	public Word(){
		
	}
	
	public Word(String source, List<String> values, String dict_name){
		this.source = source;
		this.values = new ArrayList<Value>();
		this.setValues(values, dict_name);
	}
	
	public Word(String source){
		this.id = 0;
		this.source = source;
		this.rating = 0;
		this.searchCount = 0;
		this.values = new ArrayList<Value>();
	}
	
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
	
	public void setSearchCount(int searchCount){
		this.searchCount = searchCount;
	}
	
	public int getSearchCount(){
		return this.searchCount;
	}
	
	public int getRating(){
		return rating;
	}
	
	public void setRating(int rating){
		this.rating = rating;
	}
	
	public void setValues(List<Value> values){
		this.values = values;
	}
	
	public void setValues(List<String> values, String dict_name){
		for(int i = 0; i < values.size(); i ++){
			this.values.add(new Value(values.get(i), dict_name));
		}
	}
	
	public List<Value> getValues(){
		return this.values;
	}
	
	@Override
	public String toString(){
		return source;
	}
}
