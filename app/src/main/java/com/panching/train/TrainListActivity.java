package com.panching.train;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.panching.train.Adapter.ListContentAdapter;
import com.panching.train.AsyncTask.TimeAsyncTask;
import com.panching.train.obj.OrderInfoObj;
import com.panching.train.obj.TrainInfoObj;

import org.apache.http.Header;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class TrainListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList trainList = null;
    ArrayList arriveTimeList = null;
    ArrayList totalTimeList = null;
    ArrayList carClassList = null;
    ArrayList fareList = null;
    ArrayList stateList = null;
    ArrayList orderTicketList = null;
    String orderTicketStation = null;
    String sTitle = "";
    String searchType = "";
    String mStartStationName = "";
    String mEndStationName = "";
    String mStartStationID = "";
    String mEndStationID = "";
    String mDate ="";
    String onTimeFlag = "";
//    HashMap headerMap = null;
    Hashtable shareTime = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        findViews();

        trainList = (ArrayList)getIntent().getSerializableExtra("trainList");
        arriveTimeList =  (ArrayList)getIntent().getSerializableExtra("arriveTimeList");
        totalTimeList =  (ArrayList)getIntent().getSerializableExtra("totalList");
        carClassList =  (ArrayList)getIntent().getSerializableExtra("carclassList");
        fareList = (ArrayList)getIntent().getSerializableExtra("fareList");
        stateList = (ArrayList)getIntent().getSerializableExtra("stateList");
        orderTicketList = (ArrayList)getIntent().getSerializableExtra("orderTicketList");
        orderTicketStation = (String)getIntent().getSerializableExtra("orderTicketStation");
        sTitle = getIntent().getStringExtra("sTitle");
        searchType = getIntent().getStringExtra("searchType");

        mStartStationID =  getIntent().getStringExtra("mStartStationID");
        mEndStationID =  getIntent().getStringExtra("mEndStationID");
        mDate = getIntent().getStringExtra("mDate");

        onTimeFlag = getIntent().getStringExtra("onTimeFlag");

        mStartStationName = sTitle.split(",")[0].trim();
        mEndStationName = sTitle.split(",")[1].trim();
        if(mStartStationName.indexOf("臺")==0)mStartStationName = mStartStationName.replace("臺","台");
        if(mEndStationName.indexOf("臺")==0)mEndStationName = mEndStationName.replace("臺","台");

        if(mEndStationName.equals("上行"))
            toolbar.setTitle(mStartStationName + " (  北上  ) ");
        else if(mEndStationName.equals("下行"))
            toolbar.setTitle(mStartStationName + " (  南下  ) ");
        else
            toolbar.setTitle(mStartStationName + " > " + mEndStationName);

        OrderInfoObj orderInfoObj = new OrderInfoObj();
        if(orderTicketStation!=null)
        {
            orderInfoObj.setStartStationID(orderTicketStation.split(",")[0]);
            orderInfoObj.setEndStationID(orderTicketStation.split(",")[1]);
            orderInfoObj.setDate(mDate);
        }

        final ListContentAdapter listContentAdapter = new ListContentAdapter(this, getData(),orderInfoObj,onTimeFlag);
        listView.setAdapter(listContentAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(searchType.equals("offLine"))
                {
                    Intent intent = new Intent(TrainListActivity.this,TimeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("searchType","offLine");
                    bundle.putInt("train", (int) trainList.get(position));
                    bundle.putString("carClass", (String) carClassList.get(position));
                    bundle.putSerializable("stateList",(String)stateList.get(position));
                    bundle.putString("title",mStartStationName + "," + mEndStationName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else
                {
                    ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                    String iTrain = (String) trainList.get(position);
                    TrainInfoObj trainInfoObj = new TrainInfoObj();
                    trainInfoObj.setsCarClassName((String) carClassList.get(position));
                    trainInfoObj.setiTrain(Integer.parseInt((String) trainList.get(position)));
                    trainInfoObj.setsStartStation(mStartStationName);
                    trainInfoObj.setsEndSatation(mEndStationName);
                    trainInfoObj.setStationList((String) stateList.get(position));

                    String url = "http://twtraffic.tra.gov.tw/twrail/SearchResultContent.aspx?searchdate="+mDate+"&traincode="+iTrain+"&trainclass=%E8%87%AA%E5%BC%B7&mainviaroad=0&fromstation="+mStartStationID+"&tostation="+mEndStationID+"&language=%27%20-H%20%27'";

                    TimeAsyncTask timeAsyncTask = new TimeAsyncTask(url,getApplicationContext(),progressDialog,TrainListActivity.this,trainInfoObj);
                    timeAsyncTask.execute();
                }
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_reply_white_18dp);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainListActivity.this.finish();
            }
        });
    }
    public void findViews(){
        listView = (ListView)findViewById(R.id.lvTrainList);
    }

    // 初始化一个List
    private List<Map<String, Object>> getData() {
        List tvImgText = new ArrayList();
        List tvImgBackground = new ArrayList();
        List igLineList = new ArrayList();
        List igCrippLeist = new ArrayList();
        List igBreastFeedList = new ArrayList();
        List igBikeList = new ArrayList();
        List tvNoteList = new ArrayList();
        List igOrderTicketList = new ArrayList();

        // 新建一个集合类，用于存放多条数据
        ArrayList<Map<String, Object>> listTohashMap = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> map = null;
        for (int i = 0; i < trainList.size(); i++) {
            map = new HashMap<String, Object>();
            //from line+","+cripple+","+breastFeed+","+bike+","+note
            //to "tvLine","imgBreastFeed","imgCripple","imgBike"
            String str = (String)stateList.get(i);
            String sLine = str.split(",")[0];
            String sCripple = str.split(",")[1];
            String sBreastFeed = str.split(",")[2];
            String sBike = str.split(",")[3];
            String sNote = str.split(",")[4];
            //設定山線海線
            if(sLine.equals("1")||sLine.indexOf("山")!=-1)
            {
                igLineList.add("(山)");
            }
            else if (sLine.equals("2")||sLine.indexOf("海")!=-1)
            {
                igLineList.add("(海)");
            }
            else
            {
                igLineList.add("");
            }
            if(sCripple.equals("Y"))
                igCrippLeist.add(R.drawable.breastfeeding);
            else
                igCrippLeist.add("N");

            if(sBike.equals("Y"))
                igBikeList.add(R.drawable.bike);
            else
                igBikeList.add("N");

            if(sBreastFeed.equals("Y"))
                igBreastFeedList.add(R.drawable.handicapped);
            else
                igBreastFeedList.add("N");

            if(sNote.indexOf("每日行駛。")!=-1)
                tvNoteList.add(R.drawable.everyday);
            else
                tvNoteList.add("N");

            if(orderTicketList.get(i).equals("Y"))
            {
                igOrderTicketList.add(R.drawable.order_ticket_50);
            }
            else
            {
                igOrderTicketList.add("N");
            }
            map.put("tvLine", igLineList.get(i));
            map.put("imgBreastFeed", igCrippLeist.get(i));
            map.put("imgCripple", igBreastFeedList.get(i));
            map.put("imgBike", igBikeList.get(i));
            map.put("imgNote",tvNoteList.get(i));
            map.put("tvTrain", trainList.get(i));
            map.put("tvArriveTime", arriveTimeList.get(i));
            map.put("tvTotalTime", totalTimeList.get(i));
            map.put("tvCarclass", carClassList.get(i));
            map.put("tvMoney", fareList.get(i));
            map.put("imgOrderTicket", igOrderTicketList.get(i));

            //設定ＩＣＯＮ＆ＢＡＣＫＧＲＯＵＮＤ
            if(((String)carClassList.get(i)).indexOf("區間") != -1)
            {
                tvImgText.add("區");
                tvImgBackground.add(R.drawable.blue_oval);
            }
            else if(((String)carClassList.get(i)).indexOf("復興") != -1)
            {
                tvImgText.add("復");
                tvImgBackground.add(R.drawable.blue_oval);
            }
            else if(((String)carClassList.get(i)).indexOf("莒光") != -1)
            {
                tvImgText.add("莒");
                tvImgBackground.add(R.drawable.orange_oval);
            }
            else if(((String)carClassList.get(i)).indexOf("自強") != -1)
            {
                tvImgText.add("自");
                tvImgBackground.add(R.drawable.deep_orange_oval);
            }
            else if(((String)carClassList.get(i)).indexOf("太魯閣") != -1)
            {
                tvImgText.add("太");
                tvImgBackground.add(R.drawable.red_oval);
            }
            else
            {
                tvImgText.add("普");
                tvImgBackground.add(R.drawable.red_oval);
            }
            map.put("tvImgText",tvImgText.get(i));
            map.put("tvImgBackground",tvImgBackground.get(i));
            listTohashMap.add(map);
        }
        return listTohashMap;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.train_list_menu, menu);
        if(onTimeFlag!=null)
            menu.removeItem(R.id.star);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //                deleteNote(info.id);
        shareTime =  ListContentAdapter.shareTime;
        switch (item.getItemId()) {
            case R.id.share:
            {
                if(shareTime.size()<=0)
                {
                    Toast toast = Toast.makeText(TrainListActivity.this,"請點選車種圖示增加分享項目！",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    StringBuilder shareToStr = new StringBuilder();
                    int count = 0;
                    for (Object key : shareTime.keySet())
                    {
                        if(count != 0)shareToStr.append("\n");

                        String val =(String)shareTime.get(key);
                        shareToStr.append(val.replace(",","  "));

                        count++;
                    }
                    String Result = shareToStr.toString();
                    if(onTimeFlag!=null)
                        Result = Result.replace("元","").replace("開車時間","").replace("到達時間","");
                    shareTo("時刻分享", Result, "請選擇應用程式");
                }
                return true;
            }
            case R.id.star:
            {
                Toast toast = null;
                SharedPreferences pre = getSharedPreferences("starRoute",MODE_APPEND);
                SharedPreferences.Editor preEdit = pre.edit();
                if(pre.getAll().size()!=0)
                {
                    Boolean flag = false;
                    for (int i =0 ;i<pre.getAll().size();i++)
                    {
                        String sRoute = pre.getString(String.valueOf(i),"");
                        if(sRoute.equals(mStartStationName+":"+mStartStationID + "," + mEndStationName +":"+mEndStationID))
                        {
                            toast= Toast.makeText(TrainListActivity.this, "常用路線已存在！", Toast.LENGTH_LONG);
                            flag = true;
                            break;
                        }
                    }
                    if(flag!=true)
                    {
                        preEdit.putString(String.valueOf(pre.getAll().size()), mStartStationName+":"+mStartStationID + "," + mEndStationName +":"+mEndStationID);
                        toast = Toast.makeText(TrainListActivity.this, "已新增至常用路線！", Toast.LENGTH_LONG);
                        preEdit.commit();
                    }
                }
                else
                {
                    preEdit.putString("0", mStartStationName+":"+mStartStationID + "," + mEndStationName +":"+mEndStationID);
                    toast= Toast.makeText(TrainListActivity.this, "已新增至常用路線！", Toast.LENGTH_LONG);
                    preEdit.commit();
                }

                toast.show();
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }
    /*
    *   subject：內容的標題。我實測的結果，有些 App 會忽略這個項目，所以建議不要放太重要的訊息。
    *   body：內容。想要分享的內容，可以放網址，有些 App 會自動轉成連結或直接下載縮圖。
    *   chooserTitle：選擇器的標題。當使用者的裝置中只有一個 App 能接收這個意圖時，就不會出現選擇器，而是直接打開該 App。
    * */
    private void shareTo(String subject, String body, String chooserTitle) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);

        startActivity(Intent.createChooser(sharingIntent, chooserTitle));
    }

}
