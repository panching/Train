package com.panching.train;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.panching.train.Adapter.SamplePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class RouteDirectionTabLayoutActivity extends AppCompatActivity {

    View page2View;
    List tabViewList;
    ViewPager mViewPager;
    Boolean searchType;

    private android.support.design.widget.TabLayout mTabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_tablayout_direction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LayoutInflater mInflater = getLayoutInflater().from(this);

        final View v1 = mInflater.inflate(R.layout.pager_item, null);
        final View v2 = mInflater.inflate(R.layout.pager_item2, null);
        page2View = v2;

        tabViewList = new ArrayList<View>();
        tabViewList.add(v1);
        tabViewList.add(v2);


        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabs = (android.support.design.widget.TabLayout) findViewById(R.id.tabs);
        mTabs.addTab(mTabs.newTab().setText("北上"));
        mTabs.addTab(mTabs.newTab().setText("南下"));

        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0)
                    searchType = false;
                else
                    searchType = true;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        mViewPager.setAdapter(new SamplePagerAdapter(getApplicationContext(),tabViewList));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
    }

}
