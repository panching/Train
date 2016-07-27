package com.panching.train.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ListView;
import android.widget.Toast;

import com.panching.train.Adapter.StarRouteAdapter;
import com.panching.train.R;
import com.panching.train.obj.ContextObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by panching on 16/5/3.
 */
public class MyStartRouteFragment extends Fragment
{
    static ListView listView ;
    Boolean removeFlag = false;
    View view;
    static List stationList;
    public static StarRouteAdapter starRouteAdapter;
    static Context context = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        String str = this.getArguments().getString("preStr");
//        //des :高雄:1238,臺北:1008 & 臺北:1008,高雄:1238
        String strArray[] = null;
        if(str.length()>0)
        {
            stationList = new ArrayList();
            strArray = str.split("&");
            for(int i = 0 ;i<strArray.length;i++)
            {
                stationList.add(strArray[i]);
            }
            ContextObj contextObj = (ContextObj)this.getArguments().getSerializable("contextObj");
            context = contextObj.getContext();
            starRouteAdapter = new StarRouteAdapter(stationList,context,removeFlag);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container);
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.my_start_route_fragment, container, false);
        //防止fragment穿透 預設ontouch return false 改 true
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        findViews(view);
        listView.setAdapter(starRouteAdapter);
        return view;
    }

    public void findViews(View view)
    {
        listView = (ListView)view.findViewById(R.id.lvStarRoute);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(R.id.star);
        inflater.inflate(R.menu.route_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(removeFlag)removeFlag=false;else removeFlag=true;

        if(stationList!=null)
        {
            changeView();
            if(removeFlag && stationList.size()>0)
                Toast.makeText(context,"請選擇刪除路線",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public View changeView()
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.my_start_route_fragment, null, false);
        starRouteAdapter = new StarRouteAdapter(stationList,context,removeFlag);
        listView.setAdapter(starRouteAdapter);
        return view;
    }


    public void removeListItem( final int positon) {

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {

            @Override
            public void run() {
                stationList.remove(positon);
                starRouteAdapter.notifyDataSetChanged();
            }
        }, 100);
    }
}
