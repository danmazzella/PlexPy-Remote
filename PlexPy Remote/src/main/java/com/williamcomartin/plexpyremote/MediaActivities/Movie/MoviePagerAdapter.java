package com.williamcomartin.plexpyremote.MediaActivities.Movie;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.williamcomartin.plexpyremote.SharedFragments.HistoryFragment;

public class MoviePagerAdapter extends FragmentStatePagerAdapter {

    private final String[] tabNames = {"Profile", "History"};

    private String ratingKey;

    MoviePagerAdapter(FragmentManager fm, String ratingKey) {
        super(fm);

        this.ratingKey = ratingKey;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                MovieProfileFragment profileFrag = new MovieProfileFragment();
                profileFrag.setRatingKey(ratingKey);
                return profileFrag;
            case 1:
                HistoryFragment historyFrag = new HistoryFragment();
                historyFrag.setRatingKey(ratingKey);
                return historyFrag;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }

    @Override
    public int getCount() {
        return tabNames.length;
    }
}
