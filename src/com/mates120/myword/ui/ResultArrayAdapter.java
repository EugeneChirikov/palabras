package com.mates120.myword.ui;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mates120.myword.Word;

public class ResultArrayAdapter extends ArrayAdapter<Word>{
	private Context context;

	public ResultArrayAdapter(Context context, 
			int textViewResourceId, List<Word> words){
		super(context, textViewResourceId, words);
		this.context = context;
	}
	
	private class ViewHolder {
		public TextView word;
		public TextView value;
		public TextView dictionary;
	}
	
	
}
