package com.mates120.myword;

public class Dictionary {
	private long id;
	private String name;
	private String app;
	private boolean searchIn;
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getApp(){
		return app;
	}
	
	public void setApp(String app){
		this.app = app;
	}
	
	public boolean isSearched(){
		return searchIn;
	}
	
	public void setSearched(boolean searchIn){
		this.searchIn = searchIn;
	}
}
