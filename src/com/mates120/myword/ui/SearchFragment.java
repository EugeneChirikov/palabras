package com.mates120.myword.ui;

import java.util.ArrayList;
import java.util.List;

import com.mates120.myword.DictionaryManager;
import com.mates120.myword.R;
import com.mates120.myword.Value;
import com.mates120.myword.Word;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the
 * ListView with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class SearchFragment extends ListFragment{
	
	private DictionaryManager dictionaryManager;
	private EditText editText;
	
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	static final String ARG_SECTION_NUMBER = "param1";

	// TODO: Rename and change types of parameters
	private String mParam1;

	private OnSearchFragmentInteractionListener mListener;


	// TODO: Rename and change types of parameters
	public static SearchFragment newInstance(String param1, String param2) {
		SearchFragment fragment = new SearchFragment();
		Bundle args = new Bundle();
		args.putString(ARG_SECTION_NUMBER, param1);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_SECTION_NUMBER);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search_list, container, false);
		
		dictionaryManager = new DictionaryManager(this.getActivity());
		editText = (EditText) view.findViewById(R.id.editTextSearch);
		editText.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
		    	ResultArrayAdapter adapter;
		    	List<Value> values = null;
		    	Word word = null;
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		            word = dictionaryManager.getWord(editText.getText().toString());
		            if(word != null){
		            	values = new ArrayList<Value>();
		            	for(int i = 0; i < word.getValues().size(); i++)
		            		values.add(word.getValue(i));
		            }
		            handled = true;
		        }
		        if (word != null){
		        	adapter = new ResultArrayAdapter(getActivity(),
		        			android.R.id.list, values, word.getSource());
		        }
		        else{
		        	List<Value> emptyValue = new ArrayList<Value>();
		        	emptyValue.add(new Value("",""));
		        	adapter = new ResultArrayAdapter(getActivity(),
		        			android.R.id.list, emptyValue,
		        			"No such word in the dictionary.");
		        }
		        setListAdapter(adapter);
		        return handled;
		    }
		});

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnSearchFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnSearchFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(String id);

		void onFragmentInteraction(Uri uri);
	}

}
