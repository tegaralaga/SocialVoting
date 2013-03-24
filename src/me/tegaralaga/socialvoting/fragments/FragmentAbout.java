package me.tegaralaga.socialvoting.fragments;

import me.tegaralaga.socialvoting.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class FragmentAbout extends SherlockFragment {
	
	private LayoutInflater __inflater;
	private View __view;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		__inflater=inflater;
		__view = __inflater.inflate(R.layout.fragment_about,container,false);
		return __view;
	}
}
