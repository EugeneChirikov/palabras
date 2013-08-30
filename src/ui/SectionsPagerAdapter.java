package ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
	FragmentManager fm;
	
    public SectionsPagerAdapter(FragmentManager fm){
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int i) {
    	ListFragment fragment;
    	Bundle args;
        switch (i) {
            case 0:
            	fragment = new SearchFragment();
                args = new Bundle();
                args.putInt(SearchFragment.ARG_SECTION_NUMBER, i + 1);
                fragment.setArguments(args);
                return fragment;
                
            case 1:
                fragment = new SettingsFragment();
                args = new Bundle();
                args.putInt(SettingsFragment.ARG_SECTION_NUMBER, i + 1);
                fragment.setArguments(args);
                return fragment;

            default:
                // The other sections of the app are dummy placeholders.
                fragment = new SearchFragment();
                args = new Bundle();
                args.putInt(SearchFragment.ARG_SECTION_NUMBER, i + 1);
                fragment.setArguments(args);
                return fragment;
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