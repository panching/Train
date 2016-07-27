package com.panching.train;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.panching.train.AsyncTask.TrainAsyncTask;
import com.panching.train.Fragment.MyStartRouteFragment;
import com.panching.train.Fragment.OnTimeFragment;
import com.panching.train.Fragment.SettingFragment;
import com.panching.train.obj.ContextObj;
import com.panching.train.obj.DataObj;
import com.panching.train.tool.PCTool;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    private static final String TIME_PATTERN = "HH:mm";
    TextView startStationName ;
    TextView endStationName ;
    Toolbar toolbar;
//    TextView page2_startStationName ;
//    TextView page2_endStationName ;
    TextView btDate ;
    TextView btTime ;
    TextView btSubmit;
    ImageView imgChange;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    public String startStationID;
    public String endStationID;
    String queryTime;
    String queryDate;

    Boolean searchType =false;//false == offline

    private android.support.design.widget.TabLayout mTabs;
//Remove title bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    //set content view AFTER ABOVE sequence (to avoid crash)
//        this.setContentView(R.layout.activity_main);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataBaseHelper myDbHelper = new DataBaseHelper(this);

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("查詢時刻表");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        findViews();

        SharedPreferences pre =  getSharedPreferences("mainIndex",MODE_PRIVATE);

        if(pre.getAll().size()==0)
        {
            startStationID = "1008";
            endStationID = "1238";
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
            startStationName.setText(formInfo.split(":")[0].trim());
            endStationName.setText(toInfo.split(":")[0].trim());
        }

        //設定時間
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        updateDate();
        updateTime();

        startStationName.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AreaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("flag", "startStation");
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        }));
        endStationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AreaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("flag", "endStation");
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryTrainList(searchType);
            }
        });
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TextView textView = (TextView)v;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
                    textView.setTextColor(Color.parseColor("#d0d0d0"));
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
                    textView.setTextColor(Color.parseColor("#ffffff"));
                }
                return false;
            }
        };

        imgChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String IDtemp = "";
                CharSequence textTemp = "";

                IDtemp = startStationID;
                startStationID = endStationID;
                endStationID = IDtemp;

                textTemp = startStationName.getText();
                startStationName.setText(endStationName.getText());
                endStationName.setText(textTemp);
            }
        });

        startStationName.setOnTouchListener(onTouchListener);
        endStationName.setOnTouchListener(onTouchListener);
        btSubmit.setOnTouchListener(onTouchListener);
        btDate.setOnTouchListener(onTouchListener);
        btTime.setOnTouchListener(onTouchListener);

        mTabs = (android.support.design.widget.TabLayout) findViewById(R.id.tabs);
        mTabs.addTab(mTabs.newTab().setText("離線查"));
        mTabs.addTab(mTabs.newTab().setText("線上查"));

        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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

    }
    //接收回傳值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(resultCode){//resultCode是剛剛妳A切換到B時設的resultCode
            case 0://當B傳回來的Intent的requestCode 等於當初A傳出去的話
                if(data != null)
                {
                    String stationName = data.getExtras().getString("stationName");
                    String stationID = data.getExtras().getString("stationID");
                    String flag = data.getExtras().getString("flag");
                    if(flag.equals("startStation"))
                    {
                        startStationID = stationID;
                        startStationName.setText(stationName);
                    }
                    else if(flag.equals("endStation"))
                    {
                        endStationID = stationID;
                        endStationName.setText(stationName);
                    }
                }
                break;
        }
    }

    public void findViews()
    {
        startStationName = (TextView)findViewById(R.id.tvStartStation);
        endStationName = (TextView)findViewById(R.id.tvEndStation);
//        page2_startStationName = (TextView)page2View.findViewById(R.id.page2_StartStation);
//        page2_endStationName = (TextView)page2View.findViewById(R.id.page2_EndStation);
        btDate = (TextView)findViewById(R.id.tvDate);
        btTime = (TextView)findViewById(R.id.tvTime);
        btSubmit = (TextView)findViewById(R.id.submitBt);
        imgChange = (ImageView)findViewById(R.id.imgChange);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

// toolbar setting
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.star) {
            Toast toast = null;
            SharedPreferences pre = getSharedPreferences("starRoute",MODE_APPEND);
            SharedPreferences.Editor preEdit = pre.edit();
            if(pre.getAll().size()!=0)
            {
                Boolean flag = false;
                for (int i =0 ;i<pre.getAll().size();i++)
                {
                    String sRoute = pre.getString(String.valueOf(i),"");
                    if(sRoute.equals(startStationName.getText()+":"+startStationID + "," + endStationName.getText() +":"+endStationID))
                    {
                        toast= Toast.makeText(MainActivity.this, "常用路線已存在！", Toast.LENGTH_LONG);
                        flag = true;
                        break;
                    }
                }
                if(flag!=true)
                {
                    preEdit.putString(String.valueOf(pre.getAll().size()), startStationName.getText()+":"+startStationID + "," + endStationName.getText() +":"+endStationID);
                    toast = Toast.makeText(MainActivity.this, "已新增至常用路線！", Toast.LENGTH_LONG);
                    preEdit.commit();
                }
            }
            else
            {
                preEdit.putString("0", startStationName.getText()+":"+startStationID + "," + endStationName.getText() +":"+endStationID);
                toast= Toast.makeText(MainActivity.this, "已新增至常用路線！", Toast.LENGTH_LONG);
                preEdit.commit();
            }

            toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static Fragment mainFragment = null;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        int id = item.getItemId();

        ContextObj myContext = new ContextObj();
        myContext.setContext(getApplicationContext());
        if (id == R.id.searchTrain) {
            fragment = null;
            toolbar.setTitle("查詢時刻表");
        } else if (id == R.id.myStartRoute)
        {
            String preStr = "";
            fragment = new MyStartRouteFragment();
            mainFragment = fragment;
            toolbar.setTitle("常用車站");
            //取得所有常用車站
            SharedPreferences pre =  getSharedPreferences("starRoute",MODE_APPEND);
            for(int i=0;i<pre.getAll().size();i++)
            {
                preStr += pre.getString(String.valueOf(i), "");
                if(i<(pre.getAll().size()-1))
                    preStr += " & ";
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("preStr",preStr);
            bundle.putSerializable("contextObj",myContext);
            fragment.setArguments(bundle);
        } else if (id == R.id.onTime) {
            fragment = new OnTimeFragment();
            mainFragment = fragment;
            toolbar.setTitle("即時列車資訊");
        }
        else if (id == R.id.mySetting)
        {
            fragment = new SettingFragment();
            mainFragment = fragment;
            toolbar.setTitle("我的設定");
        }
//        else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
////        }
        FragmentManager fragmentManager = getFragmentManager();
        if(fragment == null && mainFragment !=null)
        {
            fragmentManager.beginTransaction().remove(mainFragment).commit();
//how do I set MainActivity stationName & endStationName & startStationID & endStationID when I click Item in starRouteAdapter????
            SharedPreferences pre = getSharedPreferences("mainIndex", MODE_PRIVATE);
            if(pre.getAll().size()>0)
            {
                String startRouteStr = pre.getString("0","");
                //fromStation
                String formInfo = startRouteStr.split(",")[0];
                //toStation
                String toInfo = startRouteStr.split(",")[1];

                startStationName.setText(formInfo.split(":")[0].trim());
//                page2_startStationName.setText(formInfo.split(":")[0].trim());
                startStationID = formInfo.split(":")[1].trim();
                endStationName.setText(toInfo.split(":")[0].trim());
//                page2_endStationName.setText(toInfo.split(":")[0].trim());
                endStationID = toInfo.split(":")[1].trim();
            }
        }
        else if(mainFragment !=null)
        {
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
        設定時間日期選擇器部分
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
        需override two mathod "onDateSet & onTimeSet"
    */
    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        updateDate();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        updateTime();
    }

    private void updateDate() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        queryDate = sdFormat.format(calendar.getTime());
        btDate.setText(sdFormat.format(calendar.getTime()));
    }
    private void updateTime() {
        queryTime = timeFormat.format(calendar.getTime());
        btTime.setText(timeFormat.format(calendar.getTime()));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvDate:
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
            case R.id.tvTime:
                TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;
        }
    }

    /*
        主要查詢部分
     */
    public void  queryTrainList(Boolean searchType)
    {
        ArrayList trainList = new ArrayList();
        ArrayList arriveTimeList = new ArrayList();
        ArrayList totalList = new ArrayList();
        ArrayList carclassList = new ArrayList();
        ArrayList fareList = new ArrayList();
        ArrayList stateList = new ArrayList();
        ArrayList orderTicketList = new ArrayList();
        String sTitle = "";

        if(searchType == false)
        {
            try
            {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                dataBaseHelper.openDataBase();
                Hashtable table = dataBaseHelper.getTrainList(startStationID,endStationID,queryDate,queryTime);

                List timeList = (List)table.get("timeList");
                for (int i = 0;i<timeList.size();i++)
                {
                    DataObj dataObj = (DataObj)timeList.get(i);
                    sTitle = dataObj.getsTitle();
                    int iTrain = dataObj.getTrain();
                    String startTime = dataObj.getStartTime();
                    String endTime = dataObj.getEndTime();
                    String carclassName = dataObj.getsCarClassName();
                    int fare = dataObj.getiFare();
                    int line = dataObj.getiLine();
                    String cripple = dataObj.getsCripple();
                    String breastFeed = dataObj.getsBreastFeed();
                    String bike = dataObj.getsBike();
                    String note = dataObj.getsNote();

                    trainList.add(iTrain);
                    carclassList.add(carclassName);
                    arriveTimeList.add(startTime.substring(0,5)+" > "+endTime.substring(0, 5));
                    totalList.add(new PCTool().dateDiff(startTime, endTime, "HH:mm:dd", ""));
                    fareList.add("$"+fare);
                    stateList.add(line+","+cripple+","+breastFeed+","+bike+","+note);
                    orderTicketList.add("N");
                }

                if(timeList.size()!=0)
                {
                    Intent intent = new Intent(MainActivity.this, TrainListActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("searchType","offLine");
                    bundle.putString("sTitle", sTitle);
                    bundle.putString("mStartStationID",startStationID);
                    bundle.putString("mEndStationID",endStationID);
                    bundle.putSerializable("trainList", trainList);
                    bundle.putSerializable("arriveTimeList", arriveTimeList);
                    bundle.putSerializable("totalList", totalList);
                    bundle.putSerializable("carclassList", carclassList);
                    bundle.putSerializable("fareList", fareList);
                    bundle.putSerializable("stateList", stateList);
                    bundle.putSerializable("orderTicketList", orderTicketList);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else //查詢不到
                {
                    Toast toast = Toast.makeText(MainActivity.this,"查無列車資訊!", Toast.LENGTH_LONG);

                    toast.show();
                }
            }
            catch (Exception e) {
                Log.d("ex=====", "Ex====", e);
            }
        }
        else
        {
            String url= "http://twtraffic.tra.gov.tw/twrail/SearchResult.aspx?searchtype=0&searchdate="+queryDate+"&fromcity=&tocity=&fromstation="+startStationID+"&tostation="+endStationID+"&trainclass=2&timetype=1&fromtime="+queryTime.replace(":","")+"&totime=2359&redir=1'";
//            Log.d("xxxxxXXXXXx",url);
            ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());

            TrainAsyncTask trainAsyncTask = new TrainAsyncTask(url,getApplicationContext(),progressDialog,MainActivity.this,startStationID,endStationID,queryDate);
            trainAsyncTask.execute();
        }
    }
    public void changeStation(String setStartStationName,String setStartStationID,String setEndStationName,String setEndStationID)
    {
        this.startStationName.setText(setStartStationName);
        this.endStationName.setText(setEndStationName);
        this.startStationID = setStartStationID;
        this.endStationID = setEndStationID;
    }
}
