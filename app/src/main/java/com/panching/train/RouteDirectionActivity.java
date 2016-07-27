package com.panching.train;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.panching.train.AsyncTask.OnTimeAsyncTack;
import com.panching.train.obj.ContextObj;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RouteDirectionActivity extends AppCompatActivity {

    ListView listView;
    List list;
    ArrayAdapter arrayAdapter;
    ArrayList stationNameTwList = new ArrayList();
    private static final String TIME_PATTERN = "HH:mm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_direction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("請選擇乘車方向");
        toolbar.setNavigationIcon(R.drawable.ic_reply_white_18dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteDirectionActivity.this.finish();
            }
        });


        ListView listView = (ListView)findViewById(R.id.directionListView);
        final String endStationID = "";

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        final String queryDate = sdFormat.format(calendar.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        final String Time = timeFormat.format(calendar.getTime());
        final String queryTime = Time.replace(":", "");
        final String queryStation = getIntent().getStringExtra("stationID");
        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());

        stationNameTwList.add("北上");
        stationNameTwList.add("南下");

        arrayAdapter =new ArrayAdapter(this,R.layout.arrayadapter_layout,R.id.listTextView1,stationNameTwList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String southUrl= "http://twtraffic.tra.gov.tw/twrail/SearchResult.aspx?searchtype=1&searchdate="+queryDate+"&fromstation="+queryStation+"&trainclass=undefined&traindirection="+position+"&fromtime="+queryTime+"&totime=2359";
                OnTimeAsyncTack onTimeAsyncTack = new OnTimeAsyncTack(southUrl,getApplicationContext(),progressDialog,RouteDirectionActivity.this,queryStation,endStationID,queryDate,Time);
                onTimeAsyncTack.execute();
            }
        });
    }

}
