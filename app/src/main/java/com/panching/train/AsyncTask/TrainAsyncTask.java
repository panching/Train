package com.panching.train.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by panching on 16/4/30.
 */
public class TrainAsyncTask extends AsyncTask<Void,Void,Void>{

    Boolean flag = false;
    String mUrl;
    String mStartStationID;
    String mEndStationID;
    String mDate;
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

    public TrainAsyncTask() {
    }

    public TrainAsyncTask(String mUrl, Context mContext, ProgressDialog mProgressDialog, Activity mActivity, String mStartStationID, String mEndStationID,String Date) {
        this.mUrl = mUrl;
        this.mStartStationID = mStartStationID;
        this.mEndStationID = mEndStationID;
        this.mContext = mContext;
        this.mProgressDialog = mProgressDialog;
        this.mActivity = mActivity;
        this.mDate = Date;
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
                    else if(j==9)
                    {
                        if(tds.get(j).getElementById("TicketingLink")!=null)sOrder = "Y";else sOrder ="N";
                        Element orderbt= (tds.get(j).getElementById("TicketingLink")!=null)?tds.get(j).getElementById("TicketingLink"):null;
                        if(orderbt!=null)
                        {
                            //from_station=100&to_station=185&getin_date=2016/05/11&train_no=145
                            String shref = orderbt.attr("href").substring(orderbt.attr("href").indexOf("?") + 1);
                            String fromStation = (shref.split("&")[0]).split("=")[1];
                            String toStation =  (shref.split("&")[1]).split("=")[1];
                            orderTicketStation = fromStation+","+toStation;
                        }
                        infoList.add(sOrder);
                    }
                    else
                        infoList.add(tds.get(j).text());
                }

                list.add(infoList);
            }

            for(int i = 0;i<list.size();i++)
            {
                trainInfoList = (List)list.get(i);
                String sCarClass = (String)trainInfoList.get(0);
                if(sCarClass.indexOf("自強")!= -1||sCarClass.indexOf("莒光")!= -1)sCarClass+="號";
                carclassList.add(sCarClass);
                trainList.add((String)trainInfoList.get(1));
                arriveTimeList.add((String)trainInfoList.get(4)+" > "+(String)trainInfoList.get(5));
                totalList.add((String)trainInfoList.get(6));
                fareList.add(((String)trainInfoList.get(8)));
                stateList.add((String) trainInfoList.get(2) + ","+(String) trainInfoList.get(7));
                orderTicketList.add((String) trainInfoList.get(9));
            }

            Element TitleLabel = document.getElementById("TitleLabel");
            Elements titles = TitleLabel.getElementsByTag("font");
            String startTemp = String.valueOf(titles.get(1));
            String endTemp = String.valueOf(titles.get(2));
            String startStation = startTemp.substring(startTemp.indexOf(">") + 4, startTemp.indexOf("/") - 2);
            String endStation = endTemp.substring(endTemp.indexOf(">")+4,endTemp.indexOf("/")-2);

            sTitle = startStation +" , "+endStation;

        } catch (Exception e) {
            mProgressDialog.dismiss();
            flag = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        if(flag)
        {
            Toast.makeText(mContext,"查無列車資訊！",Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(mContext,TrainListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Bundle bundle = new Bundle();

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
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                mProgressDialog.dismiss();

            }
        }
    }

    public String senGetDataToInternet(String url){
        String result ="";
            HttpClient client=null;
//        headerMap = new HashMap();
            try{
                client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response = client.execute(get);
//            Object Obj[] = response.getAllHeaders();
//
//            for (Object headerObj:Obj)
//            {
//                String sHeader = String.valueOf(headerObj);
//                headerMap.put(sHeader.split(":")[0],sHeader.split(":")[1]);
//            }
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
