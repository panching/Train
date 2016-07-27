package com.panching.train.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.panching.train.R;

import java.util.List;

/**
 * Created by panching on 16/4/26.
 *
 *
 mTabs = (android.support.design.widget.TabLayout) findViewById(R.id.tabs);
 mTabs.addTab(mTabs.newTab().setText("離線"));
 mTabs.addTab(mTabs.newTab().setText("線上"));

 mViewPager = (ViewPager) findViewById(R.id.viewpager);
 mViewPager.setAdapter(new SamplePagerAdapter(getApplicationContext()));
 mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));

 */
public class SamplePagerAdapter extends PagerAdapter {

    private List<View> mList;
    private Context mContext;
    ListView listView;
    TextView startStation;
    TextView endStation;
    TextView north;
    TextView south;
    String startStationID;
    String endStationID;

    public SamplePagerAdapter(Context context,List list) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

//    public Parcelable saveState() {
//        return null;
//    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mList.get(position);
        if(position==0)//離線
        {

        }
        else//線上
        {
            startStation = (TextView)view.findViewById(R.id.page2_StartStation);
            endStation = (TextView)view.findViewById(R.id.page2_EndStation);
            north = (TextView)view.findViewById(R.id.page2_north);
            south = (TextView)view.findViewById(R.id.page2_south);
            SharedPreferences pre = mContext.getSharedPreferences("mainIndex", mContext.MODE_PRIVATE);
            if(pre.getAll().size()>0)
            {
                String startRouteStr = pre.getString("0","");
                //fromStation
                String formInfo = startRouteStr.split(",")[0];
                //toStation
                String toInfo = startRouteStr.split(",")[1];
                startStation.setText(formInfo.split(":")[0].trim());
                endStation.setText(toInfo.split(":")[0].trim());
                startStationID = formInfo.split(":")[1].trim();
                endStationID = toInfo.split(":")[1].trim();
            }
            else
            {
                startStation.setText("台北");
                endStation.setText("高雄");
                startStationID = "1008";
                endStationID = "1238";
            }
        }
        container.addView(view);
        return view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Item " + (position + 1);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
