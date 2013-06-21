package com.mates120.myword;

import java.util.List;

public class SourceWord {
	private String source;
	private List<String> values;
	
	SourceWord(String source, List<String> values){
		this.source = source;
		this.values = values;
	}
	
	public String getSource(){
		return source;
	}
	
	public List<String> getValues(){
		return values;
	}
}
