package com.panching.train.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.panching.train.RouteDirectionTabLayoutActivity;
import com.panching.train.TrainListActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by panching on 16/5/18.
 */
public class OnTimeAsyncTack extends AsyncTask<Void,Void,Void> {

    Boolean flag = false;
    String mUrl;
    String mStartStationID;
    String mEndStationID;
    String mDate;
    String mTime;
    Context mContext ;
    ProgressDialog mProgressDialog;
    Activity mActivity;

    String sTitle;
    List list = new ArrayList();
    List trainInfoList = new ArrayList();
    ArrayList trainList = new ArrayList();
    ArrayList arriveTimeList = new ArrayList();
    ArrayList totalList = new ArrayList();
    ArrayList carclassList = new ArrayList();
    ArrayList fareList = new ArrayList();
    ArrayList stateList = new ArrayList();
    ArrayList orderTicketList = new ArrayList();
    String orderTicketStation ;

    public OnTimeAsyncTack() {
        super();
    }
    public OnTimeAsyncTack(String mUrl, Context mContext, ProgressDialog mProgressDialog, Activity mActivity, String mStartStationID, String mEndStationID,String Date,String Time)
    {
        this.mUrl = mUrl;
        this.mStartStationID = mStartStationID;
        this.mEndStationID = mEndStationID;
        this.mContext = mContext;
        this.mProgressDialog = mProgressDialog;
        this.mActivity = mActivity;
        this.mDate = Date;
        this.mTime = Time;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setTitle("請稍候！");
        mProgressDialog.setMessage("資料讀取中..");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try
        {
            List infoList = null;
            List state = null;
            String sState = "";
            String sOrder = "";
            String html = senGetDataToInternet(mUrl);
            // Connect to the web site
            Document document = Jsoup.parse(html);


            Element TitleLabel = document.getElementById("TitleLabel");
            Elements titles = TitleLabel.getElementsByTag("font");
            String startTemp = String.valueOf(titles.get(1));
            String endTemp = String.valueOf(titles.get(2));
            String startStation = startTemp.substring(startTemp.indexOf(">") + 4, startTemp.indexOf("/") - 2);
            String endStation = endTemp.substring(endTemp.indexOf(">") + 4, endTemp.indexOf("/") - 2);

            sTitle = startStation +" , "+endStation;
            // Using Elements to get the Meta data
            Element table = document.getElementById("ResultGridView");
            Elements trs = table.getElementsByTag("tr");
            for (int i = 1; i < trs.size(); i++)
            {
                infoList = new ArrayList();
                state = new ArrayList();
                Elements tds = trs.get(i).getElementsByTag("td");
                for(int j = 0;j < tds.size();j++)
                {
                    sState = "";
                    if(j==7)
                    {
//                        stateList.add(line+","+cripple+","+breastFeed+","+bike+","+note);
                        if(tds.get(j).getElementById("handicapped")!=null)sState += "Y,";else sState+="N,";
                        if(tds.get(j).getElementById("breastfeeding")!=null)sState += "Y,";else sState+="N,";
                        if(tds.get(j).getElementById("bike")!=null)sState += "Y,";else sState+="N,";
                        if(tds.get(j).getElementById("everyday")!=null)sState += "每日行駛。";else sState+="N";
                        infoList.add(sState);
                    }
                    else
                        infoList.add(tds.get(j).text());
                }

                Date d1 = new Date("2016/01/01 "+tds.get(4).text()+":00");
                Date d2 = new Date("2016/01/01 "+mTime+":00");
                if(d1.after(d2))
                    list.add(infoList);
            }

            for(int i = 0;i<list.size();i++)
            {
                trainInfoList = (List)list.get(i);
                String sCarClass = (String)trainInfoList.get(0);
                String startTime = (String)trainInfoList.get(4);
                String endTime = (String)trainInfoList.get(5);
                if(sCarClass.indexOf("自強")!= -1||sCarClass.indexOf("莒光")!= -1)sCarClass+="號";
                carclassList.add(sCarClass);
                trainList.add((String)trainInfoList.get(1));
                if(endTime.equals("---"))
                    arriveTimeList.add(startTime+" 到 "+startStation);
                else
                    arriveTimeList.add(startTime+" > "+endTime);
                totalList.add("到達時間   開車時間");
                fareList.add("");
                stateList.add((String) trainInfoList.get(2) + ","+(String) trainInfoList.get(7));
                orderTicketList.add("");
            }

        } catch (Exception e) {
            mProgressDialog.dismiss();
            flag = true;
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result)
    {

        if(flag)
        {
            Toast.makeText(mContext, "查無列車資訊！", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(mContext,TrainListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Bundle bundle = new Bundle();
            try
            {
                if(trainList.size()>0)
                {
                    bundle.putString("mDate",mDate);
                    bundle.putString("mStartStationID",mStartStationID);
                    bundle.putString("mEndStationID",mEndStationID);
                    bundle.putString("searchType","online");
                    bundle.putString("sTitle", sTitle);
                    bundle.putSerializable("trainList", trainList);
                    bundle.putSerializable("arriveTimeList", arriveTimeList);
                    bundle.putSerializable("totalList", totalList);
                    bundle.putSerializable("carclassList", carclassList);
                    bundle.putSerializable("fareList", fareList);
                    bundle.putSerializable("stateList", stateList);
                    bundle.putSerializable("orderTicketList",orderTicketList);
                    bundle.putString("orderTicketStation", orderTicketStation);
                    bundle.putString("onTimeFlag", "Y");
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    mProgressDialog.dismiss();
                }
            }
            catch (Exception e)
            {
                //////???
                e.printStackTrace();
            }

        }
    }

    public String senGetDataToInternet(String url){
        String result ="";
        HttpClient client=null;
        try{
            client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                result = EntityUtils.toString(resEntity);
            }
        }catch (Exception e){
            Log.d("Tag", "xxxxxxxxxx", e);
        }
        finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }
}
