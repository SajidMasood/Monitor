package com.mr_abdali.monitor;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.mr_abdali.monitor.adapters.ViewPagerAdapter;
import com.mr_abdali.monitor.Fragments.FragmentCallogs;
import com.mr_abdali.monitor.Fragments.FragmentContacts;

/**
 * Created by sadiq on 29/05/2018.
 */

public class TabActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private final int[] ICONS={R.drawable.ic_call,R.drawable.ic_person1,R.drawable.ic_star};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity);

        tabLayout=(TabLayout) findViewById(R.id.TabLayout);
        viewPager=(ViewPager)findViewById(R.id.Viewpager);

        ViewPagerAdapter adapter= new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentCallogs(),"Calls");
        adapter.addFragment(new FragmentContacts(),"contacts");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for(int i=0; i<tabLayout.getTabCount();i++)
        {
            TabLayout.Tab tab=tabLayout.getTabAt(i);
            tab.setIcon(ICONS[i]);
        }

    }
}

