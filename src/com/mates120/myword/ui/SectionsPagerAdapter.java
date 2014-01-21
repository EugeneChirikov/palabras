package com.mates120.myword.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
	
	private Fragment searchFragment = new SearchFragment();
	private Fragment settingsFrament = new SettingsFragment();
	
    public SectionsPagerAdapter(FragmentManager fm){
        super(fm);
//        fm.beginTransaction().add(searchFragment, searchTag).commit();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return searchFragment;
                
            case 1:
                return settingsFrament;

            default:
                // The other sections of the app are dummy placeholders.
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