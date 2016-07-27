package com.panching.train;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.panching.train.obj.AreaObj;
import com.panching.train.obj.ContextObj;
import com.panching.train.obj.StationObj;

import java.util.ArrayList;
import java.util.List;

public class StationActivity extends AppCompatActivity {

    ListView listView;
    List list;
    ArrayAdapter arrayAdapter;
    ArrayList stationNameTwList = new ArrayList();
    ArrayList stationIdList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_reply_white_18dp);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StationActivity.this.finish();
            }
        });

        findViews();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());

        try
        {
            dataBaseHelper.openDataBase();
            Bundle bundle = getIntent().getExtras();
            int areaID = bundle.getInt("AreaID");
            String onTimeFlag = bundle.getString("onTimeFlag");
            list = dataBaseHelper.getStationList(areaID);
            for(int i =0;i<list.size();i++)
            {
                StationObj stationObj = (StationObj)list.get(i);

                stationNameTwList.add(stationObj.getNameTW());
                stationIdList.add(stationObj.getStation());
            }

            arrayAdapter = new ArrayAdapter(this,R.layout.arrayadapter_layout,R.id.listTextView1,stationNameTwList);
            listView.setAdapter(arrayAdapter);
            if(onTimeFlag == null)
            {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putString("stationName",(String) stationNameTwList.get(position));
                        bundle.putString("stationID",String.valueOf((Integer) stationIdList.get(position)));
                        intent.putExtras(bundle);
                        setResult(1, intent); //requestCode需跟A.class的一樣
                        StationActivity.this.finish();
                    }
                });
            }
            else
            {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(StationActivity.this,RouteDirectionActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("stationID", String.valueOf((Integer) stationIdList.get(position)));
                        intent.putExtras(bundle);
                        StationActivity.this.finish();
                        startActivity(intent);

                    }
                });
            }
        }
        catch (Exception e)
        {
            Log.d("exception", "=", e);
        }
    }

    public void findViews(){
        listView = (ListView)findViewById(R.id.lvStation);
    }
}
