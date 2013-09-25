package com.mates120.myword;

import java.util.List;

public class SourceWord {
	private String source;
	private List<String> values;
	private String value;
	
	public SourceWord(String source, List<String> values){
		this.source = source;
		this.values = values;
	}
	
	public SourceWord(String source, String value){
		this.source = source;
		this.value = value;
	}
	
	public String getSource(){
		return source;
	}
	
	public String getValue(){
		return value;
	}
	
	public List<String> getValues(){
		return values;
	}
}
