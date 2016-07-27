package com.panching.train.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panching.train.AreaActivity;
import com.panching.train.AsyncTask.OnTimeAsyncTack;
import com.panching.train.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by panching on 16/5/3.
 */
public class OnTimeFragment extends Fragment
{
    private static final String TIME_PATTERN = "HH:mm";
    TextView queryStation = null;
    TextView queryStartStationNorth = null;
    TextView queryEndStationNorth = null;
    TextView queryStartStationSouth = null;
    TextView queryEndStationSouth = null;

    String startStationID;
    String endStationID;
    String startStationName;
    String endStationName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container);
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.ontime_fragment, container, false);
        findViews(view);
        //防止fragment穿透 預設ontouch return false 改 true
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        queryStation.setOnTouchListener(onTouchListener);
        queryStartStationNorth.setOnTouchListener(onTouchListener);
        queryStartStationSouth.setOnTouchListener(onTouchListener);
        queryEndStationNorth.setOnTouchListener(onTouchListener);
        queryEndStationSouth.setOnTouchListener(onTouchListener);

        queryStation.setOnClickListener(onClickListener);
        queryStartStationNorth.setOnClickListener(onClickListener);
        queryEndStationNorth.setOnClickListener(onClickListener);
        queryStartStationSouth.setOnClickListener(onClickListener);
        queryEndStationSouth.setOnClickListener(onClickListener);

        SharedPreferences pre =  getActivity().getSharedPreferences("mainIndex", getActivity().MODE_PRIVATE);

        if(pre.getAll().size()==0)
        {
            startStationID = "1008";
            endStationID = "1238";
            startStationName = "台北";
            endStationName = "高雄";
        }
        else
        {
            String startRouteStr = pre.getString("0","");
            //fromStation
            String formInfo = startRouteStr.split(",")[0];
            //toStation
            String toInfo = startRouteStr.split(",")[1];
            startStationID = formInfo.split(":")[1].trim();
            endStationID = toInfo.split(":")[1].trim();
            startStationName = formInfo.split(":")[0].trim();
            endStationName = toInfo.split(":")[0].trim();
        }
        queryStartStationNorth.setText(startStationName);
        queryEndStationNorth.setText(endStationName);
        queryStartStationSouth.setText(startStationName);
        queryEndStationSouth.setText(endStationName);

        return view;
    }

    private void findViews(View view)
    {
        queryStation = (TextView)view.findViewById(R.id.queryStation);
        queryStartStationNorth =(TextView)view.findViewById(R.id.startStationNorth);
        queryEndStationNorth =(TextView)view.findViewById(R.id.endStationNorth);
        queryStartStationSouth =(TextView)view.findViewById(R.id.startStationSouth);
        queryEndStationSouth =(TextView)view.findViewById(R.id.endStationSouth);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(R.id.star);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TextView textView = (TextView)v;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
                textView.setTextColor(Color.parseColor("#d0d0d0"));
                textView.setBackgroundResource(R.drawable.ontime_rectangle_g);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
                textView.setTextColor(Color.parseColor("#ffffff"));
                textView.setBackgroundResource(R.drawable.ontime_rectangle);
            }
            return false;
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
            String queryDate = sdFormat.format(calendar.getTime());

            SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
            String Time = timeFormat.format(calendar.getTime());
            String queryTime = Time.replace(":", "");
            String queryStation = startStationID;

            ProgressDialog progressDialog = new ProgressDialog(getActivity());


            switch(v.getId())
            {
                case R.id.queryStation:
                {
                    Intent intent = new Intent(getActivity(), AreaActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("onTimeFlag","Y");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                }
                case R.id.startStationNorth:
                {
                    queryStation = startStationID;
                    String northUrl= "http://twtraffic.tra.gov.tw/twrail/SearchResult.aspx?searchtype=1&searchdate="+queryDate+"&fromstation="+queryStation+"&trainclass=undefined&traindirection=0&fromtime="+queryTime+"&totime=2359";
                    OnTimeAsyncTack onTimeAsyncTack = new OnTimeAsyncTack(northUrl,getActivity(),progressDialog,getActivity(),queryStation,endStationID,queryDate,Time);
                    onTimeAsyncTack.execute();
                    break;
                }
                case R.id.endStationNorth:
                {
                    queryStation = endStationID;
                    String northUrl= "http://twtraffic.tra.gov.tw/twrail/SearchResult.aspx?searchtype=1&searchdate="+queryDate+"&fromstation="+queryStation+"&trainclass=undefined&traindirection=0&fromtime="+queryTime+"&totime=2359";
                    OnTimeAsyncTack onTimeAsyncTack = new OnTimeAsyncTack(northUrl,getActivity(),progressDialog,getActivity(),queryStation,endStationID,queryDate,Time);
                    onTimeAsyncTack.execute();
                    break;
                }
                case R.id.startStationSouth:
                {
                    queryStation = startStationID;
                    String southUrl= "http://twtraffic.tra.gov.tw/twrail/SearchResult.aspx?searchtype=1&searchdate="+queryDate+"&fromstation="+queryStation+"&trainclass=undefined&traindirection=1&fromtime="+queryTime+"&totime=2359";
                    OnTimeAsyncTack onTimeAsyncTack = new OnTimeAsyncTack(southUrl,getActivity(),progressDialog,getActivity(),queryStation,endStationID,queryDate,Time);
                    onTimeAsyncTack.execute();
                    break;
                }
                case R.id.endStationSouth:
                {
                    queryStation = endStationID;
                    String southUrl= "http://twtraffic.tra.gov.tw/twrail/SearchResult.aspx?searchtype=1&searchdate="+queryDate+"&fromstation="+queryStation+"&trainclass=undefined&traindirection=1&fromtime="+queryTime+"&totime=2359";
                    OnTimeAsyncTack onTimeAsyncTack = new OnTimeAsyncTack(southUrl,getActivity(),progressDialog,getActivity(),queryStation,endStationID,queryDate,Time);
                    onTimeAsyncTack.execute();
                    break;
                }
                default:
                    System.out.println("default");
            }
        }
    };
}
