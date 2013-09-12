package com.mates120.myword.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
	private SearchFragment searchFragment = new SearchFragment();
	private SettingsFragment settingsFrament = new SettingsFragment();
	
    public SectionsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
    	Bundle args;
        switch (i) {
            case 0:
                args = new Bundle();
                args.putInt(SearchFragment.ARG_SECTION_NUMBER, i + 1);
                searchFragment.setArguments(args);
                return searchFragment;
                
            case 1:
                return settingsFrament;

            default:
                // The other sections of the app are dummy placeholders.
            	args = new Bundle();
                args.putInt(SearchFragment.ARG_SECTION_NUMBER, i + 1);
                searchFragment.setArguments(args);
                return searchFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Section " + (position + 1);
    }
}