package com.example.corbi.dartevent.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

// Implementation of PagerAdapter that represents each page as a Fragment that is persistently
// kept in the fragment manager as long as the user can return to the page.
// This version of the pager is best for use when there are a handful of typically more static
// fragments to be paged through, such as a set of tabs. The fragment of each page the user
// visits will be kept in memory.

public class ActionTabsViewPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = ActionTabsViewPagerAdapter.class.getSimpleName();;
    private ArrayList<Fragment> fragments;

    public static final int MyEvents = 0;
    public static final int FriendEvents = 1;
    public static final int PublicEvents = 2;
    public static final String UI_TAB_MyEvents = "MyEvents";
    public static final String UI_TAB_FriendEvents = "FriendEvents";
    public static final String UI_TAB_PublicEvents = "PublicEvents";


    public ActionTabsViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    // Return the Fragment associated with a specified position.
    @Override
    public Fragment getItem(int pos){
       // Log.d(TAG, "getItem " + "position" + pos);
        return fragments.get(pos);
    }

    // Return the number of views available
    public int getCount(){
        //Log.d(TAG, "getCount " + "size " + fragments.size());
        return fragments.size();
    }

    // This method may be called by the ViewPager to obtain a title string
    // to describe the specified page
    public CharSequence getPageTitle(int position) {
        Log.d(TAG, "getPageTitle " + "position " + position);
        switch (position) {
            case MyEvents:
                return UI_TAB_MyEvents;
            case FriendEvents:
                return UI_TAB_FriendEvents;
            case PublicEvents:
                return UI_TAB_PublicEvents;
            default:
                break;
        }
        return null;
    }
}
