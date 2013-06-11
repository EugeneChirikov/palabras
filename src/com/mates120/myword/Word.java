package com.mates120.myword;

public class Word {
	private long id;
	private String source;
	private int rating;
	private int searchCount;
	
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
	
	@Override
	public String toString(){
		return source;
	}
}
