package com.mr_abdali.monitor.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadiq on 18/05/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
   private List<Fragment> listFragment= new ArrayList<>();
    private List<String> listTitles=new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitles.get(position);
    }


    public void addFragment(Fragment fragment, String title )
    {
        listFragment.add(fragment);
        listTitles.add(title);

    }
}
