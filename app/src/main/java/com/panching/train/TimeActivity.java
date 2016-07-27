package com.panching.train;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panching.train.Adapter.TimeContentAdapter;
import com.panching.train.obj.TimeInfoObj;

import java.util.ArrayList;
import java.util.List;

public class TimeActivity extends AppCompatActivity {

    String sStartSatation = "";
    String sEndSatation = "";
    List queryList = new ArrayList();
    List timeList = null ;
    ListView listView;
    TextView tvCarClass;
    TextView tvTrain;
    TextView tvLine;
    TextView tvFromStation;
    TextView tvToStation;
    ImageView imgArrow;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        findViews();
        //        trainInfoList.add(mList.get(position).get("tvTrain")+","+(String)mList.get(position).get("tvCarclass")+","+mList.get(position).get("tvLine"));
        Bundle bundle = this.getIntent().getExtras();
        String stateInfo = (String)bundle.getString("stateList");
        String sLine = stateInfo.split(",")[0];
        String sCarClass = bundle.getString("carClass");
        String sTitle = bundle.getString("title");
        String searchType = bundle.getString("searchType")==null?"onLine":bundle.getString("searchType");
        timeList = ((ArrayList)getIntent().getSerializableExtra("timeList"));

        int iTrain = bundle.getInt("train");
        queryList = doQueryTimeList(iTrain,searchType);

        tvTrain.setText(iTrain+"");
        tvCarClass.setText(sCarClass);
        //設定山線海線
        if(sLine.equals("1")||sLine.equals("山"))
        {
            tvLine.setText("山線");
        }
        else if (sLine.equals("2")||sLine.equals("海"))
        {
            tvLine.setText("海線");
        }
        else
        {
            tvLine.setText("--");
        }
        tvFromStation.setText(sStartSatation);
        tvToStation.setText(sEndSatation);

        //隱藏listview的底線
        listView.setDivider(null);
        listView.setAdapter(new TimeContentAdapter(this, queryList ,sTitle,sCarClass ));

        toolbar.setNavigationIcon(R.drawable.ic_reply_white_18dp);
        toolbar.setTitle("列車資訊");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeActivity.this.finish();
            }
        });
    }

    public void findViews() {
        listView = (ListView)findViewById(R.id.listView);
        tvCarClass =(TextView)findViewById(R.id.tvCarclassBar);
        tvTrain =(TextView)findViewById(R.id.tvTrainBar);
        tvLine =(TextView)findViewById(R.id.tvLineBar);
        tvFromStation =(TextView)findViewById(R.id.tvFromStation);
        tvToStation =(TextView)findViewById(R.id.tvToStation);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        imgArrow = (ImageView)findViewById(R.id.arrow);
    }

    public List doQueryTimeList(int train,String searchType)
    {
        try
        {
            if(searchType.equals("offLine"))
            {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                dataBaseHelper.openDataBase();
                queryList = dataBaseHelper.getTimeList(train);
                TimeInfoObj timeInfoObj = (TimeInfoObj)queryList.get(0);
                sStartSatation = timeInfoObj.getStationName();
                timeInfoObj = (TimeInfoObj)queryList.get(queryList.size()-1);
                sEndSatation = timeInfoObj.getStationName();
            }
            else
            {
                queryList = timeList;
                sStartSatation = getIntent().getStringExtra("sStartSatation");
                sEndSatation = getIntent().getStringExtra("sEndSatation");
            }
        }
        catch (Exception e)
        {
            Log.d("ex=====","Ex====",e);
        }
        return queryList;
    }

}
